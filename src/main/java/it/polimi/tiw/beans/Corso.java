package it.polimi.tiw.beans;

public class Corso{
	public int id_corso;
	public String nome;
	public int cfu;
	public Corso(int id_corso, String nome,int cfu) {
		this.id_corso=id_corso;
		this.nome=nome;
		this.cfu=cfu;
	}
}