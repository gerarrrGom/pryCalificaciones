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

    /*
    fun getStudent(id: String): Query {
        return db.whereEqualTo("alumnoId", id)
    }
    */

    fun getStudent(field: String, value: String): Query {
        return db.whereEqualTo(field, value)
    }

    /*
    var db= Firebase.firestore.collection("Alumno")

    fun create(alumno: Alumno): Task<Void> {
        return db.document(alumno.alumnoId!!).set(alumno)
    }
    */

    interface AlumnoCallback {
        fun onSuccess(alumno: Alumno)
        fun onFailure(exception: Exception)
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

            val materias = mutableListOf<Materia>()
            var pendientes = materiasRefList.size
            var errorOcurrido = false

            for (materiaRef in materiasRefList) {
                materiaRef.get().addOnSuccessListener { materiaSnapshot ->
                    if (materiaSnapshot != null && materiaSnapshot.exists()) {
                        val nombreMateria = materiaSnapshot.getString("nombre")
                        val ciclo = materiaSnapshot.getString("cicloEscolar")
                        val semestre = materiaSnapshot.getString("semestre")
                        val activoMateria = materiaSnapshot.getBoolean("activo")

                        val califRef = materiaSnapshot.getDocumentReference("calificacion")
                        if (califRef!=null) {
                            califRef.get().addOnSuccessListener { califSnap ->
                                val calificacion = Calificacion(
                                    definitivo = califSnap.getString("definitivo"),
                                    especial = califSnap.getString("especial"),
                                    extra1 = califSnap.getString("extra1"),
                                    extra2 = califSnap.getString("extra2"),
                                    final = califSnap.getString("final"),
                                    parcial1 = califSnap.getString("parcial1"),
                                    parcial2 = califSnap.getString("parcial2"),
                                    parcial3 = califSnap.getString("parcial3"),
                                    promedio = califSnap.getString("promedio"),
                                    tipo = califSnap.getLong("tipo")?.toInt()
                                )
                                materias.add(
                                    Materia(
                                        nombre = nombreMateria,
                                        cicloEscolar = ciclo,
                                        semestre = semestre,
                                        activo = activoMateria,
                                        calificacion = calificacion
                                    )
                                )

                                pendientes--
                                if (pendientes == 0 && !errorOcurrido) {
                                    val alumno = Alumno(
                                        alumnoId = alumnoId,
                                        nombre = nombre,
                                        apellidoPaterno = apellidoPaterno,
                                        apellidoMaterno = apellidoMaterno,
                                        matricula = matricula,
                                        activo = activo,
                                        materia = materias // listOf() // materias
                                    )
                                    callback.onSuccess(alumno)
                                }
                            }.addOnFailureListener {
                                if (!errorOcurrido) {
                                    errorOcurrido = true
                                    Log.d("FIREBASE", "Error:${it.toString()}");
                                    callback.onFailure(it)
                                }
                            }
                        } else {
                            materias.add(
                                Materia(
                                    nombre = nombreMateria,
                                    cicloEscolar = ciclo,
                                    semestre = semestre,
                                    activo = activoMateria,
                                    calificacion = null
                                )
                            )
                            pendientes--
                            if (pendientes == 0 && !errorOcurrido) {
                                val alumno = Alumno(
                                    alumnoId = alumnoId,
                                    nombre = nombre,
                                    apellidoPaterno = apellidoPaterno,
                                    apellidoMaterno = apellidoMaterno,
                                    matricula = matricula,
                                    activo = activo,
                                    materia = materias // listOf() // materias
                                )
                                callback.onSuccess(alumno)
                            }
                        }
                    } else {
                        pendientes--
                        if (pendientes == 0 && !errorOcurrido) {
                            val alumno = Alumno(
                                alumnoId = alumnoId,
                                nombre = nombre,
                                apellidoPaterno = apellidoPaterno,
                                apellidoMaterno = apellidoMaterno,
                                matricula = matricula,
                                activo = activo,
                                materia = materias // listOf() // materias
                            )
                            callback.onSuccess(alumno)
                        }
                    }
                }.addOnFailureListener {
                    if (!errorOcurrido) {
                        errorOcurrido = true
                        Log.d("FIREBASE", "Error:${it.toString()}");
                        callback.onFailure(it)
                    }
                }
            }

        } else {
            callback.onFailure(Exception("Documento de alumno no encontrado"))
        }
    }.addOnFailureListener {
        Log.d("FIREBASE", "Error:${it.toString()}");
        callback.onFailure(it)
    }
}
}
