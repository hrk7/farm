package farmSimulation.base; // Definicja pakietu, w którym znajduje się klasa bazowa planszy
import farmSimulation.entities.*; // Importowanie wszystkich klas jednostek z pakietu entities
import java.util.ArrayList; // Importowanie klasy ArrayList do dynamicznej listy obiektów
import java.util.List; // Importowanie interfejsu List do obsługi kolekcji
import java.util.Random; // Importowanie klasy Random do generowania liczb losowych

public class Board { // Definicja publicznej klasy Board
    private int width; // Pole przechowujące szerokość planszy
    private int height; // Pole przechowujące wysokość planszy
    private int spawnRate; // Częstotliwość pojawiania się nowych jednostek
    private Entity[][] grid; // Dwuwymiarowa tablica przechowująca pozycje obiektów na planszy
    private List<Entity> entities; // Lista przechowująca wszystkie aktywne obiekty na planszy
    private Random random = new Random(); // Inicjalizacja generatora liczb losowych
    private int tickCount = 0; // Licznik wykonanych tur
    private boolean actionLogged = false; // Flaga sprawdzająca, czy w danej turze doszło do ważnej akcji
    private int score = 0; // Pole przechowujące aktualny wynik punktowy

    public Board(int width, int height, int spawnRate){ // Konstruktor klasy Board przyjmujący wymiary i częstotliwość pojawiania się jednostek
        this.width = width; // Przypisanie szerokości planszy z parametru do pola klasy
        this.height = height; // Przypisanie wysokości planszy z parametru do pola klasy
        this.spawnRate = spawnRate; // Przypisanie częstotliwości pojawiania się jednostek z parametru do pola klasy
        this.grid = new Entity[height][width]; // Tworzenie pustej siatki tablicy o zadanych wymiarach
        this.entities = new ArrayList<>(); // Inicjalizacja pustej listy na obiekty gry
    }

    public void addEntity(Entity entity){ // Metoda dodająca nową jednostkę na planszę
        if(isValid(entity.getX(), entity.getY()) && grid[entity.getY()][entity.getX()] == null){ // Sprawdzenie, czy pozycja jest poprawna i pusta
            grid[entity.getY()][entity.getX()] = entity; // Umieszczenie jednostki na planszy
            entities.add(entity); // Dodanie obiektu do listy aktywnych jednostek
            System.out.println("Dodano: " + entity.getClass().getSimpleName() + " na pozycji (" + entity.getX() + "," + entity.getY() + ")."); // Komunikat o sukcesie
        } else { // Blok wykonany, gdy miejsce jest zajęte lub nieprawidłowe
            System.out.println("Błąd: Nie można dodać " + entity.getClass().getSimpleName() + " na (" + entity.getX() + "," + entity.getY() + "). Miejsce zajęte lub poza planszą."); // Komunikat o błędzie
        }
    }

    public void removeEntity(Entity entity){ // Metoda usuwająca jednostkę z planszy
        if(isValid(entity.getX(), entity.getY()) && grid[entity.getY()][entity.getX()] == entity){ // Sprawdzenie, czy obiekt faktycznie znajduje się na swojej pozycji w siatce
            grid[entity.getY()][entity.getX()] = null; // Wyczyszczenie pola na siatce planszy
        }
        entities.remove(entity); // Usunięcie obiektu z listy aktywnych jednostek
    }

    public void markAction() { // Metoda oznaczająca, że w tej turze zaszła istotna interakcja
        this.actionLogged = true; // Ustawienie flagi akcji na true
    }

    public void addScore(int points) { // Metoda dodająca punkty do ogólnego wyniku
        this.score += points; // Zwiększenie aktualnego wyniku o podaną liczbę punktów
    }

    public int getScore() { // Metoda zwracający aktualną liczbę punktów
        return this.score; // Zwrócenie wartości pola score
    }

    public void spawnRandomEntity(Entity entity){ // Metoda losująca i tworząca nowy obiekt na planszy
        int[] freePosition = findRandomFreePosition(); // Próba znalezienia wolnego miejsca na planszy
        if (freePosition == null){ // Sprawdzenie, czy nie ma już żadnego wolnego miejsca
            System.out.println("Brak wolnego miejsca na planszy na nowy obiekt"); // Komunikat o braku miejsca
            return; // Przerwanie działania metody
        }
        int x = freePosition[0]; // Pobranie wylosowanej współrzędnej X
        int y = freePosition[1]; // Pobranie wylosowanej współrzędnej Y

        int chance = random.nextInt(100); // Wylosowanie liczby z zakresu od 0 do 99 określającej szansę

        if(entity != null) { // Sprawdzenie, czy przekazano konkretny szablon obiektu jako sugestię spawnu
            switch (entity) { // Instrukcja wyboru na podstawie typu przekazanego obiektu (Pattern Matching)
                case Potato potato -> chance = 39; // Jeśli to Potato, ustaw szansę wymuszającą spawn ziemniaka
                case Beetle beetle -> chance = 69; // Jeśli to Beetle, ustaw szansę wymuszającą spawn stonki
                case Chicken chicken -> chance = 89; // Jeśli to Chicken, ustaw szansę wymuszającą spawn kurczaka
                case Fox fox -> chance = 99; // Jeśli to Fox, ustaw szansę wymuszającą spawn lisa
                case Farmer farmer -> chance = 100; // Jeśli to Farmer, ustaw szansę wymuszającą spawn rolnika
                default -> { // Domyślna ścieżka dla nieznanych typów obiektów
                }
            }
        }

        if(chance < 40){ // Jeśli wskaźnik szansy jest mniejszy niż 40 (0-39)
            Potato newPotato = new Potato(x, y, 1.0); // Stwórz nowy ziemniak
            addEntity(newPotato); // Dodaj nowo stworzonego ziemniaka na planszę
        } else if (chance < 70){ // Jeśli wskaźnik szansy jest pomiędzy 40 a 69
            Beetle newBeetle = new Beetle(x, y); // Stwórz nową stonkę
            addEntity(newBeetle); // Dodaj nowo stworzoną stonkę na planszę
        } else if (chance < 90){ // Jeśli wskaźnik szansy jest pomiędzy 70 a 89
            Chicken newChicken = new Chicken(x, y); // Stwórz nowego kurczaka
            addEntity(newChicken); // Dodaj nowo stworzonego kurczaka na planszę
        } else if (chance == 100){ // Jeśli wskaźnik szansy wynosi dokładnie 100
            Farmer newFarmer = new Farmer(x, y, 2); // Stwórz nowego rolnika z zasięgiem 2
            addEntity(newFarmer); // Dodaj nowo stworzonego rolnika na planszę
        } else { // W każdym innym przypadku (szansa 90-99)
            Fox newFox = new Fox(x, y); // Stwórz nowy obiekt lisa
            addEntity(newFox); // Dodaj nowo stworzonego lisa na planszę
        }
    }

    private int[] findRandomFreePosition(){ // Prywatna metoda szukająca wolnego miejsca na planszie
        for(int i = 0; i < 100; i++){ // Pętla próbująca 100 razy szybko wylosować wolne miejsce
            int rx = random.nextInt(width); // Losowanie współrzędnej X w granicach szerokości planszy
            int ry = random.nextInt(height); // Losowanie współrzędnej Y w granicach wysokości planszy
            if(grid[ry][rx] == null){ // Sprawdzenie, czy wylosowane miejsce na siatce jest puste
                return new int[]{rx, ry}; // Zwrócenie współrzędnych jako dwuelementowa tablica
            }
        }
        for(int y = 0; y < height; y++){ // Awaryjne przeszukiwanie planszy wiersz po wierszu (jeśli losowanie zawiodło)
            for(int x = 0; x < width; x++){ // Przeszukiwanie każdej kolumny w danym wierszu
                if(grid[y][x] == null){ // Sprawdzenie czy bieżąca komórka jest pusta
                    return new int[]{x, y}; // Zwrócenie pierwszego znalezionego wolnego miejsca
                }
            }
        }
        return null; // Zwrócenie null, jeśli cała plansza jest całkowicie zapełniona
    }

    public void nextTick(){ // Metoda obsługująca logikę przejścia do kolejnej tury symulacji
        tickCount++; // Inkremacja (zwiększenie o 1) licznika tur

        if(tickCount % spawnRate == 0){ // Sprawdzenie, czy obecna tura jest wielokrotnością wskaźnika spawnRate
            spawnRandomEntity(null); // Wywołanie losowego spawnu nowej jednostki
        }

        actionLogged = false; // Zresetowanie flagi akcji przed rozpoczęciem przetwarzania ruchu jednostek
        System.out.println("\n === TURA " + tickCount + " ==="); // Wypisanie nagłówka informacyjnego o nowej turze

        List<Entity> copy = new ArrayList<>(entities); // Utworzenie kopii listy
        for(Entity e: copy){ // Pierwsza faza ruchu: pętla przetwarzająca tylko ziemniaki
            if(e.isAlive() && e.getClass() == Potato.class){ // Sprawdzenie czy obiekt żyje i jest dokładnie klasy Potato
                e.tick(this); // Wywołanie logiki tury dla ziemniaka
            }
        }
        for(Entity e: copy){ // Druga faza ruchu: pętla przetwarzająca tylko stonek
            if(e.isAlive() && e.getClass() == Beetle.class){ // Sprawdzenie czy obiekt żyje i jest dokładnie klasy Beetle
                e.tick(this); // Wywołanie logiki tury dla stonki
            }
        }
        for(Entity e: copy){ // Trzecia faza ruchu: pętla przetwarzająca tylko lisy
            if(e.isAlive() && e.getClass() == Fox.class){ // Sprawdzenie czy obiekt żyje i jest dokładnie klasy Fox
                e.tick(this); // Wywołanie logiki tury dla lisa
            }
        }
        for(Entity e: copy){ // Czwarta faza ruchu: pętla przetwarzająca tylko kurczaki
            if(e.isAlive() && e.getClass() == Chicken.class){ // Sprawdzenie czy obiekt żyje i jest dokładnie klasy Chicken
                e.tick(this); // Wywołanie logiki tury dla kurczaka
            }
        }
        for(Entity e: copy){ // Piąta faza ruchu: pętla przetwarzająca tylko rolników
            if(e.isAlive() && e.getClass() == Farmer.class){ // Sprawdzenie czy obiekt żyje i jest dokładnie klasy Farmer
                e.tick(this); // Wywołanie logiki tury dla rolnika
            }
        }

        if (!actionLogged) { // Sprawdzenie po zakończeniu wszystkich ruchów, czy flaga akcji pozostała fałszywa
            System.out.println("Tylko ruch na planszy, brak specjalnych interakcji."); // Wyświetlenie komunikatu o spokojnej turze
        }
    }

    public void moveEntity(Entity entity, int newX, int newY){ // Metoda przemieszczająca obiekt na nowe współrzędne
        int dx = newX - entity.getX(); // Obliczenie przesunięcia (delty) na osi X
        int dy = newY - entity.getY(); // Obliczenie przesunięcia (delty) na osi Y

        int targetX = newX; // Inicjalizacja docelowej pozycji X
        int targetY = newY; // Inicjalizacja docelowej pozycji Y

        if (targetX < 0 || targetX >= width) { // Obsługa odbicia od krawędzi: jeśli X wychodzi poza szerokość planszy
            targetX = entity.getX() - dx; // Odwrócenie kierunku ruchu w osi X (odbicie lustrzane)
        }
        if (targetY < 0 || targetY >= height) { // Obsługa odbicia od krawędzi: jeśli Y wychodzi poza wysokość planszy
            targetY = entity.getY() - dy; // Odwrócenie kierunku ruchu w osi Y (odbicie lustrzane)
        }

        if(isValid(targetX, targetY) && grid[targetY][targetX] == null){ // Sprawdzenie czy pozycja po korekcie jest prawidłowa i wolna
            grid[entity.getY()][entity.getX()] = null; // Zwolnienie starego miejsca na siatce planszy

            entity.setPosition(targetX, targetY); // Aktualizacja wewnętrznych współrzędnych obiektu

            grid[targetY][targetX] = entity; // Przypisanie obiektu do nowego miejsca na siatce planszy
        }
    }

    private boolean isValid(int x, int y){ // Prywatna metoda pomocnicza weryfikująca poprawność współrzędnych
        return  x >= 0 && x < width && y >= 0 && y < height; // Zwraca true, jeśli punkt mieści się w granicach tablicy planszy
    }

    public Potato findPotatoNearby(int cx, int cy, int r){ // Szukanie ziemniaka w określonym promieniu 'r' od punktu (cx, cy)
        return (Potato) findTypeNearby(cx, cy, Potato.class, r); // Rzutowanie wyniku ogólnego wyszukiwania na klasę Potato
    }

    public Beetle findBeetleNearby(int cx, int cy, int r){ // Szukanie żuka w określonym promieniu 'r' od punktu (cx, cy)
        return (Beetle) findTypeNearby(cx, cy, Beetle.class, r); // Rzutowanie wyniku ogólnego wyszukiwania na klasę Beetle
    }

    public Chicken findChickenNearby(int cx, int cy, int r){ // Szukanie kurczaka w określonym promieniu 'r' od punktu (cx, cy)
        return (Chicken) findTypeNearby(cx, cy, Chicken.class, r); // Rzutowanie wyniku ogólnego wyszukiwania na klasę Chicken
    }

    public Fox findFoxNearby(int cx, int cy, int r){ // Szukanie lisa w określonym promieniu 'r' od punktu (cx, cy)
        return (Fox) findTypeNearby(cx, cy, Fox.class, r); // Rzutowanie wyniku ogólnego wyszukiwania na klasę Fox
    }

    private Entity findTypeNearby(int cx, int cy, Class<?> type, int range) { // Prywatna, ogólna metoda skanująca otoczenie w poszukiwaniu danego typu klasy
        for (int dx = -range; dx <= range; dx++) { // Pętla iterująca po relatywnym przesunięciu X w zasięgu ramki pola
            for (int dy = -range; dy <= range; dy++) { // Pętla iterująca po relatywnym przesunięciu Y w zasięgu ramki pola
                int nx = cx + dx; // Wyznaczenie bezwzględnej współrzędnej sąsiedniej komórki X
                int ny = cy + dy; // Wyznaczenie bezwzględnej współrzędnej sąsiedniej komórki Y
                if (isValid(nx, ny) && grid[ny][nx] != null && type.isInstance(grid[ny][nx])) { // Sprawdzenie poprawności, niepustości i zgodności typu obiektu
                    return grid[ny][nx]; // Zwrócenie pierwszego dopasowanego obiektu znalezionego w otoczeniu
                }
            }
        }
        return null; // Zwrócenie null, jeśli w zadanym obszarze nie ma obiektu poszukiwanego typu
    }

    public int getWidth() { // Getter dla szerokości planszy
        return this.width; // Zwrócenie wartości pola width
    }

    public int getHeight() { // Getter dla wysokości planszy
        return this.height; // Zwrócenie wartości pola height
    }

    public Entity getEntityAt(int x, int y) { // Metoda pobierająca obiekt z konkretnych współrzędnych
        if (x >= 0 && x < width && y >= 0 && y < height) { // Zabezpieczenie przed wyjściem poza indeksy tablicy
            return this.grid[y][x]; // Zwrócenie obiektu znajdującego się na podanej pozycji w siatce
        }
        return null; // Zwrócenie null, jeśli zapytanie dotyczyło pozycji poza planszą
    }
}

