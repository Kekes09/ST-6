package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.awt.GridLayout;
import java.lang.reflect.Field;

public class ProgramTests {

    @Test
    public void testGameInitialization() {
        Game game = new Game();
        assertNotNull(game.board);
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        assertEquals(9, game.board.length);

        for (int i = 0; i < 9; i++) {
            assertEquals(' ', game.board[i]);
        }
        assertNull(game.cplayer);
    }

    @Test
    public void testCheckStateAllConditions() {
        Game game = new Game();

        char[] xWinHorizontal = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(xWinHorizontal));

        char[] xWinVertical = {'X', ' ', ' ', 'X', ' ', ' ', 'X', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(xWinVertical));

        char[] xWinDiagonal = {'X', ' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X'};
        assertEquals(State.XWIN, game.checkState(xWinDiagonal));

        char[] oWinHorizontal = {'O', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '};
        game.symbol = 'O';
        assertEquals(State.OWIN, game.checkState(oWinHorizontal));

        char[] oWinVertical = {'O', ' ', ' ', 'O', ' ', ' ', 'O', ' ', ' '};
        assertEquals(State.OWIN, game.checkState(oWinVertical));

        char[] draw = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        game.symbol = 'X';
        assertEquals(State.DRAW, game.checkState(draw));

        char[] playing = {'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(State.PLAYING, game.checkState(playing));
    }

    @Test
    public void testGenerateMoves() {
        Game game = new Game();

        char[] emptyBoard = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        ArrayList<Integer> emptyMoves = new ArrayList<>();
        game.generateMoves(emptyBoard, emptyMoves);
        assertEquals(9, emptyMoves.size());

        char[] partialBoard = {'X', 'O', ' ', 'X', ' ', ' ', ' ', ' ', ' '};
        ArrayList<Integer> partialMoves = new ArrayList<>();
        game.generateMoves(partialBoard, partialMoves);
        assertEquals(6, partialMoves.size());

        char[] fullBoard = {'X', 'O', 'X', 'O', 'X', 'O', 'O', 'X', 'O'};
        ArrayList<Integer> fullMoves = new ArrayList<>();
        game.generateMoves(fullBoard, fullMoves);
        assertEquals(0, fullMoves.size());
    }

    @Test
    public void testEvaluatePosition() {
        Game game = new Game();

        char[] xWinBoard = {'X', 'X', 'X', 'O', 'O', ' ', ' ', ' ', ' '};
        game.symbol = 'X';
        int xWinForX = game.evaluatePosition(xWinBoard, game.player1);
        assertEquals(Game.INF, xWinForX);

        char[] oWinBoard = {'O', 'O', 'O', 'X', 'X', ' ', ' ', ' ', ' '};
        game.symbol = 'O';
        int oWinForO = game.evaluatePosition(oWinBoard, game.player2);
        assertEquals(Game.INF, oWinForO);

        char[] drawBoard = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        game.symbol = 'X';
        assertEquals(0, game.evaluatePosition(drawBoard, game.player1));

        char[] playingBoard = {'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(-1, game.evaluatePosition(playingBoard, game.player1));
    }

    @Test
    public void testCellProperties() {
        TicTacToeCell cell1 = new TicTacToeCell(0, 0, 0);
        TicTacToeCell cell2 = new TicTacToeCell(4, 1, 1);
        TicTacToeCell cell3 = new TicTacToeCell(8, 2, 2);

        assertEquals(0, cell1.getNum());
        assertEquals(0, cell1.getCol());
        assertEquals(0, cell1.getRow());

        assertEquals(4, cell2.getNum());
        assertEquals(1, cell2.getCol());
        assertEquals(1, cell2.getRow());

        assertEquals(8, cell3.getNum());
        assertEquals(2, cell3.getCol());
        assertEquals(2, cell3.getRow());

        cell1.setMarker("X");
        assertEquals('X', cell1.getMarker());
        assertEquals("X", cell1.getText());
        assertFalse(cell1.isEnabled());

        cell2.setMarker("O");
        assertEquals('O', cell2.getMarker());
        assertEquals("O", cell2.getText());
    }

    @Test
    public void testPanelStructure() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        assertEquals(9, panel.getComponentCount());

        Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
        cellsField.setAccessible(true);
        TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);

        assertNotNull(cells);
        assertEquals(9, cells.length);

        for (int i = 0; i < 9; i++) {
            assertNotNull(cells[i]);
            assertEquals(i, cells[i].getNum());
        }

        Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game game = (Game) gameField.get(panel);
        assertNotNull(game);
        assertNotNull(game.player1);
        assertNotNull(game.player2);
    }

    @Test
    public void testUtilityFull() {
        char[] boardChar = {'X', 'O', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        int[] boardInt = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(1);
        moves.add(2);
        moves.add(3);

        assertDoesNotThrow(() -> {
            Utility.print(boardChar);
            Utility.print(boardInt);
            Utility.print(moves);
        });
    }

    @Test
    public void testPlayerFields() {
        Game game = new Game();

        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        assertEquals(0, game.player1.move);
        assertEquals(0, game.player2.move);
        assertFalse(game.player1.selected);
        assertFalse(game.player2.selected);
        assertFalse(game.player1.win);
        assertFalse(game.player2.win);

        game.player1.move = 5;
        game.player2.move = 3;
        game.player1.selected = true;
        game.player2.win = true;

        assertEquals(5, game.player1.move);
        assertEquals(3, game.player2.move);
        assertTrue(game.player1.selected);
        assertTrue(game.player2.win);
    }

    @Test
    public void testGameConstants() {
        assertEquals(100, Game.INF);

        Game game = new Game();
        game.state = State.XWIN;
        assertEquals(State.XWIN, game.state);

        game.symbol = 'O';
        assertEquals('O', game.symbol);
    }
}