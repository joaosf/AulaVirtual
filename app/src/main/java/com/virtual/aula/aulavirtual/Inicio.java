package com.virtual.aula.aulavirtual;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

//Esta classe tem como objetivo unir os procedimentos de outras interfaces.
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_inicio)
public class Inicio extends AppCompatActivity {

    @ViewById
    TextView txtHome;
    
    @AfterViews
    protected void init() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "GreatVibesRegular.otf");
        txtHome.setTypeface(typeface);
    }

    @Override
    protected void onStart() {
        Manager.getInstance().setContext(this);
        Manager.getInstance().startObservable();
        super.onStart();
    }

    @Click
    void btnAluno() {
        Manager.getInstance().setVersaoApp(Manager.VersaoApp.Aluno.valorVersao);
        Manager.getInstance().abrirNovaTela(Login_.class,false);
    }

    @Click
    void btnProfessor() {
        Manager.getInstance().setVersaoApp(Manager.VersaoApp.Professor.valorVersao);
        Manager.getInstance().abrirNovaTela(Login_.class,false);
    }
}
