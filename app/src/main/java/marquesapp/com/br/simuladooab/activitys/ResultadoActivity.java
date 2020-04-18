package marquesapp.com.br.simuladooab.activitys;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import marquesapp.com.br.simuladooab.R;
import marquesapp.com.br.simuladooab.suporte.Resultado;

public class ResultadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultado);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Resultado");

        Resultado resultado = (Resultado) getIntent().getSerializableExtra("RESULTADO");

        TextView txtTotal = (TextView) findViewById(R.id.result_total);
        txtTotal.setText("Quantidade de questões: "+resultado.getTOTAL());

        TextView txtAcertos = (TextView) findViewById(R.id.result_acertos);

        int por = 100 * resultado.getAcertos() / resultado.getTOTAL();

        txtAcertos.setText("Acertos: "+resultado.getAcertos()+ " ("+por+"%)");

        TextView txtErros = (TextView) findViewById(R.id.result_erros);

        int erros = resultado.getTOTAL() - resultado.getAcertos();
        por = 100 - por;
        txtErros.setText("Erros: "+erros+ " ("+por+"%)");

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(Calendar.HOUR, 0-date.getHours());
        c.add(Calendar.MINUTE, 0-date.getMinutes());
        c.add(Calendar.SECOND, 0-date.getSeconds());
        date = c.getTime();
        date.setTime(date.getTime()+resultado.getTempo());
        TextView txtTempo = (TextView) findViewById(R.id.result_tempo);
        txtTempo.setText("Tempo gasto: "+sdf.format(date));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}