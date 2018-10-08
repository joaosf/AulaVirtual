package com.virtual.aula.aulavirtual.Professor;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.virtual.aula.aulavirtual.Manager;
import com.virtual.aula.aulavirtual.Modelos.Exercicio;
import com.virtual.aula.aulavirtual.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EActivity(R.layout.activity_cadastro_exercicio)
public class CadastroExercicio extends AppCompatActivity {

    private Exercicio exercicio;
    private String[] stringArray;
    private Boolean[] valuesArray;

    @ViewById
    EditText txtExercicioTitulo;
    @ViewById
    EditText txtExercicioDescricao;
    @ViewById
    LinearLayout lnlExercicioAlternativas;

    @AfterViews
    protected void init() {
        exercicio = new Exercicio();
    }

    @Override
    protected void onStart() {
        Manager.getInstance().setContext(this);
        super.onStart();
    }

    @Click
    void btnExercicioAlternativa() {
        Manager.getInstance().showInputDialog(
                "Cadastro de Alternativa",
                "Informe o texto da alternativa que deseja cadastrar:",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exercicio.addAlternativa(Manager.getInstance().getTmpString(),false);
                        atualizarListAlternativas();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );
    }

    private void atualizarListAlternativas() {
        lnlExercicioAlternativas.removeAllViews();
        Collection<String> nomes = exercicio.getAlternativas().keySet();
        List<String> list = new ArrayList(nomes);
        Collections.sort(list);
        for (final String option : list) {
            LinearLayout linearLayout = Manager.getInstance().newLinearLayout();
            CheckBox checkBox = Manager.getInstance().newCheckBox(exercicio.getAlternativas().get(option));
            TextView textView = Manager.getInstance().newTextView(option);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    exercicio.addAlternativa(option,b);
                }
            });
            linearLayout.addView(checkBox);
            linearLayout.addView(textView);

            lnlExercicioAlternativas.addView(linearLayout);
        }
    }


    @Click
    void btnSalvarExercicio() {
        if (validarDadosExercicio()) {
            exercicio.setTitulo(txtExercicioTitulo.getText().toString());
            exercicio.setDescricao(txtExercicioDescricao.getText().toString());
            finish();
            Manager.getInstance().setTmpObject(exercicio);
        }
    }

    private boolean validarDadosExercicio() {
        String titulo = txtExercicioTitulo.getText().toString();
        String descricao = txtExercicioDescricao.getText().toString();
        int countCorretas = 0;
        for (Boolean b:exercicio.getAlternativas().values()) {
            countCorretas += b ? 1 : 0;
        }

        if (titulo.isEmpty()) {
            txtExercicioTitulo.setError("Informe o "+txtExercicioTitulo.getHint().toString());
            return false;
        } else
        if (descricao.isEmpty()) {
            txtExercicioDescricao.setError("Informe a "+txtExercicioDescricao.getHint().toString());
            return false;
        } else
        if (exercicio.getAlternativas().isEmpty() || countCorretas == 0) {
            Manager.getInstance().showMessageDialog("Alternativas","Você precisa cadastrar no mínimo uma alternativa correta!");
            return false;
        } else {
            return true;
        }
    }

}
