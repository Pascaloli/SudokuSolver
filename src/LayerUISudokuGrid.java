import javax.swing.JComponent;
import javax.swing.plaf.LayerUI;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class LayerUISudokuGrid extends LayerUI<JComponent> {

	private int x;
	private int y;

	public LayerUISudokuGrid(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);

		//Dicke Linien in der GUI
		Graphics2D g2 = (Graphics2D) g;
		//Breite auf 2 setzen
		g2.setStroke(new BasicStroke(2));
		int posX = x, posY = y;
		//Y Linien
		for (int i = 1; i <= 4; i++) {
			Line2D line1 = new Line2D.Float(posX, posY + 1, posX, posY + 269);
			g2.draw(line1);
			posX += 90;
		}
		//X Linien
		posX = 13;
		for (int i = 1; i <= 4; i++) {
			Line2D line1 = new Line2D.Float(posX, posY, posX + 270, posY);
			g2.draw(line1);
			posY += 90;
		}
	}
}