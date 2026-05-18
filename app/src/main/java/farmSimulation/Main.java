package farmSimulation;

import farmSimulation.base.Board;
import farmSimulation.entities.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(10, 10);

        board.addEntity(new Potato(2, 2, 3.0));
        board.addEntity(new Potato(5, 5, 4.0));
        board.addEntity(new Beetle(2, 3));
        board.addEntity(new Chicken(3, 3));
        board.addEntity(new Fox(7, 7));
        board.addEntity(new Farmer(5, 4, 2.0));

        Scanner scanner = new Scanner(System.in);
        int tickCounter = 0;

        System.out.println("Symulacja farmy uruchomiona!");

        System.out.println("\n--- STAN POCZĄTKOWY PLANSZY ---");
        printBoardTable(board);

        while (true) {
            System.out.print("\nNaciśnij [Enter] dla następnego kroku (lub wpisz 'exit', aby wyjść): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Symulacja zakończona.");
                break;
            }

            tickCounter++;
            System.out.println("\n--- KROK " + tickCounter + " ---");

            board.nextTick();

            printBoardTable(board);
        }

        scanner.close();
    }

    public static void printBoardTable(Board board) {
        int width = board.getWidth();
        int height = board.getHeight();

        printHorizontalDivider(width);

        for (int y = 0; y < height; y++) {
            System.out.print("|");
            for (int x = 0; x < width; x++) {
                Entity entity = board.getEntityAt(x, y);
                if (entity != null) {
                    System.out.print(" " + entity.getSymbol() + " |");
                } else {
                    System.out.print("   |");
                }
            }
            System.out.println();
            printHorizontalDivider(width);
        }
    }

    private static void printHorizontalDivider(int width) {
        for (int i = 0; i < width; i++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }
}
