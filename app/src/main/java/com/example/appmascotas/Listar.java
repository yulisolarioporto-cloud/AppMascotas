package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Listar extends AppCompatActivity {

    ListView lsvMascotas;
    RequestQueue requestQueue;

    private final String URL ="http://10.15.232.169/mascotas";

    private void loadUI(){
        lsvMascotas= findViewById(R.id.lsvMascotas);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar);

        loadUI();
        obtenerDatos();
    }

    private void obtenerDatos(){
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                jsonArray -> renderizarListView(jsonArray),
                error -> {
                    Log.e("ErrorWS", error.toString());
                    Toast.makeText(getApplicationContext(),"No se obtuvieron los datos", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void renderizarListView(JSONArray jsonMascotas){
        try{
            ArrayList<String> listaMascotas = new ArrayList<>();

            for(int i= 0; i<jsonMascotas.length(); i++){
                JSONObject jsonObject = jsonMascotas.getJSONObject(i);

                listaMascotas.add(
                        jsonObject.getString("tipo") + " " +
                                jsonObject.getString("nombre")
                );
            }

            ArrayAdapter adapter = new ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    listaMascotas
            );

            lsvMascotas.setAdapter(adapter);

        }catch (Exception e){
            Log.e("ErrorJson", e.toString());
        }
    }
}