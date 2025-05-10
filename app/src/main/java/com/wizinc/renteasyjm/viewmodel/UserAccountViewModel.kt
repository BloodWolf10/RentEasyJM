package com.wizinc.renteasyjm.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wizinc.renteasyjm.RentEasyApplication
import com.wizinc.renteasyjm.data.User
import com.wizinc.renteasyjm.util.RegisterValidation
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.util.validateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    application: Application
) : AndroidViewModel(application) {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateInfo = _updateInfo.asStateFlow()

    private val cloudinary by lazy {
        Cloudinary(
            ObjectUtils.asMap(
                "cloud_name", "dm1ea8wgn",
                "api_key", "742252237745278",
                "api_secret", "gqKYUS8GpUoh8SeklULC9v_-jw8"
            )
        )
    }

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            _user.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!)
            .get().addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch {
                        _user.emit(Resource.Success(it))
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun updateUserInfo(user: User, imageUri: Uri?) {
        val areInputsValid = validateEmail(user.email) is RegisterValidation.Success &&
                user.firstName.trim().isNotEmpty() &&
                user.lastName.trim().isNotEmpty()

        if (!areInputsValid) {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Error("Check your inputs"))
            }
            return
        }

        viewModelScope.launch {
            _updateInfo.emit(Resource.Loading())
        }

        if (imageUri == null) {
            saveUserInformation(user, true)
        } else {
            saveUserInformationWithNewImage(user, imageUri)
        }
    }

    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val context = getApplication<RentEasyApplication>()
                val contentResolver = context.contentResolver

                // Decode the image from URI
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                } else {
                    val source = ImageDecoder.createSource(contentResolver, imageUri)
                    ImageDecoder.decodeBitmap(source)
                }

                // Save bitmap to a temporary file
                val tempFile = kotlin.io.path.createTempFile(suffix = ".jpg").toFile()
                tempFile.outputStream().use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 96, out)
                }

                // Upload the file to Cloudinary
                val uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                    "resource_type", "image"
                ))

                // Delete the temp file
                tempFile.delete()

                // Get the image URL from Cloudinary response
                val imageUrl = uploadResult["secure_url"] as? String
                if (imageUrl.isNullOrEmpty()) {
                    _updateInfo.emit(Resource.Error("Image URL not returned from Cloudinary"))
                    return@launch
                }

                // Save updated user info with new image URL
                saveUserInformation(user.copy(imagePath = imageUrl), false)

            } catch (e: Exception) {
                e.printStackTrace()
                _updateInfo.emit(Resource.Error("Cloudinary upload failed: ${e.message}"))
            }
        }
    }

    private fun saveUserInformation(user: User, shouldRetrieveOldImage: Boolean) {
        firestore.runTransaction { transaction ->
            val documentRef = firestore.collection("user").document(auth.uid!!)

            if (shouldRetrieveOldImage) {
                val currentUser = transaction.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
                transaction.set(documentRef, newUser)
            } else {
                transaction.set(documentRef, user)
            }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Success(user))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}
