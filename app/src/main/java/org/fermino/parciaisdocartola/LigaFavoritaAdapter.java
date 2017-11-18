package org.fermino.parciaisdocartola;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.fermino.parciaisdocartola.pojos.JogadorTimeFavorito;
import org.fermino.parciaisdocartola.pojos.LigaFavorita;
import org.fermino.parciaisdocartola.pojos.TimeFavorito;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guihgf on 11/07/2017.
 */

public class LigaFavoritaAdapter extends BaseAdapter implements ListAdapter {
    private List<LigaFavorita> list=new ArrayList<LigaFavorita>();
    private LigaFavoritaFragment context;
    private  Activity activity;



    public LigaFavoritaAdapter(List<LigaFavorita> list, LigaFavoritaFragment context, Activity activity) {
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

    public void remove(int position){
        context.removeLiga(list.get(position).getLigaId());
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_liga_favorita, null);
        }

        //Handle TextView and display string from your list

        TextView listItemText = (TextView)view.findViewById(R.id.textListNomeLigaFavorita);
        listItemText.setText(list.get(position).getNome());

        ImageView imgEscudoLiga= (ImageView) view.findViewById(R.id.imgEscudoLigaFavorita);
        //faz download e cachea imagens
        Glide.with(context).load(list.get(position).getImagem()).into(imgEscudoLiga);

        return view;

    }


}
