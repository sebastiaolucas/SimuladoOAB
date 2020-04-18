package marquesapp.com.br.simuladooab.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import marquesapp.com.br.simuladooab.R;
import marquesapp.com.br.simuladooab.adapter.ProvasAdapter;
import marquesapp.com.br.simuladooab.suporte.Prova;
import marquesapp.com.br.simuladooab.suporte.Resultado;
import marquesapp.com.br.simuladooab.xml.LeitorXml;


public class ProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Provas");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LeitorXml lt = new LeitorXml(getResources().getXml(R.xml.provas));
        List<Prova> list = lt.listaProvas();

        ListView lista = (ListView) findViewById(R.id.lista_provas);

        lista.setAdapter(new ProvasAdapter(this,list));

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                finish();
                Prova prova = (Prova) adapterView.getItemAtPosition(i);
                prova.setQuestoes(Prova.listaPersonalisada(prova));
                Resultado resultado = new Resultado();
                Intent qs = new Intent(ProvasActivity.this, QuestaoActivity.class);
                qs.putExtra("PROVA", prova);
                qs.putExtra("RESULTADO", resultado);
                startActivity(qs);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
