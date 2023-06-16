package com.example.appmqtttcc;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

public class MQTTClient extends MqttAndroidClient {
    private String serverURI;
    private Context context;
    private String clientID;
    MqttAndroidClient mqttClient;

    public MQTTClient(Context context, String serverURI, String clientID) {
        super(context, serverURI, clientID);
        this.clientID = clientID;
        this.context = context;
        this.serverURI = serverURI;
    }

    public void connect(String username,
                        String password,
                        IMqttActionListener cbConnect,
                        MqttCallback cbClient) {
        mqttClient = new MqttAndroidClient(context, serverURI, clientID);
        mqttClient.setCallback(cbClient);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());

        try {
            IMqttToken a = mqttClient.connect(options, null, cbConnect);
            Boolean teste = mqttClient.isConnected();
            Boolean b = false;
        } catch (Throwable e) {
            Toast.makeText(context, "mqttclient.connect" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    public void subscribe(String topic,
                          Integer qos,
                          IMqttActionListener cbSubscribe
    ) {
        try {
            qos = 0;
            mqttClient.subscribe(topic, qos, null, cbSubscribe);
        } catch (MqttException e) {
            Toast.makeText(context, "mqttclient.subscribe" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void subscribe2(String topic,
                          Integer qos,
                          IMqttActionListener cbSubscribe
                          ,MqttCallback cbClient
    ) {
        try {
            mqttClient.setCallback(cbClient);
            qos = 0;
            mqttClient.subscribe(topic, qos, null, cbSubscribe);
        } catch (MqttException e) {
            Toast.makeText(context, "mqttclient.subscribe" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void unsubscribe(String topic,
                            IMqttActionListener cbUnsubscribe) {
        try {
            mqttClient.unsubscribe(topic, null, cbUnsubscribe);
        } catch (MqttException e) {
            Toast.makeText(context, "mqttclient.unsubscribe" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void publish(String topic,
                        String msg,
                        Integer qos,
                        Boolean retained,
                        IMqttActionListener cbPublish) {
        try {
            qos = 0;
            MqttMessage message = new MqttMessage();
            byte[] byteArrray = msg.getBytes();
            message.setPayload(byteArrray);
            message.setQos(qos);
            message.setRetained(retained);
            mqttClient.publish(topic, message, null, cbPublish);
        } catch (MqttException e) {
            Toast.makeText(context, "mqttclient.publish" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void disconnect(IMqttActionListener cbDisconnect) {
        try {
            mqttClient.disconnect(null, cbDisconnect);
        } catch (MqttException e) {
            Toast.makeText(context, "mqttclient.disconnect" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
