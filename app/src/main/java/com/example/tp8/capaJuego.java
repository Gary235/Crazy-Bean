package com.example.tp8;

import android.util.Log;
import android.view.MotionEvent;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tp8.R.raw;

import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class capaJuego extends Layer {

    CCSize _Pantalla;
    Sprite _Jugador,_Enemigo;
    CCPoint _Click = new CCPoint();
    Label lblTimer, lblContadorDeMonedas;
    CCColor3B blanco;
    public static int acumTimer = 0, contMoneda = -1;
    Sprite moneda = Sprite.sprite("moneda.png");

    ArrayList<Sprite> arrEnemigos = new ArrayList<>();
    boolean comenzo = false, tocoMoneda = false, tocoEnemigo = false;

    public capaJuego(CCSize _Pantalla) {
         acumTimer = 0; contMoneda = -1;
         comenzo = false; tocoMoneda = false; tocoEnemigo = false;

        this._Pantalla = _Pantalla;
        setIsTouchEnabled(true);
        blanco = new CCColor3B(255,255,255);

        PonerJugador();
        PonerTimer();
        PonerContadordeMonedas();
        super.schedule("listenerJugador", 0.01f);
    }


    public void listenerJugador(float nada){

        if(comenzo){
            comenzarJuego();
            super.schedule("PonerEnemigos", 3);
            super.schedule("listenerMonedas", 0.1f);
            super.schedule("listenerEnemigos", 0.01f);

            //schedule listener enemigos
            unschedule("listenerJugador");
        }
    }
    void PonerContadordeMonedas(){
        Sprite moneda = Sprite.sprite("moneda.png");
        moneda.setPosition(50, _Pantalla.getHeight() - 70);
        Log.d("JuegoPos", "MonedaCont : posX: " + moneda.getPositionX() + "   posY: " + moneda.getPositionY());




        lblContadorDeMonedas  = Label.label("0", "montserrat_semibold.ttf", 50);
        lblContadorDeMonedas.setPosition(moneda.getPositionX() + 50,_Pantalla.getHeight() - 70);
        Log.d("JuegoPos", "LBLcont : posX: " + lblContadorDeMonedas.getPositionX() + "   posY: " + lblContadorDeMonedas.getPositionY());
        lblContadorDeMonedas.setColor(blanco);

        super.addChild(moneda);
        super.addChild(lblContadorDeMonedas);
    }
    public void listenerMonedas(float inutil){


        if(tocoMoneda || contMoneda == -1){
            super.removeChild(moneda, true);
            Random generadorDeAzar = new Random();
            CCPoint posicionImagen = new CCPoint();

            posicionImagen.x = generadorDeAzar.nextInt((int) (_Pantalla.width - moneda.getWidth()));
            posicionImagen.x += moneda.getWidth() / 2;
            posicionImagen.y = generadorDeAzar.nextInt((int) (_Pantalla.height - 70));
            posicionImagen.y += moneda.getHeight() / 2;
            moneda.setPosition(posicionImagen.x, posicionImagen.y);
            super.addChild(moneda);
            contMoneda ++;
            lblContadorDeMonedas.setString(String.valueOf(contMoneda));
            tocoMoneda = false;
        }
    }
    boolean verificarTocoMoneda(){
        float img1Derecha, img1Izquierda, img1Arriba, img1Abajo;
        float img2Derecha, img2Izquierda, img2Arriba, img2Abajo;

        img1Arriba = _Jugador.getPositionY() + _Jugador.getHeight()/2;
        img1Abajo = _Jugador.getPositionY() - _Jugador.getHeight()/2;
        img1Derecha = _Jugador.getPositionX() + _Jugador.getWidth()/2;
        img1Izquierda = _Jugador.getPositionX() - _Jugador.getWidth()/2;

        img2Arriba = moneda.getPositionY() + moneda.getHeight()/2;
        img2Abajo = moneda.getPositionY() - moneda.getHeight()/2;
        img2Derecha = moneda.getPositionX() + moneda.getWidth()/2;
        img2Izquierda = moneda.getPositionX() - moneda.getWidth()/2;


        if (img1Arriba>=img2Abajo && img1Arriba<=img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
            tocoMoneda = true;
            Log.d("IntEntSprites", "Intersección caso 1");
        }

        if (img1Arriba >= img2Abajo && img1Arriba <= img2Arriba && img1Izquierda >= img2Izquierda && img1Izquierda <= img2Derecha) {
            tocoMoneda = true;
            Log.d("IntEntSprites", "Intersección caso 2");
        }

        if (img1Abajo >= img2Abajo && img1Abajo <= img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
            tocoMoneda = true;
            Log.d("IntEntSprites", "Intersección caso 3");
        }

        if (img1Abajo >= img2Abajo && img1Abajo <= img2Arriba && img1Izquierda >= img2Izquierda && img1Izquierda <= img2Derecha) {
            tocoMoneda = true;
            Log.d("IntEntSprites", "Intersección caso 4");
        }

        if (img2Arriba >= img1Abajo && img2Arriba <= img1Arriba && img2Derecha >= img1Izquierda && img2Derecha <= img1Derecha) {
            tocoMoneda = true;
            Log.d("IntEntSprites", "Intersección caso 5");
        }

        if (img2Arriba >= img1Abajo && img2Arriba <= img1Arriba && img2Izquierda >= img1Izquierda && img2Izquierda <= img1Derecha) {
            tocoMoneda = true;
            Log.d("IntEntSprites", "Intersección caso 6");
        }

        if (img2Abajo >= img1Abajo && img2Abajo <= img1Arriba && img2Derecha >= img1Izquierda && img2Derecha <= img1Derecha) {
            tocoMoneda = true;
            Log.d("IntEntSprites", "Intersección caso 7");
        }

        if (img2Abajo >= img1Abajo && img2Abajo <= img1Arriba && img2Izquierda >= img1Izquierda && img2Izquierda <= img1Derecha) {
            tocoMoneda = true;
            Log.d("IntEntSprites", "Intersección caso 8");
        }


        return tocoMoneda;
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
    public void PonerEnemigos(float inutil){
        Sprite enemigo = Sprite.sprite("enemigo.png");

        Random generadorDeAzar = new Random();
        CCPoint posicionImagen = new CCPoint();

        posicionImagen.x = generadorDeAzar.nextInt((int) (_Pantalla.width - enemigo.getWidth()));
        posicionImagen.x += enemigo.getWidth() / 2;
        posicionImagen.y = generadorDeAzar.nextInt((int) (_Pantalla.height - 70));
        enemigo.setPosition(posicionImagen.x, posicionImagen.y);

        arrEnemigos.add(enemigo);
        super.addChild(enemigo);
    }
    boolean verificarTocoEnemigo(){
        float img1Derecha, img1Izquierda, img1Arriba, img1Abajo;
        float img2Derecha, img2Izquierda, img2Arriba, img2Abajo;
        int i = 0;
        while (!tocoEnemigo && i < arrEnemigos.size()){
            img1Arriba = _Jugador.getPositionY() + _Jugador.getHeight()/2;
            img1Abajo = _Jugador.getPositionY() - _Jugador.getHeight()/2;
            img1Derecha = _Jugador.getPositionX() + _Jugador.getWidth()/2;
            img1Izquierda = _Jugador.getPositionX() - _Jugador.getWidth()/2;

            img2Arriba = arrEnemigos.get(i).getPositionY() + arrEnemigos.get(i).getHeight()/2;
            img2Abajo = arrEnemigos.get(i).getPositionY() - arrEnemigos.get(i).getHeight()/2;
            img2Derecha = arrEnemigos.get(i).getPositionX() + arrEnemigos.get(i).getWidth()/2;
            img2Izquierda = arrEnemigos.get(i).getPositionX() - arrEnemigos.get(i).getWidth()/2;


            if (img1Arriba>=img2Abajo && img1Arriba<=img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
                tocoEnemigo = true;
                Log.d("IntEntSprites", "Intersección caso 1");
            }

            if (img1Arriba >= img2Abajo && img1Arriba <= img2Arriba && img1Izquierda >= img2Izquierda && img1Izquierda <= img2Derecha) {
                tocoEnemigo = true;
                Log.d("IntEntSprites", "Intersección caso 2");
            }

            if (img1Abajo >= img2Abajo && img1Abajo <= img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
                tocoEnemigo = true;
                Log.d("IntEntSprites", "Intersección caso 3");
            }

            if (img1Abajo >= img2Abajo && img1Abajo <= img2Arriba && img1Izquierda >= img2Izquierda && img1Izquierda <= img2Derecha) {
                tocoEnemigo = true;
                Log.d("IntEntSprites", "Intersección caso 4");
            }

            if (img2Arriba >= img1Abajo && img2Arriba <= img1Arriba && img2Derecha >= img1Izquierda && img2Derecha <= img1Derecha) {
                tocoEnemigo = true;
                Log.d("IntEntSprites", "Intersección caso 5");
            }

            if (img2Arriba >= img1Abajo && img2Arriba <= img1Arriba && img2Izquierda >= img1Izquierda && img2Izquierda <= img1Derecha) {
                tocoEnemigo = true;
                Log.d("IntEntSprites", "Intersección caso 6");
            }

            if (img2Abajo >= img1Abajo && img2Abajo <= img1Arriba && img2Derecha >= img1Izquierda && img2Derecha <= img1Derecha) {
                tocoEnemigo = true;
                Log.d("IntEntSprites", "Intersección caso 7");
            }

            if (img2Abajo >= img1Abajo && img2Abajo <= img1Arriba && img2Izquierda >= img1Izquierda && img2Izquierda <= img1Derecha) {
                tocoEnemigo = true;
                Log.d("IntEntSprites", "Intersección caso 8");
            }

            i++;
        }


        return tocoEnemigo;
    }
    public void listenerEnemigos(float inutil){


        if(tocoEnemigo){
            Log.d("JuegoPerder", "PERDISTE MALOOOO");
            tocoEnemigo = false;
            GameOver();
        }
    }


    void PonerTimer(){
        lblTimer  = Label.label("0s", "montserrat_semibold.ttf", 50);
        lblTimer.setPosition(_Pantalla.getWidth() - (lblTimer.getWidth()/2 + 40),_Pantalla.getHeight() - 70);
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

        verificarTocoMoneda();
        verificarTocoEnemigo();

        _Click.x = x;
        _Click.y = y;

        return true;
    }



    void GameOver(){
        unschedule("PonerEnemigos");
        unschedule("listenerMonedas");
        unschedule("listenerEnemigos");

        Juego.escenaGameOver();

    }
}
