package mx.edu.unpa.calificaciones.models
import com.beust.klaxon.Klaxon
import com.google.firebase.firestore.DocumentReference

private val klaxon= Klaxon()

data class Alumno (
    val alumnoId: String?= null,
    val nombre: String? = null,
    val apellidoPaterno: String? = null,
    val apellidoMaterno: String? = null,
    val materia: List<Materia>?=null,
    val matricula:String?=null,
    val activo: Boolean? = null,
    val topics: List<DocumentReference> = listOf()
){
    public fun toJson()= klaxon.toJsonString(this)

    companion object{
        public fun fromJson(json:String)= klaxon.parse<Alumno>(json)
    }


}