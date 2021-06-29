package au.edu.utas.username.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.username.assignment2.databinding.ActivityAddStudentDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class  Add_student : AppCompatActivity() {
    private lateinit var ui : ActivityAddStudentDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityAddStudentDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val db = Firebase.firestore
        var studentCollection = db.collection("student")

        ui.StudentaddBtn.setOnClickListener {

            var name = ui.TypeName.text.toString()
            var num  = ui.TypeId.text.toString().toInt() //good code would check this is really an int

            val new = Student(name = name, studentNumber = num )
            for(i in 1..13){
                new.score
                onResume()
            }

            studentCollection.add(new).addOnSuccessListener {
                new.id = it.id
                items2.add(new)
                finish()

            }.addOnFailureListener {

            }
        }
    }
}