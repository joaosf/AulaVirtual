package com.virtual.aula.aulavirtual;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.virtual.aula.aulavirtual.Aluno.ResponderListas_;
import com.virtual.aula.aulavirtual.Modelos.Exercicio;
import com.virtual.aula.aulavirtual.Modelos.ListaExercicio;
import com.virtual.aula.aulavirtual.Professor.CadastroLista_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Controladores.Operacoes;
import Modelos.Filtro;

@EActivity(R.layout.activity_consultar_exercicio)
public class ConsultarExercicio extends AppCompatActivity {

    private ArrayList<ListaExercicio> arrayListaExercicio;
    private ArrayList<String> arrayListKeys;
    private boolean bExercicios = false;
    private String tmp = "";

    @ViewById
    ListView lstConsultaExercicio;
    @ViewById
    TextView txtExercicioEscolha;

    @AfterViews
    protected void init() {
        arrayListaExercicio = new ArrayList<>();
        arrayListKeys = new ArrayList<>();
        popularListViewCategorias();
        tmp = Manager.getInstance().getTmpString();
    }

    @Override
    public void onBackPressed() {
        if (bExercicios) {
            popularListViewCategorias();
        } else {
            super.onBackPressed();
        }
    }

    private void popularListViewCategorias() {
        bExercicios = false;
        Object[] objCategorias = Manager.getInstance().getCategorias().getItens().keySet().toArray();
        String[] stringArray = Arrays.copyOf(objCategorias, objCategorias.length, String[].class);
        txtExercicioEscolha.setText("Escolha uma matéria:");
        lstConsultaExercicio.setAdapter(Manager.getInstance().getArrayAdapter(stringArray));
        lstConsultaExercicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Convertendo AppCompatTextView em String
                String cat = ((AppCompatTextView) view).getText().toString();
                popularListViewExercicios(cat);
            }
        });
    }

    private void atualizarListViewExercicios(String categoria) {
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for (ListaExercicio lista:arrayListaExercicio) {
            arrayList.add(
                    Manager.getInstance().getMapForAdapter(
                            lista.getTitulo(),
                            "Grau de Ensino: "+Manager.getInstance().getDificuldadeDescricao(lista.getDificuldade())
                    )
            );
        }

        lstConsultaExercicio.setAdapter(Manager.getInstance().getSimpleAdapter(arrayList));
        txtExercicioEscolha.setText("Escolha um exercício da matéria \""+categoria+"\":");
    }

    private void popularListViewExercicios(final String categoria) {
        bExercicios = true;
        arrayListaExercicio.clear();
        arrayListKeys.clear();
        atualizarListViewExercicios(categoria);
        String categoriaUsuario = tmp.equals("") ? "" : categoria+"_"+tmp;
        String key = tmp.equals("") ? Manager.childLista : Manager.keyChildLista;

        Query qry = Operacoes.Select(Manager.childLista,new Filtro(key,categoriaUsuario));
        qry.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ListaExercicio listaExercicio = dataSnapshot.getValue(ListaExercicio.class);
                if (listaExercicio != null &&
                        categoria.equals(listaExercicio.getCategoria())
                   ) {
                    arrayListaExercicio.add(listaExercicio);
                    arrayListKeys.add(dataSnapshot.getKey());
                }
                atualizarListViewExercicios(categoria);
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
        lstConsultaExercicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (Manager.getInstance().getVersaoApp().equals(Manager.VersaoApp.Professor.toString())) {
                    Manager.getInstance().setTmpObject(arrayListaExercicio.get(i));
                    Manager.getInstance().setTmpString(arrayListKeys.get(i));
                    Manager.getInstance().abrirNovaTela(CadastroLista_.class,false);
                } else {
                    final ListaExercicio listaExercicio = arrayListaExercicio.get(i);
                    final String key = arrayListKeys.get(i);
                    Manager.getInstance().showConfirmDialog(listaExercicio.getTitulo(),
                            "Deseja responder os exercícios do Professor " + listaExercicio.getUsuario(),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Manager.getInstance().setTmpObject(listaExercicio);
                                    Manager.getInstance().setTmpString(key);
                                    Manager.getInstance().abrirNovaTela(ResponderListas_.class,true);
                                }
                            },
                            null
                    );
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Manager.getInstance().setContext(this);
        popularListViewCategorias();
    }
}
