package com.example.jdplus.FragmentsCliente;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jdplus.DatosConsulta;
import com.example.jdplus.R;
import com.example.jdplus.Sqlite.CreaBDSqlite;
import com.example.jdplus.adaptadores.AdapterConsultas;
import com.example.jdplus.objetos.Consultas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MainFragmentCliente extends Fragment {


    RecyclerView recyclerView;
    ArrayList<Consultas> listMostrar;
    AdapterConsultas adaptador;




    String url ="http://192.168.100.59:8080/proyecto/cliente/obtener_consultas_adulto.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       listMostrar = new ArrayList<Consultas>();
        View view = inflater.inflate(R.layout.cliente_main_fragment,container,false);

        CreaBDSqlite creaBDSqlite = new CreaBDSqlite(getContext());
        SQLiteDatabase db = creaBDSqlite.getWritableDatabase();

        recyclerView = view.findViewById(R.id.recicler_consultas);

        //obtenerDatos(url);


        return view;
    }

    public void obtenerDatos(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        String id = jsonObject.getString("Id");
                        String fecha_hora = jsonObject.getString("fhec_hora");
                        String tipo_consulta = jsonObject.getString("tipo_consulta");
                        String doctor = jsonObject.getString("doctora");
                        String fecha_cita = jsonObject.getString("fehca_cita");
                        String hora_cita = jsonObject.getString("hora_cita");
                        String genero = jsonObject.getString("genero");
                        String nombre = jsonObject.getString("nombre");
                        String fecha_nac = jsonObject.getString("fecha_nac");
                        String edad = jsonObject.getString("edad");
                        String tipo_sangre = jsonObject.getString("tipo_sangre");
                        String telefono = jsonObject.getString("telefono");
                        String correo = jsonObject.getString("correo");
                        String enfermedad = jsonObject.getString("enfermedad");
                        String enfermedad_info = jsonObject.getString("enfermedad_info");
                        String medicacion = jsonObject.getString("medicacion");
                        String info_medicacion = jsonObject.getString("info_medicacion");
                        String pago = jsonObject.getString("pa;");
                        String cantidad = jsonObject.getString("cantidad");
                        String clave = jsonObject.getString("clave");




                        listMostrar.add(new Consultas(id, fecha_hora, tipo_consulta, doctor, fecha_cita, hora_cita, genero, nombre, fecha_nac,
                                edad, tipo_sangre, telefono, correo, enfermedad, enfermedad_info, medicacion, info_medicacion, pago, cantidad, clave));
                    } catch (JSONException e) {

                        Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                mostrarData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }



    public void mostrarData (){

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador = new AdapterConsultas(getContext(), listMostrar);
        recyclerView.setAdapter(adaptador);
        adaptador.setonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),listMostrar.get(recyclerView.getChildAdapterPosition(v)).getNombre(),Toast.LENGTH_SHORT).show();
                String tipoConsulta = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getTipo_consulta();
                String fechaHora = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getFecha_hora();
                String fechaCita = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getFecha_cita();
                String horaCita = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getHora_cita();
                String nombrePaciente = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getNombre();
                String edadPaciente = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getEdad();
                String tipoSangre = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getTipo_sangre();
                String enfermedades = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getEnfermedad();
                String infoEnfermedades = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getEnfermedad_info();
                String medicacion = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getMedicacion();
                String infoMedicacion = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getInfo_medicacion();
                String pago = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getPago();
                String cantidad = listMostrar.get(recyclerView.getChildAdapterPosition(v)).getCantidad();


                Intent intent = new Intent( getContext(), DatosConsulta.class);
                intent.putExtra("especialidad", tipoConsulta);
                intent.putExtra("fechaHora", fechaHora);
                intent.putExtra("fechaCita", fechaCita);
                intent.putExtra("horaCita", horaCita);
                intent.putExtra("nombrePaciente", nombrePaciente);
                intent.putExtra("edadPaciente", edadPaciente);
                intent.putExtra("tipoSangre", tipoSangre);
                intent.putExtra("enfermedades", enfermedades);
                intent.putExtra("infoEnfermedades", infoEnfermedades);
                intent.putExtra("medicacion", medicacion);
                intent.putExtra("infoMedicacion", infoMedicacion);
                intent.putExtra("pago", pago);
                intent.putExtra("cantidad", cantidad);
                startActivity(intent);
            }
        });

    }
}
