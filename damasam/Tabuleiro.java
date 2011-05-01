/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package damasam;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ibrahim
 */
public class Tabuleiro implements Subject, Cloneable, Serializable{

	private peca[][] tabuleiro;
	private boolean vez; //vez de quem jogar
	private ArrayList<Observer> observador;
	public enum peca{VERMELHA, PRETA, DAMA_VERMELHA, DAMA_PRETA};
	private boolean terminado = false;

	/* pecas pretas = false
	 * sentido pra cima
	 */

	public Tabuleiro() {
		this.observador = new ArrayList<Observer>();
		this.tabuleiro = new peca[8][8];
		this.vez = true;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					tabuleiro[i][j] = peca.VERMELHA;
				}
			}
		}
		for (int i = 5; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					tabuleiro[i][j] = peca.PRETA;
				}
			}
		}
	}

	/**
	 * Recebe uma jogada J e a executa
	 * @param j
	 */
	public void executaJogada(Jogada j){
		tabuleiro[j.getxDestino()][j.getyDestino()] = tabuleiro[j.getxOrigem()][j.getyOrigem()];
		tabuleiro[j.getxOrigem()][j.getyOrigem()] = null;
		if(j.isObrigatoria()){
			int xComida = (j.getxOrigem() + j.getxDestino())/2;
			int yComida = (j.getyOrigem() + j.getyDestino())/2;
			tabuleiro[xComida][yComida] = null;
		}
		if(j.getxDestino() == 7 || j.getxDestino() == 0){
			setDama(j.getxDestino(), j.getyDestino());
		}
		notifyObservers();
	}

	public ArrayList<Jogada> listaJogadasPossiveis(boolean cor){
		ArrayList<Jogada> obrigatorias = new ArrayList<Jogada>();
		ArrayList<Jogada> js = new ArrayList<Jogada>();
		for(int i = 0; i< 8; i++){
			for(int j = 0; j < 8; j++){
				if (isPecaCor(cor, i, j)){
					js.addAll(listaJogadasPeca(i, j));
				}
			}
		}
		for (Jogada j : js){
			if(j.isObrigatoria()){
				obrigatorias.add(j);
			}
		}
		if(!obrigatorias.isEmpty()){
			return obrigatorias;
		}
		return js;
	}

	public ArrayList<Jogada> listaJogadasPeca(int x, int y){
		ArrayList<Jogada> jogadas = new ArrayList<Jogada>();
		ArrayList<Jogada> obrigatoria = new ArrayList<Jogada>();
		int sx;
		if(vez){
			sx = 1;
		} else {
			sx = -1;
		}
		Jogada[] direcao = new Jogada[4];
		direcao[0] = montaJogada(x, y, sx, 1);
		direcao[1] = montaJogada(x, y, sx, -1);
		if(isDama(x,y)){
			direcao[2] = montaJogada(x, y, -sx, 1);
			direcao[3] = montaJogada(x, y, -sx, -1);
		}
		for(Jogada j : direcao){
			if(j != null){
				if(j.isObrigatoria()){
					Tabuleiro tClone = this.clone();
					tClone.executaJogada(j);
					//tClone.imprimeTabuleiro();
					ArrayList<Jogada> seguinte = tClone.listaJogadasPeca(j.getxDestino(), j.getyDestino());
					if(!seguinte.isEmpty() && seguinte.get(0).isObrigatoria()){
						j.setSequencia(seguinte);
					}
					obrigatoria.add(j);
				}
				else{
					jogadas.add(j);
				}
			}
		}
		if(!obrigatoria.isEmpty())
			return obrigatoria;
		return jogadas;
	}

	public Jogada montaJogada(int x, int y, int sx, int sy) {
		int xD = x + sx;
		int yD = y + sy;
		if (casaValida(xD, yD)) {
			if (tabuleiro[xD][yD] == null) {
				return new Jogada(x, y, xD, yD);
			} else if (isPecaCor(!vez, xD, yD)) {
				//peca adjacente e de cor diferente
				if (casaValida(xD + sx, yD + sy)
						&& tabuleiro[xD + sx][yD + sy] == null) {
					return new Jogada(x, y, xD + sx, yD + sy);
				}
			}
		}
		return null;
	}

	private boolean casaValida(int x, int y) {
		if (x >= 0 && x < 8 && y >= 0 && y < 8) {
			return true;
		}
		return false;
	}

	public peca getCasa(int x, int y){
		return tabuleiro[x][y];
	}

	public boolean isPecaCor(boolean b, int x, int y){
		if(b){
			return (tabuleiro[x][y] == peca.VERMELHA ||
					tabuleiro[x][y] == peca.DAMA_VERMELHA);
		} else {
		return (tabuleiro[x][y] == peca.PRETA ||
					tabuleiro[x][y] == peca.DAMA_PRETA);
		}
	}

	public void setDama(int x, int y){
		if(tabuleiro[x][y] == peca.VERMELHA)
			tabuleiro[x][y] = peca.DAMA_VERMELHA;
		else if(tabuleiro[x][y] == peca.PRETA)
			tabuleiro[x][y] = peca.DAMA_PRETA;
	}

	public boolean isDama(int x, int y){
		return (tabuleiro[x][y] == peca.DAMA_PRETA ||
				tabuleiro[x][y] == peca.DAMA_VERMELHA);
	}

	public boolean getVez(){
		return this.vez;
	}

	public void passaVez(){
		this.vez = !this.vez;
	}

	public void registerObserver(Observer o) {
		observador.add(o);
	}

	public void removeObserver(Observer o) {
		observador.remove(o);
	}

	public void notifyObservers() {
		if(observador == null || terminado){
			return;
		}
		for (Observer o : observador){
			o.update(this);
		}
	}

	public void notifyTerminationObservers() {
		if (!terminado) {
			terminado = true;
			for (Observer o : observador) {
				o.terminate();
			}
		}
	}

	public void declaraVencedor(boolean cor){
		notifyTerminationObservers();
	}

	

	@Override
	public Tabuleiro clone(){
		Tabuleiro t = new Tabuleiro();
		t.observador = null;
		if(this.vez)
			t.vez = true;
		else
			t.vez = false;
		t.tabuleiro = new peca[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				peca p = this.tabuleiro[i][j];
				if(p == null){
					t.tabuleiro[i][j] = null;
					continue;
				}
				switch(p){
					case VERMELHA: t.tabuleiro[i][j] = peca.VERMELHA; break;
					case PRETA: t.tabuleiro[i][j] = peca.PRETA; break;
					case DAMA_VERMELHA: t.tabuleiro[i][j] = peca.DAMA_VERMELHA; break;
					case DAMA_PRETA: t.tabuleiro[i][j] = peca.DAMA_PRETA; break;
				}
			}
		}
		return t;
	}

	public void imprimeTabuleiro() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (isPecaCor(true, i, j)) {
					System.out.print("V");
				} else if (isPecaCor(false, i, j)) {
					System.out.print("P");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

}
