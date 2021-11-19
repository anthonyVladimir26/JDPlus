package com.example.jdplus.FirebaseVideoLlamada;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.jdplus.Activities.BandejaMensajesActivity;
import com.example.jdplus.Activities.InvitacionVideollamada;
import com.example.jdplus.R;
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
        else{

            String nombre =remoteMessage.getNotification().getTitle()+"";
            String cuerpo = remoteMessage.getNotification().getBody()+"";

            sendNotification(nombre,cuerpo);

        }
    }

    public void sendNotification( String nombre,String mensaje){

        Intent intent=new Intent(this , BandejaMensajesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);



        String channelId = "1";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_send)
                        .setContentTitle(nombre)
                        .setContentText(mensaje)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}