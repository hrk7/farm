package farmSimulation; // Definicja głównego pakietu projektu symulacji farmy

import farmSimulation.base.Board; // Importowanie klasy Board odpowiedzialnej za zarządzanie planszą symulacji
import farmSimulation.entities.*; // Importowanie wszystkich klas jednostek do celów konfiguracji spawnu
import farmSimulation.UI.simulationGUI; // Importowanie klasy interfejsu graficznego użytkownika (GUI)
import javax.swing.SwingUtilities; // Importowanie klasy SwingUtilities służącej do bezpiecznego uruchamiania wątku GUI
import java.util.HashMap; // Importowanie klasy HashMap do implementacji mapy konfiguracyjnej
import java.util.Map; // Importowanie interfejsu Map do obsługi struktur klucz-wartość

public class Main { // Definicja głównej klasy Main uruchamiającej całą aplikację
    public static void main(String[] args) { // Główna metoda startowa programu
        Map<String, Integer> spawnConfig = new HashMap<>(); // Utworzenie mapy przechowującej początkową liczbę obiektów poszczególnych typów
        spawnConfig.put("potatoes", 1); // Zdefiniowanie spawnu dla dokładnie 1 ziemniaka na starcie symulacji
        spawnConfig.put("beetles", 1); // Zdefiniowanie spawnu dla dokładnie 1 stonki na starcie symulacji
        spawnConfig.put("chickens", 1); // Zdefiniowanie spawnu dla dokładnie 1 kurczaka na starcie symulacji
        spawnConfig.put("foxes", 1); // Zdefiniowanie spawnu dla dokładnie 1 lisa na starcie symulacji
        spawnConfig.put("farmers", 1); // Zdefiniowanie spawnu dla dokładnie 1 rolnika na starcie symulacji

        Board board = new Board(10, 10, 4); // Utworzenie planszy o wymiarach 10x10, na której nowy losowy obiekt pojawia się co 4 tury

        for(int i = 0; i < spawnConfig.get("potatoes"); i++){ // Pętla generująca początkową liczbę ziemniaków pobraną z mapy konfiguracji
            board.spawnRandomEntity(new Potato(0, 0, 1.0)); // Dodanie na losowe wolne miejsce planszy nowego ziemniaka z masą startową 1.0
        }
        for(int i = 0; i < spawnConfig.get("beetles"); i++){ // Pętla generująca początkową liczbę żuków pobraną z mapy konfiguracji
            board.spawnRandomEntity(new Beetle(0, 0)); // Dodanie na losowe wolne miejsce planszy nowego żuka
        }
        for(int i = 0; i < spawnConfig.get("chickens"); i++){ // Pętla generująca początkową liczbę kurczaków pobraną z mapy konfiguracji
            board.spawnRandomEntity(new Chicken(0, 0)); // Dodanie na losowe wolne miejsce planszy nowego kurczaka
        }
        for(int i = 0; i < spawnConfig.get("foxes"); i++){ // Pętla generująca początkową liczbę lisów pobraną z mapy konfiguracji
            board.spawnRandomEntity(new Fox(0, 0)); // Dodanie na losowe wolne miejsce planszy nowego lisa
        }
        for(int i = 0; i < spawnConfig.get("farmers"); i++){ // Pętla generująca początkową liczbę rolników pobraną z mapy konfiguracji
            board.spawnRandomEntity(new Farmer(0, 0, 1)); // Dodanie na losowe wolne miejsce planszy nowego rolnika z promieniem ochrony równym 1
        }

        SwingUtilities.invokeLater(() -> { // Przekazanie zadania utworzenia interfejsu do wątku obsługi zdarzeń Swing (EDT)
            simulationGUI gui = new simulationGUI(board); // Inicjalizacja obiektu okna GUI i przekazanie mu przygotowanej planszy z jednostkami
            gui.setVisible(true); // Ustawienie widoczności okna aplikacji na ekranie użytkownika
        });
    }
}