package com.example.tp8;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCSize;

import java.io.IOException;

public class Juego {

    static Context miContexto;
    CCGLSurfaceView _Vista;
    static CCSize _Pantalla;
    static MediaPlayer MusicadeFondo;

    public Juego(CCGLSurfaceView _Vista, Context context) {
        this._Vista = _Vista;
        this.miContexto = context;
    }


    public void ComenzarJuego() {
        Log.d("Juego", "Empezo");
        Director.sharedDirector().attachInView(_Vista);
        _Pantalla = Director.sharedDirector().displaySize();
        Juego.MusicadeFondo = new MediaPlayer();

        escenaComienzo();

    }
    public void escenaComienzo() {
        Scene escena = Scene.node();
        capaComienzo capaComienzo = new capaComienzo(_Pantalla);

        escena.addChild(capaComienzo);
        Director.sharedDirector().runWithScene(escena);

    }


    public static void escenaJuego() {
        Scene escena = Scene.node();
        capaJuego capaJuego = new capaJuego(_Pantalla, miContexto);
        escena.addChild(capaJuego);
        Director.sharedDirector().replaceScene(escena);
    }

    public static void escenaGameOver() {
        Scene escena = Scene.node();
        capaGameOver capaGameOver = new capaGameOver(_Pantalla);
        escena.addChild(capaGameOver);
        Director.sharedDirector().replaceScene(escena);
    }

}
