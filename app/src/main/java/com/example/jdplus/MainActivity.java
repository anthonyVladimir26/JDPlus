package com.example.jdplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.core.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //inicializamos las varibles
    EditText edtUsuario, edtContrasena;
    MaterialButton btnLogin;
    ProgressBar progressBarInicio;

    String id,usuario,tipo, clave, nombre;

    CheckBox mantenerSesion;
    boolean start = false;
    RequestQueue requestQueue;

    private PreferenceManager preferenceManager;

    String ip = "192.168.1.66";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //vinculamos con el layout
        edtUsuario = findViewById(R.id.usuario);
        edtContrasena = findViewById(R.id.contrasena);
        btnLogin = findViewById(R.id.boton);
        mantenerSesion = findViewById(R.id.mantenerSesion);
        progressBarInicio = findViewById(R.id.inicioProgressBar);


        getApplicationContext().deleteDatabase("juno_doctor.db");

        //logica del boton
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtContrasena.getText().toString().isEmpty() || !edtUsuario.getText().toString().isEmpty()) {

                    if (mantenerSesion.isChecked()) {
                        start = true;
                    }
                    else {
                        start = false;
                    }

                    //loginCliente("http://"+ip+":8080/proyecto/cliente/validar_usuario_cliente.php");

                    iniciarSesionFirebase();

                }
                else {
                    Toast.makeText(getApplicationContext(), "no se permiten campos vacios", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void iniciarSesionFirebase(){
        btnLogin.setVisibility(View.INVISIBLE);
        progressBarInicio.setVisibility(View.VISIBLE);


        FirebaseFirestore databaseUsuario = FirebaseFirestore.getInstance();
        databaseUsuario.collection("usuarios")
                .whereEqualTo("usuario", edtUsuario.getText().toString())
                .whereEqualTo("contra",edtContrasena.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size()>0){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            usuario = documentSnapshot.getString("usuario");
                            clave= documentSnapshot.getString("clave") ;
                            nombre= documentSnapshot.getString("nombre");
                            tipo ="usuario";
                            id = documentSnapshot.getId();
                            guardarDatosUsuarioActual(id,usuario,tipo,clave,start,nombre);
                            entrarInterfaz(tipo);
                        }
                        else {

                            FirebaseFirestore databaseDoctor = FirebaseFirestore.getInstance();
                            databaseDoctor.collection("doctores")
                                    .whereEqualTo("usuario", edtUsuario.getText().toString())
                                    .whereEqualTo("contra",edtContrasena.getText().toString())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size()>0){
                                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                                usuario = documentSnapshot.getString("usuario");
                                                clave= null ;
                                                nombre= documentSnapshot.getString("nombre");
                                                tipo ="doctor";
                                                id = documentSnapshot.getId();
                                                guardarDatosUsuarioActual(id,usuario,tipo,clave,start,nombre);
                                                entrarInterfaz(tipo);
                                            }
                                            else {
                                                FirebaseFirestore databaseAsistente = FirebaseFirestore.getInstance();
                                                databaseAsistente.collection("asistentes")
                                                        .whereEqualTo("usuario", edtUsuario.getText().toString())
                                                        .whereEqualTo("contra",edtContrasena.getText().toString())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size()>0){
                                                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                                                    usuario = documentSnapshot.getString("usuario");
                                                                    clave= null ;
                                                                    nombre= documentSnapshot.getString("nombre");
                                                                    tipo ="Asistente";
                                                                    id = documentSnapshot.getId();
                                                                    guardarDatosUsuarioActual(id,usuario,tipo,clave,start,nombre);
                                                                    entrarInterfaz(tipo);
                                                                }
                                                                else {
                                                                    btnLogin.setVisibility(View.VISIBLE);
                                                                    progressBarInicio.setVisibility(View.INVISIBLE);

                                                                    Toast.makeText(getApplicationContext(),"USUARIO O CONTRASEÑA NO VALIDOS",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    //login del cliente
    private void loginCliente (String  URL){
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){

                    datosClientes("http://"+ip+":8080/proyecto/cliente/buscar_datos_cliente.php?usuario="+edtUsuario.getText().toString()+"&password="+edtContrasena.getText().toString()+"");

                }
                else {
                    loginDoctor("http://"+ip+":8080/proyecto/doctor/validar_usuario_doctor.php");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("usuario",edtUsuario.getText().toString());
                parametros.put("password",edtContrasena.getText().toString());
                return parametros;
            }
        };

        requestQueue.add(stringRequest);
    }

    //llamamos datos
    public void datosClientes(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        tipo = "usuario";
                        nombre = jsonObject.getString("Nombre");
                        clave=jsonObject.getString("clave");
                        usuario=jsonObject.getString("usuario");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                //guardarDatosUsuarioActual(usuario,tipo,clave,start,nombre);
                entrarInterfaz(tipo);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    //login del doctor y asistente
    private void loginDoctor (String  URL){

        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    datosDoctor("http://"+ip+":8080/proyecto/doctor/buscar_datos_doctor.php?usuario="+edtUsuario.getText().toString()+"&password="+edtContrasena.getText().toString()+"");
                }
                else {
                    Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("usuario",edtUsuario.getText().toString());
                parametros.put("password",edtContrasena.getText().toString());
                return parametros;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void datosDoctor(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        tipo =jsonObject.getString("tipo_user");
                        nombre = jsonObject.getString("nombre");

                        clave=null;
                        usuario=jsonObject.getString("usuario");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                //textView.setText(nombre);

                //guardarDatosUsuarioActual(usuario,tipo,clave,start,nombre);

                entrarInterfaz(tipo);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(jsonArrayRequest);
    }


    public void guardarDatosUsuarioActual(String id,String usuario ,String tipoUsuario, String clave, boolean sesionIniciada,String nombre) {
        SharedPreferences sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id",id);
        editor.putString("usuario", usuario);
        editor.putString("nombre", nombre);
        editor.putString("tipo", tipoUsuario);
        editor.putString("clave",clave);
        editor.putBoolean("sesion", sesionIniciada);
        editor.apply();

    }

    public String mostrarDatosUsuarioActual(String key){
        SharedPreferences sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"false");
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode== event.KEYCODE_BACK){
            finishAffinity();
        }

        return super.onKeyDown(keyCode, event);
    }

}