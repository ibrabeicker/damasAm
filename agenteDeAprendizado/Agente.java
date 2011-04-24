/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agenteDeAprendizado;

import damasam.Jogada;
import damasam.Jogador;
import damasam.Observer;
import damasam.Peca;
import damasam.Tabuleiro;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author ibrahim
 */
public class Agente implements Observer {

	private Jogador jogador;
	private boolean cor;
	private double[] w;
	private Tabuleiro tabuleiro;
	private double mi = 0.1;

	public Agente(Jogador jogador) {
		this.jogador = jogador;
		this.tabuleiro = jogador.getTabuleiro();
		this.cor = jogador.getCor();
		this.w[0] = 10;
		this.w[1] = 10;
		this.w[2] = -10;
		this.w[3] = 20;
		this.w[4] = -20;
		this.w[5] = -15;
		this.w[6] = 15;
	}

	private void avaliaJogo(ArrayList<Tabuleiro> jogoCompleto) {
		Collections.reverse(jogoCompleto);
		ArrayList<EstadoTabuleiro> estados = new ArrayList<EstadoTabuleiro>();
		Tabuleiro t = jogoCompleto.get(0);

		//avalia o ultimo estado para determinar se ganhou ou perdeu ou empatou
		int[] x = new int[]{1,
			getNAmigas(t),
			getNInimigas(t),
			getDamasAmigas(t),
			getDamasInimigas(t),
			getAmigasAmeacadas(t),
			getInimigasAmeacadas(t)};

		if (x[1] == 0) {
			estados.add(new EstadoTabuleiro(x,-100));
		} else if (x[2] == 0) {
			estados.add(new EstadoTabuleiro(x,100));
		} else {
			estados.add(new EstadoTabuleiro(x,0));
		}

		//primeira correcao dos pesos
		double vAproximado = calculaVAproximado(x, w);
		double diferenca = estados.get(0).getScore() - vAproximado;

		for(int i = 0; i< 7; i++){
			w[i] = w[i] * diferenca * x[i];
		}
		
		for (int i = 1; i < jogoCompleto.size(); i++) {
			t = jogoCompleto.get(i);
			x = new int[]{1,
			getNAmigas(t),
			getNInimigas(t),
			getDamasAmigas(t),
			getDamasInimigas(t),
			getAmigasAmeacadas(t),
			getInimigasAmeacadas(t)};

			//neste momento ele pega o vAproximado anterior como score atual
			estados.add(new EstadoTabuleiro(x, vAproximado));

			//vAproximado eh calculado para o estado atual
			vAproximado = calculaVAproximado(x, w);
			diferenca = estados.get(i-1).getScore() - vAproximado;

			for (i = 0; i < 7; i++) {
				w[i] = w[i] * diferenca * x[i];
			}
		}
	}

	private double calculaVAproximado(int[] x, double[] w){
		int tam = x.length;
		double resultado = 0;
		for (int i = 0; i < tam; i++){
			resultado += x[i] * w[i];
		}
		return resultado;
	}

	private int getNAmigas(Tabuleiro t) {
		return t.getListaPecas(cor).size();
	}

	private int getNInimigas(Tabuleiro t) {
		return t.getListaPecas(!cor).size();
	}

	private int getAmigasAmeacadas(Tabuleiro t) {
		t = t.clone();
		ArrayList<Jogada> jogadas = jogador.mapeiaTodasJogadas(t, !this.cor);
		if (!jogadas.isEmpty() && jogadas.get(0).come()) {
			return jogadas.size();
		} else {
			return 0;
		}
	}

	private int getInimigasAmeacadas(Tabuleiro t) {
		t = t.clone();
		ArrayList<Jogada> jogadas = jogador.mapeiaTodasJogadas(t, this.cor);
		if (!jogadas.isEmpty() && jogadas.get(0).come()) {
			return jogadas.size();
		} else {
			return 0;
		}
	}

	private int getDamasAmigas(Tabuleiro t) {
		int cont = 0;
		for (Peca p : t.getListaPecas(this.cor)) {
			if (p.getDama()) {
				cont++;
			}
		}
		return cont;
	}

	private int getDamasInimigas(Tabuleiro t) {
		int cont = 0;
		for (Peca p : t.getListaPecas(!this.cor)) {
			if (p.getDama()) {
				cont++;
			}
		}
		return cont;
	}

	public void terminate() {
	}

	public void update(Tabuleiro tabuleiro) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
