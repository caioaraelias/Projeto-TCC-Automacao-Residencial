package com.example.appmqtttcc.Models;

public class DispositivoBean {
    private Integer id_dispositivo;
    private String nome_dispositivo;
    private String topico_dispositivo;
    private Integer id_placa_dispositivo;
    private String tipo_dispositivo;
    private String saida_digital_dispositivo;

    public static String ID_DISPOSITIVO = "id_dispositivo";
    public static String NOME_DISPOSITIVO = "nome_dispositivo";
    public static String TOPICO_DISPOSITIVO = "topico_dispositivo";
    public static String ID_PLACA_DISPOSITIVO = "id_placa_dispositivo";
    public static String TIPO_DISPOSITIVO = "tipo_dispositivo";
    public static String SAIDA_DIGITAL_DISPOSITIVO = "saida_digital_dispositivo";

    public Integer getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(Integer id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }

    public String getNome_dispositivo() {
        return nome_dispositivo;
    }

    public void setNome_dispositivo(String nome_dispositivo) {
        this.nome_dispositivo = nome_dispositivo;
    }

    public String getTopico_dispositivo() {
        return topico_dispositivo;
    }

    public void setTopico_dispositivo(String topico_dispositivo) {
        this.topico_dispositivo = topico_dispositivo;
    }

    public Integer getId_placa_dispositivo() {
        return id_placa_dispositivo;
    }

    public void setId_placa_dispositivo(Integer id_placa_dispositivo) {
        this.id_placa_dispositivo = id_placa_dispositivo;
    }

    public String getTipo_dispositivo() {
        return tipo_dispositivo;
    }

    public void setTipo_dispositivo(String tipo_dispositivo) {
        this.tipo_dispositivo = tipo_dispositivo;
    }

    public String getSaida_digital_dispositivo() {
        return saida_digital_dispositivo;
    }

    public void setSaida_digital_dispositivo(String saida_digital_dispositivo) {
        this.saida_digital_dispositivo = saida_digital_dispositivo;
    }
}
