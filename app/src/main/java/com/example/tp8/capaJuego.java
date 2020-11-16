package com.example.tp8;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tp8.R.raw;

import org.cocos2d.actions.interval.IntervalAction;
import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.actions.interval.Sequence;
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
    Sprite _Jugador;
    CCPoint _Click = new CCPoint();
    Label lblTimer, lblContadorDeMonedas;
    CCColor3B blanco;
    int cont=0;
    public static int acumTimer = 0, contMoneda = -1;
    Sprite moneda = Sprite.sprite("moneda.png");
    Boolean jefe = false;
    Sprite jefeSprite = Sprite.sprite("jefe.png");
    Context miContexto;

    ArrayList<Sprite> arrEnemigos = new ArrayList<>();
    boolean comenzo = false, tocoMoneda = false, tocoEnemigo = false;

    int arrayMusica[] = {
            raw.musica0,
            raw.musica1,
            raw.musica2,
            raw.musica3,
            raw.musica4,
            raw.musica5,
            raw.musica6
    };

    public capaJuego(CCSize _Pantalla,Context context) {
         acumTimer = 0; contMoneda = -1;
         comenzo = false; tocoMoneda = false; tocoEnemigo = false;

         this.miContexto = context;
         this._Pantalla = _Pantalla;
         setIsTouchEnabled(true);
         blanco = new CCColor3B(255,255,255);
         PonerJugador();
         PonerTimer();
         PonerContadordeMonedas();
         super.schedule("listenerJugador", 0.01f);
    }


    public void listenerJugador(float nada){
        if(comenzo) {
            comenzarJuego();
            jefe = true;
            jefeSprite.scale(3);
            CCPoint posicionImagen = new CCPoint();
            posicionImagen.x = this.getWidth() / 2;
            posicionImagen.y = this.getHeight() / 2;
            jefeSprite.setPosition(posicionImagen.x, posicionImagen.y);
            super.addChild(jefeSprite, 5);

            Sprite enemigo = Sprite.sprite("enemigo.png");
            CCPoint posicionImagen2 = new CCPoint();
            posicionImagen2.x = this.getWidth() / 2;
            posicionImagen2.y = this.getHeight() / 2 - jefeSprite.getHeight()*2;
            enemigo.setPosition(posicionImagen2.x, posicionImagen2.y);
            super.addChild(enemigo);


            super.schedule("misiles", 3);



            super.schedule("PonerEnemigos", 2);

            if(!jefe) {

                super.schedule("SacarEnemigos", 7);
                super.schedule("GenerarEnemigosGrandes", 15);
                super.schedule("Niveles", 60);
            }
            super.schedule("listenerMonedas", 0.1f);
            super.schedule("listenerEnemigos", 0.01f);
            unschedule("listenerJugador");
        }
    }


    public void Niveles(float inutil) {
        cont++;
        int A=0;
        while(A!=arrEnemigos.size()){
            removeChild(arrEnemigos.get(A),true);
            arrEnemigos.remove(A);
        }
        if(!jefe) {
            if (cont > 0) {
                //super.schedule("EnemigosVoladores", 8);
            }
            if (cont > 1) {
                //super.schedule("EnemigosVoladores2", 8);
            }
            if (cont > 2) {
                //super.schedule("EnemigosRayo", 5);
            }
            if (cont % 3 == 0) {
                jefe=true;
                int contMonJ = contMoneda , contObj= contMoneda + 10;
                jefeSprite.scale(4);
                CCPoint posicionImagen = new CCPoint();
                posicionImagen.x = this.getWidth()/2;
                posicionImagen.y = this.getHeight()/2;
                jefeSprite.setPosition(posicionImagen.x,posicionImagen.y);
                super.addChild(jefeSprite);

                if(contMonJ == contObj){
                    jefe=false;
                }
            }

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

        super.addChild(moneda, 5);
        super.addChild(lblContadorDeMonedas, 5);
    }
    public void listenerMonedas(float inutil){

        if(tocoMoneda || contMoneda == -1){
            super.removeChild(moneda, true);
            Random generadorDeAzar = new Random();
            CCPoint posicionImagen = new CCPoint();
            do{
                posicionImagen.x = generadorDeAzar.nextInt((int) (_Pantalla.width - moneda.getWidth()));
                posicionImagen.x += moneda.getWidth() / 2;
                posicionImagen.y = generadorDeAzar.nextInt((int) (_Pantalla.height - 70));
                posicionImagen.y += moneda.getHeight() / 2;
                moneda.setPosition(posicionImagen.x, posicionImagen.y);
            }while (choqueMonedaEnemigo());
            super.addChild(moneda);
            contMoneda ++;
            lblContadorDeMonedas.setString(String.valueOf(contMoneda));
            tocoMoneda = false;
        }
    }
    boolean choqueMonedaEnemigo() {
        boolean Toco= false;
        float img1Derecha, img1Izquierda, img1Arriba, img1Abajo;
        float img2Derecha, img2Izquierda, img2Arriba, img2Abajo;
        int i = 0;
        img1Arriba = moneda.getPositionY() + moneda.getHeight()/2;
        img1Abajo = moneda.getPositionY() - moneda.getHeight()/2;
        img1Derecha = moneda.getPositionX() + moneda.getWidth()/2;
        img1Izquierda = moneda.getPositionX() - moneda.getWidth()/2;

        while (!tocoEnemigo && i < arrEnemigos.size()){


            img2Arriba = arrEnemigos.get(i).getPositionY() + arrEnemigos.get(i).getHeight();
            img2Abajo = arrEnemigos.get(i).getPositionY() - arrEnemigos.get(i).getHeight();
            img2Derecha = arrEnemigos.get(i).getPositionX() + arrEnemigos.get(i).getWidth();
            img2Izquierda = arrEnemigos.get(i).getPositionX() - arrEnemigos.get(i).getWidth();

            if (img1Arriba>=img2Abajo && img1Arriba<=img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 1");
            }

            if (img1Arriba >= img2Abajo && img1Arriba <= img2Arriba && img1Izquierda >= img2Izquierda && img1Izquierda <= img2Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 2");
            }

            if (img1Abajo >= img2Abajo && img1Abajo <= img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 3");
            }

            if (img1Abajo >= img2Abajo && img1Abajo <= img2Arriba && img1Izquierda >= img2Izquierda && img1Izquierda <= img2Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 4");
            }

            if (img2Arriba >= img1Abajo && img2Arriba <= img1Arriba && img2Derecha >= img1Izquierda && img2Derecha <= img1Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 5");
            }

            if (img2Arriba >= img1Abajo && img2Arriba <= img1Arriba && img2Izquierda >= img1Izquierda && img2Izquierda <= img1Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 6");
            }

            if (img2Abajo >= img1Abajo && img2Abajo <= img1Arriba && img2Derecha >= img1Izquierda && img2Derecha <= img1Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 7");
            }

            if (img2Abajo >= img1Abajo && img2Abajo <= img1Arriba && img2Izquierda >= img1Izquierda && img2Izquierda <= img1Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 8");
            }

            i++;
        }
        if(jefe) {
            img2Arriba =jefeSprite.getPositionY() + jefeSprite.getHeight()*3;
            img2Abajo = jefeSprite.getPositionY() - jefeSprite.getHeight()*3;
            img2Derecha = jefeSprite.getPositionX() + jefeSprite.getWidth()*3;
            img2Izquierda = jefeSprite.getPositionX() - jefeSprite.getWidth()*3;
            if (img1Arriba>=img2Abajo && img1Arriba<=img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 1");
            }

            if (img1Arriba >= img2Abajo && img1Arriba <= img2Arriba && img1Izquierda >= img2Izquierda && img1Izquierda <= img2Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 2");
            }

            if (img1Abajo >= img2Abajo && img1Abajo <= img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 3");
            }

            if (img1Abajo >= img2Abajo && img1Abajo <= img2Arriba && img1Izquierda >= img2Izquierda && img1Izquierda <= img2Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 4");
            }

            if (img2Arriba >= img1Abajo && img2Arriba <= img1Arriba && img2Derecha >= img1Izquierda && img2Derecha <= img1Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 5");
            }

            if (img2Arriba >= img1Abajo && img2Arriba <= img1Arriba && img2Izquierda >= img1Izquierda && img2Izquierda <= img1Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 6");
            }

            if (img2Abajo >= img1Abajo && img2Abajo <= img1Arriba && img2Derecha >= img1Izquierda && img2Derecha <= img1Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 7");
            }

            if (img2Abajo >= img1Abajo && img2Abajo <= img1Arriba && img2Izquierda >= img1Izquierda && img2Izquierda <= img1Derecha) {
                Toco = true;
                Log.d("IntEntSprites", "Intersección caso 8");
            }
        }
        return Toco;
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
        posicionImagen.y = generadorDeAzar.nextInt((int) (_Pantalla.height - 120));
        posicionImagen.y += enemigo.getHeight();
        enemigo.setPosition(posicionImagen.x, posicionImagen.y);

        arrEnemigos.add(enemigo);
        super.addChild(enemigo);
    }
    public void SacarEnemigos (float inutil) {
        removeChild(arrEnemigos.get(0),true);
        arrEnemigos.remove(0);
    }
    public void GenerarEnemigosGrandes(float inutil){
        Sprite enemigo = Sprite.sprite("enemigo.png");
        enemigo.scale(2);
        Random generadorDeAzar = new Random();
        CCPoint posicionImagen = new CCPoint();
        posicionImagen.x = generadorDeAzar.nextInt((int) (_Pantalla.width - enemigo.getWidth()));
        posicionImagen.x += enemigo.getWidth()*2;
        posicionImagen.y = generadorDeAzar.nextInt((int) (_Pantalla.height - 250));
        posicionImagen.y += enemigo.getHeight()*2;
        enemigo.setPosition(posicionImagen.x, posicionImagen.y);
        arrEnemigos.add(enemigo);
        super.addChild(enemigo);
    }
    public void EnemigosVoladores(float inutil){
        Log.d("Vuela", "EnemigosVoladores: ");
        Sprite enemigo = Sprite.sprite("enemigo.png");
        int num;
        IntervalAction secuencia;
        MoveTo derecha,izquierda;
        Random generadorDeAzar = new Random();
        CCPoint posicionImagen = new CCPoint();
        posicionImagen.x = generadorDeAzar.nextInt((int) (_Pantalla.width - enemigo.getWidth()));
        posicionImagen.x += enemigo.getWidth() / 2;
        num = generadorDeAzar.nextInt(3);
        if(num==0)
        {
            posicionImagen.y = _Pantalla.height - (_Pantalla.height/2);
        }
        if(num==1)
        {
            posicionImagen.y = _Pantalla.height - (_Pantalla.height/3);
        }
        if(num==2)
        {
            posicionImagen.y = _Pantalla.height + (_Pantalla.height/3);
        }
        enemigo.setPosition(posicionImagen.x, posicionImagen.y);
        arrEnemigos.add(enemigo);
        super.addChild(enemigo);

        derecha= MoveTo.action(5,(_Pantalla.width-enemigo.getWidth())-100,posicionImagen.y);
        izquierda= MoveTo.action(5,(enemigo.getWidth()+ 100),posicionImagen.y);
        secuencia = Sequence.actions(derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda,derecha,izquierda);
        enemigo.runAction(secuencia);

    }
    public void EnemigosVoladores2(float inutil){
        Log.d("Vuela", "EnemigosVoladores2: ");
        Sprite enemigo = Sprite.sprite("enemigo.png");
        int num;
        IntervalAction secuencia;
        MoveTo arriba,abajo;
        Random generadorDeAzar = new Random();
        CCPoint posicionImagen = new CCPoint();
        posicionImagen.y = generadorDeAzar.nextInt((int) (_Pantalla.height - 100));
        num = generadorDeAzar.nextInt(3);
        if(num==0)
        {
            posicionImagen.x = _Pantalla.width - (_Pantalla.width/2);
        }
        if(num==1)
        {
            posicionImagen.x = _Pantalla.width - (_Pantalla.width/3);
        }
        if(num==2)
        {
            posicionImagen.x = _Pantalla.width + (_Pantalla.width/3);
        }
        enemigo.setPosition(posicionImagen.x, posicionImagen.y);
        arrEnemigos.add(enemigo);
        super.addChild(enemigo);

        arriba= MoveTo.action(10,posicionImagen.x,(_Pantalla.height - enemigo.getHeight())- 100);
        abajo= MoveTo.action(10,posicionImagen.x,enemigo.getHeight()+ 100);
        secuencia = Sequence.actions(arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo,arriba,abajo);
        enemigo.runAction(secuencia);

    }
    public void EnemigosRayo(float inutil){
        Log.d("Vuela", "EnemigosVoladores2: ");
        Sprite enemigo = Sprite.sprite("enemigo.png");
        int num;
        IntervalAction secuencia;
        MoveTo ir = null;
        Random generadorDeAzar = new Random();
        CCPoint posicionImagen = new CCPoint();

        num = generadorDeAzar.nextInt(4);
        if(num==0)
        {
            posicionImagen.x = enemigo.getWidth() + 80;
            posicionImagen.y= enemigo.getHeight() + 80;
            ir= MoveTo.action(2, (_Pantalla.width - enemigo.getWidth()) - 80,(_Pantalla.height -enemigo.getHeight())  - 80);
        }
        if(num==1)
        {
            posicionImagen.x = (_Pantalla.width - enemigo.getWidth()) - 80;
            posicionImagen.y= enemigo.getHeight() + 80;
            ir= MoveTo.action(2,  enemigo.getWidth() + 80,(_Pantalla.height -enemigo.getHeight())  - 80);
        }
        if(num==2)
        {
            posicionImagen.x = (_Pantalla.width - enemigo.getWidth()) - 80;
            posicionImagen.y= (_Pantalla.height -enemigo.getHeight())  - 80;
            ir= MoveTo.action(2,  enemigo.getWidth() + 80,enemigo.getHeight() + 80);
        }
        if(num==3)
        {
            posicionImagen.x = enemigo.getWidth() + 80;
            posicionImagen.y= (_Pantalla.height -enemigo.getHeight())  - 80;
            ir= MoveTo.action(1, (_Pantalla.width - enemigo.getWidth()) - 80,enemigo.getHeight() + 80 );
        }
        enemigo.setPosition(posicionImagen.x, posicionImagen.y);
        arrEnemigos.add(enemigo);
        super.addChild(enemigo);
        secuencia = Sequence.actions(ir);
        enemigo.runAction(secuencia);


    }
    public void misiles (float inutil){
        Sprite enemigo = Sprite.sprite("enemigo.png");
        CCPoint posicionImagen = new CCPoint();
        Random generadorDeAzar = new Random();
        int num = generadorDeAzar.nextInt(4);
        MoveTo lugar = MoveTo.action(3,0,0);
        posicionImagen.x = this.getWidth() / 2;
        posicionImagen.y = this.getHeight() / 2;
        enemigo.setPosition(posicionImagen.x, posicionImagen.y);
        if(num==0){
            lugar= MoveTo.action(3,posicionImagen.x,-60);
        } else if(num==1)
        {
            lugar= MoveTo.action(3,posicionImagen.x,this.getHeight() + 60);
        } else if(num==2)
        {
            lugar= MoveTo.action(3,-60,posicionImagen.y);
        } else if(num==3)
        {
            lugar= MoveTo.action(3,this.getWidth() + 60,posicionImagen.y);
        }
        super.addChild(enemigo);
        enemigo.runAction(lugar);

    }

    boolean verificarTocoEnemigo(){
        float img1Derecha, img1Izquierda, img1Arriba, img1Abajo;
        float img2Derecha, img2Izquierda, img2Arriba, img2Abajo;
        int i = 0;
        img1Arriba = _Jugador.getPositionY() + _Jugador.getHeight()/2;
        img1Abajo = _Jugador.getPositionY() - _Jugador.getHeight()/2;
        img1Derecha = _Jugador.getPositionX() + _Jugador.getWidth()/2;
        img1Izquierda = _Jugador.getPositionX() - _Jugador.getWidth()/2;
        while (!tocoEnemigo && i < arrEnemigos.size()){
            img2Arriba = arrEnemigos.get(i).getPositionY() + arrEnemigos.get(i).getHeight()/2;
            img2Abajo = arrEnemigos.get(i).getPositionY() - arrEnemigos.get(i).getHeight()/2;
            img2Derecha = arrEnemigos.get(i).getPositionX() + arrEnemigos.get(i).getWidth()/2;
            img2Izquierda = arrEnemigos.get(i).getPositionX() - arrEnemigos.get(i).getWidth()/2;

            if( arrEnemigos.get(i).getScaleX()<=2) {
                if (img1Arriba >= img2Abajo && img1Arriba <= img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
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
            }
            else {
                if (img1Arriba >= img2Abajo * 2 && img1Arriba <= img2Arriba * 2 && img1Derecha >= img2Izquierda *2 && img1Derecha <= img2Derecha * 2) {
                    tocoEnemigo = true;
                    Log.d("IntEntSprites", "Intersección caso 1");
                }

                if (img1Arriba >= img2Abajo * 2 && img1Arriba <= img2Arriba * 2 && img1Izquierda >= img2Izquierda * 2 && img1Izquierda <= img2Derecha * 2) {
                    tocoEnemigo = true;
                    Log.d("IntEntSprites", "Intersección caso 2");
                }

                if (img1Abajo >= img2Abajo * 2 && img1Abajo <= img2Arriba * 2 && img1Derecha >= img2Izquierda * 2 && img1Derecha <= img2Derecha * 2) {
                    tocoEnemigo = true;
                    Log.d("IntEntSprites", "Intersección caso 3");
                }

                if (img1Abajo >= img2Abajo * 2 && img1Abajo <= img2Arriba * 2 && img1Izquierda >= img2Izquierda * 2 && img1Izquierda <= img2Derecha * 2) {
                    tocoEnemigo = true;
                    Log.d("IntEntSprites", "Intersección caso 4");
                }

                if (img2Arriba * 2 >= img1Abajo && img2Arriba * 2 <= img1Arriba && img2Derecha * 2 >= img1Izquierda && img2Derecha * 2 <= img1Derecha) {
                    tocoEnemigo = true;
                    Log.d("IntEntSprites", "Intersección caso 5");
                }

                if (img2Arriba * 2 >= img1Abajo && img2Arriba * 2 <= img1Arriba && img2Izquierda * 2 >= img1Izquierda && img2Izquierda * 2 <= img1Derecha) {
                    tocoEnemigo = true;
                    Log.d("IntEntSprites", "Intersección caso 6");
                }

                if (img2Abajo * 2 >= img1Abajo && img2Abajo * 2 <= img1Arriba && img2Derecha * 2 >= img1Izquierda && img2Derecha * 2 <= img1Derecha) {
                    tocoEnemigo = true;
                    Log.d("IntEntSprites", "Intersección caso 7");
                }

                if (img2Abajo * 2 >= img1Abajo && img2Abajo * 2 <= img1Arriba && img2Izquierda * 2>= img1Izquierda && img2Izquierda * 2<= img1Derecha) {
                    tocoEnemigo = true;
                    Log.d("IntEntSprites", "Intersección caso 8");
                }
            }
            i++;
        }
        if(jefe){
            img2Arriba = jefeSprite.getPositionY() + jefeSprite.getHeight() ;
            img2Abajo = jefeSprite.getPositionY() - jefeSprite.getHeight() ;
            img2Derecha = jefeSprite.getPositionX() + jefeSprite.getWidth() ;
            img2Izquierda = jefeSprite.getPositionX() - jefeSprite.getWidth() ;

            if (img1Arriba >= img2Abajo && img1Arriba <= img2Arriba && img1Derecha >= img2Izquierda && img1Derecha <= img2Derecha) {
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

        super.addChild(lblTimer, 5);

    }
    public void actualizarTimer(float inutilidad){
        lblTimer.setString(acumTimer + "s");
        acumTimer ++ ;
    }

    void ElegirMusica(){
        Random generador = new Random();
        int eleccion = generador.nextInt(7);

        try {
            Juego.MusicadeFondo = MediaPlayer.create(miContexto, arrayMusica[eleccion]);
            // Before playing, determine if playerMusic is occupied, so that no error will be reported.
            if (Juego.MusicadeFondo != null) {
                Juego.MusicadeFondo.stop();
            }
            Juego.MusicadeFondo.prepare();
            Juego.MusicadeFondo.start();
            Juego.MusicadeFondo.setLooping(true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void comenzarJuego(){
        ElegirMusica();



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
        Juego.MusicadeFondo.stop();
        Juego.MusicadeFondo.release();

    }
}
