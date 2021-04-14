package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.zip.Inflater

class SnapsActivity : AppCompatActivity() {
    var snapListView:ListView? = null
    val mAuth = FirebaseAuth.getInstance()
    var emails:ArrayList<String> = ArrayList()
    var snaps:ArrayList<DataSnapshot> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
        snapListView = findViewById(R.id.snapslistView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,emails)
        snapListView?.adapter = adapter

        mAuth.currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference().child("user").child(it).child("snap").addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                emails.add(snapshot.child("from").value as String)
                snaps.add(snapshot)
                adapter.notifyDataSetChanged()
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
        }

        snapListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val snapshot = snaps.get(position)
            var intent = Intent(this,viewSnapActivity::class.java)
            intent.putExtra("imageName" , snapshot.child("imagename").value as String)
            //////
            intent.putExtra("imageUrl",snapshot.child("imageURL").value as String)
            ///////
            intent.putExtra("message",snapshot.child("message").value as String)
            intent.putExtra("snapkey", snapshot.key)
            startActivity(intent)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.snaps,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId == R.id.createsnap){
            val intent = Intent(this,CreateSnapsActivity2::class.java)
            startActivity(intent)

        }
        else if(item?.itemId==R.id.logout){
                mAuth.signOut()
               finish()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut()
    }
}