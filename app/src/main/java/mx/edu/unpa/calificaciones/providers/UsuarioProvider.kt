package mx.edu.unpa.calificaciones.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import mx.edu.unpa.calificaciones.models.Usuario

class UsuarioProvider {
    var db = Firebase.firestore.collection("Usuario")
    var authProvider = AuthProvider()

    fun create(usuario : Usuario): Task<Void>{
        return db.document(usuario.usuarioId!!).set(usuario)
    }

    fun getUsuario(): Query{
        return db.whereEqualTo("usuarioId", authProvider.getId())
    }

    fun getId():String{
        return authProvider.getId()
    }
}