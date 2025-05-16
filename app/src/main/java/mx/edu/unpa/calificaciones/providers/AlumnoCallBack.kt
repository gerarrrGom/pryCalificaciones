package mx.edu.unpa.calificaciones.providers

import mx.edu.unpa.calificaciones.models.Alumno

// AlumnoCallback.kt
interface AlumnoCallback {
    fun onSuccess(alumno: Alumno)
    fun onFailure(e: Exception)
}