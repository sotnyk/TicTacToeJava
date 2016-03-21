package com.sotnyk.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.arraycopy;
import static java.lang.System.out;

/**
 * Created by Serge on 13/03/2016.
 */
public class Game {
    public Board board = new Board();
    public CellType humanCell;
    public CellType compCell;
    public boolean isHumanOrder;
    public boolean isXOrder;
    public Winner computerWinner;
    public Winner humanWinner;

    public Game(CellType humanCell){
        this.humanCell = humanCell;
        this.compCell = (humanCell==CellType.O)?CellType.X:CellType.O;
        this.isHumanOrder = humanCell==CellType.X;
        this.isXOrder = true;
        if (humanCell==CellType.X){
            computerWinner = Winner.WINNER_O;
            humanWinner = Winner.WINNER_X;
        }else{
            computerWinner = Winner.WINNER_X;
            humanWinner = Winner.WINNER_O;
        }
    }

    public void startGameLoop() {
        while (!board.isGameEnded()){
            board.display();
            if (isHumanOrder){
                board.cells[makeHumanMove()] = humanCell;
            }else{
                board.cells[makeCompMove()] = compCell;
            }
            isHumanOrder = !isHumanOrder;
            isXOrder = !isXOrder;
        }
        board.display();
        displayResult();
    }

    private void displayResult() {
        Winner gameResult = board.calcWinner();
        if (gameResult == Winner.DRAW) {
            out.println("Game result is DRAW. Nobody won.");
        } else if ((gameResult == Winner.WINNER_X && humanCell == CellType.X)
                || (gameResult == Winner.WINNER_O && humanCell == CellType.O)) {
            out.println("You won! Congratulates!");
        } else if ((gameResult == Winner.WINNER_X && humanCell == CellType.O)
                || (gameResult == Winner.WINNER_O && humanCell == CellType.X)) {
            out.println("I am winner! Don't worry, baby!");
        } else {
            out.println("!!!Something wrong!!!");
        }
    }

    private int makeHumanMove() {
        List<Integer> legal = board.legalMoves();
        int move = -1;
        while (!legal.contains(move)){
            move = IoUtils.askNumber("Your move. Please, select one of empty field (1...9): ", 1, Board.BOARD_SIZE)-1;
            if (!legal.contains(move)) {
                out.println();
                out.println("This field is already engaged. Please, use another one.");
                out.println();
            }
        }
        return move;
    }

    static int[][] best_moves = {
            {4, 0, 2, 6, 8, 1, 3, 5, 7},
            {8, 6, 2, 0, 4, 7, 3, 5, 1},
            {6, 2, 0, 8, 4, 7, 3, 5, 1},
            {4, 6, 2, 8, 0, 7, 3, 5, 1},
            {2, 6, 0, 8, 4, 5, 7, 1, 3},
    };

    private int makeCompMove() {

        int[] best_moves = this.best_moves[ThreadLocalRandom.current().nextInt(0, this.best_moves.length)];
        out.print("Computer's move is ");
        List<Integer> legalMoves = board.legalMoves();
        for(int move: legalMoves) {
            board.cells[move] = compCell;
            if (board.calcWinner() == computerWinner) {
                out.println(move);
                return move;
            }
            board.cells[move] = CellType.EMPTY;
        }
        for(int move: legalMoves) {
            board.cells[move] = humanCell;
            if (board.calcWinner() == humanWinner) {
                out.println(move);
                return move;
            }
            board.cells[move] = CellType.EMPTY;
        }
        List<WeightedMove> possible_moves = new ArrayList<>();
        for(int move: best_moves) {
            if (legalMoves.contains(move)) {
                possible_moves.add(new WeightedMove(move, calc_fitness_comp_move(move)));
            }
        }
        possible_moves.sort((o1, o2)->(int)-Math.signum(o1.weight-o2.weight));
        int move = possible_moves.get(0).move;
        out.println(move);
        return move;
    }

    private float calc_fitness_comp_move(int move) {
        board.cells[move] = compCell;
        float fitness = 0;

        Winner state = board.calcWinner();
        if (state==computerWinner) {
            fitness = 1;
        } else if (state==humanWinner) {
            fitness = -1;
        } else if (state==Winner.DRAW) {
            fitness = 0;
        }else if (state==Winner.GAME_ONGOING) {
            List<Integer> legal = board.legalMoves();
            List<WeightedMove> possible_moves = new ArrayList<WeightedMove>();
            for (int human_move : legal) {
                possible_moves.add(new WeightedMove(human_move, calc_fitness_human_move(human_move)));
            }
            possible_moves.sort((o1, o2) -> (int) Math.signum(o1.weight - o2.weight));
            fitness = possible_moves.get(0).weight;
        }
        board.cells[move] = CellType.EMPTY;
        return fitness;
    }

    private float calc_fitness_human_move(int move) {
        board.cells[move] = humanCell;
        float fitness = 0;

        Winner state = board.calcWinner();
        if (state==computerWinner) {
            fitness = 1;
        } else if (state==humanWinner) {
            fitness = -1;
        } else if (state==Winner.DRAW) {
            fitness = 0;
        }else if (state==Winner.GAME_ONGOING) {
            List<Integer> legal = board.legalMoves();
            List<WeightedMove> possible_moves = new ArrayList<WeightedMove>();
            for (int human_move : legal) {
                possible_moves.add(new WeightedMove(human_move, calc_fitness_comp_move(human_move)));
            }
            possible_moves.sort((o1, o2) -> (int) -Math.signum(o1.weight - o2.weight));
            fitness = possible_moves.get(0).weight;
        }
        board.cells[move] = CellType.EMPTY;
        return fitness;
    }

    public class WeightedMove{
        public int move;
        public float weight;
        public WeightedMove(int move, float weight){
            this.move = move;
            this.weight = weight;
        }
    }
}
