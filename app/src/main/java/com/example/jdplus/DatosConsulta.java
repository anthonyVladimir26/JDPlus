package com.example.jdplus;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DatosConsulta extends AppCompatActivity {

    TextView tvEspecialidad;
    TextView tvfechaHora;
    TextView tvfechaCita;
    TextView tvhoraCita;
    TextView tvnombrePaciente;
    TextView tvedadPaciente;
    TextView tvtipoSangre;
    TextView tvenfermedades;
    TextView tvmedicacion;
    TextView tvpago;


    Button regresar;


    //recojo los datos del fragment
    String especialidad;
    String fechaHora;
    String fechaCita;
    String horaCita;
    String nombrePaciente;
    String edadPaciente;
    String tipoSangre;
    String enfermedades;
    String infoEnfermedades;
    String medicacion;
    String infoMedicacion;
    String pago;
    String cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_consulta_activity);

        //vinculamos los texview
        tvEspecialidad = findViewById(R.id.tvEspecialidad);
        tvfechaHora = findViewById(R.id.fechaHora);
        tvfechaCita = findViewById(R.id.fechaCita);
        tvhoraCita = findViewById(R.id.horaCita);
        tvnombrePaciente = findViewById(R.id.nombrePaciente);
        tvedadPaciente = findViewById(R.id.edadPaciente);
        tvtipoSangre = findViewById(R.id.tipoSngre);
        tvenfermedades = findViewById(R.id.enfermedades);
        tvmedicacion = findViewById(R.id.medicacion);
        tvpago = findViewById(R.id.pago);

        //vinculamos y damos funcionamiento al boton
        regresar = findViewById(R.id.botonRegresar);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //asignamos datos del fragment
        especialidad = getIntent().getStringExtra("especialidad");
        fechaHora = getIntent().getStringExtra("fechaHora");
        fechaCita = getIntent().getStringExtra("fechaCita");
        horaCita = getIntent().getStringExtra("horaCita");
        nombrePaciente = getIntent().getStringExtra("nombrePaciente");
        edadPaciente = getIntent().getStringExtra("edadPaciente");
        tipoSangre = getIntent().getStringExtra("tipoSangre");
        enfermedades = getIntent().getStringExtra("enfermedades");
        infoEnfermedades = getIntent().getStringExtra("infoEnfermedades");
        medicacion = getIntent().getStringExtra("medicacion");
        infoMedicacion = getIntent().getStringExtra("infoMedicacion");
        pago = getIntent().getStringExtra("pago");
        cantidad = getIntent().getStringExtra("cantidad");

        //mandamos al activity
        tvEspecialidad.setText(especialidad);
        tvfechaHora.setText(fechaHora);
        tvfechaCita.setText(fechaCita);
        tvhoraCita.setText(horaCita);
        tvnombrePaciente.setText(nombrePaciente);
        tvedadPaciente.setText(edadPaciente);
        tvtipoSangre.setText(tipoSangre);
        if (enfermedades.equals("no")){
            tvenfermedades.setText("sin regitro");
        }
        else {
            tvenfermedades.setText(infoEnfermedades);
        }

        if (medicacion.equals("no")){
            tvmedicacion.setText("sin registro");
        }
        else {
            tvmedicacion.setText(infoMedicacion);
        }
        if (pago.equals("si")){
            tvpago.setText("pagado");
        }
        else {
            tvpago.setText(cantidad);
        }



    }
}