package com.example.jdplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jdplus.adaptadores.AdapterVideollamada;
import com.example.jdplus.listeners.UsersListeners;
import com.example.jdplus.objetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DatosConsulta extends AppCompatActivity implements UsersListeners{
    EditText tvEspecialidad;
    EditText tvfechaHora;
    EditText tvfechaCita;
    EditText tvhoraCita;
    EditText tvnombrePaciente;
    EditText tvedadPaciente;
    EditText tvtipoSangre;
    EditText tvenfermedades;
    EditText tvmedicacion;
    EditText tvpago;
    EditText tvDoctor;

    RecyclerView recyclerViewVideollamada;


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
    String clave;


    SharedPreferences sharedPreferences;
    String tipo;


    UsersListeners usersListeners;
    FirebaseFirestore firestore;

    private List<Usuario> usuarios;

    AdapterVideollamada adapterVideollamada;
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



        usuarios = new ArrayList<>();
        //vinculamos y damos funcionamiento al boton


        recyclerViewVideollamada = findViewById(R.id.reciclerVideollamada);




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
        clave = getIntent().getStringExtra("clave");

        getSupportActionBar().setTitle("Consulta de "+ nombrePaciente);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.browser_actions_bg_grey)));



        Toast.makeText(this, clave, Toast.LENGTH_SHORT).show();
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


        sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        tipo=sharedPreferences.getString("tipo",null);

        if (tipo.equals("doctor")){
            recyclerViewVideollamada.setVisibility(View.VISIBLE);
        }


        firestore =FirebaseFirestore.getInstance();

        adapterVideollamada =new AdapterVideollamada(getApplicationContext(),usuarios, this);
        recyclerViewVideollamada.setAdapter(adapterVideollamada);

        getUser();


    }

    public void  getUser(){
        firestore.collection("usuarios" )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {


                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                String claves = documentSnapshot.getString("clave");

                                if (claves.equals(clave)) {

                                    String usuario = documentSnapshot.getString("usuario");
                                    String nombre = documentSnapshot.getString("nombre");
                                    String fcm_token = documentSnapshot.getString("fcm_token");

                                    usuarios.add(new Usuario(usuario,nombre,fcm_token));

                                }

                            }
                            adapterVideollamada.notifyDataSetChanged();



                        }
                    }
                });

    }



    @Override
    public void initiateVideoMeeting(@NotNull Usuario usuario) {
        if (usuario.getFcm_token() == null || usuario.getFcm_token().isEmpty()){
            Toast.makeText(getApplicationContext(), usuario .getNombre()+" no esta disponible", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(getApplicationContext(), CrearVideollamada.class);
            intent.putExtra("usuario", usuario);
            intent.putExtra("tipo","video");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }
}