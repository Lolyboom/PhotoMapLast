package com.hfad.myapplication.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import androidx.fragment.app.Fragment
import com.hfad.photomaplast.MainActivity
import com.hfad.photomaplast.R
import com.hfad.photomaplast.databinding.FragmentGalleryBinding


class GalleryFragment:Fragment( ), View.OnClickListener{

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?):
            View {
        val binding = FragmentGalleryBinding.inflate(inflater)
        return binding.root
    }

    override fun onClick(v: View?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addPictureButton = view.findViewById<Button>(R.id.addPictureButton)
        val openGalleryButton = view.findViewById<Button>(R.id.openGalleryButton)
        val galleryImageView = view.findViewById<ImageView>(R.id.galleryImageView)
        val pictureEditText = view.findViewById<EditText>(R.id.pictureEditText)

        openGalleryButton.setOnClickListener {
            imagePick()
        }
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if (result!!.resultCode == Activity.RESULT_OK) {
                val data = result.data
                galleryImageView.setImageURI(data?.data)
            }
        }
        addPictureButton.setOnClickListener {
            if(TextUtils.isEmpty(pictureEditText.text.toString())) {
                pictureEditText.error = "Please enter description"
            }else {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                makeMark()
                    // добавление маркера на карту
            }

        }

    }

    private fun makeMark() {

    }

    private fun imagePick() {
        val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        imagePickerIntent.type = "image/*"
        activityResultLauncher.launch(imagePickerIntent)
    }


}