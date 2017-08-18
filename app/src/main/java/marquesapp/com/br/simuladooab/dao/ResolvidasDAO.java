package marquesapp.com.br.simuladooab.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import marquesapp.com.br.simuladooab.suporte.Questao;
import marquesapp.com.br.simuladooab.suporte.Resultado;


public class ResolvidasDAO extends SQLiteOpenHelper {

    private static final String DATABASE = "BancoSimuladoResolvidas";
    private static final int Version = 1;
    private static final String TABELA = "resolvidas";

    public ResolvidasDAO(Context context) {
        super(context, DATABASE, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA + " (id INTEGER PRIMARY KEY," +
                " nome TEXT NOT NULL," +
                " data TEXT NOT NULL," +
                " tempo REAL NOT NULL," +
                " resposta INTEGER NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABELA + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insere(Questao questao, boolean resposta){
        getWritableDatabase().insert(TABELA,null,toValues(questao,resposta));
    }

    public boolean respostasIsEmpty(){
        String sql = "SELECT * FROM "+ TABELA + ";";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        int count = c.getCount();
        c.close();
        return count == 0;
    }

    public void limpar(){
        getWritableDatabase().delete(TABELA,null,null);
    }

    private ContentValues toValues(Questao questao, boolean resposta){
        ContentValues cv = new ContentValues();
        cv.put("nome",questao.getNome());
        cv.put("tempo",questao.getTempo());
        cv.put("data",data());
        if(resposta){
            cv.put("resposta",1);
        }else{
            cv.put("resposta",0);
        }
        return cv;
    }

    public Resultado getResultado(){
        Resultado res = new Resultado();

        String sql1 = "SELECT count(id), sum(resposta), sum(tempo) FROM "+TABELA+" WHERE id >= 0;";

        Cursor c = getReadableDatabase().rawQuery(sql1,null);
        c.moveToFirst();

        res.setTOTAL(c.getInt(0));
        res.setAcertos(c.getInt(1));
        res.setTempo(c.getLong(2));

        c.close();

        return res;
    }

    public static String data(){
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(data);
    }

    public int getCountDay(String hoje) {
        String sql = "SELECT * FROM "+TABELA+" WHERE data = '"+hoje+"';";
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        int tam = c.getCount();
        c.close();
        return tam;
    }
}
