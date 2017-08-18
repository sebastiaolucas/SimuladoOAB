package marquesapp.com.br.simuladooab.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import marquesapp.com.br.simuladooab.R;
import marquesapp.com.br.simuladooab.suporte.Prova;

public class ProvasAdapter extends BaseAdapter {


    private List<Prova> lista;
    private Activity ac;

    public ProvasAdapter(Activity ac, List<Prova> lista){
        this.ac = ac;
        this.lista = lista;
    }
    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = ac.getLayoutInflater().inflate(R.layout.item_provas,null);

        TextView text = (TextView) v.findViewById(R.id.item_prova_text);
        text.setText(lista.get(i).toString());

        return v;
    }
}
