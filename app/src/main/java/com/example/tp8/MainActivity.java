package com.example.tp8;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends AppCompatActivity {

    CCGLSurfaceView vista;


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
        miJuego = new Juego(vista);
        miJuego.ComenzarJuego();
    }

}