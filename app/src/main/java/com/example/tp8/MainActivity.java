package com.example.tp8;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends AppCompatActivity {

    CCGLSurfaceView vista;
    int cantMonMax=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        vista = new CCGLSurfaceView(this);
        vista.setBackgroundColor(Color.argb(1,255,255,255));
        setContentView(vista);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Juego miJuego;
        miJuego = new Juego(vista, this);
        miJuego.ComenzarJuego();
    }


    public void actualizarAcum(int x)
    {
        cantMonMax=x;
    }


}