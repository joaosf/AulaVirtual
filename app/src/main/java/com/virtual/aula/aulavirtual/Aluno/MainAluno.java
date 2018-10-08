package com.virtual.aula.aulavirtual.Aluno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.virtual.aula.aulavirtual.ConsultarExercicio_;
import com.virtual.aula.aulavirtual.ListarResultados_;
import com.virtual.aula.aulavirtual.Manager;
import com.virtual.aula.aulavirtual.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main_aluno)
public class MainAluno extends AppCompatActivity {

    @AfterViews
    void init() {
        //
    }

    @Override
    protected void onStart() {
        super.onStart();
        Manager.getInstance().setContext(this);
    }

    @Click
    void btnConListas() {
        Manager.getInstance().abrirNovaTela(ConsultarExercicio_.class,false);
    }

    @Click
    void btnAlunoLogout() {
        Manager.getInstance().desconectarUsuario();
    }

    @Click
    void btnAlunoConResultados() {
        Manager.getInstance().abrirNovaTela(ListarResultados_.class,false);
    }
}
