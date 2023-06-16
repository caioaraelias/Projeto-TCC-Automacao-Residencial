package com.example.appmqtttcc;

import java.lang.reflect.Array;

public class Constants {
    public static String MQTT_SERVER_URI       = "tcp://mqtt.eclipseprojects.io:1883";
    public static String MQTT_CLIENT_ID        = "mqttdash-9a233127";
    public static String MQTT_USERNAME         = "";
    public static String MQTT_PWD              = "";
    public static String MQTT_TEST_TOPIC       = "tccautomacao/comando/";

    //XX (COMANDO QUE IREI MANDAR PARA O ESP32) -> 01 (LIGAR); 00(DESLIGAR) ; 03(ANALÓGICO) ; 02(INVERTE ESTADO) ; 04 (leitura)
    //YY (SAÍDA DIGITAL) -> 13 ; 04 ; 05 ; 10 ; 11
    //ZZ (VALOR DO ANALÓGICO, SE FOR DIGITAL VOU MANDAR 66) -> 0 A 99 EM HEXADECIMAL
    public static String MQTT_TEST_MSG         = "XXYYZZ00";

    public static String MQTT_TEST_TOPIC_2      = "test/topic";
    public static String MQTT_TEST_MSG_2       = "Hello!";

    public static String TOPICO_COMANDO       = "tccautomacao/comando/";
    public static String TOPICO_SUBSCRIBE       = "tccautomacao/leitura/";

    public static String LIGAR = "LIGAR";
    public static String DESLIGAR = "DESLIGAR";
    public static String L = "L";
    public static String D = "D";
    public  static String acaoLigar = "01";
    public  static String acaoDesligar = "00";
    public  static String acaoAnalogico = "03";
    public  static String acaoLeitura = "04";
}
