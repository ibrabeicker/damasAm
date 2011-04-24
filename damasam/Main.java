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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Peca> conts = new ArrayList<Peca>();
		Peca p = new Peca(true, 0, 0);
		conts.add(p);
		p.move(1, 1);
		conts.add(p);
		p.move(2, 0);
		conts.add(p);

		for (Peca p1 : conts){
			System.out.println(p.getX() + " - "+p.getY());
		}
    }

}
