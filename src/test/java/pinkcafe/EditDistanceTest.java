
package pinkcafe;

import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditDistanceTest {

    RecursiveEditDistance recursive = new RecursiveEditDistance();
    TableEditDistance table = new TableEditDistance();
    TwoRowsEditDistance twoRows = new TwoRowsEditDistance();

    private void check(String a, String b, int expected) {
        assertEquals(expected, recursive.distance(a, b));
        assertEquals(expected, table.distance(a, b));
        assertEquals(expected, twoRows.distance(a, b));
    }

    @Test
    void referenceTests() {
        check("kitten", "sitting", 3);
        check("flaw", "lawn", 2);
        check("intention", "execution", 5);
        check("", "abc", 3);
        check("abc", "abc", 0);
        check("", "", 0);
    }

    @Test
    void randomTests() {
        Random random = new Random(42);

        for (int t = 0; t < 100; t++) {
            String a = randomString(random);
            String b = randomString(random);

            int expected = table.distance(a, b);

            assertEquals(expected, recursive.distance(a, b));
            assertEquals(expected, twoRows.distance(a, b));
        }
    }

    private String randomString(Random random) {
        int length = random.nextInt(5);
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < length; i++) {
            text.append((char) ('a' + random.nextInt(4)));
        }

        return text.toString();
    }
}
