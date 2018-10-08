package com.virtual.aula.aulavirtual;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.virtual.aula.aulavirtual.Aluno.ResultadoRespostas;
import com.virtual.aula.aulavirtual.Aluno.ResultadoRespostas_;
import com.virtual.aula.aulavirtual.Modelos.RespostaExercicio;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;

import Controladores.Operacoes;
import Modelos.Filtro;

@EActivity(R.layout.activity_listar_resultados)
public class ListarResultados extends AppCompatActivity {

    private ArrayList<RespostaExercicio> arrayListaResultados;
    private ArrayList<String> arrayListKeys;
    private String tmp = "";

    @ViewById
    ExpandableListView lstResultadosConsulta;

    @AfterViews
    void init() {
        arrayListaResultados = new ArrayList<>();
        arrayListKeys = new ArrayList<>();
        tmp = Manager.getInstance().getUsuarioLogado();
        popularListViewExercicios();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Manager.getInstance().setContext(this);
        if (Manager.getInstance().getVersaoApp().equals(Manager.VersaoApp.Professor.toString())) {
            setTitle("Consultar Resultados");
        } else {
            setTitle("Consultar Meus Resultados");
        }
    }

    private void atualizarListViewExercicios() {
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String,String>>> arrayListDetalhes = new ArrayList<>();
        for (RespostaExercicio lista:arrayListaResultados) {
            String descricao = "Professor: "+lista.getListaExercicio().getUsuario();
            StringBuilder detalhes = new StringBuilder();
            detalhes.append("Acertou "+lista.getnAcertos()+" de "+lista.getListaExercicio().getExercicios().size()+"\r\n");
            detalhes.append(lista.getnPercentual()+"% da lista!"+"\r\n");
            String tituloDetalhes = "Desempenho: "+Manager.getInstance().getPontosDescricao(lista.getnPercentual());

            if (Manager.getInstance().getVersaoApp().equals(Manager.VersaoApp.Professor.toString())) {
                descricao = "Aluno: "+lista.getUsuarioResposta();
            }
            arrayList.add(
                    Manager.getInstance().getMapForAdapter(
                        lista.getListaExercicio().getTitulo(), descricao
                    )
            );

            arrayListDetalhes.add(
                    Manager.getInstance().getMapOfMapForAdapter(
                            tituloDetalhes,detalhes.toString()
                    )
            );
        }

        lstResultadosConsulta.setAdapter(Manager.getInstance().getSimpleExpandableListAdapter(arrayList,arrayListDetalhes));
    }

    private void popularListViewExercicios() {
        arrayListaResultados.clear();
        arrayListKeys.clear();
        atualizarListViewExercicios();

        Query qry = Operacoes.Select(Manager.childResultados,getFiltro());
        qry.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RespostaExercicio respostaExercicio = dataSnapshot.getValue(RespostaExercicio.class);
                if (respostaExercicio != null) {
                    arrayListaResultados.add(respostaExercicio);
                    arrayListKeys.add(dataSnapshot.getKey());
                }
                atualizarListViewExercicios();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lstResultadosConsulta.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Manager.getInstance().setTmpObject(arrayListaResultados.get(i));
                Manager.getInstance().setTmpBoolean(false);
                Manager.getInstance().abrirNovaTela(ResultadoRespostas_.class,false);
                return false;
            }
        });
    }

    private Filtro getFiltro() {
        Filtro filtro = new Filtro("","");
        filtro.setName("usuarioResposta");
        filtro.setValue(tmp);
        if (Manager.getInstance().getVersaoApp().equals(Manager.VersaoApp.Professor.toString())) {
            filtro.setName("usuarioProfessor");
            filtro.setValue(tmp);
        }
        return filtro;
    }
}
