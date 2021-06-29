package com.example.jdplus.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.jdplus.objetos.Mensajes;
//import com.example.jdplus.objetos.Usuarios;

import java.util.ArrayList;

public class SqliteOperacionesMensajes extends CreaBDSqlite{

    Context context;
    public SqliteOperacionesMensajes(@Nullable Context context) {
        super(context);
        this.context=context;
    }

    public void crearTablaSqlite(String nombreTabla){

        CreaBDSqlite creaBDSqlite = new CreaBDSqlite(context);
        SQLiteDatabase db = creaBDSqlite.getWritableDatabase();

        db.execSQL("create table if not exists "+nombreTabla+
                "(id integer  primary key AUTOINCREMENT," +
                "usuarioEnvia TEXT," +
                "mensajes TEXT," +
                "imagenes TEXT," +
                "hora TIME," +
                "dia date," +
                "tipo INTEGER)");
    }

    public int idMaxima(String nombreTabla){
        int maxiId = 0;

        CreaBDSqlite creaBDSqlite = new CreaBDSqlite(context);
        SQLiteDatabase db = creaBDSqlite.getWritableDatabase();


        Cursor c = db.rawQuery("select * from "+ nombreTabla,null);

        while (c.moveToNext()) {
            maxiId = c.getInt(c.getColumnIndex("id"));
        }

        return maxiId;

    }

    public ArrayList<Mensajes> mostraMensaje(String nombreTabla , String ip){

        CreaBDSqlite creaBDSqlite = new CreaBDSqlite(context);
        SQLiteDatabase db = creaBDSqlite.getWritableDatabase();

        ArrayList<Mensajes> listaMensajes = new ArrayList<>();

        Cursor cursor = null;

        cursor = db.rawQuery("select * from "+ nombreTabla,null);

        while (cursor.moveToNext()){

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String usuarioEnvia = cursor.getString(cursor.getColumnIndex("usuarioEnvia"));
            String mensajes = cursor.getString(cursor.getColumnIndex("mensajes"));
            String imagenes = cursor.getString(cursor.getColumnIndex("imagenes"));
            String hora = cursor.getString(cursor.getColumnIndex("hora"));
            String dia = cursor.getString(cursor.getColumnIndex("dia"));
            int tipo = cursor.getInt(cursor.getColumnIndex("tipo"));

            String foto = null;

            if (!imagenes.equals("")) {

                foto= "http://"+ip+":8080/proyecto/"+imagenes;

            }
            //if (id == idEnviar) {
                //listaMensajes.add(new Mensajes(id, usuarioEnvia, mensajes, foto, hora, dia, tipo));
            //}
        }
        cursor.close();
        return listaMensajes;
    }

    public long insertarMensaje (String tabla, String usuarioEnvia,String mensajes, String imagenes, String hora, String dia, int tipo){
        long id = 0;
        try {
            CreaBDSqlite creaBDSqlite = new CreaBDSqlite(context);
            SQLiteDatabase db = creaBDSqlite.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("usuarioEnvia", usuarioEnvia);
            values.put("mensajes", mensajes);
            values.put("imagenes", imagenes);
            values.put("hora", hora);
            values.put("dia", dia);
            values.put("tipo", tipo);

            id = db.insert(tabla, null, values);
        }
        catch (Exception e){
            e.toString();
        }

        return id;
    }
}
