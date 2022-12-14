package com.cristobalbernal.intents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button abrir;
    Button llamar;
    Button abrirPaginaWeb;
    Button mapa;
    Button email;
    Button camara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] to = {"joanmocho@gmail.com"};
        abrir = findViewById(R.id.abrirAplicacion);
        llamar = findViewById(R.id.llamar);
        abrirPaginaWeb = findViewById(R.id.abrirPaginaWeb);
        mapa = findViewById(R.id.maps);
        email = findViewById(R.id.email);
        camara = findViewById(R.id.camara);


        abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAplicacion("com.cristobalbernal.coches", "MainActivity");
            }
        });
        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarPersonas("678935630");
            }
        });
        abrirPaginaWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            //Se le tiene que poner el https o sino derror!!!!!
            public void onClick(View view) {
                abrirPaginaWeb("https://www.google.es/");
            }
        });
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abriMapa("IES LA MAR");
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarEmail(to,"AsuntoPrueba","Hola Joan!!!");
            }
        });
        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });

    }

    private void abrirCamara() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

    @SuppressLint("IntentReset")
    private void enviarEmail(String[] para,String asunto, String contexto) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        //Le tienes que pasar un array porque o sino no pilla...
        //Preguntar german!!!
        emailIntent.putExtra(Intent.EXTRA_EMAIL,para);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT,contexto);
        startActivity(Intent.createChooser(emailIntent,"Send Email!!!!"));
    }


    private void abrirAplicacion(String pck, String clase) {
        Intent abrirAplicacion = new Intent()
                .setAction(Intent.ACTION_MAIN);
        abrirAplicacion.setComponent(new ComponentName(pck, pck+ "." + clase));
        startActivity(abrirAplicacion);
    }
    private void llamarPersonas(String tel) {
        int permision = ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permision != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
            startActivity(callIntent);
        }
    }
    private void abrirPaginaWeb(String url){
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(webIntent);
    }
    private void abriMapa(String query) {
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + query));
        if (mapsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapsIntent);
        } else {
            Toast.makeText(this, "Google Maps no disponible", Toast.LENGTH_LONG).show();
        }
    }
}