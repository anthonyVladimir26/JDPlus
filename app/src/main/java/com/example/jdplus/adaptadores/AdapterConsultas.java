package com.example.jdplus.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jdplus.R;
import com.example.jdplus.objetos.Consultas;

import java.util.ArrayList;

public class AdapterConsultas  extends RecyclerView.Adapter<AdapterConsultas.ViewHolder> implements View.OnClickListener{


    LayoutInflater inflater;
    ArrayList<Consultas> listVerConsultas;

    private View.OnClickListener listener;

    public AdapterConsultas(Context context, ArrayList<Consultas> listVerConsultas){
        this.inflater = LayoutInflater.from(context);
        this.listVerConsultas =listVerConsultas;
    }


    public void setonClickListener(View.OnClickListener listener){
        this.listener =listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!= null){
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.consultas_card_view,parent,false);

        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*String nombre = listVerConsultas.get(position).getNombre();
        String dia = listVerConsultas.get(position).getFecha_cita();
        String hora = listVerConsultas.get(position).getHora_cita();
        String especialidad = listVerConsultas.get(position).getTipo_consulta();

        holder.nombrePaciente.setText(nombre);
        holder.dia.setText(dia);
        holder.hora.setText(hora);
        holder.especialidad.setText(especialidad);*/
    }

    @Override
    public int getItemCount() {
        return listVerConsultas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePaciente,hora, especialidad, dia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*nombrePaciente = itemView.findViewById(R.id.nombrePaciente);
            hora = itemView.findViewById(R.id.horaConsulta);
            especialidad = itemView.findViewById(R.id.especialidad);
            dia = itemView.findViewById(R.id.diaConsulta);*/
        }
    }
}
