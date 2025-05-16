package mx.edu.unpa.calificaciones.providers

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.oAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import mx.edu.unpa.calificaciones.HomeActivity

import mx.edu.unpa.calificaciones.models.Alumno
import mx.edu.unpa.calificaciones.models.Calificacion
import mx.edu.unpa.calificaciones.models.Materia

class AlumnoProvider {
    var db=Firebase.firestore.collection("Alumno")

    fun create (alumno: Alumno): Task<Void>{
        return db.document(alumno.alumnoId!!).set(alumno)
    }


    fun getStudent(field: String, value: String): Query {
        return db.whereEqualTo(field, value)
    }



    fun obtenerAlumnoPorId(alumnoId: String, callback: AlumnoCallback) {
            val alumnoDoc = db.document(alumnoId)

            alumnoDoc.get().addOnSuccessListener { snapshot ->
                if (snapshot != null && snapshot.exists()) {
                    val nombre = snapshot.getString("nombre")
                    val apellidoPaterno = snapshot.getString("apellidoPaterno")
                    val apellidoMaterno = snapshot.getString("apellidoMaterno")
                    val matricula = snapshot.getString("matricula")
                    val activo = snapshot.getBoolean("activo")
                    val materiasRefList = snapshot.get("materia") as? List<DocumentReference>

                    if (materiasRefList.isNullOrEmpty()) {
                        val alumno = Alumno(
                            alumnoId = alumnoId,
                            nombre = nombre,
                            apellidoPaterno = apellidoPaterno,
                            apellidoMaterno = apellidoMaterno,
                            matricula = matricula,
                            activo = activo,
                            materia = emptyList()
                        )
                        callback.onSuccess(alumno)
                        return@addOnSuccessListener
                    }

                    val materiaProvider = MateriaProvider()
                    materiaProvider.obtenerMaterias(materiasRefList) { materias, error ->
                        if (error != null) {
                            callback.onFailure(error)
                            return@obtenerMaterias
                        }

                        val alumno = Alumno(
                            alumnoId = alumnoId,
                            nombre = nombre,
                            apellidoPaterno = apellidoPaterno,
                            apellidoMaterno = apellidoMaterno,
                            matricula = matricula,
                            activo = activo,
                            materia = materias ?: emptyList()
                        )
                        callback.onSuccess(alumno)
                    }

                } else {
                    callback.onFailure(Exception("Documento de alumno no encontrado"))
                }
            }.addOnFailureListener {
                callback.onFailure(it)
            }
        }
    }


