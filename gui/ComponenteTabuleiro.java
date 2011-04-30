/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import damasam.Observer;
import damasam.Peca;
import damasam.Tabuleiro;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author ibrahim
 */
public class ComponenteTabuleiro extends JPanel implements Observer{

	private int boardDimension = 320;
	private int cellDimension = boardDimension / 8;
	private Tabuleiro board;
	private int selectedX = -1;
	private int selectedY = -1;
	private int mouseX;
	private int mouseY;
	private PlayerInterface pi;
	private BufferedImage redPiece, blackPiece, redKing, blackKing;
	private boolean movimenting;

	public ComponenteTabuleiro() {
		super();
		this.setMinimumSize(new Dimension(boardDimension, boardDimension));
		this.setMaximumSize(new Dimension(boardDimension, boardDimension));
		try {
			this.blackKing = ImageIO.read(new File("blackKing.png"));
			this.blackPiece = ImageIO.read(new File("blackPiece.png"));
			this.redKing = ImageIO.read(new File("redKing.png"));
			this.redPiece = ImageIO.read(new File("redPiece.png"));
		} catch (IOException ex) {
			Logger.getLogger(ComponenteTabuleiro.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}

	public void setTabuleiro(Tabuleiro t) {
		this.board = t;
	}

	public void setPlayerInterface(PlayerInterface pi){
		this.pi = pi;
	}

	@Override
	protected void paintComponent(Graphics g) {

		//desenha o tabuleiro sem pecas
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(Color.WHITE);
				}
				g.fillRect(i * cellDimension,
						j * cellDimension,
						cellDimension,
						cellDimension);
//				if(i == selectedX && j == selectedY){
//					g.setColor(Color.YELLOW);
//					g.fillRect(i * cellDimension + 2,
//						j * cellDimension + 2,
//						cellDimension - 4,
//						cellDimension - 4);
//				}
			}
		}

		if (board == null) {
			return;
		}

		//desenha pecas
		Peca beingDragged = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++){				
				Peca p = board.getCasa(i, j);
				if(p == null){
					continue;
				}
				if(i == selectedY && j == selectedX){
					beingDragged = p;
					continue;
				}
				if (p.getC() && p.getDama())
						g.drawImage(redKing,
								j * cellDimension,
								i * cellDimension,
								this);
				else if(p.getC() && !p.getDama())
						g.drawImage(redPiece,
								j * cellDimension,
								i * cellDimension,
								this);
				else if (!p.getC() && p.getDama())
						g.drawImage(blackKing,
								j * cellDimension,
								i * cellDimension,
								this);
				else
						g.drawImage(blackPiece,
								j * cellDimension,
								i * cellDimension,
								this);	
			}
		}
		if(beingDragged != null){
			if (beingDragged.getC() && beingDragged.getDama())
						g.drawImage(redKing,
								mouseX - (cellDimension/2),
								mouseY - (cellDimension/2),
								this);
				else if(beingDragged.getC() && !beingDragged.getDama())
						g.drawImage(redPiece,
								mouseX - (cellDimension/2),
								mouseY - (cellDimension/2),
								this);
				else if (!beingDragged.getC() && beingDragged.getDama())
						g.drawImage(blackKing,
								mouseX - (cellDimension/2),
								mouseY - (cellDimension/2),
								this);
				else
						g.drawImage(blackPiece,
								mouseX - (cellDimension/2),
								mouseY - (cellDimension/2),
								this);
		}


	}

	public void update(Tabuleiro tabuleiro) {
		this.repaint();
	}

	public void terminate() {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	void mousePressed(MouseEvent evt) {
		selectedX = evt.getX()/cellDimension;
		selectedY = evt.getY()/cellDimension;
		if(board.getCasa(selectedY, selectedX) == null){
			selectedX = -1;
			selectedY = -1;
		}
	}

	void mouseReleased(MouseEvent evt) {
		int x = evt.getX()/cellDimension;
		int y = evt.getY()/cellDimension;
		pi.makePlay(selectedY, selectedX, y, x);
		selectedX = -1;
		selectedY = -1;
		repaint();
	}

	void mouseDragged(MouseEvent evt) {
		if(selectedX > 0 && selectedY > 0){
			mouseX = evt.getX();
			mouseY = evt.getY();
			repaint();
		}
	}
}
