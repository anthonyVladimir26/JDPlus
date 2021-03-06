package com.example.jdplus.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jdplus.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //inicializamos las varibles
    EditText edtUsuario, edtContrasena;
    MaterialButton btnLogin;
    ProgressBar progressBarInicio;

    String id,usuario,tipo, clave, nombre;


    RequestQueue requestQueue;

    FirebaseAuth firebaseAuth;

    private PreferenceManager preferenceManager;

    String ip = "192.168.1.66";

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        String tipo  =sharedPreferences.getString("tipo",null);

        if (firebaseAuth.getCurrentUser() != null ){
            entrarInterfaz(tipo);

        }
        else {


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //vinculamos con el layout
        edtUsuario = findViewById(R.id.usuario);
        edtContrasena = findViewById(R.id.contrasena);
        btnLogin = findViewById(R.id.boton);

        progressBarInicio = findViewById(R.id.inicioProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();


        getApplicationContext().deleteDatabase("juno_doctor.db");

        //logica del boton
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtContrasena.getText().toString().isEmpty() || !edtUsuario.getText().toString().isEmpty()) {



                    //loginCliente("http://"+ip+":8080/proyecto/cliente/validar_usuario_cliente.php");


                    String email= edtUsuario.getText().toString()+"@junoDoctor.com";

                    String contrasena= edtContrasena.getText().toString();

                    iniciarSesionFirebase(email,contrasena);

                }
                else {
                    Toast.makeText(getApplicationContext(), "no se permiten campos vacios", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void iniciarSesionFirebase(String email,String contrasena){
        btnLogin.setVisibility(View.INVISIBLE);
        progressBarInicio.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, contrasena).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                FirebaseFirestore databaseUsuario = FirebaseFirestore.getInstance();
                databaseUsuario.collection("usuarios")
                        .get()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful() && task1.getResult() != null && task1.getResult().getDocuments().size() > 0) {
                                for (QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
                                    String id2 = documentSnapshot.getId();
                                    if (FirebaseAuth.getInstance().getUid().equals(id2)) {
                                        usuario = documentSnapshot.getString("usuario");
                                        clave = documentSnapshot.getString("clave");
                                        nombre = documentSnapshot.getString("nombre");
                                        tipo = documentSnapshot.getString("tipo");
                                        id = firebaseAuth.getUid();
                                        guardarDatosUsuarioActual(id, usuario, tipo, clave,  nombre);
                                        entrarInterfaz(tipo);
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else {
                Toast.makeText(MainActivity.this, "datos no volidos", Toast.LENGTH_SHORT).show();
                btnLogin.setVisibility(View.VISIBLE);
                progressBarInicio.setVisibility(View.INVISIBLE);
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

                    datosClientes("http://"+ip+"/proyecto/cliente/buscar_datos_cliente.php?usuario="+edtUsuario.getText().toString()+"&password="+edtContrasena.getText().toString()+"");

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


    public void guardarDatosUsuarioActual(String id,String usuario ,String tipoUsuario, String clave, String nombre) {
        SharedPreferences sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id",id);
        editor.putString("usuario", usuario);
        editor.putString("nombre", nombre);
        editor.putString("tipo", tipoUsuario);
        editor.putString("clave",clave);

        editor.apply();

    }

    public void entrarInterfaz (String tipo){
        if (tipo.equals("usuario")){
            Intent intentCliente = new Intent(this,NavDrawerCliente.class);
            startActivityForResult(intentCliente,0);}
        if (tipo.equals("doctor")){
            Intent intentDoctor = new Intent(this,NavDrawerDoctor.class);
            startActivityForResult(intentDoctor,0);}
        if (tipo.equals("asistente")){
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