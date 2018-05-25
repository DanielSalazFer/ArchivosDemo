package cgi.una.ac.cr.archivodemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import model.Archivo;

public class MainActivity extends AppCompatActivity {
    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };



    public final int EXTERNAL_REQUEST = 138;


    Archivo archivoEncontrado;

    ArrayList archivos = new ArrayList<Archivo>();

    Switch visual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visual = (Switch)findViewById(R.id.switch1);
        visual.setChecked(this.getSharedPreferences(
                getString(R.string.preference_visual), Context.MODE_PRIVATE).getBoolean(getString(R.string.preference_visual), false));



        if(this.getSharedPreferences(getString(R.string.preference_visual), Context.MODE_PRIVATE).getBoolean(getString(R.string.preference_visual), false)){

            aplicaTema(this.getSharedPreferences(getString(R.string.preference_visual_theme), Context.MODE_PRIVATE).getString(getString(R.string.preference_visual_theme), getResources().getString(R.string.temaOscuro))); // aplica el tema segun lo que diga el archivo
        }

        if(this.getSharedPreferences(getString(R.string.preference_visual), Context.MODE_PRIVATE).getBoolean(getString(R.string.preference_visual), true)){

            aplicaTema(this.getSharedPreferences(getString(R.string.preference_visual_theme), Context.MODE_PRIVATE).getString(getString(R.string.preference_visual_theme), getResources().getString(R.string.temaClaro))); // aplica el tema segun lo que diga el archivo
        }



    }

    public ArrayList<Archivo> lookForFilesAndDirectories(File file) {
        Archivo archivo = new Archivo();

        if( file.isDirectory() ) {
            String[] filesAndDirectories = file.list();
            for( String fileOrDirectory : filesAndDirectories) {
                File f = new File(file.getAbsolutePath() + "/" + fileOrDirectory);
                Log.d("Directorio:" , file.getName());
                archivo.setTipo(true);
                archivo.setNombre(file.getName());
                archivos.add(archivo);
                lookForFilesAndDirectories(f);
            }
        } else {
            Log.d("Archivo:" , file.getName());
            archivo.setTipo(false);
            archivo.setNombre(file.getName());
            archivos.add(archivo);


        }
        return archivos;
    }


    public void guardarBtn(View view) {



        EditText name = (EditText)findViewById(R.id.editText);


        this.getSharedPreferences(getString(R.string.preference_visual), Context.MODE_PRIVATE).edit().putBoolean(getString(R.string.preference_visual), visual.isChecked() ).commit();


        this.getSharedPreferences(getString(R.string.preference_visual_theme_name), Context.MODE_PRIVATE).edit().putString(getString(R.string.preference_visual_theme_name), name.getText().toString()).commit(); // almacena el nombre del tema que ingresa el usuario en el input para luego mostrarlo




        if(this.getSharedPreferences(getString(R.string.preference_visual), Context.MODE_PRIVATE).getBoolean(getString(R.string.preference_visual), false)){

            this.getSharedPreferences(getString(R.string.preference_visual_theme), Context.MODE_PRIVATE).edit().putString(getString(R.string.preference_visual_theme), getString(R.string.temaOscuro)).commit(); // guarda que tema usa, si oscuro o claro
            aplicaTema(this.getSharedPreferences(getString(R.string.preference_visual_theme), Context.MODE_PRIVATE).getString(getString(R.string.preference_visual_theme), getResources().getString(R.string.temaOscuro))); // aplica el tema segun lo que diga el archivo
        }
        else {
            this.getSharedPreferences(getString(R.string.preference_visual_theme), Context.MODE_PRIVATE).edit().putString(getString(R.string.preference_visual_theme), getString(R.string.temaClaro)).commit();
            aplicaTema(this.getSharedPreferences(getString(R.string.preference_visual_theme), Context.MODE_PRIVATE).getString(getString(R.string.preference_visual_theme), getResources().getString(R.string.temaClaro)));
        }



        Intent lista = new Intent(MainActivity.this, ListActivity.class);
        lista.putExtra(getResources().getString(R.string.intent_key), archivos);
        finish();
        startActivity(lista);
    }


    private void aplicaTema (String tema){

        ConstraintLayout mainActivity = (ConstraintLayout)findViewById(R.id.mainActivity);
        TextView nombre = (TextView)findViewById(R.id.textView);
        EditText name = (EditText)findViewById(R.id.editText);
        Button guardarBoton = (Button)findViewById(R.id.button);


        if(tema.equals(getResources().getString(R.string.temaOscuro))){
            mainActivity.setBackgroundColor(getResources().getColor(R.color.black));
            visual.setTextColor(getResources().getColor(R.color.white));
            nombre.setTextColor(getResources().getColor(R.color.white));
            name.setTextColor(getResources().getColor(R.color.white));
            name.setText(getResources().getString(R.string.temaOscuro));
            guardarBoton.setTextColor(getResources().getColor(R.color.white));
        }
        if(tema.equals(getResources().getString(R.string.temaClaro))){
            mainActivity.setBackgroundColor(getResources().getColor(R.color.white));
            visual.setTextColor(getResources().getColor(R.color.black));
            nombre.setTextColor(getResources().getColor(R.color.black));
            name.setTextColor(getResources().getColor(R.color.black));
            name.setText(getResources().getString(R.string.temaClaro));
            guardarBoton.setTextColor(getResources().getColor(R.color.black));
        }

        if(!this.getSharedPreferences(getString(R.string.preference_visual_theme_name), Context.MODE_PRIVATE).getString(getString(R.string.preference_visual_theme_name), name.getText().toString()).equals("")){
            name.setText(this.getSharedPreferences(getString(R.string.preference_visual_theme_name), Context.MODE_PRIVATE).getString(getString(R.string.preference_visual_theme_name), name.getText().toString()));
        }


    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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
