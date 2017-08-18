package marquesapp.com.br.simuladooab.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import marquesapp.com.br.simuladooab.activitys.ProvasActivity;
import marquesapp.com.br.simuladooab.activitys.QuestaoActivity;
import marquesapp.com.br.simuladooab.R;
import marquesapp.com.br.simuladooab.activitys.SimuladoPersonActivity;
import marquesapp.com.br.simuladooab.suporte.Prova;
import marquesapp.com.br.simuladooab.suporte.Resultado;
import marquesapp.com.br.simuladooab.xml.LeitorXml;


public class FrameFragment extends Fragment {

    private View layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.simulados, container, false);

        Button btn1 = (Button) layout.findViewById(R.id.btn_provas);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ProvasActivity.class));
            }
        });
        Button btn2 = (Button) layout.findViewById(R.id.btn_simulados);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),SimuladoPersonActivity.class));
            }
        });
        Button btn3 = (Button) layout.findViewById(R.id.btn_aleatorio);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LeitorXml lx = new LeitorXml(getResources().getXml(R.xml.provas));
                Prova prova = lx.listaProvaAleatoria(1);

                Intent intent = new Intent(getActivity(), QuestaoActivity.class);
                intent.putExtra("PROVA",prova);
                intent.putExtra("RESULTADO", new Resultado());
                startActivity(intent);
            }
        });
        return layout;
    }
}
