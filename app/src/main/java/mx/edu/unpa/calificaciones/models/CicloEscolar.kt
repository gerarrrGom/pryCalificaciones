package mx.edu.unpa.calificaciones.models

import com.beust.klaxon.Klaxon

private val klaxon= Klaxon()

data class CicloEscolar (
    val cicloEscolarId: String?= null,
    val añoInicio: String?= null,
    val añoFin: String?=null,
    val periodo:String?=null,
    val activo:Boolean?= null
){
    public fun toJson()= klaxon.toJsonString(this)

    companion object{
        public fun fromJson(json:String)= klaxon.parse<CicloEscolar>(json)
    }

}