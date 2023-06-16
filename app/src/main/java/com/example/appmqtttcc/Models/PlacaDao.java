package com.example.appmqtttcc.Models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PlacaDao {
    private Context ctx;
    private String sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public PlacaDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean gravarPlaca(PlacaBean placa){
        try {
            SQLiteDatabase db = new Banco(ctx).getWritableDatabase();
            gravacao = false;

            sql = "insert into PLACA (codigo_placa) values (?)";

            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, placa.getCodigo_placa());

            if (stmt.executeInsert() > 0) {
                gravacao = true;
                sql = "";
            }
            return gravacao;
        } catch (SQLiteException e) {
            Log.d("Script", e.getMessage());
            gravacao = false;
            return gravacao;
        }
    }

    public List<PlacaBean> buscarPlacasList(){
        List<PlacaBean> listaPlacas = new ArrayList<>();
        db = new Banco(ctx).getReadableDatabase();
        try{
            cursor = db.rawQuery("select * from PLACA", new String[]{});
            while (cursor.moveToNext()){
                PlacaBean placa = new PlacaBean();
                placa.setId_placa(cursor.getInt(cursor.getColumnIndex(placa.ID_PLACA)));
                placa.setCodigo_placa(cursor.getString(cursor.getColumnIndex(placa.CODIGO_PLACA)));
                listaPlacas.add(placa);
            }
        }catch (SQLiteException e){

        }finally {
            db.close();
        }
        return listaPlacas;
    }
}
