package com.example.androidadvancedlab1.fragments

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.androidadvancedlab1.R

class InstagramFragment : Fragment(R.layout.fragment_instagram) {

    lateinit var image: ImageView
    lateinit var imageURI: Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectBtn = view.findViewById<Button>(R.id.select)
        val sendBtn = view.findViewById<Button>(R.id.send)
        image = view.findViewById<ImageView>(R.id.imageView)
        selectBtn.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200)
        }
        sendBtn.setOnClickListener {
            if (::imageURI.isInitialized) {
                val intent = Intent("com.instagram.share.ADD_TO_STORY")
                val sourceApplication = "1234"
                intent.putExtra("source_application", sourceApplication)
                intent.setDataAndType(imageURI, "image/*")
                intent.putExtra("interactive_asset_uri", imageURI)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val resolveInfo = requireContext().packageManager.queryIntentActivities(intent, 0)
                if (resolveInfo.isNotEmpty()) {
                    try {
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            requireContext(),
                            "Instagram not installed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Instagram app not found.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "You did not select an image.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                val selectedImg = data?.data
                if (selectedImg != null) {
                    image.setImageURI(selectedImg)
                    imageURI = selectedImg
                }
            }
        }
    }
}