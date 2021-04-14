package com.example.snapchatclone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL

class viewSnapActivity : AppCompatActivity() {
    var messageTextView: TextView? = null
    var snapImageView: ImageView? =null
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)
        messageTextView = findViewById(R.id.messageTextView)
        snapImageView = findViewById(R.id.snapImageView)

        messageTextView?.text = intent.getStringExtra("message")

        //cpopied// download Image

        val task = ImageDownloader()
        val myImage: Bitmap
        try {
            myImage = task.execute(intent.getStringExtra("imageURL")).get()
            snapImageView?.setImageBitmap(myImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    inner class ImageDownloader : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            return try {
                val url = URL(urls[0])
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val `in` = connection.inputStream
                BitmapFactory.decodeStream(`in`)

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    ///

    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.currentUser?.uid?.let { intent.getStringExtra("snapkey")?.let { it1 -> FirebaseDatabase.getInstance().getReference().child("user").child(it).child("snap").child(it1).removeValue() } }
        intent.getStringExtra("imagename")?.let { FirebaseStorage.getInstance().getReference().child("images").child(it).delete() }
    }
    }
