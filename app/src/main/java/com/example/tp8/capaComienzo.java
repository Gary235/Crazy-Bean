package com.example.tp8;

import android.util.Log;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Label;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

public class capaComienzo extends Layer {
    CCSize _Pantalla;
    Label label;
    int cont = 0;

    public capaComienzo(CCSize pantalla) {
        setIsTouchEnabled(true);
        this._Pantalla = pantalla;
        ponerTitulo();
        ponerBotonInicio();
    }

    void ponerTitulo(){
        CCColor3B color3B;
        color3B = new CCColor3B(255,255,255);

        label  = Label.label("Crazy Bean", "montserrat_semibold.ttf", 150);
        label.setPosition(_Pantalla.getWidth()/2,_Pantalla.getHeight()/2);
        label.setColor(color3B);

        super.addChild(label);
    }
    void ponerBotonInicio(){
        Menu menu;
        MenuItemImage botonIncicio;
        botonIncicio = MenuItemImage.item("boton_inicio.png","boton_inicio.png",this,"Empezar");
        CCPoint posicionBoton = new CCPoint();
        posicionBoton.x = _Pantalla.getWidth()/2;
        posicionBoton.y = _Pantalla.getHeight()/2 - label.getHeight();
        botonIncicio.setPosition(posicionBoton.x, posicionBoton.y);
        Log.d("Juego", "posX: " + posicionBoton.x + "   posY: " + posicionBoton.y);
        menu = Menu.menu(botonIncicio);
        menu.setPosition(0,0);
        super.addChild(menu);
    }
    public void Empezar() {
        Log.d("Juego", "Apretoooooo");
        if(cont == 0){
            //cambiar de escena
            Log.d("Juego", "Apretoooooo");
            Juego.escenaJuego();
            cont++;
        }

    }



}
