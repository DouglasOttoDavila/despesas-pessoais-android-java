package com.unirriter.despesas_pessoais.view;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.unirriter.despesas_pessoais.R;

import org.w3c.dom.Text;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView txtLancamento;
    public TextView txtLancamentoDetalhe;

    public MyViewHolder(View itemView) {
        super(itemView);
        txtLancamento = itemView.findViewById(R.id.txtLancamento);
        txtLancamentoDetalhe = itemView.findViewById(R.id.txtLancamentoDetalhe);
    }
}