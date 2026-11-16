package com.example.recyclerview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Pantalla de consulta: muestra los registros de SQLite y permite buscar uno. */
public class ConsultaActivity extends AppCompatActivity {

    EditText etBuscar;
    Button btnBuscar, btnTodos;
    TextView tvResumen, tvVacio;
    RecyclerView lista;

    BaseDatos baseDatos;
    ProductoAdaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consulta);

        etBuscar = findViewById(R.id.etBuscar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnTodos = findViewById(R.id.btnTodos);
        tvResumen = findViewById(R.id.tvResumen);
        tvVacio = findViewById(R.id.tvVacio);
        lista = findViewById(R.id.listaProductos);

        // 1) Conectar con SQLite
        baseDatos = new BaseDatos(this);
        baseDatos.insertarEjemplosSiEstaVacia();   // si no hay datos, crea algunos de ejemplo

        // 2) Preparar la lista
        adaptador = new ProductoAdaptador(new ArrayList<>());
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adaptador);

        // 3) Botones
        btnTodos.setOnClickListener(v -> {
            etBuscar.setText("");
            mostrarTodos();
        });
        btnBuscar.setOnClickListener(v -> buscarProducto());

        // Al abrir la pantalla se muestran todos los registros
        mostrarTodos();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /** Consulta general: SELECT * FROM productos */
    private void mostrarTodos() {
        List<Producto> resultados = baseDatos.consultarTodos();
        mostrarResultados(resultados, "Todos los productos: " + resultados.size() + " registro(s)");
    }

    /** Consulta específica: WHERE clave = ? OR nombre LIKE ? */
    private void buscarProducto() {
        String texto = String.valueOf(etBuscar.getText()).trim();
        if (texto.isEmpty()) {
            etBuscar.setError("Escribe una clave o un nombre");
            return;
        }
        List<Producto> resultados = baseDatos.buscar(texto);
        mostrarResultados(resultados,
                "Resultados para \"" + texto + "\": " + resultados.size() + " registro(s)");
    }

    /** Muestra los registros en la pantalla (o un aviso si no hay ninguno). */
    private void mostrarResultados(List<Producto> resultados, String resumen) {
        adaptador.actualizar(resultados);
        tvResumen.setText(resumen);
        tvVacio.setVisibility(resultados.isEmpty() ? View.VISIBLE : View.GONE);
    }
}