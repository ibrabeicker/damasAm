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
public class Jogador {

	/**
	 * jogador preto = false, movimenta pra cima
	 */
	protected boolean cor;
	private Tabuleiro tabuleiro;
	private ArrayList<Jogada> jp; // jogadas possiveis

	public Jogador(Tabuleiro tabuleiro, boolean cor) {
		this.tabuleiro = tabuleiro;
		this.cor = cor;
	}

	/**
	 * Dado uma peca e as cordenadas destino executa a jogada
	 *
	 * O metodo verifica se jogada e valida e a executa no tabuleiro,
	 * verifica se ha outras jogadas possiveis depois de uma peca comer outra
	 * e caso contrario atribui a vez ao outro jogador
	 *
	 * @param tabuleiro Tabuleiro
	 * @param p Peca a ser movida
	 * @param x Cordenada x destino da peca p
	 * @param y Cordenada y destino da peca p
	 * @return true se a jogada pode ser concluida, false do contrario
	 */
	public boolean joga(int xO, int yO, int x, int y) {
		if (tabuleiro.getVez() != this.cor) {
			return false;
		}
		if(jp == null || jp.isEmpty()){
			/**
			 * se jp nao for null entao ha jogadas que sao sequencia de
			 * jogadas anteriores que comeram uma peca
			 */
			jp = tabuleiro.listaJogadasPossiveis(this.cor);
			if(jp.isEmpty())
				tabuleiro.declaraVencedor(!cor);
		}
		for(Jogada j : jp){
			if(j.correspondeA(new Jogada(xO, yO, x, y))){
				//achou uma jogada permitida correspondente
				if(j.haSequencia()){
					System.err.println("ha sequencia");
					jp = j.getSequencia();
				} else {
					jp = null;
					tabuleiro.passaVez();
				}
				System.err.println(this.cor+" " + j.toString());
				tabuleiro.executaJogada(j);
				return true;
			}
		}
		return false;
	}
	
	public Tabuleiro getTabuleiro() {
		return this.tabuleiro;
	}

	public boolean getCor() {
		return this.cor;
	}
}
