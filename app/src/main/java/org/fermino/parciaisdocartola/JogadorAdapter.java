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
import org.fermino.parciaisdocartola.pojos.TimeFavorito;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guihgf on 25/07/2017.
 */

public class JogadorAdapter extends BaseAdapter implements ListAdapter {
    private List<JogadorTimeFavorito> list = new ArrayList<JogadorTimeFavorito>();
    private JogadoresFragment context;
    private Activity activity;


    public JogadorAdapter(List<JogadorTimeFavorito> list, JogadoresFragment context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
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
            LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_jogador, null);
        }

        //Handle TextView and display string from your list

        TextView listItemText = (TextView) view.findViewById(R.id.textListNomeJogador);
        listItemText.setText(list.get(position).getApelido());

        ImageView imgEscudoTime = (ImageView) view.findViewById(R.id.imgEscudoJogador);
        //faz download e cachea imagens
        Glide.with(context).load(list.get(position).getEscudo()).into(imgEscudoTime);

        TextView listItemText3 = (TextView) view.findViewById(R.id.textPontuacaoJogador);

        Double pontuacao=list.get(position).getNumPontos();
        listItemText3.setText(String.format("%.2f", pontuacao));

        if (pontuacao > 0) {
            listItemText3.setTextColor(Color.rgb(0, 200, 0));
        } else {
            listItemText3.setTextColor(Color.rgb(200, 0, 0));
        }

        return view;

    }
}