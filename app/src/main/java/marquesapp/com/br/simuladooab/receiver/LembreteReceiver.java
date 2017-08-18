package marquesapp.com.br.simuladooab.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import marquesapp.com.br.simuladooab.R;
import marquesapp.com.br.simuladooab.activitys.EstatisticasActivity;
import marquesapp.com.br.simuladooab.activitys.InicioActivity;
import marquesapp.com.br.simuladooab.dao.LembreteDAO;
import marquesapp.com.br.simuladooab.dao.ResolvidasDAO;

public class LembreteReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(LembreteDAO.getPending(context));
        alarm.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1000*60*60*24,LembreteDAO.getPending(context));
        ResolvidasDAO dao = new ResolvidasDAO(context);
        if(dao.getCountDay(ResolvidasDAO.data()) <= 0)
            criarNotificacao(context);
    }

    private void criarNotificacao(Context ctx) {

        Intent viewIntent = new Intent(ctx, InicioActivity.class);
        viewIntent.putExtra("1", "2");
        PendingIntent viewPendingIntent = PendingIntent.getActivity(ctx, 0, viewIntent, 0);

        long[] v = {100,300};

        Resources res = ctx.getResources();

        Bitmap bit = BitmapFactory.decodeResource(res,R.mipmap.ic_launcher);

        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bit)
                .setContentText("Você não respondeu nenhuma questão hoje, vamos praticar?")
                .setContentTitle(res.getText(R.string.app_name) + ": Lembrete")
                .setContentIntent(viewPendingIntent)
                .setVibrate(v)
                .setTicker("Lembrete")
                .setAutoCancel(true);

        NotificationManagerCompat nm = NotificationManagerCompat.from(ctx);
        nm.notify(001,b.build());
    }
}
