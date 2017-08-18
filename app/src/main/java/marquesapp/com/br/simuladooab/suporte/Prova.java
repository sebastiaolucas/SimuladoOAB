package marquesapp.com.br.simuladooab.suporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Prova implements Serializable {

	private String ano;
	private String valor;
	private List<Questao> questoes;

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public List<Questao> getQuestoes() {
		return questoes;
	}

	public void setQuestoes(List<Questao> questoes) {
		this.questoes = questoes;
	}

	@Override
	public String toString() {
		return "Prova "+valor+" - ("+ano+")";
	}

	public static List<Questao> listaPersonalisada(Prova prova){
		List<Questao> qs = prova.getQuestoes();
		List<Questao> temp = new ArrayList<Questao>();
		for(Questao q: qs){
			q.setNome(q.toString() + " - " + prova.toString());
			temp.add(q);
		}
		return temp;
	}
}
