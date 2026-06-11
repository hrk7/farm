package farmSimulation.entities; // Definicja pakietu, w którym znajdują się wszystkie jednostki symulacji

import farmSimulation.base.Board; // Importowanie klasy Board z pakietu bazowego do obsługi planszy
import java.util.Random; // Importowanie klasy Random służącej do generowania liczb losowych

/**
 * Klasa reprezentująca rolnika. Główna jednostka sterowana algorytmicznie,
 * odpowiedzialna za zbieranie plonów oraz eliminację szkodników i drapieżników.
 */
public class Farmer extends Entity { // Definicja publicznej klasy Farmer dziedziczącej po klasie bazowej Entity
    private int protectionRadius; // Prywatne pole określające promień ochrony, w którym farmer zwalcza szkodniki
    private int visionRadius = 3; // Prywatne pole określające promień wzroku farmera (zasięg szukania ziemniaków)
    private Random random = new Random(); // Inicjalizacja prywatnego generatora liczb losowych

    /**
     * Konstruktor klasy Farmer.
     * * @param x Początkowa współrzędna X.
     * @param y Początkowa współrzędna Y.
     * @param protectionRadius Promień, w którym farmer automatycznie niszczy zagrożenia (lisy, stonki).
     */
    public Farmer(int x, int y, int protectionRadius) { // Konstruktor klasy Farmer przyjmujący współrzędne oraz promień ochrony
        super(x, y); // Wywołanie konstruktora klasy nadrzędnej (Entity) w celu ustawienia pozycji farmera
        this.protectionRadius = protectionRadius; // Przypisanie przekazanego promienia ochrony do pola klasy
    }

    /**
     * Implementacja tury rolnika: szukanie najbliższego ziemniaka, przemieszczanie się,
     * zbieranie dojrzałych plonów oraz zwalczanie szkodników w strefie ochrony.
     * * @param board Referencja do głównej planszy.
     */
    @Override // Nadpisanie metody z klasy bazowej Entity
    public void tick(Board board) { // Metoda obsługująca logikę zachowania farmera w danej turze
        if (!isAlive()) return; // Jeśli farmer nie żyje, przerwij wykonywanie tury

        Potato target = findClosestMaturePotato(board, visionRadius); // Szukanie najbliższego dojrzałego ziemniaka w zasięgu wzroku
        int newX = x; // Inicjalizacja tymczasowej zmiennej dla nowej pozycji X wartością obecną
        int newY = y; // Inicjalizacja tymczasowej zmiennej dla nowej pozycji Y wartością obecną

        if (target != null) { // Warunek: jeśli znaleziono dojrzałego ziemniaka w zasięgu wzroku
            newX += Integer.compare(target.getX(), x); // Wyznaczenie kierunku ruchu X w stronę ziemniaka (-1, 0 lub 1)
            newY += Integer.compare(target.getY(), y); // Wyznaczenie kierunku ruchu Y w stronę ziemniaka (-1, 0 lub 1)
        } else {
            newX += random.nextInt(3) - 1; // Losowy ruch w osi X (przesunięcie o -1, 0 lub 1)
            newY += random.nextInt(3) - 1; // Losowy ruch w osi Y (przesunięcie o -1, 0 lub 1)
        }

        board.moveEntity(this, newX, newY); // Wywołanie na planszy metody ruchu w celu przemieszczenia farmera

        Potato nearbyPotato = board.findPotatoNearby(x, y, 1); // Szukanie ziemniaka w bezpośrednim sąsiedztwie (promień 1)
        if (nearbyPotato != null && nearbyPotato.getMass() >= 5.0) { // Warunek: jeśli znaleziono ziemniaka i jest on dojrzały (masa >= 5.0)
            int targetX = nearbyPotato.getX(); // Zapamiętanie pozycji X zbieranego ziemniaka
            int targetY = nearbyPotato.getY(); // Zapamiętanie pozycji Y zbieranego ziemniaka

            harvest(nearbyPotato); // Przeprowadzenie zbioru (wyczyszczenie masy ziemniaka)
            board.removeEntity(nearbyPotato); // Usunięcie zebranego ziemniaka z planszy i listy obiektów
            board.addScore(10); // Dodanie 10 punktów do ogólnego wyniku za pomyślny zbiór plonów

            System.out.println("Farmer zbiera ziemniaka na pozycji (" + targetX + "," + targetY + ")."); // Wypisanie komunikatu o zbiorach
            board.markAction(); // Zalogowanie wystąpienia ważnej akcji w tej turze
        }

        Beetle nearbyBeetle = board.findBeetleNearby(x, y, protectionRadius); // Szukanie stonki w promieniu ochrony farmera
        if (nearbyBeetle != null && nearbyBeetle.isAlive()) { // Jeśli w strefie ochrony znaleziono żywą stonkę
            int targetX = nearbyBeetle.getX(); // Zapamiętanie pozycji X stonki
            int targetY = nearbyBeetle.getY(); // Zapamiętanie pozycji Y stonki

            board.addScore(1); // Dodanie 1 punktu do wyniku za wyeliminowanie szkodnika
            nearbyBeetle.die(); // Wywołanie metody powodującej śmierć stonki
            board.removeEntity(nearbyBeetle); // Usunięcie martwej stonki z planszy i listy obiektów
            System.out.println("Farmer zabija stonkę na pozycji (" + targetX + "," + targetY + ")."); // Wypisanie komunikatu o zabiciu stonki
            board.markAction(); // Zalogowanie wystąpienia ważnej akcji w tej turze
        }

        Fox nearbyFox = board.findFoxNearby(x, y, protectionRadius); // Szukanie lisa w promieniu ochrony farmera
        if(nearbyFox != null && nearbyFox.isAlive()){ // Jeśli w strefie ochrony znaleziono żywego lisa
            int targetX = nearbyFox.getX(); // Zapamiętanie pozycji X lisa
            int targetY = nearbyFox.getY(); // Zapamiętanie pozycji Y lisa

            nearbyFox.die(); // Wywołanie metody powodującej śmierć lisa
            board.addScore(15); // Dodanie 15 punktów do ogólnego wyniku za upolowanie drapieżnika
            board.removeEntity(nearbyFox); // Usunięcie martwego lisa z planszy i listy obiektów
            System.out.println("Farmer zabija lisa na pozycji (" + targetX + "," + targetY + ")."); // Wypisanie komunikatu o zabiciu lisa
            board.markAction(); // Zalogowanie wystąpienia ważnej akcji w tej turze
        }
    }

    private Potato findClosestMaturePotato(Board board, int radius) { // Prywatna metoda szukająca najbliższego dojrzałego ziemniaka
        Potato closest = null; // Inicjalizacja zmiennej przechowującej najbliższy znaleziony obiekt wartością null
        int minDistance = Integer.MAX_VALUE; // Inicjalizacja zmiennej na minimalny dystans najwyższą możliwą wartością int

        for (int dx = -radius; dx <= radius; dx++) { // Pętla skanująca otoczenie w poziomie (oś X) w zadanym promieniu
            for (int dy = -radius; dy <= radius; dy++) { // Pętla skanująca otoczenie w pionie (oś Y) w zadanym promieniu
                int checkX = x + dx; // Obliczenie pełnej współrzędnej X sprawdzanego pola
                int checkY = y + dy; // Obliczenie pełnej współrzędnej Y sprawdzanego pola

                Entity e = board.getEntityAt(checkX, checkY); // Pobranie obiektu znajdującego się na sprawdzanym polu
                if (e instanceof Potato) { // Sprawdzenie, czy pobrany obiekt istnieje i jest instancją klasy Potato
                    Potato p = (Potato) e; // Rzutowanie obiektu e na konkretny typ Potato
                    if (p.getMass() >= 5.0) { // Sprawdzenie, czy ten konkretny ziemniak jest już dojrzały (masa >= 5.0)
                        int dist = Math.max(Math.abs(dx), Math.abs(dy)); // Obliczenie odległości
                        if (dist < minDistance) { // Jeśli ta odległość jest mniejsza od dotychczas najmniejszej
                            minDistance = dist; // Aktualizacja wartości najmniejszego dystansu
                            closest = p; // Zapamiętanie tego ziemniaka jako najbliższego dojrzałego
                        }
                    }
                }
            }
        }
        return closest; // Zwrot referencji do najbliższego dojrzałego ziemniaka lub null, jeśli nic nie znaleziono
    }

    /**
     * Przeprowadza zbiór ziemniaka, konsumując całą jego masę.
     * * @param potato Obiekt dojrzałego ziemniaka przeznaczony do zebrania.
     */
    public void harvest(Potato potato) { // Publiczna metoda realizująca proces zbioru ziemniaka
        double harvestedMass = potato.getMass(); // Pobranie aktualnej masy ziemniaka przeznaczonego do zbioru
        potato.consume(harvestedMass); // Zredukowanie masy ziemniaka do zera poprzez skonsumowanie całej jego masy
    }

    @Override // Nadpisanie metody pobierania symbolu tekstowego
    public char getSymbol() { // Metoda zwracająca symbol reprezentujący farmera
        return 'F'; // Zwrócenie znaku 'F' (Farmer) jako tekstowej reprezentacji obiektu na planszy
    }
}