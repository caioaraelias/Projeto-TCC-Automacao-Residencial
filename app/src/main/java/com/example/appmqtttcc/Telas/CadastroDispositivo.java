package com.example.appmqtttcc.Telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmqtttcc.Adapter.RecyclerAdapterListaDispositivos;
import com.example.appmqtttcc.Constants;
import com.example.appmqtttcc.Models.DispositivoBean;
import com.example.appmqtttcc.Models.DispositivoDao;
import com.example.appmqtttcc.Models.PlacaBean;
import com.example.appmqtttcc.Models.PlacaDao;
import com.example.appmqtttcc.R;
import com.example.appmqtttcc.Util.UtilMudaTelas;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CadastroDispositivo extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView txt_saida_digital, txt_aviso_quantidade_maxima;
    private EditText edit_nome_dispositivo;
    private Spinner spinner_tipo, spinner_saida_digital /*spinner_placa*/;
    private Button btn_salvar_dispositivo;
    private String opcaoTipoDispositivo = "";
    private String opcaoSaidaDigital = "";
    //private String opcaoPlaca = "";
    private List<String> arrayTiposDispositivos = new ArrayList<String>();
    //private List<String> arrayPlacas = new ArrayList<String>();
    private List<String> arraySaidasDigitais = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterTiposDispositivos, arrayAdapterSaidasDigitais /*arrayAdapterPlacas*/;
    DispositivoDao dispositivoDao;
    DispositivoBean dispositivoBean;
    private UtilMudaTelas utilMudaTelas;
    private Integer quantidadeSaidasUtilizadas = 0;
    private LinearLayout layout_full_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_dispositivo);

        dispositivoDao = new DispositivoDao(getApplicationContext());
        dispositivoBean = new DispositivoBean();
        declaraObjetos();
        populaSpinners();

        spinner_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int posicao, long id) {
                opcaoTipoDispositivo = spinner.getItemAtPosition(posicao).toString();
                if (opcaoTipoDispositivo.equals("Digital")) {
                    opcaoTipoDispositivo = "D";
                    txt_saida_digital.setVisibility(View.VISIBLE);
                    spinner_saida_digital.setVisibility(View.VISIBLE);
                } else {
                    opcaoTipoDispositivo = "A";
                    opcaoSaidaDigital = "";
                    txt_saida_digital.setVisibility(View.GONE);
                    spinner_saida_digital.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_saida_digital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int posicao, long id) {
                opcaoSaidaDigital = spinner.getItemAtPosition(posicao).toString();
                switch (opcaoSaidaDigital) {
                    case "1":
                        opcaoSaidaDigital = "13";
                        break;
                    case "2":
                        opcaoSaidaDigital = "04";
                        break;
                    case "3":
                        opcaoSaidaDigital = "05";
                        break;
                    case "4":
                        opcaoSaidaDigital = "10";
                        break;
                    case "5":
                        opcaoSaidaDigital = "11";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*spinner_placa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int posicao, long id) {
                opcaoPlaca = spinner.getItemAtPosition(posicao).toString();
                opcaoPlaca = opcaoPlaca.substring(0, 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
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
                break;
            case R.id.opcao_menu_lista_dispositivos:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroDispositivo.this);
                utilMudaTelas.chamaTelaListaDispositivos();
                break;
            case R.id.opcao_menu_teste_conexao:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroDispositivo.this);
                utilMudaTelas.chamaTelaTesteConexao();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_salvar_dispositivo:
                Boolean retorno = salvar();
                if (retorno) {
                    Toast.makeText(getApplicationContext(), "Dispositivo salvo com sucesso", Toast.LENGTH_SHORT).show();
                    utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroDispositivo.this);
                    utilMudaTelas.chamaTelaListaDispositivos();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao salvar dispositivo ", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void declaraObjetos() {
        try {
            //INICIAÇÃO DA TOOLBAR
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //FINAL TOOLBAR

            edit_nome_dispositivo = (EditText) findViewById(R.id.edit_nome_dispositivo);

            spinner_tipo = (Spinner) findViewById(R.id.spinner_tipo);
            spinner_saida_digital = (Spinner) findViewById(R.id.spinner_saida_digital);
            //spinner_placa = (Spinner) findViewById(R.id.spinner_placa);

            btn_salvar_dispositivo = (Button) findViewById(R.id.btn_salvar_dispositivo);
            btn_salvar_dispositivo.setOnClickListener(this);

            txt_saida_digital = (TextView) findViewById(R.id.txt_saida_digital);
            txt_aviso_quantidade_maxima = (TextView) findViewById(R.id.txt_aviso_quantidade_maxima);

            layout_full_screen = (LinearLayout) findViewById(R.id.layout_full_screen);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "erro declaraobjetos" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void populaSpinners() {
        try {
            arrayTiposDispositivos.clear();
            arraySaidasDigitais.clear();
            montaArraysSpinner();

            arrayAdapterTiposDispositivos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayTiposDispositivos);
            spinner_tipo.setAdapter(arrayAdapterTiposDispositivos);

            arrayAdapterSaidasDigitais = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySaidasDigitais);
            spinner_saida_digital.setAdapter(arrayAdapterSaidasDigitais);

            /*
            PlacaBean placaBean = new PlacaBean();
            PlacaDao placaDao = new PlacaDao(getApplication());
            List<PlacaBean> placaBeanList = new ArrayList<>();
            placaBeanList = placaDao.buscarPlacasList();

            arrayPlacas.clear();
            for (int i = 0; i < placaBeanList.size(); i++) {
                arrayPlacas.add(placaBeanList.get(i).getId_placa() + " - " + placaBeanList.get(i).getCodigo_placa());
            }

            arrayAdapterPlacas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayPlacas);
            spinner_placa.setAdapter(arrayAdapterPlacas);*/
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "erro populaSpinners" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void montaArraysSpinner() {
        try {
            List<String> arraySaidasCadastradas = new ArrayList<>();
            List<DispositivoBean> dispositivoBeanList = dispositivoDao.buscarSaidasDigitais();
            if (dispositivoBeanList.size() > 0){
                for (DispositivoBean dispositivoBean : dispositivoBeanList) {
                    switch (dispositivoBean.getSaida_digital_dispositivo()) {
                        case "13":
                            arraySaidasCadastradas.add("1");
                            quantidadeSaidasUtilizadas ++;
                            break;
                        case "04":
                            arraySaidasCadastradas.add("2");
                            quantidadeSaidasUtilizadas ++;
                            break;
                        case "05":
                            arraySaidasCadastradas.add("3");
                            quantidadeSaidasUtilizadas ++;
                            break;
                        case "10":
                            arraySaidasCadastradas.add("4");
                            quantidadeSaidasUtilizadas ++;
                            break;
                        case "11":
                            arraySaidasCadastradas.add("5");
                            quantidadeSaidasUtilizadas ++;
                            break;
                    }
                }
            }

            if (arraySaidasCadastradas.size() > 0) {
                if (!arraySaidasCadastradas.contains("1")) {
                    arraySaidasDigitais.add("1");
                }
                if (!arraySaidasCadastradas.contains("2")) {
                    arraySaidasDigitais.add("2");
                }
                if (!arraySaidasCadastradas.contains("3")) {
                    arraySaidasDigitais.add("3");
                }
                if (!arraySaidasCadastradas.contains("4")) {
                    arraySaidasDigitais.add("4");
                }
                if (!arraySaidasCadastradas.contains("5")) {
                    arraySaidasDigitais.add("5");
                }
            } else {
                arraySaidasDigitais.add("1");
                arraySaidasDigitais.add("2");
                arraySaidasDigitais.add("3");
                arraySaidasDigitais.add("4");
                arraySaidasDigitais.add("5");
            }

            List<DispositivoBean> dispositivoBeanListAnalogicas = dispositivoDao.buscarSaidasAnalogicas();
            Boolean permitirOpcaoAnalogica = true;
            if (dispositivoBeanListAnalogicas.size() > 0) {
                permitirOpcaoAnalogica = false;
            }

            if (quantidadeSaidasUtilizadas != 5) {
                arrayTiposDispositivos.add("Digital");
                if (permitirOpcaoAnalogica) {
                    arrayTiposDispositivos.add("Analógico");
                }
            } else {
                if (permitirOpcaoAnalogica) {
                    arrayTiposDispositivos.add("Analógico");
                } else {
                    txt_aviso_quantidade_maxima.setVisibility(View.VISIBLE);
                    layout_full_screen.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "erro montaArraysSpinner" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean salvar() {
        boolean retorno = false;
        try {
            if (edit_nome_dispositivo.getText().toString().equals("")
                || opcaoTipoDispositivo.equals("")
            ) {
                Toast.makeText(getApplicationContext(), "Informe todos os campos", Toast.LENGTH_LONG).show();
            } else {
                if (opcaoTipoDispositivo.equals(Constants.D) && opcaoSaidaDigital.equals("")) {
                    Toast.makeText(getApplicationContext(), "Informe a saída digital", Toast.LENGTH_LONG).show();
                } else {
                    setaObjetoDispositivo();
                    retorno = dispositivoDao.gravarDispositivo(dispositivoBean);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "salvar" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return retorno;
    }

    private void setaObjetoDispositivo() {
        try {
            dispositivoBean.setNome_dispositivo(edit_nome_dispositivo.getText().toString());
            dispositivoBean.setTopico_dispositivo(Constants.TOPICO_COMANDO);
            dispositivoBean.setSaida_digital_dispositivo(opcaoSaidaDigital);
            dispositivoBean.setTipo_dispositivo(opcaoTipoDispositivo);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "erro setaObjetoDispositivo", Toast.LENGTH_LONG).show();
        }
    }
}