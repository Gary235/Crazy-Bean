package com.example.tp8;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class IngresoNombre extends AppCompatActivity {
    EditText edxN;
    Button btn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_ingreso);
        Log.d("EnNombre", "aAqui estoy");
        edxN = findViewById(R.id.ingresenombre);
        btn = findViewById(R.id.submit);
    }

    public void onClick(View vista){
    if (vista.getId() == btn.getId())
    {
        Log.d("TAG", "onClick: ");
    }

    }
}
