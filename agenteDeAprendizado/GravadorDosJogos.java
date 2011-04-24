/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenteDeAprendizado;

import damasam.Observer;
import damasam.Tabuleiro;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ibrahim
 */
public class GravadorDosJogos implements Observer {

	private ArrayList<Tabuleiro> jogoCompleto;

	public GravadorDosJogos() {
		this.jogoCompleto = new ArrayList<Tabuleiro>();
	}

	public void update(Tabuleiro t) {
		jogoCompleto.add(t.clone());
	}

	public void terminate() {
		try {
			ObjectOutputStream objOutStr = new ObjectOutputStream(
					new FileOutputStream("jogos/" + Long.toString(new Date().getTime())));
			objOutStr.writeObject(jogoCompleto);
			objOutStr.close();
		} catch (IOException ex) {
			Logger.getLogger(GravadorDosJogos.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
