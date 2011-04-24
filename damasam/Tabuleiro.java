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
		tabuleiro[j.getP().getX()][j.getP().getY()] = null;
		tabuleiro[j.getxDestino()][j.getyDestino()] = j.getP();
		j.getP().move(j.getxDestino(), j.getyDestino());
		if(j.come()){
			Peca comida = j.getComida();
			tabuleiro[comida.getX()][comida.getY()] = null;
			pecas.remove(comida);
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

	public void fezJogada(Jogador jg){
		this.vez = !this.vez;
	}

	public void registerObserver(Observer o) {
		observador.add(o);
	}

	public void removeObserver(Observer o) {
		observador.remove(o);
	}

	public void notifyObservers() {
		for (Observer o : observador){
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
		Tabuleiro t = new Tabuleiro(this.tabuleiro.clone());
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
