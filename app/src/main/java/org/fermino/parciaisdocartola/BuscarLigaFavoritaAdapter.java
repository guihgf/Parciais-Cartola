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

import org.fermino.parciaisdocartola.pojos.LigaFavorita;
import org.fermino.parciaisdocartola.pojos.TimeFavorito;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guihgf on 11/07/2017.
 */

public class BuscarLigaFavoritaAdapter extends BaseAdapter implements ListAdapter {
    private List<LigaFavorita> list=new ArrayList<LigaFavorita>();
    private BuscarLigaFavoritaFragment context;
    private  Activity activity;



    public BuscarLigaFavoritaAdapter(List<LigaFavorita> list, BuscarLigaFavoritaFragment context, Activity activity) {
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
            view = inflater.inflate(R.layout.list_buscar_liga, null);
        }

        //Handle TextView and display string from your list

        TextView listItemText = (TextView)view.findViewById(R.id.textListNomeLiga);
        listItemText.setText(list.get(position).getNome());

        ImageView imgEscudoLiga= (ImageView) view.findViewById(R.id.imgEscudoLiga);
        //faz download e cachea imagens
        Glide.with(context).load(list.get(position).getImagem()).into(imgEscudoLiga);

        ImageButton addBtn = (ImageButton)view.findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                //notifyDataSetChanged();
                context.addLigaFavorita(list.get(position));
            }
        });

        return view;

    }


}
