package mx.edu.unpa.calificaciones.models
import com.beust.klaxon.Klaxon

private val klaxon= Klaxon()

data class Materia(
    val activo:Boolean?= null,
    val calificacion: Calificacion?= null,
    val cicloEscolar: String?= null,
    val nombre: String?=null,
    val semestre:String?=null
){
    public fun toJson()= klaxon.toJsonString(this)

    companion object{
        public fun fromJson(json:String)= klaxon.parse<Materia>(json)
    }

}