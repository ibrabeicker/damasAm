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

	/**
	 * x[0] = 1
	 * x[1] = numero de pecas amigas
	 * x[2] = numero de pecas inimigas
	 * x[3] = numero de damas amigas
	 * x[4] = numero de damas inimigas
	 * x[5] = numero de amigas ameacadas
	 * x[6] = numero de inimigas ameacadas
	 */
	private int[] x;
	private double score;

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
