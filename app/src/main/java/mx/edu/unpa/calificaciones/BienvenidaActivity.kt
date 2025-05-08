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
import mx.edu.unpa.calificaciones.models.Alumno
import mx.edu.unpa.calificaciones.models.Usuario
import mx.edu.unpa.calificaciones.providers.AlumnoProvider
import mx.edu.unpa.calificaciones.providers.AuthProvider
import mx.edu.unpa.calificaciones.providers.UsuarioProvider

class BienvenidaActivity : AppCompatActivity() {
    lateinit var name:TextView

    private val authProvider: AuthProvider= AuthProvider()
    private val usuarioProvider: UsuarioProvider =UsuarioProvider()
    private val alumnoProvider: AlumnoProvider= AlumnoProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        name=findViewById(R.id.txtNombre)
    }

    override fun onStart() {
        super.onStart()
        getUsuario()

    }

    private fun getUsuario(){
        usuarioProvider.getUsuario().get().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this@BienvenidaActivity,"Consulta exitosa",Toast.LENGTH_LONG).show()

                val document= it.result.documents
                val usuario= document[0].toObject(Usuario::class.java)
                Log.d("FIREBASE", "getUsuario [Usuario: ${usuario}]");

                // Consultar datos del estudiante
                getStudent(usuario);
            }else{
                Toast.makeText(this@BienvenidaActivity,"Cosulta fallida ${it.exception.toString()}",Toast.LENGTH_LONG).show()
                Log.d("FIREBASE", "Error: ${it.exception.toString()}");
            }
        }
    }

    private fun getStudent(usuario: Usuario?){
        alumnoProvider.getStudent(usuario?.usuarioId.toString()).get().addOnCompleteListener{
            if(it.isSuccessful){
                // Toast.makeText(this@BienvenidaActivity,"Consulta exitosa",Toast.LENGTH_LONG).show()

                val document= it.result.documents
                val student= document[0].toObject(Alumno::class.java)
                Log.d("FIREBASE", "getStudent [Estudiante: ${student}]");

                name.setText(student?.nombre);
                // val intent = Intent(this,HomeActivity::class.java)
                // startActivity(intent)
            }else{
                // Toast.makeText(this@BienvenidaActivity,"Cosulta fallida ${it.exception.toString()}",Toast.LENGTH_LONG).show()
                Log.d("FIREBASE", "Error: ${it.exception.toString()}");
            }
        }
    }

    /*override fun onStart() {
        super.onStart()
        alumnoProvider.getAlumnoById("AC1Vs93aUTbA1hWDep220jrrxao1"){
            alumno: Alumno? ->
            if (alumno!=null){
                Toast.makeText(this,"Lo lograste bro @alumno",Toast.LENGTH_LONG).show()
                Toast.makeText(this,alumno.name,Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"no hubo alumno, bro",Toast.LENGTH_LONG).show()
            }
        }
    }*/

    fun exit(view: View){
        if(authProvider.exitsSession()){
            authProvider.exitSession()
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}