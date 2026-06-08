package farmSimulation.entities; // Definicja pakietu, w którym znajdują się wszystkie jednostki symulacji

import farmSimulation.base.Board; // Importowanie klasy Board z pakietu bazowego do obsługi planszy
import java.util.Random; // Importowanie klasy Random służącej do generowania liczb losowych

public class Fox extends Entity { // Definicja publicznej klasy Fox dziedziczącej po klasie bazowej Entity
    private Random random = new Random(); // Inicjalizacja prywatnego generatora liczb losowych

    public Fox(int x, int y) { // Konstruktor klasy Fox przyjmujący początkowe współrzędne X i Y
        super(x, y); // Wywołanie konstruktora klasy nadrzędnej (Entity) w celu ustawienia pozycji
    }

    public void hunt(Chicken chicken) { // Publiczna metoda realizująca polowanie na kurczaka
        if (chicken != null && chicken.isAlive()) { // Sprawdzenie, czy przekazany obiekt kurczaka istnieje i żyje
            chicken.die(); // Wywołanie metody powodującej śmierć kurczaka
        }
    }

    @Override // Nadpisanie metody z klasy bazowej Entity
    public void tick(Board board) { // Metoda obsługująca logikę zachowania lisa w danej turze
        if (!isAlive()) return; // Jeśli lis nie żyje, przerwij wykonywanie tury

        Chicken targetChicken = board.findChickenNearby(x, y, 1); // Szukanie kurczaka w najbliższym otoczeniu (promień 1)

        if (targetChicken != null && targetChicken.isAlive()) { // Warunek: jeśli w pobliżu znaleziono żywego kurczaka
            int targetX = targetChicken.getX(); // Zapamiętanie pozycji X
            int targetY = targetChicken.getY(); // Zapamiętanie pozycji Y

            hunt(targetChicken); // Wywołanie metody ataku na znalezionego kurczaka
            board.removeEntity(targetChicken); // Usunięcie martwego kurczaka z planszy i listy obiektów
            board.addScore(-10); // Odjęcie 10 punktów od ogólnego wyniku za stratę kurczaka
            System.out.println("Lis zabija kurczaka na pozycji (" + targetX + "," + targetY + ")."); // Wypisanie komunikatu o udanym polowaniu
            board.markAction(); // Zalogowanie wystąpienia ważnej interakcji na planszy
        } else {
            int newX = x + random.nextInt(5) - 2; // Wylosowanie nowej pozycji X (przesunięcie w zakresie od -2 do 2)
            int newY = y + random.nextInt(5) - 2; // Wylosowanie nowej pozycji Y (przesunięcie w zakresie od -2 do 2)
            board.moveEntity(this, newX, newY); // Wywołanie na planszy metody ruchu dla tego lisa na nowe współrzędne
        }
    }

    @Override // Nadpisanie metody pobierania symbolu tekstowego
    public char getSymbol() { // Metoda zwracająca symbol reprezentujący lisa
        return '狐'; // Zwrócenie znaku '狐' (chiński znak oznaczający lisa) jako reprezentacji obiektu
    }
}