package com.example.jdplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class GenerarConsulta extends AppCompatActivity {

    EditText edtnombrechat,edtdoctor, edtcliente;
    Button subir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_consulta);

        edtnombrechat = findViewById(R.id.chat_nombre);
        edtdoctor = findViewById(R.id.doctor_nombre);
        edtcliente = findViewById(R.id.cliente_nombre);
        subir= findViewById(R.id.subir_datos);

        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearChat("http://192.168.1.70:8080/archivosPHP/mensajes/insertar_nuevo_chat.php");

            }
        });


    }

    private void crearChat (String  URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){

                    Toast.makeText(getApplicationContext(),"AGREGADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"NO SE AGREGO CORRECTAMENTE",Toast.LENGTH_SHORT).show();
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
                parametros.put("nombreChat",edtnombrechat.getText().toString());
                parametros.put("doctor",edtdoctor.getText().toString());
                parametros.put("cliente",edtcliente.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}