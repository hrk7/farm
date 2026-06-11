package farmSimulation.entities; // Definicja pakietu, w którym znajdują się wszystkie jednostki symulacji

import farmSimulation.base.Board; // Importowanie klasy Board z pakietu bazowego

import java.util.Random; // Importowanie klasy Random służącej do generowania liczb losowych

/**
 * Klasa reprezentująca kurczaka. Zwierzę hodowlane o podwójnej naturze:
 * zjada szkodniki (stonki), ale w przypadku ich braku podjada uprawy (ziemniaki).
 */
public class Chicken extends Entity { // Definicja publicznej klasy Chicken dziedziczącej po klasie bazowej Entity
    private Random random = new Random(); // Inicjalizacja prywatnego generatora liczb losowych

    public Chicken(int x, int y) { // Konstruktor klasy Chicken przyjmujący początkowe współrzędne X i Y
        super(x, y); // Wywołanie konstruktora klasy nadrzędnej (Entity) w celu ustawienia pozycji
    }

    /**
     * Implementacja tury kurczaka: priorytetowe zjadanie stonek,
     * w ostateczności jedzenie ziemniaków lub losowy ruch.
     * * @param board Referencja do głównej planszy.
     */
    @Override // Nadpisanie metody z klasy bazowej Entity
    public void tick(Board board) { // Metoda obsługująca logikę zachowania kurczaka w danej turze
        if (!isAlive()) return; // Jeśli kurczak nie żyje, przerwij wykonywanie tury

        Beetle nearbyBeetle = board.findBeetleNearby(x,y,1); // Szukanie stonki w najbliższym otoczeniu (promień 1)
        if(nearbyBeetle != null && nearbyBeetle.isAlive()){ // Warunek: jeśli w pobliżu znaleziono żywą stonkę
            System.out.println("Kura zjada stonkę na pozycji (" + nearbyBeetle.getX() + "," + nearbyBeetle.getY() + ")."); // Wypisanie komunikatu o zjedzeniu stonki
            nearbyBeetle.die(); // Wywołanie metody powodującej śmierć stonki
            board.addScore(5); // Dodanie 5 punktów do ogólnego wyniku za zlikwidowanie szkodnika
            board.removeEntity(nearbyBeetle); // Usunięcie martwej stonki z planszy i listy obiektów
            board.markAction(); // Zalogowanie wystąpienia ważnej interakcji na planszy
        } else { // Blok wykonany, gdy w najbliższym otoczeniu nie ma żadnej stonki
            Potato nearbyPotato = board.findPotatoNearby(x,y,1); // Szukanie ziemniaka w najbliższym otoczeniu (promień 1)
            if(nearbyPotato != null && nearbyPotato.getMass() > 0){ // Warunek: jeśli znaleziono ziemniaka
                System.out.println("Brak stonki. Kura podjada ziemniaka na (" + nearbyPotato.getX() + "," + nearbyPotato.getY() + ")."); // Komunikat o braku szkodnika i podjadaniu plonów
                nearbyPotato.consume(1.5); // Kurczak podjada ziemniaka, zmniejszając jego masę o 1.5
                if(!nearbyPotato.isAlive()){ // Sprawdzenie, czy ziemniak został całkowicie zjedzony przez kurczaka
                    board.removeEntity(nearbyPotato); // Usunięcie zjedzonego ziemniaka z planszy i listy obiektów
                    board.addScore(3); // Dodanie 3 punktów do wyniku
                }
                board.markAction(); // Zalogowanie wystąpienia ważnej interakcji na planszy
            }
            else {
                int newX = x + random.nextInt(3) - 1; // Wylosowanie nowej pozycji X (przesunięcie o -1, 0 lub 1)
                int newY = y + random.nextInt(3) - 1; // Wylosowanie nowej pozycji Y (przesunięcie o -1, 0 lub 1)
                board.moveEntity(this, newX, newY); // Wywołanie na planszy metody ruchu dla tego kurczaka na nowe współrzędne
            }
        }
    }

    @Override // Nadpisanie metody pobierania symbolu tekstowego
    public char getSymbol() { // Metoda zwracająca symbol reprezentujący kurczaka
        return 'C'; // Zwrócenie znaku 'C' (Chicken) jako tekstowej reprezentacji obiektu
    }
}