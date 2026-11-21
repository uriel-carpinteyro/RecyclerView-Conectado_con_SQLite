package com.example.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

/** Formulario para modificar un producto que se eligió en la pantalla de consulta. */
public class ActualizarActivity extends AppCompatActivity {

    TextInputEditText etClave2, etNombre2, etDescripcion2, etPrecio2, etExistencias2, etCategoria2;
    Button btnActualizar2;
    BaseDatos baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar);

        baseDatos = new BaseDatos(this);

        etClave2 = findViewById(R.id.etClave2);
        etNombre2 = findViewById(R.id.etNombre2);
        etDescripcion2 = findViewById(R.id.etDescripcion2);
        etPrecio2 = findViewById(R.id.etPrecio2);
        etExistencias2 = findViewById(R.id.etExistencias2);
        etCategoria2 = findViewById(R.id.etCategoria2);
        btnActualizar2 = findViewById(R.id.btnActualizar2);

        // 1) Recibir los datos del producto seleccionado y mostrarlos en el formulario
        Intent intent = getIntent();
        etClave2.setText(intent.getStringExtra("clave"));
        etNombre2.setText(intent.getStringExtra("nombre"));
        etDescripcion2.setText(intent.getStringExtra("descripcion"));
        etPrecio2.setText(intent.getStringExtra("precio"));
        etExistencias2.setText(intent.getStringExtra("existencias"));
        etCategoria2.setText(intent.getStringExtra("categoria"));

        // 2) Guardar los cambios al presionar ACTUALIZAR
        btnActualizar2.setOnClickListener(v -> {
            if (datosValidos()) {
                actualizarProducto();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void actualizarProducto() {
        // Se crea el objeto con los valores modificados (trim() quita espacios sobrantes)
        Producto producto = new Producto(
                texto(etClave2),
                texto(etNombre2),
                texto(etDescripcion2),
                Double.parseDouble(texto(etPrecio2)),
                Integer.parseInt(texto(etExistencias2)),
                texto(etCategoria2));

        // Se envía al método UPDATE de la base de datos
        int filas = baseDatos.actualizar(producto);

        if (filas > 0) {
            Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();

            // Regresa a la pantalla de consulta, donde se ven los cambios
            Intent regresar = new Intent(this, ConsultaActivity.class);
            regresar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(regresar);
            finish();
        } else {
            Toast.makeText(this, "No se encontró el producto", Toast.LENGTH_SHORT).show();
        }
    }

    private String texto(TextInputEditText campo) {
        return String.valueOf(campo.getText()).trim();
    }

    private boolean datosValidos() {
        if (texto(etNombre2).isEmpty()) {
            etNombre2.setError("El nombre es obligatorio");
            return false;
        }
        try {
            double precio = Double.parseDouble(texto(etPrecio2));
            if (precio < 0) {
                etPrecio2.setError("El precio no puede ser negativo");
                return false;
            }
        } catch (NumberFormatException e) {
            etPrecio2.setError("Escribe un precio válido");
            return false;
        }
        try {
            int existencias = Integer.parseInt(texto(etExistencias2));
            if (existencias < 0) {
                etExistencias2.setError("Las existencias no pueden ser negativas");
                return false;
            }
        } catch (NumberFormatException e) {
            etExistencias2.setError("Escribe una cantidad válida");
            return false;
        }
        return true;
    }
}
