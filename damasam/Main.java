/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package damasam;

import agenteDeAprendizado.Agente;
import agenteDeAprendizado.GravadorDosJogos;
import gui.Janela;
import gui.PlayerController;
import java.io.IOException;

/**
 *
 * @author ibrahim
 */
public class Main {

	private static Tabuleiro t;

	public static void empate(){
		t.declaraVencedor(true);
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

//		ObjectInputStream oin = new ObjectInputStream(new FileInputStream("pesos.object"));
//		double x[] = (double[]) oin.readObject();
//		for (double d : x){
//			System.out.println(d);
//		}
//		oin.close();


		Janela j = new Janela();

		t = new Tabuleiro();
		Jogador v = new Jogador(t, true);
		Jogador p = new Jogador(t, false);
		PlayerController pc = new PlayerController(p, v);
		//j.setPlayerInterface(pc);
		Agente age = new Agente(v,"pesos.object");
		Agente age2 = new Agente(p,"pesos.object2");
		age2.setNomeMi(0.05);
		
		GravadorDosJogos logger = new GravadorDosJogos();
		t.registerObserver(logger);
		t.registerObserver(j.getGUI());
		t.registerObserver(age);
		t.registerObserver(age2);

		j.getGUI().setPlayerInterface(pc);
		j.setTabuleiro(t);
		j.setVisible(true);
		
		t.notifyObservers();


		

//		t[] array = new t[]{t.A,t.B,t.C};
//		t pont = array[1];
//		pont = t.C;
//		System.out.println(array[1]);
    }

}
