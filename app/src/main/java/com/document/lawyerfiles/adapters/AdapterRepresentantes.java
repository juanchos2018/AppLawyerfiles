package com.document.lawyerfiles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.document.lawyerfiles.Clases.ClsRepresentantes;
import com.document.lawyerfiles.R;

import java.util.List;

public class AdapterRepresentantes  extends ArrayAdapter<ClsRepresentantes> {


    public AdapterRepresentantes(Context context, List<ClsRepresentantes> objects) {
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
            //Si no existe, entonces inflarlo
            v = inflater.inflate( R.layout.item_lista, parent,false);
        }

        //Obteniendo instancias de los elementos
        TextView especieAnimal = (TextView)v.findViewById(R.id.especieAnimal);
        TextView descAnimal = (TextView)v.findViewById(R.id.descAnimal);
        TextView cargo = (TextView)v.findViewById(R.id.cargo);
        TextView numeroodoc = (TextView)v.findViewById(R.id.numerodoc);
//        ImageView imagenAnimal = (ImageView)v.findViewById(R.id.imagenAnimal);
        //Obteniendo instancia de la Tarea en la posición actual
        ClsRepresentantes item = getItem(position);

        especieAnimal.setText(item.getTipoDoc());
        numeroodoc.setText(item.getNroDoc());
        descAnimal.setText(item.getNombres());
        cargo.setText(item.getCargo());
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
