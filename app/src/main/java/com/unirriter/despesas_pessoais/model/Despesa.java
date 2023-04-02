package com.unirriter.despesas_pessoais.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Despesa {

    //Instancia das variáveis
    private Date dataDespesa;
    private String descricaoDespesa;
    private double valorDespesa;

    //Construtor da classe
    public Despesa() {
    }

    public Despesa(Date dataDespesa) {
        this.dataDespesa = dataDespesa;
    }

    public Despesa(String descricaoDespesa, double valorDespesa) {
        this.descricaoDespesa = descricaoDespesa;
        this.valorDespesa = valorDespesa;
    }

    public Despesa(Date dataDespesa, String descricaoDespesa, double valorDespesa) {
        this.dataDespesa = dataDespesa;
        this.descricaoDespesa = descricaoDespesa;
        this.valorDespesa = valorDespesa;
    }

    //Getters and Setters
    public Date getDataDespesa() {
        return dataDespesa;
    }

    public void setDataDespesa(Date dataDespesa) {
        this.dataDespesa = dataDespesa;
    }

    public String getDescricaoDespesa() {
        return descricaoDespesa;
    }

    public void setDescricaoDespesa(String descricaoDespesa) {
        this.descricaoDespesa = descricaoDespesa;
    }

    public double getValorDespesa() {
        return valorDespesa;
    }

    public void setValorDespesa(double valorDespesa) {
        this.valorDespesa = valorDespesa;
    }

    public void imprimeDespesa() {
        System.out.println("Data: " + this.dataDespesa);
        System.out.println("Descrição: " + this.descricaoDespesa);
        System.out.println("Valor: " + this.valorDespesa);
    }

    public String getDataFormatada() {
        Locale locale = new Locale("pt", "BR");
        String pattern = "dd/MM/yyyy";
        String dataFormatada;

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);

        // Format the Date value using the SimpleDateFormat object
        dataFormatada = sdf.format(dataDespesa);

        // Display the formatted date
        System.out.println(dataFormatada);
        return dataFormatada;
    }

    @Override
    public String toString() {
        return "Data: " + this.dataDespesa + " - Descrição: " + this.descricaoDespesa + " - Valor: " + this.valorDespesa;
    }
}