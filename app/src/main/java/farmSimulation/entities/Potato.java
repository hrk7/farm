package farmSimulation.entities; // Definicja pakietu, w którym znajdują się wszystkie jednostki symulacji

import farmSimulation.base.Board; // Importowanie klasy Board z pakietu bazowego do obsługi planszy

public class Potato extends Entity{ // Definicja publicznej klasy Potato dziedziczącej po klasie bazowej Entity
    private double mass; // Prywatne pole przechowujące aktualną masę ziemniaka
    private final double growthRate = 0.5; // Stałe, prywatne pole określające przyrost masy ziemniaka w każdej turze

    public Potato(int x, int y, double initialMass){ // Konstruktor klasy Potato przyjmujący pozycję oraz masę początkową
        super(x, y); // Wywołanie konstruktora klasy nadrzędnej (Entity) w celu ustawienia współrzędnych
        this.mass = initialMass; // Przypisanie przekazanej masy początkowej do pola klasy
    }

    @Override // Nadpisuje metody z klasy bazowej Entity
    public void tick(Board board){ // Metoda obsługująca logikę zachowania ziemniaka w danej turze
        if(mass > 0 && mass < 5.0){ // Jeśli ziemniak istnieje (mass > 0) i jeszcze nie w pełni dojrzał (mass < 5.0)
            mass += growthRate; // Zwiększenie aktualnej masy ziemniaka o wartość stałej growthRate (0.5)
        }
    }

    public void consume(double amount){ // Publiczna metoda obsługująca podjadanie masy ziemniaka
        this.mass = Math.max(0, this.mass - amount); // Zmniejszenie masy o podaną wartość, zabezpieczone przed spadkiem poniżej 0
        System.out.println("Ziemniak na (" + x + "," + y + ") traci masę. Zostało: " + String.format("%.1f", this.mass)); // Wypisanie informacji o aktualnym stanie masy

        if(this.mass <= 0){ // Jeśli masa ziemniaka spadła do zera (lub poniżej)
            System.out.println("Ziemniak na (" + x + "," + y + ") został całkowicie zjedzony!"); // Wypisanie komunikatu o całkowitym zjedzeniu
            die(); // Wywołanie metody uśmiercającej ten obiekt (ustawienie flagi alive na false)
        }
    }

    public double getMass(){ // Getter zwracający aktualną masę ziemniaka
        return mass; // Zwrócenie wartości pola mass
    }

    @Override // Nadpisanie metody pobierania symbolu tekstowego
    public char getSymbol() { // Metoda zwracająca symbol reprezentujący ziemniaka w zależności od jego dojrzałości
        if(mass >= 5.0){ // Sprawdzenie, czy ziemniak osiągnął maksymalną dojrzałość (masa większa bądź równa 5.0)
            return 'O'; // Zwrócenie wielkiej litery 'O' dla w pełni dojrzałego ziemniaka
        }
        return 'o'; // Zwrócenie małej litery 'o' dla wciąż rosnącego, niedojrzałego ziemniaka
    }
}