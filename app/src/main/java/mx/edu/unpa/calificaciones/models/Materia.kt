package mx.edu.unpa.calificaciones.models
// import android.os.Parcelable
import com.beust.klaxon.Klaxon
// import kotlinx.parcelize.Parcelize
// import kotlinx.parcelize.RawValue

private val klaxon= Klaxon()

//@Parcelize
data class Materia(
    val activo:Boolean?= null,
    val calificacion:  Calificacion?= null,
    val cicloEscolar: String?= null,
    val nombre: String?=null,
    val semestre:String?=null
)//: Parcelable
{
    public fun toJson()= klaxon.toJsonString(this)

    companion object{
        public fun fromJson(json:String)= klaxon.parse<Materia>(json)
    }

}