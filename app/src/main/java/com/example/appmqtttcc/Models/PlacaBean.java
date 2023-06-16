package com.example.appmqtttcc.Models;

public class PlacaBean {
    private Integer id_placa;
    private String codigo_placa;

    public static String ID_PLACA = "id_placa";
    public static String CODIGO_PLACA = "codigo_placa";

    public Integer getId_placa() {
        return id_placa;
    }

    public void setId_placa(Integer id_placa) {
        this.id_placa = id_placa;
    }

    public String getCodigo_placa() {
        return codigo_placa;
    }

    public void setCodigo_placa(String codigo_placa) {
        this.codigo_placa = codigo_placa;
    }
}
