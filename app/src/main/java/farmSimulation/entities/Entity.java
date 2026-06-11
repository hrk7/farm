package farmSimulation.entities; // Definicja pakietu, w którym znajdują się wszystkie jednostki

import farmSimulation.base.Board; // Importowanie klasy Board z pakietu bazowego do obsługi planszy

/**
 * Abstrakcyjna klasa bazowa reprezentująca wszystkie jednostki w symulacji.
 * Definiuje wspólne właściwości, takie jak współrzędne na planszy oraz stan życia.
 */
public abstract class Entity { // Definicja publicznej klasy abstrakcyjnej Entity, będącej wzorcem dla wszystkich obiektów
    protected int x; // Chronione pole przechowujące współrzędną X (kolumnę) pozycji obiektu
    protected int y; // Chronione pole przechowujące współrzędną Y (wiersz) pozycji obiektu
    protected boolean alive = true; // Chroniona flaga określająca stan życiowy obiektu (domyślnie żywy)

    /**
     * Konstruktor inicjujący pozycję jednostki na planszy.
     * * @param x Początkowa współrzędna X (kolumna).
     * @param y Początkowa współrzędna Y (wiersz).
     */
    public Entity(int x, int y) { // Konstruktor klasy Entity, przyjmujący początkowe współrzędne obiektu
        this.x = x; // Przypisanie wartości parametru x do pola chronionego klasy
        this.y = y; // Przypisanie wartości parametru y do pola chronionego klasy
    }

    /**
     * Główna metoda logiki wywoływana w każdej turze symulacji.
     * Wymusza na klasach pochodnych implementację własnego zachowania.
     * * @param board Referencja do obiektu planszy.
     */
    public abstract void tick(Board board); // Abstrakcyjna metoda wymuszająca implementację logiki tury w klasach pochodnych

    public int getX() { // Getter zwracający aktualną współrzędną X obiektu
        return x; // Zwrócenie wartości pola x
    }
    public int getY() { // Getter zwracający aktualną współrzędną Y obiektu
        return y; // Zwrócenie wartości pola y
    }

    /**
     * Zmienia aktualną pozycję obiektu na planszy.
     * * @param x Nowa współrzędna X.
     * @param y Nowa współrzędna Y.
     */
    public void setPosition(int x, int y) { // Metoda umożliwiająca zmianę pozycji obiektu na planszy
        this.x = x; // Ustawienie nowej współrzędnej X
        this.y = y; // Ustawienie nowej współrzędnej Y
    }
    public boolean isAlive(){ return alive;} // Metoda sprawdzająca, czy obiekt jest żywy
    /**
     * Uśmierca obiekt, zmieniając jego flagę aktywności na fałsz.
     * Obiekty martwe nie wykonują akcji i są usuwane z planszy.
     */
    public void die() { this.alive = false;} // Metoda uśmiercająca obiekt poprzez zmianę flagi alive na false
    public abstract char getSymbol(); // Abstrakcyjna metoda wymuszająca implementację znaku tekstowego reprezentującego obiekt
}