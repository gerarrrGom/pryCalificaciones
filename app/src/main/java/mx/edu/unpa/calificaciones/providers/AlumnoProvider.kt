package mx.edu.unpa.calificaciones.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

import mx.edu.unpa.calificaciones.models.Alumno

class AlumnoProvider {
    var db= Firebase.firestore.collection("Alumno")

    fun create(alumno: Alumno): Task<Void> {
        return db.document(alumno.alumnoId!!).set(alumno)
    }

    fun getAlumnoById(alumnoId: String, callback: (Alumno?) -> Unit) {
        db.whereEqualTo("alumnoId",alumnoId)
            .get()
            .addOnCompleteListener{ doc ->
                if(doc.result.isEmpty){
                    callback(null)
                }else{
                    val alumno = doc.result.first().toObject<Alumno>()
                    callback(alumno)
                }
            }
            .addOnFailureListener{ ex ->
                println("Tas bien tostao")
                callback(null)
            }
    }
}