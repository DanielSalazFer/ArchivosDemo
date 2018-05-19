package cgi.una.ac.cr.archivodemo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import model.Archivo;

/**
 * Created by Daniel on 19/05/2018.
 */


public class ListAdapter extends ArrayAdapter<Archivo> implements View.OnClickListener{

    private ArrayList<Archivo> dataSet;
    Context mContext;



    public ListAdapter(ArrayList<Archivo> data, Context context) {
        super(context, R.layout.activity_list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }



    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Archivo  dataModel=(Archivo)object;

        /*
        switch (v.getId())
        {
            case R.id.listBoton:

                dataSet.remove(dataModel);
                notifyDataSetChanged(); // "Actualiza" la lista
                Snackbar.make(v, "Eliminado " + dataModel.getDescripcion(), Snackbar.LENGTH_LONG).setAction("No Action", null).show();
                break;
        }*/

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        System.out.println("Position = "+position);
        // Get the data item for this position
        final Archivo dataModel = getItem(position);




        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.activity_list_item, null);
        }


        ImageView icon = (ImageView) convertView.findViewById(R.id.list_icon);

        if(dataModel.isTipo()){
            icon.setImageResource(android.R.drawable.ic_menu_directions); // true directorios
            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                 Intent intent = new Intent(mContext, DirectoriosActivity.class);
                 intent.putExtra(finalConvertView.getResources().getString(R.string.intent_key_directorios), dataModel.getPath());
                 mContext.startActivity(intent);
                }
            });
        }
        else {
            icon.setImageResource(android.R.drawable.ic_menu_agenda);
        }


        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(dataModel.getNombre());



        return convertView;
    }
}
