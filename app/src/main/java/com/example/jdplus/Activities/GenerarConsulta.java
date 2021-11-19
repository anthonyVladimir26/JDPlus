package com.example.jdplus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.example.jdplus.R;
import com.example.jdplus.adaptadores.AdapterDoctorConsulta;
import com.example.jdplus.adaptadores.AdapterUsuarioConsulta;
import com.example.jdplus.objetos.DoctorConsulta;
import com.example.jdplus.objetos.UsuarioConsulta;
import com.example.jdplus.utilities.Constans;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerarConsulta extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    RequestQueue requestQueue;

    ProgressDialog dialogCarga;

    LinearLayout linearDoctor, linearUsuario;

    public static LinearLayout linearDatosUsuario, linearDatosDoctor;

    LinearLayout linearDatosNino;

    FirebaseDatabase database;

    String meses;
    String horaVer;
    String minutosVer;

    String tipoConsulta;


    String formato ="";

    AlertDialog.Builder dialogBuilder;
    public static AlertDialog dialog;

    AdapterDoctorConsulta adapterDoctor;
    AdapterUsuarioConsulta adapterUsuario;

    ArrayList<DoctorConsulta> listDoctores;
    List<UsuarioConsulta> listUsarios;

    public static Spinner spinnerEspecialidad;
    Spinner spinnerHorario;

    RecyclerView recyclerView;

    public static TextView txvnombreDoctor;
    public static TextView txvnombreUsuario;

    //booleanos
    boolean usuario= false;
    boolean doctor= false;
    boolean enfermedadUsuario=false;
    boolean medicacionUsuario=false;
    boolean fechaCita=false;
    boolean horaCita =false;
    boolean costoCita=false;
    boolean especialidad = false;


    //datos doctor
    public static String nombreDoctor;
    public static String claveDoctor;



    //datos usuario
    public static String nombreUsuario;
    public static String userUsuario;
    public static String claveUsuario;
    public static String generoUsuario;
    public static String tipoSangreUsuario;
    public static String edadUsuario;
    public static String correoUsuario;
    public static String telefonoUsuario;
    public static String fechaNacimientoUsuario;

    String nombreChat;

    String enfermedad, info_enfermedad;

    String medicina,info_medicina;

    String fecha;

    String hora;

    public static ImageView imagenDoctor,imagenUsuario,imagenGenero;


    //editTextUsuario
    public static EditText edtGeneroUsuario,edtTipoSangreUsuario,edtEdadUsuario,edtCorreoUsuario,edtTelefonoUsuario,edtFechaNacimiento;

    EditText edtDatosEnfermedadUsuario,edtDatosMedicinaUsuario;

    //editTextConsulta
    EditText edtFechaConsulta, edtHoraConsulta,edtCostoConsulta;

    //radio botons
    RadioButton rbtnMedicinaSi,rbtnMedicinaNo,rbtnEnfermedadSi,rbtnEnfermedadNo;

    //boton para crear consulta
    Button btnCrearConsulta;

    String[] especialidades;

    String espe = "";

    ArrayAdapter<String> adapterSpinnerEspecialidad;

    //dato niño

    EditText edtNombreNino,edtFechaNacimientoNino,edtTipoSangreNino,edtEdadNino,edtDatosEnfermedadNino,edtDatosMedicinaNino,edtDatosHermanoNino;

    RadioButton rbtnMedicinaNinoSi,rbtnMedicinaNinoNo,rbtnEnfermedadNinoSi,rbtnEnfermedadNinoNo, rbtnHermanoNinoSi,rbtnHermanoNinoNo;

    String medicacionNino, medicacionNino_info =" ";

    String enfermedadNino, enfermedadNino_info=" ";

    String hermanoNino, hermanoNino_info;

    Boolean boolEnfermedadNino,boolMedicinaNino,boolHermanoNino;

    Boolean creado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_consulta);

        database = FirebaseDatabase.getInstance();


        //dialog = new Dialog(this);
        linearDoctor =findViewById(R.id.layout_elegir_doctor);
        linearUsuario =findViewById(R.id.layout_elegir_usuario);
        linearDatosUsuario = findViewById(R.id.datosUsuario);

        //spinner
        spinnerEspecialidad= findViewById(R.id.spinnerEspecialidades);
        spinnerHorario = findViewById(R.id.spinnerHorario);

        especialidades = new String[5];




        //arrayAdapterHoras
        ArrayAdapter<CharSequence> adapterSpinnerHora = ArrayAdapter.createFromResource(this,R.array.horas_array
                , android.R.layout.simple_spinner_item);

        adapterSpinnerHora.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerHorario.setAdapter(adapterSpinnerHora);


        linearDatosNino = findViewById(R.id.datosNinoLayout);

        String tipoConsulta = getIntent().getStringExtra("tipo");

        if (tipoConsulta.equals("infantil")) linearDatosNino.setVisibility(View.VISIBLE);
        else linearDatosNino.setVisibility(View.GONE);



        //usuario
        txvnombreUsuario = findViewById(R.id.nombreUsuario);
        imagenUsuario = findViewById(R.id.imagenUsuario);
        edtGeneroUsuario =findViewById(R.id.generoUsuario);
        edtTipoSangreUsuario= findViewById(R.id.tipoSangreUsuario);
        edtEdadUsuario=findViewById(R.id.edadUsuario);
        edtCorreoUsuario = findViewById(R.id.correoUsuario);
        edtTelefonoUsuario = findViewById(R.id.telefonoUsuario);
        edtFechaNacimiento = findViewById(R.id.fechaNacimientoUsuario);
        imagenGenero = findViewById(R.id.imagenGenero);

        edtDatosEnfermedadUsuario = findViewById(R.id.edtEnfermedadUsuario);
        edtDatosMedicinaUsuario = findViewById(R.id.edtMedicinaUsuario);

        rbtnEnfermedadSi =findViewById(R.id.rbtnEnfermedadSi);
        rbtnEnfermedadNo =findViewById(R.id.rbtnEnfermedadNo);

        rbtnMedicinaSi =findViewById(R.id.rbtnMedicinaSi);
        rbtnMedicinaNo =findViewById(R.id.rbtnMedicinaNo);

        //doctor
        txvnombreDoctor = findViewById(R.id.nombreDoctor);
        imagenDoctor = findViewById(R.id.imagenDoctor);






        //niño
        edtNombreNino = findViewById(R.id.edtNombreNino);
        edtFechaNacimientoNino = findViewById(R.id.edtFechaNacimientoNino);
        edtTipoSangreNino = findViewById(R.id.edtTipoSangreNino);
        edtEdadNino = findViewById(R.id.edtEdadNino);
        edtDatosEnfermedadNino = findViewById(R.id.edtEnfermedadNino);
        edtDatosMedicinaNino = findViewById(R.id.edtMedicinaNino);
        edtDatosHermanoNino = findViewById(R.id.edtHermanoNino);

        rbtnMedicinaNinoSi = findViewById(R.id.rbtnMedicinaNinoSi);
        rbtnMedicinaNinoNo = findViewById(R.id.rbtnMedicinaNinoNo);
        rbtnEnfermedadNinoSi = findViewById(R.id.rbtnEnfermedadNinoSi);
        rbtnEnfermedadNinoNo = findViewById(R.id.rbtnEnfermedadNinoNo);
        rbtnHermanoNinoSi = findViewById(R.id.rbtnHermanoSi);
        rbtnHermanoNinoNo = findViewById(R.id.rbtnHermanoNo);

        rbtnEnfermedadNinoSi.setOnClickListener(v -> {
            edtDatosEnfermedadNino.setVisibility(View.VISIBLE);
            enfermedadNino ="Si";
        });
        rbtnEnfermedadNinoNo.setOnClickListener(v -> {
            edtDatosEnfermedadNino.setVisibility(View.GONE);
            enfermedadNino ="No";
        });

        rbtnMedicinaNinoSi.setOnClickListener(v -> {
            edtDatosMedicinaNino.setVisibility(View.VISIBLE);
            medicacionNino = "Si";
        });

        rbtnMedicinaNinoNo.setOnClickListener(v -> {
            edtDatosMedicinaNino.setVisibility(View.GONE);
            medicacionNino = "No";
        });
        rbtnHermanoNinoSi.setOnClickListener(v -> {
            edtDatosHermanoNino.setVisibility(View.VISIBLE);
            hermanoNino = "Si";
        });

        rbtnHermanoNinoNo.setOnClickListener(v -> {
            edtDatosHermanoNino.setVisibility(View.GONE);
            hermanoNino = "No";
        });


        //editText consulta
        edtFechaConsulta = findViewById(R.id.edtFechaCita);
       // edtHoraConsulta = findViewById(R.id.edtHoraCita);
        edtCostoConsulta = findViewById(R.id.edtCostoCita);

        dialogCarga = new ProgressDialog(this);

        //boton crear cita
        btnCrearConsulta = findViewById(R.id.btnCrearConsulta);

        //editText de la fecha
        edtFechaConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog();
            }
        });



        listDoctores = new ArrayList<>();
        listUsarios = new ArrayList<>();

        rbtnEnfermedadSi.setOnClickListener(v -> {
            edtDatosEnfermedadUsuario.setVisibility(View.VISIBLE);
            enfermedad ="Si";
        });
        rbtnEnfermedadNo.setOnClickListener(v -> {
            edtDatosEnfermedadUsuario.setVisibility(View.GONE);
            enfermedad ="No";
        });

        rbtnMedicinaSi.setOnClickListener(v -> {
            edtDatosMedicinaUsuario.setVisibility(View.VISIBLE);
            medicina = "Si";
        });

        rbtnMedicinaNo.setOnClickListener(v -> {
            edtDatosMedicinaUsuario.setVisibility(View.GONE);
            medicina = "No";
        });

        linearUsuario.setOnClickListener(v -> ventanaCliente());
        linearDoctor.setOnClickListener(v -> ventanaDoctor());


        btnCrearConsulta.setOnClickListener(v -> {




            if  (txvnombreUsuario.getText().toString().equals("Elegir Cliente")){
                //Toast.makeText(GenerarConsulta.this, "Por Favor eliga cliente", Toast.LENGTH_SHORT).show();
                usuario=false;
            }else {
                usuario=true;
            }

            if (txvnombreDoctor.getText().toString().equals("Elegir Doctor")){
                //Toast.makeText(GenerarConsulta.this, "Por Favor eliga doctor", Toast.LENGTH_SHORT).show();
                doctor=false;
            }else {
                doctor=true;
            }


            if (rbtnEnfermedadSi.isChecked()){
                enfermedad= "Si";
                if(edtDatosEnfermedadUsuario.getText().toString().equals("")){
                    //Toast.makeText(GenerarConsulta.this, "por favor ingrese los datos de la enfermedad", Toast.LENGTH_SHORT).show();
                    enfermedadUsuario=false;
                }else {
                    enfermedadUsuario=true;
                    info_enfermedad =edtDatosEnfermedadUsuario.getText().toString();
                }
            }

            if (rbtnEnfermedadNo.isChecked()){
                enfermedad= "No";
                enfermedadUsuario=true;
                edtDatosEnfermedadUsuario.setText("");
            }

            if (rbtnMedicinaSi.isChecked()){
                medicina= "Si";
                if(edtDatosMedicinaUsuario.getText().toString().equals("")){
                    //Toast.makeText(GenerarConsulta.this, "por favor ingrese los datos de la medicacion", Toast.LENGTH_SHORT).show();
                    medicacionUsuario=false;
                }
                else {
                    medicacionUsuario = true;
                    info_medicina =edtDatosMedicinaUsuario.getText().toString();
                }
            }

            if (rbtnMedicinaNo.isChecked()){
                medicina= "No";
                medicacionUsuario = true;
                edtDatosMedicinaUsuario.setText("");
            }


            // niño

            if (rbtnEnfermedadNinoSi.isChecked()){
                enfermedadNino= "Si";
                if(edtDatosEnfermedadUsuario.getText().toString().equals("")){
                    //Toast.makeText(GenerarConsulta.this, "por favor ingrese los datos de la enfermedad", Toast.LENGTH_SHORT).show();
                    boolEnfermedadNino=false;
                }else {
                    boolEnfermedadNino=true;
                    enfermedadNino_info =edtDatosEnfermedadNino.getText().toString();
                }
            }

            if (rbtnEnfermedadNinoNo.isChecked()){
                enfermedadNino= "No";
                boolEnfermedadNino=true;
                edtDatosEnfermedadUsuario.setText("");
            }

            if (rbtnMedicinaNinoSi.isChecked()){
                medicacionNino= "Si";
                if(edtDatosMedicinaUsuario.getText().toString().equals("")){
                    //Toast.makeText(GenerarConsulta.this, "por favor ingrese los datos de la medicacion", Toast.LENGTH_SHORT).show();
                    boolMedicinaNino=false;
                }
                else {
                    boolMedicinaNino = true;
                    medicacionNino_info =edtDatosMedicinaNino.getText().toString();
                }
            }

            if (rbtnMedicinaNinoNo.isChecked()){
                medicacionNino= "No";
                boolMedicinaNino = true;
                edtDatosMedicinaNino.setText("");
            }


            if (rbtnHermanoNinoSi.isChecked()){
                hermanoNino= "Si";
                if(edtDatosMedicinaUsuario.getText().toString().equals("")){
                    //Toast.makeText(GenerarConsulta.this, "por favor ingrese los datos de la medicacion", Toast.LENGTH_SHORT).show();
                    boolHermanoNino=false;
                }
                else {
                    boolHermanoNino = true;
                    hermanoNino_info =edtDatosHermanoNino.getText().toString();
                }
            }

            if (rbtnHermanoNinoNo.isChecked()){
                hermanoNino= "No";
                boolHermanoNino = true;
                edtDatosHermanoNino.setText("");
                hermanoNino_info = " ";
            }



            if (edtFechaConsulta.getText().toString().equals("")){
                //Toast.makeText(GenerarConsulta.this, "por favor ingrese la fecha de la consulta", Toast.LENGTH_SHORT).show();
                fechaCita=false;
            }else {
                fechaCita =true;
            }


            if (edtCostoConsulta.getText().toString().equals("")){
                //Toast.makeText(GenerarConsulta.this, "por favor ingrese el costo de la consulta", Toast.LENGTH_SHORT).show();
                costoCita=false;
            }else {
                costoCita=true;
            }
            if (spinnerEspecialidad.getSelectedItem().toString().equals("Seleccione una especialidad")){
                //Toast.makeText(GenerarConsulta.this, "seleccione una especiadlidad valida", Toast.LENGTH_SHORT).show();
                especialidad=false;
            }else {
                especialidad =true;
            }

            if (spinnerHorario.getSelectedItem().toString().equals("Seleccione una hora")){
                //Toast.makeText(GenerarConsulta.this, "seleccione una especiadlidad valida", Toast.LENGTH_SHORT).show();
                horaCita=false;
            }else {
                horaCita =true;
            }


            if (tipoConsulta.equals("adulto")){


                if (fechaCita&&costoCita&&horaCita&&medicacionUsuario&&enfermedadUsuario&&doctor&&usuario&&especialidad){
                    dialogCarga.setMessage("Creando...");
                    dialogCarga.setCancelable(false);
                    dialogCarga.show();

                    traerHora(spinnerHorario.getSelectedItem().toString());

                    nombreChat = claveDoctor+claveUsuario;
                    Toast.makeText(GenerarConsulta.this, "se creo la consulta", Toast.LENGTH_SHORT).show();

                    crearChat("http://"+getString(R.string.ip)+"/proyecto/mensajes/crear_chat.php");
                    crearConsultaAdulto("http://"+getString(R.string.ip)+"/proyecto/doctor/crear_consulta_adulto.php");

                }
                else{
                    Toast.makeText(this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                }

            }

            else if (tipoConsulta.equals("infantil")){

                if (fechaCita&&costoCita&&horaCita&&medicacionUsuario&&enfermedadUsuario&&doctor&&usuario&&especialidad&&
                        !edtNombreNino.getText().toString().equals("") && !edtFechaNacimientoNino.getText().toString().equals("")&&
                        !edtTipoSangreNino.getText().toString().equals("") && !edtEdadNino.getText().toString().equals("")
                        &&boolMedicinaNino&&boolEnfermedadNino&&boolHermanoNino){

                    dialogCarga.setMessage("Creando...");
                    dialogCarga.setCancelable(false);
                    dialogCarga.show();

                    traerHora(spinnerHorario.getSelectedItem().toString());

                    nombreChat = claveDoctor+claveUsuario;
                    Toast.makeText(GenerarConsulta.this, "se creo la consulta", Toast.LENGTH_SHORT).show();

                    crearChat("http://"+getString(R.string.ip)+"/proyecto/mensajes/crear_chat.php");
                    crearConsultaNino("http://"+getString(R.string.ip)+"/proyecto/doctor/crear_consulta_nino.php");

                }
                else{
                    Toast.makeText(this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    private void crearConsultaNino(String URL) {

        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    dialogCarga.dismiss();

                    Toast.makeText(GenerarConsulta.this, "se creo", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GenerarConsulta.this, "hubo un problema", Toast.LENGTH_SHORT).show();
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
                parametros.put("tipo_consulta",spinnerEspecialidad.getSelectedItem().toString());
                parametros.put("doctora",nombreDoctor);
                parametros.put("fehca_cita",fecha);
                parametros.put("hora_cita",hora);
                parametros.put("tutor","padre");
                parametros.put("nombre_tt",nombreUsuario);
                parametros.put("fecha_nac",fechaNacimientoUsuario);
                parametros.put("edad",edadUsuario);
                parametros.put("tipo_sangre",tipoSangreUsuario);
                parametros.put("telefono",telefonoUsuario);
                parametros.put("correo",correoUsuario);
                parametros.put("enfermedad",enfermedad);
                parametros.put("enfermedad_info",edtDatosEnfermedadUsuario.getText().toString());
                parametros.put("medicacion",medicina);
                parametros.put("info_medicacion",edtDatosMedicinaUsuario.getText().toString());

                parametros.put("nombre_nino",edtNombreNino.getText().toString());
                parametros.put("fecha_nac_nino",edtFechaNacimientoNino.getText().toString());
                parametros.put("tipo_sangre_nino",edtTipoSangreNino.getText().toString());
                parametros.put("edad_nino",edtEdadNino.getText().toString());
                parametros.put("enfermedad_nino",enfermedadNino);
                parametros.put("enfermedad_nino_info",enfermedadNino_info);
                parametros.put("medicacion_nino",medicacionNino);
                parametros.put("medicacion_nino_info",medicacionNino_info);
                parametros.put("hermano_nino",hermanoNino);
                parametros.put("hermano_nino_info",hermanoNino_info);

                parametros.put("pa","Si");
                parametros.put("cantidad", edtCostoConsulta.getText().toString());
                parametros.put("claveC",claveUsuario);
                parametros.put("claveD",claveDoctor);

                return parametros;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void traerHora(String toString) {

        if (toString.equals("9 a.m")){
            hora ="9:00:00";
        }

        if (toString.equals("10 a.m")){
            hora ="10:00:00";
        }
        if (toString.equals("11 a.m")){
            hora ="11:00:00";
        }
        if (toString.equals("12 p.m")){
            hora ="12:00:00";
        }
        if (toString.equals("1 p.m")){
            hora ="13:00:00";
        }
        if (toString.equals("2 p.m")){
            hora ="14:00:00";
        }
        if (toString.equals("3 p.m")){
            hora ="15:00:00";
        }
        if (toString.equals("4 p.m")){
            hora ="16:00:00";
        }
        if (toString.equals("5 p.m")){
            hora ="17:00:00";
        }
        if (toString.equals("6 p.m")){
            hora ="18:00:00";
        }
        if (toString.equals("7 p.m")){
            hora ="19:00:00";
        }
        if (toString.equals("8 p.m")){
            hora ="20:00:00";
        }
    }


    private void crearConsultaAdulto(String URL) {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    dialogCarga.dismiss();

                    Toast.makeText(GenerarConsulta.this, "se creo", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GenerarConsulta.this, "hubo un problema", Toast.LENGTH_SHORT).show();
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
                parametros.put("tipo_consulta",spinnerEspecialidad.getSelectedItem().toString());
                parametros.put("doctora",nombreDoctor);
                parametros.put("fehca_cita",fecha);
                parametros.put("hora_cita",hora);
                parametros.put("genero",generoUsuario);
                parametros.put("nombre",nombreUsuario);
                parametros.put("fecha_nac",fechaNacimientoUsuario);
                parametros.put("edad",edadUsuario);
                parametros.put("tipo_sangre",tipoSangreUsuario);
                parametros.put("telefono",telefonoUsuario);
                parametros.put("correo",correoUsuario);
                parametros.put("enfermedad",enfermedad);
                parametros.put("enfermedad_info",edtDatosEnfermedadUsuario.getText().toString());
                parametros.put("medicacion",medicina);
                parametros.put("info_medicacion",edtDatosMedicinaUsuario.getText().toString());
                parametros.put("pa","Si");
                parametros.put("cantidad", edtCostoConsulta.getText().toString());
                parametros.put("claveC",claveUsuario);
                parametros.put("claveD",claveDoctor);

                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void crearChat(String URL) {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    dialogCarga.dismiss();



                    database.getReference()
                            .child("chats")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                                        String nombreCat= dataSnapshot.getKey();
                                       if (nombreCat.equals(nombreChat)){
                                           creado=true;
                                       }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    Toast.makeText(GenerarConsulta.this, ""+creado, Toast.LENGTH_SHORT).show();

                    if (creado == false) {



                        //llamamos los datos de la fecha y hora actual
                        Date date = new Date();
                        HashMap<String, Object> datosChatFirebaseUsuario = new HashMap<>();


                        datosChatFirebaseUsuario.put(Constans.NOMBRE_CHAT, nombreDoctor);

                        datosChatFirebaseUsuario.put(Constans.TIPO_MENSAJE_CHAT, 0);
                        datosChatFirebaseUsuario.put(Constans.ULTIMO_MENSAJE_CHAT, "");
                        datosChatFirebaseUsuario.put(Constans.HORA_ULTIMO_MENSAJE, date.getTime());

                        datosChatFirebaseUsuario.put(Constans.NUMERO_MENSAJES_NUEVOS,0);


                        HashMap<String, Object> datosChatFirebaseDoctor = new HashMap<>();


                        datosChatFirebaseDoctor.put(Constans.NOMBRE_CHAT, nombreUsuario);

                        datosChatFirebaseDoctor.put(Constans.TIPO_MENSAJE_CHAT, 0);
                        datosChatFirebaseDoctor.put(Constans.ULTIMO_MENSAJE_CHAT, "");
                        datosChatFirebaseDoctor.put(Constans.HORA_ULTIMO_MENSAJE, date.getTime());

                        datosChatFirebaseDoctor.put(Constans.NUMERO_MENSAJES_NUEVOS,0);



                        database.getReference().child("chats").child(claveUsuario).child(claveDoctor)
                        .updateChildren(datosChatFirebaseUsuario);
                        database.getReference().child("chats").child(claveDoctor).child(claveUsuario)
                                .updateChildren(datosChatFirebaseDoctor);
                        Toast.makeText(GenerarConsulta.this, "se creo el chat", Toast.LENGTH_SHORT).show();


                    }
                    else {
                        creado = false;
                    }
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
                parametros.put("doctor",nombreDoctor);
                parametros.put("cliente",nombreUsuario);
                parametros.put("claveDoctor",claveDoctor);
                parametros.put("claveCliente",claveUsuario);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }



    public void ventanaDoctor(){
        listDoctores.clear();

        dialogBuilder= new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.ventana_emergente_doctor,null);
        recyclerView = view.findViewById(R.id.doctorCrearConsultaReciclerView);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://"+getString(R.string.ip)+"/proyecto/doctor/obtener_doctor.php", response -> {
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);

                    String nombre = jsonObject.getString("nombre");
                    String clave = jsonObject.getString("clave");

                    String id1= jsonObject.getString("id_especialidad1");
                    String id2= jsonObject.getString("id_especialidad2");
                    String id3= jsonObject.getString("id_especialidad3");
                    String id4= jsonObject.getString("id_especialidad4");
                    String id5= jsonObject.getString("id_especialidad5");

                    listDoctores.add(new DoctorConsulta(nombre,clave,id1,id2,id3,id4,id5));


                } catch (JSONException e) {

                    Toast.makeText(GenerarConsulta.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            adapterDoctor.notifyDataSetChanged();


        }, error -> Toast.makeText(GenerarConsulta.this, error.getMessage(),Toast.LENGTH_SHORT).show());

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterDoctor = new AdapterDoctorConsulta(getApplicationContext(), listDoctores);
        recyclerView.setAdapter(adapterDoctor);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();


    }





    public void ventanaCliente(){
        listUsarios.clear();

        dialogBuilder= new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.ventana_emegente_usuario,null);
        recyclerView = view.findViewById(R.id.usuarioCrearConsultaReciclerView);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://"+getString(R.string.ip)+"/proyecto/cliente/obtener_usuarios.php", response -> {
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);


                    String nombre = jsonObject.getString("Nombre");
                    String usuario = jsonObject.getString("usuario");
                    String clave = jsonObject.getString("clave");
                    String genero = jsonObject.getString("genero");
                    String tipoSangre = jsonObject.getString("tipo_sangre");
                    String edad = jsonObject.getString("edad");
                    String correo = jsonObject.getString("correo");
                    String telefono = jsonObject.getString("Telefono");
                    String fechaNacimiento = jsonObject.getString("fechaNacimiento");

                    listUsarios.add(new UsuarioConsulta(nombre,usuario,clave,genero,tipoSangre,edad,correo,telefono,fechaNacimiento));


                } catch (JSONException e) {

                    Toast.makeText(GenerarConsulta.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            adapterUsuario.notifyDataSetChanged();


        }, error -> Toast.makeText(GenerarConsulta.this, error.getMessage(),Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterUsuario = new AdapterUsuarioConsulta(getApplicationContext(), listUsarios);
        recyclerView.setAdapter(adapterUsuario);


        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
    }


    private void showDatePickerDailog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


            fecha = year + "-" + month + "-" + dayOfMonth;


            if (month == 0) {
                meses = "Ene";
            }
            if (month == 1) {
                meses = "Feb";
            }
            if (month == 2) {
                meses = "Mar";
            }
            if (month == 3) {
                meses = "Abr";
            }
            if (month == 4) {
                meses = "May";
            }
            if (month == 5) {
                meses = "Jun";
            }
            if (month == 6) {
                meses = "Jul";
            }
            if (month == 7) {
                meses = "Ago";
            }
            if (month == 8) {
                meses = "Sep";
            }
            if (month == 9) {
                meses = "Oct";
            }
            if (month == 10) {
                meses = "Nov";
            }
            if (month == 11) {
                meses = "Dic";
            }

            edtFechaConsulta.setText(dayOfMonth + "-" + meses + "-" + year);
    }

    private void showTimePickerDailog(){
        Calendar calendar= Calendar.getInstance();

        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos =calendar.get(Calendar.MINUTE);



        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {

                    minutosVer = minute+"";

                    if (hourOfDay<13){
                        formato ="A.M";
                    }
                    else {
                        formato ="P.M";
                    }

                    if (hourOfDay==0){ horaVer="12";}
                    if (hourOfDay==1){ horaVer="01";}
                    if (hourOfDay==2){ horaVer="02";}
                    if (hourOfDay==3){ horaVer="03";}
                    if (hourOfDay==4){ horaVer="04";}
                    if (hourOfDay==5){ horaVer="05";}
                    if (hourOfDay==6){ horaVer="06";}
                    if (hourOfDay==7){ horaVer="07";}
                    if (hourOfDay==8){ horaVer="08";}
                    if (hourOfDay==9){ horaVer="09";}
                    if (hourOfDay==10){ horaVer="10";}
                    if (hourOfDay==11){ horaVer="11";}
                    if (hourOfDay==12){ horaVer="12";}

                    if (hourOfDay==13){ horaVer="01";}
                    if (hourOfDay==14){ horaVer="02";}
                    if (hourOfDay==15){ horaVer="03";}
                    if (hourOfDay==16){ horaVer="04";}
                    if (hourOfDay==17){ horaVer="05";}
                    if (hourOfDay==18){ horaVer="06";}
                    if (hourOfDay==19){ horaVer="07";}
                    if (hourOfDay==20){ horaVer="08";}
                    if (hourOfDay==21){ horaVer="09";}
                    if (hourOfDay==22){ horaVer="10";}
                    if (hourOfDay==23){ horaVer="11";}

                    if (minute ==0){minutosVer ="00";}
                    if (minute ==1){minutosVer ="01";}
                    if (minute ==2){minutosVer ="02";}
                    if (minute ==3){minutosVer ="03";}
                    if (minute ==4){minutosVer ="04";}
                    if (minute ==5){minutosVer ="05";}
                    if (minute ==6){minutosVer ="06";}
                    if (minute ==7){minutosVer ="07";}
                    if (minute ==8){minutosVer ="08";}
                    if (minute ==9){minutosVer ="09";}

                    edtHoraConsulta.setText(horaVer+":"+minutosVer+" "+formato);
                },hora,minutos,android.text.format.DateFormat.is24HourFormat(getApplicationContext())
        );

        timePickerDialog.show();
    }
}