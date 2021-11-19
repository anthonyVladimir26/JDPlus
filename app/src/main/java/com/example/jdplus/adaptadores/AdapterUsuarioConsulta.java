package com.example.jdplus.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jdplus.Activities.GenerarConsulta;
import com.example.jdplus.R;
import com.example.jdplus.objetos.UsuarioConsulta;

import java.util.List;

public class AdapterUsuarioConsulta extends RecyclerView.Adapter<AdapterUsuarioConsulta.ViewHolder>{

    Context context;
    List<UsuarioConsulta> listUsuario;


      public AdapterUsuarioConsulta(Context context, List<UsuarioConsulta> listUsuario){
          this.context =context;
          this.listUsuario =listUsuario;
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
            holder.nombre.setText(listUsuario.get(position).getNombre());
        }

        @Override
        public int getItemCount() {
            return listUsuario.size();
        }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreMensaje);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "se eligio: "+ listUsuario.get(getAdapterPosition()).getNombre(), Toast.LENGTH_SHORT).show();

                    GenerarConsulta.nombreUsuario =listUsuario.get(getAdapterPosition()).getNombre();
                    GenerarConsulta.txvnombreUsuario.setText(GenerarConsulta.nombreUsuario);
                    GenerarConsulta.userUsuario = listUsuario.get(getAdapterPosition()).getUsuario();
                    GenerarConsulta.claveUsuario =listUsuario.get(getAdapterPosition()).getClave();

                    GenerarConsulta.generoUsuario = listUsuario.get(getAdapterPosition()).getGenero();
                    GenerarConsulta.edtGeneroUsuario.setText(GenerarConsulta.generoUsuario);

                    GenerarConsulta.tipoSangreUsuario = listUsuario.get(getAdapterPosition()).getTipoSangre();
                    GenerarConsulta.edtTipoSangreUsuario.setText(GenerarConsulta.tipoSangreUsuario);

                    GenerarConsulta.edadUsuario = listUsuario.get(getAdapterPosition()).getEdad();
                    GenerarConsulta.edtEdadUsuario.setText(GenerarConsulta.edadUsuario);

                    GenerarConsulta.correoUsuario = listUsuario.get(getAdapterPosition()).getCorreo();
                    GenerarConsulta.edtCorreoUsuario.setText(GenerarConsulta.correoUsuario);

                    GenerarConsulta.telefonoUsuario = listUsuario.get(getAdapterPosition()).getTelefono();
                    GenerarConsulta.edtTelefonoUsuario.setText(GenerarConsulta.telefonoUsuario);

                    GenerarConsulta.fechaNacimientoUsuario = listUsuario.get(getAdapterPosition()).getFechaNacimiento();
                    GenerarConsulta.edtFechaNacimiento.setText(GenerarConsulta.fechaNacimientoUsuario);


                    if ( GenerarConsulta.generoUsuario.equals("Hombre")) {
                        GenerarConsulta.imagenGenero.setImageResource(R.mipmap.masculino);
                    }else {
                        GenerarConsulta.imagenGenero.setImageResource(R.mipmap.femenino);
                    }
                    GenerarConsulta.imagenUsuario.setVisibility(View.GONE);
                    GenerarConsulta.linearDatosUsuario.setVisibility(View.VISIBLE);
                    GenerarConsulta.dialog.dismiss();
                }
            });
        }
    }
}
