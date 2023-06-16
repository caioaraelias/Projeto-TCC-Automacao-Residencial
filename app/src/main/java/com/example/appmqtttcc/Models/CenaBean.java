package com.example.appmqtttcc.Models;

public class CenaBean {
    private Integer id_cena;
    private String nome_cena;

    public static String ID_CENA = "id_cena";
    public static String NOME_CENA = "nome_cena";

    public Integer getId_cena() {
        return id_cena;
    }

    public void setId_cena(Integer id_cena) {
        this.id_cena = id_cena;
    }

    public String getNome_cena() {
        return nome_cena;
    }

    public void setNome_cena(String nome_cena) {
        this.nome_cena = nome_cena;
    }
}
