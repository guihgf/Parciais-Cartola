package org.fermino.parciaisdocartola;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.fermino.parciaisdocartola.pojos.MercadoStatus;
import org.fermino.parciaisdocartola.services.RestClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusMercadoFragment extends Fragment {

    private ProgressDialog progress;
    private View v;

    public StatusMercadoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(Util.isNetworkAvailable(getActivity())){
            progress = new ProgressDialog(getActivity());
            progress.setTitle("Carregando dados...");
            progress.show();

            RestClient rest=new RestClient();
            rest.requestMercadoStatus(StatusMercadoFragment.this);
        }
        else{
            Toast.makeText(getActivity(), "Sem conexão com a internet...", Toast.LENGTH_SHORT).show();
        }

        v=inflater.inflate(R.layout.content_status_mercado, container, false);

        return v;
    }

    public void preencherStatusMercado(MercadoStatus mercadoStatus){
        if(mercadoStatus!=null){
            TextView txtRodadaAtual=(TextView)v.findViewById(R.id.txtRodadaAtual);
            TextView txtStatus=(TextView)v.findViewById(R.id.txtRodadaStatus);
            TextView txtJogadores=(TextView)v.findViewById(R.id.txtJogadores);
            TextView txtFechamento=(TextView)v.findViewById(R.id.txtFechamento);
            txtRodadaAtual.setText("Rodada: " + mercadoStatus.getRodada());
            txtStatus.setText("Mercado esta: " + mercadoStatus.getStatus());
            txtJogadores.setText("Jogadores: " + String.format("%,d", mercadoStatus.getTotalTimes()) );
            txtFechamento.setText("Fechamento em: "+mercadoStatus.getFechamento());
            if(progress!=null)
                progress.hide();
        }

    }

}
