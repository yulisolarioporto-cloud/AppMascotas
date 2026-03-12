package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    String tipo, nombre, color;
    double peso;
    EditText edtTipo, edtNombre, edtColor, edtPeso;
    Button btnRegistrarMascota;

    //Enviar / recibir los datos hacia el servicio
    RequestQueue requestQueue;

    //URL
    private final String URL ="http://192.168.56.1:3000/mascotas";

    private void loadUI(){
        edtTipo = findViewById(R.id.edtTipo);
        edtNombre = findViewById(R.id.edtNombre);
        edtColor = findViewById(R.id.edtColor);
        edtPeso = findViewById(R.id.edtPeso);
        btnRegistrarMascota = findViewById(R.id.btnRegistrarMascota);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });

        loadUI();

        btnRegistrarMascota.setOnClickListener(v -> validarRegistro());
    }

    private void resetUI(){
        edtTipo.setText(null);
        edtNombre.setText(null);
        edtColor.setText(null);
        edtPeso.setText(null);
        edtTipo.requestFocus();
    }
    private void validarRegistro() {

        if (edtTipo.getText().toString().isEmpty()) {
            edtTipo.setError("Complete con Perro o Gato");
            edtTipo.requestFocus();
            return;
        }

        if (edtNombre.getText().toString().isEmpty()) {
            edtNombre.setError("Escriba el nombre");
            edtNombre.requestFocus();
            return;
        }

        if (edtColor.getText().toString().isEmpty()) {
            edtColor.setError("Este campo es obligatorio");
            edtColor.requestFocus();
            return;
        }

        if (edtPeso.getText().toString().isEmpty()) {
            edtPeso.setError("Ingrese un valor");
            edtPeso.requestFocus();
            return;
        }

        tipo = edtTipo.getText().toString().trim();
        nombre = edtNombre.getText().toString().trim();
        color = edtColor.getText().toString().trim();
        peso = Double.parseDouble(edtPeso.getText().toString());


        if (!tipo.equals("Perro") && !tipo.equals("Gato")) {
            edtTipo.setError("Solo se permite: Perro o Gato");
            edtTipo.requestFocus();
            return;
        }

        if (peso < 0) {
            edtPeso.setError("Solo se permiten valores positivos");
            edtPeso.requestFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mascotas");
        builder.setMessage("¿Seguro de registrar?");

        builder.setPositiveButton("Sí", (a, b) -> {
            registrarMascota();
        });

        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void registrarMascota(){
        //Comunicación
        requestQueue= Volley.newRequestQueue(this);

        //POST = REQUIERE JSON (datos a enviar) //Empaquetamos los datos

        JSONObject jsonObject = new JSONObject();

        //Asignar los valores de las cajas

        try{
            jsonObject.put("tipo",tipo);
            jsonObject.put("nombre",nombre);
            jsonObject.put("color", color);
            jsonObject.put("pesokg",peso);
        }catch (JSONException e){
            Log.e("Error",e.toString());
        }


        //Definir objeto (respuesta obtener)
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //EXITO
                        Log.d("Resultado", jsonObject.toString());

                        try {
                            //Obtener ID generado
                            int idGenerado = jsonObject.getInt("id");

                            //Mostrar ID en mensaje
                            Toast.makeText(getApplicationContext(),
                                    "Guardado exitosamente. ID: " + idGenerado,
                                    Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        resetUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        NetworkResponse response= volleyError.networkResponse;
                        //Evaluar por codigo por error
                        if(response != null && response.data !=null){
                            //capturar el codigo de error  4xx, 5xx
                            int statusCode= response.statusCode;
                            String errorJson = new String(response.data);
                            Log.e("VolleyError", "Código:" + statusCode);
                            Log.e("VolleyError", "Cuerpo" + errorJson);
                        }else{
                            Log.e("VolleyError","Sin respuesta de red");
                        }
                        //Log.e("ErrorWs", volleyError.toString());
                    }
                }
        );

        //Ejecutamos el proceso
        requestQueue.add(jsonObjectRequest);

    }
}