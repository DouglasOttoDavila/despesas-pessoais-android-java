package com.unirriter.despesas_pessoais.model;

import java.util.ArrayList;

public class ListaDespesas {
    private ArrayList<Despesa> despesas = new ArrayList<Despesa>();

    public ArrayList<Despesa> getDespesas() {
        return despesas;
    }

    public ListaDespesas() {
        despesas = new ArrayList<Despesa>();
    }

    public ArrayList<Despesa> getListaDespesas() {
        return despesas;
    }

    public void setListaDespesas(ArrayList<Despesa> despesas) {
        this.despesas = despesas;
    }

    public void adicionarDespesas(Despesa despesas) {
        this.despesas.add(despesas);
    }

    public void apagarTudo() {
        this.despesas.clear();
    }

    public int obterQuantidadeDespesas() {
        return this.despesas.size();
    }

    public Despesa getListDespesas(int index) {
        return this.despesas.get(index);
    }

    public void listaTodasDespesas() {
        for (int i = 0; i < this.despesas.size(); i++) {
            System.out.println("Data: " + this.despesas.get(i).getDataDespesa());
            System.out.println("Descrição: " + this.despesas.get(i).getDescricaoDespesa());
            System.out.println("Valor: " + this.despesas.get(i).getValorDespesa());
        }
    }

    public void listDescricaoDespesas(int index) {
        for (int i = 0; i < this.despesas.size(); i++) {
            System.out.println("Descrição: " + this.despesas.get(index).getDescricaoDespesa());
        }

    }

    public void listDataDespesas(int index) {
        for (int i = 0; i < this.despesas.size(); i++) {
            System.out.println("Data: " + this.despesas.get(index).getDataDespesa());
        }
    }

    public void listDataDescricaoDespesas(int index) {
        for (int i = 0; i < this.despesas.size(); i++) {
            System.out.println("Data: " + this.despesas.get(index).getDataDespesa());
            System.out.println("Descrição: " + this.despesas.get(index).getDescricaoDespesa());
        }
    }

    @Override
    public String toString() {
        return "ListaDespesas{" +
                "despesas=" + despesas +
                '}';
    }




}
