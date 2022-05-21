package `in`.androguy.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth=FirebaseAuth.getInstance()
        button.setOnClickListener{
            val email=emailEt.text.toString()
            val password=passET.text.toString()
            val confirmpass=confirmPassEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmpass.isNotEmpty()){
                if (password==confirmpass){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                        if (it.isSuccessful) {

                            Toast.makeText(this, "SignUp is Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                }else{
                    Toast.makeText(this, "Password is not matching !", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Empty Fields Are not Allowed !", Toast.LENGTH_SHORT).show()

            }

        }

        tvSignIn.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}