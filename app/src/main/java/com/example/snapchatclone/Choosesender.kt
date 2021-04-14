package com.example.snapchatclone
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class Choosesender : AppCompatActivity() {

    var chooseuserListView: ListView? = null
    var emails: ArrayList<String> = ArrayList()
    var keys: ArrayList<String> = ArrayList()
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
                    snapshot.key?.let { keys.add(it) }
                    adapter.notifyDataSetChanged()
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })

    chooseuserListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        val snapMap: Map<String, String?> = mapOf("from" to FirebaseAuth.getInstance().currentUser!!.email!! , "message" to intent.getStringExtra("message"), "imageName" to intent.getStringExtra(("imagename")))
        FirebaseDatabase.getInstance().getReference().child("user").child(keys.get(position)).child("snap").push().setValue(snapMap)
        val intent = Intent(this,CreateSnapsActivity2::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
}