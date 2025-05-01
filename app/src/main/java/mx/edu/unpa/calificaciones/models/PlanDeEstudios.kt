package mx.edu.unpa.calificaciones.models
import com.beust.klaxon.Klaxon

private val klaxon= Klaxon()

data class PlanDeEstudios(
    val Alumnos:List<Alumno>?=null,
    val clave: String?=null,
    val descripcion:String?=null
){
    public fun toJson()= klaxon.toJsonString(this)

    companion object{
        public fun fromJson(json:String)= klaxon.parse<PlanDeEstudios>(json)
    }
}