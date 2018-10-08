package com.virtual.aula.aulavirtual.Aluno;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.virtual.aula.aulavirtual.Manager;
import com.virtual.aula.aulavirtual.Modelos.Exercicio;
import com.virtual.aula.aulavirtual.Modelos.RespostaExercicio;
import com.virtual.aula.aulavirtual.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import Controladores.Operacoes;

@EActivity(R.layout.activity_resultado_respostas)
public class ResultadoRespostas extends AppCompatActivity {
    RespostaExercicio respostaExercicioModel;

    @ViewById
    TextView txtAcertos;
    @ViewById
    TextView txtAcertosPercentual;
    @ViewById
    ListView lstResultado;
    @ViewById
    TextView txtResultado;

    @AfterViews
    void init() {
        respostaExercicioModel = (RespostaExercicio) Manager.getInstance().getTmpObject();
        calcularNumeroAcertos();
        calcularNumeroPrecentual();

        if (Manager.getInstance().getTmpBoolean()) {
            Operacoes.InsertOrUpdate(Manager.childResultados,respostaExercicioModel,true);
        }

        txtAcertos.setText(getAcertos());
        txtAcertosPercentual.setText(getAcertosPercentual());
        txtResultado.setText(Manager.getInstance().getPontosDescricao(respostaExercicioModel.getnPercentual()));
        processarLista();
    }

    private void calcularNumeroAcertos() {
        int acertos = 0;
        for (int i = 0; i < respostaExercicioModel.getListaExercicio().getExercicios().size();i++) {
            String forI = i+" ";
            Exercicio exercicio = respostaExercicioModel.getListaExercicio().getExercicios().get(i);
            String respostaAluno = respostaExercicioModel.getRespostas().get(forI) == null ? "" : respostaExercicioModel.getRespostas().get(forI);

            Object[] obj = exercicio.getAlternativas().values().toArray();
            Boolean[] boolArray = Arrays.copyOf(obj, obj.length, Boolean[].class);

            Object[] objString = exercicio.getAlternativas().keySet().toArray();
            String[] stringArray = Arrays.copyOf(objString, objString.length, String[].class);

            for (int x = 0; x < exercicio.getAlternativas().size(); x++) {
                if (boolArray[x] && stringArray[x].equals(respostaAluno)) {
                    acertos += 1;
                    break;
                }
            }
        }
        respostaExercicioModel.setnAcertos(acertos);
    }

    private void calcularNumeroPrecentual() {
        long percentualExato = (respostaExercicioModel.getnAcertos()*100)/respostaExercicioModel.getListaExercicio().getExercicios().size();
        Integer percentual = (int) percentualExato;
        respostaExercicioModel.setnPercentual(percentual);
    }

    @Override
    protected void onStart() {
        Manager.getInstance().setContext(this);
        super.onStart();
    }

    private void processarLista() {
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for (int i = 0; i < respostaExercicioModel.getListaExercicio().getExercicios().size();i++) {
            String forI = i+" ";
            Exercicio exercicio = respostaExercicioModel.getListaExercicio().getExercicios().get(i);
            String respostaAluno = respostaExercicioModel.getRespostas().get(forI) == null ? "" : respostaExercicioModel.getRespostas().get(forI);
            String respostaCertaArray = "";

            Object[] obj = exercicio.getAlternativas().values().toArray();
            Boolean[] boolArray = Arrays.copyOf(obj, obj.length, Boolean[].class);

            Object[] objString = exercicio.getAlternativas().keySet().toArray();
            String[] stringArray = Arrays.copyOf(objString, objString.length, String[].class);

            for (int x = 0; x < exercicio.getAlternativas().size(); x++) {
                if (boolArray[x]) {
                    respostaCertaArray += "\""+stringArray[x]+"\";";
                }
            }

            String suaResposta = "Sua Resposta: "+respostaAluno;
            String respostaCorreta = "Resposta(s) Correta(s): "+respostaCertaArray;
            arrayList.add(
                    Manager.getInstance().getMapForAdapter(
                            exercicio.getTitulo()+"\r\n"+respostaCorreta,
                            suaResposta
                    )
            );
        }

        lstResultado.setAdapter(Manager.getInstance().getSimpleAdapter(arrayList));
    }

    private String getAcertos() {
        String retorno = "Acertou "+respostaExercicioModel.getnAcertos()+" de "+respostaExercicioModel.getListaExercicio().getExercicios().size() +" exercícios!";
        return retorno;
    }

    private String getAcertosPercentual() {
        String retorno = respostaExercicioModel.getnPercentual()+"% da lista!";
        return retorno;
    }
}
