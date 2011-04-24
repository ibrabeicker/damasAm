/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package damasam;

/**
 *
 * @author ibrahim
 */
public interface Observer {

	public void update(Tabuleiro tabuleiro);
	
	public void terminate();
}
