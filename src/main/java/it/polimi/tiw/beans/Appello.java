package it.polimi.tiw.beans;

import java.sql.Date;

public class Appello{
	public int id_appello;
	public Date data;
	public Appello(int id_appello, Date data) {
		this.id_appello=id_appello;
		this.data=data;
	}
	
}