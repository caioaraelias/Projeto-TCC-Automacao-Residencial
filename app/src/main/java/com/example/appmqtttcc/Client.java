package com.example.appmqtttcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Client extends AppCompatActivity implements View.OnClickListener {
    Button button_clean_client, button_prefill_client, button_disconnect, button_subscribe, button_unsubscribe, button_publish;
    EditText edittext_pubtopic, edittext_subtopic, edittext_pubmsg;
    TextView textview_resposta_subscribe;
    private Intent SERVERURI_INTENT;
    private String serverURI = "";
    private Intent CLIENTID_INTENT;
    private String clientID = "";
    private Intent USERNAME_INTENT;
    private String username = "";
    private Intent PASSWORD_INTENT;
    private String password = "";

    MQTTClient mqttClient;
    private Boolean conectou = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        declaraObjetos();

        SERVERURI_INTENT = getIntent();
        serverURI = SERVERURI_INTENT.getStringExtra("SERVERURI");
        CLIENTID_INTENT = getIntent();
        clientID = CLIENTID_INTENT.getStringExtra("CLIENTID");
        USERNAME_INTENT = getIntent();
        username = USERNAME_INTENT.getStringExtra("USERNAME");
        PASSWORD_INTENT = getIntent();
        password = PASSWORD_INTENT.getStringExtra("PASSWORD");
        conectarMQTT();
    }

    public void conectarMQTT() {
        mqttClient = new MQTTClient(getApplicationContext(), serverURI, clientID);

        mqttClient.connect(
                username,
                password,
                new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Toast.makeText(Client.this, "Conexão MQTT realizada com sucesso", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Toast.makeText(Client.this, "Conexão MQTT não realizada", Toast.LENGTH_LONG).show();
                    }
                },
                new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Toast.makeText(Client.this, "Conexão perdida", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Toast.makeText(Client.this, "Resposta do subscribe: " + message.toString(), Toast.LENGTH_LONG).show();
                        textview_resposta_subscribe.setText("Resposta do subscribe: " + message.toString());
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Toast.makeText(Client.this, "Entrega completa", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void declaraObjetos() {
        button_clean_client = (Button) findViewById(R.id.button_clean_client);
        button_clean_client.setOnClickListener(this);
        button_prefill_client = (Button) findViewById(R.id.button_prefill_client);
        button_prefill_client.setOnClickListener(this);
        button_disconnect = (Button) findViewById(R.id.button_disconnect);
        button_disconnect.setOnClickListener(this);
        button_subscribe = (Button) findViewById(R.id.button_subscribe);
        button_subscribe.setOnClickListener(this);
        button_unsubscribe = (Button) findViewById(R.id.button_unsubscribe);
        button_unsubscribe.setOnClickListener(this);
        button_publish = (Button) findViewById(R.id.button_publish);
        button_publish.setOnClickListener(this);

        edittext_pubtopic = (EditText) findViewById(R.id.edittext_pubtopic);
        edittext_subtopic = (EditText) findViewById(R.id.edittext_subtopic);
        edittext_pubmsg = (EditText) findViewById(R.id.edittext_pubmsg);
        textview_resposta_subscribe = (TextView) findViewById(R.id.textview_resposta_subscribe);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_clean_client:
                limparCampos();
                break;
            case R.id.button_prefill_client:
                preencherCamposPadrão();
                break;
            case R.id.button_disconnect:
                desconectar();
                break;
            case R.id.button_subscribe:
                subscribe();
                break;
            case R.id.button_unsubscribe:
                unsubscribe();
                break;
            case R.id.button_publish:
                publish();
                break;
        }
    }

    public void preencherCamposPadrão() {
        edittext_pubtopic.setText(Constants.MQTT_TEST_TOPIC);
        edittext_subtopic.setText(Constants.TOPICO_SUBSCRIBE);
        edittext_pubmsg.setText(Constants.MQTT_TEST_MSG);
    }

    public void limparCampos() {
        edittext_pubtopic.setText("");
        edittext_subtopic.setText("");
        edittext_pubmsg.setText("");
    }

    public void desconectar() {
        if (mqttClient.isConnected()) {
            mqttClient.disconnect(
                    new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Toast.makeText(getApplicationContext(), "Desconectado do broker MQTT", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Toast.makeText(getApplicationContext(), "Erro ao desconectar" + exception.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
        } else {
            Toast.makeText(getApplicationContext(), "Impossível desconectar! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }
    }

    public void subscribe() {
        String topic   = edittext_subtopic.getText().toString();

        if (mqttClient.isConnected()) {
            mqttClient.subscribe2(Constants.TOPICO_SUBSCRIBE,
                    1,
                    new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Toast.makeText(getApplicationContext(), "Subscrito ao topic" + topic + " com sucesso!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Toast.makeText(getApplicationContext(), "Não foi possível subscrever ao topic " + topic, Toast.LENGTH_LONG).show();
                        }
                    },
                    new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            String valor = message.toString().substring(4, 6);
                            if (valor.equals(Constants.acaoDesligar)) {
                                valor = "Desligado";
                            } else if (valor.equals(Constants.acaoLigar)) {
                                valor = "Ligado";
                            }
                            textview_resposta_subscribe.setText("Resposta do subscribe: " + valor);
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Impossível subscrever! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }

        String comando = Constants.acaoLeitura + "13" + "0000";
        publish2(comando);
    }

    public void unsubscribe() {
        String topic   = edittext_pubtopic.getText().toString();

        if (mqttClient.isConnected()) {
            mqttClient.unsubscribe(topic,
                    new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Toast.makeText(getApplicationContext(), "Subscrição do topic" + topic + " cancelada com sucesso!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Toast.makeText(getApplicationContext(), "Não foi possível cancelar subscrição do topic " + topic, Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Impossível cancelar subscrição! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }
    }

    public void publish() {
        String topic   = edittext_pubtopic.getText().toString();
        String message    = edittext_pubmsg.getText().toString();

        if (mqttClient.isConnected()) {
            try {
                mqttClient.publish(topic,
                        message,
                        1,
                        false,
                        new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Toast.makeText(getApplicationContext(), "Publish realizado com sucesso!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Toast.makeText(getApplicationContext(), "Erro no publish", Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (Throwable e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Impossível publicar! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }
    }

    public void publish2(String message) {
        String topic   = edittext_pubtopic.getText().toString();
        if (mqttClient.isConnected()) {
            try {
                mqttClient.publish(topic,
                        message,
                        1,
                        false,
                        new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Toast.makeText(Client.this, "Erro ao alterar valor!", Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (Throwable e) {
                Toast.makeText(Client.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(Client.this, "Impossível publicar! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }
    }
}