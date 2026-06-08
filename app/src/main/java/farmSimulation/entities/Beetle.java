package farmSimulation.entities; // Definicja pakietu, w którym znajdują się wszystkie jednostki
import farmSimulation.base.Board; // Importowanie klasy Board z pakietu bazowego do obsługi planszy

import java.util.Random; // Importowanie klasy Random służącej do generowania liczb losowych

public class Beetle extends Entity{ // Definicja publicznej klasy Beetle dziedziczącej po klasie bazowej Entity
    private Random random = new Random(); // Inicjalizacja prywatnego generatora liczb losowych dla każdej stonki

    public Beetle(int x, int y){ // Konstruktor klasy Beetle przyjmujący początkowe współrzędne X i Y
        super(x, y); // Wywołanie konstruktora klasy nadrzędnej (Entity) w celu ustawienia pozycji
    }

    @Override // Nadpisanie metody z klasy bazowej Entity
    public void tick(Board board){ // Metoda obsługująca logikę zachowania stonki w danej turze
        if(!isAlive()) return; // Jeśli stonka nie żyje, przerwij wykonywanie tury

        Potato nearbyPotato = board.findPotatoNearby(x, y, 1); // Szukanie ziemniaka w najbliższym otoczeniu (promień 1)
        if(nearbyPotato != null){ // Jeśli w pobliżu znaleziono jakiegoś ziemniaka
            nearbyPotato.consume(1.0); // Stonka nadżera znalezionego ziemniaka, zmniejszając jego stan o 1.0
            if(!nearbyPotato.isAlive()){ // Sprawdzenie, czy ziemniak został całkowicie zjedzony
                board.removeEntity(nearbyPotato); // Usunięcie martwego ziemniaka z planszy i listy obiektów
                board.addScore(-3); // Odjęcie 3 punktów od ogólnego wyniku za stratę plonów
            }
            System.out.println("Stonka żeruje na ziemniaku na pozycji (" + x + "," + y + ")."); // Wypisanie komunikatu o żerowaniu
            board.markAction(); // Zalogowanie wystąpienia ważnej interakcji na planszy
        } else {
            int newX = x + random.nextInt(3) - 1; // Wylosowanie nowej pozycji X (przesunięcie o -1, 0 lub 1)
            int newY = y + random.nextInt(3) - 1; // Wylosowanie nowej pozycji Y (przesunięcie o -1, 0 lub 1)
            board.moveEntity(this, newX, newY); // Wywołanie na planszy metody ruchu dla tego stonki na nowe współrzędne
        }
    }
    @Override // Nadpisanie metody pobierania symbolu tekstowego
    public char getSymbol() { return 'B'; } // Zwrócenie znaku 'B' jako tekstowej reprezentacji stonki
}
