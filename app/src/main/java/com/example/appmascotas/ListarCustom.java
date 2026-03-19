package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
    private final String URL ="http://10.15.232.169/mascotas";

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mascotas");
        builder.setMessage("Confirme que desea eleminar a" + mascota.getNombre()+ "?");

        builder.setPositiveButton("Sí",(a,b)->{
            eliminarMascota(mascota.getId(),position);
                });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eliminarMascota(int id, int position) {

        String urlEliminar = URL + "/" + id;

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                urlEliminar,
                null,
                response -> {
                    try {
                        boolean eliminado = response.getBoolean("success");
                        String mensaje = response.getString("message");

                        if (eliminado) {
                            listaMascotas.remove(position);
                            adapter.notifyItemRemoved(position);

                            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.e("ErrorJSON", e.toString());
                    }
                },
                error -> {
                    Log.e("ErrorWS", error.toString());
                    Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
    }