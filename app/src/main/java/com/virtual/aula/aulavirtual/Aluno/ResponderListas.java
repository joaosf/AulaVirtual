package com.virtual.aula.aulavirtual.Aluno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.virtual.aula.aulavirtual.Manager;
import com.virtual.aula.aulavirtual.Modelos.Exercicio;
import com.virtual.aula.aulavirtual.Modelos.ListaExercicio;
import com.virtual.aula.aulavirtual.Modelos.RespostaExercicio;
import com.virtual.aula.aulavirtual.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import Controladores.Operacoes;

@EActivity(R.layout.activity_responder_listas)
public class ResponderListas extends AppCompatActivity {
    ListaExercicio listaExercicio;
    Exercicio exercicio;
    Integer indexExercicio;
    HashMap<String,String> respostas = new HashMap<>();

    @ViewById
    TextView txtResponderTituloLista;
    @ViewById
    TextView txtResponderMateriaLista;
    @ViewById
    TextView txtResponderDificuldadeLista;

    @ViewById
    ProgressBar pbResponderAndamento;
    @ViewById
    TextView txtResponderAndamento;

    @ViewById
    TextView txtResponderTituloExercicio;
    @ViewById
    TextView txtResponderDescricaoExercicio;
    @ViewById
    ListView lstResponderAlternativas;

    @AfterViews
    void init() {
        listaExercicio = (ListaExercicio) Manager.getInstance().getTmpObject();
        txtResponderTituloLista.setText(listaExercicio.getTitulo());
        txtResponderMateriaLista.setText("Matéria: "+listaExercicio.getCategoria());
        txtResponderDificuldadeLista.setText("Grau de Ensino: "+Manager.getInstance().getDificuldadeDescricao(listaExercicio.getDificuldade()));
        pbResponderAndamento.setMax(listaExercicio.getExercicios().size());
        btnResponderAvancar();
    }

    @Override
    protected void onStart() {
        Manager.getInstance().setContext(this);
        super.onStart();
    }

    @Click
    void btnResponderAvancar() {
        if (indexExercicio == null || indexExercicio < listaExercicio.getExercicios().size()-1) {
            indexExercicio = indexExercicio == null ? 0 : indexExercicio+1;
            sincronizarExercicioTela();
        } else {
            RespostaExercicio respostaExercicio = new RespostaExercicio();
            respostaExercicio.setKeyListaExercicio(Manager.getInstance().getTmpString());
            respostaExercicio.setListaExercicio(listaExercicio);
            respostaExercicio.setRespostas(respostas);
            respostaExercicio.setUsuarioResposta(Manager.getInstance().getUsuarioLogado());
            respostaExercicio.setUsuarioProfessor(listaExercicio.getUsuario());
            Manager.getInstance().setTmpObject(respostaExercicio);
            Manager.getInstance().abrirNovaTela(ResultadoRespostas_.class,true);
        }
    }

    @Click
    void btnResponderVoltar() {
        if (indexExercicio == null || indexExercicio > 0) {
            indexExercicio = indexExercicio == null ? 0 : indexExercicio-1;
            sincronizarExercicioTela();
        }
    }

    private void sincronizarExercicioTela() {
        exercicio = listaExercicio.getExercicios().get(indexExercicio);
        Integer atual = indexExercicio+1;
        Integer total = listaExercicio.getExercicios().size();
        txtResponderTituloExercicio.setText(exercicio.getTitulo());
        txtResponderDescricaoExercicio.setText(exercicio.getDescricao());
        txtResponderAndamento.setText("Exercício "+atual+" de "+total);
        pbResponderAndamento.setProgress(atual);

        Object[] obj = exercicio.getAlternativas().keySet().toArray();
        String[] stringArray = Arrays.copyOf(obj, obj.length, String[].class);

        lstResponderAlternativas.setAdapter(Manager.getInstance().getArrayAdapter(stringArray));
        lstResponderAlternativas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = ((AppCompatTextView) view).getText().toString();
                respostas.put(indexExercicio+" ",value);
                btnResponderAvancar();
            }
        });
    }
}
