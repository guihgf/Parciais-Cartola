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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
import org.fermino.parciaisdocartola.pojos.LigaFavorita;
import org.fermino.parciaisdocartola.pojos.Scout;
import org.fermino.parciaisdocartola.pojos.TimeFavorito;
import org.fermino.parciaisdocartola.pojos.TimeLiga;
import org.fermino.parciaisdocartola.services.HttpUtil;
import org.fermino.parciaisdocartola.services.RestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by guihgf on 19/07/2017.
 */

public class LigaTimesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RestClient rest;
    ProgressDialog progress;
    private DatabaseHelper databaseHelper = null;
    List<LigaFavorita> ligas;
    private ListView lview;
    private int posicaoSelecionada;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View v;
    private LigaPosicoesAdapter adapter;
    private int ligaId;
    List<TimeLiga> timesValidados;
    int rodada;
    int statusRodada;
    Spinner spinner;
    Spinner spinner2;
    int parciaisDisponiveis=0;
    private  String nomeLiga;
    List<JogadoresTimes> jogadoresTimes;

    public LigaTimesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v= inflater.inflate(R.layout.content_liga_posicoes, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v;
        mSwipeRefreshLayout.setOnRefreshListener(LigaTimesFragment.this);

        lview = (ListView)v.findViewById(R.id.listLigaPosicoes);

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
        progress.setTitle("Carregando dados.");
        progress.setMessage("Isso pode demorar se for necessário atualizar elencos ou resultados finais, aguarde por favor...");
        progress.show();

        Bundle extras = getArguments();

        ligaId = extras.getInt("liga");
        nomeLiga=extras.getString("nome_liga");

        getActivity().setTitle(nomeLiga);

        carregarTimes();

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(nomeLiga);
    }

    public void carregarTimes(){
        timesValidados = new ArrayList<TimeLiga>();
        if(Util.isNetworkAvailable(getActivity())) {
            //Verifico rodada e condição atual
            RequestParams params = new RequestParams();
            params.put("servico","mercado_status");
            HttpUtil.get("cartola.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    jogadoresTimes=new ArrayList<JogadoresTimes>();
                    try {
                        if (response.has("rodada_atual")) {
                            rodada = Integer.parseInt(response.getString("rodada_atual"));
                            statusRodada = Integer.parseInt(response.getString("status_mercado"));

                            //com a rodada obtida, verifico a liga
                            final Dao<LigaFavorita, Integer> ligaFavoritaDao;
                            ligaFavoritaDao = getHelper().getLigaFavoritaDao();

                            final Dao<TimeLiga, Integer> timeLigaDao;

                            timeLigaDao = getHelper().getTimeLigaDao();

                            /*List<TimeLiga> deletes= timeLigaDao.queryBuilder().where().eq("ligaId", ligaId).query();

                            for(int i=0;i<deletes.size();i++){
                                timeLigaDao.deleteById(deletes.get(i).getId());
                            }*/

                            LigaFavorita liga = ligaFavoritaDao.queryForId(ligaId);
                            if (liga != null) {
                                if(statusRodada==1||statusRodada==2){
                                    if (statusRodada != liga.getStatusRodadaTime() || rodada != liga.getRodadaTime() ||(timeLigaDao.queryBuilder().where().eq("ligaId", ligaId).query().size()==0)) {
                                        List<TimeLiga> deletes= timeLigaDao.queryBuilder().where().eq("ligaId", ligaId).query();

                                        for(int i=0;i<deletes.size();i++){
                                            timeLigaDao.deleteById(deletes.get(i).getId());
                                        }
                                        //varros os times para salvar
                                        try {
                                            RequestParams params = new RequestParams();
                                            params.put("servico", "detalhe_liga");
                                            params.put("slug", liga.getSlug());
                                            HttpUtil.get("cartola.php", params, new JsonHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                                    try {
                                                        if (response.length() > 0) {
                                                            final Dao<TimeLiga, Integer> timeLigaDao;
                                                            timeLigaDao = getHelper().getTimeLigaDao();

                                                            for (int i = 0; i < response.length(); i++) {
                                                                JSONObject time = response.getJSONObject(i);
                                                                if(!response.getJSONObject(0).has("matamata")){
                                                                    TimeLiga timeLiga = new TimeLiga(ligaId, time.getInt("time_id"), time.getString("nome"),
                                                                            time.getString("nome_cartola"), time.getString("slug"), time.getString("escudo"),
                                                                            time.getString("foto_perfil"), time.getDouble("patrimonio"), /*time.getString("ranking"),*/
                                                                            time.getString("pontos")/*, time.getString("variacao")*/);

                                                                    timeLiga.setJsonTime(time.getString("atletas"));
                                                                    timesValidados.add(timeLiga);
                                                                }
                                                            }

                                                            preencherListaTimes();
                                                        }

                                                    } catch (Exception e) {
                                                        progress.hide();
                                                        Toast.makeText(getActivity(), "Não foi possível obter status da rodada..."+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    progress.hide();
                                                    Toast.makeText(getActivity(), "Não foi possível obter status da rodada...", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (Exception e) {
                                            progress.hide();
                                            Toast.makeText(getActivity(), "Não foi possível obter os dados da liga", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        timesValidados=timeLigaDao.queryBuilder().where().eq("ligaId", ligaId).query();
                                        preencherListaTimes();

                                    }
                                }

                            } else {
                                throw new Exception();
                            }
                        }
                        else{
                            progress.hide();
                            Toast.makeText(getActivity(), "Não foi possível obter status da liga...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
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


                for (int i = 0; i < timesValidados.size(); i++) {
                    TimeLiga time =new TimeLiga();
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
                                        parciaisDisponiveis=1;
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

                        double totalParcial=0;
                        int total_jogadores=0;

                        for(int k=0;k<jogadoresEncontrados.size();k++){
                            totalParcial+=jogadoresEncontrados.get(k).getNumPontos();

                            if(jogadoresEncontrados.get(k).getScouts().size()>0 || (jogadoresEncontrados.get(k).getPosicaoDesc()=="TEC" && jogadoresEncontrados.get(k).getNumPontos()!=0.0)){
                                total_jogadores+=1;
                            }
                        }
                        time.setParcial(totalParcial);
                        time.setJogadoresJogaram(total_jogadores);

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
                    else{
                        time.setParcial(0.0);
                    }

                    timesValidados.set(i,time);
                }

                final Dao<LigaFavorita, Integer> ligaFavoritaDao;
                try {
                    ligaFavoritaDao = getHelper().getLigaFavoritaDao();
                    LigaFavorita liga = ligaFavoritaDao.queryForId(ligaId);
                    if (statusRodada != liga.getStatusRodadaTime() || rodada != liga.getRodadaTime()){
                        liga.setStatusRodadaTime(statusRodada);
                        liga.setRodadaTime(rodada);
                        ligaFavoritaDao.createOrUpdate(liga);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                /*filtro("campeonato");
                ordenacao("rodada", "desc");*/

                spinner = (Spinner) v.findViewById(R.id.filtroCampeonato);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                        R.array.liga_filtros, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter1);

                spinner2 = (Spinner) v.findViewById(R.id.ordemCampeonato);
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                        R.array.liga_ordens, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        String filtro=spinner.getSelectedItem().toString();

                        String ordem=spinner2.getSelectedItem().toString();
                        ordem=ordem.equals("Crescente")?"asc":"desc";

                        filtrarOrdernar(filtro,ordem);

                        adapter = new LigaPosicoesAdapter(timesValidados, LigaTimesFragment.this,getActivity());
                        lview.setAdapter(adapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        String filtro=spinner.getSelectedItem().toString();

                        String ordem=spinner2.getSelectedItem().toString();
                        ordem=ordem.equals("Crescente")?"asc":"desc";

                        filtrarOrdernar(filtro,ordem);

                        adapter = new LigaPosicoesAdapter(timesValidados, LigaTimesFragment.this,getActivity());
                        lview.setAdapter(adapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

                spinner.setVisibility(View.VISIBLE);
                spinner2.setVisibility(View.VISIBLE);

                adapter = new LigaPosicoesAdapter(timesValidados, LigaTimesFragment.this,getActivity());
                lview.setAdapter(adapter);

                lview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
                    {
                        if(timesValidados.get(position).getJogadores()!=null ){
                            TimeFavorito tf =new TimeFavorito();
                            tf.setJogadores(timesValidados.get(position).getJogadores());
                            tf.setEscudo(timesValidados.get(position).getEscudo());
                            tf.setJsonTime(timesValidados.get(position).getJsonTime());
                            tf.setNome(timesValidados.get(position).getNome());
                            tf.setSlug(timesValidados.get(position).getSlug());

                            Bundle bundle = new Bundle();
                            bundle.putString("time", new Gson().toJson(tf));
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


                try {
                    final Dao<TimeLiga, Integer> timeLigaDao;

                    timeLigaDao = getHelper().getTimeLigaDao();
                    for(int i=0;i<timesValidados.size();i++) {
                        timeLigaDao.createOrUpdate(timesValidados.get(i));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

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

    public void filtro(String tipo){


        if(tipo.equals("campeonato")){
            for(int i=0;i<timesValidados.size();i++){
                if(timesValidados.get(i).getJsonPontos()!=null && !timesValidados.get(i).getJsonPontos().equals("[]")){
                    JsonParser parser = new JsonParser();
                    JsonObject timeObj=parser.parse(timesValidados.get(i).getJsonPontos()).getAsJsonObject();
                    if(timeObj.has("campeonato")&& !timeObj.get("campeonato").isJsonNull()){
                        timesValidados.get(i).setPontuacao(timeObj.get("campeonato").getAsDouble()+(parciaisDisponiveis==1?timesValidados.get(i).getParcial():0));
                    }
                    else{
                        timesValidados.get(i).setPontuacao(0.0);
                    }
                }
            }
        }
        else if(tipo.equals("turno")){
            for(int i=0;i<timesValidados.size();i++){
                if(timesValidados.get(i).getJsonPontos()!=null && !timesValidados.get(i).getJsonPontos().equals("[]")){
                    JsonParser parser = new JsonParser();
                    JsonObject timeObj=parser.parse(timesValidados.get(i).getJsonPontos()).getAsJsonObject();
                    if(timeObj.has("turno")&& !timeObj.get("turno").isJsonNull()){
                        timesValidados.get(i).setPontuacao(timeObj.get("turno").getAsDouble()+(parciaisDisponiveis==1?timesValidados.get(i).getParcial():0));
                    }
                    else{
                        timesValidados.get(i).setPontuacao(0.0);
                    }
                }
            }
        }
        else if(tipo.equals("mes")){
            for(int i=0;i<timesValidados.size();i++){
                if(timesValidados.get(i).getJsonPontos()!=null && !timesValidados.get(i).getJsonPontos().equals("[]")){
                    JsonParser parser = new JsonParser();
                    JsonObject timeObj=parser.parse(timesValidados.get(i).getJsonPontos()).getAsJsonObject();
                    if(timeObj.has("mes")&& !timeObj.get("mes").isJsonNull()){
                        timesValidados.get(i).setPontuacao(timeObj.get("mes").getAsDouble()+(parciaisDisponiveis==1?timesValidados.get(i).getParcial():0));
                    }
                    else{
                        timesValidados.get(i).setPontuacao(0.0);
                    }
                }
            }
        }
        else if(tipo.equals("patrimonio")){
            for(int i=0;i<timesValidados.size();i++){
                timesValidados.get(i).setPontuacao(timesValidados.get(i).getPatrimonio());
            }
        }
    }
    public void ordenacao(String tipo, final String ordenacao){

        if(tipo.equals("rodada")){
            Collections.sort(timesValidados, new Comparator<TimeLiga>() {
                @Override
                public int compare(TimeLiga lhs, TimeLiga rhs) {
                    double lhsPontos=0;
                    double rhsPontos=0;

                    if(lhs==null || lhs.getJogadores()==null)
                        lhsPontos=0;
                    else
                        for(JogadorTimeFavorito jogadores: lhs.getJogadores()){
                            lhsPontos+=jogadores.getNumPontos();
                        }

                    if(rhs==null || rhs.getJogadores()==null)
                        rhsPontos=0;
                    else
                        for(JogadorTimeFavorito jogadores: rhs.getJogadores()){
                            rhsPontos+=jogadores.getNumPontos();
                        }
                    if(ordenacao.equals("desc"))
                        return rhsPontos > lhsPontos? 1 : (rhsPontos < lhsPontos) ? -1 : 0;
                    else
                        return rhsPontos > lhsPontos? -1 : (rhsPontos < lhsPontos) ? 1 : 0;
                }
            });
        }
        else if(tipo.equals("pontuacao")){
            Collections.sort(timesValidados, new Comparator<TimeLiga>() {
                @Override
                public int compare(TimeLiga lhs, TimeLiga rhs) {
                    if(ordenacao.equals("desc"))
                        return rhs.getPontuacao() > lhs.getPontuacao()? 1 : (rhs.getPontuacao() < lhs.getPontuacao()) ? -1 : 0;
                    else
                        return rhs.getPontuacao() > lhs.getPontuacao()? -1 : (rhs.getPontuacao() < lhs.getPontuacao()) ? 1 : 0;
                }
            });
        }

    }

    public void filtrarOrdernar(String filtro,String ordem){
        if(filtro.equals("Campeonato")){
            filtro("campeonato");
            ordenacao("pontuacao",ordem);
        }
        else if(filtro.equals("Turno")){
            filtro("turno");
            ordenacao("pontuacao",ordem);
        }
        else if(filtro.equals("Mês")){
            filtro("mes");
            ordenacao("pontuacao",ordem);
        }
        else if(filtro.equals("Patrimônio")){
            filtro("patrimonio");
            ordenacao("pontuacao",ordem);
        }
        else{
            filtro("campeonato");
            ordenacao("rodada",ordem);
        }
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




}