package com.unirriter.despesas_pessoais;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unirriter.despesas_pessoais.view.DespesaAdapter;
import com.unirriter.despesas_pessoais.model.Despesa;
import com.unirriter.despesas_pessoais.model.ListaDespesas;
import com.unirriter.despesas_pessoais.utils.MoneyTextWatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editDataDespesa, editDescricaoDespesa, editValorDespesa;
    Button btnSalvar, btnLimpar, btnSair;
    TextView txtSaldoInicial, txtTotalDespesa, txtSaldoFinal, txtLancamento,txtLancamentoDetalhe;
    RecyclerView recyclerView;
    double saldoInicial;
    double valorDespesa;
    double totalDespesa = 0;
    double saldoAtual = saldoInicial;
    Despesa despesa;
    ListaDespesas listaDespesas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarFormulario();
        setSaldoInicial();


        //Cria o objeto despesa
        despesa = new Despesa();
        listaDespesas = new ListaDespesas();

        /*Toast.makeText(getBaseContext(), "Despesa: " + despesa.getDescricaoDespesa() + " - Valor: " + despesa.getValorDespesa(), Toast.LENGTH_LONG).show();*/

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcularSaldo();
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparFormulario();
            }
        });
    }

    private void iniciarFormulario() {
        editDataDespesa = (EditText) findViewById(R.id.editDataDespesa);
        editDescricaoDespesa = (EditText) findViewById(R.id.editDescricaoDespesa);
        editValorDespesa = (EditText) findViewById(R.id.editValorDespesa);
        formataCampoValor();
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnLimpar = (Button) findViewById(R.id.btnLimpar);
        btnSair = (Button) findViewById(R.id.btnSair);
        txtSaldoInicial = (TextView) findViewById(R.id.txtSaldoInicial);
        txtTotalDespesa = (TextView) findViewById(R.id.txtTotalDespesa);
        txtSaldoFinal = (TextView) findViewById(R.id.txtSaldoFinal);
        txtLancamento = (TextView) findViewById(R.id.txtLancamento);
        txtLancamentoDetalhe = (TextView) findViewById(R.id.txtLancamentoDetalhe);
        recyclerView = findViewById(R.id.recyclerview);
    }

    private void imprimirSaldoAtual() {
        txtSaldoInicial.setText("R$ " + String.valueOf(saldoInicial));
        txtTotalDespesa.setText("R$ " + String.valueOf(totalDespesa));
        txtSaldoFinal.setText("R$ " + String.valueOf(saldoAtual));
        if (saldoAtual < 0) {
            txtSaldoFinal.setTextColor(Color.RED);
        } else {
            txtSaldoFinal.setTextColor(Color.DKGRAY);
        }
        System.out.println("Saldo Inicial: " + saldoInicial + "Valor da Despesa" + totalDespesa + " Saldo Final: " + saldoAtual);
    }

    private void calcularSaldo() {
        despesa = new Despesa();
        despesa.setDescricaoDespesa(editDescricaoDespesa.getText().toString());
        despesa.setValorDespesa(removeFormatoValor());

        validarData();

        double valDespesa = removeFormatoValor();
        valorDespesa = valDespesa;
        saldoAtual = saldoAtual - valorDespesa;
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
    }

    private void atualizaSaldoAtual() {
        saldoAtual = saldoInicial - valorDespesa;
    }

    private void setSaldoInicial() {
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
                imprimirSaldoAtual();
            }
        });

        // Show the dialog
        builder.show();
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
            String dataLancamento = editDataDespesa.getText().toString();
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
        editDataDespesa.setText("");
        editDescricaoDespesa.setText("");
        editValorDespesa.setText("0");
        System.out.println("Saldo Inicial: " + saldoInicial + "Valor da Despesa" + valorDespesa + " Saldo Final: " + saldoAtual);
    }

    private void formataCampoValor() {
        Locale mLocale = new Locale("pt", "BR");
        editValorDespesa.addTextChangedListener(new MoneyTextWatcher(editValorDespesa, mLocale));
    }

    private Double removeFormatoValor() {
        String valorFormatado = editValorDespesa.getText().toString();
        String valorSemFormatacao = valorFormatado.replaceAll("[^\\d]", "");
        double valorNumerico = Double.parseDouble(valorSemFormatacao) / 100.0;
        return valorNumerico;
    }


}