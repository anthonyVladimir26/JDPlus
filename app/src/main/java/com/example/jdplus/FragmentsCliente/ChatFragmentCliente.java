package com.example.jdplus.FragmentsCliente;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jdplus.BandejaDeMensajes;
import com.example.jdplus.R;
import com.example.jdplus.Sqlite.CreaBDSqlite;
import com.example.jdplus.Sqlite.SqliteOperacionesUsuarioChat;
import com.example.jdplus.adaptadores.AdapterUsuarios;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatFragmentCliente extends Fragment {

    RecyclerView recyclerView;
  //  ArrayList<Usuarios> listMostrar;
    AdapterUsuarios adaptador;

    SqliteOperacionesUsuarioChat operacionesUsuarioChat;

    boolean mostrarUsuarios = true;
    boolean mandarSqlite = false;

    SharedPreferences preferences;

    String url ;

    String nombre;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cliente_chat_fragment,container,false);


       // listMostrar = new ArrayList<Usuarios>();
        recyclerView = view.findViewById(R.id.chatRecyclerViewCliente);

        preferences = this.getActivity().getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);

        
        nombre = preferences.getString("nombre","");

        operacionesUsuarioChat= new SqliteOperacionesUsuarioChat(getContext());

        Toast.makeText(getContext(),nombre,Toast.LENGTH_SHORT).show();

        url = "http://192.168.100.59:8080/proyecto/mensajes/mostrar_usuarios_chat.php?nombreU="+nombre+"";
        //obtenerDatosMysql(url);

        //mostrarData();
        return view;
    }

  /*  public void obtenerDatosMysql(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String nombrechat = jsonObject.getString("nombrechat");
                        String doctor = jsonObject.getString("doctor");
                        String cliente = jsonObject.getString("cliente");
                        String usuarioDoctor = jsonObject.getString("userDoctor");
                        String usuarioCliente = jsonObject.getString("userCliente");





                        boolean existeUser = operacionesUsuarioChat.nombreUsuario(usuarioDoctor);

                        if (existeUser ==false){
                            insertarBDSqliteUsuario(usuarioDoctor, doctor);
                        }

                        //listMostrar.add(new usuarios(id, usuario, nombre));


                    } catch (JSONException e) {

                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //              Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }


    public void mostrarData() {


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador = new AdapterUsuarios(getContext(), operacionesUsuarioChat.mostrarUsuarios());
        recyclerView.setAdapter(adaptador);
        adaptador.setonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), operacionesUsuarioChat.mostrarUsuarios().get(recyclerView.getChildAdapterPosition(v)).getNombre(), Toast.LENGTH_SHORT).show();

                String id = operacionesUsuarioChat.mostrarUsuarios().get(recyclerView.getChildAdapterPosition(v)).getId()+"";
                String usuario = operacionesUsuarioChat.mostrarUsuarios().get(recyclerView.getChildAdapterPosition(v)).getUsuario();
                String nombre = operacionesUsuarioChat.mostrarUsuarios().get(recyclerView.getChildAdapterPosition(v)).getNombre();

                Intent intent = new Intent(getContext(), BandejaDeMensajes.class);
                intent.putExtra("id", id);
                intent.putExtra("usuario", usuario);
                intent.putExtra("nombre", nombre);
                startActivity(intent);
            }
        });



    }

    public void insertarBDSqliteUsuario (String usuario, String nombre) {


        operacionesUsuarioChat.insertarusuarios_chat(usuario,nombre);

    }
*/
}
