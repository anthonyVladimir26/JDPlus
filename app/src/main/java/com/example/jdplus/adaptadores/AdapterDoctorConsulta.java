package com.example.jdplus.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jdplus.GenerarConsulta;
import com.example.jdplus.R;
import com.example.jdplus.objetos.DoctorConsulta;

import java.util.List;

public class AdapterDoctorConsulta extends RecyclerView.Adapter<AdapterDoctorConsulta.ViewHolder>{

    private List<DoctorConsulta> doctores;
    Context context;

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
        holder.nombre.setText(doctores.get(position).getNombre());
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "se eligio: "+ doctores.get(getAdapterPosition()).getNombre(), Toast.LENGTH_SHORT).show();
                    GenerarConsulta.nombreDoctor = doctores.get(getAdapterPosition()).getNombre();
                    GenerarConsulta.usuarioDoctor =doctores.get(getAdapterPosition()).getUsuario();
                    GenerarConsulta.txvnombreDoctor.setText(GenerarConsulta.nombreDoctor);
                    GenerarConsulta.imagenDoctor.setVisibility(View.GONE);
                    GenerarConsulta.dialog.dismiss();
                }
            });
        }
    }
}
