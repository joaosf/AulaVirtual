package com.virtual.aula.aulavirtual.Modelos;

import java.util.HashMap;

public class Categorias {
    private HashMap<String, Boolean> itens;

    public Categorias() {
        this.itens = new HashMap<>();
    }

    public HashMap<String, Boolean> getItens() {
        return itens;
    }

    public void setItens(HashMap<String, Boolean> itens) {
        this.itens = itens;
    }

    public void addItem(String itemDesc, Boolean ativo) {
        this.itens.put(itemDesc,ativo);
    }

    public void removeItem(String key) {
        this.itens.remove(key);
    }
}
