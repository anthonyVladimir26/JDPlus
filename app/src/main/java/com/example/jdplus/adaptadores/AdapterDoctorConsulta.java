package com.example.jdplus.adaptadores;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jdplus.Activities.GenerarConsulta;
import com.example.jdplus.R;
import com.example.jdplus.objetos.DoctorConsulta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterDoctorConsulta extends RecyclerView.Adapter<AdapterDoctorConsulta.ViewHolder>{

    private List<DoctorConsulta> doctores;
    Context context;

    String espe="";

    private View.OnClickListener listener;

    public AdapterDoctorConsulta(Context context, List<DoctorConsulta> doctores){
        this.context =context;
        this.doctores = doctores;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.usuarios_mensajes_card_view,
                parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorConsulta doctorConsulta = doctores.get(position);

        holder.nombre.setText(doctores.get(position).getNombre());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(context, "se eligio: "+ doctorConsulta.getNombre(), Toast.LENGTH_SHORT).show();
                GenerarConsulta.nombreDoctor = doctorConsulta.getNombre();
                GenerarConsulta.claveDoctor =doctorConsulta.getClave();
                GenerarConsulta.txvnombreDoctor.setText(GenerarConsulta.nombreDoctor);
                GenerarConsulta.imagenDoctor.setVisibility(View.GONE);




                //arrayAdapterEspecialidad
                String[] idEspecialidades = new String[5];
                idEspecialidades[0] = doctorConsulta.getId1();
                idEspecialidades[1] = doctorConsulta.getId2();
                idEspecialidades[2] = doctorConsulta.getId3();
                idEspecialidades[3] = doctorConsulta.getId4();
                idEspecialidades[4] = doctorConsulta.getId5();


                String[] especialidades = new String[5];

                for (int i = 0; i < idEspecialidades.length; i++) {


                    if (idEspecialidades[i].equals("1")){
                        especialidades[i]= "Medico Familiar";
                    }
                    if (idEspecialidades[i].equals("2")){
                        especialidades[i]= "Pediatria";
                    }
                    if (idEspecialidades[i].equals("3")){
                        especialidades[i]= "Nefrologia";
                    }
                    if (idEspecialidades[i].equals("4")){
                        especialidades[i]= "Neurologia";
                    }
                    if (idEspecialidades[i].equals("5")){
                        especialidades[i]= "Hematologia";
                    }
                    if (idEspecialidades[i].equals("6")){
                        especialidades[i]= "Ciruguia pediatrica";
                    }
                    if (idEspecialidades[i].equals("7")){
                        especialidades[i]= "Urologia - Adultos";
                    }
                    if (idEspecialidades[i].equals("8")){
                        especialidades[i]= "Urologia Pediatrica - Niños ";
                    }
                    if (idEspecialidades[i].equals("9")){
                        especialidades[i]= "Cardiologia Pediatrica";
                    }
                    if (idEspecialidades[i].equals("10")){
                        especialidades[i]= "Gastroenterologia";
                    }
                    if (idEspecialidades[i].equals("11")){
                        especialidades[i]= "Psicologia infantil ";
                    }
                    if (idEspecialidades[i].equals("12")){
                        especialidades[i]= "Imagenologia";
                    }
                    if (idEspecialidades[i].equals("13")){
                        especialidades[i]= "Terapia del lenguaje";
                    }
                    if (idEspecialidades[i].equals("14")){
                        especialidades[i]= "Nutrición Pediatrica";
                    }
                    if (idEspecialidades[i].equals("15")){
                        especialidades[i]= "Asesoria de Lactancia ";
                    }
                    if (idEspecialidades[i].equals("16")){
                        especialidades[i]= "Endocrinologia Pediatrica";
                    }


                }

                ArrayAdapter<String> adapterSpinnerEspecialidad= new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, especialidades);
                adapterSpinnerEspecialidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                GenerarConsulta.spinnerEspecialidad.setAdapter(adapterSpinnerEspecialidad);
                GenerarConsulta.spinnerEspecialidad.setVisibility(View.VISIBLE);
                GenerarConsulta.dialog.dismiss();
            }
        });
    }



    public String conseguirEspecialidad (String especialidad){
        final String[] a = {""};

        
        if (especialidad.equals("0") || especialidad.isEmpty()){
            return null;

        }
        else {

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://"+context.getString(R.string.ip)+"/proyecto/doctor/buscar_especialidad.php?id="+especialidad, response -> {
                JSONObject jsonObject = null;
                String esp = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                         esp =jsonObject.getString("nombre");
                        
                        Toast.makeText(context, esp, Toast.LENGTH_SHORT).show();
                       


                    } catch (JSONException e) {

                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }, error -> Toast.makeText(context, error.getMessage(),Toast.LENGTH_SHORT).show());

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonArrayRequest);

            

            Toast.makeText(context, a[0], Toast.LENGTH_SHORT).show();
            return a[0];
        }

    }



    @Override
    public int getItemCount() {
        return doctores.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreMensaje);


        }
    }



}
