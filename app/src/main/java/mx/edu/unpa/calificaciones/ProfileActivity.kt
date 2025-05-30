package mx.edu.unpa.calificaciones

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import mx.edu.unpa.calificaciones.models.Alumno
import mx.edu.unpa.calificaciones.models.Calificacion
import mx.edu.unpa.calificaciones.providers.AlumnoCallback
import mx.edu.unpa.calificaciones.providers.AlumnoProvider
import mx.edu.unpa.calificaciones.providers.AuthProvider
import mx.edu.unpa.calificaciones.providers.UsuarioProvider

class ProfileActivity : AppCompatActivity() {

    private val authProvider: AuthProvider = AuthProvider()
    private val alumnoProvider: AlumnoProvider = AlumnoProvider()
    private val usuarioProvider: UsuarioProvider = UsuarioProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        FooterHelper.setupBottomNavigation(this, bottomNav)
        bottomNav.selectedItemId = R.id.action_perf

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombreTextView = findViewById<TextView>(R.id.idNombrePerfil)
        val carreraTextView = findViewById<TextView>(R.id.idCarreraPerfil)
        val semestreTextView = findViewById<TextView>(R.id.idSemestrePerfil)
        val promedioTextView = findViewById<TextView>(R.id.idPromedioPerfil)
        val alumnoId = usuarioProvider.getId()

        alumnoProvider.obtenerAlumnoPorId(alumnoId, object : AlumnoCallback {
            override fun onSuccess(alumno: Alumno) {
                if (alumno != null) {
                    nombreTextView.text = "${alumno.nombre} ${alumno.apellidoPaterno} ${alumno.apellidoMaterno}"
                    carreraTextView.text = alumno.matricula ?: "Sin matrícula"
                    semestreTextView.text = "2024 - 2025 B"
                    val promedioParcial =  Alumno.promedioDesdeAlumnoConCalculo(alumno)
                    promedioTextView.text = "Promedio Parcial: " + String.format("%.2f", promedioParcial)

                }
            }


            override fun onFailure(e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error al obtener datos del alumno", Toast.LENGTH_SHORT).show()
                Log.e("ProfileActivity", "Error al obtener alumno", e)
            }
        })
    }





    fun exit(view: View) {
        if (authProvider.exitsSession()) {
            authProvider.exitSession()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
