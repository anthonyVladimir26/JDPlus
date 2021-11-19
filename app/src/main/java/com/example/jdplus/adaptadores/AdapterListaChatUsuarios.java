package com.example.jdplus.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jdplus.Activities.BandejaMensajesActivity;
import com.example.jdplus.R;
import com.example.jdplus.objetos.UsuariosMensajes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterListaChatUsuarios extends RecyclerView.Adapter<AdapterListaChatUsuarios.UsersViewHolder>{

    Context context;
    ArrayList<UsuariosMensajes> listUsuarios;

    public AdapterListaChatUsuarios(Context context, ArrayList<UsuariosMensajes> listUsuarios){
        this.context = context;
        this.listUsuarios = listUsuarios;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_chat,parent,false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {

        UsuariosMensajes usuariosMensajes = listUsuarios.get(position);

        Long time= usuariosMensajes.getHoraYfechaUltimoMensaje();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");

        Date date = new Date();

        String diaActual = formato.format(new Date(date.getTime()));

        String diaMensaje = formato.format(new Date(time));

        if (diaActual.equals(diaMensaje)){

            SimpleDateFormat dateFormat= new SimpleDateFormat("hh:mm a");
            holder.hora.setText(dateFormat.format(new Date(time)));
        }else {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            holder.hora.setText(dateFormat.format(new Date(time)));

        }
        if (usuariosMensajes.getTipoUltimoMensaje() ==2) {
            holder.imagenFoto.setVisibility(View.VISIBLE);
            holder.chat.setVisibility(View.GONE);
        }
        else {
            holder.imagenFoto.setVisibility(View.GONE);
            holder.chat.setVisibility(View.VISIBLE);
            holder.chat.setText(usuariosMensajes.getUltimoMensaje());
        }

        holder.nombre.setText(usuariosMensajes.getNombre());

        if (usuariosMensajes.getNumeroMensajeNuevo() ==0){
            holder.mensajesNuevos.setVisibility(View.GONE);
        }else {
            holder.mensajesNuevos.setVisibility(View.VISIBLE);
            holder.mensajesNuevos.setText("");
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BandejaMensajesActivity.class);
            intent.putExtra("clave",usuariosMensajes.getClave());
            intent.putExtra("nombre",usuariosMensajes.getNombre());
            context.startActivity(intent);


        });

    }

    @Override
    public int getItemCount() {
        return listUsuarios.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, chat,hora,mensajesNuevos;
        ImageView imagenFoto;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre= itemView.findViewById(R.id.nombre);
            chat = itemView.findViewById(R.id.chat);
            hora = itemView.findViewById(R.id.hora);
            mensajesNuevos = itemView.findViewById(R.id.mensajesNuevos);

            imagenFoto = itemView.findViewById(R.id.imagenChatFoto);
        }
    }

}
