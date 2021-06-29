package com.example.jdplus.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;



import java.util.ArrayList;

public class SqliteOperacionesUsuarioChat extends CreaBDSqlite{

    Context context;

    public SqliteOperacionesUsuarioChat(@Nullable Context context) {
        super(context);
        this.context = context;
    }


    public long insertarusuarios_chat (String usuario, String nombre){
        long id = 0;

    try {
        CreaBDSqlite creaBDSqlite = new CreaBDSqlite(context);
        SQLiteDatabase db = creaBDSqlite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("usuario", usuario);
        values.put("nombre", nombre);

        id = db.insert(Nombre_tabla, null, values);
    }
    catch (Exception e){
        e.toString();
    }

        return id;
    }

    public Boolean nombreUsuario (String nombreUsuario){

        CreaBDSqlite creaBDSqlite = new CreaBDSqlite(context);
        SQLiteDatabase db = creaBDSqlite.getWritableDatabase();

        Boolean existe =false;

        String usuario =null;
        Cursor c = db.rawQuery("select * from "+ Nombre_tabla,null);

        while (c.moveToNext()) {
            usuario = c.getString(c.getColumnIndex("usuario"));

        if (nombreUsuario.equals(usuario)){
            existe = true;
        }
        }
        return existe;
    }

    /*public ArrayList<Usuarios> mostrarUsuarios(){

        CreaBDSqlite creaBDSqlite = new CreaBDSqlite(context);
        SQLiteDatabase db = creaBDSqlite.getWritableDatabase();

        ArrayList<Usuarios> listaUsuarios = new ArrayList<>();

        Cursor cursor = null;

        cursor = db.rawQuery("select * from "+ Nombre_tabla,null);

            while (cursor.moveToNext()){

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String usuario = cursor.getString(cursor.getColumnIndex("usuario"));
                String nombre = cursor.getString(cursor.getColumnIndex("nombre"));

                //listaUsuarios.add(new Usuarios(id,usuario,nombre));
            }

        cursor.close();

        return listaUsuarios;
    }*/

}
