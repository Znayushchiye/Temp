import java.util.Arrays;
import java.util.Scanner;

public class TicTacToe {
    private boolean alreadyTaken(char[] table, int position) {
        return table[position] == 'X' || table[position] == 'O';
    }

    private boolean hasWon(char[] table) {
        for (int i = 0; i < 3; i++) {
            if (table[i] == table[i + 3] && table[i + 3] == table[i + 6]) {
                return true;
            }
        }
        for (int i = 0; i < 9; i += 3) {
            if (table[i] == table[i + 1] && table[i + 1] == table[i + 2]) {
                return true;
            }
        }
        return (table[0] == table[4] && table[4] == table[8]) || (table[2] == table[4] && table[4] == table[6]);
    }

    private char[] humanTurn(char[] table, int position) {
        table[position - 1] = 'X';
        return table;
    }

    private char[] computerTurn(char[] table) {
        char[] zero = {}, negative = {};
        int[] action = actions(table, noOfMoves(table));
        for (int element : action) {
            char[] resultTable = result(table, element);
            int minimaxValue = Minimax(resultTable);
            if (minimaxValue == 1) {
                return resultTable;
            } else if (minimaxValue == 0) {
                zero = resultTable.clone();
            } else {
                negative = resultTable.clone();
            }
        }
        char[] empty = {};
        if (Arrays.equals(zero, empty)) {
            return negative;
        }
        return zero;
    }

    private void printTable(char[] table) {
        System.out.println("   " + table[0] + " | " + table[1] + " | " + table[2]);
        System.out.println("  ___|___|___");
        System.out.println("   " + table[3] + " | " + table[4] + " | " + table[5]);
        System.out.println("  ___|___|___");
        System.out.println("   " + table[6] + " | " + table[7] + " | " + table[8]);
    }

    private char winParty(char[] table) {
        for (int i = 0; i < 3; i++) {
            if (table[i] == table[i + 3] && table[i + 3] == table[i + 6]) {
                return table[i];
            }
        }
        for (int i = 0; i < 9; i += 3) {
            if (table[i] == table[i + 1] && table[i + 1] == table[i + 2]) {
                return table[i];
            }
        }
        if (table[0] == table[4] && table[4] == table[8]) {
            return table[0];
        }
        if (table[2] == table[4] && table[4] == table[6]) {
            return table[2];
        }
        return '\0';
    }

    private boolean hasTied(char[] table) {
        for (char element : table) {
            if (element != 'X' && element != 'O') {
                return false;
            }
        }
        return true;
    }

    private boolean isTerminalState(char[] table) {
        if (hasWon(table)) {
            return true;
        }
        return hasTied(table);
    }

    private int stateValue(char[] table) {
        if (winParty(table) == 'X') {
            return -1; // Human
        } else if (winParty(table) == 'O') {
            return 1; // Computer
        }
        return 0;
    }

    private int turnOf(char[] table) {
        int humanMoves = 0, computerMoves = 0;
        for (char element : table) {
            if (element == 'X') {
                humanMoves++;
            } else if (element == 'O') {
                computerMoves++;
            }
        } // Human is 0, Computer is 1
        return humanMoves > computerMoves ? 1 : 0;
    }

    private int noOfMoves(char[] table) {
        int moves = 0;
        for (char element : table) {
            if (element != 'X' && element != 'O') {
                moves++;
            }
        }
        return moves;
    }

    private int[] actions(char[] table, int noOfMoves) {
        int[] action = new int[noOfMoves];
        int move = 0, i = 0;
        for (char element : table) {
            move++;
            if (!(element == 'X' || element == 'O')) {
                action[i] = move;
                i++;
            }
        }
        return action;
    }

    private char[] result(char[] table, int move) {
        char[] result = table.clone();
        result[move - 1] = turnOf(table) == 0 ? 'X' : 'O';
        return result;
    }

    private int Minimax(char[] table) {
        if (isTerminalState(table)) {
            return stateValue(table);
        }
        int player = turnOf(table);
        if (player == 1) {
            int value = -99999;
            for (int action : actions(table, noOfMoves(table))) {
                char[] resultTable = result(table, action);
                value = Math.max(value, Minimax(resultTable));
            }
            return value;
        }
        if (player == 0) {
            int value = 99999;
            for (int action : actions(table, noOfMoves(table))) {
                char[] resultTable = result(table, action);
                value = Math.min(value, Minimax(resultTable));
            }
            return value;
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("------------------------------------------");
        System.out.println("Tic-Tac-Toe");
        System.out.println("You choose 'X' and the computer chooses 'O'.");
        System.out.println("Get 3 in a row, column or diagonal to win.");
        System.out.println("Do you want to begin? (y/n)");

        Scanner scanner = new Scanner(System.in);
        char start = scanner.nextLine().toUpperCase().charAt(0);
        if (start == 'Y') {
            TicTacToe game = new TicTacToe();
            char[] table = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
            int count = 0;
            do {
                count++;
                System.out.println("------------------------------------------");
                System.out.println((count != 1) ? ("  Current Table:\n") : ("  Game start\n"));
                game.printTable(table);
                int choice;
                boolean flag;
                do {
                    flag = false;
                    System.out.print("\nEnter your choice : ");
                    choice = scanner.nextInt();
                    if (choice < 1 || choice > 9) {
                        System.out.println("\nWrong choice! Enter again.");
                        flag = true;
                    } else if (game.alreadyTaken(table, choice - 1)) {
                        System.out.println("\nPosition already taken! Enter again.");
                        flag = true;
                    }
                } while (flag);
                table = game.humanTurn(table, choice).clone();
                if (game.isTerminalState(table)) {
                    System.out.println("------------------------------------------");
                    break;
                }
                table = game.computerTurn(table).clone();
            } while (!game.hasWon(table));
            System.out.println("------------------------------------------");
            System.out.println("\n  Final Table:\n");
            game.printTable(table);
            int winner = game.winParty(table);
            if (winner == 'X') {
                System.out.println("\nCongratulations! You win the match! :-D");
            } else if (winner == 'O') {
                System.out.println("\nYou lost the game :-(");
            } else {
                System.out.println("\nMatch tied.");
            }
            System.out.println("------------------------------------------");
        } else {
            System.out.println("Fin.");
            System.out.println("------------------------------------------");
        }
    }
}
