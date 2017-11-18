package org.fermino.parciaisdocartola;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.fermino.parciaisdocartola.dao.DatabaseHelper;
import org.fermino.parciaisdocartola.pojos.JogadorTimeFavorito;
import org.fermino.parciaisdocartola.pojos.JogadoresTimes;
import org.fermino.parciaisdocartola.pojos.Scout;
import org.fermino.parciaisdocartola.pojos.TimeFavorito;
import org.fermino.parciaisdocartola.services.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by guihgf on 25/07/2017.
 */

public class JogadoresFragment extends Fragment
{
    private RestClient rest;
    ProgressDialog progress;
    List<TimeFavorito> times;
    private ListView lview;
    private int posicaoSelecionada;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    TimeFavorito time;
    private JogadorAdapter adapter;
    List<JogadoresTimes> jogadoresTimes;
    private JogadoresTimes jt=null;


    public JogadoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v= inflater.inflate(R.layout.content_jogadores, container, false);


        lview = (ListView)v.findViewById(R.id.listJogadores);

        progress = new ProgressDialog(getActivity());
        progress.setTitle("Carregando dados...");
        progress.show();

        String jsonMyObject="";
        String jsonJogadoresTimes="";

        Bundle extras = getArguments();
        if (extras != null) {
            jsonMyObject = extras.getString("time");
            jsonJogadoresTimes=extras.getString("jogadores");
        }
        time = new Gson().fromJson(jsonMyObject, TimeFavorito.class);

        if(jsonJogadoresTimes!=null)
            jogadoresTimes=new Gson().fromJson(jsonJogadoresTimes,new TypeToken<List<JogadoresTimes>>(){}.getType()) ;

        getActivity().setTitle(time.getNome());

        TextView txtTotal=(TextView)v.findViewById(R.id.txtTotalPontosJogadores);

        Double total=0.0;

        for(int i=0;i<time.getJogadores().size();i++){
            total+=time.getJogadores().get(i).getNumPontos();
        }

        txtTotal.setText(String.format("%.2f", total));

        if(total>0){
            txtTotal.setTextColor(Color.rgb(0,200,0));
        }
        else{
            txtTotal.setTextColor(Color.rgb(200,0,0));
        }

        preencherListaJogadores();

        return  v;
    }

    public void preencherListaJogadores(){
        //instantiate custom adapter
        Collections.sort(time.getJogadores(), new Comparator<JogadorTimeFavorito>() {
            @Override
            public int compare(JogadorTimeFavorito lhs, JogadorTimeFavorito rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return rhs.getPosicaoId() > lhs.getPosicaoId() ? -1 : (rhs.getPosicaoId() < lhs.getPosicaoId() ) ? 1 : 0;
            }
        });

        adapter = new JogadorAdapter(time.getJogadores(), JogadoresFragment.this,getActivity());
        lview.setAdapter(adapter);
        progress.hide();

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v2, int position, long id) {

                showPopup(time.getJogadores().get(position).getId(),time.getJogadores().get(position).getApelido(),time.getJogadores().get(position).getEscudo(),time.getJogadores().get(position).getScouts(),time.getJogadores().get(position).getPosicaoId());
            }
        });
    }

    public void showPopup(int id, String nome, String escudo,List<Scout> scouts, int posicao) {


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.modal_scout, null);

        View viewParent=inflater.inflate(R.layout.content_jogadores, null);

        TextView tvNome =(TextView) popupView.findViewById(R.id.textNomeScout);
        tvNome.setText(nome);

        ImageView imgEscudoTime= (ImageView) popupView.findViewById(R.id.imgEscudoScout);
        Glide.with(JogadoresFragment.this).load(escudo).into(imgEscudoTime);

        Button btnTotal=(Button) popupView.findViewById(R.id.btnTotalTimes);


        for(int i=0;i<jogadoresTimes.size();i++){
            if(jogadoresTimes.get(i).getId()==id)
                jt=jogadoresTimes.get(i);
        }
        btnTotal.setText("Em " +jt.getTimes().size() + " time(s)" );

        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View popupView = inflater.inflate(R.layout.modal_jogadores_times, null);

                View viewParent=inflater.inflate(R.layout.content_jogadores, null);

                LinearLayout ll =(LinearLayout) popupView.findViewById(R.id.linearJogadoresTime);

                //ordenando por nome
                Collections.sort(jt.getTimes(), new Comparator<TimeFavorito>() {
                    @Override
                    public int compare(TimeFavorito lhs, TimeFavorito rhs) {
                        return (lhs.getNome().compareToIgnoreCase(rhs.getNome()));
                    }
                });


                for(int i=0;i<jt.getTimes().size();i++){
                    LinearLayout ld=new LinearLayout(getActivity());
                    ld.setOrientation(LinearLayout.HORIZONTAL);

                    ImageView imgEscudoTime= new ImageView(getActivity());
                    Glide.with(JogadoresFragment.this).load(jt.getTimes().get(i).getEscudo()).into(imgEscudoTime);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50,50);

                    imgEscudoTime.setLayoutParams(params);

                    ld.addView(imgEscudoTime);

                    TextView tv = new TextView(getActivity());
                    tv.setText(jt.getTimes().get(i).getNome());
                    ld.addView(tv);



                    ll.addView(ld);
                }


                PopupWindow popupWindow = new PopupWindow(popupView,
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                // If the PopupWindow should be focusable
                popupWindow.setFocusable(true);

                // If you need the PopupWindow to dismiss when when touched outside
                popupWindow.setBackgroundDrawable(new ColorDrawable());

                // Using location, the PopupWindow will be displayed right under anchorView
                popupWindow.showAtLocation(viewParent,Gravity.CENTER, 0, 0);


            }
        });

        LinearLayout ll =(LinearLayout) popupView.findViewById(R.id.linearScout);

        if(scouts.size()>0){
            LinearLayout lh=new LinearLayout(getActivity());
            lh.setOrientation(LinearLayout.HORIZONTAL);

            TextView th = new TextView(getActivity());
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics());
            th.setWidth(Math.round(px));
            th.setText("Scout");
            th.setTypeface(null, Typeface.BOLD_ITALIC);

            TextView th2 = new TextView(getActivity());
            th2.setText("Qtd.");
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
            th2.setWidth(Math.round(px));
            th2.setGravity(Gravity.CENTER);
            th2.setTypeface(null, Typeface.BOLD_ITALIC);

            TextView th3 = new TextView(getActivity());
            th3.setText("Pontos");
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, r.getDisplayMetrics());
            th3.setWidth(Math.round(px));
            th3.setGravity(Gravity.CENTER);
            th3.setTypeface(null, Typeface.BOLD_ITALIC);

            lh.addView(th);
            lh.addView(th2);
            lh.addView(th3);

            ll.addView(lh);

            for (int i = 0; i < scouts.size(); i++)
            {
                LinearLayout ld=new LinearLayout(getActivity());
                ld.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv = new TextView(getActivity());
                px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics());
                tv.setWidth(Math.round(px));

                tv.setText(scouts.get(i).getCodigo());

                TextView tv2 = new TextView(getActivity());
                tv2.setText(String.valueOf(scouts.get(i).getQuantidade()));
                tv2.setGravity(Gravity.CENTER);
                px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
                tv2.setWidth(Math.round(px));

                TextView tv3 = new TextView(getActivity());
                Double pontos=scouts.get(i).getQuantidade()*scouts.get(i).getPontos();
                tv3.setText(String.format("%.2f", pontos));
                tv3.setGravity(Gravity.CENTER);
                px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, r.getDisplayMetrics());
                tv3.setWidth(Math.round(px));
                if(pontos>0){
                    tv3.setTextColor(Color.rgb(0,200,0));
                }
                else{
                    tv3.setTextColor(Color.rgb(200,0,0));
                }


                ld.addView(tv);
                ld.addView(tv2);
                ld.addView(tv3);

                ll.addView(ld);
            }


        }
        else{
            TextView th = new TextView(getActivity());
            if(posicao!=6)
                th.setText("Scout não computado para este jogador!");
            else
                th.setText("Pontuação do técnico é medida pela média da pontuação de sua equipe.");
            th.setGravity(Gravity.CENTER);

            ll.addView(th);

        }





        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(viewParent,Gravity.CENTER, 0, 0);


    }
}
