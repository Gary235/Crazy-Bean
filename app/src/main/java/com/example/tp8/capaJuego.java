package com.example.tp8;

import android.util.Log;
import android.view.MotionEvent;

import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

public class capaJuego extends Layer {

    CCSize _Pantalla;
    Sprite _Jugador;
    CCPoint _Click = new CCPoint();
    Label lblTimer, lblContadorDeMonedas;
    CCColor3B blanco;
    int acum = 0;

    public capaJuego(CCSize _Pantalla) {
        this._Pantalla = _Pantalla;
        setIsTouchEnabled(true);
        blanco = new CCColor3B(255,255,255);


        setearPantalla();
        super.schedule("actualizarTimer", 1);

    }

    void PonerContadordeMonedas(){
        Sprite moneda = Sprite.sprite("moneda.png");
        CCPoint posicionBoton = new CCPoint();
        posicionBoton.x = 100;
        posicionBoton.y = _Pantalla.getHeight() - (moneda.getHeight()/2 + 20);
        _Jugador.setPosition(posicionBoton.x, posicionBoton.y);
        Log.d("Juego", "posX: " + posicionBoton.x + "   posY: " + posicionBoton.y);




        lblContadorDeMonedas  = Label.label("0", "montserrat_semibold.ttf", 50);
        lblContadorDeMonedas.setPosition(moneda.getPositionX() + 20,_Pantalla.getHeight() - (moneda.getHeight()/2 + 20));
        lblContadorDeMonedas.setColor(blanco);

        super.addChild(moneda);
        super.addChild(lblContadorDeMonedas);
    }
    void PonerJugador(){
        _Jugador = Sprite.sprite("jugador.png");

        CCPoint posicionBoton = new CCPoint();
        posicionBoton.x = _Pantalla.getWidth()/2;
        posicionBoton.y = 200;

        _Jugador.setPosition(posicionBoton.x, posicionBoton.y);
        Log.d("Juego", "posX: " + posicionBoton.x + "   posY: " + posicionBoton.y);

        super.addChild(_Jugador);

    }

    void PonerTimer(){
        lblTimer  = Label.label("0s", "montserrat_semibold.ttf", 50);
        lblTimer.setPosition(_Pantalla.getWidth() - (lblTimer.getWidth()/2 + 20),_Pantalla.getHeight() - lblTimer.getHeight()/2);
        lblTimer.setColor(blanco);

        super.addChild(lblTimer);

    }
    public void actualizarTimer(float inutilidad){
        lblTimer.setString(acum + "s");
        acum ++ ;
    }

    void setearPantalla(){
        PonerJugador();
        PonerTimer();
        PonerContadordeMonedas();
    }


    void MoverJugador(float x, float y){

        _Jugador.setPosition(x, y);

    }
    int DetectarClick(){

        float img1Derecha, img1Izquierda, img1Arriba, img1Abajo;

        img1Arriba = _Jugador.getPositionY() + _Jugador.getHeight()/2;
        img1Abajo = _Jugador.getPositionY() - _Jugador.getHeight()/2;
        img1Derecha = _Jugador.getPositionX() + _Jugador.getWidth()/2;
        img1Izquierda = _Jugador.getPositionX() - _Jugador.getWidth()/2;


        if((_Click.x <= img1Derecha && _Click.x >= img1Izquierda) && (_Click.y <= img1Arriba && _Click.y >= img1Abajo))
            return 1;

        return  0;
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        float x,y;
        x = event.getX();
        y = _Pantalla.getHeight() - event.getY();
        Log.d("Juego", "X -- " + x + " -- Y -- " + y);
        _Click.x = x;
        _Click.y = y;

        return true;
    }

    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        float x,y;
        x = event.getX();
        y = _Pantalla.getHeight() - event.getY();
        Log.d("Juego", "X -- " + x + " -- Y -- " + y);


        if(DetectarClick() == 1)
            MoverJugador(x,y);
        else if(DetectarClick() == 0){

        }
        _Click.x = x;
        _Click.y = y;

        return true;
    }

}
