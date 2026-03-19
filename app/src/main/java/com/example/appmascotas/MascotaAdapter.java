package com.example.appmascotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    private final ArrayList<Mascota> lista;
    private final Context context;
    private final OnAccionListener listener;

    public interface OnAccionListener{
        void onEditar (int posicion, Mascota mascota);
        void onEliminar (int position, Mascota mascota);
    }

    public MascotaAdapter (Context context, ArrayList<Mascota> lista, OnAccionListener listener){
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombre, txtTipo, txtPeso;
        Button btnEditar, btnEliminar;

        ViewHolder(View itemview){
            super(itemview);
            txtNombre = itemview.findViewById(R.id.txtNombre);
            txtTipo = itemview.findViewById(R.id.txtTipo);
            txtPeso = itemview.findViewById(R.id.txtPeso);
            btnEditar= itemview.findViewById(R.id.btnEditar);
            btnEliminar= itemview.findViewById(R.id.btnEliminar);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mascota, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mascota mascota = lista.get(position);

        holder.txtNombre.setText(mascota.getNombre());
        holder.txtTipo.setText(mascota.getTipo());
        holder.txtPeso.setText(mascota.getPesokg() + " kg");

        holder.btnEditar.setOnClickListener(v ->
                listener.onEditar(holder.getAdapterPosition(), mascota)
        );

        holder.btnEliminar.setOnClickListener(v ->
                listener.onEliminar(holder.getAdapterPosition(), mascota)
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void eliminarItem(int position){
        lista.remove(position);
        notifyItemRemoved(position);
    }
}