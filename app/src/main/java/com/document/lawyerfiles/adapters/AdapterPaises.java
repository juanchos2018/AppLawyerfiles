package com.document.lawyerfiles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.document.lawyerfiles.Clases.ClsCountries;
import com.document.lawyerfiles.R;

import java.util.List;

public class AdapterPaises   extends ArrayAdapter<ClsCountries> {

    public AdapterPaises(Context context, List<ClsCountries> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View v = convertView;
        //Comprobando si el View no existe
        if (null == convertView) {

            v = inflater.inflate( R.layout.item_paises, parent,false);
        }

        //Obteniendo instancias de los elementos
        TextView nombre = (TextView)v.findViewById(R.id.id_tvpais);
        TextView confirmados = (TextView)v.findViewById(R.id.id_NewConfirmed);

        ClsCountries item = getItem(position);

        nombre.setText(item.getCountry());
        confirmados.setText(item.getNewConfirmed());

        return v;

    }

    /**
     * Este método nos permite obtener el Id de un drawable a través
     * de su nombre
     * @param nombre  Nombre del drawable sin la extensión de la imagen
     *
     * @return Retorna un tipo int que representa el Id del recurso
     */
    private int convertirRutaEnId(String nombre){
        Context context = getContext();
        return context.getResources()
                .getIdentifier(nombre, "drawable", context.getPackageName());
    }
}
