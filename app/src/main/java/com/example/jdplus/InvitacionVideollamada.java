package com.example.jdplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdplus.network.ApiClient;
import com.example.jdplus.network.ApiService;
import com.example.jdplus.utilities.Constans;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitacionVideollamada extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitacion_videollamada);

        ImageView imageView = findViewById(R.id.imagenAceptarVideollamada);

        imageView.setImageResource(R.drawable.ic_video_llamada);

        TextView txtNombre = findViewById(R.id.textNombreUsuario);

        txtNombre.setText(getIntent().getStringExtra("nombre"));

        ImageView imgAceptarVideoLlamada = findViewById(R.id.aceptarVideollamadaimg);

        imgAceptarVideoLlamada.setOnClickListener(v -> enviarInvitacion(Constans.REMOTE_INVITATION_ACCEPTED, getIntent().getStringExtra(Constans.REMOTE_INVITER_TOKEN)));

        ImageView imgRechazatVideoLlamada = findViewById(R.id.rechazarVideollamadaimg);
        imgRechazatVideoLlamada.setOnClickListener(v -> enviarInvitacion(Constans.REMOTE_INVITATION_REJECTED, getIntent().getStringExtra(Constans.REMOTE_INVITER_TOKEN)));
    }
    private void enviarInvitacion(String tipo, String token){
        try {



            JSONArray tokens = new JSONArray();
            tokens.put(token);

            JSONObject body = new JSONObject();
            JSONObject data =new JSONObject();

            data.put(Constans.REMOTE_TYPE,Constans.REMOTE_INVITATION_RESPONSE);
            data.put(Constans.REMOTE_INVITATION_RESPONSE, tipo);

            body.put(Constans.REMOTE_DATA, data);
            body.put(Constans.REMOTE_REGISTRATION_IDS,tokens);


            enviarLlamada(body.toString(),tipo);


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
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()){
                    if (type.equals(Constans.REMOTE_INVITATION_ACCEPTED)){

                           try {
                                URL serverURL = new URL("https://meet.jit.si");
                                JitsiMeetConferenceOptions conferenceOptions=
                                        new JitsiMeetConferenceOptions.Builder()
                                                .setServerURL(serverURL)
                                                .setWelcomePageEnabled(false)
                                                .setRoom(getIntent().getStringExtra(Constans.REMOTE_MEETING_ROOM))
                                                .build();

                                JitsiMeetActivity.launch(InvitacionVideollamada.this, conferenceOptions);
                                finish();
                            }catch (Exception e){
                                Toast.makeText(InvitacionVideollamada.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }

                    }else {
                        Toast.makeText(InvitacionVideollamada.this, "llamada Rechazada", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }else {
                    Toast.makeText(InvitacionVideollamada.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Toast.makeText(InvitacionVideollamada.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private BroadcastReceiver invitadoRecibe = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String tipo = intent.getStringExtra(Constans.REMOTE_INVITATION_RESPONSE);
            if (tipo!= null){
                if (tipo.equals(Constans.REMOTE_INVITATION_CANCELLED)){
                    Toast.makeText(context, "Llamada cancelada", Toast.LENGTH_SHORT).show();
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