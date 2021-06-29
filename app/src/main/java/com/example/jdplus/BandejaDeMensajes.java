package com.example.jdplus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jdplus.Sqlite.CreaBDSqlite;
import com.example.jdplus.Sqlite.SqliteOperacionesMensajes;
import com.example.jdplus.adaptadores.AdapterMensajes;
import com.example.jdplus.objetos.Mensajes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BandejaDeMensajes extends AppCompatActivity {

    //carpeta para que se guarde las fotos al tomarlas
    private static final String CARPETA_PRINCIPAL= "junoDoctor/";
    private static final String CARPETA_IMAGEN= "imagenes/";
    private static final String DIRECTORIO_IMAGEN= CARPETA_PRINCIPAL+CARPETA_IMAGEN;
    public String path;
    File fileImagen;
    Bitmap bitmap;
    String rutaImagen;

    //datos para seleccionar o tomar foto
    private static final int COD_SELECCIONA = 10;

    //llamamos a la clase para realizar las operaciones sqlite
    SqliteOperacionesMensajes operacionesMensajes;

    //iniciaamos un contador para verificar que no se repite los mensaje
    int contador=1;
    Long consecutivo;
    boolean mandarMensaje= false;

    //conseguimos y vinculamos a variables los datos del usuario actual
    String tipo ;
    String usuario ;
    String nombre ;

    //nombre que tendra la foto
    String nombreImagen;

    //creamso un CountDownTimer
    CountDownTimer count ;

    //el requestQueue para enviar y recibir datos
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    //barra  superior
    TextView tvnombreUsuario;
    Button btnregresar;

    //llamamos los layout de la pantalla
    LinearLayout lyBarraSuperior, lyFoto,lyRecyclerView,lyEnviarMensaje;

    //inicializamos las partes para que se vean los mensajes
    RecyclerView recyclerView;
    ArrayList<Mensajes> listMensajes;
    AdapterMensajes adaptador;


    //iniciamos partes para mandar el mansaje
    EditText edtEnviarMensaje;
    FloatingActionButton botonEnviar;
    FloatingActionButton botonEnviarFoto;
    Button btnEnviarFoto, btnCancelarFotos;
    ImageView imageView;

    //variables que guardaran los datos del fragment anterior
    String recibeId;
    String recibeUsuario;
    String recibeNombre;

    //ip del servidor
    String ip = "192.168.100.59";

    //datos para crear tabla chat
    String nombreChat, doctor, cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandeja_de_mensajes);

        //viculamos con los componentes del layout
        tvnombreUsuario = findViewById(R.id.nombreUsuario);
        btnregresar = findViewById(R.id.botonRegresar);
        botonEnviar = findViewById(R.id.botonEnviarDoctor);
        botonEnviarFoto = findViewById(R.id.botonEnviarFotoDoctor);
        edtEnviarMensaje = findViewById(R.id.mensaje);
        recyclerView = findViewById(R.id.chatRecyclerViewDoctor);
        lyBarraSuperior =findViewById(R.id.layoutSuperior);
        lyEnviarMensaje = findViewById(R.id.layoutEnviarMensaje);
        lyFoto = findViewById(R.id.layoutFoto);
        lyRecyclerView = findViewById(R.id.layoutRecyclerViev);

        listMensajes = new ArrayList<Mensajes>();
        imageView = findViewById(R.id.imageView);
        btnEnviarFoto = findViewById(R.id.botonEnviarFoto);
        btnCancelarFotos = findViewById(R.id.botonCancelarFoto);

        operacionesMensajes = new SqliteOperacionesMensajes(BandejaDeMensajes.this);


        crearChat("http://"+ip+":8080/proyecto/mensajes/crear_chat.php");


        //vinculamos los datos de los usuarios actuales
        SharedPreferences preferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        tipo = preferences.getString("tipo", "");
        usuario = preferences.getString("usuario", "");
        nombre = preferences.getString("nombre", "");

        //asignamos los datos del fragment anterior
        recibeId = getIntent().getStringExtra("id");
        recibeUsuario = getIntent().getStringExtra("usuario");
        recibeNombre = getIntent().getStringExtra("nombre");

        //asignamos los datos al textviev superior
        tvnombreUsuario.setText(recibeNombre);

        //especificamos los datos de la tabla
        if (tipo.equals("doctor")) {
            doctor = nombre;
            cliente = recibeNombre;
            nombreChat = usuario + recibeUsuario;
        } else {
            doctor = recibeNombre;
            cliente = nombre;
            nombreChat = recibeUsuario + usuario;
        }
        operacionesMensajes.crearTablaSqlite(nombreChat);




        //mostramos los datos de los usuarios
        mostrarDatos();
        obtenerMensajes("http://"+ip+":8080/proyecto/mensajes/mostrar_chat.php?nombreChat="+nombreChat+"");

        //iniciamos un temporizador que durara 2 segundo
        count = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                //mostramos los datos de los usuarios

                obtenerMensajes("http://"+ip+":8080/proyecto/mensajes/mostrar_chat.php?nombreChat="+nombreChat+"");

                if (mandarMensaje ==true){
                    mostrarDatos();

                    mandarMensaje =false;
                }
                count.start();
            }
        };
        count.start();

        //boton para regresar a la ventana anterios
        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //boton para enviar un mensaje
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarMensajes("http://"+ip+":8080/proyecto/mensajes/enviar_mensajes.php");
            }
        });

        //boton para enviar una imagen
        botonEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consecutivo= System.currentTimeMillis()/1000;
                nombreImagen = "Foto"+consecutivo.toString()+".jpg";

                obtenerMensajes("http://"+ip+":8080/proyecto/mensajes/mostrar_chat.php?nombreChat="+nombreChat+"");

                mostrarDialogoOpciones();

            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerMensajes("http://"+ip+":8080/proyecto/mensajes/mostrar_chat.php?nombreChat="+nombreChat+"");

                enviarImagenMensaje();

                lyEnviarMensaje.setVisibility(View.VISIBLE);
                lyRecyclerView.setVisibility(View.VISIBLE);
                lyFoto.setVisibility(View.GONE);
            }
        });
        btnCancelarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lyEnviarMensaje.setVisibility(View.VISIBLE);
                lyRecyclerView.setVisibility(View.VISIBLE);
                lyFoto.setVisibility(View.GONE);

            }

        });

    }

    private void mostrarDialogoOpciones() {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de la galeria", "cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(BandejaDeMensajes.this);
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (opciones[which].equals("Tomar Foto")){


                            abrirCamara();
                        }
                        else {
                            if (opciones[which].equals("Elegir de la galeria")){

                                Intent intent= new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/");
                                startActivityForResult(intent.createChooser(intent, "seleccione"), COD_SELECCIONA);


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

    private void abrirCamara() {


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null){

                File imagenArchivo = null;
                try {
                    imagenArchivo = crearImagen();

                }catch (IOException ex){
                    Log.e("error", ex.toString());
                }

                if (imagenArchivo != null){
                    Uri fotoUri = FileProvider.getUriForFile(this, "com.example.jdplus.fileprovider", imagenArchivo);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);
                    startActivityForResult(intent, 1);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONA:

                lyEnviarMensaje.setVisibility(View.GONE);
                lyRecyclerView.setVisibility(View.GONE);
                lyFoto.setVisibility(View.VISIBLE);

                Uri miPath= data.getData();
                imageView.setImageURI(miPath);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),miPath);
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();

                }

                break;
        }

        if (requestCode == 1 && resultCode ==RESULT_OK){

            lyEnviarMensaje.setVisibility(View.GONE);
            lyRecyclerView.setVisibility(View.GONE);
            lyFoto.setVisibility(View.VISIBLE);

             bitmap= BitmapFactory.decodeFile(rutaImagen);
             imageView.setImageBitmap(bitmap);

        }

    }

    private void  enviarImagenMensaje(){
        String url = "http://"+ip+":8080/proyecto/mensajes/enviar_foto.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("registra")){
                    Toast.makeText(BandejaDeMensajes.this, "registro",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(BandejaDeMensajes.this,"No se ha registrado por "+ response,Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BandejaDeMensajes.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nombreDelChat = nombreChat;
                String usuarioEnvia = usuario;
                String tipoM = "2";

                String imagen = convertirImgString(bitmap);

                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombreChat",nombreDelChat);
                parametros.put("usuarioEnvia",usuarioEnvia);
                parametros.put("imagenes",imagen);
                parametros.put("nombreImagen",nombreImagen);
                parametros.put("tipo",tipoM);

                return parametros;

            }
        };
        requestQueue.add(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }

    private File crearImagen() throws IOException {

            File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imagen = File.createTempFile(nombreImagen, ".jpg",directorio );

            rutaImagen = imagen.getAbsolutePath();

            return imagen;
    }

    private void crearChat(String URL) {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nombreChat",nombreChat);
                parametros.put("doctor",doctor);
                parametros.put("cliente",cliente);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void enviarMensajes(String URL) {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    edtEnviarMensaje.setText(" ");
                    //mostramos los datos de los usuarios
                    obtenerMensajes("http://192.168.1.70:8080/proyecto/mensajes/mostrar_chat.php?nombreChat="+nombreChat+"");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nombreChat",nombreChat);
                parametros.put("usuarioEnvia",usuario);
                parametros.put("mensajes",edtEnviarMensaje.getText().toString());
                parametros.put("tipo","1");
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void obtenerMensajes(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        String usuarioEnvia = jsonObject.getString("usuarioEnvia");
                        String mensajes = jsonObject.getString("mensajes");
                        String imagenes = jsonObject.getString("imagenes");
                        String hora = jsonObject.getString("hora");
                        String dia = jsonObject.getString("dia");
                        int tipo = jsonObject.getInt("tipo");


                        int idMaxima = operacionesMensajes.idMaxima(nombreChat);
                        if (id> idMaxima ) {
                            operacionesMensajes.insertarMensaje(nombreChat,usuarioEnvia,mensajes,imagenes,hora,dia,tipo);

                            mandarMensaje =true;
                        }
                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void mostrarDatos(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adaptador = new AdapterMensajes(getApplicationContext(), operacionesMensajes.mostraMensaje(nombreChat,ip));
        recyclerView.setAdapter(adaptador);
    }
}