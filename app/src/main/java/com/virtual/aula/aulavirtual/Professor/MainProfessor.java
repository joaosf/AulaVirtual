package com.virtual.aula.aulavirtual.Professor;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.virtual.aula.aulavirtual.ConsultarExercicio_;
import com.virtual.aula.aulavirtual.ListarResultados;
import com.virtual.aula.aulavirtual.ListarResultados_;
import com.virtual.aula.aulavirtual.Manager;
import com.virtual.aula.aulavirtual.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main_professor)
public class MainProfessor extends AppCompatActivity {

    @AfterViews
    protected void init() {
        //
    }

    @Override
    protected void onStart() {
        Manager.getInstance().setContext(this);
        super.onStart();
    }

    @Click
    void btnCadLista() {
        Manager.getInstance().abrirNovaTela(CadastroLista_.class,false);
    }

    @Click
    void btnCadCategoria() {
        Manager.getInstance().showInputDialog(
                "Cadastro de Matéria",
                "Informe a descrição da matéria que deseja cadastrar:",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Manager.getInstance().addCategoria(Manager.getInstance().getTmpString(),true);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );
    }

    @Click
    void btnConListas() {
        Manager.getInstance().setTmpString(Manager.getInstance().getUsuarioLogado());
        Manager.getInstance().abrirNovaTela(ConsultarExercicio_.class,false);
    }

    @Click
    void btnProfessorLogout() {
        Manager.getInstance().desconectarUsuario();
    }

    @Click
    void btnProfessorConResultados() {
        Manager.getInstance().abrirNovaTela(ListarResultados_.class,false);
    }
}
