package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Intent;
import java.util.ArrayList;

public class ListarCustom extends AppCompatActivity implements MascotaAdapter.OnAccionListener {

    RecyclerView recyclerMascota;
    MascotaAdapter adapter;
    ArrayList<Mascota> listaMascotas;

    RequestQueue requestQueue;
    private final String URL ="http://192.168.56.1:3000/mascotas";

    private void loadUI(){
        recyclerMascota= findViewById(R.id.recyclerMascotas);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_custom);

        loadUI();

        listaMascotas = new ArrayList<>();
        adapter= new MascotaAdapter(this, listaMascotas, this);

        recyclerMascota.setLayoutManager(new LinearLayoutManager(this));
        recyclerMascota.setAdapter(adapter);

        obtenerDatos();
    }

    private void obtenerDatos(){
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                jsonArray -> renderizarLista(jsonArray),
                error -> {
                    Log.e("ErrorWs", error.toString());
                    Toast.makeText(this, "No se obtuvieron los datos", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void renderizarLista(JSONArray jsonmascota){
        try {
            listaMascotas.clear();

            for (int i = 0; i < jsonmascota.length(); i++){
                JSONObject json = jsonmascota.getJSONObject(i);

                listaMascotas.add(new Mascota(
                        json.getInt("id"),
                        json.getString("tipo"),
                        json.getString("nombre"),
                        json.getString("color"),
                        json.getDouble("pesokg")
                ));
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e){
           Log.e("ErrorJSON", e.toString());
        }
    }

    @Override
    public void onEditar(int posicion, Mascota mascota) {
        Intent intent = new Intent(this, Actualizar.class);

        intent.putExtra("id", mascota.getId());
        intent.putExtra("tipo", mascota.getTipo());
        intent.putExtra("nombre", mascota.getNombre());
        intent.putExtra("color", mascota.getColor());
        intent.putExtra("pesokg", mascota.getPesokg());

        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        obtenerDatos();
    }
    @Override
    public void onEliminar(int position, Mascota mascota) {
        adapter.eliminarItem(position);
        Toast.makeText(this, "Eliminado: " + mascota.getNombre(), Toast.LENGTH_SHORT).show();
    }
}
