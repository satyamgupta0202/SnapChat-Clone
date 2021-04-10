package com.example.snapchatclone

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*

class CreateSnapsActivity2 : AppCompatActivity() {
    var createSnapImageview: ImageView? = null
    var messageEditText: EditText? = null
    val ImageName = UUID.randomUUID().toString()+".jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snaps2)
        createSnapImageview = findViewById(R.id.imageselect)
       // messageEditText = findViewById(R.id.message)
    }

    fun getphoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    fun uploadImage(view: View) {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            getphoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedImage = data?.data
        if(requestCode==1 && resultCode== Activity.RESULT_OK && data!=null){
            try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                createSnapImageview?.setImageBitmap(bitmap)

            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getphoto()
            }
        }
    }
    fun nextclicked(view: View){

        // Get the data from an ImageView as bytes
        // Get the data from an ImageView as bytes
        createSnapImageview?.setDrawingCacheEnabled(true)
        createSnapImageview?.buildDrawingCache()
        val bitmap = (createSnapImageview?.getDrawable() as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()


        val uploadTask: UploadTask = FirebaseStorage.getInstance().getReference().child("images").child(ImageName).putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(this,"upload Failed",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc
            // ..
            val intent = Intent(this,Choosesender::class.java)
            startActivity(intent)
        }
    }
}

        