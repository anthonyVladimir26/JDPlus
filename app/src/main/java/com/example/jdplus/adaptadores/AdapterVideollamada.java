package com.example.jdplus.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jdplus.R;
import com.example.jdplus.listeners.UsersListeners;
import com.example.jdplus.objetos.Usuario;

import java.util.List;

public class AdapterVideollamada extends RecyclerView.Adapter<AdapterVideollamada.ViewHolder>{


    private List<Usuario> usuarios;
    Context context;
    private UsersListeners usersListeners;

    public AdapterVideollamada(Context context, List<Usuario> usuarios, UsersListeners usersListeners){
        this.context =context;
        this.usuarios = usuarios;
        this.usersListeners = usersListeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_videollamada,
                parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setUserData(usuarios.get(position));

    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        LinearLayout layout;
        ViewHolder(@NonNull View itemView){
            super(itemView);

            layout = itemView.findViewById(R.id.layoutVideoLlamada);

        }

        void setUserData(Usuario usuario){


            layout.setOnClickListener(v -> usersListeners.initiateVideoMeeting(usuario));


        }

    }
}
