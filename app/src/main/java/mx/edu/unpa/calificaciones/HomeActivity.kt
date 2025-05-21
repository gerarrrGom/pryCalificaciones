package mx.edu.unpa.calificaciones

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.edu.unpa.calificaciones.models.Alumno
import mx.edu.unpa.calificaciones.models.Calificacion
import mx.edu.unpa.calificaciones.models.Carrera
import mx.edu.unpa.calificaciones.models.Materia
import mx.edu.unpa.calificaciones.models.PlanDeEstudios
import mx.edu.unpa.calificaciones.providers.AlumnoCallback
import mx.edu.unpa.calificaciones.providers.AlumnoProvider
import mx.edu.unpa.calificaciones.providers.UsuarioProvider
import java.time.LocalDate

class HomeActivity : AppCompatActivity() {



    private lateinit var txtAsignatura1: TextView
    private lateinit var txtParAsig1: TextView
    private lateinit var txtPar2Asig1: TextView
    private lateinit var txtPar3Asig1: TextView
    private lateinit var txtPPAsig1: TextView
    private lateinit var txtOAsig1: TextView
    private lateinit var txtPFAsig1: TextView

    private lateinit var txtAsignatura2: TextView
    private lateinit var txtParAsig2: TextView
    private lateinit var txtPar2Asig2: TextView
    private lateinit var txtPar3Asig2: TextView
    private lateinit var txtPPAsig2: TextView
    private lateinit var txtOAsig2: TextView
    private lateinit var txtPFAsig2: TextView

    private lateinit var txtAsignatura3: TextView
    private lateinit var txtParAsig3: TextView
    private lateinit var txtPar2Asig3: TextView
    private lateinit var txtPar3Asig3: TextView
    private lateinit var txtPPAsig3: TextView
    private lateinit var txtOAsig3: TextView
    private lateinit var txtPFAsig3: TextView

    private lateinit var txtAsignatura4: TextView
    private lateinit var txtParAsig4: TextView
    private lateinit var txtPar2Asig4: TextView
    private lateinit var txtPar3Asig4: TextView
    private lateinit var txtPPAsig4: TextView
    private lateinit var txtOAsig4: TextView
    private lateinit var txtPFAsig4: TextView

    private lateinit var txtAsignatura5: TextView
    private lateinit var txtParAsig5: TextView
    private lateinit var txtPar2Asig5: TextView
    private lateinit var txtPar3Asig5: TextView
    private lateinit var txtPPAsig5: TextView
    private lateinit var txtOAsig5: TextView
    private lateinit var txtPFAsig5: TextView

    private lateinit var usuarioProvider: UsuarioProvider
    private lateinit var alumnoProvider: AlumnoProvider
    // private lateinit var alumno: Alumno
    private var alumno: Alumno? = null

    //private lateinit var calificacion: Calificacion
    //private lateinit var carrera: Carrera

    private lateinit var spinnerCiclos: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // Ajuste de insets para Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar vistas después de setContentView

        spinnerCiclos = findViewById<Spinner>(R.id.spinnerCiclos)

        txtAsignatura1 = findViewById(R.id.txtAsignatura1)
        txtParAsig1    = findViewById(R.id.txtParAsig1)
        txtPar2Asig1   = findViewById(R.id.txtPar2Asig1)
        txtPar3Asig1   = findViewById(R.id.txtPar3Asig1)
        txtPPAsig1     = findViewById(R.id.txtPPAsig1)
        txtOAsig1      = findViewById(R.id.txtOAsig1)
        txtPFAsig1     = findViewById(R.id.txtPFAsig1)

        txtAsignatura2 = findViewById(R.id.txtAsignatura2)
        txtParAsig2    = findViewById(R.id.txtParAsig2)
        txtPar2Asig2   = findViewById(R.id.txtPar2Asig2)
        txtPar3Asig2   = findViewById(R.id.txtPar3Asig2)
        txtPPAsig2     = findViewById(R.id.txtPPAsig2)
        txtOAsig2      = findViewById(R.id.txtOAsig2)
        txtPFAsig2     = findViewById(R.id.txtPFAsig2)

        txtAsignatura3 = findViewById(R.id.txtAsignatura3)
        txtParAsig3    = findViewById(R.id.txtParAsig3)
        txtPar2Asig3   = findViewById(R.id.txtPar2Asig3)
        txtPar3Asig3   = findViewById(R.id.txtPar3Asig3)
        txtPPAsig3     = findViewById(R.id.txtPPAsig3)
        txtOAsig3      = findViewById(R.id.txtOAsig3)
        txtPFAsig3     = findViewById(R.id.txtPFAsig3)

        txtAsignatura4 = findViewById(R.id.txtAsignatura4)
        txtParAsig4    = findViewById(R.id.txtParAsig4)
        txtPar2Asig4   = findViewById(R.id.txtPar2Asig4)
        txtPar3Asig4   = findViewById(R.id.txtPar3Asig4)
        txtPPAsig4     = findViewById(R.id.txtPPAsig4)
        txtOAsig4      = findViewById(R.id.txtOAsig4)
        txtPFAsig4     = findViewById(R.id.txtPFAsig4)

        txtAsignatura5 = findViewById(R.id.txtAsignatura5)
        txtParAsig5    = findViewById(R.id.txtParAsig5)
        txtPar2Asig5   = findViewById(R.id.txtPar2Asig5)
        txtPar3Asig5   = findViewById(R.id.txtPar3Asig5)
        txtPPAsig5     = findViewById(R.id.txtPPAsig5)
        txtOAsig5      = findViewById(R.id.txtOAsig5)
        txtPFAsig5     = findViewById(R.id.txtPFAsig5)

        // Inicializar provider
        alumnoProvider = AlumnoProvider()
        usuarioProvider = UsuarioProvider()

        alumnoProvider.obtenerAlumnoPorId(usuarioProvider.getId(), object : AlumnoCallback {
            override fun onSuccess(alumno: Alumno) {
                println("Alumno obtenido: ${alumno.toJson()}")
                this@HomeActivity.alumno = alumno
                llenarDatos()
                Spinner()
            }

            override fun onFailure(exception: Exception) {
                println("Error al obtener alumno: ${exception.message}")
            }
        })
        activarEventList()
    }


    private fun llenarDatos() {
        // Datos generales

        val materias = alumno!!.materia ?: emptyList()

        // Función auxiliar para llenar una fila
        fun cargarAsignatura(index: Int, nombre: TextView, par1: TextView, par2: TextView, par3: TextView, pp: TextView, o: TextView, pf: TextView) {
            val materia = materias.getOrNull(index)
            var cicloSeleccionado = getCicloEscolar().first()
            if (spinnerCiclos.selectedItem!=null){
                cicloSeleccionado = spinnerCiclos.selectedItem.toString()
            }
            Toast.makeText(this,"filtrando por ciclo : $cicloSeleccionado", Toast.LENGTH_LONG).show()
            if(materia?.cicloEscolar == cicloSeleccionado)
                    nombre.text = materia?.nombre ?: ""
                    par1.text = materia?.calificacion?.parcial1 ?: ""
                    par2.text = materia?.calificacion?.parcial2 ?: ""
                    par3.text = materia?.calificacion?.parcial3 ?: ""
                    pp.text = materia?.calificacion?.promedio ?: ""
                    o.text = materia?.calificacion?.final ?: ""
                    pf.text = materia?.calificacion?.definitivo ?: ""
        }

        // Llenar datos de cada asignatura
        cargarAsignatura(0, txtAsignatura1, txtParAsig1, txtPar2Asig1, txtPar3Asig1, txtPPAsig1, txtOAsig1, txtPFAsig1)
        cargarAsignatura(1, txtAsignatura2, txtParAsig2, txtPar2Asig2, txtPar3Asig2, txtPPAsig2, txtOAsig2, txtPFAsig2)
        cargarAsignatura(2, txtAsignatura3, txtParAsig3, txtPar2Asig3, txtPar3Asig3, txtPPAsig3, txtOAsig3, txtPFAsig3)
        cargarAsignatura(3, txtAsignatura4, txtParAsig4, txtPar2Asig4, txtPar3Asig4, txtPPAsig4, txtOAsig4, txtPFAsig4)
        cargarAsignatura(4, txtAsignatura5, txtParAsig5, txtPar2Asig5, txtPar3Asig5, txtPPAsig5, txtOAsig5, txtPFAsig5)

    }

    private fun getStudent() {
        alumnoProvider.getStudent("alumnoId", usuarioProvider.getId())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Consulta exitosa", Toast.LENGTH_LONG).show()
                    val documents = task.result.documents
                    if (documents.isNotEmpty()) {
                        val student = documents[0].toObject(Alumno::class.java)
                        Log.d("Firestore", "Estudiante: $student")
                        student?.let { llenarDatos() }
                    }
                } else {
                    Toast.makeText(this, "Consulta fallida: ${task.exception}", Toast.LENGTH_LONG).show()
                    Log.e("Firestore", "Error al obtener estudiante", task.exception)
                }
            }
    }

    private fun Spinner(){
        spinnerCiclos = findViewById(R.id.spinnerCiclos);

        val ciclos = getCicloEscolar() // Tu método para obtener los ciclos
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ciclos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCiclos.adapter = adapter


    }

    private fun getCicloEscolar(): List<String> {
        val ciclos = mutableListOf<String>()
        val hoy = LocalDate.now()

        if(alumno != null)
        {
            val añoIngreso = alumno!!.matricula?.substring(0, 2)!!.toIntOrNull();

            if (añoIngreso != null) {
                val añoInicio = if (añoIngreso > LocalDate.now().year % 100)
                    1900 + añoIngreso
                else
                    2000 + añoIngreso

                val añoActual = hoy.year // LocalDate.now().year

                for (año in añoInicio..añoActual) {
                    val siguienteAño = año + 1

                    // Periodo A: 1 oct año – 28 feb siguiente año
                    val periodoAInicio = LocalDate.of(año, 10, 1)
                    val periodoAFin = LocalDate.of(siguienteAño, 2, 28)
                    if (hoy >= periodoAInicio) {
                        ciclos.add("$año - $siguienteAño A")
                    }

                    // Periodo B: 1 mar siguiente año – 30 jun siguiente año
                    val periodoBInicio = LocalDate.of(siguienteAño, 3, 1)
                    val periodoBFin = LocalDate.of(siguienteAño, 6, 30)
                    if (hoy >= periodoBInicio) {
                        ciclos.add("$año - $siguienteAño B")
                    }

                    // Periodo V: 1 mar siguiente año – 30 sep siguiente año
                    val periodoVInicio = LocalDate.of(siguienteAño, 3, 1)
                    val periodoVFin = LocalDate.of(siguienteAño, 9, 30)
                    if (hoy >= periodoVInicio) {
                        ciclos.add("$año - $siguienteAño V")
                    }
                }
            }
        }

        return ciclos.reversed()
    }
    fun activarEventList() {
        spinnerCiclos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                llenarDatos()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Puedes dejarlo vacío o hacer algo si se desea
            }
        }
    }
}
