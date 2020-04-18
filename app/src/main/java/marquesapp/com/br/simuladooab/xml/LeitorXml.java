package marquesapp.com.br.simuladooab.xml;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import marquesapp.com.br.simuladooab.suporte.Prova;
import marquesapp.com.br.simuladooab.suporte.Questao;


public class LeitorXml {

    private XmlResourceParser parser;
    List<Prova> provas;
    Prova prova;
    Questao questao;
    List<Questao> questoes;

    public LeitorXml(XmlResourceParser parser){
        this.parser = parser;
    }

    public List<Prova> listaProvas(){
        try{
            int evento = parser.getEventType();
            while (evento != XmlResourceParser.END_DOCUMENT) {
                if (evento == XmlResourceParser.START_TAG && "provas".equals(parser.getName())) {
                    provas = new ArrayList<Prova>();
                    evento = parser.next();
                    continue;
                }

                if (evento == XmlResourceParser.START_TAG && "prova".equals(parser.getName())) {
                    questoes = new ArrayList<Questao>();
                    prova = new Prova();
                    prova.setAno(parser.getAttributeValue(1));
                    prova.setValor(parser.getAttributeValue(0));
                    evento = parser.next();
                    continue;
                }

                if (evento == XmlResourceParser.START_TAG && "questao".equals(parser.getName())) {
                    questao = new Questao();
                    questao.setNumero(Integer.parseInt(parser.getAttributeValue(0)));
                    evento = parser.next();
                    continue;
                }

                if(evento == XmlResourceParser.START_TAG && "texto".equals(parser.getName())){
                    evento = parser.next();
                    questao.setTexto(parser.getText());
                    evento = parser.next();
                    continue;
                }

                if(evento == XmlResourceParser.START_TAG && "item".equals(parser.getName())){
                    String letra = parser.getAttributeValue(0);

                    switch(letra){
                        case "a":
                            evento = parser.next();
                            questao.setA(parser.getText());
                            continue;
                        case "b":
                            evento = parser.next();
                            questao.setB(parser.getText());
                            continue;
                        case "c":
                            evento = parser.next();
                            questao.setC(parser.getText());
                            continue;
                        case "d":
                            evento = parser.next();
                            questao.setD(parser.getText());
                            continue;
                    }
                }

                if(evento == XmlResourceParser.START_TAG && "resposta".equals(parser.getName())){
                    evento = parser.next();
                    questao.setResposta(parser.getText());
                    continue;
                }

                if (evento == XmlResourceParser.END_TAG && "questao".equals(parser.getName())) {
                    questoes.add(questao);
                    evento = parser.next();
                    continue;
                }

                if (evento == XmlResourceParser.END_TAG && "prova".equals(parser.getName())) {
                    prova.setQuestoes(questoes);
                    provas.add(prova);
                    questoes = new ArrayList<Questao>();
                    evento = parser.next();
                    continue;
                }

                if (evento == XmlResourceParser.END_TAG && "provas".equals(parser.getName())) {
                    parser.close();
                    return provas;
                }

                evento = parser.next();
            }

            return provas;

        }catch (XmlPullParserException | IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public Prova listaProvaAleatoria(int tam){
        Prova prova = new Prova();
        prova.setAno("2017");
        prova.setValor("");

        List<Questao> qs = new ArrayList<Questao>();
        List<Prova> prs = this.listaProvas();

        for(Prova pr: prs)
            qs.addAll(Prova.listaPersonalisada(pr));

        List<Questao> quests = new ArrayList<Questao>();
        for(int i=0; i<tam; i++){
            quests.add(getQuestao(qs));
        }

        prova.setQuestoes(quests);
        return prova;
    }

    private Questao getQuestao(List<Questao> qs){
        Random r = new Random();
        return qs.remove(r.nextInt(qs.size()));
    }
}
