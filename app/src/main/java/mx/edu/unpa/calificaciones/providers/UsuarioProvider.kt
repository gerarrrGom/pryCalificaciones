package mx.edu.unpa.calificaciones.providers

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import mx.edu.unpa.calificaciones.BienvenidaActivity
import mx.edu.unpa.calificaciones.models.Alumno
import mx.edu.unpa.calificaciones.models.Usuario

class UsuarioProvider {
    var db = Firebase.firestore.collection("Usuario")
    var authProvider = AuthProvider()
    var alumnoProvider = AlumnoProvider()

    fun create(usuario : Usuario): Task<Void>{
        return db.document(usuario.usuarioId!!).set(usuario)
    }



    fun getUsuario(field: String, value: String): Query {
        return db.whereEqualTo(field, value)
    }

    fun getId():String{
        return authProvider.getId()
    }

    // stId = student ID (Matricula)
    fun login(stId: String, pass:String):Task<AuthResult>{
        val taskCompletionSource = TaskCompletionSource<AuthResult>()

        alumnoProvider.getStudent("matricula", stId).get().addOnCompleteListener{
            if(it.isSuccessful){
                val result = it.result
                if (!result.isEmpty) {
                    val document = result.documents[0]
                    val alumnoId = document.getString("alumnoId")

                    if (!alumnoId.isNullOrEmpty()) {
                        getUsuario("usuarioId", alumnoId).get().addOnCompleteListener{
                            if(it.isSuccessful){
                                val result = it.result
                                if (!result.isEmpty) {
                                    val document = result.documents[0]
                                    val email = document.getString("email")

                                    if (!email.isNullOrEmpty()) {
                                        authProvider.login(email, pass).addOnCompleteListener{ authResult ->
                                            taskCompletionSource.setResult(authResult.result)
                                        }.addOnFailureListener { e ->
                                            taskCompletionSource.setException(e)
                                        }
                                    } else {
                                        taskCompletionSource.setException(Exception("Email no encontrado para esta matrícula."))
                                    }
                                }else{
                                    taskCompletionSource.setException(Exception("Usuario Id no encontrado para esta matrícula."))
                                }
                            }else{
                                taskCompletionSource.setException(it.exception ?: Exception("Error al consultar la base de datos."))
                            }
                        }
                    } else {
                        taskCompletionSource.setException(Exception("Alumno ID no encontrado para esta matrícula."))
                    }
                } else {
                    taskCompletionSource.setException(Exception("Matrícula no encontrada."))
                }
            }else{
                taskCompletionSource.setException(it.exception ?: Exception("Error al consultar la base de datos."))
                // Toast.makeText(this@BienvenidaActivity,"Cosulta fallida ${it.exception.toString()}",Toast.LENGTH_LONG).show()
                Log.d("FIREBASE", "Error: ${it.exception.toString()}");
            }
        }
        return taskCompletionSource.task
    }
}