package `in`.androguy.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.emailEt
import kotlinx.android.synthetic.main.activity_sign_in.passET

class SignInActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        firebaseAuth=FirebaseAuth.getInstance()


        btnSignIn.setOnClickListener{
            val email=emailEt.text.toString()
            val password=passET.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

                    if (it.isSuccessful) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            }else{
                Toast.makeText(this, "Empty Fields Are not Allowed !", Toast.LENGTH_SHORT).show()
            }

        }

        tvSignUp.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}