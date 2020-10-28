package com.example.tp8;

import android.graphics.Color;
import android.util.Log;

import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCSize;

public class Juego {

    CCGLSurfaceView _Vista;
    static CCSize _Pantalla;

    public Juego(CCGLSurfaceView _Vista) {
        this._Vista = _Vista;
    }

    public void ComenzarJuego() {
        Log.d("Juego", "Empezo");
        Director.sharedDirector().attachInView(_Vista);
        _Pantalla = Director.sharedDirector().displaySize();
        _Vista.setBackgroundColor(Color.argb(1,255,255,255));

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
        capaJuego capaJuego = new capaJuego(_Pantalla);

        escena.addChild(capaJuego);
        Director.sharedDirector().replaceScene(escena);
    }




}
