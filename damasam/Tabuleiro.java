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
public class Tabuleiro implements Subject, Cloneable{

	private Peca[][] tabuleiro;
	private ArrayList<Peca> pecas;
	private boolean vez; //vez de quem jogar
	private ArrayList<Observer> observador;

	/* pecas pretas = false
	 * sentido pra cima
	 */

	public Tabuleiro() {
		this.observador = new ArrayList<Observer>();
		this.tabuleiro = new Peca[8][8];
		this.pecas = new ArrayList<Peca>();
		this.vez = false;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					Peca p = new Peca(true, i, j);
					tabuleiro[i][j] = p;
					pecas.add(p);
				}
			}
		}
		for (int i = 5; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					Peca p = new Peca(false, i, j);
					tabuleiro[i][j] = p;
					pecas.add(p);
				}
			}
		}
	}

	/**
	 * Cria um novo tabuleiro para o metodo clone()
	 *
	 * @param tabuleiro A matriz do tabuleiro
	 */
	public Tabuleiro(Peca[][] tabuleiro){
		this.tabuleiro = tabuleiro;
		this.pecas = new ArrayList<Peca>();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(this.tabuleiro[i][j] != null){
					pecas.add(tabuleiro[i][j]);
				}
			}
		}
	}

	/**
	 * Recebe uma jogada J e a executa
	 * @param j
	 */
	public void executaJogada(Jogada j){
		//System.err.println(this.toString() + " executando");
		Peca p = tabuleiro[j.getxOrigem()][j.getyOrigem()];
		tabuleiro[j.getxDestino()][j.getyDestino()] = p;
		p.move(j.getxDestino(), j.getyDestino());
		tabuleiro[j.getxOrigem()][j.getyOrigem()] = null;
		if(j.isObrigatoria()){
			int xComida = (j.getxOrigem() + j.getxDestino())/2;
			int yComida = (j.getyOrigem() + j.getyDestino())/2;
			Peca comida = tabuleiro[xComida][yComida];
			pecas.remove(comida);
			tabuleiro[xComida][yComida] = null;
		}
		if(j.getxDestino() == 7 || j.getxDestino() == 0){
			p.setDama(true);
		}
		notifyObservers();
	}

	public ArrayList<Peca> getListaPecas(boolean cor){
		ArrayList<Peca> listaPecas = new ArrayList<Peca>();
		for (Peca p : pecas){
			if (p.getC() == cor){
				listaPecas.add(p);
			}
		}
		return listaPecas;
	}

	public Peca getCasa(int x, int y){
		return tabuleiro[x][y];
	}

	public boolean getVez(){
		return this.vez;
	}

	public void passaVez(Jogador jg){
		this.vez = !this.vez;
	}

	public void registerObserver(Observer o) {
		observador.add(o);
	}

	public void removeObserver(Observer o) {
		observador.remove(o);
	}

	public void notifyObservers() {
		if(observador == null){
			return;
		}
		//this.imprimeTabuleiro();
		for (Observer o : observador){
			//System.err.println(o.getClass().toString());
			o.update(this);
		}
	}

	public void declaraVencedor(boolean cor){
		notifyTerminationObservers();
	}

	public void notifyTerminationObservers() {
		for (Observer o : observador){
			o.terminate();
		}
	}

	@Override
	public Tabuleiro clone(){
		Tabuleiro t = new Tabuleiro();
		t.observador = null;
		t.tabuleiro = new Peca[8][8];
		t.pecas = new ArrayList<Peca>();
		for(Peca p : this.pecas){
			Peca pClone = p.clone();
			t.pecas.add(pClone);
			t.tabuleiro[pClone.getX()][pClone.getY()] = pClone;
		}
		return t;
	}

	public void imprimeTabuleiro(){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(this.tabuleiro[i][j] != null
						&& !this.tabuleiro[i][j].getC()){
					System.out.print("P");
				} else if(this.tabuleiro[i][j] != null
						&& this.tabuleiro[i][j].getC()){
					System.out.print("V");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
