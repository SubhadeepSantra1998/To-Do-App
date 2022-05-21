package `in`.androguy.todoapp

//class Model (val task: String?=null, val description: String?=null, val id:String?=null, val date: String?=null){
//}



class Model{
    var task: String?=null
    var description: String?=null
    var id:String?=null
    var date: String?=null

    constructor(){

    }


    constructor(task: String?, description: String?, id: String?, date: String?) {
        this.task = task
        this.description = description
        this.id = id
        this.date = date
    }


}