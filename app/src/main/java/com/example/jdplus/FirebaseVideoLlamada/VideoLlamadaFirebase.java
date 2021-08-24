package com.example.jdplus.FirebaseVideoLlamada;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.jdplus.InvitacionVideollamada;
import com.example.jdplus.utilities.Constans;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class VideoLlamadaFirebase extends FirebaseMessagingService {
    Context context;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(Constans.REMOTE_TYPE);

        if (type != null){
            if (type.equals(Constans.REMOTE_INVITATION)){
                Intent intent = new Intent(getApplicationContext(), InvitacionVideollamada.class);
                intent.putExtra(Constans.REMOTE_MEETING_TYPE, remoteMessage.getData().get(Constans.REMOTE_MEETING_TYPE));
                intent.putExtra("nombre", remoteMessage.getData().get("nombre"));
                intent.putExtra(Constans.REMOTE_INVITER_TOKEN, remoteMessage.getData().get(Constans.REMOTE_INVITER_TOKEN));
                intent.putExtra(Constans.REMOTE_MEETING_ROOM,remoteMessage.getData().get(Constans.REMOTE_MEETING_ROOM));

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(type.equals(Constans.REMOTE_INVITATION_RESPONSE)){
                Intent intent = new Intent(Constans.REMOTE_INVITATION_RESPONSE);
                intent.putExtra(Constans.REMOTE_INVITATION_RESPONSE, remoteMessage.getData().get(Constans.REMOTE_INVITATION_RESPONSE));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }
    }
}
