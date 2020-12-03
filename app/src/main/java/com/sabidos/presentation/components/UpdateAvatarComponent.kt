package com.sabidos.presentation.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sabidos.R
import com.sabidos.infrastructure.extensions.compressImageFile
import com.sabidos.infrastructure.extensions.dpToPx
import com.sabidos.infrastructure.extensions.getOrientationFrom
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.logging.Logger
import kotlinx.android.synthetic.main.sabidos_update_avatar_component.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpdateAvatarComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_update_avatar_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var fragment: Fragment? = null

    override fun setupComponent() {
        super.setupComponent()
        hide()
    }

    fun setup(fragment: Fragment, padding: Float = 6f) {
        this.fragment = fragment
        cameraIconView.setPadding(
            context.dpToPx(padding),
            context.dpToPx(padding),
            context.dpToPx(padding),
            context.dpToPx(padding)
        )
        setOnClickListener {
            openGallery()
        }
    }

    fun onDestroy() {
        fragment = null
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GALLERY_PERMISSION_CODE -> {
                if (!userDoesNotAcceptRequiredPermissions(grantResults)) {
                    openGallery()
                }
            }
        }
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        callback: (String?, Int) -> Unit
    ) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                if (uri.path != null) {
                    GlobalScope.launch(Dispatchers.Main) {
                        callback(
                            context.compressImageFile(uri.path!!, false, uri),
                            context.getOrientationFrom(uri)
                        )
                    }
                }
            }
        }
    }

    private fun openGallery() {
        requestOnDemandPermissions {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = GALLERY_IMAGE_TYPE
            fragment?.startActivityForResult(
                intent, GALLERY_REQUEST_CODE
            )
        }
    }

    private fun userDoesNotAcceptRequiredPermissions(grantResults: IntArray): Boolean {
        var notAllGranted = false
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                notAllGranted = true
            }
        }
        return grantResults.isEmpty() || notAllGranted
    }

    private fun requestOnDemandPermissions(callback: () -> Unit) {
        runCatching {

            val pendingPermissions: ArrayList<String> = ArrayList()
            permissions.forEachIndexed { _, permission ->
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission
                    ) == PackageManager.PERMISSION_DENIED
                )
                    pendingPermissions.add(permission)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val array = arrayOfNulls<String>(pendingPermissions.size)
                pendingPermissions.toArray(array)

                if (pendingPermissions.isNotEmpty()) {
                    fragment?.requestPermissions(array, GALLERY_PERMISSION_CODE)
                } else {
                    callback()
                }
            }

        }.onFailure {
            Logger.withTag(UpdateAvatarComponent::class.java.simpleName).withCause(it)
            return
        }
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 4575
        const val GALLERY_PERMISSION_CODE = 545

        const val GALLERY_IMAGE_TYPE = "image/*"
    }

}