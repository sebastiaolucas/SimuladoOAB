package marquesapp.com.br.simuladooab.dao;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import marquesapp.com.br.simuladooab.suporte.Lembrete;

public class LembreteDAO extends SQLiteOpenHelper {

    private static final String DATABASE = "BancoSimuladoLembrete";
    private static final int Version = 1;
    private static final String TABELA = "lembrete";

    private Context ctx;

    public LembreteDAO(Context context) {
        super(context, DATABASE, null, Version);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA + " (id INTEGER PRIMARY KEY," +
                " hora INTEGER NOT NULL," +
                " minuto INTEGER NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABELA + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    public Lembrete getLembrete(){
        Lembrete l = new Lembrete();
        String sql = "SELECT * FROM "+TABELA+";";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        if(c.getCount() == 0){
            c.close();
            return l;
        }else{
            c.moveToLast();
            l.setLembrete(true);
            l.setHora(c.getInt(1));
            l.setMinuto(c.getInt(2));
            c.close();
            return l;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setLembrete(Lembrete lembrete){
        getWritableDatabase().delete(TABELA,null,null);
        if(lembrete.isLembrete()){
            getWritableDatabase().insert(TABELA,null,toValeus(lembrete));
        }
        criarAlarme(lembrete);
    }

    private ContentValues toValeus(Lembrete lembrete){
        ContentValues cv = new ContentValues();
        cv.put("hora",lembrete.getHora());
        cv.put("minuto",lembrete.getMinuto());
        return cv;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void criarAlarme(Lembrete lembrete){
        AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(getPending(ctx));
        Date date = getTempo(lembrete);
        if(lembrete.isLembrete()){
            alarm.setExact(AlarmManager.RTC_WAKEUP,date.getTime(),getPending(ctx));
        }
    }

    public static PendingIntent getPending(Context ctx){
        Intent intent = new Intent("LEMBRETE");
        return PendingIntent.getBroadcast(ctx,0,intent,0);
    }

    private Date getTempo(Lembrete l){
        Date date = new Date();

        boolean maior = false;

        if(date.getHours() >= l.getHora()){
            if(date.getHours() == l.getHora() && date.getMinutes() < l.getMinuto()){
                maior = false;
            }else{
                maior = true;
            }
        }

        Calendar c = new GregorianCalendar();
        c.setTime(date);

        int hora = 0;
        int minuto = l.getMinuto() - date.getMinutes();
        if(maior){
            hora = 24 - date.getHours() + l.getHora();
        }else{
            hora = l.getHora() - date.getHours();
        }
        c.add(Calendar.HOUR, hora);
        c.add(Calendar.MINUTE, minuto);
        c.add(Calendar.SECOND, 0-date.getSeconds());
        return c.getTime();
    }
}
