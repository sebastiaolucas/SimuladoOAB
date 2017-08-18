package marquesapp.com.br.simuladooab.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import marquesapp.com.br.simuladooab.R;
import marquesapp.com.br.simuladooab.dao.ResolvidasDAO;
import marquesapp.com.br.simuladooab.suporte.Prova;
import marquesapp.com.br.simuladooab.suporte.Questao;
import marquesapp.com.br.simuladooab.suporte.Resultado;

public class QuestaoActivity extends AppCompatActivity {

    private RadioGroup rg;
    private Questao qs;
    private Prova prova;
    private Resultado resultado;
    private long inicio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questao);

        inicio = System.currentTimeMillis();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        Button btn_pular = (Button) findViewById(R.id.btn_pular);

        prova = (Prova) getIntent().getSerializableExtra("PROVA");
        resultado = (Resultado) getIntent().getSerializableExtra("RESULTADO");

        if(prova.getQuestoes().size() == 1)
            btn_pular.setVisibility(View.GONE);
        qs = prova.getQuestoes().get(0);

        getSupportActionBar().setTitle(qs.getNome());

        TextView corpo = (TextView) findViewById(R.id.corpo);
        corpo.setText(qs.getTexto());


        final RadioButton radioA = (RadioButton) findViewById(R.id.radio_a);
        final RadioButton radioB = (RadioButton) findViewById(R.id.radio_b);
        final RadioButton radioC = (RadioButton) findViewById(R.id.radio_c);
        final RadioButton radioD = (RadioButton) findViewById(R.id.radio_d);


        radioA.setText("A) "+qs.getA());
        radioB.setText("B) "+qs.getB());
        radioC.setText("C) "+qs.getC());
        radioD.setText("D) "+qs.getD());

        rg = (RadioGroup) findViewById(R.id.radio_questions);

        btn_pular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long fim = System.currentTimeMillis() - inicio;

                qs.setTempo(qs.getTempo()+fim);
                resultado.setTempo(resultado.getTempo()+fim);

                prova.getQuestoes().remove(qs);
                prova.getQuestoes().add(qs);
                proximo();
            }
        });


        Button btn_responder = (Button) findViewById(R.id.btn_responder);
        btn_responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = "Selecione uma opção";

                if(rg.getCheckedRadioButtonId() == radioA.getId()){
                    s = "a";
                }else if(rg.getCheckedRadioButtonId() == radioB.getId()){
                    s = "b";
                }else if(rg.getCheckedRadioButtonId() == radioC.getId()){
                    s = "c";
                }else if(rg.getCheckedRadioButtonId() == radioD.getId()){
                    s = "d";
                } else{
                    Toast.makeText(QuestaoActivity.this,s,Toast.LENGTH_SHORT).show();
                    return;
                }

                long fim = System.currentTimeMillis() - inicio;
                qs.setTempo(qs.getTempo()+fim);

                resultado.setTOTAL(resultado.getTOTAL()+1);
                resultado.setTempo(resultado.getTempo()+fim);

                ResolvidasDAO dao = new ResolvidasDAO(QuestaoActivity.this);

                if(qs.getResposta().equals(s)){
                    dao.insere(qs,true);
                    alertaResposta(true,"Acertou");
                    resultado.setAcertos(resultado.getAcertos()+1);
                }else{
                    dao.insere(qs,false);
                    alertaResposta(false,qs.getResposta().toUpperCase());
                }
                dao.close();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(resultado.getTOTAL() == 0){
            super.onBackPressed();
            return;
        }
        sair();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(resultado.getTOTAL() == 0){
            finish();
            return super.onOptionsItemSelected(item);
        }
        sair();
        return super.onOptionsItemSelected(item);
    }

    private void proximo(){
        finish();
        Intent intent;
        if(prova != null && !prova.getQuestoes().isEmpty()){
            intent = new Intent(this, QuestaoActivity.class);
            intent.putExtra("PROVA", prova);
            intent.putExtra("RESULTADO", resultado);
        }else{
            intent = new Intent(this,ResultadoActivity.class);
            intent.putExtra("RESULTADO", resultado);
        }
        startActivity(intent);
    }

    private void sair(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Sair?");
        b.setMessage("Deseja terminar o simulado?");
        b.setPositiveButton("Terminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                Intent res = new Intent(QuestaoActivity.this,ResultadoActivity.class);

                long fim = System.currentTimeMillis() - inicio;

                qs.setTempo(qs.getTempo()+fim);
                resultado.setTempo(resultado.getTempo()+fim);

                res.putExtra("RESULTADO", resultado);
                startActivity(res);
            }
        });
        b.setNegativeButton("Cancelar",null);
        b.show();
    }

    private void alertaResposta(boolean acertou, String letra){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        if(acertou){
            b.setTitle("Acertou!");
        }else{
            b.setTitle("Errou!");
            b.setMessage("Resposta correta: "+letra);
        }
        b.setPositiveButton("Ok",null);

        b.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(prova != null)
                    prova.getQuestoes().remove(qs);
                proximo();
            }
        });
        b.show();
    }
}
