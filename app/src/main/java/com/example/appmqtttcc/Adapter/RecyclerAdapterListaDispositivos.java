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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmqtttcc.Client;
import com.example.appmqtttcc.Constants;
import com.example.appmqtttcc.MQTTClient;
import com.example.appmqtttcc.Models.DispositivoBean;
import com.example.appmqtttcc.Models.DispositivoDao;
import com.example.appmqtttcc.R;
import com.example.appmqtttcc.Telas.CadastroDispositivo;
import com.example.appmqtttcc.Telas.ListaDispositivos;
import com.example.appmqtttcc.ViewHolder.ViewHolderDispositivos;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public class RecyclerAdapterListaDispositivos extends RecyclerView.Adapter<ViewHolderDispositivos> {
    private List<DispositivoBean> list;
    private Activity activity;
    private AlertDialog alertDialog, alertDialogDeletar;
    MQTTClient mqttClient;
    String respostaSubscribe = "";
    Boolean mostrarAlerta = true;
    String estadoAtual = "";

    //PASSANDO A ACTIVITY Q CHAMA ESSA CLASSE PARA FECHA-LA QUANDO CLICAR NO BOTÃO DETALHAR DE CADA ITEM
    public RecyclerAdapterListaDispositivos(List<DispositivoBean> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        conectarMQTT();
    }

    @NonNull
    @Override
    public ViewHolderDispositivos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dispositivo, parent, false);
        return new ViewHolderDispositivos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDispositivos holder, int position) {
        final DispositivoBean dispositivoBean = list.get(position);

        holder.txt_nome_dispositivo.setText(dispositivoBean.getNome_dispositivo());
        if (dispositivoBean.getTipo_dispositivo().equals(Constants.D)) {
            holder.txt_tipo_dispositivo.setText("Digital");
            holder.txt_saida_dispositivo.setText(retornaNumeroSaidaDigital(dispositivoBean.getSaida_digital_dispositivo()));
        } else {
            holder.txt_tipo_dispositivo.setText("Analógico");
            holder.txt_saida_dispositivo.setText("1");
        }

        holder.opcao_detalhar_dispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detalhar(view, dispositivoBean);
            }
        });

        holder.btndeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletar(view, dispositivoBean);
            }
        });
    }
    
    public String retornaNumeroSaidaDigital(String saidaPlaca) {
        String saida = "";
        switch (saidaPlaca) {
            case "13":
                saida = "1";
                break;
            case "04":
                saida = "2";
                break;
            case "05":
                saida = "3";
                break;
            case "10":
                saida = "4";
                break;
            case "11":
                saida = "5";
                break;
        }
        
        return saida;
    }

    public void detalhar(View view, DispositivoBean dispositivoBean) {
        AlertDialog.Builder builderAlert = new AlertDialog.Builder(view.getContext());
        mostrarAlerta = true;

        if (mqttClient.isConnected()) {
            mqttClient.subscribe2(Constants.TOPICO_SUBSCRIBE,
                    1,
                    new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Toast.makeText(activity.getApplicationContext(), "Não foi possível subscrever ao topic " , Toast.LENGTH_LONG).show();
                        }
                    },
                    new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {
                            Toast.makeText(activity.getApplicationContext(), "Conexão perdida", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            respostaSubscribe = message.toString();

                            if (!respostaSubscribe.equals("")) {
                                estadoAtual = formataRespostaSubscribe(respostaSubscribe, dispositivoBean.getTipo_dispositivo());
                                builderAlert.setTitle("Alterar estado do dispositivo " + dispositivoBean.getNome_dispositivo());
                                builderAlert.setMessage("Estado atual: " + estadoAtual);

                                if (dispositivoBean.getTipo_dispositivo().equals(Constants.D)) {
                                    builderAlert.setPositiveButton("LIGAR", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alterarValorDispositivo(
                                                    dispositivoBean.getSaida_digital_dispositivo(),
                                                    dispositivoBean.getTopico_dispositivo(),
                                                    dispositivoBean.getTipo_dispositivo(),
                                                    "LIGAR"
                                            );
                                        }
                                    });
                                    builderAlert.setNegativeButton("DESLIGAR", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alterarValorDispositivo(
                                                    dispositivoBean.getSaida_digital_dispositivo(),
                                                    dispositivoBean.getTopico_dispositivo(),
                                                    dispositivoBean.getTipo_dispositivo(),
                                                    "DESLIGAR"
                                            );
                                        }
                                    });
                                } else
                                {
                                    final EditText editTextValor = new EditText(view.getContext());
                                    editTextValor.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    editTextValor.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
                                    editTextValor.setText(estadoAtual);
                                    editTextValor.setPadding(64, 32, 64, 32);
                                    builderAlert.setMessage("Digite um valor (entre 0 e 99):");
                                    builderAlert.setView(editTextValor);

                                    builderAlert.setPositiveButton("ALTERAR VALOR", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String valor = editTextValor.getText().toString();
                                            alterarValorDispositivo(
                                                    dispositivoBean.getSaida_digital_dispositivo(),
                                                    dispositivoBean.getTopico_dispositivo(),
                                                    dispositivoBean.getTipo_dispositivo(),
                                                    valor
                                            );
                                        }
                                    });
                                }
                            } else {
                                builderAlert.setTitle("Impossível buscar estado do dispositivo" + dispositivoBean.getNome_dispositivo());
                            }

                            alertDialog = builderAlert.create();
                            if (mostrarAlerta) {
                                alertDialog.show();

                                if (dispositivoBean.getTipo_dispositivo().equals(Constants.D)) {
                                    if (estadoAtual.equals("Ligado")) {
                                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.green));
                                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.black));
                                    } else {
                                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.black));
                                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.green));
                                    }
                                }
                            }
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
                        }
                    });
        } else {
            Toast.makeText(activity.getApplicationContext(), "Impossível subscrever! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }

        //TENHO QUE MANDAR UM PUBLISH ANTES DO SUBSCRIBE PARA ESP RECEBER QUAL SAÍDA LER E MANDAR RESPOSTA
        //SE FOR ANALÓGICO, VAI SER 04000000

        String comando = "";
        if (dispositivoBean.getTipo_dispositivo().equals("D")) {
            comando = Constants.acaoLeitura + dispositivoBean.getSaida_digital_dispositivo() + "0000";
        } else {
            comando = Constants.acaoLeitura + "000000";
        }

        publish2(dispositivoBean.getTopico_dispositivo(), comando);
    }

    public void deletar(View view, DispositivoBean dispositivoBean) {
        AlertDialog.Builder builderAlertDeletar = new AlertDialog.Builder(view.getContext());
        DispositivoDao dispositivoDao = new DispositivoDao(view.getContext());

        builderAlertDeletar.setTitle("Deseja realmente excluir o dispositivo " + dispositivoBean.getNome_dispositivo() + " ?");
        builderAlertDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    dispositivoDao.deletarDispositivo(dispositivoBean.getId_dispositivo());
                    Toast.makeText(view.getContext(), "Dispositivo excluído com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity.getApplicationContext(), ListaDispositivos.class);
                    view.getContext().startActivity(intent);
                    activity.finish();
                } catch (SQLiteException e) {
                    Toast.makeText(view.getContext(), "Erro ao excluir dispositivo", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(activity.getApplicationContext(), "Valor alterado com sucesso!", Toast.LENGTH_LONG).show();
                                mostrarAlerta = false;
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Toast.makeText(activity.getApplicationContext(), "Erro ao alterar valor!", Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (Throwable e) {
                Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity.getApplicationContext(), "Impossível publicar! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }
    }

    public void publish2(String topic, String message) {
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
                                Toast.makeText(activity.getApplicationContext(), "Erro ao alterar valor!", Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (Throwable e) {
                Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity.getApplicationContext(), "Impossível publicar! Não conectado ao broker", Toast.LENGTH_LONG).show();
        }
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
            String message = Constants.acaoAnalogico;
            String valorHexaDecimal = "";
            if (Integer.parseInt(valor) >= 10 && Integer.parseInt(valor) <= 15) {
                valorHexaDecimal = "0" + Integer.toHexString(Integer.parseInt(valor));
            } else {
                valorHexaDecimal = Integer.toHexString(Integer.parseInt(valor));
            }
            String comando = message + "00" + valorHexaDecimal + "00";

            publish(topico, comando);
        }
    }

    public String formataRespostaSubscribe(String respostaSubscribe, String tipo) {
        // A RESPOSTA SÃO 6 CARACTERES COM O VALOR NO DOIS ÚLTIMOS (00 A 99), TUDO EM HEXADECIMAL
        String valor = respostaSubscribe.substring(4, 6);
        if (tipo.equals(Constants.D)) {
            if (valor.equals(Constants.acaoDesligar)) {
                valor = "Desligado";
            } else if (valor.equals(Constants.acaoLigar)) {
                valor = "Ligado";
            }
        } else {
            int valorAnalogico = Integer.parseInt(valor, 16);
            valor = Integer.toString(valorAnalogico);
        }
        return valor;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
