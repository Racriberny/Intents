package com.cristobalbernal.intents;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> photoActivityResultLauncher;
    Button abrir;
    Button llamar;
    Button abrirPaginaWeb;
    Button mapa;
    Button email;
    Button camara;
    private final int DEFAULT_ZOOM = 17;
    private final int TAKE_PHOTO = 2;
    private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] to = {"joanmonvi@gmail.com,daminto2001@gmail.com "};
        abrir = findViewById(R.id.abrirAplicacion);
        llamar = findViewById(R.id.llamar);
        abrirPaginaWeb = findViewById(R.id.abrirPaginaWeb);
        mapa = findViewById(R.id.maps);
        email = findViewById(R.id.email);
        camara = findViewById(R.id.camara);
        ivPhoto = findViewById(R.id.imageView);
        photoActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            ivPhoto.setImageBitmap(imageBitmap);
                        }
                    }
                });

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
                abrirMapa(38.9911726,-1.8647103,DEFAULT_ZOOM,"");
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarEmail(to,"AsuntoPrueba","Eres un payaso!!!");
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
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (photoIntent.resolveActivity(getPackageManager()) != null) {
                int permision = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                if (permision != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, TAKE_PHOTO);
                } else {
                    //Intent intent = new Intent(this, SomeActivity.class);
                    photoActivityResultLauncher.launch(photoIntent);
                    //startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
                    /* En onActivityResult recibiremos el resultado */
                }
            }
        }

    }

    @SuppressLint("IntentReset")
    private void enviarEmail(String[] para,String asunto, String contexto) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,para);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT,contexto);

        if (emailIntent.resolveActivity(getPackageManager()) !=null){
            startActivity(emailIntent);
        }
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

    private void abrirMapa(double lat, double lon){
        abrirMapa(lat,lon,DEFAULT_ZOOM,"");
    }

    private void abrirMapa(double lat, double lon, double zoom){
        abrirMapa(lat,lon,zoom);
    }
    private void abrirMapa(double lat, double lon, double zoom, String querry){
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+lat +","+lon+ "?z="+zoom+ "&q=" +querry));
        if (mapsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapsIntent);
        } else {
            Toast.makeText(this, "Google Maps no disponible", Toast.LENGTH_LONG).show();
        }
    }


    private void abrirMapa(String query) {
        abrirMapa(0,0,DEFAULT_ZOOM,query);
    }
}