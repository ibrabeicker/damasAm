/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package damasam;

import java.util.ArrayList;

/**
 *
 * @author ibrahim
 */
public class Jogada {

	private Peca p;
	private Peca comida;
	private int xDestino, yDestino;
	/**
	 * proximas obrigatorias faz uma estrutura de arvore com as jogadas que
	 * comem mais de 1 peca
	 */
	private ArrayList<Jogada> jogadasDaSequencia;

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

	public void setSequencia(ArrayList<Jogada> js){
		jogadasDaSequencia = js;
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

	public boolean isObrigatoria(){
		return this.comida != null;
	}

	public ArrayList<Jogada> getSequencia() {
		return jogadasDaSequencia;
	}

	public boolean haSequencia(){
		if(this.jogadasDaSequencia == null ||
				this.jogadasDaSequencia.isEmpty())
			return false;
		return true;
	}

	/**
	 * Calcula o numero de jogadas consecutivas que uma jogada permite
	 * @param j
	 * @return o numero de jogadas consecutivas
	 */
	public int getNumSequencia(){
		int max = 1;
		if(getSequencia() == null ||
				getSequencia().isEmpty()){
			return max;
		}
		for(Jogada j1 : getSequencia()){
			int i = j1.getNumSequencia() + 1;
			if (i > max)
				max = i;
		}
		return max;
	}
}
