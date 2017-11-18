package org.fermino.parciaisdocartola;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.fermino.parciaisdocartola.pojos.TimeFavorito;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guihgf on 11/07/2017.
 */

public class BuscarTimeFavoritoAdapter extends BaseAdapter implements ListAdapter {
    private List<TimeFavorito> list=new ArrayList<TimeFavorito>();
    private BuscarTimeFavoritoFragment context;
    private  Activity activity;



    public BuscarTimeFavoritoAdapter(List<TimeFavorito> list, BuscarTimeFavoritoFragment context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;//list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_buscar_time, null);
        }

        //Handle TextView and display string from your list

        TextView listItemText = (TextView)view.findViewById(R.id.textListNomeTime);
        listItemText.setText(list.get(position).getNome());

        TextView listItemText2 = (TextView)view.findViewById(R.id.textListNCartoleiro);
        listItemText2.setText(list.get(position).getNomeCartola());

        ImageView imgEscudoTime= (ImageView) view.findViewById(R.id.imgEscudoTime);
        //faz download e cachea imagens
        Glide.with(context).load(list.get(position).getEscudo()).into(imgEscudoTime);

        ImageButton addBtn = (ImageButton)view.findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                //notifyDataSetChanged();
                context.addTimeFavorito(list.get(position));
            }
        });

        return view;

        /*deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });*/
    }


}
