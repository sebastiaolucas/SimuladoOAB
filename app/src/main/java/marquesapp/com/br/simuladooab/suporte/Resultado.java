package marquesapp.com.br.simuladooab.suporte;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import java.io.Serializable;

public class Resultado implements Serializable {

    private int acertos;
    private long tempo;
    private int TOTAL;

    public Resultado(){
        this.acertos =0;
        this.tempo=0;
        this.TOTAL = 0;
    }

    public int getAcertos() {
        return acertos;
    }

    public void setAcertos(int acertos) {
        this.acertos = acertos;
    }

    public long getTempo() {
        return tempo;
    }

    public void setTempo(long tempo) {
        this.tempo = tempo;
    }

    public int getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(int TOTAL) {
        this.TOTAL = TOTAL;
    }
}
