package com.example.jdplus.FragmentsDoctor;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.jdplus.CrearVideollamada;
import com.example.jdplus.R;
import com.example.jdplus.adaptadores.AdapterUsuarios;
import com.example.jdplus.listeners.UsersListeners;
import com.example.jdplus.objetos.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainFragmentDoctor extends Fragment implements UsersListeners {

    RecyclerView recyclerView;

    private List<Usuario> usuarios;
    private AdapterUsuarios adapterUsuarios;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_main_fragment,container,false);

        recyclerView = view.findViewById(R.id.chatRecyclerView);


        usuarios = new ArrayList<>();
        adapterUsuarios = new AdapterUsuarios(getContext(),usuarios, this);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapterUsuarios);




        getUsers();

        return view;
    }

    private void getUsers(){


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("usuarios")
            .get()
                .addOnCompleteListener(task -> {



                    if (task.isSuccessful() && task.getResult() != null){


                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                            String nombre = documentSnapshot.getString("nombre");
                            String usuario = documentSnapshot.getString("usuario");
                            String token= documentSnapshot.getString("fcm_token");

                            usuarios.add(new Usuario(usuario,nombre,token));

                        }
                        if(usuarios.size() >0){
                            adapterUsuarios.notifyDataSetChanged();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void initiateVideoMeeting(Usuario usuario) {
        if (usuario.getFcm_token() == null || usuario.getFcm_token().isEmpty()){
            Toast.makeText(getContext(), usuario.getNombre()+" no esta disponible", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(getContext(), CrearVideollamada.class);
            intent.putExtra("usuario", usuario);
            intent.putExtra("tipo","video");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }
}
