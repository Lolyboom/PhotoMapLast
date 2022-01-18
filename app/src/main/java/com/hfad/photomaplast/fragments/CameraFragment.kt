package com.hfad.photomaplast.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.hfad.photomaplast.MainActivity
import com.hfad.photomaplast.R
import com.hfad.photomaplast.databinding.FragmentCameraBinding

class CameraFragment:Fragment(), View.OnClickListener {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?):
            View {
        val binding = FragmentCameraBinding.inflate(inflater)
        return binding.root
    }

    override fun onClick(v: View?) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addPhotoButton = view.findViewById<Button>(R.id.AddPhotoButton)
        val cameraImageView = view.findViewById<ImageView>(R.id.cameraImageView)
        val photoEditText = view.findViewById<EditText>(R.id.photoEditText)

        cameraImageView.setOnClickListener {
            cameraDialog()
        }
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if (result!!.resultCode == Activity.RESULT_OK) {
                cameraImageView.setImageBitmap(null)
                val data = result.data
                val image = data?.extras?.get("data") as Bitmap
                cameraImageView.setImageBitmap(image)
            }
        }
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if (result!!.resultCode == Activity.RESULT_OK) {
                cameraImageView.setImageURI(null)
                val data = result.data
                cameraImageView.setImageURI(data?.data)
            }
        }

        addPhotoButton.setOnClickListener  {
            if(TextUtils.isEmpty(photoEditText.text.toString())) {
                photoEditText.error = "Please enter description"
            }else {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                    // добавление маркера на карту
            }
        }
    }

    private fun cameraDialog() {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle("Photo Map")
        builder.setMessage("Take your choice?")
        builder.setNeutralButton("Back"){dialog, i ->
        }
        builder.setNegativeButton("Camera"){dialog, i ->
            openCamera()
        }
        builder.setPositiveButton("Gallery"){dialog, i ->
            imagePick()
        }
        builder.show()
    }

    private fun openCamera() {
        val doPhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activityResultLauncher.launch(doPhotoIntent)
    }

    private fun imagePick() {
        val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        imagePickerIntent.type = "image/*"
        activityResultLauncher.launch(imagePickerIntent)
    }
}