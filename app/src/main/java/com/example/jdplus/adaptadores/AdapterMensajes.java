package com.example.jdplus.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jdplus.Activities.ImagenChatActivity;
import com.example.jdplus.R;
import com.example.jdplus.objetos.Mensaje;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterMensajes extends RecyclerView.Adapter{

    Context context;
    ArrayList<Mensaje> listMensaje;

    final int ITEM_SENT=1;
    final int ITEM_RECEIVE=2;
    final int ITEM_DATE=3;

    SharedPreferences sharedPreferences;
    public AdapterMensajes(Context context, ArrayList<Mensaje> listMensaje){
        this.context =context;
        this.listMensaje=listMensaje;

        sharedPreferences= context.getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType ==ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_send,parent,false);
            return new EnviarViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false);
            return new RecibirViewHolder(view);
        }



    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje =  listMensaje.get(position);
        String clave =sharedPreferences.getString("clave","");

        if (clave.equals(mensaje.getClaveEnvia())) {
            return ITEM_SENT;
        }
        else {
            return ITEM_RECEIVE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Mensaje mensaje =  listMensaje.get(position);
        SimpleDateFormat horaFormato= new SimpleDateFormat("hh:mm a");

        SimpleDateFormat diaFormato= new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();

        String fechaActual = diaFormato.format(date.getTime());
        String fechaMensaje = diaFormato.format(mensaje.getHora());

        String dia ="";

        if (fechaActual.equals(fechaMensaje)){
            dia ="Hoy";
        }
        else {
            dia =fechaMensaje;
        }

        if (holder.getClass() == EnviarViewHolder.class){
            EnviarViewHolder enviarViewHolder = (EnviarViewHolder) holder;
            enviarViewHolder.mensaje.setText(mensaje.getMensaje());


            enviarViewHolder.horaMensaje.setText(dia +" "+horaFormato.format(new Date(mensaje.getHora())));

            enviarViewHolder.imageMensaje.setVisibility(View.GONE);
            enviarViewHolder.mensaje.setVisibility(View.VISIBLE);

            if (mensaje.getTipo() ==2) {
                enviarViewHolder.imageMensaje.setVisibility(View.VISIBLE);
                enviarViewHolder.mensaje.setVisibility(View.GONE);


                Glide.with(context)
                        .load("http://" + context.getString(R.string.ip) + mensaje.getImagenUrl())
                        .placeholder(R.mipmap.placeholder)
                        .into(enviarViewHolder.imageMensaje);

            }


                enviarViewHolder.imageMensaje.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(context, ImagenChatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("urlImagen","http://"+context.getString(R.string.ip)+mensaje.getImagenUrl());
                        intent.putExtra("claveEnvia",mensaje.getClaveEnvia());
                        intent.putExtra("hora",mensaje.getHora());

                        context.startActivity(intent);


                    }
                });


        }
        else {
            RecibirViewHolder recibirViewHolder = (RecibirViewHolder) holder;



            recibirViewHolder.mensaje.setText(mensaje.getMensaje());

            recibirViewHolder.horaMensaje.setText(dia +" "+horaFormato.format(new Date(mensaje.getHora())));

            recibirViewHolder.imageMensaje.setVisibility(View.GONE);

            recibirViewHolder.mensaje.setVisibility(View.VISIBLE);

            if (mensaje.getTipo() ==2) {
                recibirViewHolder.imageMensaje.setVisibility(View.VISIBLE);
                recibirViewHolder.mensaje.setVisibility(View.GONE);

                Glide.with(context)
                        .load("http://"+context.getString(R.string.ip)+mensaje.getImagenUrl())
                        .placeholder(R.mipmap.placeholder)
                        .into(recibirViewHolder.imageMensaje);

                recibirViewHolder.imageMensaje.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(context, ImagenChatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("urlImagen","http://"+context.getString(R.string.ip)+mensaje.getImagenUrl());
                        intent.putExtra("claveEnvia",mensaje.getClaveEnvia());
                        intent.putExtra("hora",mensaje.getHora());

                        context.startActivity(intent);


                    }
                });
            }


        }


    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    public class EnviarViewHolder extends RecyclerView.ViewHolder{
        TextView mensaje,horaMensaje;
        ImageView imageMensaje;

        public EnviarViewHolder(@NonNull View itemView) {
            super(itemView);

            mensaje =itemView.findViewById(R.id.mensajeEnviado);
            imageMensaje= itemView.findViewById(R.id.imagenEnvia);
            horaMensaje =itemView.findViewById(R.id.horaMensajeEnviado);

        }
    }

    public class RecibirViewHolder extends RecyclerView.ViewHolder{
        TextView mensaje,horaMensaje;
        ImageView imageMensaje;

        public RecibirViewHolder(@NonNull View itemView) {
            super(itemView);

            mensaje =  itemView.findViewById(R.id.mensajeRecibido);
            imageMensaje= itemView.findViewById(R.id.imagenRecibe);
            horaMensaje=itemView.findViewById(R.id.horaMensajeRecibido);

        }
    }

}
