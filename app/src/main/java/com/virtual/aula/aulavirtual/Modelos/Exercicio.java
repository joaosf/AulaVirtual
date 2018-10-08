package com.virtual.aula.aulavirtual.Modelos;

import java.util.HashMap;

public class Exercicio {
    private String Titulo;
    private String Descricao;
    private HashMap<String, Boolean> Alternativas;

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        this.Titulo = titulo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        this.Descricao = descricao;
    }

    public HashMap<String, Boolean> getAlternativas() {
        return Alternativas;
    }

    public void setAlternativas(HashMap<String, Boolean> alternativas) {
        this.Alternativas = alternativas;
    }

    public void addAlternativa(String descricaoAlternativa, Boolean ativo) {
        Alternativas = Alternativas == null ? new HashMap<String, Boolean>() : Alternativas;
        Alternativas.put(descricaoAlternativa+" ",ativo);
    }
}
