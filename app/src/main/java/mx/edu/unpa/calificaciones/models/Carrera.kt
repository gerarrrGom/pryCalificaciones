package mx.edu.unpa.calificaciones.models
import com.beust.klaxon.Klaxon

private val klaxon= Klaxon()

data class Carrera(
    val descripcion: String?=null,
    val planDeEstudios: PlanDeEstudios?=null,
){
    public fun toJson()= klaxon.toJsonString(this)

    companion object{
        public fun fromJson(json:String)= klaxon.parse<PlanDeEstudios>(json)
    }
}