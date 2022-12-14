package com.cristobalbernal.intents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button abrir;
    Button llamar;
    Button abrirPaginaWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        abrir = findViewById(R.id.abrirAplicacion);
        llamar = findViewById(R.id.llamar);
        abrirPaginaWeb = findViewById(R.id.abrirPaginaWeb);
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
            public void onClick(View view) {
                abrirPaginaWeb("www.google.es");
            }
        });

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
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}