package com.example.jdplus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdplus.R;
import com.example.jdplus.utilities.Constans;

import com.example.jdplus.network.ApiClient;
import com.example.jdplus.network.ApiService;
import com.example.jdplus.objetos.Usuario;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearVideollamada extends AppCompatActivity {

    private String invitacionToken = null;
    String meetingRoom = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_videollamada);



        ImageView imagenVideoLlamada = findViewById(R.id.imagenAceptarVideollamada);
        imagenVideoLlamada.setImageResource(R.drawable.ic_video_llamada);
        String tipoLlamada = getIntent().getStringExtra("tipo");

        TextView txtNombre = findViewById(R.id.textNombreUsuario);

        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        if (usuario != null){
            txtNombre.setText(usuario.getNombre());
        }

        ImageView imagenDetenerVideollamada = findViewById(R.id.rechazarPararLlamada);

        if (tipoLlamada!= null && usuario!= null)  {


        }

        imagenDetenerVideollamada.setOnClickListener(v -> {
            if (usuario!= null) {
                cancelarInvitacion(usuario.getFcm_token());
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult()!= null) {
                        invitacionToken = task.getResult();
                        if (usuario!= null){
                            iniciarLlamada(tipoLlamada, usuario.getFcm_token());
                        }
                    }
                });

    }

    private void iniciarLlamada (String tipo,String token){
        try {


            SharedPreferences preferences = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);

            String nombre = preferences.getString("nombre","");
            JSONArray tokens = new JSONArray();
            tokens.put(token);

            JSONObject body = new JSONObject();
            JSONObject data =new JSONObject();

            data.put(Constans.REMOTE_TYPE,Constans.REMOTE_INVITATION);
            data.put(Constans.REMOTE_MEETING_TYPE, tipo);
            data.put("nombre",nombre);
            data.put(Constans.REMOTE_INVITER_TOKEN,invitacionToken);

            meetingRoom = preferences.getString("id","")+" "+
                    UUID.randomUUID().toString().substring(0,5);

            data.put(Constans.REMOTE_MEETING_ROOM, meetingRoom);

            body.put(Constans.REMOTE_DATA, data);
            body.put(Constans.REMOTE_REGISTRATION_IDS,tokens);


            enviarLlamada(body.toString(),Constans.REMOTE_INVITATION);


        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void enviarLlamada (String remoteMessageBody, String type){
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                Constans.obtenerClaveCloudMessaging(), remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if (response.isSuccessful()){
                    if (type.equals(Constans.REMOTE_INVITATION)){

                    }
                    else if (type.equals(Constans.REMOTE_INVITATION_RESPONSE)){
                        Toast.makeText(CrearVideollamada.this, "Invitacion cancelada", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    Toast.makeText(CrearVideollamada.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Toast.makeText(CrearVideollamada.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private void cancelarInvitacion( String token){
        try {



            JSONArray tokens = new JSONArray();
            tokens.put(token);

            JSONObject body = new JSONObject();
            JSONObject data =new JSONObject();

            data.put(Constans.REMOTE_TYPE,Constans.REMOTE_INVITATION_RESPONSE);
            data.put(Constans.REMOTE_INVITATION_RESPONSE, Constans.REMOTE_INVITATION_CANCELLED);

            body.put(Constans.REMOTE_DATA, data);
            body.put(Constans.REMOTE_REGISTRATION_IDS,tokens);


            enviarLlamada(body.toString(), Constans.REMOTE_INVITATION_RESPONSE);


        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private BroadcastReceiver invitadoRecibe = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String tipo = intent.getStringExtra(Constans.REMOTE_INVITATION_RESPONSE);
            if (tipo!= null){
                if (tipo.equals(Constans.REMOTE_INVITATION_ACCEPTED)){
                    try {
                        URL serverURL = new URL("https://meet.jit.si");
                        JitsiMeetConferenceOptions conferenceOptions=
                                new JitsiMeetConferenceOptions.Builder()
                                        .setServerURL(serverURL)
                                        .setWelcomePageEnabled(false)
                                        .setRoom(meetingRoom)
                                        .build();

                        JitsiMeetActivity.launch(CrearVideollamada.this, conferenceOptions);
                        finish();
                    }catch (Exception e){
                        Toast.makeText(CrearVideollamada.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                if (tipo.equals(Constans.REMOTE_INVITATION_REJECTED)){
                    Toast.makeText(context, "Llamada rechazada", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitadoRecibe,
                new IntentFilter(Constans.REMOTE_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitadoRecibe
        );
    }
}