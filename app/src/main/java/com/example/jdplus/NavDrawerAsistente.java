package com.example.jdplus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jdplus.FragmentsAsistente.FragmentHistorialClinico;
import com.example.jdplus.FragmentsAsistente.MainFragmentAsistente;
import com.example.jdplus.FragmentsCliente.MainFragmentCliente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Document;

import java.util.HashMap;

public class NavDrawerAsistente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        String nombre ;
        String id ;
        boolean sesion ;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Toolbar toolbar;

    //cambiar de fragments variables

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //variables de ChatMensajesFragments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_asistente);

        SharedPreferences sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);

         nombre =sharedPreferences.getString("nombre","false");
         id = sharedPreferences.getString("id","");
         sesion = sharedPreferences.getBoolean("sesion",false);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);



        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        //cargar Fragments

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new MainFragmentAsistente());
        fragmentTransaction.commit();
        toolbar.setTitle("consultas");

        //crear token fcm
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful() && task.getResult()!= null) {
                            // Get new FCM registration token
                            String token = task.getResult();

                            Toast.makeText(NavDrawerAsistente.this, token, Toast.LENGTH_SHORT).show();
                            sendFCMTokenToDatabase(token);
                        }


                    }
                });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        if (item.getItemId() == R.id.consultas) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new MainFragmentAsistente());
            fragmentTransaction.commit();

            toolbar.setTitle("consultas");
        }

        if (item.getItemId() == R.id.historial) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new FragmentHistorialClinico());
            fragmentTransaction.commit();
            toolbar.setTitle("chat");
        }

        if (item.getItemId()  == R.id.C_asistente ){
            AlertDialog.Builder alerta =  new AlertDialog.Builder(this);
            alerta.setMessage("¿Desea cerrar sesion?")
                    .setCancelable(true)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            cerrarSesion();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Cerrar Sesion");
            titulo.show();

        }
        return false;
    }

    public void cerrarSesion(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("usuarios").document(id);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("fcm_token", FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(aVoid -> {
                    guardarDatosUsuarioActual(null, null,null,null,false);

                    Intent intentAsistente = new Intent(NavDrawerAsistente.this, PresentacionActivity.class);
                    startActivityForResult(intentAsistente,0);
                })
                .addOnFailureListener(e -> Toast.makeText(NavDrawerAsistente.this, "error "+e.getMessage()+"al cerrar sesion", Toast.LENGTH_SHORT).show());

    }

        public void guardarDatosUsuarioActual(String id ,String usuario ,String tipoUsuario, String clave, boolean sesionIniciada) {
            SharedPreferences sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id", id);
            editor.putString("usuario", usuario);
            editor.putString("tipo", tipoUsuario);
            editor.putString("clave",clave);
            editor.putBoolean("sesion", sesionIniciada);
            editor.apply();

        }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode== event.KEYCODE_BACK){

            if (!sesion){
                cerrarSesion();
            }

            finishAffinity();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void sendFCMTokenToDatabase(String token){


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("asistentes").document(id);
        documentReference.update("fcm_token",token)
                .addOnSuccessListener(aVoid -> {
                    //Toast.makeText(this, "creado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(this, "no se pudo crear por: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });



    }



    }
