
package pinkcafe;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class StringSearchTest {

    NaiveSearch naive = new NaiveSearch();
    KMPSearch kmp = new KMPSearch();

    @Test
    void lpsTests() {
        assertArrayEquals(
                new int[]{0, 1, 2, 0, 1, 2, 3, 3},
                kmp.lps("AAACAAAA")
        );

        assertArrayEquals(
                new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4},
                kmp.lps("ABABCABAB")
        );
    }

    @Test
    void referenceTests() {
        check("ABABDABACDABABCABAB",
                "ABABCABAB", List.of(10));

        check("AAAAA", "AA", List.of(0, 1, 2, 3));

        check("ABABABA", "ABA", List.of(0, 2, 4));
    }

    private void check(String text, String pattern,
                       List<Integer> expected) {
        assertEquals(expected, naive.search(text, pattern));
        assertEquals(expected, kmp.search(text, pattern));
    }

    @Test
    void randomTests() {
        Random random = new Random(42);

        for (int t = 0; t < 100; t++) {
            String text = randomString(random, random.nextInt(21));
            String pattern = randomString(random, 1 + random.nextInt(5));

            assertEquals(
                    naive.search(text, pattern),
                    kmp.search(text, pattern)
            );
        }
    }

    private String randomString(Random random, int length) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            result.append((char) ('A' + random.nextInt(3)));
        }

        return result.toString();
    }

    @Test
    void edgeCases() {
        assertThrows(IllegalArgumentException.class,
                () -> naive.search("ABC", ""));

        assertThrows(IllegalArgumentException.class,
                () -> kmp.search("ABC", ""));

        assertTrue(naive.search("AB", "ABCD").isEmpty());
        assertTrue(kmp.search("AB", "ABCD").isEmpty());
    }
}
