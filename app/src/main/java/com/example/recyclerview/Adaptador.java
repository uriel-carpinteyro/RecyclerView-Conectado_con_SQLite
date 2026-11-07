package com.example.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.Vista> {

    public Context context;
    public List<String> nombres;
    public List<String> telefonos;
    int[] imagenes;

    public Adaptador(Context context, List<String> nombres, List<String> telefonos, int[] imagenes) {
        this.context = context;
        this.nombres = nombres;
        this.telefonos = telefonos;
        this.imagenes = imagenes;
    }

    @NonNull
    @Override
    public Adaptador.Vista onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.elemento, parent, false);
        return new Vista(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.Vista holder, int position) {
        holder.txtnombre.setText(nombres.get(position));
        holder.txtTelefono.setText(telefonos.get(position));
        holder.imgFoto.setImageResource(imagenes[position]);
    }

    @Override
    public int getItemCount() {
        return nombres.size();
    }

    public class Vista extends RecyclerView.ViewHolder {

        ImageView imgFoto;
        TextView txtnombre, txtTelefono;

        public Vista(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            txtnombre = itemView.findViewById(R.id.txtnombre);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);
        }
    }
}