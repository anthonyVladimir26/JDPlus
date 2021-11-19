package com.example.jdplus.FragmentsCliente;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.content.Context;
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
import com.example.jdplus.R;
import com.example.jdplus.adaptadores.AdapterListaChatUsuarios;
import com.example.jdplus.adaptadores.AdapterUsuario;
import com.example.jdplus.objetos.Usuarios;
import com.example.jdplus.objetos.UsuariosMensajes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Stack;

public class ChatFragmentCliente extends Fragment {

    FirebaseDatabase firebaseDatabase;

    String tipo;
    String clave;

    RecyclerView recyclerView;
    ArrayList<UsuariosMensajes> listaUsuarios;
    AdapterListaChatUsuarios adapterListaChatUsuarios;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cliente_chat_fragment,container,false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        clave = sharedPreferences.getString("clave","");
        tipo= sharedPreferences.getString("tipo","");

        firebaseDatabase = FirebaseDatabase.getInstance();
        recyclerView = view.findViewById(R.id.chatRecyclerView);


        listaUsuarios = new ArrayList<>();
        adapterListaChatUsuarios = new AdapterListaChatUsuarios(getContext(),listaUsuarios);
        recyclerView.setAdapter(adapterListaChatUsuarios);

        mostrarDatos();

        return view;
    }


    void mostrarDatos(){
        Query ultimoMensajeQuery=firebaseDatabase.getReference().child("chats").child(clave).orderByChild("horaUltimoMensaje")
                .startAt(1);

        ultimoMensajeQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaUsuarios.removeAll(listaUsuarios);
                Stack<UsuariosMensajes> usuariosMensajesStack  = new Stack<>();





                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String nombre = dataSnapshot.child("nombreUsuario").getValue(String.class);
                    String claveFirebase = dataSnapshot.getKey();
                    String ultimoMensaje = dataSnapshot.child("ultimoMensaje").getValue(String.class);

                    int tipoUltimoMensaje = dataSnapshot.child("tipoUltimoMensaje").getValue(Integer.class);
                    int numeroMensajeNuevos = dataSnapshot.child("numeroMensajeNuevos").getValue(Integer.class);

                    Long horaUltimoMensaje = dataSnapshot.child("horaUltimoMensaje").getValue(Long.class);



                    UsuariosMensajes usuariosMensajes = new UsuariosMensajes(nombre, claveFirebase, ultimoMensaje, tipoUltimoMensaje, horaUltimoMensaje,numeroMensajeNuevos);

                    usuariosMensajesStack.push(usuariosMensajes);
                }

                while (!usuariosMensajesStack.isEmpty()){ listaUsuarios.add(usuariosMensajesStack.pop()); }


                adapterListaChatUsuarios.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
