/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import damasam.Observer;
import damasam.Tabuleiro;
import damasam.Tabuleiro.peca;
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
public class ComponenteTabuleiro extends JPanel implements Observer {

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
			this.blackKing = ImageIO.read(new File("src/resources/blackKing.png"));
			this.blackPiece = ImageIO.read(new File("src/resources/blackPiece.png"));
			this.redKing = ImageIO.read(new File("src/resources/redKing.png"));
			this.redPiece = ImageIO.read(new File("src/resources/redPiece.png"));
		} catch (IOException ex) {
			Logger.getLogger(ComponenteTabuleiro.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void setTabuleiro(Tabuleiro t) {
		this.board = t;
	}

	public void setPlayerInterface(PlayerInterface pi) {
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
			}
		}

		if (board == null) {
			return;
		}

		//desenha pecas
		peca beingDragged = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (j == selectedX && i == selectedY) {
					beingDragged = board.getCasa(i, j);
					continue;
				}
				peca p = board.getCasa(i, j);
				if (p == null) {
					continue;
				}
				BufferedImage img = null;
				switch (p) {
					case VERMELHA:
						img = redPiece;
						break;
					case PRETA:
						img = blackPiece;
						break;
					case DAMA_VERMELHA:
						img = redKing;
						break;
					case DAMA_PRETA:
						img = blackKing;
						break;
				}
				g.drawImage(img,
						j * cellDimension,
						i * cellDimension,
						this);
			}
		}

		if (beingDragged != null) {
			BufferedImage img = null;
			switch (beingDragged) {
				case VERMELHA:
					img = redPiece;
					break;
				case PRETA:
					img = blackPiece;
					break;
				case DAMA_VERMELHA:
					img = redKing;
					break;
				case DAMA_PRETA:
					img = blackKing;
					break;
			}
			g.drawImage(img,
					mouseX - cellDimension / 2,
					mouseY - cellDimension / 2,
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
		selectedX = evt.getX() / cellDimension;
		selectedY = evt.getY() / cellDimension;
		if (board.getCasa(selectedY, selectedX) == null) {
			selectedX = -1;
			selectedY = -1;
		}
	}

	void mouseReleased(MouseEvent evt) {
		int x = evt.getX() / cellDimension;
		int y = evt.getY() / cellDimension;
		pi.makePlay(selectedY, selectedX, y, x);
		selectedX = -1;
		selectedY = -1;
		repaint();
	}

	void mouseDragged(MouseEvent evt) {
		if (selectedX > 0 && selectedY > 0) {
			mouseX = evt.getX();
			mouseY = evt.getY();
			repaint();
		}
	}
}
