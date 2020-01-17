import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {

	public static void main(String[] args) throws Exception {
		new Main().main();
	}

	private JFrame frame = new JFrame();
	private JTextField prevTextFocus;
	private SOLVINGSTATE solvingState = SOLVINGSTATE.NONE;
	private JTextField[][] grid = new JTextField[9][9];
	private Font font = new Font("Verdana", Font.BOLD, 20);
	private int sudokuX = 13;
	private int sudokuY = 12;
	private Color gray = new Color(240, 240, 240);

	public void main() throws Exception {
		//Fenster Titel setzen
		frame.setTitle("Sudoku solver");
		//Fenster größe nicht veränderbar
		frame.setResizable(false);
		//Fenster an der Position x50 y50 erzeugen
		frame.setLocation(50, 50);
		//Fenster sichtbar machen
		frame.setVisible(true);
		//Fenster Design passend zum OS Design setzen
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		//Fenster ohne vordefiniertes Layout erzeugen
		frame.setLayout(null);

		//Panel mit den Elementen erzeugen
		JPanel panel = createGridPanel();

		//Wir benötigen ein LayerUI um mit Graphics2D linien zu zeichnen
		LayerUISudokuGrid sudokuGrid = new LayerUISudokuGrid(sudokuX, sudokuY);
		JLayer<JComponent> jlayer = new JLayer<>(panel, sudokuGrid);
		jlayer.setBounds(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
		frame.add(jlayer);

		//Fenster größe setzen
		frame.setMinimumSize(new Dimension(300, 400));
		//frame.pack();
		//Fenster bei X knopf schließen
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}

	private JPanel createGridPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);

		int posX = sudokuX;
		int posY;
		//Swap this everytime a new field is added to the grid so you can change the color of it
		boolean colorswitch = false;

		//9x9 loop
		for (int x = 0; x < 9; x++) {
			posY = 12;
			for (int y = 0; y < 9; y++) {

				//Rechteckiges Textfeld erzeugen, mit custom font
				JTextField field = new JTextField();
				field.setBounds(posX, posY, 30, 30);
				field.setHorizontalAlignment(JTextField.CENTER);
				field.setFont(font);

				colorswitch = !colorswitch;
				if (colorswitch) {
					field.setBackground(gray);
				}

				//Focus listener für das focusGained Event
				field.addFocusListener(new FocusListener() {
					@Override
					public void focusLost(FocusEvent e) {
					}

					@Override
					public void focusGained(FocusEvent e) {
						//Die variable prevTextFocus auf das angeklickte feld setzen
						prevTextFocus = field;

						//Wenn sich das grid im gelösten zustand befindet und reingeklickt wird, soll die lösung wieder verschwinden
						if (solvingState == SOLVINGSTATE.SOLVED) {
							boolean colorswitch = false;
							for (int x = 0; x < 9; x++) {
								for (int y = 0; y < 9; y++) {
									colorswitch = !colorswitch;
									JTextField field = grid[x][y];
									if (field.getBackground().equals(Color.WHITE) || field.getBackground().equals(gray)) {
										field.setText("");
									} else {
										field.setBackground(colorswitch ? Color.WHITE : gray);
									}
								}
							}
							solvingState = SOLVINGSTATE.NONE;
						}

						//Wenn sich das grid im ungültigen zustand befindet und reingeklickt wird, sollen die error verschwinden
						if (solvingState == SOLVINGSTATE.INVALID) {
							boolean colorswitch = false;
							for (int x = 0; x < 9; x++) {
								for (int y = 0; y < 9; y++) {
									colorswitch = !colorswitch;
									JTextField field = grid[x][y];
									if (field.getBackground().equals(Color.RED)) {
										field.setBackground(colorswitch ? Color.WHITE : gray);
									}
								}
							}
							solvingState = SOLVINGSTATE.NONE;
						}
					}
				});

				//Key listener um abzuprüfen welche taste gedrückt ist
				field.addKeyListener(new KeyListener() {
					@Override
					public void keyTyped(KeyEvent e) {
						//Feld inhalt auf eingegebene Zahl setzen
						int input = Character.getNumericValue(e.getKeyChar());
						if (!(input >= 1 && input <= 9)) {
							e.consume();
						} else {
							field.setText("");
						}
					}

					@Override
					public void keyReleased(KeyEvent e) {
					}

					@Override
					public void keyPressed(KeyEvent e) {

						int x = field.getX();
						int y = field.getY();
						int newX, newY;
						//Bei drücken von pfeiltasten soll entsprechend der focus sich bewegen
						switch (e.getKeyCode()) {
							case 38: // pfeiltaste hoch
								//Prüfungen ob der tastendruck aus dem grid heraus geht, wenn ja die position auf das gegenüberliegende setzen
								newY = y - 30;
								if (newY < 10)
									newY = 252;
								//Erstes getComponentAt gibt JLayer zurück, zeites das Panel und das dritte das Textfeld welches wir wollen
								frame.getContentPane().getComponentAt(0, 0).getComponentAt(0, 0)
										.getComponentAt(x, newY).requestFocus();
								break;
							case 39: // pfeiltaste rechts
								newX = x + 30;
								if (newX > 253)
									newX = 13;
								frame.getContentPane().getComponentAt(0, 0).getComponentAt(0, 0)
										.getComponentAt(newX, y).requestFocus();
								System.out.println("Rechts fokus");
								break;
							case 37: // pfeiltaste links
								newX = x - 30;
								if (newX < 13)
									newX = 253;
								frame.getContentPane().getComponentAt(0, 0).getComponentAt(0, 0)
										.getComponentAt(newX, y).requestFocus();
								break;
							case 40: // pfeiltaste runter
								newY = y + 30;
								if (newY > 252)
									newY = 12;
								frame.getContentPane().getComponentAt(0, 0).getComponentAt(0, 0)
										.getComponentAt(x, newY).requestFocus();
								System.out.println("Runter fokus");
								break;
						}
					}
				});
				grid[x][y] = field;
				panel.add(field);
				posY += 30;
			}
			posX += 30;
		}

		//Tasten für 1-9 Nummern hinzufügen
		posX = sudokuX;
		for (int i = 0; i < 9; i++) {
			JButton button = new JButton(String.valueOf(i + 1));
			button.setBorder(null);
			button.setFocusPainted(false);
			button.setRolloverEnabled(true);
			button.setBounds(posX + (i * 30), 290, 30, 30);
			button.addActionListener(e -> {
				JTextField c = prevTextFocus;

				if (c != null) {
					c.setText(button.getText());
				}
			});
			panel.add(button);
		}

		//Solve button
		JButton button = new JButton("Solve");
		button.setBounds(150 - (80 / 2), 325, 80, 35);
		button.addActionListener(e -> {
			//solving

			Thread solvingThread = new Thread(() -> {
				BacktrackingSolver solver = new BacktrackingSolver(grid);
				if (!solver.checkValidity()) {
					solvingState = SOLVINGSTATE.INVALID;
					JOptionPane.showMessageDialog(frame, "Sudoku not valid.");
				} else {

					for (int x = 0; x < 9; x++) {
						for (int y = 0; y < 9; y++) {
							JTextField field = grid[x][y];
							if (!field.getText().isEmpty()) {
								field.setBackground(Color.GRAY);
							}
						}
					}

					solvingState = SOLVINGSTATE.SOLVED;
					try {
						solver.solve();
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			});
			solvingThread.start();

		});
		panel.add(button);
		return panel;
	}

	public enum SOLVINGSTATE {
		SOLVED, INVALID, NONE;
	}

}


