package mx.edu.unpa.calificaciones.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import mx.edu.unpa.calificaciones.models.Materia

class MateriaProvider {
    var db = Firebase.firestore.collection("Materia")
    // var _id: String? = null
/*
    fun create(materia : Materia): Task<Void>{
        materia.materiaId = materia.materiaId ?: db.document().id
        return db.document(materia.materiaId!!).set(materia)
    }
*/
    fun getGrade(id: String): Query{
        return db.whereEqualTo("gradeId", id)
    }

    /*fun getId(): String {
        return _id.toString()
    }
    fun setId(id: String) {
        _id = id
    }*/
}