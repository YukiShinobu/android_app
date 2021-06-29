package au.edu.utas.username.assignment2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import au.edu.utas.username.assignment2.databinding.ActivityInsideStudentPageDisplayBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InsideStudentPageDisplay:   AppCompatActivity()  {

    private lateinit var ui :ActivityInsideStudentPageDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityInsideStudentPageDisplayBinding.inflate(layoutInflater)
        setContentView(ui.root)

        var g: String? = "No Data Here"

        if(studentD?.score?.isNotEmpty()!!){
            ui.sumarryGrades.text = studentD?.score.toString()
        }else if (studentD!!.score.isEmpty()) {
            ui.sumarryGrades.text = g
        }
    }
}

