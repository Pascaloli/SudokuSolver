import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class BacktrackingSolver {

	private JTextField[][] grid;

	public BacktrackingSolver(JTextField[][] grid) {
		this.grid = grid;
	}

	public boolean checkValidity() {
		boolean valid = true;

		//Check ob all reihen und spalten valid sind
		for (int i = 0; i < 9; i++) {
			//9 felder langes boolean array für rows
			boolean usedRowNumbers[] = new boolean[9];
			boolean usedColumnNumbers[] = new boolean[9];
			for (int j = 0; j < 9; j++) {
				int numberRow = getNumberInGrid(i, j);
				int numberColumn = getNumberInGrid(j, i);

				//Check reihe
				if (numberRow != 0) {
					//wenn usedRowNumbers[zahl im feld-1] schon true = sudoku ungültig
					if (usedRowNumbers[numberRow - 1]) {
						valid = false;
					} else
						usedRowNumbers[numberRow - 1] = true;
				}

				//Check spalte
				if (numberColumn != 0) {
					//wenn usedColumnNumbers[zahl im feld-1] schon true = sudoku ungültig
					if (usedColumnNumbers[numberColumn - 1]) {
						valid = false;
					} else
						usedColumnNumbers[numberColumn - 1] = true;
				}
			}
		}

		//checke jeden 3x3 block
		for (int block = 0; block < 9; block++) {
			boolean[] usedNumber = new boolean[9];

			for (int i = block / 3 * 3; i < block / 3 * 3 + 3; i++) {
				for (int j = block % 3 * 3; j < block % 3 * 3 + 3; j++) {

					int number = getNumberInGrid(i, j);
					if (number != 0) {
						if (usedNumber[number - 1]) {
							valid = false;
						} else
							usedNumber[number - 1] = true;
					}
				}
			}
		}


		return valid;
	}

	public boolean solve() throws InterruptedException {
		for (int column = 0; column < 9; column++) {
			for (int row = 0; row < 9; row++) {
				//Check if position in grid is unassigned
				if (getNumberInGrid(row, column) == 0) {
					//Loop through all "possible" numbers (1-9)
					for (int num = 1; num <= 9; num++) {
						//Check is number is safe to assign
						if (isAssignable(row, column, num)) {
							updateGUI(row, column, String.valueOf(num));

							//Hier kann man ein delay einbauen um die funktionsweise des algorithmus zu zeigen
							Thread.sleep(200);

							//recursive solving
							if (solve())
								return true;
							else {
								updateGUI(row, column, "");
							}
						}
					}
					return false;
				}
			}
		}
		return true;
	}

	private boolean isAssignable(int row, int column, int number) {
		//Check if Row is valid
		for (int i = 0; i < 9; i++) {
			if (getNumberInGrid(row, i) == number) {
				return false;
			}
		}

		//Check if Column is valid
		for (int i = 0; i < 9; i++) {
			if (getNumberInGrid(i, column) == number) {
				return false;
			}
		}

		//Check if Box is valid
		int xBox = row - (row % 3);
		int yBox = column - (column % 3);
		for (int x = xBox; x < xBox + 3; x++) {
			for (int y = yBox; y < yBox + 3; y++) {
				if (getNumberInGrid(x, y) == number)
					return false;
			}
		}

		//All previous checks didnt return, so the number being placed is valid
		return true;
	}

	//Return the number as int of the JTextField at the gived grid position
	public int getNumberInGrid(int x, int y) {
		String text = grid[x][y].getText();
		return text.isEmpty() ? 0 : Integer.parseInt(text);
	}

	public void updateGUI(int x, int y, String text) {
		//dass die änderung intern direkt da ist
		grid[x][y].setText(text);
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					//so dass der swing thread die änderung auch anzeigt
					grid[x][y].setText(text);
				}
			});
			return;
		}
	}
}
