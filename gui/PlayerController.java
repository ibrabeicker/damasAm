/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import damasam.Jogador;
import damasam.Peca;
import damasam.Tabuleiro;

/**
 *
 * @author ibrahim
 */
public class PlayerController implements PlayerInterface {


	private Jogador jPreto, jVermelho;
	private Tabuleiro t;

	public PlayerController(Jogador jPreto, Jogador jVermelho){
		this.jPreto = jPreto;
		this.jVermelho = jVermelho;
		this.t = jPreto.getTabuleiro();
	}

	public void makePlay(int oX, int oY, int dX, int dY) {
		if(t.getVez()){
			jVermelho.joga(oX, oY, dX, dY);
		} else {
			jPreto.joga(oX, oY, dX, dY);
		}
	}

}
