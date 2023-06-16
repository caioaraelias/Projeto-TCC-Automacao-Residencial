package com.example.appmqtttcc.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmqtttcc.Connect;
import com.example.appmqtttcc.Telas.CadastroCena;
import com.example.appmqtttcc.Telas.CadastroDispositivo;
import com.example.appmqtttcc.Telas.CadastroPlaca;
import com.example.appmqtttcc.Telas.ListaCenas;
import com.example.appmqtttcc.Telas.ListaDispositivos;

public class UtilMudaTelas extends AppCompatActivity {
    Context context;
    Activity activity;

    public UtilMudaTelas(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void chamaTelaCadastroPlaca(){
        try {
            Intent intent = new Intent(context, CadastroPlaca.class);
            activity.startActivity(intent);
        }catch (Exception e){
            Toast.makeText(context, "ERRO chamatelainicio", Toast.LENGTH_LONG).show();
        }
    }

    public void chamaTelaCadastroDispositivo(){
        try {
            Intent intent = new Intent(context, CadastroDispositivo.class);
            activity.startActivity(intent);
        }catch (Exception e){
            Toast.makeText(context, "ERRO chamatelapedido", Toast.LENGTH_LONG).show();
        }
    }

    public void chamaTelaListaDispositivos(){
        try {
            Intent intent = new Intent(context, ListaDispositivos.class);
            activity.startActivity(intent);
        }catch (Exception e){
            Toast.makeText(context, "ERRO chamatelapedido", Toast.LENGTH_LONG).show();
        }
    }

    public void chamaTelaCadastroCena(){
        try {
            Intent intent = new Intent(context, CadastroCena.class);
            activity.startActivity(intent);
        }catch (Exception e){
            Toast.makeText(context, "ERRO chamaTelaTesteConexao", Toast.LENGTH_LONG).show();
        }
    }

    public void chamaTelaListaCenas(){
        try {
            Intent intent = new Intent(context, ListaCenas.class);
            activity.startActivity(intent);
        }catch (Exception e){
            Toast.makeText(context, "ERRO chamaTelaTesteConexao", Toast.LENGTH_LONG).show();
        }
    }

    public void chamaTelaTesteConexao(){
        try {
            Intent intent = new Intent(context, Connect.class);
            activity.startActivity(intent);
        }catch (Exception e){
            Toast.makeText(context, "ERRO chamaTelaTesteConexao", Toast.LENGTH_LONG).show();
        }
    }
}
