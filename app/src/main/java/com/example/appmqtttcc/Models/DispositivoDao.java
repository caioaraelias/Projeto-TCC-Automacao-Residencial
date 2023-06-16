package com.example.appmqtttcc.Models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DispositivoDao {
    private Context ctx;
    private String sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public DispositivoDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean gravarDispositivo(DispositivoBean dispositivo){
        try {
            SQLiteDatabase db = new Banco(ctx).getWritableDatabase();
            gravacao = false;

            sql = "insert into DISPOSITIVO (nome_dispositivo, tipo_dispositivo, topico_dispositivo, saida_digital_dispositivo) values (?,?,?,?)";

            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, dispositivo.getNome_dispositivo());
            stmt.bindString(2, dispositivo.getTipo_dispositivo());
            stmt.bindString(3, dispositivo.getTopico_dispositivo());
            stmt.bindString(4, dispositivo.getSaida_digital_dispositivo());

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

    public boolean atualizaStatusPedidoEnvio(DispositivoBean dispositivoBean) {
        try {

            db = new Banco(ctx).getWritableDatabase();
            sql = "update DISPOSITIVO set nome_dispositivo = ?, tipo_dispositivo = ?, topico_dispositivo = ?, saida_digital_dispositivo = ? where id_dispositivo = ? ";
            stmt = db.compileStatement(sql);
            stmt.bindString(1, dispositivoBean.getNome_dispositivo());
            stmt.bindString(2, dispositivoBean.getTipo_dispositivo());
            stmt.bindString(3, dispositivoBean.getTopico_dispositivo());
            stmt.bindString(4, dispositivoBean.getSaida_digital_dispositivo());
            stmt.bindLong(5, dispositivoBean.getId_dispositivo());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            return false;
        } finally {
            db.close();
            stmt.close();
        }
        return true;
    }

    public List<DispositivoBean> buscarDispositivosList(){
        List<DispositivoBean> listaDispositivos = new ArrayList<>();
        db = new Banco(ctx).getReadableDatabase();
        try{
            cursor = db.rawQuery("select * from DISPOSITIVO", new String[]{});
            while (cursor.moveToNext()){
                DispositivoBean dispositivo = new DispositivoBean();
                dispositivo.setId_dispositivo(cursor.getInt(cursor.getColumnIndex(dispositivo.ID_DISPOSITIVO)));
                dispositivo.setNome_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.NOME_DISPOSITIVO)));
                dispositivo.setSaida_digital_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.SAIDA_DIGITAL_DISPOSITIVO)));
                dispositivo.setTopico_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.TOPICO_DISPOSITIVO)));
                dispositivo.setTipo_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.TIPO_DISPOSITIVO)));
                listaDispositivos.add(dispositivo);
            }
        }catch (SQLiteException e){

        }finally {
            db.close();
        }
        return listaDispositivos;
    }

    public List<DispositivoBean> buscarSaidasDigitais(){
        List<DispositivoBean> listaDispositivos = new ArrayList<>();
        db = new Banco(ctx).getReadableDatabase();
        try{
            cursor = db.rawQuery("select saida_digital_dispositivo from DISPOSITIVO where tipo_dispositivo = 'D'", new String[]{});
            while (cursor.moveToNext()){
                DispositivoBean dispositivo = new DispositivoBean();
                dispositivo.setSaida_digital_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.SAIDA_DIGITAL_DISPOSITIVO)));
                listaDispositivos.add(dispositivo);
            }
        }catch (SQLiteException e){

        }finally {
            db.close();
        }
        return listaDispositivos;
    }

    public List<DispositivoBean> buscarSaidasAnalogicas(){
        List<DispositivoBean> listaDispositivos = new ArrayList<>();
        db = new Banco(ctx).getReadableDatabase();
        try{
            cursor = db.rawQuery("select saida_digital_dispositivo from DISPOSITIVO where tipo_dispositivo = 'A'", new String[]{});
            while (cursor.moveToNext()){
                DispositivoBean dispositivo = new DispositivoBean();
                dispositivo.setSaida_digital_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.SAIDA_DIGITAL_DISPOSITIVO)));
                listaDispositivos.add(dispositivo);
            }
        }catch (SQLiteException e){

        }finally {
            db.close();
        }
        return listaDispositivos;
    }

    public DispositivoBean buscarDispositivo(Integer idDispositivo){
        db = new Banco(ctx).getReadableDatabase();
        DispositivoBean dispositivo = new DispositivoBean();
        try{
            cursor = db.rawQuery("select * from DISPOSITIVO WHERE id_dispositivo = ?", new String[]{idDispositivo.toString()});
            if (cursor.moveToFirst()){
                dispositivo.setId_dispositivo(cursor.getInt(cursor.getColumnIndex(dispositivo.ID_DISPOSITIVO)));
                dispositivo.setNome_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.NOME_DISPOSITIVO)));
                dispositivo.setSaida_digital_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.SAIDA_DIGITAL_DISPOSITIVO)));
                dispositivo.setTopico_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.TOPICO_DISPOSITIVO)));
                dispositivo.setTipo_dispositivo(cursor.getString(cursor.getColumnIndex(dispositivo.TIPO_DISPOSITIVO)));
            }
        }catch (SQLiteException e){
            Log.e("tag", e.getMessage());
        }finally {
            db.close();
        }
        return dispositivo;
    }

    public void deletarDispositivo(Integer idDispositivo){
        SQLiteDatabase db = new Banco(ctx).getWritableDatabase();
        String sql = "DELETE FROM DISPOSITIVO where id_dispositivo = " + idDispositivo +"";
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
