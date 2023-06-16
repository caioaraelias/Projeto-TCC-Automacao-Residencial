package com.example.appmqtttcc.Models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CenaDao {
    private Context ctx;
    private String sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public CenaDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean gravarCena(CenaBean cenaBean){
        try {
            SQLiteDatabase db = new Banco(ctx).getWritableDatabase();
            gravacao = false;

            sql = "insert into CENA (nome_cena) values (?)";

            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, cenaBean.getNome_cena());

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

    public Integer buscarUltimoIdCena(){
        db = new Banco(ctx).getReadableDatabase();
        Integer id_cena = 0;
        try{
            cursor = db.rawQuery("select id_cena from CENA order by id_cena desc", new String[]{});
            if (cursor.moveToFirst()){
                id_cena = cursor.getInt(cursor.getColumnIndex("id_cena"));
            }
        }catch (SQLiteException e){

        }finally {
            db.close();
        }
        return id_cena;
    }

    public List<CenaBean> buscarCenasList(){
        List<CenaBean> cenaBeanList = new ArrayList<>();
        db = new Banco(ctx).getReadableDatabase();
        try{
            cursor = db.rawQuery("select * from CENA", new String[]{});
            while (cursor.moveToNext()){
                CenaBean cenaBean = new CenaBean();
                cenaBean.setId_cena(cursor.getInt(cursor.getColumnIndex(cenaBean.ID_CENA)));
                cenaBean.setNome_cena(cursor.getString(cursor.getColumnIndex(cenaBean.NOME_CENA)));
                cenaBeanList.add(cenaBean);
            }
        }catch (SQLiteException e){

        }finally {
            db.close();
        }
        return cenaBeanList;
    }

    public void deletarCena(Integer idCena){
        SQLiteDatabase db = new Banco(ctx).getWritableDatabase();
        String sql = "DELETE FROM CENA where id_cena = " + idCena +"";
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
