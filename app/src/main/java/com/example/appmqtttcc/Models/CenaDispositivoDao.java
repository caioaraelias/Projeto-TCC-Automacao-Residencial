package com.example.appmqtttcc.Models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CenaDispositivoDao {
    private Context ctx;
    private String sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public CenaDispositivoDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean gravarCenaDispositivo(CenaDispositivoBean cenaDispositivoBean){
        try {
            SQLiteDatabase db = new Banco(ctx).getWritableDatabase();
            gravacao = false;

            sql = "insert into CENA_DISPOSITIVO (id_cena, id_dispositivo, valor) values (?, ?, ?)";

            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindLong(1, cenaDispositivoBean.getId_cena());
            stmt.bindLong(2, cenaDispositivoBean.getId_dispositivo());
            stmt.bindString(3, cenaDispositivoBean.getValor());

            if (stmt.executeInsert() > 0) {
                gravacao = true;
                sql = "";
            }
            return gravacao;
        } catch (SQLiteException e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
            gravacao = false;
            return gravacao;
        }
    }

    public List<CenaDispositivoBean> buscarCenasDispositivosList(Integer idCena){
        List<CenaDispositivoBean> cenaDispositivoBeanList = new ArrayList<>();
        db = new Banco(ctx).getReadableDatabase();
        try{
            cursor = db.rawQuery("select * from CENA_DISPOSITIVO where id_cena = ?", new String[]{idCena.toString()});
            while (cursor.moveToNext()){
                CenaDispositivoBean cenaDispositivoBean = new CenaDispositivoBean();
                cenaDispositivoBean.setId_cena(cursor.getInt(cursor.getColumnIndex(cenaDispositivoBean.ID_CENA)));
                cenaDispositivoBean.setId_dispositivo(cursor.getInt(cursor.getColumnIndex(cenaDispositivoBean.ID_DISPOSITIVO)));
                cenaDispositivoBean.setValor(cursor.getString(cursor.getColumnIndex(cenaDispositivoBean.VALOR)));
                cenaDispositivoBeanList.add(cenaDispositivoBean);
            }
        }catch (SQLiteException e){

        }finally {
            db.close();
        }
        return cenaDispositivoBeanList;
    }

    public void deletarCenaDispositivo(Integer idCena){
        SQLiteDatabase db = new Banco(ctx).getWritableDatabase();
        String sql = "DELETE FROM CENA_DISPOSITIVO where id_cena = " + idCena +"";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.executeUpdateDelete();
        } catch (SQLiteException e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
            stmt.close();
        }
    }
}
