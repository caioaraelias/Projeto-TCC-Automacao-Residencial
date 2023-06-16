package com.example.appmqtttcc.ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmqtttcc.R;

public class ViewHolderCenas extends RecyclerView.ViewHolder{
    public TextView txt_nome_cena;
    public AppCompatButton btn_executar_cena, btn_deletar_cena;
    public LinearLayout layout_infos_cena;

    public ViewHolderCenas(@NonNull View itemView) {
        super(itemView);
        this.txt_nome_cena = itemView.findViewById(R.id.txt_nome_cena);
        this.btn_executar_cena = itemView.findViewById(R.id.btn_executar_cena);
        this.btn_deletar_cena = itemView.findViewById(R.id.btn_deletar_cena);
        this.layout_infos_cena = itemView.findViewById(R.id.layout_infos_cena);
    }
}
