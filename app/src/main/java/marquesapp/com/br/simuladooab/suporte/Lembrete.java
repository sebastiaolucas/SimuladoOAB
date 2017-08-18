package marquesapp.com.br.simuladooab.suporte;

public class Lembrete {

    private boolean lembrete;
    private int hora;
    private int minuto;

    public Lembrete(){
        this.lembrete = false;
        this.hora = 18;
        this.minuto = 30;
    }

    public Lembrete(boolean lembrete, int hora, int minuto){
        this.lembrete = lembrete;
        this.hora = hora;
        this.minuto = minuto;
    }


    public boolean isLembrete() {
        return lembrete;
    }

    public void setLembrete(boolean lembrete) {
        this.lembrete = lembrete;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }
}
