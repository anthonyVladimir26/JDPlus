package com.example.jdplus.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jdplus.Activities.BandejaMensajesActivity;
import com.example.jdplus.R;
import com.example.jdplus.objetos.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterUsuario extends  RecyclerView.Adapter<AdapterUsuario.UsersViewHolder>{

    Context context;
    ArrayList<Usuarios> listUsuarios;




    public AdapterUsuario(Context context, ArrayList<Usuarios>listUsuarios){
        this.context=context;
        this.listUsuarios=listUsuarios;


    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_chat,parent,false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        Usuarios usuarios = listUsuarios.get(position);

        String idEnvia= FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String usuario = documentSnapshot.getString("usuario");
                                if (usuario.equals(usuarios.getUsuario())){

                                    String idRecibe =documentSnapshot.getId();
                                    String senderRoom = idEnvia+ idRecibe;

                                    FirebaseMessaging.getInstance()
                                            .subscribeToTopic(idRecibe)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override

                                                public void onSuccess(Void aVoid) {

                                                    Toast.makeText(context, "regitrado", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("chats")
                                            .child(senderRoom)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                                                        Long time = snapshot.child("lastMsgTime").getValue(Long.class);

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
                                                        holder.chat.setText(lastMsg);

                                                    }
                                                    else {
                                                        holder.chat.setText("Presiona para charlar");
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                }
                            }
                        }
                    }
                });

        holder.nombre.setText(usuarios.getNombre());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BandejaMensajesActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("nombre", usuarios.getNombre());
                intent.putExtra("clave", usuarios.getClave());
                intent.putExtra("correo", usuarios.getCorreo());
                intent.putExtra("id", usuarios.getId());
                intent.putExtra("usuario", usuarios.getUsuario());

                //og.d("notificaion","recibe" +recievedRoom);
                //og.d("notificaion","envia" +senderRoom);
                //
                //irebaseMessaging.getInstance()
                //       .subscribeToTopic(senderRoom)
                //       .addOnSuccessListener(new OnSuccessListener<Void>() {
                //           @Override
                //           public void onSuccess(Void aVoid) {
                //
                //               FirebaseMessaging.getInstance()
                //                       .subscribeToTopic(recievedRoom)
                //                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                //                           @Override
                //
                //                           public void onSuccess(Void aVoid) {
                //
                //                               context.startActivity(intent);
                //                           }
                //                       });
                //           }
                //       });
                //
                //
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUsuarios.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, chat,hora;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre= itemView.findViewById(R.id.nombre);
            chat = itemView.findViewById(R.id.chat);
            hora = itemView.findViewById(R.id.hora);
        }
    }
}
