package mx.edu.unpa.calificaciones.providers

import com.google.firebase.firestore.DocumentReference
import mx.edu.unpa.calificaciones.models.Calificacion

class CalificacionProvider {

    fun obtenerCalificacion(
        ref: DocumentReference,
        callback: (Calificacion?, Exception?) -> Unit
    ) {
        ref.get().addOnSuccessListener { snapshot ->
            if (snapshot != null && snapshot.exists()) {
                val calificacion = Calificacion(
                    definitivo = snapshot.getString("definitivo"),
                    especial = snapshot.getString("especial"),
                    extra1 = snapshot.getString("extra1"),
                    extra2 = snapshot.getString("extra2"),
                    final = snapshot.getString("final"),
                    parcial1 = snapshot.getString("parcial1"),
                    parcial2 = snapshot.getString("parcial2"),
                    parcial3 = snapshot.getString("parcial3"),
                    promedio = snapshot.getString("promedio"),
                    tipo = snapshot.getLong("tipo")?.toInt()
                )
                callback(calificacion, null)
            } else {
                callback(null, Exception("Documento de calificación no encontrado"))
            }
        }.addOnFailureListener {
            callback(null, it)
        }
    }
}
