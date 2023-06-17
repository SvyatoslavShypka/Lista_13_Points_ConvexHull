import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class ConvexHullTests {
    @Test
    public void threePoints() {
        var points = Arrays.asList(
                new Point(1, 2),
                new Point(-1, 4),
                new Point(2, -2)
        );

        var expectedPoints = Arrays.asList(
                new Point(2, -2),
                new Point(1, 2),
                new Point(-1, 4),
                new Point(2, -2)
        );

        assertEquals(expectedPoints, ConvexHull.solve(points));
    }

    @Test
    public void twoPoints() {
        var points = Arrays.asList(
                new Point(1, 1)
        );

        assertThrows(IllegalArgumentException.class, () -> ConvexHull.solve(points));
    }

    @Test
    public void collinearPoints() {
        var points = Arrays.asList(
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3),
                new Point(4, 4)
        );

        assertThrows(IllegalArgumentException.class, () -> ConvexHull.solve(points));
    }

    @Test
    public void almostCollinearPoints() {
        var points = Arrays.asList(
                new Point(1, 1),
                new Point(3, 3),
                new Point(0, 5),
                new Point(1, 1)
        );

        assertEquals(points, ConvexHull.solve(points));
    }

    @Test
    public void square() {
        var points = new LinkedList<Point>();

        for (var x = 1; x <= 5; x++) {
            for (var y = 1; y <= 5; y++) {
                points.add(new Point(x, y));
            }
        }

        var expectedPoints = Arrays.asList(
                new Point(1, 1),
                new Point(5, 1),
                new Point(5, 5),
                new Point(1, 5),
                new Point(1, 1)
        );

        assertEquals(expectedPoints, ConvexHull.solve(points));
    }

    @Test
    public void triangle() {
        var points = new LinkedList<Point>();
        points.add(new Point(0, 5));

        var x = 1;
        for (var y = 4; y > 0; y--) {
            points.add(new Point(x, y));
            points.add(new Point(-x, y));
            x++;
        }

        var expectedPoints = Arrays.asList(
                new Point(-4, 1),
                new Point(4, 1),
                new Point(0, 5),
                new Point(-4, 1)
        );

        assertEquals(expectedPoints, ConvexHull.solve(points));
    }

    @Test
    public void pseudoRandomMap() {
        var points = new LinkedList<Point>();
        points.add(new Point(-4, 3));
        points.add(new Point(7, 9));
        points.add(new Point(-4, 0));
        points.add(new Point(4, -10));
        points.add(new Point(-6, -6));
        points.add(new Point(-4, -2));
        points.add(new Point(-7, -6));
        points.add(new Point(-2, 9));
        points.add(new Point(-2, -7));
        points.add(new Point(-4, -2));
        points.add(new Point(-3, -5));
        points.add(new Point(-8, -9));
        points.add(new Point(10, 9));
        points.add(new Point(7, 0));
        points.add(new Point(1, -7));
        points.add(new Point(-10, -10));
        points.add(new Point(-5, 9));
        points.add(new Point(6, -6));
        points.add(new Point(1, 2));
        points.add(new Point(3, 9));

        var expectedPoints = Arrays.asList(
                new Point(-10, -10),
                new Point(4, -10),
                new Point(6, -6),
                new Point(10, 9),
                new Point(-5, 9),
                new Point(-10, -10)
        );

        assertEquals(expectedPoints, ConvexHull.solve(points));
    }
}
