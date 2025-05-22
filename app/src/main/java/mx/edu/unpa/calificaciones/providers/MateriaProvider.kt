package mx.edu.unpa.calificaciones.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
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

        fun obtenerMaterias(
            referencias: List<DocumentReference>,
            callback: (List<Materia>?, Exception?) -> Unit
        ) {
            val materias = mutableListOf<Materia>()
            var pendientes = referencias.size
            var errorOcurrido = false

            for (ref in referencias) {
                ref.get().addOnSuccessListener { snapshot ->
                    if (snapshot != null && snapshot.exists()) {
                        val nombre = snapshot.getString("nombre")
                        val ciclo = snapshot.getString("cicloEscolar")

                        val semestre = snapshot.getString("semestre")
                        val activo = snapshot.getBoolean("activo")
                        val califRef = snapshot.getDocumentReference("calificacion")

                        if (califRef != null) {
                            val calificacionProvider = CalificacionProvider()
                            calificacionProvider.obtenerCalificacion(califRef) { calificacion, error ->
                                if (error != null && !errorOcurrido) {
                                    errorOcurrido = true
                                    callback(null, error)
                                    return@obtenerCalificacion
                                }

                                materias.add(
                                    Materia(
                                        nombre = nombre,
                                        cicloEscolar = ciclo,
                                        semestre = semestre,
                                        activo = activo,
                                        calificacion = calificacion
                                    )
                                )
                                pendientes--
                                if (pendientes == 0 && !errorOcurrido) {
                                    callback(materias, null)
                                }
                            }
                        } else {
                            materias.add(
                                Materia(
                                    nombre = nombre,
                                    cicloEscolar = ciclo,
                                    semestre = semestre,
                                    activo = activo,
                                    calificacion = null
                                )
                            )
                            pendientes--
                            if (pendientes == 0 && !errorOcurrido) {
                                callback(materias, null)
                            }
                        }

                    } else {
                        pendientes--
                        if (pendientes == 0 && !errorOcurrido) {
                            callback(materias, null)
                        }
                    }
                }.addOnFailureListener {
                    if (!errorOcurrido) {
                        errorOcurrido = true
                        callback(null, it)
                    }
                }
            }
        }
    }