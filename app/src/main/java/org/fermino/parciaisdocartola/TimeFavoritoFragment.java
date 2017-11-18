package org.fermino.parciaisdocartola;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.fermino.parciaisdocartola.dao.DatabaseHelper;
import org.fermino.parciaisdocartola.pojos.JogadorTimeFavorito;
import org.fermino.parciaisdocartola.pojos.JogadoresTimes;
import org.fermino.parciaisdocartola.pojos.MercadoStatus;
import org.fermino.parciaisdocartola.pojos.Scout;
import org.fermino.parciaisdocartola.pojos.TimeFavorito;
import org.fermino.parciaisdocartola.services.HttpUtil;
import org.fermino.parciaisdocartola.services.RestClient;
import org.json.*;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by guihgf on 19/07/2017.
 */

public class TimeFavoritoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RestClient rest;
    ProgressDialog progress;
    private DatabaseHelper databaseHelper = null;
    List<TimeFavorito> times;
    List<TimeFavorito> timesValidados;
    private ListView lview;
    private TimeFavoritoAdapter adapter;
    private int posicaoSelecionada;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View v;
    List<JogadoresTimes> jogadoresTimes;

    public TimeFavoritoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        v= inflater.inflate(R.layout.content_time_favorito, container, false);

        Button btnBuscar=(Button)v.findViewById(R.id.btnBuscarTimes);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.mainFragment, new BuscarTimeFavoritoFragment()).addToBackStack(null);
                ft.commit();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) v;
        mSwipeRefreshLayout.setOnRefreshListener(TimeFavoritoFragment.this);

        lview = (ListView)v.findViewById(R.id.listMeuTimeFavorito);

        //Para o swipe funcionar
        lview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });



        progress = new ProgressDialog(getActivity());
        progress.setTitle("Carregando dados...");
        progress.show();

        carregarTimes();

        getActivity().setTitle("Meus times favoritos");

        return  v;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle("Meus times favoritos");
    }

    @Override
    public void onRefresh() {

        carregarTimes();



    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(),DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void carregarTimes(){

        if(Util.isNetworkAvailable(getActivity())) {
            //Verifico rodada e condição atual
            RequestParams params = new RequestParams();
            params.put("servico","mercado_status");
            HttpUtil.get("cartola.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        jogadoresTimes=new ArrayList<JogadoresTimes>();
                        if(response.has("rodada_atual")){
                            int rodada=Integer.parseInt(response.getString("rodada_atual"));
                            int statusRodada=Integer.parseInt(response.getString("status_mercado"));

                            //com a rodada obtida varros os times
                            final Dao<TimeFavorito, Integer> timeFavoritoDao;
                            timeFavoritoDao = getHelper().getTimeFavoritoDao();

                            times=timeFavoritoDao.queryForAll();

                            if(times!=null && times.size()>0){

                                timesValidados=new ArrayList<TimeFavorito>();
                                for (int i = 0; i < times.size(); i++) {

                                    //Se não tiver detalhe nenhum do time, carrego ele
                                    //Se houve troca de status da rodada no time
                                    //Se a rodada do time for diferente da rodada atual
                                    if(statusRodada==1||statusRodada==2){
                                        if(times.get(i).getJsonTime()==null || (statusRodada!=times.get(i).getStatusRodadaTime()) || (rodada!=times.get(i).getRodadaTime())){
                                            RequestParams params = new RequestParams();
                                            params.put("servico","dados_times");
                                            params.put("slug",times.get(i).getSlug());
                                            times.get(i).setRodadaTime(rodada);
                                            times.get(i).setStatusRodadaTime(statusRodada);
                                            HttpUtil.get("cartola.php", params, new JsonHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                                                    try {
                                                        for (int j = 0; j < times.size(); j++) {
                                                            if(jsonObject.get("slug").equals(times.get(j).getSlug())){
                                                                times.get(j).setJsonTime(jsonObject.get("atletas").toString());
                                                                timeFavoritoDao.createOrUpdate(times.get(j));
                                                                timesValidados.add(times.get(j));

                                                                if(timesValidados.size()==times.size()){
                                                                    preencherListaTimes();
                                                                    break;
                                                                }

                                                            }

                                                        }

                                                    }
                                                    catch (Exception e){
                                                        progress.hide();
                                                        Toast.makeText(getActivity(), "Não foi possível obter os dados do time...", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    progress.hide();
                                                    Toast.makeText(getActivity(), "Não foi possível obter os dados do time...", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else{
                                            timesValidados.add(times.get(i));
                                        }
                                    }

                                }

                                if(timesValidados.size()==times.size())
                                    preencherListaTimes();

                            }
                            else{
                                //Se não houver times encerro loading
                                progress.hide();
                            }

                        }
                        else{
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        progress.hide();
                        Toast.makeText(getActivity(), "Não foi possível obter status da rodada...", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progress.hide();
                    Toast.makeText(getActivity(), "Não foi possível obter status da rodada...", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            progress.hide();
            Toast.makeText(getActivity(), "Sem conexão com a internet...", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    public void preencherListaTimes() throws JSONException {
        RequestParams params = new RequestParams();
        params.put("servico","parciais");

        HttpUtil.get("cartola.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ordenando por nome
                Collections.sort(timesValidados, new Comparator<TimeFavorito>() {
                    @Override
                    public int compare(TimeFavorito lhs, TimeFavorito rhs) {
                        return (lhs.getNome().compareToIgnoreCase(rhs.getNome()));
                    }
                });

                for (int i = 0; i < timesValidados.size(); i++) {
                    TimeFavorito time =new TimeFavorito();
                    time=timesValidados.get(i);

                    if(time.getJsonTime()!=null && !time.getJsonTime().equals("[]")){
                        JsonParser parser = new JsonParser();
                        JsonArray arr = parser.parse(time.getJsonTime()).getAsJsonArray();

                        List<JogadorTimeFavorito> jogadoresEncontrados=new ArrayList<JogadorTimeFavorito>();

                        for (int j = 0; j < arr.size(); j++) {
                            JsonObject jogador = arr.get(j).getAsJsonObject();

                            //demitidos
                            if(jogador.get("clube_id").getAsInt()!=1){
                                JogadorTimeFavorito jogadorTimeFavorito=new JogadorTimeFavorito(jogador.get("atleta_id").getAsInt(),
                                        jogador.get("apelido").getAsString(),
                                        jogador.get("clube_id").getAsInt(),
                                        jogador.get("posicao_id").getAsInt(),
                                        jogador.get("pontos_num").getAsDouble(),
                                        jogador.get("escudo").getAsString());

                                List<Scout> scoutsJogador= new ArrayList<Scout>();
                                JsonObject scout = null;

                                if(response.has(String.valueOf(jogadorTimeFavorito.getId()))){
                                    try {
                                        JSONObject atleta=(JSONObject)response.get(String.valueOf(jogadorTimeFavorito.getId()));
                                        jogadorTimeFavorito.setNumPontos(atleta.getDouble("pontuacao"));
                                        if(atleta.has("scout") )
                                            scout=parser.parse(atleta.getString("scout")).getAsJsonObject();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {

                                    if (jogador.has("scout") ) {
                                        scout = jogador.get("scout").getAsJsonObject();
                                    }
                                }

                                if(scout!=null){
                                    List<String> key = new ArrayList<String>();
                                    for (Map.Entry<String, JsonElement> e : scout.entrySet()) {


                                        String scoutCodigo = e.getKey();
                                        Scout s = new Scout();
                                        s.setCodigo(scoutCodigo);
                                        s.setQuantidade(scout.get(scoutCodigo).getAsInt());
                                        scoutsJogador.add(s);
                                    }
                                }


                                jogadorTimeFavorito.setScout(scoutsJogador);

                                jogadoresEncontrados.add(jogadorTimeFavorito);
                            }

                        }
                        time.setJogadores(jogadoresEncontrados);

                        //mapeando jogadores x times
                        for(int l=0;l<jogadoresEncontrados.size();l++){
                            int encontrou=0;
                            for(int m=0;m<jogadoresTimes.size();m++){
                                if(jogadoresTimes.get(m).getId()==jogadoresEncontrados.get(l).getId()){
                                    TimeFavorito tf=new TimeFavorito();
                                    tf.setId(time.getId());
                                    tf.setNome(time.getNome());
                                    tf.setEscudo(time.getEscudo());
                                    jogadoresTimes.get(m).setTimes(tf);
                                    encontrou=1;
                                    break;
                                }

                            }

                            if(encontrou==0){
                                JogadoresTimes jt =new JogadoresTimes();
                                jt.setId(jogadoresEncontrados.get(l).getId());
                                jt.setNome(jogadoresEncontrados.get(l).getApelido());

                                TimeFavorito tf=new TimeFavorito();
                                tf.setId(time.getId());
                                tf.setNome(time.getNome());
                                tf.setEscudo(time.getEscudo());

                                jt.setTimes(tf);
                                jogadoresTimes.add(jt);
                            }

                        }
                    }

                    timesValidados.set(i,time);

                }
                //instantiate custom adapter
                adapter = new TimeFavoritoAdapter(timesValidados, TimeFavoritoFragment.this,getActivity());
                lview.setAdapter(adapter);

                lview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                   int position, long arg3) {

                        posicaoSelecionada=position;

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        adapter.remove(posicaoSelecionada);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        Vibrator v = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        v.vibrate(30);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Deseja remover o time dos seus favoritos?").setPositiveButton("Sim", dialogClickListener)
                                .setNegativeButton("Não", dialogClickListener).show();



                        return true;
                    }

                });

                lview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
                    {
                        if(timesValidados.get(position).getJogadores()!=null ){
                            Bundle bundle = new Bundle();
                            bundle.putString("time", new Gson().toJson(timesValidados.get(position)));
                            bundle.putString("jogadores",new Gson().toJson(jogadoresTimes) );

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            JogadoresFragment fj =    new JogadoresFragment();
                            fj.setArguments(bundle);
                            ft.add(R.id.mainFragment,fj ).addToBackStack(null);
                            ft.commit();
                        }
                        else{
                            Toast.makeText(getActivity(), "Time não escalado para esta rodada...", Toast.LENGTH_SHORT).show();
                        }

                        //Log.d("times",times.get(position).getNome());


                    }
                });
                mSwipeRefreshLayout.setRefreshing(false);
                progress.hide();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.hide();
                Toast.makeText(getActivity(), "Não foi possível obter as parciais da rodada...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void removeTime(int id){
        final Dao<TimeFavorito, Integer> timeFavoritoDao;
        try {
            timeFavoritoDao = getHelper().getTimeFavoritoDao();
            timeFavoritoDao.deleteById(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}