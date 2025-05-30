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
import mx.edu.unpa.calificaciones.providers.AuthProvider
import mx.edu.unpa.calificaciones.providers.UsuarioProvider

class MainActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var pass:EditText

    private val authProvider: AuthProvider=AuthProvider()
    private val usuarioProvider: UsuarioProvider=UsuarioProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        if(authProvider.exitsSession()){
              val intent=Intent(this, HomeActivity::class.java)
            //val intent=Intent(this, BienvenidaActivity::class.java)


            startActivity(intent)
        }

    }


    // Login con stId = student ID (Matricula) y pass
    fun login(view: View){
        email=findViewById(R.id.txtEmail)
        pass=findViewById(R.id.txtContraseña)
        Log.d("FIREBASE","Email:$(email.text.toString()}");
        Log.d("FIREBASE","Pass:$(pass.text.toString()}");

        if (isValidForm(email.text.toString(),pass.text.toString())){
            usuarioProvider.login(email.text.toString(),pass.text.toString()).addOnCompleteListener{
                if (it.isSuccessful){
                    // val intent= Intent(this, HomeActivity::class.java)
                    val intent=Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Error al iniciar sesión",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun isValidForm(email:String,pass:String):Boolean{
        if(email.isEmpty()){
            Toast.makeText(this,"Ingresar tu correo electronico",Toast.LENGTH_LONG).show()
            return false
        }
        if(pass.isEmpty()){
           Toast.makeText(this,"Ingresa tu contraseña",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    fun registrar(view: View) {

        val intent = Intent(this, RegistrarActivity::class.java)
        startActivity(intent);
    }


}