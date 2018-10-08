package com.virtual.aula.aulavirtual;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.virtual.aula.aulavirtual.Aluno.MainAluno_;
import com.virtual.aula.aulavirtual.Modelos.Categorias;
import com.virtual.aula.aulavirtual.Professor.MainProfessor_;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Controladores.Operacoes;
import Modelos.Filtro;

public class Manager {

    //<editor-fold defaultstate="collapsed" desc="Basic Data Singleton">
    private static final Manager ourInstance = new Manager();

    public static Manager getInstance() {
        return ourInstance;
    }

    private Manager() {

    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Manipulação de Telas e Contextos">

    private static Context contexto;

    public void setContext(Context contexto) {
        this.contexto = contexto;
    }

    public void abrirNovaTela(Class Destino, Boolean fecharAtual) {
        Intent tela = new Intent(contexto,Destino);
        contexto.startActivity(tela);
        if (fecharAtual) {
            ((Activity)contexto).finish();
        }
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tabelas e Indices">

    public static String childUsuarios = "Usuarios";
    public static String childUsuariosLogados = "UsuariosLogados";
    public static String childCategorias = "Categorias";
    public static String childLista = "ListasExercicios";
    public static String childResultados = "ResultadosListas";
    public static String keyChildLista = "categoriaUsuario";

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Versao APP & Descrições">

    public String getPontosDescricao(int percentual) {
        String retorno = "";
        if (percentual > 95) {
            retorno = "Perfeito!";
        } else if (percentual > 90) {
            retorno = "Excelente!";
        } else if (percentual > 80) {
            retorno = "Muito Bom!";
        } else if (percentual > 60) {
            retorno = "Bom!";
        } else if (percentual > 50) {
            retorno = "Nada mal!";
        } else {
            retorno = "Você pode mais!";
        }
        return retorno;
    }

    private String Versao;
    public enum VersaoApp {
        Professor("Professor"),Aluno("Aluno");

        public String valorVersao;
        VersaoApp(String valor) {
            valorVersao = valor;
        }
    }

    public void setVersaoApp(String versao) {
        this.Versao = versao;
    }

    public String getVersaoApp() {
        return this.Versao;
    }

    public String getDificuldadeDescricao(int Index) {
        String retorno = "";
        switch (Index) {
            case 1: {
                retorno = "Básico";
                break;
            }
            case 2: {
                retorno = "Médio";
                break;
            }
            case 3: {
                retorno = "Superior";
                break;
            }
            case 4: {
                retorno = "Especialização";
                break;
            }
        }
        return retorno;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dialogs">

    private ProgressDialog pd;
    private Runnable taskAfterShow;

    public ProgressDialog showLoadingDialog(String Titulo) {
        pd = new ProgressDialog(contexto);
        pd.setMessage(Titulo);
        pd.setCancelable(false);
        pd.show();
        return pd;
    }

    public void showMessageDialog(String Titulo, String Mensagem) {
        AlertDialog.Builder alert = new AlertDialog.Builder(contexto)
                .setTitle(Titulo)
                .setCancelable(false)
                .setPositiveButton("ok",null);

        if (!Mensagem.equals("")) {
            alert.setMessage(Mensagem);
        }

        alert.show();
    }

    public void showConfirmDialog(String Titulo, String Mensagem,DialogInterface.OnClickListener NotifySim, DialogInterface.OnClickListener NotifyNao) {
        boolean retorno = false;
        AlertDialog.Builder alert = new AlertDialog.Builder(contexto)
                .setTitle(Titulo)
                .setCancelable(false)
                .setPositiveButton("Sim", NotifySim)
                .setNegativeButton("Não",NotifyNao);

        if (!Mensagem.equals("")) {
            alert.setMessage(Mensagem);
        }

        alert.show();
    }

    public void showInputDialog(String Titulo, String Mensagem, final DialogInterface.OnClickListener notifyConcluir, final DialogInterface.OnClickListener notifyCancelar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle(Titulo);

        final EditText input = new EditText(contexto);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                setTmpString(m_Text);
                Manager.getInstance().showConfirmDialog("Confirmação","Você digitou \""+m_Text+"\", está certo disto?",notifyConcluir,notifyCancelar);
            }
        });
        builder.setNegativeButton("Cancelar", notifyCancelar);

        builder.show();
        input.requestFocus();
    }

    public Runnable getTaskAfterShow() {
        return taskAfterShow;
    }

    public void setTaskAfterShow(Runnable taskAfterShow) {
        this.taskAfterShow = taskAfterShow;
    }

    public void verificarTaskAfterShow() {
        if (taskAfterShow != null) {
            taskAfterShow.run();
            taskAfterShow = null;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TmpObject & TmpString">

    private Object tmpObject;
    private String tmpString = "";
    private Boolean tmpBoolean = true;

    public Object getTmpObject() {
        Object retorno = tmpObject;
        tmpObject = null;
        return retorno;
    }

    public void setTmpObject(Object tmpObject) {
        this.tmpObject = tmpObject;
    }

    public String getTmpString() {
        String retorno = tmpString;
        tmpString = "";
        return retorno;

    }

    public void setTmpString(String tmpString) {
        this.tmpString = tmpString;
    }

    public Boolean getTmpBoolean() {
        Boolean retorno = tmpBoolean;
        tmpBoolean = true;
        return retorno;
    }

    public void setTmpBoolean(Boolean tmpBoolean) {
        this.tmpBoolean = tmpBoolean;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Observable Categorias">
    private Categorias categorias = new Categorias();

    public Categorias getCategorias() {
        return categorias;
    }

    public void addCategoria(String descricao, Boolean ativo) {
        this.categorias.addItem(descricao,ativo);
        syncCloud();
        showMessageDialog("Salvo com sucesso!","");
    }

    public void startObservable() {
        Query qry = Operacoes.Select(childCategorias,new Filtro(childCategorias,""));
        qry.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                categorias.addItem(dataSnapshot.getKey(),Boolean.parseBoolean(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                categorias.addItem(dataSnapshot.getKey(),Boolean.parseBoolean(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                categorias.removeItem(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void syncCloud() {
        Operacoes.InsertOrUpdate(Manager.childCategorias,categorias.getItens(),false);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Componentes">
    public LinearLayout newLinearLayout() {
        LinearLayout retorno = new LinearLayout(contexto);
        retorno.setOrientation(LinearLayout.HORIZONTAL);
        retorno.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return retorno;
    }

    public TextView newTextView(String text) {
        TextView retorno = new TextView(contexto);
        retorno.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        retorno.setText(text);
        return retorno;
    }

    public CheckBox newCheckBox(Boolean b) {
        CheckBox retorno = new CheckBox(contexto);
        retorno.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        retorno.setChecked(b);
        return retorno;
    }

    public ArrayAdapter<String> getArrayAdapter(ArrayList<String> list) {
        ArrayAdapter<String> listaAdapter = new ArrayAdapter<>(contexto,android.R.layout.simple_list_item_1, list);
        return listaAdapter;
    }

    public ArrayAdapter<String> getArrayAdapter(String[] list) {
        ArrayAdapter<String> listaAdapter = new ArrayAdapter<>(contexto,android.R.layout.simple_list_item_1, list);
        return listaAdapter;
    }

    public SimpleAdapter getSimpleAdapter(ArrayList<HashMap<String, String>> list){
        SimpleAdapter adapter = new SimpleAdapter(contexto, list,
                android.R.layout.simple_list_item_2,
                new String[] {"Titulo", "Descricao" },
                new int[] {android.R.id.text1, android.R.id.text2 });
        return adapter;
    }

    public SimpleExpandableListAdapter getSimpleExpandableListAdapter(
            ArrayList<HashMap<String, String>> list,
            ArrayList<ArrayList<HashMap<String, String>>> dados){

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                contexto,
                list,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {"Titulo", "Descricao" },
                new int[] {android.R.id.text1, android.R.id.text2 },
                dados,
                android.R.layout.simple_list_item_2,
                new String[] { "Titulo", "Descricao" },
                new int[] { android.R.id.text1, android.R.id.text2 });
        return adapter;
    }

    public ArrayList<HashMap<String,String>> getMapOfMapForAdapter(final String Titulo, final String Descricao) {
        ArrayList<HashMap<String, String>> listOfChildGroups = new ArrayList<>();

        HashMap<String, String> childGroupForFirstGroupRow = new HashMap<String,String>(){{
            put("Titulo", Titulo);
            put("Descricao", Descricao);
        }};
        listOfChildGroups.add(childGroupForFirstGroupRow);

        return listOfChildGroups;
    }
    public HashMap<String,String> getMapForAdapter(String Titulo, String Descricao) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Titulo",Titulo);
        hashMap.put("Descricao",Descricao);
        return hashMap;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Gerenciador de Usuário Logado">

    public String getPKAndroid() {
        String id = Settings.Secure.getString(contexto.getContentResolver(), Settings.Secure.ANDROID_ID);
        String retorno = android.provider.Settings.System.getString(contexto.getContentResolver(), android.provider.Settings.System.ANDROID_ID);
        return retorno;
    }

    private String UsuarioLogado = "";

    public String getUsuarioLogado() {
        return UsuarioLogado;
    }

    private void setUsuarioLogado(String usuarioLogado) {
        UsuarioLogado = usuarioLogado;
    }

    public void verificarManterConectado() {
        Query qry = Operacoes.Select(childUsuariosLogados,new Filtro(childUsuariosLogados,""));
        qry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object obj = (Object) dataSnapshot.getValue();
                HashMap<String, String> hashMap = (HashMap<String, String>) obj;
                String key = getPKAndroid()+"_"+Versao;
                if (hashMap != null && hashMap.get(key) != null) {
                    setUsuarioLogado(hashMap.get(key));
                    if (Versao.equals(VersaoApp.Aluno.toString())) {
                        abrirNovaTela(MainAluno_.class,true);
                    } else {
                        abrirNovaTela(MainProfessor_.class,true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void manterUsuarioConectado(boolean manter, String usuarioLogado) {
        if (manter) {
            String key = getPKAndroid()+"_"+Versao;
            Operacoes.InsertOrUpdate(childUsuariosLogados+"/"+key,usuarioLogado,false);
        }
        setUsuarioLogado(usuarioLogado);
    }

    public void desconectarUsuario() {
        String key = getPKAndroid()+"_"+Versao;
        Operacoes.InsertOrUpdate(childUsuariosLogados+"/"+key,null,false);
        setUsuarioLogado("");
        abrirNovaTela(Login_.class,true);
    }

    //</editor-fold>
}
