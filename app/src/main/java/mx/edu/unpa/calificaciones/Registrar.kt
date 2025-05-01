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
import mx.edu.unpa.calificaciones.providers.AlumnoProvider
import mx.edu.unpa.calificaciones.providers.AuthProvider


class Registrar : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var lastname:EditText
    lateinit var email: EditText
    lateinit var pass: EditText

    private val authProvider: AuthProvider = AuthProvider()
    private val AlumnoProvider: AlumnoProvider =AlumnoProvider()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun Welcome(view: View) {
        val intent = Intent(this, Bienvenida::class.java)
        startActivity(intent);
    }

    fun Home(view: View) {

        val intent = Intent(this,MainActivity ::class.java)
        startActivity(intent);
    }

    fun registrar(view: View){
        email=findViewById(R.id.txtcuadroNombreR)
        pass=findViewById(R.id.txtCuadroR)
        Log.d("FIREBASE", "Email: $(email.text.toString()}");
        Log.d("FIREBASE", "Pass: $(pass.text.toString()}");

        authProvider.register(email.text.toString(), pass.text.toString()).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this@Registrar,"Registro exitoso",Toast.LENGTH_LONG).show()
                val alumno=Alumno(
                    alumnoId=authProvider.getId(),
                    nombre="Amaranny",
                    apellidoPaterno="Olea",
                    apellidoMaterno="Herrera",
                    matricula = "21010019",


                )
                AlumnoProvider.create(alumno).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this@Registrar,"Registro exitoso del alumno",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this@Registrar,"Error alamcenaado datos de estudiante ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                    }
                }
           }
           else{
                Toast.makeText(this@Registrar,"Registro fallido ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                Log.d("FREBASE", "Error:${it.exception.toString()}");
            }
        }


    }
}