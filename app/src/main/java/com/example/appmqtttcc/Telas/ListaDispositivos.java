package com.example.appmqtttcc.Telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appmqtttcc.Adapter.RecyclerAdapterListaDispositivos;
import com.example.appmqtttcc.Constants;
import com.example.appmqtttcc.MQTTClient;
import com.example.appmqtttcc.Models.DispositivoBean;
import com.example.appmqtttcc.Models.DispositivoDao;
import com.example.appmqtttcc.R;
import com.example.appmqtttcc.Util.UtilMudaTelas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class ListaDispositivos extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private RecyclerView recyclerteladispositivos;
    private RecyclerAdapterListaDispositivos adapter;
    private UtilMudaTelas utilMudaTelas;
    private FloatingActionButton btn_add_chama_tela_cadastro_dispositivo;
    private Button btn_cena, btn_cena2;
    MQTTClient mqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dispositivos);

        declaraObjetos();
        populaListaDispositivos();
        conectarMQTT();
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
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), ListaDispositivos.this);
                utilMudaTelas.chamaTelaCadastroDispositivo();
                break;
            case R.id.opcao_menu_lista_cenas:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), ListaDispositivos.this);
                utilMudaTelas.chamaTelaListaCenas();
                break;
            case R.id.opcao_menu_lista_dispositivos:
                break;
            case R.id.opcao_menu_teste_conexao:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), ListaDispositivos.this);
                utilMudaTelas.chamaTelaTesteConexao();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void declaraObjetos() {
        //INICIAÇÃO RECYCLER VIEW
        recyclerteladispositivos = (RecyclerView) findViewById(R.id.recyclerteladispositivos);
        recyclerteladispositivos.setLayoutManager(new LinearLayoutManager(this));

        //INICIAÇÃO DA TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_add_chama_tela_cadastro_dispositivo = (FloatingActionButton) findViewById(R.id.btn_add_chama_tela_cadastro_dispositivo);
        btn_add_chama_tela_cadastro_dispositivo.setOnClickListener(this);

        btn_cena = (Button) findViewById(R.id.btn_cena);
        btn_cena.setOnClickListener(this);
        btn_cena2 = (Button) findViewById(R.id.btn_cena2);
        btn_cena2.setOnClickListener(this);
    }

    public void populaListaDispositivos(){
        try {
            List<DispositivoBean> dispositivoBeanList = new ArrayList<>();
            DispositivoDao dispositivoDao = new DispositivoDao(getApplicationContext());
            dispositivoBeanList = dispositivoDao.buscarDispositivosList();

            if (dispositivoBeanList.size() > 0){
                adapter = new RecyclerAdapterListaDispositivos(dispositivoBeanList, ListaDispositivos.this);
                recyclerteladispositivos.setAdapter(adapter);
            }
        }catch (Exception e){
            Toast.makeText(this, "ERRO populaListaPedidosTeste!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_chama_tela_cadastro_dispositivo:
                utilMudaTelas = new UtilMudaTelas(getApplicationContext(), ListaDispositivos.this);
                utilMudaTelas.chamaTelaCadastroDispositivo();
                break;
            case R.id.btn_cena:
                rodarCena();
                break;
            case R.id.btn_cena2:
                rodarCena2();
                break;
        }
    }
    
    public void rodarCena(){
        DispositivoDao dispositivoDao = new DispositivoDao(ListaDispositivos.this);
        List<DispositivoBean> dispositivoBeanList = new ArrayList<>();
        dispositivoBeanList = dispositivoDao.buscarDispositivosList();
        int index = 0;
        String saidas = "";
        if (dispositivoBeanList.size() > 0){
            for (DispositivoBean dispositivoBean : dispositivoBeanList) {
                if ( dispositivoBean.getTipo_dispositivo().equals(Constants.D)) {
                    String valor;
                    if (index % 2 == 0) {
                        valor = "LIGAR";
                        saidas = saidas + dispositivoBean.getSaida_digital_dispositivo() + "+";
                    } else {
                        valor = "DESLIGAR";
                    }
                    index++;
                    alterarValorDispositivo(dispositivoBean.getSaida_digital_dispositivo(),
                            dispositivoBean.getTopico_dispositivo(),
                            dispositivoBean.getTipo_dispositivo(), valor);
                }
            }
        }
        btn_cena.setText(saidas);
    }

    public void rodarCena2(){
        DispositivoDao dispositivoDao = new DispositivoDao(ListaDispositivos.this);
        List<DispositivoBean> dispositivoBeanList = new ArrayList<>();
        dispositivoBeanList = dispositivoDao.buscarDispositivosList();
        int index = 0;
        String saidas = "";
        if (dispositivoBeanList.size() > 0){
            for (DispositivoBean dispositivoBean : dispositivoBeanList) {
                if ( dispositivoBean.getTipo_dispositivo().equals(Constants.D)) {


                    String valor;
                    if (index % 2 != 0) {
                        valor = "LIGAR";
                        saidas = saidas + dispositivoBean.getSaida_digital_dispositivo() + "+";
                    } else {
                        valor = "DESLIGAR";

                    }
                    index++;
                    alterarValorDispositivo(dispositivoBean.getSaida_digital_dispositivo(),
                            dispositivoBean.getTopico_dispositivo(),
                            dispositivoBean.getTipo_dispositivo(), valor);
                }

            }
        }
        btn_cena2.setText(saidas);
    }

    public void alterarValorDispositivo(String saida, String topico, String tipo, String valor)
    {
        if (tipo.equals(Constants.D)) {
            String message = "";
            if (valor.equals(Constants.LIGAR)) {
                message =  Constants.acaoLigar;
            } else {
                message =  Constants.acaoDesligar;
            }
            String comando = message + saida + "0000";
            publish(topico, comando);
        } else {
            String message =  Constants.acaoAnalogico;
            String comando = message + saida + "0000";
            publish(topico, comando);
        }
    }

    public void conectarMQTT() {
        mqttClient = new MQTTClient(ListaDispositivos.this, Constants.MQTT_SERVER_URI, Constants.MQTT_CLIENT_ID);

        mqttClient.connect(
                "",
                "",
                new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Toast.makeText(ListaDispositivos.this, "Conexão MQTT não realizada", Toast.LENGTH_LONG).show();
                    }
                },
                new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                    }
                }
        );
    }

    public void publish(String topic, String message) {
        

        if (mqttClient.isConnected()) {
            try {
                mqttClient.publish(topic,
                        message,
                        1,
                        false,
                        new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Toast.makeText(getApplicationContext(), asyncActionToken.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Toast.makeText(getApplicationContext(), "Erro ao publicar", Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (Throwable e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Impossível publicar! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }
    }
}