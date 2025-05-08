package mx.edu.unpa.calificaciones

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.edu.unpa.calificaciones.models.Alumno
import mx.edu.unpa.calificaciones.models.Usuario
import mx.edu.unpa.calificaciones.providers.AlumnoProvider
import mx.edu.unpa.calificaciones.providers.AuthProvider
import mx.edu.unpa.calificaciones.providers.UsuarioProvider


class RegistrarActivity : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var lastname:EditText
    lateinit var email: EditText
    lateinit var password: EditText

    private val authProvider: AuthProvider = AuthProvider()
    private val usuarioProvider: UsuarioProvider =UsuarioProvider()
    private val alumnoProvider: AlumnoProvider =AlumnoProvider()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutRegistrar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email=findViewById(R.id.txtRegistrarEmail)
        password=findViewById(R.id.txtRegistrarContraseña)
        // Log.d("FIREBASE", "Email: $(email.text.toString()}");
        // Log.d("FIREBASE", "Pass: $(password.text.toString()}");
    }

    fun Welcome(view: View) {
        val intent = Intent(this, BienvenidaActivity::class.java)
        startActivity(intent);
    }

    fun Home(view: View) {

        val intent = Intent(this,MainActivity ::class.java)
        startActivity(intent);
    }

    // Registrar los datos del usuario
    fun registrarUsuario(view: View){
        authProvider.register(email.text.toString(), password.text.toString()).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this@RegistrarActivity,"Registro exitoso",Toast.LENGTH_LONG).show()
                val usuario = Usuario(
                    usuarioId= authProvider.getId(),
                    email= email.text.toString(),
                    Password= password.text.toString(),
                    activo= true
                )

                usuarioProvider.create(usuario).addOnCompleteListener{
                    if(it.isSuccessful){
                        // Toast.makeText(this@Registrar,"Registro exitoso del usuario",Toast.LENGTH_LONG).show()
                        Log.d("FIREBASE", "registrar [Registro exitoso del usuario]");

                        // Registrar los datos del alumno
                        registarAlumno(usuario);
                    }
                    else{
                        //Toast.makeText(this@Registrar,"Error alamcenaado datos del usuario ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                        Log.d("FIREBASE", "Error:${it.exception.toString()}");
                    }
                }
           }
           else{
                Toast.makeText(this@RegistrarActivity,"Registro fallido ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                Log.d("FIREBASE", "Error:${it.exception.toString()}");
            }
        }


    }

    fun registarAlumno(usuario: Usuario){
        val alumno=Alumno(
            alumnoId=usuario.usuarioId,
            nombre="Amaranny",
            apellidoPaterno="Olea",
            apellidoMaterno="Herrera",
            materia = listOf(),
            matricula = "21010019",
            activo = true
        )

        alumnoProvider.create(alumno).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this@RegistrarActivity,"Registro exitoso del alumno",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this@RegistrarActivity,"Error alamcenaado datos de estudiante ${it.exception.toString()}", Toast.LENGTH_LONG).show()
            }
        }
    }
}