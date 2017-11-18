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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.fermino.parciaisdocartola.dao.DatabaseHelper;
import org.fermino.parciaisdocartola.pojos.JogadorTimeFavorito;
import org.fermino.parciaisdocartola.pojos.LigaFavorita;
import org.fermino.parciaisdocartola.pojos.TimeFavorito;
import org.fermino.parciaisdocartola.services.RestClient;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by guihgf on 19/07/2017.
 */

public class LigaFavoritaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RestClient rest;
    ProgressDialog progress;
    private DatabaseHelper databaseHelper = null;
    List<LigaFavorita> ligas;
    private ListView lview;
    private int posicaoSelecionada;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View v;
    private LigaFavoritaAdapter adapter;

    public LigaFavoritaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        v= inflater.inflate(R.layout.content_ligas_favoritas, container, false);

        Button btnBuscar=(Button)v.findViewById(R.id.btnBuscarLigas);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.mainFragment, new BuscarLigaFavoritaFragment()).addToBackStack(null);
                ft.commit();
            }
        });

        lview = (ListView)v.findViewById(R.id.listBuscaLigas);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v;
        mSwipeRefreshLayout.setOnRefreshListener(LigaFavoritaFragment.this);

        progress = new ProgressDialog(getActivity());
        progress.setTitle("Carregando dados...");
        progress.show();

        carregarLigas();

        getActivity().setTitle("Minhas ligas favoritas");

        return  v;
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle("Minhas ligas favoritas");
    }

    @Override
    public void onRefresh() {

        carregarLigas();
        mSwipeRefreshLayout.setRefreshing(false);


    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(),DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void carregarLigas(){
        final Dao<LigaFavorita, Integer> ligaFavoritaDao;
        try {


            ligaFavoritaDao = getHelper().getLigaFavoritaDao();

            ligas=ligaFavoritaDao.queryForAll();

            if(ligas!=null && ligas.size()>0){
                adapter = new LigaFavoritaAdapter(ligas, LigaFavoritaFragment.this,getActivity());
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
                        builder.setMessage("Deseja remover a liga dos seus favoritos?").setPositiveButton("Sim", dialogClickListener)
                                .setNegativeButton("Não", dialogClickListener).show();



                        return true;
                    }

                });

                lview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putInt("liga", ligas.get(position).getLigaId());
                        bundle.putString("nome_liga", ligas.get(position).getNome());
                        bundle.putString("slug", ligas.get(position).getSlug());

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        LigaTimesFragment lf =    new LigaTimesFragment();
                        lf.setArguments(bundle);
                        ft.add(R.id.mainFragment,lf ).addToBackStack(null);
                        ft.commit();

                    }
                });

                progress.hide();
            }
            else{
                progress.hide();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeLiga(int id){
        final Dao<LigaFavorita, Integer> ligaFavoritaDao;
        try {
            ligaFavoritaDao = getHelper().getLigaFavoritaDao();
            ligaFavoritaDao.deleteById(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}