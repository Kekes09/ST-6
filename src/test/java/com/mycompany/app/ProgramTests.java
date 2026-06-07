package com.mycompany.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProgramTests {

    private Game igra;
    private Player igrokX;
    private Player igrokO;

    @BeforeEach
    void podgotovka() {
        igra = new Game();
        igrokX = new Player();
        igrokX.symbol = 'X';
        igrokO = new Player();
        igrokO.symbol = 'O';
    }

    @Test
    void testSozdanieIgroka() {
        Player igrok = new Player();
        igrok.symbol = 'X';
        igrok.move = 5;
        igrok.selected = true;
        igrok.win = true;
        assertEquals('X', igrok.symbol);
        assertEquals(5, igrok.move);
        assertTrue(igrok.selected);
        assertTrue(igrok.win);
    }

    @Test
    void testInitsializatsiyaDoski() {
        Game novaya = new Game();
        assertNotNull(novaya.board);
        assertEquals(9, novaya.board.length);
        for (char c : novaya.board) assertEquals(' ', c);
        assertEquals(State.PLAYING, novaya.state);
        assertEquals('X', novaya.player1.symbol);
        assertEquals('O', novaya.player2.symbol);
    }

    @Test
    void testProverkaGorizontalX() {
        char[] doska = {'X','X','X', ' ',' ',' ', ' ',' ',' '};
        ustanovitSimvol(igra, 'X');
        assertEquals(State.XWIN, igra.checkState(doska));
    }

    @Test
    void testProverkaVertikalO() {
        char[] doska = {'O',' ',' ', 'O',' ',' ', 'O',' ',' '};
        ustanovitSimvol(igra, 'O');
        assertEquals(State.OWIN, igra.checkState(doska));
    }

    @Test
    void testProverkaDiagonal() {
        char[] doska = {'X',' ',' ', ' ','X',' ', ' ',' ','X'};
        ustanovitSimvol(igra, 'X');
        assertEquals(State.XWIN, igra.checkState(doska));
    }

    @Test
    void testProverkaNichya() {
        char[] doska = {'X','O','X', 'X','O','O', 'O','X','X'};
        ustanovitSimvol(igra, 'X');
        assertEquals(State.DRAW, igra.checkState(doska));
    }

    @Test
    void testProverkaIgraet() {
        char[] doska = {'X','O',' ', ' ',' ',' ', ' ',' ',' '};
        ustanovitSimvol(igra, 'X');
        assertEquals(State.PLAYING, igra.checkState(doska));
    }

    @ParameterizedTest
    @CsvSource({"0, 1, 2", "0, 3, 6", "0, 4, 8", "2, 4, 6"})
    void testVyigryshnyeKombinatsii(int a, int b, int c) {
        char[] doska = new char[9];
        for(int i=0; i<9; i++) doska[i] = ' ';
        doska[a] = 'X'; doska[b] = 'X'; doska[c] = 'X';
        ustanovitSimvol(igra, 'X');
        assertEquals(State.XWIN, igra.checkState(doska));
    }

    @Test
    void testGeneratsiyaKhodovPustaya() {
        char[] doska = new char[9];
        for(int i=0; i<9; i++) doska[i] = ' ';
        ArrayList<Integer> khody = new ArrayList<>();
        igra.generateMoves(doska, khody);
        assertEquals(9, khody.size());
    }

    @Test
    void testGeneratsiyaKhodovPolnaya() {
        char[] doska = {'X','O','X','O','X','O','X','O','X'};
        ArrayList<Integer> khody = new ArrayList<>();
        igra.generateMoves(doska, khody);
        assertTrue(khody.isEmpty());
    }

    @Test
    void testOtsenkaXVyigralDlyaX() {
        char[] doska = {'X','X','X', ' ',' ',' ', ' ',' ',' '};
        ustanovitSimvol(igra, 'X');
        assertEquals(Game.INF, igra.evaluatePosition(doska, igrokX));
    }

    @Test
    void testOtsenkaXVyigralDlyaO() {
        char[] doska = {'X','X','X', ' ',' ',' ', ' ',' ',' '};
        ustanovitSimvol(igra, 'X');
        assertEquals(-Game.INF, igra.evaluatePosition(doska, igrokO));
    }

    @Test
    void testOtsenkaNichya() {
        char[] doska = {'X','O','X', 'X','O','O', 'O','X','X'};
        assertEquals(0, igra.evaluatePosition(doska, igrokX));
    }

    @Test
    void testOtsenkaNeZavershena() {
        char[] doska = new char[9];
        for(int i=0; i<9; i++) doska[i] = ' ';
        assertEquals(-1, igra.evaluatePosition(doska, igrokX));
    }

    @Test
    void testMiniMaxPochtiPolnaya() {
        char[] doska = {'X','O','X', 'O','X','O', 'X','O',' '};
        ustanovitSimvol(igra, 'O');
        int khod = igra.MiniMax(doska, igrokO);
        assertEquals(9, khod);
    }

    @Test
    void testMinMovePobeda() {
        char[] doska = {'O','O','O', ' ',' ',' ', ' ',' ',' '};
        ustanovitSimvol(igra, 'O');
        assertEquals(Game.INF, igra.MinMove(doska, igrokO));
    }

    @Test
    void testMaxMovePobeda() {
        char[] doska = {'X','X','X', ' ',' ',' ', ' ',' ',' '};
        ustanovitSimvol(igra, 'X');
        assertEquals(Game.INF, igra.MaxMove(doska, igrokX));
    }

    @Test
    void testUtilitaPechatChar() {
        char[] doska = {'X','O','X', 'O','X','O', 'X','O','X'};
        assertDoesNotThrow(() -> Utility.print(doska));
    }

    @Test
    void testUtilitaPechatInt() {
        int[] arr = {1,2,3,4,5,6,7,8,9};
        assertDoesNotThrow(() -> Utility.print(arr));
    }

    @Test
    void testUtilitaPechatList() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0); list.add(4); list.add(8);
        assertDoesNotThrow(() -> Utility.print(list));
    }

    @Test
    void testYacheykaSozdanie() {
        TicTacToeCell y = new TicTacToeCell(4, 1, 1);
        assertNotNull(y);
        assertEquals(4, y.getNum());
        assertEquals(1, y.getRow());
        assertEquals(1, y.getCol());
        assertEquals(' ', y.getMarker());
    }

    @Test
    void testYacheykaSetMarker() {
        TicTacToeCell y = new TicTacToeCell(0, 0, 0);
        y.setMarker("X");
        assertEquals('X', y.getMarker());
        assertEquals("X", y.getText());
        assertFalse(y.isEnabled());
    }

    @Test
    void testPanelSozdaetYacheyki() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new java.awt.GridLayout(3,3));
        Field f = TicTacToePanel.class.getDeclaredField("cells");
        f.setAccessible(true);
        TicTacToeCell[] cells = (TicTacToeCell[]) f.get(panel);
        assertNotNull(cells);
        assertEquals(9, cells.length);
    }

    @Test
    void testEnumValues() {
        assertEquals(4, State.values().length);
        assertEquals(State.PLAYING, State.valueOf("PLAYING"));
    }

    @Test
    void testKonstantaINF() {
        assertEquals(100, Game.INF);
    }

    private void ustanovitSimvol(Game igra, char simvol) {
        try {
            Field f = Game.class.getDeclaredField("symbol");
            f.setAccessible(true);
            f.set(igra, simvol);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}