package mx.edu.unpa.calificaciones.models
import com.beust.klaxon.Klaxon
import com.google.firebase.firestore.DocumentReference
import mx.edu.unpa.calificaciones.models.Calificacion.Companion.calcularPromedioReal

private val klaxon= Klaxon()

data class Alumno (
    val alumnoId: String?= null,
    val nombre: String? = null,
    val apellidoPaterno: String? = null,
    val apellidoMaterno: String? = null,
    val materia: List<Materia>?=null,
    // val materia: List<DocumentReference>?=null,
    val matricula:String?=null,
    val activo: Boolean? = null
){
    public fun toJson()= klaxon.toJsonString(this)

    companion object{
        public fun fromJson(json:String)= klaxon.parse<Alumno>(json)

        fun promedioDesdeAlumnoConCalculo(alumno: Alumno): Double {
            val promedios = alumno.materia
                ?.mapNotNull { it.calificacion?.let { cal -> calcularPromedioReal(cal) } }
                ?: emptyList()

            return if (promedios.isNotEmpty()) promedios.average() else 0.0
        }

    }


}