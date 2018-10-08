package com.virtual.aula.aulavirtual.Professor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.virtual.aula.aulavirtual.Manager;
import com.virtual.aula.aulavirtual.Modelos.Categorias;
import com.virtual.aula.aulavirtual.Modelos.Exercicio;
import com.virtual.aula.aulavirtual.Modelos.ListaExercicio;
import com.virtual.aula.aulavirtual.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import Controladores.Operacoes;

@EActivity(R.layout.activity_cadastro_lista)
public class CadastroLista extends AppCompatActivity {
    private ListaExercicio listaExercicio;
    private String key = "";

    @ViewById
    EditText txtListaTitulo;
    @ViewById
    Spinner spnListaCategoria;
    @ViewById
    ListView lstListaExercicios;
    @ViewById
    RadioGroup rgGrau;


    @AfterViews
    protected void init() {
        listaExercicio = new ListaExercicio();

        Object[] objCategorias = Manager.getInstance().getCategorias().getItens().keySet().toArray();
        String[] stringArray = Arrays.copyOf(objCategorias, objCategorias.length, String[].class);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringArray);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnListaCategoria.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Manager.getInstance().setContext(this);
        Object object = Manager.getInstance().getTmpObject();
        if (object != null) {
            try {
                Exercicio exercicio = (Exercicio) object;
                listaExercicio.addExercicios(exercicio);
            } catch (Exception e) {
                listaExercicio = (ListaExercicio) object;
                txtListaTitulo.setText(listaExercicio.getTitulo());
                spnListaCategoria.setSelection(indexOfCategoria(listaExercicio.getCategoria()));
                key = Manager.getInstance().getTmpString();
            }
            atualizarListaExercicios();

        }
    }

    private int indexOfCategoria(String categoria) {
        Object[] objCategorias = Manager.getInstance().getCategorias().getItens().keySet().toArray();
        String[] stringArray = Arrays.copyOf(objCategorias, objCategorias.length, String[].class);
        for (int i = 0; i < stringArray.length;i++) {
            if (stringArray[i].equals(categoria)) {
                return i;
            }
        }
        return 0;
    }

    private void atualizarListaExercicios() {
        ArrayList<String> list = new ArrayList<>();
        for (Exercicio exercicio:listaExercicio.getExercicios()) {
            list.add(exercicio.getTitulo());
        }
        lstListaExercicios.setAdapter(Manager.getInstance().getArrayAdapter(list));
    }

    @Click
    void btnListaAddExercicio() {
        Manager.getInstance().abrirNovaTela(CadastroExercicio_.class,false);
    }

    private boolean validarDados() {
        String titulo = txtListaTitulo.getText().toString();
        String categoria = spnListaCategoria.getSelectedItem().toString();

        if (titulo.isEmpty()) {
            txtListaTitulo.setError("Informe o " + txtListaTitulo.getHint().toString());
            return false;
        } else if (categoria.isEmpty()) {
            Manager.getInstance().showMessageDialog("Matéria", "Você precisa selecionar a matéria da sua lista de exercícios!");
            return false;
        } else if (listaExercicio.getExercicios() == null || listaExercicio.getExercicios().isEmpty()) {
            Manager.getInstance().showMessageDialog("Exercícios", "Você precisa cadastrar no mínimo um exercício!");
            return false;
        } if (rgGrau.getCheckedRadioButtonId() == -1) {
            Manager.getInstance().showMessageDialog("Grau de Ensino", "Você precisa informar o grau de ensino!");
            return false;
        } else {
            return true;
        }
    }

    @Click
    void btnSalvarLista() {
        if (validarDados()) {
            listaExercicio.setCategoria(spnListaCategoria.getSelectedItem().toString());
            listaExercicio.setTitulo(txtListaTitulo.getText().toString());
            listaExercicio.setUsuario(Manager.getInstance().getUsuarioLogado());
            listaExercicio.setCategoriaUsuario(listaExercicio.getCategoria()+"_"+listaExercicio.getUsuario());
            listaExercicio.setDificuldade(rgGrau.indexOfChild(findViewById(rgGrau.getCheckedRadioButtonId()))+1);

            if (key.equals("")) {
                Operacoes.InsertOrUpdate(Manager.childLista,listaExercicio,true);
            } else {
                Operacoes.InsertOrUpdate(Manager.childLista+"/"+key,listaExercicio,false);
            }

            finish();
        }
    }
}
