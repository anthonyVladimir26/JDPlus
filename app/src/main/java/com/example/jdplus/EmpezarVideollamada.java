package com.example.jdplus;


import com.example.jdplus.listeners.UsersListeners;
import com.example.jdplus.objetos.Usuario;

public class EmpezarVideollamada {

    public UsersListeners usersListeners;

    public void iniciar (Usuario usuario){
        usersListeners.initiateVideoMeeting(usuario);
    }
}


