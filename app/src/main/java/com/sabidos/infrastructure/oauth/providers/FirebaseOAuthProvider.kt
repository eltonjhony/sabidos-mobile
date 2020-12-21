package com.sabidos.infrastructure.oauth.providers

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sabidos.data.remote.model.AuthErrorResponse
import com.sabidos.data.remote.model.AuthErrorResponse.Companion.ERROR_CREDENTIAL_ALREADY_IN_USE
import com.sabidos.data.remote.model.AuthErrorResponse.Companion.ERROR_EMAIL_ALREADY_IN_USE
import com.sabidos.domain.User
import com.sabidos.infrastructure.Constants
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.*
import com.sabidos.infrastructure.helpers.SignInPrefsHelper
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.infrastructure.oauth.OAuthProvider
import com.sabidos.infrastructure.oauth.VerificationPhoneNumberListener
import java.io.Closeable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class FirebaseOAuthProvider(private val signInPrefsHelper: SignInPrefsHelper) :
    OAuthProvider {

    private val disposeBag = mutableListOf<Closeable>()

    private val auth: FirebaseAuth by lazy { Firebase.auth }

    init {
        auth.useAppLanguage()
    }

    override fun isUserLogged(): ResultWrapper<Boolean> = runCatching {
        Success(auth.currentUser != null)
    }.getOrThrow()

    override suspend fun getCurrentUser(): ResultWrapper<User> = runCatching {

        convertOAuthUserToUser(auth.currentUser)?.let {
            Success(it)
        } ?: DataNotFoundError

    }.getOrThrow()

    override fun getAuthToken(
        forceRefresh: Boolean,
        callback: ((ResultWrapper<String?>) -> Unit)
    ) {

        runCatching {

            when {
                auth.currentUser != null -> {

                    auth.currentUser!!.getIdToken(forceRefresh)
                        .addOnSuccessListener {
                            callback(Success(it.token))
                        }.addOnCanceledListener {
                            callback(GenericError())
                        }.addOnFailureListener {
                            callback(handleFirebaseException(it))
                        }

                }
                else -> callback(DataNotFoundError)
            }

        }.onFailure {
            callback(GenericError(Error(it)))
        }
    }

    override fun signInAnonymously(callback: ((ResultWrapper<User>) -> Unit)) {
        runCatching {

            auth.signInAnonymously()
                .addOnSuccessListener { result ->

                    convertOAuthUserToUser(result.user)?.let {
                        callback(Success(it))
                    } ?: DataNotFoundError

                }.addOnCanceledListener {
                    callback(GenericError())
                }.addOnFailureListener {
                    callback(handleFirebaseException(it))
                }

        }.getOrThrow()
    }

    override fun signInWithEmailLink(email: String, callback: ((ResultWrapper<Boolean>) -> Unit)) {
        runCatching {

            val actionSettings = ActionCodeSettings
                .newBuilder()
                .setHandleCodeInApp(true)
                .setUrl(Constants.Configs.SABIDOS_MAGIC_LINK_URL)
                .setAndroidPackageName(
                    Constants.Configs.APPLICATION_ID,
                    true,
                    Constants.Configs.SDK_MIN_VERSION
                )
                .build()

            auth.sendSignInLinkToEmail(email, actionSettings)
                .addOnSuccessListener {
                    signInPrefsHelper.putEmail(email)
                    callback(Success(true))
                }.addOnCanceledListener {
                    callback(GenericError())
                }.addOnFailureListener {
                    callback(handleFirebaseException(it))
                }

        }.getOrThrow()

    }

    override fun isSignInWithEmailLink(emailLink: String): ResultWrapper<Boolean> = runCatching {
        Success(auth.isSignInWithEmailLink(emailLink))
    }.getOrThrow()

    override fun completeSignWithEmailLink(
        emailLink: String,
        callback: ((ResultWrapper<User>) -> Unit)
    ) {
        runCatching {

            signInPrefsHelper.getEmail()?.let { email ->

                if (auth.currentUser?.isAnonymous == true) {

                    val credential = EmailAuthProvider.getCredentialWithLink(email, emailLink)
                    linkCurrentUserWith(credential, callback)

                } else {

                    auth.signInWithEmailLink(email, emailLink)
                        .addOnSuccessListener { result ->

                            convertOAuthUserToUser(result.user)?.let {
                                callback(Success(it))
                            } ?: DataNotFoundError

                        }.addOnCanceledListener {
                            callback(GenericError())
                        }.addOnFailureListener {
                            callback(handleFirebaseException(it))
                        }
                }

            } ?: callback(DataNotFoundError)

        }.getOrThrow()
    }

    override fun verifyPhoneNumber(
        phoneNumber: String,
        callback: ((ResultWrapper<Boolean>) -> Unit)
    ) = runCatching {

        val listener = VerificationPhoneNumberListener(object :
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationFailed(e: FirebaseException) {
                callback(handleFirebaseException(e))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                signInPrefsHelper.putVerificationId(verificationId)
                signInPrefsHelper.putPhoneNumber(phoneNumber)
                callback(Success(true))
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
            }

        })

        disposeBag.add(listener)

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            Executors.newSingleThreadExecutor(),
            listener
        )

    }.getOrThrow()

    override fun signInWithPhoneNumber(code: String, callback: ((ResultWrapper<User>) -> Unit)) {
        val verificationId = signInPrefsHelper.getVerificationId()

        verificationId?.let { id ->

            val credential = PhoneAuthProvider.getCredential(id, code)

            if (auth.currentUser?.isAnonymous == true) {
                linkCurrentUserWith(credential) {
                    when (it) {
                        is Success -> callback(Success(it.data))
                        is AuthError -> callback(it)
                        else -> callback(GenericError())
                    }
                }
            } else {
                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        convertOAuthUserToUser(it.user, false)?.let { user ->
                            callback(Success(user))
                        } ?: DataNotFoundError
                    }.addOnCanceledListener {
                        callback(GenericError())
                    }.addOnFailureListener {
                        callback(handleFirebaseException(it))
                    }
            }

        } ?: callback(DataNotFoundError)
    }

    override fun clear() {
        runCatching {
            disposeBag.forEach {
                it.close()
            }
            disposeBag.clear()
        }.onFailure { Logger.withTag(FirebaseOAuthProvider::class.java.simpleName).withCause(it) }
    }

    override suspend fun logout(): ResultWrapper<Boolean> = runCatching {
        auth.signOut()
        Success(true)
    }.getOrThrow()

    private fun linkCurrentUserWith(
        credential: AuthCredential,
        callback: ((ResultWrapper<User>) -> Unit)
    ) {

        auth.currentUser!!.linkWithCredential(credential)
            .addOnSuccessListener { result ->

                convertOAuthUserToUser(result.user, true)?.let {
                    callback(Success(it))
                } ?: DataNotFoundError

            }.addOnCanceledListener {
                callback(GenericError())
            }.addOnFailureListener {
                callback(handleFirebaseException(it))
            }

    }

    private fun convertOAuthUserToUser(fbUser: FirebaseUser?, isLinked: Boolean = false): User? {
        return fbUser?.let {
            User(
                it.uid,
                it.isAnonymous,
                it.phoneNumber,
                it.email,
                isLinked
            )
        }
    }

    private fun handleFirebaseException(e: Exception): ResultWrapper<Nothing> =
        runCatching {
            when (e) {
                is FirebaseNetworkException -> NetworkError
                is FirebaseAuthException -> {
                    if (ERROR_CREDENTIAL_ALREADY_IN_USE == e.errorCode || ERROR_EMAIL_ALREADY_IN_USE == e.errorCode) {
                        AuthError(AuthErrorResponse(e.errorCode, e.message))
                    } else {
                        GenericError(Error(e))
                    }
                }
                else -> GenericError(Error(e))
            }
        }.getOrThrow()

}