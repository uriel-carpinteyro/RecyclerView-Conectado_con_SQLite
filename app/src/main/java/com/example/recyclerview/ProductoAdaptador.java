package com.example.recyclerview;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

/** Muestra en la lista los productos que se recuperaron de SQLite. */
public class ProductoAdaptador extends RecyclerView.Adapter<ProductoAdaptador.Vista> {

    private final List<Producto> productos;

    public ProductoAdaptador(List<Producto> productos) {
        this.productos = productos;
    }

    /** Reemplaza los datos de la lista y vuelve a dibujarla. */
    public void actualizar(List<Producto> nuevos) {
        productos.clear();
        productos.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vista onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new Vista(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vista holder, int position) {
        Producto p = productos.get(position);
        holder.tvNombre.setText(p.nombre);
        holder.tvClaveCategoria.setText("Clave: " + p.clave + "   |   Categoría: " + valor(p.categoria));
        holder.tvDescripcion.setText(valor(p.descripcion));
        holder.tvPrecioExistencias.setText(String.format(Locale.getDefault(),
                "Precio: $%,.2f   |   Existencias: %d", p.precio, p.existencias));

        // Al tocar un producto se abre el formulario de actualización con sus datos
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ActualizarActivity.class);
            intent.putExtra("clave", p.clave);
            intent.putExtra("nombre", p.nombre);
            intent.putExtra("descripcion", p.descripcion);
            intent.putExtra("precio", String.valueOf(p.precio));
            intent.putExtra("existencias", String.valueOf(p.existencias));
            intent.putExtra("categoria", p.categoria);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    private String valor(String texto) {
        return (texto == null || texto.isEmpty()) ? "-" : texto;
    }

    public static class Vista extends RecyclerView.ViewHolder {
        TextView tvNombre, tvClaveCategoria, tvDescripcion, tvPrecioExistencias;

        public Vista(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvClaveCategoria = itemView.findViewById(R.id.tvClaveCategoria);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecioExistencias = itemView.findViewById(R.id.tvPrecioExistencias);
        }
    }
}