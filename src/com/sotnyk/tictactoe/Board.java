package com.sotnyk.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Created by Serge on 13/03/2016.
 */
public class Board {
    public static final int BOARD_SIZE = 9;
    public CellType[] cells = new CellType[BOARD_SIZE];

    public Board() {
        for (int i = 0; i < BOARD_SIZE; ++i)
            cells[i] = CellType.EMPTY;
    }

    public void display() {
        out.println();
        out.println("\t" + asChar(cells[0]) + " | " + asChar(cells[1]) + " | " + asChar(cells[2]));
        out.println("\t--+---+--");
        out.println("\t" + asChar(cells[3]) + " | " + asChar(cells[4]) + " | " + asChar(cells[5]));
        out.println("\t--+---+--");
        out.println("\t" + asChar(cells[6]) + " | " + asChar(cells[7]) + " | " + asChar(cells[8]));
        out.println();
    }

    public char asChar(CellType cell) {
        switch (cell) {
            case EMPTY:
                return ' ';
            case O:
                return 'O';
            case X:
                return 'X';
            default:
                //throw new Exception("Unknown cell type");
                return '?';
        }
    }

    public Winner calcWinner() {
        return calcWinner(cells);
    }

    private static int[][] waysToWin =
            {{0, 1, 2},
                    {3, 4, 5},
                    {6, 7, 8},
                    {0, 3, 6},
                    {1, 4, 7},
                    {2, 5, 8},
                    {0, 4, 8},
                    {2, 4, 6}};

    public static Winner calcWinner(CellType[] cells) {
        for (int[] way : waysToWin) {
            if (cells[way[0]] == cells[way[1]] && cells[way[0]] == cells[way[2]] && cells[way[0]] != CellType.EMPTY)
                return cells[way[0]] == CellType.X ? Winner.WINNER_X : Winner.WINNER_O;
        }

        List<CellType> cellsList = Arrays.asList(cells);
        if (!cellsList.contains(CellType.EMPTY))
            return Winner.DRAW;

        return Winner.GAME_ONGOING;
    }

    public boolean isGameEnded() {
        return calcWinner() != Winner.GAME_ONGOING;
    }

    public List<Integer> legalMoves(){
        List<Integer> result = new ArrayList<Integer>();
        for(int i=0; i<BOARD_SIZE;++i){
            if (cells[i]==CellType.EMPTY)
                result.add(i);
        }
        return result;
    }
}
