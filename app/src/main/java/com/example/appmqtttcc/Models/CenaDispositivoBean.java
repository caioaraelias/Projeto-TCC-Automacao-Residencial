package com.example.appmqtttcc.Models;

public class CenaDispositivoBean {
    private Integer id_cena;
    private Integer id_dispositivo;
    private String valor;

    public static String ID_CENA = "id_cena";
    public static String ID_DISPOSITIVO = "id_dispositivo";
    public static String VALOR = "valor";

    public Integer getId_cena() {
        return id_cena;
    }

    public void setId_cena(Integer id_cena) {
        this.id_cena = id_cena;
    }

    public Integer getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(Integer id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
