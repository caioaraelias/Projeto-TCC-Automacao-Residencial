package com.example.appmqtttcc.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmqtttcc.Client;
import com.example.appmqtttcc.Constants;
import com.example.appmqtttcc.MQTTClient;
import com.example.appmqtttcc.Models.CenaBean;
import com.example.appmqtttcc.Models.CenaDao;
import com.example.appmqtttcc.Models.CenaDispositivoBean;
import com.example.appmqtttcc.Models.CenaDispositivoDao;
import com.example.appmqtttcc.Models.DispositivoBean;
import com.example.appmqtttcc.Models.DispositivoDao;
import com.example.appmqtttcc.R;
import com.example.appmqtttcc.Telas.ListaCenas;
import com.example.appmqtttcc.ViewHolder.ViewHolderCenas;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterListaCenas extends RecyclerView.Adapter<ViewHolderCenas> {
    private List<CenaBean> list;
    private Activity activity;
    private AlertDialog alertDialogInfoCena, alertDialogDeletar;
    MQTTClient mqttClient;

    //PASSANDO A ACTIVITY Q CHAMA ESSA CLASSE PARA FECHA-LA QUANDO CLICAR NO BOTÃO DETALHAR DE CADA ITEM
    public RecyclerAdapterListaCenas(List<CenaBean> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        conectarMQTT();
    }

    @NonNull
    @Override
    public ViewHolderCenas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cena, parent, false);
        return new ViewHolderCenas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCenas holder, int position) {
        final CenaBean cenaBean = list.get(position);

        holder.txt_nome_cena.setText(cenaBean.getNome_cena());

        holder.btn_executar_cena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executarCena(cenaBean);
            }
        });

        holder.btn_deletar_cena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletar(view, cenaBean);
            }
        });

        holder.layout_infos_cena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infosCena(view, cenaBean);
            }
        });
    }

    public void infosCena(View view, CenaBean cenaBean) {
        AlertDialog.Builder builderAlertInfoCena = new AlertDialog.Builder(view.getContext());
        builderAlertInfoCena.setTitle("Configuração da cena " + cenaBean.getNome_cena());

        CenaDispositivoDao cenaDispositivoDao = new CenaDispositivoDao(view.getContext());
        List<CenaDispositivoBean> cenaDispositivoBeanList = new ArrayList<>();
        cenaDispositivoBeanList = cenaDispositivoDao.buscarCenasDispositivosList(cenaBean.getId_cena());

        if (cenaDispositivoBeanList.size() > 0){
            LinearLayout layout = new LinearLayout(view.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(64, 32, 64, 32);
            for (CenaDispositivoBean cenaDispositivoBean : cenaDispositivoBeanList) {
                DispositivoDao dispositivoDao = new DispositivoDao(activity.getApplicationContext());
                DispositivoBean dispositivoBean = dispositivoDao.buscarDispositivo(cenaDispositivoBean.getId_dispositivo());

                final TextView textView = new TextView(view.getContext());

                if (dispositivoBean.getTipo_dispositivo().equals(Constants.D)) {
                    if (cenaDispositivoBean.getValor().equals(Constants.L)) {
                        textView.setText(dispositivoBean.getNome_dispositivo() + " - " + Constants.LIGAR);
                    } else {
                        textView.setText(dispositivoBean.getNome_dispositivo() + " - " + Constants.DESLIGAR);
                    }
                } else {
                    textView.setText(dispositivoBean.getNome_dispositivo() + " - " + cenaDispositivoBean.getValor() + "%");
                }

                textView.setPadding(16, 16, 16, 16);
                textView.setTextColor(activity.getResources().getColor(R.color.black));
                layout.addView(textView);
            }
            builderAlertInfoCena.setView(layout);
        } else {
            final TextView textView = new TextView(view.getContext());
            textView.setText("Nenhum dispositivo configurado para a cena!");
        }

        alertDialogInfoCena = builderAlertInfoCena.create();
        alertDialogInfoCena.show();
    }

    public void deletar(View view, CenaBean cenaBean) {
        AlertDialog.Builder builderAlertDeletar = new AlertDialog.Builder(view.getContext());
        CenaDao cenaDao = new CenaDao(view.getContext());
        CenaDispositivoDao cenaDispositivoDao = new CenaDispositivoDao(view.getContext());

        builderAlertDeletar.setTitle("Deseja realmente excluir a cena " + cenaBean.getNome_cena() + " ?");
        builderAlertDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    cenaDao.deletarCena(cenaBean.getId_cena());
                    cenaDispositivoDao.deletarCenaDispositivo(cenaBean.getId_cena());
                    Toast.makeText(view.getContext(), "Cena excluída com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity.getApplicationContext(), ListaCenas.class);
                    view.getContext().startActivity(intent);
                    activity.finish();
                } catch (SQLiteException e) {
                    Toast.makeText(view.getContext(), "Erro ao excluir cena", Toast.LENGTH_LONG).show();
                }
            }
        });
        builderAlertDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        alertDialogDeletar = builderAlertDeletar.create();
        alertDialogDeletar.show();
    }

    public void executarCena(CenaBean cenaBean) {
        try {
            CenaDispositivoDao cenaDispositivoDao = new CenaDispositivoDao(activity.getApplicationContext());
            List<CenaDispositivoBean> cenaDispositivoBeanList = new ArrayList<>();
            cenaDispositivoBeanList = cenaDispositivoDao.buscarCenasDispositivosList(cenaBean.getId_cena());

            if (cenaDispositivoBeanList.size() > 0){
                for (CenaDispositivoBean cenaDispositivoBean : cenaDispositivoBeanList) {
                    DispositivoDao dispositivoDao = new DispositivoDao(activity.getApplicationContext());
                    DispositivoBean dispositivoBean = dispositivoDao.buscarDispositivo(cenaDispositivoBean.getId_dispositivo());
                    alterarValorDispositivo(dispositivoBean.getSaida_digital_dispositivo(),
                            dispositivoBean.getTopico_dispositivo(),
                            dispositivoBean.getTipo_dispositivo(), cenaDispositivoBean.getValor());
                }
            }
            Toast.makeText(activity.getApplicationContext(), "Cena " + cenaBean.getNome_cena() + " executada com sucesso!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), "Erro ao executar cena!", Toast.LENGTH_LONG).show();
        }
    }

    public void alterarValorDispositivo(String saida, String topico, String tipo, String valor)
    {
        if (tipo.equals(Constants.D)) {
            String message = "";
            if (valor.equals(Constants.L)) {
                message = Constants.acaoLigar;
            } else {
                message = Constants.acaoDesligar;
            }
            String comando = message + saida + "0000";
            publish(topico, comando);
        } else {
            String message = Constants.acaoAnalogico;
            String comando = message + "00" + Integer.toHexString(Integer.parseInt(valor)) + "00";
            publish(topico, comando);
        }
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
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Toast.makeText(activity.getApplicationContext(), "Erro ao publicar", Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (Throwable e) {
                Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity.getApplicationContext(), "Impossível publicar! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }
    }

    public void conectarMQTT() {
        mqttClient = new MQTTClient(activity.getApplicationContext(), Constants.MQTT_SERVER_URI, Constants.MQTT_CLIENT_ID);

        mqttClient.connect(
                "",
                "",
                new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Toast.makeText(activity.getApplicationContext(), "Conexão MQTT não realizada", Toast.LENGTH_LONG).show();
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

    @Override
    public int getItemCount() {
        return list.size();
    }
}
