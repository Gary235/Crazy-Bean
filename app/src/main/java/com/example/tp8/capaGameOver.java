package com.example.tp8;

import android.util.Log;
import android.view.MotionEvent;

import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

public class capaGameOver extends Layer {

    CCSize _Pantalla;
    Label label;
    Sprite sprBoton;
    boolean apretado = false;
    int cont = 0;


    public capaGameOver(CCSize pantalla) {
        this._Pantalla = pantalla;
        setIsTouchEnabled(true);


        ponerTitulo();
        ponerBotonInicio();
        super.schedule("VerificarBoton", 0.01f);
    }

    void ponerTitulo(){
        CCColor3B color3B;
        color3B = new CCColor3B(255,255,255);

        label  = Label.label("Game Over", "montserrat_semibold.ttf", 150);
        label.setPosition(_Pantalla.getWidth()/2,_Pantalla.getHeight()/2);
        label.setColor(color3B);

        super.addChild(label);
    }

    void ponerBotonInicio(){
        sprBoton = Sprite.sprite("boton_gameover.png");

        CCPoint posicionBoton = new CCPoint();
        posicionBoton.x = _Pantalla.getWidth()/2;
        posicionBoton.y = _Pantalla.getHeight()/2 - label.getHeight();

        sprBoton.setPosition(posicionBoton.x, posicionBoton.y);
        Log.d("Juego", "posX: " + posicionBoton.x + "   posY: " + posicionBoton.y);

        super.addChild(sprBoton);

    }
    public void VerificarBoton(float floatInutil){
        if(apretado && cont == 0){
            //cambiar de escena
            Log.d("Juego", "Apretoooooo");
            Juego.escenaJuego();

            cont = 1;
        }
    }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {
            float x,y;
            x = event.getX();
            y = _Pantalla.getHeight() - event.getY();
            Log.d("Juego", "X -- " + x + " -- Y -- " + y);

            if(DetectarClick(x,y)) {
                apretado = true;
            }

            return true;
        }

        boolean DetectarClick(float x, float y){

            float img1Derecha, img1Izquierda, img1Arriba, img1Abajo;

            img1Arriba = sprBoton.getPositionY() + sprBoton.getHeight()/2;
            img1Abajo = sprBoton.getPositionY() - sprBoton.getHeight()/2;
            img1Derecha = sprBoton.getPositionX() + sprBoton.getWidth()/2;
            img1Izquierda = sprBoton.getPositionX() - sprBoton.getWidth()/2;


            return (x <= img1Derecha && x >= img1Izquierda) && (y <= img1Arriba && y >= img1Abajo);
        }

}
