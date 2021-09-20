package com.jonathan.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.jonathan.myapplication.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var cameraPath: String = ""
    private var absoluteCameraPath: String = ""
    private var fileCamera: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.buttonOpenCamera.setOnClickListener {
            openCamera(1)
        }
    }

    private fun openCamera(requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var cameraFile: File? = null
            try {
                cameraFile = createPhotoFile(requestCode)

            } catch (e: Exception) {
                Toast.makeText(this, "Hubo un error en el archivo" + e.message, Toast.LENGTH_LONG).show()

            }
            if (cameraFile != null) {
                val photoUri = FileProvider.getUriForFile(this,"com.jonathan.myapplication", cameraFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, requestCode)
            }
        }
    }

    private fun createPhotoFile(requestCode: Int): File? {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val cameraFile = File.createTempFile(
            Date().toString() + "_photo",
            ".jpg",
            storageDir
        )
        if (requestCode == 1) {
            cameraPath = "file:" + cameraFile.absoluteFile
            absoluteCameraPath = cameraFile.absolutePath
        }
        return cameraFile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            fileCamera = File(absoluteCameraPath)
            Picasso.get().load(cameraPath).into(binding.imageViewCamera)
        }
    }
}