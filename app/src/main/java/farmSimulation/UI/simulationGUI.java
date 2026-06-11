package farmSimulation.UI; // Definicja pakietu, w którym znajduje się klasa interfejsu graficznego użytkownika (GUI)

import farmSimulation.base.Board; // Importowanie klasy Board z pakietu bazowego do zarządzania stanem symulacji
import farmSimulation.entities.*; // Importowanie wszystkich klas jednostek do rozpoznawania obiektów graficznych

import javax.swing.*; // Importowanie biblioteki Swing służącej do budowania okienkowego interfejsu użytkownika
import java.awt.*; // Importowanie podstawowego pakietu AWT m.in. do obsługi kolorów, czcionek i układów komponentów
import java.awt.event.ActionEvent; // Importowanie klasy ActionEvent reprezentującej zdarzenie (np. kliknięcie przycisku)
import java.awt.event.ActionListener; // Importowanie interfejsu ActionListener do przechwytywania i obsługi zdarzeń
import javax.imageio.ImageIO; // Importowanie klasy ImageIO służącej do wczytywania plików graficznych
import java.io.File; // Importowanie klasy File do reprezentacji ścieżek plików w systemie
import java.io.IOException; // Importowanie klasy wyjątku IOException obsługującej błędy wejścia/wyjścia (np. brak pliku)

/**
 * Główna klasa interfejsu graficznego (GUI) symulacji oparta na bibliotece Swing.
 * Odpowiada za renderowanie siatki obiektów za pomocą zewnętrznych tekstur,
 * wyświetlanie punktacji oraz obsługę panelu kontrolnego (prędkość, autoplay).
 */
public class simulationGUI extends JFrame { // Definicja publicznej klasy okna GUI dziedziczącej po standardowym JFrame
    private Board board; // Prywatne pole przechowujące referencję do zarządzanej planszy symulacji
    private GridPanel gridPanel; // Prywatne pole przechowujące panel graficzny odpowiedzialny za rysowanie siatki
    private Timer timer; // Prywatny obiekt zegara Swing służący do automatycznego odpalania kolejnych kroków symulacji
    private JLabel scoreLabel; // Prywatny etykieta tekstowa wyświetlająca aktualny wynik punktowy symulacji

    private Image farmerImg; // Prywatne pole przechowujące grafikę reprezentującą rolnika
    private Image foxImg; // Prywatne pole przechowujące grafikę reprezentującą lisa
    private Image chickenImg; // Prywatne pole przechowujące grafikę reprezentującą kurczaka
    private Image beetleImg; // Prywatne pole przechowujące grafikę reprezentującą żuka (stonkę)
    private Image potatoImg; // Prywatne pole przechowujące grafikę reprezentującą małego (niedojrzałego) ziemniaka
    private Image potatoBigImg; // Prywatne pole przechowujące grafikę reprezentującą dużego (dojrzałego) ziemniaka

    public simulationGUI(Board board) { // Konstruktor klasy GUI przyjmujący obiekt planszy do powiązania z interfejsem
        this.board = board; // Przypisanie przekazanej w parametrze planszy do pola instancji klasy

        try { // Blok try-catch zabezpieczający proces ładowania zewnętrznych plików graficznych
            farmerImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/farmer_new.png")); // Wczytanie tekstury rolnika z podanej ścieżki
            foxImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/fox_new.png")); // Wczytanie tekstury lisa z podanej ścieżki
            chickenImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/chicken_new.png")); // Wczytanie tekstury kurczaka z podanej ścieżki
            beetleImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/beetle_new.png")); // Wczytanie tekstury żuka z podanej ścieżki
            potatoImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/potato_new.png")); // Wczytanie tekstury małego ziemniaka z podanej ścieżki
            potatoBigImg = ImageIO.read(new File("app/src/main/java/farmSimulation/images/potatoBig_new.png")); // Wczytanie tekstury dużego ziemniaka z podanej ścieżki
        } catch (IOException e) { // Obsługa błędu w przypadku, gdy któryś z plików graficznych nie zostanie znaleziony
            System.out.println("Błąd ładowania obrazków."); // Wypisanie informacji o problemie w konsoli tekstowej
            e.printStackTrace(); // Drukowanie szczegółowego śladu stosu błędu dla celów debugowania
        }

        setTitle("simulation v2"); // Ustawienie tekstu wyświetlanego na pasku tytułowym okna aplikacji
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Konfiguracja zamknięcia okna tak, aby kończyło działanie całego procesu Javy
        setLayout(new BorderLayout()); // Ustawienie menedżera układu okna na BorderLayout

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Utworzenie górnego panelu z wyśrodkowanym układem elementów
        scoreLabel = new JLabel("Wynik: 0"); // Inicjalizacja etykiety wyniku z początkową wartością zero
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Ustawienie pogrubionej czcionki Arial o rozmiarze 16 dla etykiety wyniku
        topPanel.add(scoreLabel); // Dodanie etykiety punktowej do górnego panelu
        add(topPanel, BorderLayout.NORTH); // Umieszczenie całego górnego panelu w górnej sekcji okna głównego

        gridPanel = new GridPanel(); // Inicjalizacja wewnętrznego panelu rysującego siatkę gry
        add(gridPanel, BorderLayout.CENTER); // Umieszczenie panelu siatki w głównej części okna

        JPanel controlPanel = new JPanel(); // Utworzenie dolnego panelu przeznaczonego na przyciski i pola sterujące
        JButton stepButton = new JButton("Następny krok"); // Utworzenie przycisku do ręcznego wywoływania pojedynczych tur
        JButton autoButton = new JButton("Auto-play"); // Utworzenie przycisku do włączania/wyłączania automatycznej rozgrywki

        JLabel speedLabel = new JLabel("Prędkość (mnożnik): "); // Utworzenie etykiety opisującej pole tekstowe prędkości
        JTextField speedField = new JTextField("1", 3); // Utworzenie pola tekstowego na mnożnik o domyślnej wartości "1" i szerokości 3 kolumn
        speedField.setToolTipText("Wpisz mnożnik i naciśnij Enter"); // Ustawienie dymka podpowiedzi wyświetlanego po najechaniu kursorem myszy

        stepButton.addActionListener(new ActionListener() { // Dodanie nasłuchiwania akcji na przycisk pojedynczego kroku
            @Override // Nadpisanie metody obsługi zdarzenia
            public void actionPerformed(ActionEvent e) { // Metoda uruchamiana po kliknięciu przycisku "Następny krok"
                board.nextTick(); // Wywołanie metody przejścia do kolejnej tury w logice planszy
                scoreLabel.setText("Wynik: " + board.getScore()); // Aktualizacja tekstu etykiety o świeżo pobrany z planszy wynik
                gridPanel.repaint(); // Wymuszenie ponownego przerysowania grafiki na panelu siatki
            }
        });

        timer = new Timer(500, new ActionListener() { // Inicjalizacja timera z domyślnym taktem co 500 milisekund
            @Override // Nadpisanie metody obsługi zdarzenia zegara
            public void actionPerformed(ActionEvent e) { // Metoda wywoływana automatycznie przy każdym tyknięciu zegara
                board.nextTick(); // Wykonanie logicznej tury symulacji na planszy
                scoreLabel.setText("Wynik: " + board.getScore()); // Odświeżenie bieżącego wyniku na ekranie
                gridPanel.repaint(); // Odświeżenie widoku siatki obiektów
            }
        });

        autoButton.addActionListener(new ActionListener() { // Dodanie nasłuchiwania akcji na przycisk automatycznej gry
            @Override // Adnotacja oznaczająca nadpisanie metody obsługi zdarzenia
            public void actionPerformed(ActionEvent e) { // Metoda uruchamiana po kliknięciu przycisku "Auto-play" / "Stop"
                if (timer.isRunning()) { // Sprawdzenie, czy automatyczna symulacja jest w tym momencie uruchomiona
                    timer.stop(); // Zatrzymanie zegara automatycznych tur
                    autoButton.setText("Auto-play"); // Przywrócenie pierwotnego tekstu na przycisku startu
                    stepButton.setEnabled(true); // Ponowne aktywowanie przycisku wykonania pojedynczego kroku ręcznego
                    speedField.setEnabled(true); // Ponowne aktywowanie pola edycji prędkości symulacji
                } else {
                    timer.start(); // Uruchomienie zegara do cyklicznego odpalania tur
                    autoButton.setText("Stop"); // Zmiana napisu na przycisku informująca o możliwości zatrzymania
                    stepButton.setEnabled(false); // Dezaktywacja przycisku kroku ręcznego, by uniknąć konfliktów w trakcie gry automatycznej
                }
            }
        });

        speedField.addActionListener(new ActionListener() { // Dodanie nasłuchiwania na zatwierdzenie wartości (Enter) w polu tekstowym
            @Override // Nadpisanie metody obsługi zdarzenia pola tekstowego
            public void actionPerformed(ActionEvent e) { // Metoda uruchamiana po wciśnięciu klawisza Enter w polu speedField
                try { // Blok try-catch chroniący przed wpisaniem niepoprawnego formatu liczby
                    double multiplier = Double.parseDouble(speedField.getText().replace(',', '.')); // Parsowanie tekstu na liczbę zmiennoprzecinkową, zamieniając przecinki na kropki

                    if (multiplier <= 0) { // Zabezpieczenie przed wpisaniem mnożnika mniejszego bądź równego zero
                        multiplier = 1.0; // Ustawienie bezpiecznej, domyślnej wartości mnożnika
                        speedField.setText("1"); // Zresetowanie tekstu wyświetlanego w polu edycyjnym na "1"
                    }

                    int newDelay = (int) (500 / multiplier); // Obliczenie nowego opóźnienia w milisekundach na podstawie mnożnika prędkości
                    timer.setDelay(Math.max(1, newDelay)); // Ustawienie nowego czasu oczekiwania timera (nie mniejszego niż 1 ms)

                } catch (NumberFormatException ex) { // Obsługa wyjątku, gdy użytkownik wpisze tekst niebędący liczbą
                    speedField.setText("1"); // Przywrócenie domyślnej wartości tekstowej "1" w polu
                    timer.setDelay(500); // Przywrócenie domyślnego opóźnienia timera o wartości 500 milisekund
                }
            }
        });

        controlPanel.add(stepButton); // Dodanie przycisku pojedynczego kroku do panelu kontrolnego
        controlPanel.add(autoButton); // Dodanie przycisku automatycznej gry do panelu kontrolnego
        controlPanel.add(speedLabel); // Dodanie etykiety opisu prędkości do panelu kontrolnego
        controlPanel.add(speedField); // Dodanie pola tekstowego mnożnika do panelu kontrolnego
        add(controlPanel, BorderLayout.SOUTH); // Umieszczenie całego panelu kontrolnego w dolnej części okna

        pack(); // Automatyczne dostosowanie rozmiaru okna głównego do wymiarów wszystkich zawartych w nim komponentów
        setLocationRelativeTo(null); // Wycentrowanie okna aplikacji bezpośrednio na środku ekranu użytkownika
    }

    private class GridPanel extends JPanel { // Definicja wewnętrznej klasy GridPanel dziedziczącej po JPanel, rysującej planszę gry
        private final int CELL_SIZE = 50; // Stała określająca rozmiar pojedynczej komórki siatki w pikselach (kwadrat 50x50)

        public GridPanel() { // Konstruktor klasy wewnętrznej GridPanel
            setPreferredSize(new Dimension(board.getWidth() * CELL_SIZE, board.getHeight() * CELL_SIZE)); // Ustawienie preferowanego rozmiaru panelu na podstawie wymiarów planszy i wielkości kafelka
        }

        @Override // Nadpisanie standardowej metody rysowania komponentu z biblioteki Swing
        protected void paintComponent(Graphics g) { // Metoda wywoływana automatycznie, gdy system potrzebuje odrysować panel na ekranie
            super.paintComponent(g); // Wywołanie metody bazowej w celu prawidłowego oczyszczenia i przygotowania tła panelu

            for (int y = 0; y < board.getHeight(); y++) { // Pętla iterująca po wszystkich wierszach planszy (oś Y)
                for (int x = 0; x < board.getWidth(); x++) { // Pętla zagnieżdżona iterująca po wszystkich kolumnach planszy (oś X)
                    int px = x * CELL_SIZE; // Obliczenie pozycji pikselowej lewego górnego rogu komórki na osi X
                    int py = y * CELL_SIZE; // Obliczenie pozycji pikselowej lewego górnego rogu komórki na osi Y

                    g.setColor(new Color(144, 238, 144)); // Ustawienie koloru pędzla na jasnozielony (trawa)
                    g.fillRect(px, py, CELL_SIZE, CELL_SIZE); // Pokolorowanie wnętrza kwadratu komórki kolorem trawy

                    g.setColor(Color.GRAY); // Zmiana koloru pędzla graficznego na szary
                    g.drawRect(px, py, CELL_SIZE, CELL_SIZE); // Narysowanie szarych konturów (ramki) wokół bieżącej komórki

                    Entity entity = board.getEntityAt(x, y); // Pobranie jednostki logicznej znajdującej się na tych współrzędnych planszy
                    if (entity != null) { // Jeśli na danym polu stoi jakikolwiek obiekt
                        drawEntity(g, entity, px, py); // Wywołanie metody rysującej grafikę przypisaną do tego obiektu
                    }
                }
            }
        }

        private void drawEntity(Graphics g, Entity entity, int px, int py) { // Prywatna metoda pomocnicza dopasowująca i rysująca teksturę jednostki
            Image imgToDraw = null; // Inicjalizacja zmiennej przechowującej obraz do narysowania wartością null

            if (entity instanceof Farmer) { // Jeśli obiekt na polu jest instancją klasy Farmer
                imgToDraw = farmerImg; // Przypisanie obrazka rolnika do narysowania
            } else if (entity instanceof Fox) { // Jeśli obiekt na polu jest instancją klasy Fox
                imgToDraw = foxImg; // Przypisanie obrazka lisa do narysowania
            } else if (entity instanceof Chicken) { // Jeśli obiekt na polu jest instancją klasy Chicken
                imgToDraw = chickenImg; // Przypisanie obrazka kurczaka do narysowania
            } else if (entity instanceof Beetle) { // Jeśli obiekt na polu jest instancją klasy Beetle
                imgToDraw = beetleImg; // Przypisanie obrazka żuka do narysowania
            } else if (entity instanceof Potato) { // Jeśli obiekt na polu jest instancją klasy Potato (ziemniak)
                Potato p = (Potato) entity; // Rzutowanie ogólnego obiektu entity na konkretny typ Potato
                if (p.getMass() >= 5.0) { // Sprawdzenie, czy masa ziemniaka klasyfikuje go jako w pełni dojrzały plon
                    imgToDraw = potatoBigImg; // Wybór obrazka reprezentującego dużego ziemniaka
                } else { // W przeciwnym razie (gdy ziemniak wciąż rośnie)
                    imgToDraw = potatoImg; // Wybór obrazka reprezentującego małego ziemniaka
                }
            }

            if (imgToDraw != null) { // Sprawdzenie, czy pomyślnie dopasowano jakikolwiek obrazek dla znalezionej jednostki
                g.drawImage(imgToDraw, px, py, CELL_SIZE, CELL_SIZE, null); // Narysowanie wybranej tekstury w określonym miejscu siatki z przeskalowaniem do rozmiaru komórki
            }
        }
    }
}