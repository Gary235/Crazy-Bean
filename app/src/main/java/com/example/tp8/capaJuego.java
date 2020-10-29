package com.example.tp8;

import android.util.Log;
import android.view.MotionEvent;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tp8.R.raw;

import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

import java.io.IOException;
import java.util.ArrayList;

public class capaJuego extends Layer {

    CCSize _Pantalla;
    Sprite _Jugador,_Enemigo;
    CCPoint _Click = new CCPoint();
    Label lblTimer, lblContadorDeMonedas;
    CCColor3B blanco;
    int acumTimer = 0;

    boolean comenzo = false;

    public capaJuego(CCSize _Pantalla) {
        this._Pantalla = _Pantalla;
        setIsTouchEnabled(true);
        blanco = new CCColor3B(255,255,255);


        PonerJugador();
        PonerTimer();
        PonerContadordeMonedas();
        PonerEnemigo();
        super.schedule("listenerJugador", 0.01f);


    }


    public void listenerJugador(float nada){

        if(comenzo){
            comenzarJuego();
            unschedule("listenerJugador");
        }
    }
    void PonerContadordeMonedas(){
        Sprite moneda = Sprite.sprite("moneda.png");
        moneda.setPosition(50, _Pantalla.getHeight() - (moneda.getHeight()/2 + 20));
        Log.d("JuegoPos", "MonedaCont : posX: " + moneda.getPositionX() + "   posY: " + moneda.getPositionY());




        lblContadorDeMonedas  = Label.label("0", "montserrat_semibold.ttf", 50);
        lblContadorDeMonedas.setPosition(moneda.getPositionX() + 50,_Pantalla.getHeight() - (moneda.getHeight()/2 + 20));
        Log.d("JuegoPos", "LBLcont : posX: " + lblContadorDeMonedas.getPositionX() + "   posY: " + lblContadorDeMonedas.getPositionY());
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
        Log.d("JuegoPos", "Jugador: posX: " + posicionBoton.x + "   posY: " + posicionBoton.y);

        super.addChild(_Jugador);

    }
    void PonerEnemigo(){
        _Enemigo = Sprite.sprite("enemigo.json");

        _Enemigo.setPosition( _Pantalla.getWidth()/2, 600);
        Log.d("JuegoPos", "Enemigo: posX: " + _Enemigo.getPositionX() + "   posY: " + _Enemigo.getPositionY());

        super.addChild(_Enemigo);

    }

    void PonerTimer(){
        lblTimer  = Label.label("0s", "montserrat_semibold.ttf", 50);
        lblTimer.setPosition(_Pantalla.getWidth() - (lblTimer.getWidth()/2 + 40),_Pantalla.getHeight() - 20);
        Log.d("JuegoPos", "Timer: posX: " + lblTimer.getPositionX() + "   posY: " + lblTimer.getPositionY());
        lblTimer.setColor(blanco);

        super.addChild(lblTimer);

    }
    public void actualizarTimer(float inutilidad){
        lblTimer.setString(acumTimer + "s");
        acumTimer ++ ;
    }
    void comenzarJuego(){


        try { Juego.MusicadeFondo.prepare(); } catch (IOException e) { e.printStackTrace(); }
        Juego.MusicadeFondo.start();

        super.schedule("actualizarTimer", 1);


    }

    void MoverJugador(float x, float y){

        _Jugador.setPosition(x, y);

    }
    boolean DetectarClick(){

        float img1Derecha, img1Izquierda, img1Arriba, img1Abajo;

        img1Arriba = _Jugador.getPositionY() + _Jugador.getHeight()/2;
        img1Abajo = _Jugador.getPositionY() - _Jugador.getHeight()/2;
        img1Derecha = _Jugador.getPositionX() + _Jugador.getWidth()/2;
        img1Izquierda = _Jugador.getPositionX() - _Jugador.getWidth()/2;

        return (_Click.x <= img1Derecha && _Click.x >= img1Izquierda) && (_Click.y <= img1Arriba && _Click.y >= img1Abajo);
    }
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        float x,y;
        x = event.getX();
        y = _Pantalla.getHeight() - event.getY();
        Log.d("Juego", "X -- " + x + " -- Y -- " + y);
        _Click.x = x;
        _Click.y = y;

        if(DetectarClick()) {
            comenzo = true;
        }


        return true;
    }
    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        float x,y;
        x = event.getX();
        y = _Pantalla.getHeight() - event.getY();
        Log.d("Juego", "X -- " + x + " -- Y -- " + y);


        if(DetectarClick() ){
            comenzo = true;
            MoverJugador(x,y);

        }


        _Click.x = x;
        _Click.y = y;

        return true;
    }

}
