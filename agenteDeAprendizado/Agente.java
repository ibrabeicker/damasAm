/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenteDeAprendizado;

import damasam.Jogada;
import damasam.Jogador;
import damasam.Observer;
import damasam.Tabuleiro;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ibrahim
 */
public class Agente implements Observer {

	private Jogador jogador;
	private boolean cor;
	private int[] w;
	private double mi = 0.0001; //valor de reducao do erro
	private String arquivoObjetoPesos = "pesos.object";
	private int cont = 0;

	public Agente(Jogador jogador, String arquivo) {
		this.arquivoObjetoPesos = arquivo;
		this.jogador = jogador;
		this.cor = jogador.getCor();
		try {
			ObjectInputStream oin = new ObjectInputStream(
					new FileInputStream(arquivoObjetoPesos));
			this.w = (int[]) oin.readObject();
			oin.close();
		} catch (IOException ex) {
			System.err.println(ex.toString());
			this.w = new int[]{10, 10, -10, 20, -20, -15, 15};
		} catch (ClassNotFoundException ex1) {
			System.err.println("Classe nao encontrada");
		}
		for (int d : w) {
			System.err.println(d);
		}
	}

	public void setNomeMi(double mi) {
		this.mi = mi;
	}

	/**
	 * Avalia um jogo pronto para ajustar os pesos
	 * @param jogoCompleto ArrayList de estados do tabuleiro de um jogo completo
	 */
	public void funcaoAprendizado(ArrayList<Tabuleiro> jogoCompleto) {
		Collections.reverse(jogoCompleto);
		Tabuleiro t = jogoCompleto.get(0);

		int x[];
		int vAproximado = 0, vTrain, diferenca;
		for (int i = 0; i < jogoCompleto.size(); i++) {
			t = jogoCompleto.get(i);
			x = new int[]{1,
						getNAmigas(t),
						getNInimigas(t),
						getDamasAmigas(t),
						getDamasInimigas(t),
						getAmigasAmeacadas(t),
						getInimigasAmeacadas(t)};

			if (i == 0) {
				if (x[1] == 0) {
					vAproximado = -100;
				} else if (x[2] == 0) {
					vAproximado = 100;
				} else {
					vAproximado = 0;
				}
			}

			//neste momento ele pega o vAproximado anterior como score atual
			vTrain = vAproximado;
			//estados.add(new EstadoTabuleiro(x, vTrain));

			//vAproximado eh calculado para o estado atual
			vAproximado = calculaVAproximado(x, w);


			diferenca = vTrain - vAproximado;

			for (int j = 0; j < 7; j++) {
				int pesoAntigo = w[j];
				w[j] = (int) (w[j] + mi * diferenca * x[j]);
				System.err.println("Peso w["+j+"] "+pesoAntigo+"->"+w[j]);
			}
		}
		System.err.println("aprendeu");
		try {
			ObjectOutputStream oout = new ObjectOutputStream(
					new FileOutputStream(arquivoObjetoPesos));
			oout.writeObject(w);
			oout.close();
		} catch (IOException ex) {
			Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void joga() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException ex) {
			Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
		}
		ArrayList<Jogada> possiveis = this.jogador.getTabuleiro().listaJogadasPossiveis(cor);
		if (possiveis.isEmpty()) {
			System.err.println("nao ha jogadas possiveis, "+ cont);
			cont++;
			jogador.getTabuleiro().declaraVencedor(cor);
			return;
		}
		Collections.shuffle(possiveis);
		int scoreMax = 0, score;
		Tabuleiro t1;
		Jogada escolhida = null;
		for (Jogada j : possiveis) {
			t1 = this.jogador.getTabuleiro().clone();
			t1.executaJogada(j);
			int[] x = new int[]{1,
				getNAmigas(t1),
				getNInimigas(t1),
				getDamasAmigas(t1),
				getDamasInimigas(t1),
				getAmigasAmeacadas(t1),
				getInimigasAmeacadas(t1)};
			score = calculaVAproximado(x, w);
			score = (int) score;
			if (scoreMax == 0 || score > scoreMax) {
				scoreMax = score;
				escolhida = j;
			}
		}
		jogador.joga(escolhida.getxOrigem(),
				escolhida.getyOrigem(),
				escolhida.getxDestino(),
				escolhida.getyDestino());
	}

	//private double calculaScore
	private int calculaVAproximado(int[] x, int[] w) {
		int tam = x.length;
		int resultado = 0;
		for (int i = 0; i < tam; i++) {
			resultado += x[i] * w[i];
		}
		return resultado;
	}

	private int getNAmigas(Tabuleiro t) {
		int cont = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (t.isPecaCor(cor, i, j)) {
					cont++;
				}
			}
		}
		return cont;
	}

	private int getNInimigas(Tabuleiro t) {
		int cont = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (t.isPecaCor(!cor, i, j)) {
					cont++;
				}
			}
		}
		return cont;
	}

	private int getAmigasAmeacadas(Tabuleiro t) {
		if(t.getVez() != jogador.getCor())
			return 0;
		ArrayList<Jogada> jogadas = t.listaJogadasPossiveis(!jogador.getCor());
		if (!jogadas.isEmpty() && jogadas.get(0).isObrigatoria()) {
			return jogadas.size();
		} else {
			return 0;
		}
	}

	private int getInimigasAmeacadas(Tabuleiro t) {
		if(t.getVez() != jogador.getCor())
			return 0;
		ArrayList<Jogada> jogadas = t.listaJogadasPossiveis(jogador.getCor());
		if (!jogadas.isEmpty() && jogadas.get(0).isObrigatoria()) {
			return jogadas.size();
		} else {
			return 0;
		}
	}

	private int getDamasAmigas(Tabuleiro t) {
		int cont = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (t.isDama(i, j) && t.isPecaCor(cor, i, j)) {
					cont++;
				}
			}
		}
		return cont;
	}

	private int getDamasInimigas(Tabuleiro t) {
		int cont = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (t.isDama(i, j) && t.isPecaCor(!cor, i, j)) {
					cont++;
				}
			}
		}
		return cont;
	}

	public void terminate() {
		System.err.println("Terminou");
		funcaoAprendizado(GravadorDosJogos.getInstance());
	}

	public void update(Tabuleiro tabuleiro) {
		if (tabuleiro.getVez() == this.cor) {
			joga();
		}
	}
}
