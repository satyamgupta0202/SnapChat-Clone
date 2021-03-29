package com.example.snapchatclone
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
     var passwordEditText: EditText ? = null
     var emailEditText: EditText ?= null
     val mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        passwordEditText = findViewById(R.id.passwordEditText)
        emailEditText = findViewById(R.id.emailEditText)

        if (mAuth.currentUser != null) {
            login()
        }
    }


        fun goClicked(view: View){

            mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(),passwordEditText?.text.toString())
                .addOnCompleteListener(this)
                { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        login()
                    } else {
                        // If sign in fails, display a message to the user.
                         mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                             .addOnCompleteListener(this)
                             { task ->
                             if(task.isSuccessful){
                                 login()
                             }
                             else{
                                 Toast.makeText(this,"Login FialedTry Ahain",Toast.LENGTH_SHORT).show()
                             }
                         }
                    }
                }
        }

        fun login(){
            // new page
           val intent = Intent(this,SnapsActivity::class.java)
            startActivity(intent)
        }
}