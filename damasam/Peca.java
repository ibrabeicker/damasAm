/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package damasam;

/**
 *
 * @author ibrahim
 */
public class Peca {

	private boolean c;
	private Boolean dama;
	private int x, y;

	public Peca(boolean c, int x, int y) {
		this.c = c;
		this.dama = false;
		this.x = x;
		this.y = y;
	}

	public boolean getC() {
		return c;
	}

	public Boolean getDama() {
		return dama;
	}

	public void setDama(Boolean dama) {
		this.dama = dama;
	}

	public void move(int x, int y){
		this.x = x;
		this.y = y;
		//System.err.println("Peca "+this.toString()+" -> "+x+":"+y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public Peca clone(){
		Peca p = new Peca(this.c, this.x, this.y);
		return p;
	}

}
