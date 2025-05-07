package mx.edu.unpa.calificaciones.models

import com.beust.klaxon.Klaxon
import com.google.firebase.firestore.DocumentReference

private val klaxon= Klaxon()

data class Usuario (
    val usuarioId: String?= null,
    val email: String? = null,
    val Password: String? = null,
    val activo: Boolean? = null
){
    public fun toJson()= klaxon.toJsonString(this)

    companion object{
        public fun fromJson(json:String)= klaxon.parse<Usuario>(json)
    }


}