package com.example.recycleme.view.ui.scan

import com.example.recycleme.view.ui.result.ResultActivity
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.recycleme.data.getImageUri
import com.example.recycleme.data.remote.response.PredictionResponse
import com.example.recycleme.data.remote.retrofit.ApiConfig
import com.example.recycleme.databinding.ActivityScanBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null
    private val apiService = ApiConfig.getApiService()

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            setupListeners()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(REQUIRED_PERMISSION), REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun setupListeners() {
        binding.addButtonkamera.setOnClickListener {
            openCamera()
        }

        binding.addButtongallery.setOnClickListener {
            openGallery()
        }

        binding.buttonAdd.setOnClickListener {
            uploadImage()
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun openCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri = result.data?.data as Uri
            binding.previewImageView.setImageURI(selectedImage)
        }
    }

    private fun uploadImage() {
        val bitmap = binding.previewImageView.drawable.toBitmap()
        val file = convertBitmapToFile(bitmap)

        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("Image", file.name, requestFile)

        binding.addPb.visibility = View.VISIBLE

        apiService.uploadImage(body).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                binding.addPb.visibility = View.GONE
                if (response.isSuccessful) {
                    val prediction = response.body()
                    if (prediction != null) {
                        showResult(prediction)
                    }
                } else {
                    Toast.makeText(this@ScanActivity, "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                binding.addPb.visibility = View.GONE
                Toast.makeText(this@ScanActivity, "Upload failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun convertBitmapToFile(bitmap: Bitmap): File {
        val file = File(cacheDir, "image.jpg")
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        file.writeBytes(outputStream.toByteArray())
        return file
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showResult(prediction: PredictionResponse) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("result", prediction)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setupListeners()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

