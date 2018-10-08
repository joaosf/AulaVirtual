package com.virtual.aula.aulavirtual;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.virtual.aula.aulavirtual.Aluno.MainAluno_;
import com.virtual.aula.aulavirtual.Modelos.Usuario;
import com.virtual.aula.aulavirtual.Professor.MainProfessor_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import Controladores.Operacoes;
import Modelos.Filtro;

@EActivity(R.layout.activity_login)
public class Login extends AppCompatActivity {

    @ViewById
    EditText txtLoginNomeUsuario;

    @ViewById
    EditText txtLoginSenha;

    @ViewById
    CheckBox chkLoginManter;

    @AfterViews
    protected void init() {
        //
    }

    @Override
    protected void onStart() {
        Manager.getInstance().setContext(this);
        Manager.getInstance().verificarManterConectado();
        super.onStart();
        Manager.getInstance().verificarTaskAfterShow();
    }

    @Click
    void btnLoginEntrar() {
        final ProgressDialog pd = Manager.getInstance().showLoadingDialog("Entrando");
        Query qry = Operacoes.Select(Manager.childUsuarios,new Filtro("Login",txtLoginNomeUsuario.getText().toString()));
        qry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuarioModel = Usuario.parseOf(dataSnapshot.child(txtLoginNomeUsuario.getText().toString()).getValue());
                if (usuarioModel != null) {
                    if (usuarioModel.getSenha().equals(txtLoginSenha.getText().toString())) {
                        pd.dismiss();
                        Manager.getInstance().manterUsuarioConectado(chkLoginManter.isChecked(),usuarioModel.getLogin());
                        if (Manager.getInstance().getVersaoApp().equals(Manager.VersaoApp.Professor.toString())) {
                            Manager.getInstance().abrirNovaTela(MainProfessor_.class, true);
                        } else {
                            Manager.getInstance().abrirNovaTela(MainAluno_.class, true);
                        }
                    } else {
                        pd.dismiss();
                        Manager.getInstance().showMessageDialog(
                                "Senha inválida!",""
                        );
                    }
                } else {
                    pd.dismiss();
                    Manager.getInstance().showMessageDialog(
                            "Login inválido!",""
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
                Manager.getInstance().showMessageDialog(
                        "Ops",
                        "Tivemos problemas para encontrar seu usuário, tente novamente!"
                );
            }
        });

    }

    @Click
    void btnLoginCadastrar() {
        Manager.getInstance().abrirNovaTela(CadastroUsuario_.class, false);
    }

}
