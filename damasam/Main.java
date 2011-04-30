/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package damasam;

import agenteDeAprendizado.Agente;
import agenteDeAprendizado.GravadorDosJogos;
import gui.ComponenteTabuleiro;
import gui.Janela;
import gui.PlayerController;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author ibrahim
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        ArrayList<Peca> conts = new ArrayList<Peca>();
//		Peca p = new Peca(true, 0, 0);
//		conts.add(p);
//		p.move(1, 1);
//		conts.add(p);
//		p.move(2, 0);
//		conts.add(p);
//
//		for (Peca p1 : conts){
//			System.out.println(p.getX() + " - "+p.getY());
//		}

//		JFrame j = new JFrame();
//		j.setMaximizedBounds(new Rectangle(320, 320));
//		j.setPreferredSize(new Dimension(360, 360));
//		j.getContentPane().add(new ComponenteTabuleiro());
//		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		j.setLocationRelativeTo(null);
//		j.pack();
//		j.setVisible(true);

		Janela j = new Janela();

		Tabuleiro t = new Tabuleiro();
		Jogador v = new Jogador(t, true);
		Jogador p = new Jogador(t, false);
		PlayerController pc = new PlayerController(p, v);
		//j.setPlayerInterface(pc);
		Agente age = new Agente(p);
		GravadorDosJogos logger = new GravadorDosJogos();
		t.registerObserver(logger);
		t.registerObserver(j.getGUI());
		t.registerObserver(age);
		
		
		j.getGUI().setPlayerInterface(pc);
		j.setTabuleiro(t);
		j.setVisible(true);
    }

}
