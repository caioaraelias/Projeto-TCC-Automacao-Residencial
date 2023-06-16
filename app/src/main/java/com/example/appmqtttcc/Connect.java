package com.example.appmqtttcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Connect extends AppCompatActivity implements View.OnClickListener {
    Button button_prefill, button_clean, button_connect;
    EditText edittext_server_uri, edittext_client_id, edittext_username, edittext_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        declaraObjetos();
    }

    private void declaraObjetos() {
        button_prefill = (Button) findViewById(R.id.button_prefill);
        button_prefill.setOnClickListener(this);
        button_clean = (Button) findViewById(R.id.button_clean);
        button_clean.setOnClickListener(this);
        button_connect = (Button) findViewById(R.id.button_connect);
        button_connect.setOnClickListener(this);

        edittext_password = (EditText) findViewById(R.id.edittext_password);
        edittext_client_id = (EditText) findViewById(R.id.edittext_client_id);
        edittext_username = (EditText) findViewById(R.id.edittext_username);
        edittext_server_uri = (EditText) findViewById(R.id.edittext_server_uri);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_prefill:
                preencherCamposPadrão();
                break;
            case R.id.button_clean:
                limparCampos();
                break;
            case R.id.button_connect:
                conectar();
                break;
        }
    }

    public void preencherCamposPadrão() {
        edittext_server_uri.setText(Constants.MQTT_SERVER_URI);
        edittext_client_id.setText(Constants.MQTT_CLIENT_ID);
        edittext_username.setText(Constants.MQTT_USERNAME);
        edittext_password.setText(Constants.MQTT_PWD);
    }

    public void limparCampos() {
        edittext_server_uri.setText("");
        edittext_client_id.setText("");
        edittext_username.setText("");
        edittext_password.setText("");
    }

    public void conectar() {
        String serverURIFromEditText   = edittext_server_uri.getText().toString();
        String clientIDFromEditText    = edittext_client_id.getText().toString();
        String usernameFromEditText    = edittext_username.getText().toString();
        String pwdFromEditText         = edittext_password.getText().toString();

        Intent intent = new Intent(getBaseContext(), Client.class);
        intent.putExtra("SERVERURI", serverURIFromEditText);
        intent.putExtra("CLIENTID", clientIDFromEditText);
        intent.putExtra("USERNAME", usernameFromEditText);
        intent.putExtra("PASSWORD", pwdFromEditText);
        startActivity(intent);
    }
}