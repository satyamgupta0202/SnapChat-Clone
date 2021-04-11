package com.example.snapchatclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class Choosesender : AppCompatActivity() {
    var chooseuserListView: ListView? = null
    var emails: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosesender)
        chooseuserListView = findViewById(R.id.ChooseUserListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,emails)
        chooseuserListView?.adapter = adapter
        FirebaseDatabase.getInstance().getReference().child("user")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val email = snapshot?.child("email")?.value as String
                    emails.add(email)
                    adapter.notifyDataSetChanged()
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}