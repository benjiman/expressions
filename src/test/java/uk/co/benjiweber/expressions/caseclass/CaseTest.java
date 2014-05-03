package uk.co.benjiweber.expressions.caseclass;

import org.junit.Test;
import uk.co.benjiweber.expressions.caseclass.Case3;

import static org.junit.Assert.assertEquals;

public class CaseTest {

    @Test
    public void case_example() {
        Shape cube = Cube.create(4f);
        Shape circle = Circle.create(6f);
        Shape rectangle = Rectangle.create(1f, 2f);

        assertEquals("Cube with size 4.0", description(cube));
        assertEquals("Circle with radius 6.0", description(circle));
        assertEquals("Rectangle 1.0x2.0", description(rectangle));
    }

    public String description(Shape shape) {
        return shape.match()
            .when(Rectangle.class, rect -> "Rectangle " + rect.width() + "x" + rect.length())
            .when(Circle.class, circle -> "Circle with radius " + circle.radius())
            .when(Cube.class, cube -> "Cube with size " + cube.size());
    }

    interface Shape extends Case3<Rectangle, Circle, Cube> { }

    interface Rectangle extends Shape {
        float length();
        float width();
        static Rectangle create(float width, float length) {
            return new Rectangle() {
                public float length() {
                    return length;
                }
                public float width() {
                    return width;
                }
            };
        }
    }

    interface Circle extends Shape {
        float radius();
        static Circle create(float radius) {
            return () -> radius;
        }
    }

    interface Cube extends Shape {
        float size();
        static Cube create(float size) {
            return () -> size;
        }
    }


}
