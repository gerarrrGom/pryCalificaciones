package mx.edu.unpa.calificaciones.models
import com.beust.klaxon.Klaxon

private val klaxon= Klaxon()

data class Alumno (
    val alumnoId: String?= null,
    val name: String? = null,
    val lastName: String? = null,
    val middleName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val active: String? = null
){
    public fun toJson()= klaxon.toJsonString(this)

    companion object{
        public fun fromJson(json:String)= klaxon.parse<Alumno>(json)
    }


}