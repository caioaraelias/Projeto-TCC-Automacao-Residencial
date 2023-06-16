package com.example.appmqtttcc.Telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appmqtttcc.Adapter.RecyclerAdapterListaCenas;
import com.example.appmqtttcc.Adapter.RecyclerAdapterListaDispositivos;
import com.example.appmqtttcc.MQTTClient;
import com.example.appmqtttcc.Models.CenaBean;
import com.example.appmqtttcc.Models.CenaDao;
import com.example.appmqtttcc.R;
import com.example.appmqtttcc.Util.UtilMudaTelas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListaCenas extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private RecyclerView recyclertelacenas;
    private RecyclerAdapterListaCenas adapter;
    private UtilMudaTelas utilMudaTelas;
    private FloatingActionButton btn_add_chama_tela_cadastro_cena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cenas);

        declaraObjetos();
        populaListaCenas();
    }

    //MÉTODO PARA INFLAR O MENU DA TOOLBAR COM ICONES
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cenas, menu);
        return true;
    }

    //MÉTODO DE CLICK DOS ITENS DO MENU SUPERIOR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.opcao_menu_lista_dispositivos:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), ListaCenas.this);
                utilMudaTelas.chamaTelaListaDispositivos();
                break;
            case R.id.opcao_adicionar_registro:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), ListaCenas.this);
                utilMudaTelas.chamaTelaCadastroCena();
                break;
            case R.id.opcao_menu_lista_cenas:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void declaraObjetos() {
        //INICIAÇÃO RECYCLER VIEW
        recyclertelacenas = (RecyclerView) findViewById(R.id.recyclertelacenas);
        recyclertelacenas.setLayoutManager(new LinearLayoutManager(this));

        //INICIAÇÃO DA TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_add_chama_tela_cadastro_cena = (FloatingActionButton) findViewById(R.id.btn_add_chama_tela_cadastro_cena);
        btn_add_chama_tela_cadastro_cena.setOnClickListener(this);
    }

    public void populaListaCenas(){
        try {
            List<CenaBean> cenaBeanList = new ArrayList<>();
            CenaDao cenaDao = new CenaDao(getApplicationContext());
            cenaBeanList = cenaDao.buscarCenasList();

            if (cenaBeanList.size() > 0){
                adapter = new RecyclerAdapterListaCenas(cenaBeanList, ListaCenas.this);
                recyclertelacenas.setAdapter(adapter);
            }
        }catch (Exception e){
            Toast.makeText(this, "ERRO populaListaPedidosTeste!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_chama_tela_cadastro_cena:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), ListaCenas.this);
                utilMudaTelas.chamaTelaCadastroCena();
                break;
        }
    }
}