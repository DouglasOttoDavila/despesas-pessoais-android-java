package com.unirriter.despesas_pessoais.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.unirriter.despesas_pessoais.R;
import com.unirriter.despesas_pessoais.model.Despesa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        String dataDespesa = data.get(position).getDataFormatada();
        String descricaoDespesa = data.get(position).getDescricaoDespesa();
        String valorDespesa = String.valueOf(data.get(position).getValorDespesa());
        holder.txtLancamentoDetalhe.setText(descricaoDespesa);
        holder.txtLancamento.setText(dataDespesa + "\nR$ " + valorDespesa);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
