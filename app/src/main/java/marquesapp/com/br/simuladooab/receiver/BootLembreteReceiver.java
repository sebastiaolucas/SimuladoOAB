package marquesapp.com.br.simuladooab.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import marquesapp.com.br.simuladooab.dao.LembreteDAO;
import marquesapp.com.br.simuladooab.suporte.Lembrete;

public class BootLembreteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LembreteDAO dao = new LembreteDAO(context);
        Lembrete lembrete = dao.getLembrete();
        dao.criarAlarme(lembrete);
    }
}
