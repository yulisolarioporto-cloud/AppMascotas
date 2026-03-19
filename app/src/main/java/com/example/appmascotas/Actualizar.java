package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Actualizar extends AppCompatActivity {

    int idMascota;
    String tipoA, nombreA, colorA;
    double pesoA;

    EditText edtTipoA, edtNombreA, edtColorA, edtPesoA;
    Button btnActualizarMascota, btnVolver;
    TextView txtId;

    RequestQueue requestQueue;

    private String URL = "http://192.168.56.1:3000/mascotas/";

    private void loadUI(){
        edtTipoA = findViewById(R.id.edtTipoA);
        edtNombreA = findViewById(R.id.edtNombreA);
        edtColorA = findViewById(R.id.edtColorA);
        edtPesoA = findViewById(R.id.edtPesoA);
        btnActualizarMascota = findViewById(R.id.btnActualizarMascota);
        btnVolver = findViewById(R.id.btnVolver);
        txtId = findViewById(R.id.txtId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        loadUI();

        //  RECIBIR DATOS
        Bundle datos = getIntent().getExtras();

        if (datos != null){
            idMascota = datos.getInt("id");

            edtTipoA.setText(datos.getString("tipo"));
            edtNombreA.setText(datos.getString("nombre"));
            edtColorA.setText(datos.getString("color"));
            edtPesoA.setText(String.valueOf(datos.getDouble("peso")));

            txtId.setText("ID: " + idMascota);
        }

        btnActualizarMascota.setOnClickListener(v -> validarRegistro());

        btnVolver.setOnClickListener(v -> finish());
    }

    private void validarRegistro(){

        if (edtTipoA.getText().toString().isEmpty()){
            edtTipoA.setError("Ingrese tipo");
            return;
        }

        if (edtNombreA.getText().toString().isEmpty()){
            edtNombreA.setError("Ingrese nombre");
            return;
        }

        if (edtColorA.getText().toString().isEmpty()){
            edtColorA.setError("Ingrese color");
            return;
        }

        if (edtPesoA.getText().toString().isEmpty()){
            edtPesoA.setError("Ingrese peso");
            return;
        }

        tipoA = edtTipoA.getText().toString();
        nombreA = edtNombreA.getText().toString();
        colorA = edtColorA.getText().toString();
        pesoA = Double.parseDouble(edtPesoA.getText().toString());

        new AlertDialog.Builder(this)
                .setTitle("Actualizar")
                .setMessage("¿Desea actualizar?")
                .setPositiveButton("Sí", (a,b)-> Actualizar())
                .setNegativeButton("No", null)
                .show();
    }

    private void Actualizar(){

        requestQueue = Volley.newRequestQueue(this);

        JSONObject json = new JSONObject();

        try{
            json.put("tipo", tipoA);
            json.put("nombre", nombreA);
            json.put("color", colorA);
            json.put("pesokg", pesoA);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL + idMascota,
                json,
                response -> {
                    Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_LONG).show();
                    finish();
                },
                error -> {
                    Log.e("ERROR", error.toString());
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }
}