package com.example.jdplus.FragmentsAsistente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jdplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class FragmentHistorialClinico extends Fragment {

    EditText usuario, tipo,nombre,id,contra,clave;
    Button anadir;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.asistente_historial_clinico_fragment,container,false);

        usuario = view.findViewById(R.id.usuario);
        tipo = view.findViewById(R.id.tipo);
        nombre = view.findViewById(R.id.nombre);
        contra = view.findViewById(R.id.contra);
        usuario = view.findViewById(R.id.usuario);
        clave = view.findViewById(R.id.clave);

        anadir= view.findViewById(R.id.anadir);

        anadir.setOnClickListener(v -> {

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            HashMap<String, Object> usuarios = new HashMap<>();
            usuarios.put("nombre","Manuel Angel Rivera Martinez");
            usuarios.put("usuario", "Man");
            usuarios.put("contra", "Man285");
            usuarios.put("correo","anyel_rmtz26@hotmail.com");
            database.collection("usuarios")
                    .add(usuarios)
                    .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "usuario insertado", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "usuario no insertado por: "+e.getMessage(), Toast.LENGTH_SHORT).show());

        });


        return view;
    }
}
