package com.unirriter.despesas_pessoais.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.unirriter.despesas_pessoais.R;
import com.unirriter.despesas_pessoais.databinding.ActivityMainBinding;
import com.unirriter.despesas_pessoais.model.Despesa;
import com.unirriter.despesas_pessoais.model.ListaDespesas;
import com.unirriter.despesas_pessoais.utils.MoneyTextWatcher;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView txtLancamento,txtLancamentoDetalhe;
    RecyclerView recyclerView;
    double saldoInicial;
    double valorDespesa;
    double totalDespesa = 0;
    double saldoAtual = saldoInicial;
    Despesa despesa;
    ListaDespesas listaDespesas;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Infla o layout da activity através do binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Inicializa o formulário e os botões da tela
        iniciarFormulario();
        //Inicializa o saldo inicial
        setSaldoInicial();

        //Cria o objeto despesa
        despesa = new Despesa();
        listaDespesas = new ListaDespesas();

        binding.btnSalvar.setOnClickListener(v -> calcularSaldo());
        binding.btnSair.setOnClickListener(v -> finish());
        binding.btnLimpar.setOnClickListener(v -> limparFormulario());
    }

    private void iniciarFormulario() {
        formataCampoValor();
        txtLancamento = (TextView) findViewById(R.id.txtLancamento);
        txtLancamentoDetalhe = (TextView) findViewById(R.id.txtLancamentoDetalhe);
        recyclerView = findViewById(R.id.recyclerview);
    }

    private void imprimirSaldoAtual() {
        binding.txtSaldoInicial.setText("R$ " + saldoInicial);
        binding.txtTotalDespesa.setText("R$ " + totalDespesa);
        binding.txtSaldoFinal.setText("R$ " + saldoAtual);
        if (saldoAtual < 0) {
            binding.txtSaldoFinal.setTextColor(Color.RED);
        } else {
            binding.txtSaldoFinal.setTextColor(Color.DKGRAY);
        }
        System.out.println("Saldo Inicial: " + saldoInicial + "Valor da Despesa" + totalDespesa + " Saldo Final: " + saldoAtual);
    }

    private void calcularSaldo() {
        despesa = new Despesa();
        despesa.setDescricaoDespesa(binding.editDescricaoDespesa.getText().toString());
        despesa.setValorDespesa(removeFormatoValor());

        validarData();

        double valDespesa = removeFormatoValor();
        valorDespesa = valDespesa;
        saldoAtual = saldoAtual - valorDespesa;

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("valorDespesa", (float) valorDespesa);
        editor.putFloat("saldoAtual", (float) saldoAtual);
        editor.apply();

        atualizaDespesaTotal();

        System.out.println("Saldo Inicial: " + saldoInicial + "Valor da Despesa" + valorDespesa + " Saldo Final: " + saldoAtual);

        imprimirSaldoAtual();

        //ação principal do botão salvar - adicionar despesa na lista
        listaDespesas.adicionarDespesas(despesa);

        Log.i("AppDespesa", "Despesa: " + listaDespesas.getDespesas().toString());



        //Cria um layout manager para o recyclerview e define o tipo de layout (vertical e reverso)
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true); // last argument (true) is flag for reverse layout
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        //Cria um adapter para o recyclerview e insere os dados
        DespesaAdapter adapter = new DespesaAdapter(listaDespesas.getListaDespesas());
        recyclerView.setAdapter(adapter);

    }

    private void atualizaDespesaTotal() {
        totalDespesa = totalDespesa + valorDespesa;
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("totalDespesa", (float) totalDespesa);
        editor.apply();
    }

    private void atualizaSaldoAtual() {
        saldoAtual = saldoInicial - valorDespesa;
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("saldoAtual", (float) saldoAtual);
        editor.apply();
    }

    private void setSaldoInicial() {
        //Inicializa o SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("saldoInicial")) {
            saldoInicial = sharedPreferences.getFloat("saldoInicial", 0);
            saldoAtual = sharedPreferences.getFloat("saldoAtual", 0);
            totalDespesa = sharedPreferences.getFloat("totalDespesa", 0);
            imprimirSaldoAtual();
        } else {
            // Create the modal dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Saldo Inicial");
            builder.setMessage("Informe seu saldo inicial:");

            // Add an EditText view to the dialog
            final EditText input = new EditText(this);
            builder.setView(input);

            // Set the positive button for the dialog
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    saldoInicial = Double.parseDouble(input.getText().toString()) ;
                    saldoAtual = saldoInicial;

                    // Save the login data in Shared Preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("saldoInicial", (float) saldoInicial);
                    editor.putFloat("saldoAtual", (float) saldoAtual);
                    editor.apply();

                    imprimirSaldoAtual();
                }
            });
            // Show the dialog
            builder.show();
        }


    }

    private void atualizaUltimosLancamentos() {
        txtLancamento.setText(validarData() + " - " + despesa.getDescricaoDespesa());
        txtLancamentoDetalhe.setText("R$ " + despesa.getValorDespesa());
    }

    private String validarData() {
        String dataFormatada = null;
        try{
            //Converte String em Date
            SimpleDateFormat formatoDataEntrada = new SimpleDateFormat("dd/M/yyyy");
            String dataLancamento = binding.editDataDespesa.getText().toString();
            Date data;
            try {
                data = formatoDataEntrada.parse(dataLancamento);
                despesa.setDataDespesa(data);
            } catch (ParseException e) {
                System.out.println("Erro ao converter data: " + e.getMessage());
                Log.e("AppDespesa", "Erro ao converter data: " + e.getMessage());
            }
            SimpleDateFormat formatoDataSaida = new SimpleDateFormat("dd/MM/yyyy");
            dataFormatada = formatoDataSaida.format(despesa.getDataDespesa());
        } catch (Exception e){
            System.out.println("Erro ao imprimir saldo atual: " + e.getMessage());
        }
        return dataFormatada;
    }
    private void limparFormulario() {
        binding.editDataDespesa.setText("");
        binding.editDescricaoDespesa.setText("");
        binding.editValorDespesa.setText("0");
        // Clear the user session data from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Finish the current activity and start the LoginActivity
        finish();
    }

    private void formataCampoValor() {
        Locale mLocale = new Locale("pt", "BR");
        binding.editValorDespesa.addTextChangedListener(new MoneyTextWatcher(binding.editValorDespesa, mLocale));
    }

    private Double removeFormatoValor() {
        String valorFormatado = binding.editValorDespesa.getText().toString();
        String valorSemFormatacao = valorFormatado.replaceAll("[^\\d]", "");
        double valorNumerico = Double.parseDouble(valorSemFormatacao) / 100.0;
        return valorNumerico;
    }


}