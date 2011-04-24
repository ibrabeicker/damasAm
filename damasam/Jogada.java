/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package damasam;

/**
 *
 * @author ibrahim
 */
public class Jogada {

	private Peca p;
	private Peca comida;
	private int xDestino, yDestino;

	public Jogada(Peca p, Peca comida, int xDestino, int yDestino) {
		this.p = p;
		this.comida = comida;
		this.xDestino = xDestino;
		this.yDestino = yDestino;
	}

	public boolean correspondeA(Peca p, int x, int y){
		if(p.equals(this.p) && this.xDestino == x && this.yDestino == y){
			return true;
		}
		return false;
	}

	public Peca getComida() {
		return comida;
	}

	public Peca getP() {
		return p;
	}

	public int getxDestino() {
		return xDestino;
	}

	public int getyDestino() {
		return yDestino;
	}

	public boolean come(){
		return this.comida != null;
	}
	
}
