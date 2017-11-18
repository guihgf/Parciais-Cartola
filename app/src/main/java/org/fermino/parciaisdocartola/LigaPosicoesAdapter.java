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
import org.fermino.parciaisdocartola.pojos.TimeLiga;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guihgf on 11/07/2017.
 */

public class LigaPosicoesAdapter extends BaseAdapter implements ListAdapter {
    private List<TimeLiga> list=new ArrayList<TimeLiga>();
    private LigaTimesFragment context;
    private  Activity activity;



    public LigaPosicoesAdapter(List<TimeLiga> list, LigaTimesFragment context, Activity activity) {
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
            LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_liga_posicoes, null);
        }

        //Handle TextView and display string from your list

        TextView listItemText = (TextView)view.findViewById(R.id.textNomeTimeLiga);
        listItemText.setText(list.get(position).getNome());

        TextView listItemText1 = (TextView)view.findViewById(R.id.textParcialNaRodadaLiga);
        TextView listItemText2 = (TextView)view.findViewById(R.id.textTotalJogadoresNaRodaLiga);

        listItemText1.setText( String.format( "%.2f", list.get(position).getParcial() ));
        if(list.get(position).getParcial() >0){
            listItemText1.setTextColor(Color.rgb(0,200,0));
        }
        else{
            listItemText1.setTextColor(Color.rgb(200,0,0));
        }

        listItemText2.setText(String.valueOf(list.get(position).getJogadoresJogaram())+"/12");

        TextView listItemText3 = (TextView)view.findViewById(R.id.textNomeCartoleiroLiga);
        listItemText3.setText(list.get(position).getNomeCartola());

        TextView listItemText4= (TextView)view.findViewById(R.id.textTotalPatrimonioNaLiga);
        listItemText4.setText("C$"+String.format( "%.2f", list.get(position).getPatrimonio() ) );

        TextView listItemText5= (TextView)view.findViewById(R.id.textPosicaoNaLiga);
        listItemText5.setText(String.valueOf(position+1)+"º");

        TextView listItemText6= (TextView)view.findViewById(R.id.textTotalPontosNaLiga);
        listItemText6.setText(String.format( "%.2f", list.get(position).getPontuacao() ));

        ImageView imgEscudoTimeLiga= (ImageView) view.findViewById(R.id.imgEscudoTimeLiga);
        //faz download e cachea imagens
        Glide.with(context).load(list.get(position).getEscudo()).into(imgEscudoTimeLiga);

        return view;

    }


}
