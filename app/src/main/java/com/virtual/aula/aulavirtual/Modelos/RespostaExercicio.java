package com.virtual.aula.aulavirtual.Modelos;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RespostaExercicio {
    private String keyListaExercicio;
    private ListaExercicio listaExercicio;
    private HashMap<String,String> respostas;
    private String usuarioResposta;
    private String usuarioProfessor;
    private Integer nAcertos;
    private Integer nPercentual;

    public ListaExercicio getListaExercicio() {
        return listaExercicio;
    }

    public void setListaExercicio(ListaExercicio listaExercicio) {
        this.listaExercicio = listaExercicio;
    }

    public HashMap<String, String> getRespostas() {
        return respostas;
    }

    public void setRespostas(HashMap<String, String> respostas) {
        this.respostas = respostas;
    }

    public String getUsuarioResposta() {
        return usuarioResposta;
    }

    public void setUsuarioResposta(String usuarioResposta) {
        this.usuarioResposta = usuarioResposta;
    }

    public Integer getnAcertos() {
        return nAcertos;
    }

    public void setnAcertos(Integer nAcertos) {
        this.nAcertos = nAcertos;
    }

    public Integer getnPercentual() {
        return nPercentual;
    }

    public void setnPercentual(Integer nPercentual) {
        this.nPercentual = nPercentual;
    }

    public String getKeyListaExercicio() {
        return keyListaExercicio;
    }

    public void setKeyListaExercicio(String keyListaExercicio) {
        this.keyListaExercicio = keyListaExercicio;
    }

    public String getUsuarioProfessor() {
        return usuarioProfessor;
    }

    public void setUsuarioProfessor(String usuarioProfessor) {
        this.usuarioProfessor = usuarioProfessor;
    }
}
