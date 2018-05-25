package cgi.una.ac.cr.archivodemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import model.Archivo;

/**
 * Created by Daniel on 19/05/2018.
 */


public class ListAdapter extends ArrayAdapter<File> implements View.OnClickListener{

    private ArrayList<File> dataSet;
    Context mContext;
    private boolean tema; // 1 oscuro, 0 claro



    public ListAdapter(ArrayList<File> data, Context context) {
        super(context, R.layout.activity_list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }



    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        File  dataModel=(File)object;


    }


    public void setTema(boolean tema) {
        this.tema = tema;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        // Get the data item for this position
        final File dataModel = getItem(position);

        final int posicion = position;



        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.activity_list_item, null);
        }


        ImageView icon = (ImageView) convertView.findViewById(R.id.list_icon);

        if(dataModel.isDirectory()){
            icon.setImageResource(android.R.drawable.ic_menu_directions); // true directorios
            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    File dataModel = (File) dataSet.get(posicion);
                    dataSet.clear();
                    dataSet.addAll(lookForFilesAndDirectoriesX(dataModel));
                    notifyDataSetChanged();

                    Snackbar.make(view, dataModel.getPath(), Snackbar.LENGTH_LONG)
                            .show();

                }
            });
        }
        else {
            icon.setImageResource(android.R.drawable.ic_menu_agenda);
        }


        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(dataModel.getName());

        if(tema)
            name.setTextColor(convertView.getResources().getColor(R.color.white));
        else
            name.setTextColor(convertView.getResources().getColor(R.color.black));


        return convertView;
    }


    public ArrayList<File> lookForFilesAndDirectoriesX(File file) {


        String[] filesAndDirectories = file.list();
        String path = file.getAbsolutePath();
        ArrayList files = new ArrayList<File>();
        for( String fileOrDirectory : filesAndDirectories) {
            File f = new File(path + "/" + fileOrDirectory);
            files.add(f);
        }
        return files;
    }


}
