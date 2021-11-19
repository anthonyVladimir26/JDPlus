package com.example.jdplus.FragmentsAsistente;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jdplus.Activities.GenerarConsulta;
import com.example.jdplus.R;
import com.example.jdplus.adaptadores.AdapterConsulta;
import com.example.jdplus.objetos.Consultas;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConsultasFragmentAsistente extends Fragment {

    //vinculamos con los componentes de la interfaz
    SwipeRefreshLayout srlRecargaConsultas;
    RecyclerView rvConsultas;

    //hacemos una lista con el objeto creado Consultas
    ArrayList<Consultas> listaConsultas;

    //llamamos al adapter de la consulta
    AdapterConsulta adapterConsulta;

    //creamos la variable url donde se aloja nuestro archivo php
    String url;

    //FloatingActionButton btnAnadirConsulta;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.asistente_consulta_fragment,container,false);


        //en nuestra variable url ingresamos la url donde se aloja nueestro archivo php
        url ="http://"+getString(R.string.ip)+"/proyecto/doctor/obtener_consultas_asistentes.php";

        //llamamos los componentes del layout
        srlRecargaConsultas = view.findViewById(R.id.refresh_consulta_asistente);
        rvConsultas = view.findViewById(R.id.recicler_consultas_asistente);

        //btnAnadirConsulta = view.findViewById(R.id.boton_crear_consulta_asistente);

        //inicializamos la lista de la consulta
        listaConsultas = new ArrayList<>();


        //inicializamos el refresh layout para que inicie con el
        srlRecargaConsultas.setOnRefreshListener(() -> obtenerDatos(url));

        view.findViewById(R.id.boton_crear_consulta_asistente).setOnClickListener(v -> {
            menuElegirTipoConsulta();
        });

        //mandamos a llamar al metodo obtener datos con la variable url
        obtenerDatos(url);

        return view;
    }



    public void menuElegirTipoConsulta(){

         CharSequence[] opciones = {"Consulta infantil", "Consulta para adultos", "cancelar"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (opciones[which].equals("Consulta infantil")){
                            Toast.makeText(getContext(), "Consulta infantil", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getContext(),GenerarConsulta.class);

                            intent.putExtra("tipo","infantil");

                            startActivity(intent);

                        }
                        else if (opciones[which].equals("Consulta para adultos")){
                            Toast.makeText(getContext(), "Consulta para adultos", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getContext(),GenerarConsulta.class);

                            intent.putExtra("tipo","adulto");

                            startActivity(intent);
                        }


                        else {
                            dialog.dismiss();

                        }

                    }
                }

        );

        builder.show();

    }

    public void obtenerDatos(String URL){
        srlRecargaConsultas.setRefreshing(true);
        listaConsultas.clear();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, response -> {
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);

                    String nombreCliente = jsonObject.getString("nombreCliente");
                    String nombreDoctor = jsonObject.getString("nombreDoctor");
                    String especialidad = jsonObject.getString("especialidad");
                    String fechaHoraCreada = jsonObject.getString("fechaHoraCreada");
                    String fechaConsulta = jsonObject.getString("fechaConsulta");
                    String horaConsulta = jsonObject.getString("horaConsulta");
                    String claveUsuario = jsonObject.getString("claveUsuario");
                    String claveDoctor = jsonObject.getString("claveDoctor");
                    String tipo = jsonObject.getString("tipo");
                    String estatus = jsonObject.getString("estatus");
                    String idConsulta = jsonObject.getString("idConsulta");


                    listaConsultas.add(new Consultas(nombreCliente,nombreDoctor,especialidad,fechaHoraCreada
                            ,fechaConsulta,horaConsulta,claveUsuario,claveDoctor,tipo,estatus,idConsulta));
                } catch (JSONException e) {

                    Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            srlRecargaConsultas.setRefreshing(false);

            rvConsultas.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterConsulta = new AdapterConsulta(getContext(), listaConsultas);
            rvConsultas.setAdapter(adapterConsulta);


        }, error -> Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_SHORT).show());


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }



}
