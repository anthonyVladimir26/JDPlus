package com.example.jdplus.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.jdplus.R;
import com.example.jdplus.adaptadores.AdapterMensajes;

import com.example.jdplus.objetos.Mensaje;
import com.example.jdplus.utilities.Constans;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BandejaMensajesActivity extends AppCompatActivity {
    //requestqueue para enviar imagen al servidor
    RequestQueue requestQueue;
    //botones de la interfaz
    ImageButton btnEnviarMensaje,btnEnviarImagen;

    //edittect donde se pondra el mensaje
    EditText edtMensajes;

    //componentes para ver los mensajes
    RecyclerView rvMensajes;
    AdapterMensajes adapterMensajes;
    ArrayList<Mensaje>listMensaje;

    //datos donde guardaremos los datos de ls interfaz anterior
    String claveIntent,nombreIntent;

    //datos del usuario actual
    String claveActual,tipoActual;

    //nombre del chat
    String nombreChat;

    //llamamos variable de firebase
    FirebaseDatabase firebaseDatabase ;
    FirebaseFirestore firebaseFirestore;


    //date para llamar datos del dia actual para el mensaje
    Date date;

    //nombre que tendra la foto
    String nombreImagen;
    Long consecutivo;


    //variable para cargar el token del usuario
    String fcmToken= "";

    //enviamos notificacion?
    void enviarNotificacion ( String mensaje, String token,String nombre){

        //Toast.makeText(BandejaMensajesActivity.this, token, Toast.LENGTH_SHORT).show();

        if (token!= null || !token.equals("")) {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //url para enviar notificaciones
                String URL = "https://fcm.googleapis.com/fcm/send";

                //datos del mensaje
                JSONObject data = new JSONObject();
                data.put("title", nombre);
                data.put("body", mensaje);


                //datos para la notificacion
                JSONObject notificationData = new JSONObject();
                notificationData.put("notification", data);
                notificationData.put("to", token);


                JsonObjectRequest request = new JsonObjectRequest(URL, notificationData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(BandejaMensajesActivity.this, "exito", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BandejaMensajesActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        String key = "Key=AAAAaOdkHrs:APA91bH4XnpjrbssrcRdAdbqNospmaSxpfHAhU1AZZbuUcJE51YVBqm4yQCURj0DeObULlPK3Uiyyuft6dPdndzQkHZMLrXwH7VEznysQkZfH_4lKMWVAyGIBupqQUuIuPDwAlkQfHBk";
                        map.put("Authorization", key);
                        map.put("Content-Type", "application/json");
                        return map;
                    }
                };

                requestQueue.add(request);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(BandejaMensajesActivity.this, "no enviado", Toast.LENGTH_SHORT).show();
        }

    }


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandeja_mensajes);

        //llamomos datos de la interfaz
        btnEnviarMensaje = findViewById(R.id.btnEnviarMensajeBandejaDeMensaje);
        btnEnviarImagen = findViewById(R.id.btnEnviarImagenBandejaDeMensaje);

        edtMensajes = findViewById(R.id.editTextMensajeBandejaDeMensajes);

        //inicializamos variables de firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();

        //inizializamos los datos del dia actual
        date = new Date();

        //llamamos los datos del intent
        claveIntent = getIntent().getStringExtra("clave");



        //llamamos los datos del usuario actual
        SharedPreferences sharedPreferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        claveActual = sharedPreferences.getString("clave","");
        tipoActual= sharedPreferences.getString("tipo","");


        //damos datos a nombrechat
        if (tipoActual.equals("usuario")) nombreChat= claveIntent+claveActual;
        else nombreChat = claveActual+claveIntent;

        //llamamos al adapter, la lista y el reciclerView
        rvMensajes = findViewById(R.id.reciclerViewMensajeBandejaDeMensajes);
        listMensaje = new ArrayList<>();
        adapterMensajes = new AdapterMensajes(this,listMensaje);
        rvMensajes.setAdapter(adapterMensajes);


        //llamamos al metodo para mostrar los datos del chat
        mostrarDatos();

            firebaseFirestore.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()&&task.getResult()!= null){
                        for (QueryDocumentSnapshot document:task.getResult()) {
                            String clave =document.getString("clave");

                            if (clave.equals(claveIntent)){
                                fcmToken = document.getString("fcm_token");
                                //Toast.makeText(this, fcmToken, Toast.LENGTH_SHORT).show();

                                nombreIntent = document.getString("nombre");

                                //mandamos los datos al barra de navegacion
                                getSupportActionBar().setTitle(nombreIntent);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_200)));


                            }
                        }
                    }
                })
                .addOnFailureListener(e-> Toast.makeText(this, "error" +e.getMessage(), Toast.LENGTH_SHORT).show());




        //boton para enviar el mensaje
        btnEnviarMensaje.setOnClickListener(v -> {
            //llamamos al objeto mensaje para guardar los dato
            String mensaje = edtMensajes.getText().toString();

            //verificamos si el edittext esta vacio
            if (mensaje.equals("")){
                Toast.makeText(this, "envié un mensaje", Toast.LENGTH_SHORT).show();
            }
            else {

                //Toast.makeText(this, fcmToken, Toast.LENGTH_SHORT).show();
                //llanamos el objeto mensaje y llamamos el metodo para enviar mensaje
                Mensaje datosMensaje = new Mensaje(mensaje,claveActual,"",date.getTime(),1);
                enviarMensaje(datosMensaje);
            }

        });

        //boton para enviar una imagen
        btnEnviarImagen.setOnClickListener(v -> {
            //mostramos una ventana donde decidira donde tomar una foto o llamar de la galeria
            mostrarVentanaOpcionesFoto();
        });


    }

    private void mostrarVentanaOpcionesFoto() {
        consecutivo= System.currentTimeMillis()/1000;
        nombreImagen = "Juno_Foto_"+consecutivo.toString()+".jpg";

        final CharSequence[] opciones = {"Tomar Foto", "Elegir de la galeria", "cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(BandejaMensajesActivity.this);
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (opciones[which].equals("Tomar Foto")){

                            int permisoCamara = ContextCompat.checkSelfPermission(BandejaMensajesActivity.this, Manifest.permission.CAMERA);
                            int permisoAlmacenamiente = ContextCompat.checkSelfPermission(BandejaMensajesActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

                            if (permisoCamara== PackageManager.PERMISSION_GRANTED && permisoAlmacenamiente == PackageManager.PERMISSION_GRANTED) {

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 10);
                            }
                            else{
                                requestPermissions(new String[]{Manifest.permission.CAMERA},200);
                                Toast.makeText(BandejaMensajesActivity.this, "se denegaron", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            if (opciones[which].equals("Elegir de la galeria")){

                                Intent intent= new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent.createChooser(intent, "seleccione"), 20);

                            }
                            else {
                                dialog.dismiss();

                            }
                        }
                    }
                }

        );
        builder.show();
    }

    //metodo que se activa cuando tengo imagen;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==20 && resultCode ==RESULT_OK){


            Uri imgUri = data.getData();
            try {
                Bitmap bitmap =MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),imgUri);

                enviarImagenMensaje(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            Intent intent= new Intent(BandejaMensajesActivity.this,EnviarImagenActivity.class);
            String path = imgUri.toString();
            intent.putExtra("img",path);
            startActivity(intent);
             */


        }

        else if(requestCode ==10 && resultCode ==RESULT_OK){

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            enviarImagenMensaje(imageBitmap);
            /*
             Intent intent= new Intent(BandejaMensajesActivity.this,EnviarImagenActivity.class);
            intent.putExtra("img",path );
            intent.putExtra("claveIntent",claveIntent);
            intent.putExtra("claveActual",claveActual);
            startActivity(intent);
            //Toast.makeText(BandejaMensajesActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
             */



        }
    }
    private void  enviarImagenMensaje(Bitmap bitmap){

        String url = "http://"+getString(R.string.ip)+"/proyecto/mensajes/enviar_foto.php";
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("registra")){
                    Toast.makeText(BandejaMensajesActivity.this, "registro",Toast.LENGTH_SHORT).show();

                    Mensaje mensaje = new Mensaje("foto",claveActual,"/proyecto/imagenes/chats/"+nombreChat+"/"+nombreImagen,date.getTime(),2);

                    enviarMensaje(mensaje);
                }
                else{
                    Toast.makeText(BandejaMensajesActivity.this,"No se ha registrado por "+ response,Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BandejaMensajesActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                String imagen = convertirImgString(bitmap);

                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombreChat",nombreChat);
                parametros.put("imagenes",imagen);
                parametros.put("nombreImagen",nombreImagen);

                return parametros;

            }
        };
        requestQueue.add(stringRequest);
    }

    //motodo para mostrar mensaje
    private void mostrarDatos() {

        firebaseDatabase.getReference().child("chats").child(claveActual)
                .child(claveIntent)
                .child("mensajes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        listMensaje.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()) {


                            String claveEnvia = dataSnapshot.child("claveEnvia").getValue(String.class);
                            String mensaje = dataSnapshot.child("mensaje").getValue(String.class);
                            String imagenUrl = dataSnapshot.child("imagenUrl").getValue(String.class);
                            Long hora= dataSnapshot.child("hora").getValue(Long.class);
                            int tipo= dataSnapshot.child("tipo").getValue(Integer.class);


                            listMensaje.add(new Mensaje(mensaje,claveEnvia,imagenUrl,hora,tipo));
                        }

                        HashMap<String,Object> mensajesNuevos = new HashMap<>();
                        mensajesNuevos.put("numeroMensajeNuevos",0);
                        firebaseDatabase.getReference().child("chats").child(claveActual).child(claveIntent).updateChildren(mensajesNuevos);
                        adapterMensajes.notifyDataSetChanged();
                        rvMensajes.scrollToPosition(listMensaje.size() -1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void enviarMensaje(Mensaje mensaje) {
        edtMensajes.setText("");

        if (fcmToken.equals("") || fcmToken.equals(" ") || fcmToken.isEmpty()||fcmToken == null){
            Toast.makeText(BandejaMensajesActivity.this, "no se pudo enviar la notificacion", Toast.LENGTH_SHORT).show();
        }
        else {
            enviarNotificacion( mensaje.getMensaje(), fcmToken, nombreIntent);
        }
        //datos del ultimo mensaje
        HashMap<String, Object> lastMsgObjActual = new HashMap<>();
        lastMsgObjActual.put("ultimoMensaje", mensaje.getMensaje());
        lastMsgObjActual.put("horaUltimoMensaje", mensaje.getHora());
        lastMsgObjActual.put("tipoUltimoMensaje",mensaje.getTipo());

        HashMap<String, Object> lastMsgObjRecibe = new HashMap<>();
        lastMsgObjRecibe.put("ultimoMensaje", mensaje.getMensaje());
        lastMsgObjRecibe.put("horaUltimoMensaje", mensaje.getHora());
        lastMsgObjRecibe.put("tipoUltimoMensaje",mensaje.getTipo());
        lastMsgObjRecibe.put("numeroMensajeNuevos",1);


        firebaseDatabase.getReference().child("chats").child(claveActual).child(claveIntent).updateChildren(lastMsgObjActual);
        firebaseDatabase.getReference().child("chats").child(claveIntent).child(claveActual).updateChildren(lastMsgObjRecibe);
        //enviados los datos
        firebaseDatabase.getReference().child("chats").child(claveActual).child(claveIntent).child("mensajes").push().setValue(mensaje);
        firebaseDatabase.getReference().child("chats").child(claveIntent).child(claveActual).child("mensajes").push().setValue(mensaje);

        }


    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }


}