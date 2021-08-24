package com.example.jdplus;

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
import com.example.jdplus.adaptadores.AdapterDoctorConsulta;
import com.example.jdplus.adaptadores.AdapterUsuarioConsulta;
import com.example.jdplus.objetos.DoctorConsulta;
import com.example.jdplus.objetos.UsuarioConsulta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerarConsulta extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    RequestQueue requestQueue;

    ProgressDialog dialogCarga;

    LinearLayout linearDoctor, linearUsuario;

    public static LinearLayout linearDatosUsuario, linearDatosDoctor;

    String meses;
    String horaVer;
    String minutosVer;


    String formato ="";

    AlertDialog.Builder dialogBuilder;
    public static AlertDialog dialog;

    AdapterDoctorConsulta adapterDoctor;
    AdapterUsuarioConsulta adapterUsuario;

    ArrayList<DoctorConsulta> listDoctores;
    List<UsuarioConsulta> listUsarios;

    Spinner spinnerEspecialidad;
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
    public static String usuarioDoctor;

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

    String ip ="192.168.1.69:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_consulta);


        //dialog = new Dialog(this);
        linearDoctor =findViewById(R.id.layout_elegir_doctor);
        linearUsuario =findViewById(R.id.layout_elegir_usuario);
        linearDatosUsuario = findViewById(R.id.datosUsuario);

        //spinner
        spinnerEspecialidad= findViewById(R.id.spinnerEspecialidades);

        //arrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.especialidades_array
                , android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerEspecialidad.setAdapter(adapter);



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

        //editText consulta
        edtFechaConsulta = findViewById(R.id.edtFechaCita);
        edtHoraConsulta = findViewById(R.id.edtHoraCita);
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

        edtHoraConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDailog();
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


        btnCrearConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if  (txvnombreUsuario.getText().toString().equals("Elegir Cliente")){
                    Toast.makeText(GenerarConsulta.this, "Por Favor eliga cliente", Toast.LENGTH_SHORT).show();
                    usuario=false;
                }else {
                    usuario=true;
                }

                if (txvnombreDoctor.getText().toString().equals("Elegir Doctor")){
                    Toast.makeText(GenerarConsulta.this, "Por Favor eliga doctor", Toast.LENGTH_SHORT).show();
                    doctor=false;
                }else {
                    doctor=true;
                }


                if (rbtnEnfermedadSi.isChecked()){
                    enfermedad= "Si";
                    if(edtDatosEnfermedadUsuario.getText().toString().equals("")){
                        Toast.makeText(GenerarConsulta.this, "por favor ingrese los datos de la enfermedad", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GenerarConsulta.this, "por favor ingrese los datos de la medicacion", Toast.LENGTH_SHORT).show();
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

                if (edtFechaConsulta.getText().toString().equals("")){
                    Toast.makeText(GenerarConsulta.this, "por favor ingrese la fecha de la consulta", Toast.LENGTH_SHORT).show();
                    fechaCita=false;
                }else {
                    fechaCita =true;
                }

                if (edtHoraConsulta.getText().toString().equals("")){
                    Toast.makeText(GenerarConsulta.this, "por favor ingrese la hora de la consulta", Toast.LENGTH_SHORT).show();
                    horaCita=false;

                }else {
                    horaCita=true;
                }

                if (edtCostoConsulta.getText().toString().equals("")){
                    Toast.makeText(GenerarConsulta.this, "por favor ingrese el costo de la consulta", Toast.LENGTH_SHORT).show();
                    costoCita=false;
                }else {
                    costoCita=true;
                }
                if (spinnerEspecialidad.getSelectedItem().toString().equals("Seleccione una especialidad")){
                    Toast.makeText(GenerarConsulta.this, "seleccione una especiadlidad valida", Toast.LENGTH_SHORT).show();
                    especialidad=false;
                }else {
                    especialidad =true;
                }

                if (fechaCita&&costoCita&&horaCita&&medicacionUsuario&&enfermedadUsuario&&doctor&&usuario&&especialidad){
                    dialogCarga.setMessage("Creando...");
                    dialogCarga.setCancelable(false);
                    dialogCarga.show();

                    nombreChat = usuarioDoctor+userUsuario;
                    Toast.makeText(GenerarConsulta.this, "se creo la consulta", Toast.LENGTH_SHORT).show();

                    crearChat("http://"+ip+"/proyecto/mensajes/crear_chat.php");
                    crearConsulta("http://"+ip+"/proyecto/doctor/crear_consulta.php");

                }

            }
        });


    }



    private void crearConsulta(String URL) {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    dialogCarga.dismiss();

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
                parametros.put("hora_cita",edtHoraConsulta.getText().toString());
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
                parametros.put("clave",claveUsuario);

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
                parametros.put("usuDoctor",usuarioDoctor);
                parametros.put("usuCliente",userUsuario);
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


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://"+ip+"/proyecto/doctor/obtener_doctor.php", response -> {
            JSONObject jsonObject = null;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);


                    String usuario = jsonObject.getString("usuario");
                    String nombre = jsonObject.getString("nombre");


                    listDoctores.add(new DoctorConsulta(usuario,nombre));


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


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://"+ip+"/proyecto/cliente/obtener_usuarios.php", response -> {
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
        fecha = dayOfMonth +"/"+ month +"/"+year;



        if (month ==0){ meses ="Ene";}
        if (month ==1){ meses ="Feb";}
        if (month ==2){ meses ="Mar";}
        if (month ==3){ meses ="Abr";}
        if (month ==4){ meses ="May";}
        if (month ==5){ meses ="Jun";}
        if (month ==6){ meses ="Jul";}
        if (month ==7){ meses ="Ago";}
        if (month ==8){ meses ="Sep";}
        if (month ==9){ meses ="Oct";}
        if (month ==10){ meses ="Nov";}
        if (month ==11){ meses ="Dic";}

        edtFechaConsulta.setText(dayOfMonth +"/"+ meses +"/"+year);
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