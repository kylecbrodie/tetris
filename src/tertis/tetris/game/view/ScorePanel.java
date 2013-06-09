package tertis.tetris.game.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ScorePanel extends JPanel {
    
    public ScorePanel() {
        _format = new java.text.DecimalFormat("#####");
        _format.setMaximumIntegerDigits(_numberCount);
        _format.setMinimumIntegerDigits(_numberCount);        
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        String str = _format.format(_score);
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        g.setColor(Color.RED);
        g.setFont(new Font("Courier", Font.BOLD, 25));
        FontMetrics fm = g.getFontMetrics();
        int fw = fm.stringWidth(str);
        int fh = fm.getAscent();
        g.drawString(str, w/2 - fw/2, h/2 + fh/4);
    }

    public void setScore(int score) {
        _score = score;
        repaint();
    }

    private int _score = 0;
    private static final int _numberCount = 5;
    private java.text.DecimalFormat _format;
}