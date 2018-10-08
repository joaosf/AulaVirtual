package com.virtual.aula.aulavirtual;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.virtual.aula.aulavirtual.Modelos.Usuario;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.KeyDown;
import org.androidannotations.annotations.ViewById;

import Controladores.Operacoes;
import Modelos.Filtro;

@EActivity(R.layout.activity_cadastro_usuario)
public class CadastroUsuario extends AppCompatActivity {

    @ViewById
    EditText txtUsuarioNome;
    @ViewById
    EditText txtUsuarioSobrenome;
    @ViewById
    EditText txtUsuarioLogin;
    @ViewById
    EditText txtUsuarioSenha;

    private boolean loginValido = false;

    @AfterViews
    protected void init() {
        setTitle("Cadastro de "+Manager.getInstance().getVersaoApp());
        txtUsuarioLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                verificarCadastro();
            }
        });
    }

    @Override
    protected void onStart() {
        Manager.getInstance().setContext(this);
        super.onStart();
    }

    @Click
    void btnUsuarioCancelar() {
        finish();
    }

    @Click
    void btnUsuarioSalvar() {
        salvarUsuario();
    }

    private void verificarCadastro() {
        Query qry = Operacoes.Select(Manager.childUsuarios, new Filtro("Login",txtUsuarioLogin.getText().toString()));
        qry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Usuario usuarioModel = Usuario.parseOf(dataSnapshot.child(txtUsuarioLogin.getText().toString()).getValue());
                    if (usuarioModel != null) {
                        txtUsuarioLogin.setError("Login já cadastrado, escolha outro!");
                        loginValido = false;
                    } else {
                        loginValido = true;
                    }
                } catch (Exception e) {
                    txtUsuarioLogin.setError("Login inválido (utilize somente letras e números)!");
                    loginValido = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean validarDados() {
        String nomeUsuario = txtUsuarioNome.getText().toString();
        String sobrenomeUsuario = txtUsuarioSobrenome.getText().toString();
        String loginUsuario = txtUsuarioLogin.getText().toString();
        String senhaUsuario = txtUsuarioSenha.getText().toString();
        boolean retorno = true;
        if (nomeUsuario.isEmpty()) {
            txtUsuarioNome.setError("Informe o nome de usuário!");
            retorno = false;
        }
        if (sobrenomeUsuario.isEmpty()) {
            txtUsuarioSobrenome.setError("Informe o sobrenome de usuário!");
            retorno = false;
        }
        if (!loginValido) {
            txtUsuarioLogin.setError("Login inválido, tente outro!");
            retorno = false;
        }
        if (senhaUsuario.length() < 6) {
            txtUsuarioSenha.setError("Sua senha precisa ter ao menos 6 caracteres, tente outra!");
            retorno = false;
        }
        return retorno;
    }

    private void salvarUsuario() {
        if (!validarDados()) {
            return;
        }
        Usuario usuarioModel = new Usuario();

        usuarioModel.setNome(txtUsuarioNome.getText().toString());
        usuarioModel.setSobrenome(txtUsuarioSobrenome.getText().toString());
        usuarioModel.setLogin(txtUsuarioLogin.getText().toString());
        usuarioModel.setSenha(txtUsuarioSenha.getText().toString());
        usuarioModel.setTipo(Manager.getInstance().getVersaoApp());

        final ProgressDialog pd = Manager.getInstance().showLoadingDialog("Salvando");
        Operacoes.InsertOrUpdate(usuarioModel);
        Query qry = Operacoes.Select(Manager.childUsuarios,new Filtro("Login",usuarioModel.getLogin()));
        qry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario usuarioModel = Usuario.parseOf(dataSnapshot.child(txtUsuarioLogin.getText().toString()).getValue());
                if (usuarioModel != null) {
                    pd.dismiss();
                    finish();
                    Runnable task = new Runnable() {
                        @Override
                        public void run() {
                            Manager.getInstance().showMessageDialog(
                                    "Salvo com sucesso!",
                                    "Faça login para começar!"
                            );
                        }
                    };

                    Manager.getInstance().setTaskAfterShow(task);
                } else {
                    pd.dismiss();
                    Manager.getInstance().showMessageDialog(
                            "Erro ao salvar",
                            "Ocorreu um problema ao salvar seu cadastro, tente novamente mais tarde!"
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
                Manager.getInstance().showMessageDialog(
                        "Erro ao salvar",
                        "Ocorreu um problema ao salvar seu cadastro, tente novamente mais tarde!"
                );
            }
        });

    }
}
