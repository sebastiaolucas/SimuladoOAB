package marquesapp.com.br.simuladooab.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import marquesapp.com.br.simuladooab.R;
import marquesapp.com.br.simuladooab.suporte.Prova;
import marquesapp.com.br.simuladooab.suporte.Resultado;
import marquesapp.com.br.simuladooab.xml.LeitorXml;

public class SimuladoPersonActivity extends AppCompatActivity {

    EditText texto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simulado_person);

        getSupportActionBar().setTitle("Personalizado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        texto = (EditText) findViewById(R.id.quantidade);
        texto.addTextChangedListener(new TextWatcher() {

            int num = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                num = i;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length() > 0 && editable.charAt(0) == '0'){
                    editable.delete(num , num+1);
                }
                if(editable.length() > 0 && Integer.parseInt(editable.toString()) > 100) {
                    if(num == 0) {
                        editable.delete(num, 1);
                    }else{
                        editable.delete(num , num+1);
                    }
                }
            }
        });

        Button btnSimulado = (Button) findViewById(R.id.btn_iniciar);
        btnSimulado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(texto.getText().length() == 0){
                    Toast.makeText(SimuladoPersonActivity.this, "Campo Vazio", Toast.LENGTH_SHORT).show();
                    return;
                }

                finish();

                LeitorXml lx = new LeitorXml(getResources().getXml(R.xml.provas));
                Prova prova = lx.listaProvaAleatoria(Integer.parseInt(texto.getText().toString()));

                Intent intent = new Intent(SimuladoPersonActivity.this, QuestaoActivity.class);
                intent.putExtra("PROVA",prova);
                intent.putExtra("RESULTADO", new Resultado());
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}