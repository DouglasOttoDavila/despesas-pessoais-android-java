package com.unirriter.despesas_pessoais.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.unirriter.despesas_pessoais.R;
import com.unirriter.despesas_pessoais.model.Despesa;
import com.unirriter.despesas_pessoais.view.MyViewHolder;
import java.util.ArrayList;
import java.util.Collections;

public class DespesaAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private ArrayList<Despesa> data;

    public DespesaAdapter(ArrayList<Despesa> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String dataDespesa = data.get(position).getDataDespesa().toString();
        String descricaoDespesa = data.get(position).getDescricaoDespesa();
        holder.txtLancamentoDetalhe.setText(descricaoDespesa);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
