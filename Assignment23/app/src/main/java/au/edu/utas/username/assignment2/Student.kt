package au.edu.utas.username.assignment2

class Student (
    var id : String? = null,

    var name : String? = null,
    var studentNumber : Int? = null,
    var score : MutableMap<String, String> = mutableMapOf(),
    var attendance: MutableList<String> = mutableListOf()
)