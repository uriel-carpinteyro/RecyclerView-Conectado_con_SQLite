package com.example.recyclerview;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class CrudActivity extends AppCompatActivity {

    TextInputEditText etClave, etNombre, etDescripcion, etPrecio, etExistencias, etCategoria;
    Button btnNuevo, btnGuardar, btnActualizar, btnEliminar, btnConsultar;
    BaseDatos baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crud);

        baseDatos = new BaseDatos(this);

        etClave = findViewById(R.id.etClave);
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etExistencias = findViewById(R.id.etExistencias);
        etCategoria = findViewById(R.id.etCategoria);
        btnNuevo = findViewById(R.id.btnNuevo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnConsultar = findViewById(R.id.btnConsultar);

        btnNuevo.setOnClickListener(v -> limpiarFormulario());

        btnGuardar.setOnClickListener(v -> {
            if (datosValidos()) {
                guardarProducto();
            }
        });

        btnActualizar.setOnClickListener(v -> {
            if (datosValidos()) {
                mensaje("Modificar: datos del producto validados");
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (texto(etClave).isEmpty()) {
                etClave.setError("Escribe la clave del producto");
            } else {
                mensaje("Eliminar: producto " + texto(etClave));
            }
        });

        // Abre la pantalla que lee los productos de SQLite (R de CRUD)
        btnConsultar.setOnClickListener(v ->
                startActivity(new Intent(CrudActivity.this, ConsultaActivity.class)));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void guardarProducto() {
        SQLiteDatabase db = baseDatos.getWritableDatabase();   // abre la BD en modo escritura

        ContentValues valores = new ContentValues();           // datos que se van a guardar
        valores.put("clave", texto(etClave));
        valores.put("nombre", texto(etNombre));
        valores.put("descripcion", texto(etDescripcion));
        valores.put("precio", Double.parseDouble(texto(etPrecio)));
        valores.put("existencias", Integer.parseInt(texto(etExistencias)));
        valores.put("categoria", texto(etCategoria));

        long resultado = db.insert("productos", null, valores); // ejecuta la inserción
        db.close();                                             // cierra la conexión

        if (resultado == -1) {
            etClave.setError("Ya existe un producto con esa clave");
        } else {
            mensaje("Producto guardado correctamente");
            limpiarFormulario();
        }
    }

    private String texto(TextInputEditText campo) {
        return String.valueOf(campo.getText()).trim();
    }

    private boolean datosValidos() {
        if (texto(etClave).isEmpty()) {
            etClave.setError("La clave es obligatoria");
            return false;
        }
        if (texto(etNombre).isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            return false;
        }
        try {
            double precio = Double.parseDouble(texto(etPrecio));
            if (precio < 0) {
                etPrecio.setError("El precio no puede ser negativo");
                return false;
            }
        } catch (NumberFormatException e) {
            etPrecio.setError("Escribe un precio válido");
            return false;
        }
        try {
            int existencias = Integer.parseInt(texto(etExistencias));
            if (existencias < 0) {
                etExistencias.setError("Las existencias no pueden ser negativas");
                return false;
            }
        } catch (NumberFormatException e) {
            etExistencias.setError("Escribe una cantidad válida");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        etClave.setText("");
        etNombre.setText("");
        etDescripcion.setText("");
        etPrecio.setText("");
        etExistencias.setText("");
        etCategoria.setText("");
        etClave.requestFocus();
    }

    private void mensaje(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}