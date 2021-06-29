package au.edu.utas.username.assignment2

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.CheckBox
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.username.assignment2.databinding.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dispaly_student.view.*
import kotlinx.android.synthetic.main.activity_inside_student_page.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_total_summary_page.*
import kotlinx.android.synthetic.main.activity_total_summary_page_display.*
import kotlin.collections.ArrayList
//get db connection
val db = Firebase.firestore
var displayCollection = db.collection("student")
val items2 = ArrayList<Student>()
const val FIREBASE_TAG = "FirebaseLogging"
val Grades : MutableList<String> = mutableListOf("Choose Grades", "HD+", "HD", "DN", "CR", "PP", "NN")
var week = ""
var studentD : Student? = null

class MainActivity : AppCompatActivity() {

    private lateinit var ui: ActivityMainBinding
    private lateinit var uisa: ActivityAddStudentDetailsBinding
    private lateinit var  u2: ActivityDispalyStudentBinding
    private lateinit var u3: ActivityInsideStudentPageBinding
    private lateinit var u4: ActivityInsideStudentPageDisplayBinding
    private lateinit var u5: ActivityTotalSummaryPageDisplayBinding
    private lateinit var u6: ActivityTotalSummaryPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        uisa = ActivityAddStudentDetailsBinding.inflate(layoutInflater)
        u2 = ActivityDispalyStudentBinding.inflate(layoutInflater)
        u3 = ActivityInsideStudentPageBinding.inflate(layoutInflater)
        u4 = ActivityInsideStudentPageDisplayBinding.inflate(layoutInflater)
        u5 = ActivityTotalSummaryPageDisplayBinding.inflate(layoutInflater)

        findViewById<RecyclerView>(R.id.myList).adapter = StudentAdapter(student = items2)

        ui.myList.layoutManager = LinearLayoutManager(this)

        displayCollection
                .get()
                .addOnSuccessListener { result ->
                    items2.clear() //this line clears the list, and prevents a bug where items would be duplicated upon rotation of screen
                    Log.d(FIREBASE_TAG, "--- all movies ---")
                    for (document in result) {
                        val movie = document.toObject<Student>()
                        movie.id = document.id
                        Log.d(FIREBASE_TAG, movie.toString())
                        items2.add(movie)
                    }
                    (ui.myList.adapter as StudentAdapter).notifyDataSetChanged()
                }

        ui.addBtn.setOnClickListener {
            var i = Intent(this, Add_student::class.java)
            startActivity(i)
        }

        //Swiped function
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteItem(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
            ) {

                super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                val background = ColorDrawable(Color.RED)
                val deleteIcon = AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_baseline_delete_36
                )
                val iconMarginVertical =
                        (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight) / 2
                deleteIcon.setBounds(
                        itemView.left + iconMarginVertical,
                        itemView.top + iconMarginVertical,
                        itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth,
                        itemView.bottom - iconMarginVertical
                )
                background.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.right + dX.toInt(),
                        itemView.bottom
                )
                background.draw(c)
                deleteIcon.draw(c)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(ui.myList)

        //Spinner for Week Calender
        val adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinnerItems,
                android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        ui.spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        week = parent.getItemAtPosition(position).toString()
                        onResume()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
         }

    inner class StudentHolder(var ui: ActivityDispalyStudentBinding) : RecyclerView.ViewHolder(ui.root) {}

    inner class StudentAdapter(private val student: MutableList<Student>) : RecyclerView.Adapter<StudentHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentHolder {
            val ui = ActivityDispalyStudentBinding.inflate(
                    layoutInflater, parent, false)   //inflate a new row from the my_list_item.xml
            return StudentHolder(ui)
        }

        override fun getItemCount(): Int {
            return student.size
        }

        //var p = 0

        override fun onBindViewHolder(holder: StudentHolder, position: Int) {

            val person = student[position]   //get the data at the requested position
            //p = position
            holder.ui.stuName.text = person.name //set the TextView in the row we are recycling
            holder.ui.stuId.text = person.studentNumber.toString()

            if(student[position].attendance.contains(week)){
                holder.ui.checkbox.setChecked(true)
            }else{
                holder.ui.checkbox.setChecked(false)
            }
            holder.ui.checkbox.setOnClickListener {
                //onCheckboxClicked(holder.ui.checkbox, student[position], week)
                if(holder.ui.checkbox.isChecked){
                    Log.d("CHECKBOX", "IS CLICKED")
                    if(!student[position].attendance.contains(week.toString())){
                        student[position].attendance.add(week.toString())
                        displayCollection.document(student[position].id!!).set(student[position]).addOnSuccessListener {
                            Log.d("DELETE", " Success ")
                        }

                    }else{
                        Log.d("Check Box", "Student Already Attended")
                    }
                }else{
                    Log.d("CHECK BOX", "UNCLICKED")
                    if(student[position].attendance.contains(week.toString())){
                        student[position].attendance.remove(week.toString())
                        displayCollection.document(student[position].id!!).set(student[position]).addOnSuccessListener {
                            Log.d("DELETE", " Success ")
                        }
                    }else{
                        Log.d("Check Box", "Student Not Attended")
                    }
                }
            }

            holder.ui.gradeSpinner.adapter = ArrayAdapter<String>(
                    this@MainActivity,
                    android.R.layout.simple_spinner_dropdown_item, Grades
            )

            holder.ui.gradeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    if (parent.getItemAtPosition(position).equals("Choose Grades")) {
                        //do nothing
                    } else {
                        val g = parent.getItemAtPosition(position).toString()
                        person.score[week] = g
                        displayCollection.document(person.id!!).set(person).addOnSuccessListener { Log.d("Grade add", "") }
                        //removeEmptyChoise()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

            val db = Firebase.firestore
            var displayCollection = db.collection("student")

            /*holder.ui.InsidePage.setOnClickListener {
                val person = student[position]
                studentD = null
                studentD = student[position]

                var n =  person.id
                var id = person.id

                //var i = Intent(this@MainActivity, InsideStudentPages::class.java)
                *//*i.putExtra("name", n)
                i.putExtra("id", id)
                startActivity(i)*//*
            }*/

            holder.ui.InsidePage.setOnClickListener {
                val person = student[position]
                studentD = null
                studentD = student[position]

                var i2 = Intent(this@MainActivity, InsideStudentPageDisplay::class.java)
                startActivity(i2)
            }

            ui.tButton.setOnClickListener {
                val document = student[position]
                studentD = null
                studentD = student[position]
                val a = document.id
                var i3 = Intent(this@MainActivity, TotalSummaryPageDisplay::class.java)
                startActivity(i3)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        ui.myList.adapter?.notifyDataSetChanged()
    }

    //To delete swiped item
    fun deleteItem(position: Int) {
        val db = Firebase.firestore
        var displayCollection = db.collection("student")
        //var te = db.collection("Testing")

        val currentItem = items2[position]
        items2.removeAt(position)
        displayCollection.document(currentItem.id!!).delete().addOnSuccessListener {
            Log.d("DELETE", " Success ")
        }
        ui.myList.adapter?.notifyItemRemoved(position)
    }

    fun onCheckboxClicked(view: View, s : Student, w : String){
        val db = Firebase.firestore
        //val data = hashMapOf("student" to true)
        val currentItem = view
        var displayCollection = db.collection("student")

        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            if (checked) {
                if(!s. attendance.contains(w)){
                    if (checked) {
                        s.attendance.add(w)
                        displayCollection.document(s.id!!).set(s).addOnSuccessListener {
                            Log.d("DELETE", " Success ")
                        }

                    } else {
                    }
                }else{
                    Log.d("Check Box", "Student Already Attended")
                }

            }else{
                if(s.attendance.contains(w)){
                    if (checked) {
                        s.attendance.remove(w)
                        displayCollection.document(s.id!!).set(s).addOnSuccessListener {
                            Log.d("DELETE", " Success ")
                        }

                    } else {
                    }
                }else{
                    Log.d("Check Box", "Student Not Attended")
                }
            }

        } else { Log.d("Check box","This is not Check box")}

    }
}













