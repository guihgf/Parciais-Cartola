package org.fermino.parciaisdocartola;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import org.fermino.parciaisdocartola.pojos.MercadoStatus;

/**
 * Created by guihgf on 19/07/2017.
 */

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v=inflater.inflate(R.layout.content_main, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v;
        mSwipeRefreshLayout.setOnRefreshListener(MainFragment.this);

        getActivity().setTitle(R.string.app_name);

        statusMercado();

        FloatingActionButton btnAddTime=(FloatingActionButton)v.findViewById(R.id.fab_add_times);
        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFragment, new TimeFavoritoFragment()).addToBackStack(null);
                ft.commit();
            }


        });

        FloatingActionButton btnAddLiga=(FloatingActionButton)v.findViewById(R.id.fab_add_ligas);
        btnAddLiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFragment, new LigaFavoritaFragment()).addToBackStack(null);
                ft.commit();
            }


        });

        return v;
    }

    @Override
    public void onRefresh() {

        statusMercado();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void statusMercado(){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.add(R.id.statusMercadoFragment,new StatusMercadoFragment());
        ft.commit();
    }

}