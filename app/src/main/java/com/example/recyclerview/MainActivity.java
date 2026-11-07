package com.example.recyclerview;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView lista;
    List<String> nombres;
    List<String> telefonos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.lista);
        lista.setLayoutManager(new LinearLayoutManager(this));

        nombres = new ArrayList<String>();
        nombres.add("Priscila");
        nombres.add("Guamiro");
        nombres.add("Sinforosa");
        nombres.add("Indalesio");

        telefonos = new ArrayList<String>();
        telefonos.add("12345678");
        telefonos.add("98765432");
        telefonos.add("13579864");
        telefonos.add("97532468");

        int[] imagenes = {
                R.drawable.imagen1,
                R.drawable.imagen2,
                R.drawable.imagen3,
                R.drawable.imagen4
        };

        Adaptador adaptador = new Adaptador(this, nombres, telefonos, imagenes);
        lista.setAdapter(adaptador);

        findViewById(R.id.btnCrud).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CrudActivity.class)));
    }
}