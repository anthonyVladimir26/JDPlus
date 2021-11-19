package com.example.jdplus.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.jdplus.R;
import com.example.jdplus.ScaleListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagenChatActivity extends AppCompatActivity {


    Toolbar toolbar;
    ProgressDialog progressDialog;

    TextView nombre,horaYdia;
    PhotoView imagenAmpliadoChat;
    ImageView imgRegresar;


    String urlImagen,claveEnvia;
    Long hora;

    String claveUsuarioActual;


    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_chat);




        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cangando...");
        progressDialog.setCancelable(false);

        SimpleDateFormat formatoHora= new SimpleDateFormat("hh:mm a");
        SimpleDateFormat formatoDia = new SimpleDateFormat("dd/MM/yy");



        toolbar= findViewById(R.id.toolbar2);
        nombre = findViewById(R.id.nombreEnviaImagen);
        horaYdia = findViewById(R.id.fecheYhoraImagen);
        imagenAmpliadoChat =findViewById(R.id.imagenGrande);
        //photoViewAttacher = new PhotoViewAttacher(imagenAmpliadoChat);
        imgRegresar = findViewById(R.id.imageRegresarImagenChat);

        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        claveUsuarioActual  =sharedPreferences.getString("clave",null);

        urlImagen = getIntent().getStringExtra("urlImagen");
        hora = getIntent().getLongExtra("hora",1);
        claveEnvia = getIntent().getStringExtra("claveEnvia");

        Glide.with(this)
                .load(urlImagen)
                .placeholder(R.mipmap.placeholder)
                .into(imagenAmpliadoChat);


        imgRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        firebaseFirestore =FirebaseFirestore.getInstance();

        Date date = new Date();

        String diaActual = formatoDia.format(new Date(date.getTime()));


        String horaMensaje = formatoHora.format(new Date(hora));

        String diaMensaje = formatoDia.format(new Date(hora));


        if (diaActual.equals(diaMensaje)){
            horaYdia.setText("Hoy "+horaMensaje);
        }
        else {
            horaYdia.setText(diaMensaje+" "+horaMensaje);
        }


         if (claveEnvia.equals(claveUsuarioActual)){
            nombre.setText("Tú");
        }
        else {
            firebaseFirestore.collection("usuarios")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                String clave = documentSnapshot.getString("clave");



                                if (clave.equals(claveEnvia)){

                                    String name = documentSnapshot.getString("nombre");
                                    nombre.setText(name);

                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "error" + e.getMessage(), Toast.LENGTH_SHORT).show());
        }





    }





}