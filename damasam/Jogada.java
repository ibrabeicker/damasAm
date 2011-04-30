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

	private int xOrigem, yOrigem, xDestino, yDestino;
	/**
	 * proximas obrigatorias faz uma estrutura de arvore com as jogadas que
	 * comem mais de 1 peca
	 */
	private ArrayList<Jogada> jogadasDaSequencia;

	public Jogada(int xOrigem, int yOrigem, int xDestino, int yDestino) {
		this.xOrigem = xOrigem;
		this.yOrigem = yOrigem;
		this.xDestino = xDestino;
		this.yDestino = yDestino;
	}

	public boolean correspondeA(Jogada j){
		if(j.getxOrigem() == xOrigem &&
				j.getyOrigem() == yOrigem &&
				j.getxDestino() == xDestino &&
				j.getyDestino() == yDestino)
			return true;
		return false;
	}

	public void setSequencia(ArrayList<Jogada> js){
		jogadasDaSequencia = js;
	}

	public int getxOrigem() {
		return xOrigem;
	}

	public int getyOrigem() {
		return yOrigem;
	}

	public int getxDestino() {
		return xDestino;
	}

	public int getyDestino() {
		return yDestino;
	}

	public boolean isObrigatoria(){
		if(xOrigem - xDestino == 2 || xOrigem - xDestino == -2)
			return true;
		return false;
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

	@Override
	public String toString(){
		String s = "";
		s += xOrigem+":"+yOrigem+" para "+xDestino+":"+yDestino;
		return s;
	}

	@Override
	public Jogada clone(){
		Jogada j = new Jogada(xOrigem, yOrigem, xDestino, yDestino);
		return j;
	}

	Peca getComida() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
