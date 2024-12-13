package com.dicoding.nutrimate.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.nutrimate.api.ApiConfig
import com.dicoding.nutrimate.databinding.ActivityUploadBinding
import com.dicoding.nutrimate.upload.CameraActivity.Companion.CAMERAX_RESULT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var upViewModel: UploadModel
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        upViewModel = ViewModelProvider(this).get(UploadModel::class.java)
        upViewModel.imageUri.observe(this, Observer { uri ->
            uri?.let {
                binding.previewImageView.setImageURI(it)
            }
        })

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener { uploadImage() }
        if (savedInstanceState != null) {
            currentImageUri = savedInstanceState.getParcelable("imageUri")
            currentImageUri?.let {
                binding.previewImageView.setImageURI(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("imageUri", currentImageUri)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            upViewModel.setImageUri(it)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        if (currentImageUri != null) {
            binding.uploadProgressBar.visibility = View.VISIBLE

            val inputStream = contentResolver.openInputStream(currentImageUri!!)

            if (inputStream != null) {
                val file = createTempFileFromStream(inputStream)
                val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("image", file.name, requestBody)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ApiConfig.api.uploadImage(part)

                        if (response.isSuccessful) {
                            val modelResponse = response.body()
                            val meals = modelResponse?.data?.meals
                            withContext(Dispatchers.Main) {
                                binding.uploadProgressBar.visibility = View.GONE

                                val intent = Intent(this@UploadActivity, HasilActivity::class.java)
                                intent.putExtra("result", modelResponse?.data?.result)
                                intent.putExtra("gambar", currentImageUri.toString())
                                intent.putParcelableArrayListExtra("meals", ArrayList(meals))
                                startActivity(intent)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                binding.uploadProgressBar.visibility = View.GONE
                                Toast.makeText(this@UploadActivity, "Failed to upload image", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.uploadProgressBar.visibility = View.GONE
                            Toast.makeText(this@UploadActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                binding.uploadProgressBar.visibility = View.GONE
                Toast.makeText(this, "Unable to read the image file", Toast.LENGTH_LONG).show()
            }
        } else {
            binding.uploadProgressBar.visibility = View.GONE
            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show()
        }
    }



    private fun createTempFileFromStream(inputStream: InputStream): File {
        val tempFile = File.createTempFile("uploaded_image", ".jpg", cacheDir)
        val outputStream = FileOutputStream(tempFile)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }


    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}