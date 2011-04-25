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
	protected int sx; //sentido movimentacao do X
	private ArrayList<Jogada> jp; // jogadas possiveis

	public Jogador(Tabuleiro tabuleiro, boolean cor) {
		this.tabuleiro = tabuleiro;
		this.cor = cor;
		if(!cor){
			sx = -1;
		} else {
			sx = 1;
		}
	}

	/**
	 * Dado uma peca e as cordenadas destino executa a jogada
	 *
	 * O metodo verifica se jogada e valida e a executa no tabuleiro,
	 * verifica se ha outras jogadas possiveis depois de uma peca comer outra
	 * e caso contrario atribui a vez ao outro jogador
	 *
	 * @param t Tabuleiro
	 * @param p Peca a ser movida
	 * @param x Cordenada x destino da peca p
	 * @param y Cordenada y destino da peca p
	 * @return true se a jogada pode ser concluida, false do contrario
	 */
	public boolean joga(Tabuleiro t, Peca p, int x, int y) {
		if (t.getVez() != this.cor) {
			//nao eh a vez dele
			return false;
		}
		if(jp == null){
			// caso em que ha dois ou mais movimentos para comer
			jp = mapeiaTodasJogadas(t, cor);
		}
		for(Jogada j : jp){
			if(j.correspondeA(p, x, y)){
				//achou uma jogada permitida correspondente
				t.executaJogada(j);
				if(j.haProxima()){
					jp = j.getProximasObrigatorias();
				} else {
					jp = null;
					t.passaVez(this);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Lista todas as jogadas possiveis de um jogador
	 * @param t o tabuleiro
	 * @param cor a cor do jogador
	 * @return lista de todas as jogadas possiveis do jogador
	 */
	public ArrayList<Jogada> mapeiaTodasJogadas(Tabuleiro t, boolean cor) {
		ArrayList<Jogada> jogadas = new ArrayList<Jogada>();
		ArrayList<Peca> pecas = t.getListaPecas(cor);
		
		for (Peca p : pecas) {
			jogadas.addAll(mapeiaJogadaPeca(t, p));
		}
		return jogadas;
	}

	/**
	 * Mapeia todas as jogadas de uma peca em dado tabuleiro
	 *
	 * Criada para o caso em que uma peca come outra e so ha interesse
	 * de mapear as proximas jogadas possiveis daquela peca
	 *
	 * @param t Tabuleiro
	 * @param p Peca
	 * @return A lista de jogadas possiveis da peca
	 */
	private ArrayList<Jogada> mapeiaJogadaPeca(Tabuleiro t,	Peca p){
		boolean jogadaObrigatoria = false;
		ArrayList<Jogada> jogadas = new ArrayList<Jogada>();
		ArrayList<Jogada> jogadasObrigatorias = new ArrayList<Jogada>();
		Jogada[] j = new Jogada[4];
		j[0] = montaJogada(t, p, sx, -1);
		j[1] = montaJogada(t, p, sx, 1);
		if (p.getDama()) {
			j[2] = montaJogada(t, p, -sx, -1);
			j[3] = montaJogada(t, p, -sx, 1);
		}
		for (int i = 0; i < 4; i++) {
			if (j[i] != null && j[i].isObrigatoria()) {
				jogadaObrigatoria = true;
				/**
				 *  calcula se ha jogada seguinte obrigatoria, o metodo clone
				 *  eh chamado pois o tabuleiro deve ser alterado para calcular
				 *  as jogadas possiveis depois de uma peca comer outra
				 */
				Tabuleiro t1 = t.clone();
				t1.executaJogada(j[i]);
				ArrayList<Jogada> proximas = mapeiaJogadaPeca(t1, j[i].getP());
				if(!proximas.isEmpty() && proximas.get(0).isObrigatoria()){
					//ha proxima jogada obrigatoria
					j[i].setProxima(proximas);
				}
				jogadasObrigatorias.add(j[i]);
			} else {
				jogadas.add(j[i]);
			}
		}
		if(jogadaObrigatoria){
			return jogadasObrigatorias;
		}
		return jogadas;
	}

	// sx = sentido de x, sy = sentido de y
	private Jogada montaJogada(Tabuleiro t, Peca p, int sx, int sy) {
		int xD = p.getX() + sx;
		int yD = p.getY() + sy;
		if (casaValida(xD, yD)) {
			if (t.getCasa(xD, yD) == null) {
				return new Jogada(p, null, xD, yD);
			} else if (!(t.getCasa(xD, yD).getC() == p.getC())) {
				//peca adjacente e de cor diferente
				if (casaValida(xD + sx, yD + sy)
						&& t.getCasa(xD + sx, yD + sy) == null) {
					return new Jogada(p, t.getCasa(xD, yD), xD + sx, yD + sy);
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

	public Tabuleiro getTabuleiro() {
		return this.tabuleiro;
	}

	public boolean getCor() {
		return this.cor;
	}
}
