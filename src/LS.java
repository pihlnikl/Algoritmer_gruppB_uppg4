//package tictactoe;

import java.awt.*;

// Class to represent Move objects
class Move {
    int val;   // Value of the move
    int row;   // Row and column coordinates
    int col;

    public Move(int v, int r, int c) {
        val = v;
        row = r;
        col = c;
    }

    public Move immediateCompWin() {
        for (int i = 1; i <= 9; i++) {
            int[] coordinates = getCoordinates(i);
            if (LS.isEmpty(coordinates[0], coordinates[1])) {
                LS.place(coordinates[0], coordinates[1], LS.COMPUTER);
                if (LS.checkResult() == LS.COMPUTER_WIN) {
                    LS.unplace(coordinates[0], coordinates[1]);
                    return new Move(LS.HUMAN_WIN, coordinates[0], coordinates[1]);
                }
                LS.unplace(coordinates[0], coordinates[1]);
            }
        }
        return null;
    }

    public Move immediateHumanWin() {
        for (int i = 1; i <= 9; i++) {
            int[] coordinates = getCoordinates(i);
            if (LS.isEmpty(coordinates[0], coordinates[1])) {
                LS.place(coordinates[0], coordinates[1], LS.HUMAN);
                if (LS.checkResult() == LS.HUMAN_WIN) {
                    LS.unplace(coordinates[0], coordinates[1]);
                    return new Move(LS.COMPUTER_WIN, coordinates[0], coordinates[1]);
                }
                LS.unplace(coordinates[0], coordinates[1]);
            }
        }
        return null;
    }

    public int[] getCoordinates(int position) {
        int[] coordinates = new int[2];
        // Get row (y) and column (x) of the position
        coordinates[0] = (position - 1) / LS.SIZE;
        coordinates[1] = (position - 1) % LS.SIZE;
        return coordinates;
    }

    public Move findCompMove(int alpha, int beta) {
        int responseValue;
        int value;
        int bestMove = -1;
        Move quickWinInfo;

        quickWinInfo = immediateCompWin();
        if (quickWinInfo != null) {
            return quickWinInfo;
        } else {
            value = alpha;
            for (int i = 1; i <= 9; i++) {
                int[] coordinates = getCoordinates(i);
                if (LS.isEmpty(coordinates[0], coordinates[1])) {
                    LS.place(coordinates[0], coordinates[1], LS.COMPUTER);
                    responseValue = findHumanMove(value, beta).val;
                    LS.unplace(coordinates[0], coordinates[1]);
                    if (responseValue > value) {
                        value = responseValue;
                        bestMove = i;
                    }

                    if (value >= beta) {
                        if (bestMove != -1) {
                            coordinates = getCoordinates(bestMove);
                            return new Move(value, coordinates[0], coordinates[1]);
                        } else {
                            return new Move(value, -1, -1);
                        }
                    }
                }
            }
        }
        if (bestMove != -1) {
            int[] coordinates = getCoordinates(bestMove);
            if (LS.isEmpty(coordinates[0], coordinates[1])) {
                return new Move(value, coordinates[0], coordinates[1]);
            }

        }
        for (int i = 1; i <= 9; i++) {
            int[] coordinates = getCoordinates(i);
            if (LS.isEmpty(coordinates[0], coordinates[1])) {
                return new Move(value, 1, 1);
            }
        }
        return new Move(LS.DRAW, -1, -1);
    }

    public Move findHumanMove(int compValue, int beta) {
        int responseValue;
        int value;
        int bestMove = -1;
        Move quickWinInfo;

        quickWinInfo = immediateHumanWin();
        if (quickWinInfo != null) {
            return quickWinInfo;
        } else {
            value = beta;
            for (int i = 1; i <= 9; i++) {
                int[] coordinates = getCoordinates(i);
                if (LS.isEmpty(coordinates[0], coordinates[1])) {
                    LS.place(coordinates[0], coordinates[1], LS.HUMAN);
                    responseValue = findCompMove(compValue, value).val;
                    LS.unplace(coordinates[0], coordinates[1]);
                    if (responseValue < value) {
                        value = responseValue;
                        bestMove = i;
                    }
                    if (value <= compValue) {
                        if (bestMove != -1) {
                            coordinates = getCoordinates(bestMove);
                            return new Move(value, coordinates[0], coordinates[1]);
                        } else {
                            return new Move(value, -1, -1);
                        }
                    }
                }
            }
        }
        if (bestMove != -1) {
            int[] coordinates = getCoordinates(bestMove);
            if (LS.isEmpty(coordinates[0], coordinates[1])) {
                return new Move(value, coordinates[0], coordinates[1]);
            }

        }
        for (int i = 1; i <= 9; i++) {
            int[] coordinates = getCoordinates(i);
            if (LS.isEmpty(coordinates[0], coordinates[1])) {
                return new Move(value, 1, 1);
            }
        }
        return new Move(LS.DRAW, -1, -1);
    }
}
// Class Button extends JButton with (x,y) coordinates
class Button extends javax.swing.JButton {
    public int i;   // The row and column coordinate of the button in a GridLayout
    public int j;

    public Button (int x, int y) {
	// Create a JButton with a blank icon. This also gives the button its correct size.
	super();
	super.setIcon(new javax.swing.ImageIcon(getClass().getResource("None.png")));
	this.i = x;
	this.j = y;
    }

    // Return row coordinate
    public int get_i () {
	return i;
    }

    // Return column coordinate
    public int get_j () {
	return j;
    }

}

public class LS extends javax.swing.JFrame {

    // Marks on the board
    public static final int EMPTY    = 0;
    public static final int HUMAN    = 1;
    public static final int COMPUTER = 2;

    // Outcomes of the game
    public static final int HUMAN_WIN    = 4;
    public static final int DRAW         = 5;
    public static final int CONTINUE     = 6;
    public static final int COMPUTER_WIN = 7;
    
    public static final int SIZE = 3;
    private static int[][] board = new int[SIZE][SIZE];  // The marks on the board
    private javax.swing.JButton[][] jB;           // The buttons of the board
    private int turn = HUMAN;                    // HUMAN starts the game

    /* Constructor for the Tic Tac Toe game */
    public LS() {
	// Close the window when the user exits
	setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	initBoard();      // Set up the board with all marks empty
    }

    // Initalize an empty board. 
    private void initBoard(){
	// Create a SIZE*SIZE gridlayput to hold the buttons	
	java.awt.GridLayout layout = new GridLayout(SIZE, SIZE);
	getContentPane().setLayout(layout);

	// The board is a grid of buttons
	jB = new Button[SIZE][SIZE];
	for (int i=0; i<SIZE; i++) {
	    for (int j=0; j<SIZE; j++) {
		// Create a new button and add an actionListerner to it
		jB[i][j] = new Button(i,j);
		// Add an action listener to the button to handle mouse clicks
		jB[i][j].addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent act) {
			    jBAction(act);
			}
		    });
		add(jB[i][j]);   // Add the buttons to the GridLayout
		
		board[i][j] = EMPTY;     // Initialize all marks on the board to empty
	    }
	}
	// Pack the GridLayout and make it visible
	pack();
    }

    // TODO: 1) Check if square is empty
    public static boolean isEmpty(int row, int col) {
        return true;
    }

    // Action listener which handles mouse clicks on the buttons
    private void jBAction(java.awt.event.ActionEvent act) {
	Button thisButton = (Button) act.getSource();   // Get the button clicked on
    // Get the grid coordinates of the clicked button
    int i = thisButton.get_i();
    int j = thisButton.get_j();

    if (isEmpty(i, j)) {
        System.out.println("Button[" + i + "][" + j + "] was clicked by " + turn);  // DEBUG
        thisButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("X.png")));
        place(i, j, turn);
        if (checkResult() == HUMAN_WIN) {
            System.out.println("Player Wins!");
            return;
        }
        if (checkResult() == DRAW) {
            System.out.println("Draw!");
            return;
        }
        turn = COMPUTER;

        Move compMove = new Move(0, 0, 1).findCompMove(0, 1);
        System.out.println("Button[" + compMove.row + "][" + compMove.col + "] was clicked by " + turn);  // DEBUG
        jB[compMove.row][compMove.col].setIcon(new javax.swing.ImageIcon(getClass().getResource("O.png")));
        place(compMove.row, compMove.col, turn);
        if (checkResult() == COMPUTER_WIN) {
            System.out.println("Computer Wins!");
            return;
        }

    } else {
        System.out.println("Square is not empty, try another one");
    }

	// TODO: 3) Check if we are done (that is COMPUTER or HUMAN wins)
	if (checkResult() != CONTINUE) {
        System.out.println("Draw!");
	    return;
	}
        turn = HUMAN;
    }

    // TODO: 4) This
    public static int checkResult() {
	// This function should check if one player (HUMAN or COMPUTER) wins, if the board is full (DRAW)
	// or if the game should continue. You implement this.
	return CONTINUE;
    }
    
    // Place a mark for one of the playsers (HUMAN or COMPUTER) in the specified position
    public static void place(int row, int col, int player){
	board [row][col] = player;
    }

    public static void unplace(int row, int col) {
        board [row][col] = EMPTY;
    }

    // TODO: Return true if board is full
    public static boolean fullBoard() {

        return false;
    }

    
    public static void main (String [] args){

	String threadName = Thread.currentThread().getName();
	LS lsGUI = new LS();      // Create a new user inteface for the game
	lsGUI.setVisible(true);
	
	java.awt.EventQueue.invokeLater (new Runnable() {
		public void run() {
		    while ( (Thread.currentThread().getName() == threadName) &&
		    	    (lsGUI.checkResult() == CONTINUE) ){
			try {
			    Thread.sleep(100);  // Sleep for 100 millisecond, wait for button press
			} catch (InterruptedException e) { };
		    }
		}
	    });
    }
}
