package com.virtual.aula.aulavirtual.Modelos;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.virtual.aula.aulavirtual.Manager;

import java.util.HashMap;

import Anotacoes.Fields;
import Anotacoes.FrameworkAnnotation;
import Controladores.Operacoes;
import Modelos.Filtro;

@FrameworkAnnotation(tableName = "Usuarios",keepSync = true)
public class Usuario {
    @Fields(name = "Nome", key = false)
    public String Nome;
    @Fields(name = "Sobrenome", key = false)
    public String Sobrenome;
    @Fields(name = "Login", key = true)
    public String Login;
    @Fields(name = "Senha", key = false)
    public String Senha;
    @Fields(name = "Tipo", key = false)
    public String Tipo;

    public static Usuario parseOf(Object object) {
        try {
            HashMap<String,String> hashMap = (HashMap<String, String>) object;
            Usuario usuarioModel = new Usuario();

            usuarioModel.setNome(hashMap.get("Nome"));
            usuarioModel.setSobrenome(hashMap.get("Sobrenome"));
            usuarioModel.setLogin(hashMap.get("Login"));
            usuarioModel.setSenha(hashMap.get("Senha"));
            usuarioModel.setTipo(hashMap.get("Tipo"));

            return usuarioModel;
        } catch (Exception e) {
            return null;
        }
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getSobrenome() {
        return Sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        Sobrenome = sobrenome;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }
}
