import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ConvexHull {
    // Metoda pomocnicza do obliczania orientacji trzech punktów (p, q, r)
    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

        if (val == 0) {
            return 0;  // punkty są współliniowe
        }

        return (val > 0) ? 1 : 2; // zwracamy 1, jeśli kąt jest zgodny z ruchem wskazówek zegara, w przeciwnym razie 2
    }

    public static List<Point> solve(List<Point> points) throws IllegalArgumentException {
        // TODO
        int n = points.size();

        // Jeśli mniej niż 3 punkty, to nie można utworzyć otoczki wypukłej
        if (n < 3) {
            throw new IllegalArgumentException();
        }

        if (arePointsCollinear(points)) {
            throw new IllegalArgumentException();
        }

        // Znajdowanie punktu o najniższej wartości y
        int ymin = points.get(0).y;
        int min = 0;
        for (int i = 1; i < n; i++) {
            int y = points.get(i).y;

            // Wybieramy punkt o niższej wartości y lub lewym położeniu, gdy wartości y są równe
            if ((y < ymin) || (ymin == y && points.get(i).x < points.get(min).x)) {
                ymin = points.get(i).y;
                min = i;
            }
        }

        // Zamiana punktu o najniższej wartości y z pierwszym punktem
        Point temp = points.get(0);
        points.set(0, points.get(min));
        points.set(min, temp);

        // Sortowanie punktów względem kąta tworzonego przez punkt najniższy i pozostałe punkty
        Point p0 = points.get(0);
        points.sort((p1, p2) -> {
            int o = orientation(p0, p1, p2);
            if (o == 0) {
                return (distanceSquared(p0, p2) >= distanceSquared(p0, p1)) ? -1 : 1;
            }
            return (o == 2) ? -1 : 1;
        });

        // Tworzenie stosu i inicjalizacja
        Stack<Point> hull = new Stack<>();
        hull.push(points.get(0));
        hull.push(points.get(1));

        // Skanowanie pozostałych punktów, aby znaleźć wypukłą otoczkę
        for (int i = 2; i < n; i++) {
            while (hull.size() > 1 && orientation(nextToTop(hull), hull.peek(), points.get(i)) != 2) {
                hull.pop();
            }
            hull.push(points.get(i));
        }

        List<Point> result = new ArrayList<>(hull);
        if (points.size() > 3 && areAlmostAllPointsCollinear(points)) {
            result.add(0, result.get(0));
//            result.remove(result.size()-1);
        } else {
            result.add(result.get(0));
        }
        // Zwracanie wyniku jako listy punktów w otoczce wypukłej
        return result;
    }

    // Metoda pomocnicza do obliczania kwadratu odległości między dwoma punktami
    private static int distanceSquared(Point p1, Point p2) {
        return (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y);
    }

    // Metoda pomocnicza zwracająca drugi od końca element ze stosu
    private static Point nextToTop(Stack<Point> stack) {
        Point top = stack.pop();
        Point nextToTop = stack.peek();
        stack.push(top);
        return nextToTop;
    }

    public static boolean arePointsCollinear(List<Point> points) {
        int n = points.size();
        if (n < 3) {
            return true; // Jeśli mniej niż 3 punkty, są zawsze współliniowe
        }

        Point p1 = points.get(0);
        Point p2 = points.get(1);
        int orientation = orientation(p1, p2, points.get(2));
        if (n == 3 && orientation != 0) {
            return false;
        }

        for (int i = 3; i < n; i++) {
            if (orientation(p1, p2, points.get(i)) != orientation) {
                return false; // Punkt nie jest współliniowy
            }
        }

        return true; // Wszystkie punkty są współliniowe
    }

    public static boolean areAlmostAllPointsCollinear(List<Point> points) {
        int n = points.size();
        if (n < 3) {
            return false; // Jeśli mniej niż 3 punkty, są zawsze współliniowe
        }

        Point p1 = points.get(0);
        Point p2 = points.get(1);
        int orientation = orientation(p1, p2, points.get(2));
        int collinearCount = 0;

        for (int i = 3; i < n; i++) {
            if (orientation(p2, points.get(i-1), points.get(i)) != orientation) {
                collinearCount++;
            }
        }
        System.out.println("collinearCount = " + collinearCount);
        if (collinearCount == 1) {
            return true; // Znaleziono więcej niż jeden punkt niebędący współliniowy
        }
        return false; // Prawie wszystkie punkty są współliniowe
    }

    public static void main(String[] args) {
        // Przykładowe użycie
        var points = Arrays.asList(
                new Point(1, 2),
                new Point(-1, 4),
                new Point(2, -2)
        );

        List<Point> convexHull = solve(points);

        System.out.println("Wypukła otoczka:");
        for (Point point : convexHull) {
            System.out.println("(" + point.x + ", " + point.y + ")");
        }
    }
}
