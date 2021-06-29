package com.example.jdplus.utilities;

import java.util.HashMap;

public class Constans {

    public static final String REMOTE_AUTHORIZATION = "Authorization";
    public static final String REMOTE_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_TYPE = "type";
    public static final String REMOTE_INVITATION = "invitation";
    public static final String REMOTE_MEETING_TYPE = "meetingType";
    public static final String REMOTE_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_DATA = "data";
    public static final String REMOTE_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_INVITATION_RESPONSE = "invitationResponse";

    public static final String REMOTE_INVITATION_ACCEPTED = "accepted";
    public static final String REMOTE_INVITATION_REJECTED = "rejected";
    public static final String REMOTE_INVITATION_CANCELLED = "cancelled";

    public static final String REMOTE_MEETING_ROOM = "meetingRoom";


    public static HashMap<String,String> obtenerClaveCloudMessaging(){
        HashMap<String,String> header = new HashMap<>();
        header.put(
                Constans.REMOTE_AUTHORIZATION,
                "key=AAAAaOdkHrs:APA91bH4XnpjrbssrcRdAdbqNospmaSxpfHAhU1AZZbuUcJE51YVBqm4yQCURj0DeObULlPK3Uiyyuft6dPdndzQkHZMLrXwH7VEznysQkZfH_4lKMWVAyGIBupqQUuIuPDwAlkQfHBk"
        );
        header.put(Constans.REMOTE_CONTENT_TYPE,"application/json");
        return header;
    }
}
