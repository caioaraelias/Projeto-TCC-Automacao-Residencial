package com.example.appmqtttcc.Models;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Banco extends SQLiteOpenHelper {
    public static String Dbname = "bancoAppTcc.db";
    public static int versao = 3;

    public Banco(Context ctx) {
        super(ctx, Dbname, null, versao);
    }

    private static String TABELA_PLACA = "CREATE TABLE [PLACA] (" +
            "id_placa INTEGER PRIMARY KEY AUTOINCREMENT, codigo_placa VARCHAR(100));";

    private static String TABELA_DISPOSITIVO = "CREATE TABLE [DISPOSITIVO] (" +
            "id_dispositivo INTEGER PRIMARY KEY AUTOINCREMENT, nome_dispositivo VARCHAR(30), topico_dispositivo VARCHAR(100), " +
            "id_placa_dispositivo INTEGER, tipo_dispositivo VARCHAR(1), saida_digital_dispositivo VARCHAR(10));";

    private static String ALTER_TABELA_DISPOSITIVO = "ALTER TABLE DISPOSITIVO ADD COLUMN saida_digital_dispositivo VARCHAR(10)";

    private static String TABELA_CENA = "CREATE TABLE [CENA] (" +
            "id_cena INTEGER PRIMARY KEY AUTOINCREMENT, nome_cena VARCHAR(30));";

    private static String TABELA_CENA_DISPOSITIVO = "CREATE TABLE [CENA_DISPOSITIVO] (" +
            "id_cena INTEGER, id_dispositivo INTEGER, valor VARCHAR(2));";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABELA_PLACA);
            db.execSQL(TABELA_DISPOSITIVO);
            db.execSQL(TABELA_CENA);
            db.execSQL(TABELA_CENA_DISPOSITIVO);
        }catch (Exception e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (newVersion >=2 && oldVersion < 2){
                db.execSQL(ALTER_TABELA_DISPOSITIVO);
            }
            if (newVersion >=3 && oldVersion < 3){
                db.execSQL(TABELA_CENA);
                db.execSQL(TABELA_CENA_DISPOSITIVO);
            }
        }catch (Exception e){

        }
    }
}
