package com.example.appmascotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.crypto.Mac;

//RecyclerViewAdapter exige 3 metodos
public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    private final ArrayList<Mascota> lista;
    private final Context context;

    private final OnAccionListener listener;

    //Interface

    public interface OnAccionListener{
       void onEditar (int posicion, Mascota mascota);
       void onEliminar (int position, Mascota mascota);

    }


  //   Constructor
    // Contexto (Activity), Lista(obtenemos por WS GET), listener (eventos de los botones

    public MascotaAdapter ( Context context, ArrayList<Mascota> lista, OnAccionListener listener){
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }


    //Representara cada fila
    static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView txtNombre, txtTipo, txtPeso;
        Button btnEditar, btnEliminar;

        //Vinculacion

        ViewHolder(View itemview){
            super(itemview);
            txtNombre = itemview.findViewById(R.id.txtNombre);
            txtTipo = itemview.findViewById(R.id.txtTipo);
            txtPeso = itemview.findViewById(R.id.txtPeso);
            btnEditar= itemview.findViewById(R.id.btnEditar);
            btnEliminar= itemview.findViewById(R.id.btnEliminar);
        }
    }

    //1 Inflar= crear layout para cada fila
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mascota, parent, false);
        return new ViewHolder(view);
    }
    //Llenar los datos
    @Override
    public void onBindViewHolder(@NonNull MascotaAdapter.ViewHolder holder, int position) {
        Mascota mascota = lista.get(position);

        //TextView muestran los datos

        holder.txtNombre.setText(mascota.getNombre());
        holder.txtTipo.setText(mascota.getTipo());
        holder.txtPeso.setText(String.valueOf(mascota.getPesokg())+ "kg.");

        //Botones
        holder.btnEditar.setOnClickListener(v-> {
            listener.onEditar(holder.getAdapterPosition(),mascota);
        });
        holder.btnEliminar.setOnClickListener(v -> {
            listener.onEliminar(holder.getAdapterPosition(),mascota);

        });

    }
    //Calcular la cantidad de elementos de la lista
    @Override
    public int getItemCount() {
        return lista.size();
    }

    //Eliminar un elemento de la lista <Mascota>

    public void eliminarItem(int position){
        lista.remove(position);
        notifyItemRemoved(position);
    }
}
