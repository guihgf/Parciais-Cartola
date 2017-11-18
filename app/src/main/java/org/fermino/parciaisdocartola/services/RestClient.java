package org.fermino.parciaisdocartola.services;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.fermino.parciaisdocartola.BuscarLigaFavoritaFragment;
import org.fermino.parciaisdocartola.BuscarTimeFavoritoFragment;
import org.fermino.parciaisdocartola.MainActivity;
import org.fermino.parciaisdocartola.StatusMercadoFragment;
import org.fermino.parciaisdocartola.TimeFavoritoFragment;
import org.fermino.parciaisdocartola.pojos.JogadorTimeFavorito;
import org.fermino.parciaisdocartola.pojos.LigaFavorita;
import org.fermino.parciaisdocartola.pojos.MercadoStatus;
import org.fermino.parciaisdocartola.pojos.Scout;
import org.fermino.parciaisdocartola.pojos.TimeFavorito;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by guihgf on 02/07/2017.
 */

public class RestClient
{
    public void requestMercadoStatus(final StatusMercadoFragment activity){
        RequestParams params = new RequestParams();
        params.put("servico","mercado_status");
        HttpUtil.get("cartola.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.has("rodada_atual")){
                        MercadoStatus mercadoStatus=new MercadoStatus();
                        mercadoStatus.setRodada(Integer.parseInt(response.getString("rodada_atual")));
                        mercadoStatus.setStatus(Integer.parseInt(response.getString("status_mercado")));
                        mercadoStatus.setTotalTimes(Integer.parseInt(response.getString("times_escalados")));
                        mercadoStatus.setFechamento(response.getString("fechamento"));
                        activity.preencherStatusMercado(mercadoStatus);
                    }
                    else{
                        throw new Exception("Resultado não encontrato.");
                    }
                } catch (Exception e) {
                    activity.preencherStatusMercado(null);
                    Log.e("Erro mercado/status",e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("Erro mercado/status",responseString.toString());
                activity.preencherStatusMercado(null);
            }
        });
    }

    public void requestBuscaTimeFavorito(String nomeTime, final BuscarTimeFavoritoFragment activity){
        RequestParams params = new RequestParams();
        params.put("servico","buscar_time");
        params.put("nome",nomeTime);
        HttpUtil.get("cartola.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray times) {
                try {
                    if(times.length()>0){
                        List<TimeFavorito> timesEncontrados=new ArrayList<TimeFavorito>();

                        for (int i = 0; i < times.length(); i++) {
                            JSONObject time = times.getJSONObject(i);
                            TimeFavorito timeFavorito=new TimeFavorito(time.getInt("time_id"),
                                                                        time.getString("nome"),
                                                                        time.getString("nome_cartola"),
                                                                        time.getString("slug"),
                                                                        time.getString("url_escudo_png"));
                            timesEncontrados.add(timeFavorito);
                            //Log.d("times",time.getString("nome_cartola"));
                        }

                        activity.preencherListaTimes(timesEncontrados,null);
                    }
                    else{
                        activity.preencherListaTimes(null,"Time não encontrato");
                    }
                } catch (Exception e) {
                    Log.e("Erro mercado/status",e.getMessage());
                    activity.preencherListaTimes(null,e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("Erro mercado/status",responseString.toString());
                activity.preencherListaTimes(null,responseString.toString());
            }
        });
    }

    /*public void requestJogadoresTimeFavorito(final String slug, final TimeFavoritoFragment activity){
        RequestParams params = new RequestParams();
        params.put("servico","pontucao_time");
        params.put("slug",slug);
        HttpUtil.get("cartola.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    if(response.length()>0){

                        JSONArray jogadores= response;

                       List<JogadorTimeFavorito> jogadoresEncontrados=new ArrayList<JogadorTimeFavorito>();

                        for (int i = 0; i < jogadores.length(); i++) {
                            JSONObject jogador = jogadores.getJSONObject(i);
                            JogadorTimeFavorito jogadorTimeFavorito=new JogadorTimeFavorito(jogador.getInt("atleta_id"),
                                    jogador.getString("apelido"),
                                    jogador.getInt("clube_id"),
                                    jogador.getInt("posicao_id"),
                                    jogador.getDouble("pontos_num"),
                                    jogador.getString("escudo"));

                            List<Scout> scoutsJogador= new ArrayList<Scout>();

                            if(jogador.has("scout")){
                                JSONObject scout=(JSONObject)jogador.get("scout");
                                Iterator<String> keys = scout.keys();
                                while(keys.hasNext()){
                                    String scoutCodigo=keys.next();
                                    Scout s=new Scout();
                                    s.setCodigo(scoutCodigo);
                                    s.setQuantidade(scout.getInt(scoutCodigo));
                                    scoutsJogador.add(s);
                                }

                            }
                            jogadorTimeFavorito.setScout(scoutsJogador);

                            jogadoresEncontrados.add(jogadorTimeFavorito);
                            //Log.d("times",time.getString("nome_cartola"));
                        }
                        activity.carregarJogadoresTimes(slug,jogadoresEncontrados,null);

                    }
                    else{
                        activity.carregarJogadoresTimes(slug,new ArrayList<JogadorTimeFavorito>(),null);

                    }
                } catch (Exception e) {
                    Log.e("Erro mercado/status",e.getMessage());
                    activity.carregarJogadoresTimes(null,null,e.getMessage());
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("Erro mercado/status",responseString.toString());
                activity.carregarJogadoresTimes(null,null,responseString.toString());
            }
        });

    }*/

    public void requestBuscaLigaFavorita(String nomeLiga, final BuscarLigaFavoritaFragment activity){
        RequestParams params = new RequestParams();
        params.put("servico","buscar_liga");
        params.put("nome",nomeLiga);
        HttpUtil.get("cartola.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray ligas) {
                try {
                    if(ligas.length()>0){
                        List<LigaFavorita> ligasEncontradas=new ArrayList<LigaFavorita>();

                        for (int i = 0; i < ligas.length(); i++) {
                            JSONObject time = ligas.getJSONObject(i);
                            LigaFavorita ligaFavorita=new LigaFavorita(time.getInt("liga_id"),
                                    time.getString("nome"),
                                    time.getString("slug"),
                                    time.getString("imagem"),
                                    time.getBoolean("mata_mata"));
                            ligasEncontradas.add(ligaFavorita);
                        }

                        activity.preencherListaLigas(ligasEncontradas,null);
                    }
                    else{
                        activity.preencherListaLigas(null,"Liga não encontrada");
                    }
                } catch (Exception e) {
                    Log.e("Erro liga",e.getMessage());
                    activity.preencherListaLigas(null,e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("Erro liga",responseString.toString());
                activity.preencherListaLigas(null,responseString.toString());
            }
        });
    }


}
