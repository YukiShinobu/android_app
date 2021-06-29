package au.edu.utas.username.assignment2

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.username.assignment2.databinding.ActivityInsideStudentPageDisplayBinding
import au.edu.utas.username.assignment2.databinding.ActivityTotalSummaryPageDisplayBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_inside_student_page.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_total_summary_page.*
import kotlinx.android.synthetic.main.activity_total_summary_page_display.*

/*
* HD+ 90
* HD  80
* DN  70
* CR  60
* PP  50
* NN  0
*
* */

class TotalSummaryPageDisplay: AppCompatActivity() {

    private lateinit var ui: ActivityTotalSummaryPageDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityTotalSummaryPageDisplayBinding.inflate(layoutInflater)
        setContentView(ui.root)
        var g: Int? = 0
        val temp = arrayOf<Student>()

        val db = FirebaseFirestore.getInstance()
        db.collection("student")
                .get()
                .addOnCompleteListener {
                    val result: StringBuffer = StringBuffer()
                    val result2: StringBuffer = StringBuffer()

                    if (it.isSuccessful) {
                        for (document in it.result!!) {
                                //temp.append(document.data.getValue("score")).append("\n\n")
                                result.append(document.data.getValue("score")).append("\n\n")
                                result2.append(document.data.getValue("name")).append("\n\n")
                            }
                            //ui.totalsummary.text = g.toString()
                        }

                    totalsummary.setText(result).toString()
                    studentname.setText(result2).toString()
                    //studentname.setText(result2)
                    //ui.totalsummary.text = g
                }

        /*if (studentD?.score?.get(week)?.contains("HD+")!!) {
            g = 90
        } else if (studentD?.score?.get(week)?.contains("HD")!!) {
            g = 80
        } else if (studentD?.score?.get(week)?.contains("DN")!!) {
            g = 70
        } else if (studentD?.score?.get(week)?.contains("CR")!!) {
            g = 60
        } else if (studentD?.score?.get(week)?.contains("PP")!!) {
            g = 50
        } else if (studentD?.score?.get(week)?.contains("NN")!!) {
            g = 0
        }*/
    }
}