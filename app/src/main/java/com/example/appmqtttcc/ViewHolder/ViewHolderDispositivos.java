package com.example.appmqtttcc.ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmqtttcc.R;

public class ViewHolderDispositivos extends RecyclerView.ViewHolder{
    public TextView txt_nome_dispositivo;
    public TextView txt_tipo_dispositivo, txt_saida_dispositivo;
    public AppCompatButton btndeletar;
    public AppCompatButton btneditar;
    public LinearLayout opcao_detalhar_dispositivo;

    public ViewHolderDispositivos(@NonNull View itemView) {
        super(itemView);
        this.txt_nome_dispositivo = itemView.findViewById(R.id.txt_nome_dispositivo);
        this.txt_tipo_dispositivo = itemView.findViewById(R.id.txt_tipo_dispositivo);
        this.btndeletar = itemView.findViewById(R.id.btn_deletar_dispositivo);
        this.opcao_detalhar_dispositivo = itemView.findViewById(R.id.opcao_detalhar_dispositivo);
        this.txt_saida_dispositivo = itemView.findViewById(R.id.txt_saida_dispositivo);
    }
}
