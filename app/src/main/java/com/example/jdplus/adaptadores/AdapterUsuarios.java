package com.example.jdplus.adaptadores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jdplus.R;
import com.example.jdplus.listeners.UsersListeners;
import com.example.jdplus.objetos.Usuario;

import java.util.List;


public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.UserViewHolder>{

    private List<Usuario> usuarios;
    Context context;
    private UsersListeners usersListeners;

    public AdapterUsuarios(Context context,List<Usuario> usuarios, UsersListeners usersListeners){
        this.context =context;
        this.usuarios = usuarios;
        this.usersListeners = usersListeners;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.prueba_video_llamada,
                parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(usuarios.get(position));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView nombre;
        ImageView videoLlamada;
        UserViewHolder(@NonNull View itemView){
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreMensaje);
            videoLlamada = itemView.findViewById(R.id.imgVideoLlamada);
        }

        void setUserData(Usuario usuario){
            nombre.setText(usuario.getNombre());

            videoLlamada.setOnClickListener(v -> usersListeners.initiateVideoMeeting(usuario));


        }

    }

}
