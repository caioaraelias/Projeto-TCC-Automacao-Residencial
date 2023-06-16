package com.example.appmqtttcc.Telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appmqtttcc.Models.PlacaBean;
import com.example.appmqtttcc.Models.PlacaDao;
import com.example.appmqtttcc.R;
import com.example.appmqtttcc.Util.UtilMudaTelas;

import java.util.ArrayList;
import java.util.List;

public class CadastroPlaca extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText edit_codigo_placa;
    private Button btn_salvar_placa;
    private PlacaBean placaBean;
    private PlacaDao placaDao;
    private UtilMudaTelas utilMudaTelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_placa);

        placaDao = new PlacaDao(getApplicationContext());
        placaBean = new PlacaBean();

        //List<PlacaBean> placaBeanList = placaDao.buscarPlacasList();

        declaraObjetos();
    }

    //MÉTODO PARA INFLAR O MENU DA TOOLBAR COM ICONES
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_superior, menu);
        return true;
    }

    //MÉTODO DE CLICK DOS ITENS DO MENU SUPERIOR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //SE CLICAR NO BOTÃO VOLTAR EM CIMA, CHAMA O ONBACKPRESSED E VOLTA PARA TELA DO CARRINHO
            case R.id.opcao_menu_cadastro_dispositivo:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroPlaca.this);
                utilMudaTelas.chamaTelaCadastroDispositivo();
                break;
            /*case R.id.opcao_menu_cadastro_placa:
                break;*/
            case R.id.opcao_menu_lista_dispositivos:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroPlaca.this);
                utilMudaTelas.chamaTelaListaDispositivos();
                break;
            case R.id.opcao_menu_teste_conexao:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroPlaca.this);
                utilMudaTelas.chamaTelaTesteConexao();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_salvar_placa:
                salvar();
                break;
        }
    }

    private void declaraObjetos() {
        try {
            //INICIAÇÃO DA TOOLBAR
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //FINAL TOOLBAR

            edit_codigo_placa = (EditText) findViewById(R.id.edit_codigo_placa);

            btn_salvar_placa = (Button) findViewById(R.id.btn_salvar_placa);
            btn_salvar_placa.setOnClickListener(this);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "erro declaraobjetos" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean salvar() {
        boolean retorno = false;
        try {
            if (edit_codigo_placa.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Informe todos os campos", Toast.LENGTH_LONG).show();
            } else {
                setaObjetoPlaca();
                retorno = placaDao.gravarPlaca(placaBean);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "salvar" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return retorno;
    }

    private void setaObjetoPlaca() {
        try {
            placaBean.setCodigo_placa(edit_codigo_placa.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "erro setaObjetoDispositivo", Toast.LENGTH_LONG).show();
        }
    }
}