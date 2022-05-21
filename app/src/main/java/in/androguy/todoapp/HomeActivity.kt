package `in`.androguy.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.input_file.view.*
import kotlinx.android.synthetic.main.update_delete.view.*
import java.text.DateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    lateinit var reference: DatabaseReference

    lateinit var auth: FirebaseAuth
    lateinit var mUser: FirebaseUser


    var onlineUserID: String=""


    lateinit var mRecyclerview : RecyclerView






        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)




            auth = Firebase.auth
        mUser = auth.currentUser!!
        onlineUserID = mUser.uid
        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID)



            mRecyclerview=findViewById(R.id.recyclerView)
            reference=FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID)
            mRecyclerview.layoutManager=LinearLayoutManager(this)


            val option : FirebaseRecyclerOptions<Model> = FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference,Model::class.java)
                .build()

            val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Model, MyViewHolder >(option){
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                    val itemView = LayoutInflater.from(applicationContext).inflate(R.layout.item,parent,false)
                    return MyViewHolder(itemView)
                }

                override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Model) {
                    val refid: String=getRef(position).key.toString()
                    reference.child(refid).addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            holder.date.setText(model.date)
                            holder.task.setText(model.task)
                            holder.desc.setText(model.description)

                            holder.itemView.setOnClickListener {
                                val key = getRef(position).getKey().toString();
                                val task = model.task.toString();
                                val description = model.description.toString();

                                updateDeleteTask(key,task,description);

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
                }

            }

            mRecyclerview.adapter=firebaseRecyclerAdapter
            firebaseRecyclerAdapter.startListening()





        fab.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val layoutInflator = LayoutInflater.from(this).inflate(R.layout.input_file, null)
            builder.setView(layoutInflator)
            builder.setCancelable(false)
            val mAlertDialog:AlertDialog=builder.show()


            layoutInflator.CancelBtn.setOnClickListener{
                mAlertDialog.dismiss()
            }

            layoutInflator.saveBtn.setOnClickListener{
                val task: String=layoutInflator.etTask.text.toString().trim()
                val description: String=layoutInflator.etdescription.text.toString().trim()
                val id = reference.push().key
                val date: String = DateFormat.getDateInstance().format(Date())

                if (task.isEmpty() && description.isEmpty()) {
                    Toast.makeText(this, "All fields are reuired",Toast.LENGTH_LONG).show()
                }else{

                    val model = Model(task, description, id, date)
                    if (id != null) {
                        reference.child(id).setValue(model).addOnSuccessListener {
                            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            mAlertDialog.dismiss()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }

        }



    }

    private fun updateDeleteTask(key: String, task: String, description: String) {
        val builder = AlertDialog.Builder(this)
        val layoutInflator = LayoutInflater.from(this).inflate(R.layout.update_delete, null)
        builder.setView(layoutInflator)
        val mAlertDialog:AlertDialog=builder.show()

        layoutInflator.mEditTextTask.setText(task)
        layoutInflator.mEditTextDescription.setText(description)


        layoutInflator.btnDelete.setOnClickListener{
            reference.child(key).removeValue().addOnSuccessListener {
                Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();
                mAlertDialog.dismiss()
            }
                .addOnFailureListener{
                    Toast.makeText(this, "Deletion Failed. Try Again", Toast.LENGTH_SHORT).show();
                }
        }

        layoutInflator.btnUpdate.setOnClickListener{
            val updatedTask: String = layoutInflator.mEditTextTask.text.toString().trim()
            val updatedDesc: String = layoutInflator.mEditTextDescription.text.toString().trim()
            val updatedDate: String = DateFormat.getDateInstance().format(Date()).toString()

            val model = Model(updatedTask, updatedDesc, key, updatedDate)
            reference.child(key).setValue(model).addOnSuccessListener {
                Toast.makeText(this, "Updated Successfully ", Toast.LENGTH_SHORT).show();
                mAlertDialog.dismiss()
            }
                .addOnFailureListener{
                    Toast.makeText(this, "Update Failed. Try Again", Toast.LENGTH_SHORT).show();
                }
        }
    }

    class MyViewHolder(itemView: View?):RecyclerView.ViewHolder(itemView!!){
        var date:TextView= itemView!!.findViewById(R.id.dateTv)
        var task:TextView=itemView!!.findViewById(R.id.taskTv)
        var desc:TextView=itemView!!.findViewById(R.id.descriptionTv)
    }



}