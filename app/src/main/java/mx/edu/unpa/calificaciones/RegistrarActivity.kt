package mx.edu.unpa.calificaciones

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import mx.edu.unpa.calificaciones.models.Alumno
import mx.edu.unpa.calificaciones.models.Usuario
import mx.edu.unpa.calificaciones.providers.AlumnoProvider
import mx.edu.unpa.calificaciones.providers.AuthProvider
import mx.edu.unpa.calificaciones.providers.UsuarioProvider

class RegistrarActivity : AppCompatActivity() {

    // Usa TextInputEditText ya que en tu layout estás usando ese tipo
    private lateinit var nombre: TextInputEditText
    private lateinit var apellidoPat: TextInputEditText
    private lateinit var apellidoMat: TextInputEditText
    private lateinit var matricula: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText

    private val authProvider: AuthProvider = AuthProvider()
    private val usuarioProvider: UsuarioProvider = UsuarioProvider()
    private val alumnoProvider: AlumnoProvider = AlumnoProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutRegistrar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de los campos
        nombre = findViewById(R.id.txtRegistrarNombre)
        apellidoPat = findViewById(R.id.txtRegistrarApellidoPat)
        apellidoMat = findViewById(R.id.txtRegistrarApellidoMat)
        matricula = findViewById(R.id.txtRegistrarMatricula)
        email = findViewById(R.id.txtRegistrarEmail)
        password = findViewById(R.id.txtRegistrarContraseña)
    }

    fun Welcome(view: View) {
        val intent = Intent(this, BienvenidaActivity::class.java)
        startActivity(intent)
    }

    fun Home(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Registrar los datos del usuario
    fun registrarUsuario(view: View) {
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Email y contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        authProvider.register(emailText, passwordText).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()

                val usuario = Usuario(
                    usuarioId = authProvider.getId(),
                    email = emailText,
                    Password = passwordText,
                    activo = true
                )

                usuarioProvider.create(usuario).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FIREBASE", "Registro exitoso del usuario")
                        registrarAlumno(usuario)
                    } else {
                        Log.e("FIREBASE", "Error registrando usuario: ${task.exception}")
                    }
                }
            } else {
                Log.e("FIREBASE", "Error en Auth: ${it.exception}")
                Toast.makeText(this, "Error en registro: ${it.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun registrarAlumno(usuario: Usuario) {
        val matriculaText = matricula.text.toString().trim()
        val nombreText = nombre.text.toString().trim()
        val apellidoPatText = apellidoPat.text.toString().trim()
        val apellidoMatText = apellidoMat.text.toString().trim()

        if (matriculaText.isEmpty() || nombreText.isEmpty() || apellidoPatText.isEmpty() || apellidoMatText.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        val alumno = Alumno(
            alumnoId = usuario.usuarioId,
            nombre = nombreText,
            apellidoPaterno = apellidoPatText,
            apellidoMaterno = apellidoMatText,
            materia = listOf(),
            matricula = matriculaText,
            activo = true
        )

        alumnoProvider.create(alumno).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Registro exitoso del alumno", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error guardando alumno: ${it.exception?.message}", Toast.LENGTH_LONG).show()
                Log.e("FIREBASE", "Error guardando alumno: ${it.exception}")
            }
        }
    }
}
