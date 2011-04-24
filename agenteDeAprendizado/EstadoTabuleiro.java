/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package agenteDeAprendizado;

/**
 *
 * @author ibrahim
 */
public class EstadoTabuleiro {

	private int[] x;
	private double score;
//	private int an, //numero de vermelhas
//			ad, //numero de damas vermelhas
//			aa, //vermelhas ameacadas
//			in, //pretas
//			id, //damas pretas
//			ia,
//			score; //pretas ameacadas

	public EstadoTabuleiro(int[] x, double score) {
		this.x = x;
		this.score = score;
	}

	public int getX(int x){
		return this.x[x];
	}

	public double getScore(){
		return this.score;
	}
	
}
