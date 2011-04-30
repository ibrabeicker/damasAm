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
	private double[] w;
	private double mi = 0.1; //valor de reducao do erro
	private String arquivoObjetoPesos = "pesos.object";
	private boolean jogando = true;

	public Agente(Jogador jogador) {
		this.jogador = jogador;
		this.cor = jogador.getCor();
		try {
			ObjectInputStream oin = new ObjectInputStream(
					new FileInputStream(arquivoObjetoPesos));
			this.w = (double[]) oin.readObject();
			oin.close();
		} catch (IOException ex) {
			this.w = new double[]{10,10,-10,20,-20,-15,15};
		} catch (ClassNotFoundException ex1) {
			System.err.println("Classe nao encontrada");
		}
	}

	/**
	 * Avalia um jogo pronto para ajustar os pesos
	 * @param jogoCompleto ArrayList de estados do tabuleiro de um jogo completo
	 */
	public void funcaoAprendizado(ArrayList<Tabuleiro> jogoCompleto) {
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
			w[i] = w[i] + mi * diferenca * x[i];
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
				w[i] = w[i] + mi * diferenca * x[i];
			}
		}
		try {
			ObjectOutputStream oout = new ObjectOutputStream(
					new FileOutputStream(arquivoObjetoPesos));
			oout.writeObject(w);
			oout.close();
		} catch (IOException ex) {
			Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	//private double calculaScore

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
		ArrayList<Jogada> jogadas = jogador.mapeiaTodasJogadas(t, !this.cor);
		if (!jogadas.isEmpty() && jogadas.get(0).isObrigatoria()) {
			// ha jogadas que comem uma peca
			int max = 1;
			for(Jogada j : jogadas){
				int i = j.getNumSequencia();
				if (i > max)
					max = i;
			}
			return max;
		} else {
			return 0;
		}
	}

	private int getInimigasAmeacadas(Tabuleiro t) {
		ArrayList<Jogada> jogadas = jogador.mapeiaTodasJogadas(t, this.cor);
		if (!jogadas.isEmpty() && jogadas.get(0).isObrigatoria()) {
			// ha jogadas que comem uma peca
			int max = 1;
			for(Jogada j : jogadas){
				int i = j.getNumSequencia();
				if (i > max)
					max = i;
			}
			return max;
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
		funcaoAprendizado(GravadorDosJogos.getInstance());
	}

	public void update(Tabuleiro tabuleiro) {
		if(tabuleiro.getVez() == this.cor){
			//TODO: calcular melhor jogada e jogar
		}
	}

	public void setJogando(boolean b){
		this.jogando = b;
	}
}
