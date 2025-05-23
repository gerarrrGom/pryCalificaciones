package mx.edu.unpa.calificaciones.models

import com.beust.klaxon.Klaxon

private val klaxon= Klaxon()
data class Calificacion(
    val definitivo: String? = null,
    val especial: String? = null,
    val extra1: String? = null,
    val extra2: String? = null,
    val final: String? = null,
    val parcial1: String? = null,
    val parcial2: String? = null,
    val parcial3: String? = null,
    val promedio: String? = null,
    val tipo: Int? = null
){
    public fun toJson()= klaxon.toJsonString(this)

    companion object {
        fun fromJson(json: String) = klaxon.parse<Calificacion>(json)
        fun calcularPromedioReal(calificacion: Calificacion): Double? {
            val notas = listOfNotNull(
                calificacion.parcial1?.toDoubleOrNull(),
                calificacion.parcial2?.toDoubleOrNull(),
                calificacion.parcial3?.toDoubleOrNull()
            )

            return if (notas.isNotEmpty()) notas.average() else null
        }

    }



}