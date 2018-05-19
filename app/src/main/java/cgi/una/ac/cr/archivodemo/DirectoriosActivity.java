package cgi.una.ac.cr.archivodemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import model.Archivo;

public class DirectoriosActivity extends AppCompatActivity {



    ArrayList dataModels = new ArrayList<Archivo>();

    ArrayList archivos = new ArrayList<Archivo>();

    Archivo archivoEncontrado = new Archivo();


    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };


    ListAdapter adapter;

    public final int EXTERNAL_REQUEST = 138;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        if(getIntent().getSerializableExtra(getResources().getString(R.string.intent_key_directorios))!=null){


           // System.out.println("Path: "+getIntent().getSerializableExtra(getResources().getString(R.string.intent_key_directorios)));
            archivos= (ArrayList) getIntent().getSerializableExtra(getResources().getString(R.string.intent_key_directorios));

            adapter.addAll(archivos);


        }

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
                    dataModels.addAll(hs);
                    */

                    System.out.println("else3");
                }
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directorios);
    }



    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



    public ArrayList<Archivo> lookForDirectories(File file) {
        Archivo archivo = new Archivo();

        if( file.isDirectory() ) {
            String[] filesAndDirectories = file.list();
            for( String fileOrDirectory : filesAndDirectories) {
                File f = new File(file.getAbsolutePath() );
                Log.d("Directorio:" , file.getName());
                archivo.setTipo(true);
                archivo.setNombre(file.getName());
                archivo.setPath(file.getAbsolutePath());
                archivos.add(archivo);
                lookForDirectories(f);
            }
        } else {
            Log.d("Archivo:" , file.getName());
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
}
