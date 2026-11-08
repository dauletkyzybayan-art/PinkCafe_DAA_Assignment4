
package pinkcafe;

public class TwoRowsEditDistance {

    private long operations = 0;

    // State: previous[j] and current[j] store distances for two DP rows.
    // Transition: choose the minimum of insert, delete, and replace.
    // Base case: an empty string needs the length of the other string.
    public int distance(String a, String b) {
        operations = 0;

        // Keep the shorter string in b to save memory.
        if (b.length() > a.length()) {
            String temp = a;
            a = b;
            b = temp;
        }

        int n = a.length();
        int m = b.length();

        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            previous[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            current[0] = i;

            for (int j = 1; j <= m; j++) {
                operations++;

                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    current[j] = previous[j - 1];
                } else {
                    current[j] = 1 + Math.min(
                            previous[j - 1],
                            Math.min(previous[j], current[j - 1])
                    );
                }
            }

            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[m];
    }

    public long getOperations() {
        return operations;
    }
}
