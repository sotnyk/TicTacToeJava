package com.sotnyk.tictactoe;

import static com.sotnyk.tictactoe.IoUtils.*;
import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        // write your code here
        showUsage();
        do {
            Game game = new Game(askYesNo("Do you wanna move first? (y/n)") ? CellType.X : CellType.O);
            game.startGameLoop();
        } while (askYesNo("Do you wanna repeat? (y/n)"));
    }

    static void showUsage() {
        out.println();
        out.println("Welcome to the tic-tac-toe game.");
        out.println("To make a move, enter a digit from 1 to 9. This digit accords");
        out.println("to the board's fields as it defined below:");
        out.println();
        out.println("1 | 2 | 3");
        out.println("--+---+--");
        out.println("4 | 5 | 6");
        out.println("--+---+--");
        out.println("7 | 8 | 9");
        out.println();
    }
}
