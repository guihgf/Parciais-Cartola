package org.fermino.parciaisdocartola;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.fermino.parciaisdocartola.dao.DatabaseHelper;
import org.fermino.parciaisdocartola.pojos.LigaFavorita;
import org.fermino.parciaisdocartola.services.RestClient;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by guihgf on 19/07/2017.
 */

public class BuscarLigaFavoritaFragment extends Fragment {
    private RestClient rest;
    ProgressDialog progress;
    private DatabaseHelper databaseHelper = null;
    private ListView lView;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        v= inflater.inflate(R.layout.content_buscar_liga_favorita, container, false);

        rest=new RestClient();


        Button btnBuscar=(Button)v.findViewById(R.id.btnBuscarLigaFavorita);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                if(Util.isNetworkAvailable(getActivity())) {
                    progress = new ProgressDialog(getActivity());
                    progress.setTitle("Carregando dados...");
                    progress.show();
                    EditText txtNomeLiga = (EditText) v.findViewById(R.id.txtNomeLiga);
                    rest.requestBuscaLigaFavorita(txtNomeLiga.getText().toString(), BuscarLigaFavoritaFragment.this);
                }
                else{
                    Toast.makeText(getActivity(), "Sem conexão com a internet...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lView=(ListView)v.findViewById(R.id.listLigaFavorita);

        getActivity().setTitle("Busque a sua liga");

        return v;

    }

    @Override
    public  void onDestroy() {
        super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public void preencherListaLigas(List<LigaFavorita> ligas, String erro){
        progress.hide();

        if(erro!=null){
            Toast.makeText(getActivity(), erro, Toast.LENGTH_LONG).show();
        }
        else{
            if(ligas!=null && ligas.size()>0){
                //instantiate custom adapter
                BuscarLigaFavoritaAdapter adapter = new BuscarLigaFavoritaAdapter(ligas, BuscarLigaFavoritaFragment.this,getActivity());

                //handle listview and assign adapter
                lView.setAdapter(adapter);
            }
        }

    }
    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(),DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void addLigaFavorita(LigaFavorita liga){
        try {
            final Dao<LigaFavorita, Integer> ligaFavoritaDao = getHelper().getLigaFavoritaDao();

            ligaFavoritaDao.create(liga);

            Toast.makeText(getActivity(), liga.getNome()+" adicionada aos seus favoritos.", Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
