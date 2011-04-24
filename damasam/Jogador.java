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

	/*
	 * jogador preto = false, movimenta pra cima
	 */
	protected boolean cor;
	private Tabuleiro tabuleiro;
	protected int sx; //sentido movimentacao do X

	public Jogador(Tabuleiro tabuleiro, boolean cor) {
		this.tabuleiro = tabuleiro;
		this.cor = cor;
		if(cor){
			sx = -1;
		} else {
			sx = 1;
		}
	}

	public boolean joga(Tabuleiro t, Peca p, int x, int y) {
		if (t.getVez() != this.cor) {
			//nao eh a vez dele
			return false;
		}
		boolean jogaOutraVez = false;
		ArrayList<Jogada> jogadasPossiveis = mapeiaTodasJogadas(t, this.cor);
		if (jogadasPossiveis.isEmpty()) {
			t.declaraVencedor(!this.cor);
		}
		for (Jogada j : jogadasPossiveis) {
			if (j.correspondeA(p, x, y)) {
				t.executaJogada(j);
				if (j.come()) {
					jogadasPossiveis = mapeiaTodasJogadas(t, this.cor);
					if (!jogadasPossiveis.isEmpty()
							&& jogadasPossiveis.get(0).come()); else {
						jogaOutraVez = true;
					}
				}
				if (!jogaOutraVez) {
					t.fezJogada(this);
				}
				return true;
			}
		}
		return false;
	}

	public ArrayList<Jogada> mapeiaTodasJogadas(Tabuleiro t, boolean cor) {
		ArrayList<Jogada> jogadas = new ArrayList<Jogada>();
		ArrayList<Jogada> jogadasObrigatorias = new ArrayList<Jogada>();
		boolean jogadaObrigatoria = false;

		ArrayList<Peca> pecas = t.getListaPecas(cor);
		for (Peca p : pecas) {
			Jogada[] j = new Jogada[4];
			j[0] = montaJogada(t, p, sx, -1);
			j[1] = montaJogada(t, p, sx, 1);
			if (p.getDama()) {
				j[2] = montaJogada(t, p, -sx, -1);
				j[3] = montaJogada(t, p, -sx, 1);
			}
			for (int i = 0; i < 4; i++) {
				if (j[i] != null) {
					if (j[i].come()) {
						jogadaObrigatoria = true;
						jogadasObrigatorias.add(j[i]);
					} else {
						jogadas.add(j[i]);
					}
				}
			}
		}
		// fim for pecas
		if (jogadaObrigatoria) {
			return jogadasObrigatorias;
		} else {
			return jogadas;
		}
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
					return new Jogada(p, t.getCasa(xD, yD), xD + 2 * sx, yD + 2 * sy);
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
