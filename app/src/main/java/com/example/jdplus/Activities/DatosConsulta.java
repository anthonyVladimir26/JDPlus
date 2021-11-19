package com.example.jdplus.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jdplus.EmpezarVideollamada;
import com.example.jdplus.R;
import com.example.jdplus.adaptadores.AdapterVideollamada;
import com.example.jdplus.listeners.UsersListeners;
import com.example.jdplus.objetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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



    String tipo,id,tipoUsuario,claveUsuario;

    FirebaseFirestore firestore;

    private List<Usuario> usuarios;

    RecyclerView recyclerViewVideollamada;

    AdapterVideollamada adapterVideollamada;

    String claveCliente,claveDoctor;

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

        firestore = FirebaseFirestore.getInstance();

        usuarios = new ArrayList<>();


        recyclerViewVideollamada = findViewById(R.id.reciclerVideollamada);

        SharedPreferences sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);

        tipoUsuario =sharedPreferences.getString("tipo","");
        claveUsuario = sharedPreferences.getString("clave","");

        if (tipoUsuario.equals("doctor")) recyclerViewVideollamada.setVisibility(View.VISIBLE);
        else recyclerViewVideollamada.setVisibility(View.INVISIBLE);


        //jalamos datos de la pantalla anterior
        tipo= getIntent().getStringExtra("tipo");
        id= getIntent().getStringExtra("id");



        if (tipo.equals("0")){
            datosConsultaInfantil("http://"+getString(R.string.ip)+"/proyecto/cliente/obtener_consultas_nino.php?id="+id);
        }
        else{
            datosConsultaAdulto("http://"+getString(R.string.ip)+"/proyecto/cliente/obtener_consultas_adulto.php?id="+id);
        }

        firestore =FirebaseFirestore.getInstance();

        adapterVideollamada =new AdapterVideollamada(getApplicationContext(),usuarios, this);
        recyclerViewVideollamada.setAdapter(adapterVideollamada);


        getUser();

    }

    public void  getUser(){
        firestore.collection("usuarios" )
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {


                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            String claves = documentSnapshot.getString("clave");

                            if (claves.equals(claveCliente)) {

                                String usuario = documentSnapshot.getString("usuario");
                                String nombre = documentSnapshot.getString("nombre");
                                String fcm_token = documentSnapshot.getString("fcm_token");

                                usuarios.add(new Usuario(usuario,nombre,fcm_token));
                            }

                        }

                        adapterVideollamada.notifyDataSetChanged();


                    }
                });

    }

    public void datosConsultaAdulto(String URL) {


        //srlRecargaConsultas.setRefreshing(true);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, response -> {
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);

                    claveCliente = jsonObject.getString("clave_cliente");
                    claveDoctor = jsonObject.getString("clave_doctor");

                    String especialidad = jsonObject.getString("tipo_consulta");
                    String fechaHora = jsonObject.getString("fecha_hora");
                    String fechaCita = jsonObject.getString("fehca_cita");
                    String horaCita = jsonObject.getString("hora_cita");
                    String nombrePaciente = jsonObject.getString("nombre");
                    String edadPaciente = jsonObject.getString("edad");
                    String tipoSangre = jsonObject.getString("tipo_sangre");
                    String enfermedades = jsonObject.getString("enfermedad_info");
                    String medicacion = jsonObject.getString("info_medicacion");
                    String costo = jsonObject.getString("cantidad");

                    tvEspecialidad.setText(especialidad);
                    tvfechaHora.setText(fechaHora);
                    tvfechaCita.setText(fechaCita);
                    tvhoraCita.setText(horaCita);
                    tvnombrePaciente.setText(nombrePaciente);
                    tvedadPaciente.setText(edadPaciente);
                    tvtipoSangre.setText(tipoSangre);
                    tvenfermedades.setText(enfermedades);
                    tvmedicacion.setText(medicacion);
                    tvpago.setText(costo);


                } catch (JSONException e) {
                    Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }


        }, error -> Toast.makeText(this, error.getMessage(),Toast.LENGTH_SHORT).show());


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


    }

    public void datosConsultaInfantil(String URL){
        Toast.makeText(this, "es una consulta infantil", Toast.LENGTH_SHORT).show();


        //srlRecargaConsultas.setRefreshing(true);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, response -> {
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);

                    claveCliente = jsonObject.getString("claveCliente");
                    claveDoctor = jsonObject.getString("clave_doctor");



                    String especialidad = jsonObject.getString("tipo_consulta");
                    String fechaHora = jsonObject.getString("fecha_hora");
                    String fechaCita = jsonObject.getString("fehca_cita");
                    String horaCita = jsonObject.getString("hora_cita");
                    String nombrePaciente = jsonObject.getString("nombre_nino");
                    String edadPaciente = jsonObject.getString("edad");
                    String tipoSangre = jsonObject.getString("tipo_sangre");
                    String enfermedades = jsonObject.getString("info_enfer_nino");
                    String medicacion = jsonObject.getString("medicacion_nino");
                    String costo = jsonObject.getString("cantidad");

                    tvEspecialidad.setText(especialidad);
                    tvfechaHora.setText(fechaHora);
                    tvfechaCita.setText(fechaCita);
                    tvhoraCita.setText(horaCita);
                    tvnombrePaciente.setText(nombrePaciente);
                    tvedadPaciente.setText(edadPaciente);
                    tvtipoSangre.setText(tipoSangre);
                    tvenfermedades.setText(enfermedades);
                    tvmedicacion.setText(medicacion);
                    tvpago.setText(costo);



                } catch (JSONException e) {

                    Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }






        }, error -> Toast.makeText(this, error.getMessage(),Toast.LENGTH_SHORT).show());


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

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