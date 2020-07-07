package com.document.lawyerfiles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.document.lawyerfiles.Clases.ClsArchivos;
import com.document.lawyerfiles.R;

import java.util.ArrayList;

public class AdapterArchivos  extends ArrayAdapter<ClsArchivos> {

    Context context;

    ViewHolder viewHolder;
    ArrayList<ClsArchivos> al_menu = new ArrayList<>();


    public AdapterArchivos(Context context, ArrayList<ClsArchivos> al_menu) {
        super(context, R.layout.item_carpetas2, al_menu);
        this.al_menu = al_menu;
        this.context = context;
    }

    @Override
    public int getCount() {
        return al_menu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.size() > 0) {
            return al_menu.size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView== null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_archivos2, parent, false);

            viewHolder.tv_foldern = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imageView2);//
            viewHolder.imgicnono = (ImageView) convertView.findViewById(R.id.imgicono);//imgicono
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(al_menu.get(position).getTipo_archivo().equals("docx")){
            viewHolder.img.setImageResource(R.drawable.fondodock);
            viewHolder.imgicnono.setImageResource(R.drawable.ico_wor3);
        }

        if(al_menu.get(position).getTipo_archivo().equals("doc")){
            viewHolder.img.setImageResource(R.drawable.fondodock);
            viewHolder.imgicnono.setImageResource(R.drawable.ico_wor3);
        }

        if(al_menu.get(position).getTipo_archivo().equals("ppt") || al_menu.get(position).getTipo_archivo().equals("pptx")){
            viewHolder.img.setImageResource(R.drawable.ppt_logo);
            viewHolder.imgicnono.setImageResource(R.drawable.ico_ppt2);
        }

        viewHolder.tv_foldern.setText(al_menu.get(position).getNombre_archivo());
        return convertView;

    }

    public static class ViewHolder {
        ImageView img,imgicnono;
        TextView tv_foldern, tv_foldersize;

    }
}
