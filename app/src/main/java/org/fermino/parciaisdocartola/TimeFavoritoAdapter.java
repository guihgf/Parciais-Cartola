package org.fermino.parciaisdocartola;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.fermino.parciaisdocartola.pojos.JogadorTimeFavorito;
import org.fermino.parciaisdocartola.pojos.TimeFavorito;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guihgf on 11/07/2017.
 */

public class TimeFavoritoAdapter extends BaseAdapter implements ListAdapter {
    private List<TimeFavorito> list=new ArrayList<TimeFavorito>();
    private TimeFavoritoFragment context;
    private  Activity activity;



    public TimeFavoritoAdapter(List<TimeFavorito> list, TimeFavoritoFragment context, Activity activity) {
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
        context.removeTime(list.get(position).getId());
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_time_favorito, null);
        }

        //Handle TextView and display string from your list

        TextView listItemText = (TextView)view.findViewById(R.id.textListNomeTimeFavorito);
        listItemText.setText(list.get(position).getNome());

        ImageView imgEscudoTime= (ImageView) view.findViewById(R.id.imgEscudoTimeFavorito);
        //faz download e cachea imagens
        Glide.with(context).load(list.get(position).getEscudo()).into(imgEscudoTime);

        TextView listItemText3 = (TextView)view.findViewById(R.id.textPontuacao);

        TextView listItemText4 = (TextView)view.findViewById(R.id.textTotalJogadores);

        Double pontuacao=0.0;
        int total_jogadores=0;


        if(list.get(position).getJogadores()!=null){
            for(JogadorTimeFavorito jogadores: list.get(position).getJogadores()){
                pontuacao+=jogadores.getNumPontos();

                if(jogadores.getScouts().size()>0 || (jogadores.getPosicaoDesc()=="TEC" && jogadores.getNumPontos()!=0.0)){
                    total_jogadores+=1;
                }
            }
        }

        listItemText3.setText( String.format( "%.2f", pontuacao ));
        if(pontuacao>0){
            listItemText3.setTextColor(Color.rgb(0,200,0));
        }
        else{
            listItemText3.setTextColor(Color.rgb(200,0,0));
        }

        listItemText4.setText(String.valueOf(total_jogadores)+"/12");

       /* ImageButton btnExc=(ImageButton) view.findViewById(R.id.remove_btn);

        btnExc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimeFavoritoActivity activity=(TimeFavoritoActivity) context;
                activity.removeTime(list.get(position).getId());
                list.remove(position);
                notifyDataSetChanged();
            }
        });*/

        return view;

    }


}
