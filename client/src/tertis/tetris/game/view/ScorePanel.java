package tertis.tetris.game.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DecimalFormat;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ScorePanel extends JPanel {
	
	private int score = 0;
	private static final int numberCount = 5;
	private DecimalFormat format;

	public ScorePanel() {
		format = new DecimalFormat("#####");
		format.setMaximumIntegerDigits(numberCount);
		format.setMinimumIntegerDigits(numberCount);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		String str = format.format(score);
		int w = this.getWidth();
		int h = this.getHeight();
		g.clearRect(0, 0, w, h);
		g.setColor(Color.RED);
		g.setFont(new Font("Courier", Font.BOLD, 25));
		FontMetrics fm = g.getFontMetrics();
		int fw = fm.stringWidth(str);
		int fh = fm.getAscent();
		g.drawString(str, w / 2 - fw / 2, h / 2 + fh / 4);
	}

	public void setScore(int score) {
		this.score = score;
		repaint();
	}
}