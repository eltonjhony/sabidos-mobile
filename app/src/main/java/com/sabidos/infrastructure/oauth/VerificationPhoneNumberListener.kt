package com.sabidos.infrastructure.oauth

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.io.Closeable

class VerificationPhoneNumberListener(private var delegate: PhoneAuthProvider.OnVerificationStateChangedCallbacks?) :
    PhoneAuthProvider.OnVerificationStateChangedCallbacks(), Closeable {

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        // This callback will be invoked in two situations:
        // 1 - Instant verification. In some cases the phone number can be instantly
        //     verified without needing to send or enter a verification code.
        // 2 - Auto-retrieval. On some devices Google Play services can automatically
        //     detect the incoming verification SMS and perform verification without
        //     user action.
    }

    override fun onVerificationFailed(e: FirebaseException) {
        delegate?.onVerificationFailed(e)
    }

    override fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        delegate?.onCodeSent(verificationId, token)
    }

    override fun close() {
        delegate = null
    }

}