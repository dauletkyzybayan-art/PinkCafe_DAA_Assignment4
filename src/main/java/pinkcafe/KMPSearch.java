
package pinkcafe;

import java.util.ArrayList;
import java.util.List;

public class KMPSearch {

    private long comparisons = 0;

    // Build the longest proper prefix which is also a suffix.
    public int[] lps(String pattern) {
        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {
            comparisons++;

            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else if (length != 0) {
                length = lps[length - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }

        return lps;
    }

    public List<Integer> search(String text, String pattern) {
        comparisons = 0;

        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be empty");
        }

        List<Integer> positions = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return positions;
        }

        int[] prefix = lps(pattern);
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            comparisons++;

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    positions.add(i - j);
                    j = prefix[j - 1];
                }
            } else if (j != 0) {
                j = prefix[j - 1];
            } else {
                i++;
            }
        }

        return positions;
    }

    public long getComparisons() {
        return comparisons;
    }
}
