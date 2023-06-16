package com.example.appmqtttcc.Telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmqtttcc.Adapter.RecyclerAdapterListaDispositivos;
import com.example.appmqtttcc.Constants;
import com.example.appmqtttcc.Models.CenaBean;
import com.example.appmqtttcc.Models.CenaDispositivoBean;
import com.example.appmqtttcc.Models.CenaDispositivoDao;
import com.example.appmqtttcc.Models.DispositivoBean;
import com.example.appmqtttcc.Models.CenaDao;
import com.example.appmqtttcc.Models.DispositivoDao;
import com.example.appmqtttcc.R;
import com.example.appmqtttcc.Util.UtilMudaTelas;

import java.util.ArrayList;
import java.util.List;

public class CadastroCena extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText edit_nome_cena, edit_dispositivo_analogico;
    private Button btn_salvar_cena;
    private TextView txt_dispositivo_digital_1, txt_dispositivo_digital_2, txt_dispositivo_digital_3;
    private TextView txt_dispositivo_digital_4, txt_dispositivo_digital_5, txt_dispositivo_analogico;
    /*private Spinner spinner_dispositivo_digital_1, spinner_dispositivo_digital_2, spinner_dispositivo_digital_3;
    private Spinner spinner_dispositivo_digital_4, spinner_dispositivo_digital_5;*/
    private ArrayAdapter<String> arrayAdapterDispositivoDigital;
    private List<String> arrayOpcoesDigital = new ArrayList<String>();
    private LinearLayout layout_lista_dispositivos;
    private String opcaoSpinnerDigital1, opcaoSpinnerDigital2, opcaoSpinnerDigital3, opcaoSpinnerDigital4, opcaoSpinnerDigital5;
    CenaDao cenaDao;
    CenaBean cenaBean;
    DispositivoBean dispositivoBean;
    DispositivoDao dispositivoDao;
    private UtilMudaTelas utilMudaTelas;
    private Integer[] arrayIdsDispositivosDisponiveis = {0, 0, 0, 0, 0, 0};
    Switch switchMasterOnOff, switch1, switch2, switch3, switch4, switch5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cena);

        cenaDao = new CenaDao(getApplicationContext());
        cenaBean = new CenaBean();
        dispositivoDao = new DispositivoDao(getApplicationContext());
        dispositivoBean = new DispositivoBean();
        declaraObjetos();

        edit_dispositivo_analogico.setText("0");

        populaDispositivosDisponiveis();

        switchMasterOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch1.setChecked(isChecked);
                switch2.setChecked(isChecked);
                switch3.setChecked(isChecked);
                switch4.setChecked(isChecked);
                switch5.setChecked(isChecked);
                if (isChecked) {
                    edit_dispositivo_analogico.setText("99");
                } else {
                    edit_dispositivo_analogico.setText("0");
                }
            }
        });
        //populaSpinners();

        /*spinner_dispositivo_digital_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int posicao, long id) {
                opcaoSpinnerDigital1 = spinner.getItemAtPosition(posicao).toString();
                if (opcaoSpinnerDigital1.equals(Constants.LIGAR)) {
                    opcaoSpinnerDigital1 = Constants.L;
                } else {
                    opcaoSpinnerDigital1 = Constants.D;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_dispositivo_digital_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int posicao, long id) {
                opcaoSpinnerDigital2 = spinner.getItemAtPosition(posicao).toString();
                if (opcaoSpinnerDigital2.equals(Constants.LIGAR)) {
                    opcaoSpinnerDigital2 = Constants.L;
                } else {
                    opcaoSpinnerDigital2 = Constants.D;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_dispositivo_digital_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int posicao, long id) {
                opcaoSpinnerDigital3 = spinner.getItemAtPosition(posicao).toString();
                if (opcaoSpinnerDigital3.equals(Constants.LIGAR)) {
                    opcaoSpinnerDigital3 = Constants.L;
                } else {
                    opcaoSpinnerDigital3 = Constants.D;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_dispositivo_digital_4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int posicao, long id) {
                opcaoSpinnerDigital4 = spinner.getItemAtPosition(posicao).toString();
                if (opcaoSpinnerDigital4.equals(Constants.LIGAR)) {
                    opcaoSpinnerDigital4 = Constants.L;
                } else {
                    opcaoSpinnerDigital4 = Constants.D;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_dispositivo_digital_5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int posicao, long id) {
                opcaoSpinnerDigital5 = spinner.getItemAtPosition(posicao).toString();
                if (opcaoSpinnerDigital5.equals(Constants.LIGAR)) {
                    opcaoSpinnerDigital5 = Constants.L;
                } else {
                    opcaoSpinnerDigital5 = Constants.D;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    /*private void populaSpinners() {
        try {
            arrayOpcoesDigital.clear();
            arrayOpcoesDigital.add(Constants.LIGAR);
            arrayOpcoesDigital.add(Constants.DESLIGAR);
            arrayAdapterDispositivoDigital = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayOpcoesDigital);
            spinner_dispositivo_digital_1.setAdapter(arrayAdapterDispositivoDigital);
            spinner_dispositivo_digital_2.setAdapter(arrayAdapterDispositivoDigital);
            spinner_dispositivo_digital_3.setAdapter(arrayAdapterDispositivoDigital);
            spinner_dispositivo_digital_4.setAdapter(arrayAdapterDispositivoDigital);
            spinner_dispositivo_digital_5.setAdapter(arrayAdapterDispositivoDigital);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "erro populaSpinners" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }*/

    public void populaDispositivosDisponiveis(){
        try {
            List<DispositivoBean> dispositivoBeanList = new ArrayList<>();
            DispositivoDao dispositivoDao = new DispositivoDao(getApplicationContext());
            dispositivoBeanList = dispositivoDao.buscarDispositivosList();

            if (dispositivoBeanList.size() > 0){
                for (DispositivoBean dispositivoBean : dispositivoBeanList) {
                    if (dispositivoBean.getTipo_dispositivo().equals("D")) {
                        switch (dispositivoBean.getSaida_digital_dispositivo()) {
                            case "13":
                                txt_dispositivo_digital_1.setVisibility(View.VISIBLE);
                                txt_dispositivo_digital_1.setText(dispositivoBean.getNome_dispositivo());
                                switch1.setVisibility(View.VISIBLE);
                                arrayIdsDispositivosDisponiveis[0] = dispositivoBean.getId_dispositivo();
                                break;
                            case "04":
                                txt_dispositivo_digital_2.setVisibility(View.VISIBLE);
                                txt_dispositivo_digital_2.setText(dispositivoBean.getNome_dispositivo());
                                switch2.setVisibility(View.VISIBLE);
                                arrayIdsDispositivosDisponiveis[1] = dispositivoBean.getId_dispositivo();
                                break;
                            case "05":
                                txt_dispositivo_digital_3.setVisibility(View.VISIBLE);
                                txt_dispositivo_digital_3.setText(dispositivoBean.getNome_dispositivo());
                                switch3.setVisibility(View.VISIBLE);
                                arrayIdsDispositivosDisponiveis[2] = dispositivoBean.getId_dispositivo();
                                break;
                            case "10":
                                txt_dispositivo_digital_4.setVisibility(View.VISIBLE);
                                txt_dispositivo_digital_4.setText(dispositivoBean.getNome_dispositivo());
                                switch4.setVisibility(View.VISIBLE);
                                arrayIdsDispositivosDisponiveis[3] = dispositivoBean.getId_dispositivo();
                                break;
                            case "11":
                                txt_dispositivo_digital_5.setVisibility(View.VISIBLE);
                                txt_dispositivo_digital_5.setText(dispositivoBean.getNome_dispositivo());
                                switch5.setVisibility(View.VISIBLE);
                                arrayIdsDispositivosDisponiveis[4] = dispositivoBean.getId_dispositivo();
                                break;
                        }
                    } else {
                        txt_dispositivo_analogico.setVisibility(View.VISIBLE);
                        txt_dispositivo_analogico.setText(dispositivoBean.getNome_dispositivo());
                        edit_dispositivo_analogico.setVisibility(View.VISIBLE);
                        arrayIdsDispositivosDisponiveis[5] = dispositivoBean.getId_dispositivo();
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(this, "ERRO populaDispositivosDisponiveis!", Toast.LENGTH_LONG).show();
        }
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
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroCena.this);
                utilMudaTelas.chamaTelaListaDispositivos();
                break;
            case R.id.opcao_adicionar_registro:
                break;
            case R.id.opcao_menu_lista_cenas:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroCena.this);
                utilMudaTelas.chamaTelaListaCenas();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_salvar_cena:
                Boolean retorno = salvarCena();
                if (retorno) {
                    Integer idCena = cenaDao.buscarUltimoIdCena();
                    try {
                        Toast.makeText(getApplicationContext(), "Cena salva com sucesso", Toast.LENGTH_SHORT).show();
                        salvarDispositivosCenas(idCena);
                        utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroCena.this);
                        utilMudaTelas.chamaTelaListaCenas();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Erro ao salvar dispositivos da cena ", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao salvar cena ", Toast.LENGTH_LONG).show();
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

            edit_nome_cena = (EditText) findViewById(R.id.edit_nome_cena);

            btn_salvar_cena = (Button) findViewById(R.id.btn_salvar_cena);
            btn_salvar_cena.setOnClickListener(this);

            layout_lista_dispositivos = (LinearLayout) findViewById(R.id.layout_lista_dispositivos);

            txt_dispositivo_digital_1 = (TextView) findViewById(R.id.txt_dispositivo_digital_1);
            txt_dispositivo_digital_2 = (TextView) findViewById(R.id.txt_dispositivo_digital_2);
            txt_dispositivo_digital_3 = (TextView) findViewById(R.id.txt_dispositivo_digital_3);
            txt_dispositivo_digital_4 = (TextView) findViewById(R.id.txt_dispositivo_digital_4);
            txt_dispositivo_digital_5 = (TextView) findViewById(R.id.txt_dispositivo_digital_5);
            
            txt_dispositivo_analogico = (TextView) findViewById(R.id.txt_dispositivo_analogico);
            edit_dispositivo_analogico = (EditText) findViewById(R.id.edit_dispositivo_analogico);

            switchMasterOnOff = (Switch) findViewById(R.id.switchMasterOnOff);
            switch1 = (Switch) findViewById(R.id.switch1);
            switch2 = (Switch) findViewById(R.id.switch2);
            switch3 = (Switch) findViewById(R.id.switch3);
            switch4 = (Switch) findViewById(R.id.switch4);
            switch5 = (Switch) findViewById(R.id.switch5);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "erro declaraobjetos" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean salvarCena() {
        boolean retorno = false;
        try {
            if (edit_nome_cena.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Informe todos os campos", Toast.LENGTH_LONG).show();
            } else {
                setaObjetoCena();
                retorno = cenaDao.gravarCena(cenaBean);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "salvar" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return retorno;
    }
    
    private void salvarDispositivosCenas(Integer idCena) {
        try {
            CenaDispositivoDao cenaDispositivoDao = new CenaDispositivoDao(getApplicationContext());
            for (Integer i = 0; i < 6; i++) {
                Integer id_dispositivo = arrayIdsDispositivosDisponiveis[i];
                CenaDispositivoBean cenaDispositivoBean = new CenaDispositivoBean();
                if (id_dispositivo != 0) {
                    cenaDispositivoBean.setId_dispositivo(id_dispositivo);
                    cenaDispositivoBean.setId_cena(idCena);
                    switch (i) {
                        case 0:
                            String valor = "";
                            if (switch1.isChecked()) {
                                valor = Constants.L;
                            } else {
                                valor = Constants.D;
                            }
                            cenaDispositivoBean.setValor(valor);
                            cenaDispositivoDao.gravarCenaDispositivo(cenaDispositivoBean);
                            break;
                        case 1:
                            String valor2 = "";
                            if (switch2.isChecked()) {
                                valor2 = Constants.L;
                            } else {
                                valor2 = Constants.D;
                            }
                            cenaDispositivoBean.setValor(valor2);
                            cenaDispositivoDao.gravarCenaDispositivo(cenaDispositivoBean);
                            break;
                        case 2:
                            String valor3 = "";
                            if (switch3.isChecked()) {
                                valor3 = Constants.L;
                            } else {
                                valor3 = Constants.D;
                            }
                            cenaDispositivoBean.setValor(valor3);
                            cenaDispositivoDao.gravarCenaDispositivo(cenaDispositivoBean);
                            break;
                        case 3:
                            String valor4 = "";
                            if (switch4.isChecked()) {
                                valor4 = Constants.L;
                            } else {
                                valor4 = Constants.D;
                            }
                            cenaDispositivoBean.setValor(valor4);
                            cenaDispositivoDao.gravarCenaDispositivo(cenaDispositivoBean);
                            break;
                        case 4:
                            String valor5 = "";
                            if (switch5.isChecked()) {
                                valor5 = Constants.L;
                            } else {
                                valor5 = Constants.D;
                            }
                            cenaDispositivoBean.setValor(valor5);
                            cenaDispositivoDao.gravarCenaDispositivo(cenaDispositivoBean);
                            break;
                        case 5:
                            cenaDispositivoBean.setValor(edit_dispositivo_analogico.getText().toString());
                            cenaDispositivoDao.gravarCenaDispositivo(cenaDispositivoBean);
                            break;
                    }
                }
            }
            Toast.makeText(getApplicationContext(), "Cena salva com sucesso", Toast.LENGTH_SHORT).show();
            utilMudaTelas = new UtilMudaTelas(getApplicationContext(), CadastroCena.this);
            utilMudaTelas.chamaTelaListaCenas();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Erro ao salvarDispositivosCenas", Toast.LENGTH_LONG).show();
        }
    }

    private void setaObjetoCena() {
        try {
            cenaBean.setNome_cena(edit_nome_cena.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "erro setaObjetoCena", Toast.LENGTH_LONG).show();
        }
    }
}