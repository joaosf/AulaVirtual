package com.virtual.aula.aulavirtual.Modelos;

import com.google.firebase.database.DataSnapshot;
import com.virtual.aula.aulavirtual.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListaExercicio {
    private String Titulo;
    private String Categoria;
    private String Usuario;
    private String CategoriaUsuario;
    private int Dificuldade;
    private ArrayList<Exercicio> Exercicios;

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public List<Exercicio> getExercicios() {
        return Exercicios;
    }

    public void setExercicios(ArrayList<Exercicio> exercicios) {
        Exercicios = exercicios;
    }

    public void addExercicios(Exercicio exercicio) {
        Exercicios = Exercicios == null ? new ArrayList<Exercicio>() : Exercicios;
        Exercicios.add(exercicio);
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getCategoriaUsuario() {
        return CategoriaUsuario;
    }

    public void setCategoriaUsuario(String categoriaUsuario) {
        CategoriaUsuario = categoriaUsuario;
    }

    public int getDificuldade() {
        return Dificuldade;
    }

    public void setDificuldade(int dificuldade) {
        Dificuldade = dificuldade;
    }
}
