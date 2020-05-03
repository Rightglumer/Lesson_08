import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameLogic extends JPanel {

    static final char DOT_X = 'X';
    static final char DOT_O = 'O';
    static final char DOT_EMPTY = ' ';
    static final char[] DOTS = {DOT_X, DOT_O};

    static final int CHECK_LEFT = 0;
    static final int CHECK_RIGHT = 1;
    static final int CHECK_UP = 2;
    static final int CHECK_DOWN = 3;
    static final int CHECK_RIGHT_DOWN = 4;
    static final int CHECK_LEFT_DOWN = 5;
    static final int CHECK_LEFT_UP = 6;
    static final int CHECK_RIGHT_UP = 7;

    static final int CHECK_SIDE_COUNT = 8;

    static final int INTERVAL = 5;

    static final int[] DELTA_ROW = {0, 0, -1, 1, 1, 1, -1, -1};
    static final int[] DELTA_COL = {-1, 1, 0, 0, 1, -1, -1, 1};

    public static char[][] gameMap;
    public static Random randDigit = new Random();
    public static int freeCellCount = 0;
    public static int humanTurnOrder;

    public static int size;
    public static int numberToWin;
    public int cellSize;
    public boolean gameFinished;

    public GameLogic(int size, int numberToWin) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });

        startNewGame(size, numberToWin);
    }

    public void startNewGame(int size, int numberToWin) {
        this.size = size;
        this.numberToWin = numberToWin;
        gameFinished = false;
        createMap();
        freeCellCount = size * size;
        humanTurnOrder = getHumanTurnOrder();

        if (humanTurnOrder == 1) {
            turnComputer(1 - humanTurnOrder);
        }

        repaint();
    }

    private void update(MouseEvent e) {
        int cellY = e.getX() / cellSize;
        int cellX = e.getY() / cellSize;

        if (!gameFinished) {
            turnHuman(cellX, cellY);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintMap(g);
    }

    private void paintMap(Graphics g) {
        int panelSize = getWidth(); // width = height

        cellSize = panelSize / size;

        for (int i = 0; i < size; i++) {
            int xy = i * cellSize;
            g.drawLine(xy, 0, xy, panelSize);
            g.drawLine(0, xy, panelSize, xy);
        }
        g.drawLine(0, panelSize, panelSize, panelSize);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (gameMap[i][j] == DOT_O) {
                    drawDotO(g, j, i);
                }
                if (gameMap[i][j] == DOT_X) {
                    drawDotX(g, j, i);
                }
            }
        }
    }

    private void drawDotO(Graphics g, int cellX, int cellY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLUE);
        g2.drawOval(cellX * cellSize + INTERVAL, cellY * cellSize + INTERVAL, cellSize - INTERVAL * 2, cellSize - INTERVAL * 2);
    }

    private void drawDotX(Graphics g, int cellX, int cellY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.GREEN);
        g2.drawLine(cellX * cellSize + INTERVAL, cellY * cellSize + INTERVAL, (cellX + 1) * cellSize - INTERVAL, (cellY + 1) * cellSize - INTERVAL);
        g2.drawLine((cellX + 1) * cellSize - INTERVAL, cellY * cellSize + INTERVAL, cellX * cellSize + INTERVAL, (cellY + 1) * cellSize - INTERVAL);
    }

//        public static void main(String[] args) {
//            createMap();
//            freeCellCount = NUMBER_COL * NUMBER_ROW;
//            int turnType = getHumanTurnOrder();
//            humanTurnOrder = turnType;
//            if (humanTurnOrder == 0){
//                printMap();
//            }
//            do {
//                nextTurn(turnType);
//                turnType = 1 - turnType;
//                printMap();
//            } while ((!hasWinner(NUMBER_TO_WIN, DOT_EMPTY)) && hasEmptyCell());
//            if (!hasEmptyCell()){
//                System.out.println("It's a draw");
//            }
//            else{
//                if (turnType == 1){
//                    System.out.println("You win :(");
//                }
//                else{
//                    System.out.println("Computer wins!");
//                }
//            }
//        }

    public static boolean hasEmptyCell() {
        return freeCellCount > 0;
    }

    public static int getHumanTurnOrder() {
        if (randDigit.nextInt(10) < 5) {
            return 0; // human's first turn
        } else {
            return 1; // computer's first turn
        }
    }

    public void createMap() {
        gameMap = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gameMap[i][j] = DOT_EMPTY;
            }
        }
    }

    public static boolean isCellValid(int row, int col) {
        return (gameMap[row][col] == DOT_EMPTY);
    }

    public void stopGame(int currentTurn) {
        if (hasWinner(numberToWin, DOT_EMPTY)){
            if (currentTurn == 0) {
                JOptionPane.showMessageDialog(this, "Вам повезло в этот раз");
            } else {
                JOptionPane.showMessageDialog(this, "Skynet победил, Нео...");
            }
        } else {
                JOptionPane.showMessageDialog(this, "Победила дружба");
            }

        gameFinished = true;
    }

    public void turnHuman(int cellX, int cellY) {
        if (!isCellValid(cellX, cellY)) {
            JOptionPane.showMessageDialog(this, "Эта ячейка уже занята");
            return;
        }
        gameMap[cellX][cellY] = DOTS[humanTurnOrder];
        freeCellCount --;
        repaint();
        if (hasWinner(numberToWin, DOT_EMPTY) || !hasEmptyCell()) {
            stopGame(0);
        } else {
            turnComputer(1 - humanTurnOrder);
        }
    }

    public void turnComputer(int turnOrder) {
        int x, y;
        boolean winStrategy;
        boolean needStop = false;
        if ((humanTurnOrder == 1) && (gameMap[0][0] == DOT_EMPTY)) {
            gameMap[0][0] = DOTS[1 - humanTurnOrder];
            needStop = true;
        }
        if (!needStop) {
            // check, could computer win
            winStrategy = hasWinner(numberToWin - 1, DOTS[1 - humanTurnOrder]);
            if (winStrategy) {
                for (int i = 0; i < size; i++) {
                    if (needStop) {
                        break;
                    }
                    for (int j = 0; j < size; j++) {
                        if (gameMap[i][j] == DOT_EMPTY) {
                            gameMap[i][j] = DOTS[1 - humanTurnOrder];
                            if (!hasWinner(numberToWin, DOTS[1 - humanTurnOrder])) {
                                gameMap[i][j] = DOT_EMPTY;
                            } else {
                                needStop = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (!needStop) {
            // check, could human win
            winStrategy = hasWinner(numberToWin - 1, DOTS[humanTurnOrder]);
            if (winStrategy) {
                for (int i = 0; i < size; i++) {
                    if (needStop) {
                        break;
                    }
                    for (int j = 0; j < size; j++) {
                        if (gameMap[i][j] == DOT_EMPTY) {
                            gameMap[i][j] = DOTS[humanTurnOrder];
                            if (!hasWinner(numberToWin, DOTS[humanTurnOrder])) {
                                gameMap[i][j] = DOT_EMPTY;
                            } else {
                                gameMap[i][j] = DOTS[1 - humanTurnOrder];
                                needStop = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (!needStop) {
            // random turn
            do {
                x = randDigit.nextInt(size);
                y = randDigit.nextInt(size);
            } while (!isCellValid(x, y));
            gameMap[x][y] = DOTS[1 - humanTurnOrder];
        }

        freeCellCount --;
        if (hasWinner(numberToWin, DOT_EMPTY) || !hasEmptyCell()) {
            stopGame(1);
        }
    }

    public static boolean canCheckSide(int direction, int position, int checkCellsCount) {
        boolean canCheck = true;
        switch (direction) {
            case (-1):
                canCheck &= (position - checkCellsCount + 1) >= 0;
                break;
            case (1):
                canCheck &= (position + checkCellsCount - 1) < size;
                break;
        }
        return canCheck;
    }

    public static boolean canCheckDirection(int direction, int posRow, int posCol, int checkCellsCount) {
        boolean canCheck = true;
        switch (direction) {
            case (CHECK_LEFT):
                canCheck &= canCheckSide(DELTA_COL[CHECK_LEFT], posCol, checkCellsCount);
                break;
            case (CHECK_RIGHT):
                canCheck &= canCheckSide(DELTA_COL[CHECK_RIGHT], posCol, checkCellsCount);
                break;
            case (CHECK_UP):
                canCheck &= canCheckSide(DELTA_ROW[CHECK_UP], posRow, checkCellsCount);
                break;
            case (CHECK_DOWN):
                canCheck &= canCheckSide(DELTA_ROW[CHECK_DOWN], posRow, checkCellsCount);
                break;
            case (CHECK_RIGHT_DOWN):
                canCheck &= (canCheckSide(DELTA_ROW[CHECK_RIGHT_DOWN], posRow, checkCellsCount) && canCheckSide(DELTA_COL[CHECK_RIGHT_DOWN], posCol, checkCellsCount));
                break;
            case (CHECK_LEFT_DOWN):
                canCheck &= (canCheckSide(DELTA_ROW[CHECK_LEFT_DOWN], posRow, checkCellsCount) && canCheckSide(DELTA_COL[CHECK_LEFT_DOWN], posCol, checkCellsCount));
                break;
            case (CHECK_LEFT_UP):
                canCheck &= (canCheckSide(DELTA_ROW[CHECK_LEFT_UP], posRow, checkCellsCount) && canCheckSide(DELTA_COL[CHECK_LEFT_UP], posCol, checkCellsCount));
                break;
            case (CHECK_RIGHT_UP):
                canCheck &= (canCheckSide(DELTA_ROW[CHECK_RIGHT_UP], posRow, checkCellsCount) && canCheckSide(DELTA_COL[CHECK_RIGHT_UP], posCol, checkCellsCount));
                break;
        }
        return canCheck;
    }

    public static boolean hasWinner(int checkCellsCount, char checkDot) {
        char curCell;
        boolean[] winDirection = new boolean[8];
        boolean hasWinner = false;

        // for every cell
        for (int row = 0; row < size; row++) {
            if (hasWinner) {
                break;
            }
            for (int col = 0; col < size; col++) {
                curCell = gameMap[row][col];
                if (checkDot != DOT_EMPTY) {
                    if (curCell != checkDot) {
                        break;
                    }
                }
                if (hasWinner) {
                    break;
                }
                if (gameMap[row][col] != DOT_EMPTY) {
                    // clear win cells
                    for (int x = 0; x < 8; x++) {
                        winDirection[x] = true;
                    }

                    for (int x = 0; x < checkCellsCount; x++) {
                        for (int direction = 0; direction < CHECK_SIDE_COUNT; direction++) {
                            if (winDirection[direction]) {
                                if (canCheckDirection(direction, row, col, checkCellsCount)) {
                                    winDirection[direction] &= gameMap[row + DELTA_ROW[direction] * x][col + DELTA_COL[direction] * x] == curCell;
                                } else {
                                    winDirection[direction] = false;
                                }
                            }
                        }
                    }
                    for (int x = 0; x < CHECK_SIDE_COUNT; x++) {
                        hasWinner |= winDirection[x];
                    }
                }
            }
        }
        return hasWinner;
    }
}
