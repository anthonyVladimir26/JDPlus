package com.example.jdplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class PresentacionActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);

        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
                boolean sesion = preferences.getBoolean("sesion",false);
                String tipo = preferences.getString("tipo","");

                if (sesion){
                    entrarInterfaz(tipo);
                }
                else{
                    entrarInterfaz("main");
                }
            }
        },500);
    }
    public void entrarInterfaz (String tipo){
        if (tipo.equals("usuario")){
            Intent intentCliente = new Intent(this,NavDrawerCliente.class);
            startActivityForResult(intentCliente,0);}
        if (tipo.equals("doctor")){
            Intent intentDoctor = new Intent(this,NavDrawerDoctor.class);
            startActivityForResult(intentDoctor,0);}
        if (tipo.equals("Asistente")){
            Intent intentAsistente = new Intent(this,NavDrawerAsistente.class);
            startActivityForResult(intentAsistente,0);}

        if (tipo.equals("main")){
            Intent intentAsistente = new Intent(this,MainActivity.class);
            startActivityForResult(intentAsistente,0);}
    }

}