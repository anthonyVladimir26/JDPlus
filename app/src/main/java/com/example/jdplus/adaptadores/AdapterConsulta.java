package com.example.jdplus.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jdplus.Activities.DatosConsulta;
import com.example.jdplus.R;
import com.example.jdplus.objetos.Consultas;

import java.util.ArrayList;

public class AdapterConsulta extends RecyclerView.Adapter<AdapterConsulta.ViewHolder>{


    Context context;
    ArrayList<Consultas> listConsultas;

    public AdapterConsulta(Context context, ArrayList<Consultas> listConsultas){
        this.context = context;
        this.listConsultas = listConsultas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.consultas_card_view,
                parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Consultas consultas = listConsultas.get(position);

        holder.tvNombrePaciente.setText(consultas.getNombreCliente());
        holder.tvDia.setText(consultas.getFechaConsulta());
        holder.tvHora.setText(consultas.getHoraConsulta());
        holder.tvEspecialidad.setText(consultas.getEspecialidad());


       holder.layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, DatosConsulta.class);
               intent.putExtra("tipo",consultas.getTipo());
               intent.putExtra("id",consultas.getIdConsulta());
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return listConsultas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvNombrePaciente,tvHora, tvEspecialidad, tvDia;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombrePaciente = itemView.findViewById(R.id.nombrePaciente_cardview_consultas);
            tvHora = itemView.findViewById(R.id.horaConsulta_cardview_consultas);
            tvEspecialidad = itemView.findViewById(R.id.especialidad_cardview_consultas);
            tvDia = itemView.findViewById(R.id.diaConsulta_cardview_consultas);

            layout = itemView.findViewById(R.id.layout_cardview_consultas);

        }
    }
}
