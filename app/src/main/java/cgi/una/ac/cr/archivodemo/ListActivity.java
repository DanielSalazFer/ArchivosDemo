package cgi.una.ac.cr.archivodemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.transform.Templates;

import model.Archivo;

public class ListActivity extends AppCompatActivity {

    ArrayList dataModels = new ArrayList<Archivo>();

    ArrayList archivos = new ArrayList<Archivo>();

    Archivo archivoEncontrado = new Archivo();


    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };


    ListAdapter adapter;

    public final int EXTERNAL_REQUEST = 138;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //dataModels.add(new Archivo(true, "FILE1"));




        if ( isExternalStorageReadable() ) {
            if (requestForPermission()) {
                String sdCardState = Environment.getExternalStorageState();
                if (!sdCardState.equals(Environment.MEDIA_MOUNTED)) {
                    //displayMessage("No SD Card.");
                    return;
                } else {
                    System.out.println("else");
                    File root = Environment.getExternalStorageDirectory();
                    System.out.println("else2");
                    dataModels = lookForFilesAndDirectories(root);


                    /*Set<String> hs = new HashSet<>();
                    hs.addAll(dataModels);
                    dataModels.clear();
                    dataModels.addAll(hs);*/


                    System.out.println("else3");
                }
            }
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



        boolean tema = aplicaTema();

        ListView lista = (ListView) findViewById(R.id.listView);



        adapter = new ListAdapter(dataModels,getApplicationContext());
        adapter.setTema(tema);
        lista.setAdapter(adapter);










    }


    private boolean aplicaTema(){

        ConstraintLayout listActivity = (ConstraintLayout)findViewById(R.id.listActivity);
        boolean tema;

        if(this.getSharedPreferences(getString(R.string.preference_visual), Context.MODE_PRIVATE).getBoolean(getString(R.string.preference_visual), false))
        {
            listActivity.setBackgroundColor(getResources().getColor(R.color.black));
            tema = true;
        }
        else
        {
            listActivity.setBackgroundColor(getResources().getColor(R.color.white));
            tema = false;
        }


        return tema;
    }





    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



    public ArrayList<Archivo> lookForFilesAndDirectories(File file) {
        Archivo archivo = new Archivo();

        if( file.isDirectory() ) {
            String[] filesAndDirectories = file.list();
            for( String fileOrDirectory : filesAndDirectories) {
                File f = new File(file.getAbsolutePath() + "/" + fileOrDirectory);
                //Log.d("Directorio:" , file.getName());
                archivo.setTipo(true);
                archivo.setNombre(file.getName());
                archivo.setPath(file.getAbsolutePath());
                archivos.add(archivo);
                lookForFilesAndDirectories(f);
            }
        } else {
            //Log.d("Archivo:" , file.getName());
            archivo.setTipo(false);
            archivo.setNombre(file.getName());
            archivos.add(archivo);


        }
        return archivos;
    }



    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i = new Intent(ListActivity.this, MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
