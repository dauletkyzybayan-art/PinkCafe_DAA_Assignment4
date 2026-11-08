
package pinkcafe;

public class RecursiveEditDistance {

    private long calls = 0;

    // State: i and j are the lengths of the remaining prefixes.
    // Transition: match characters or try insert, delete, replace.
    // Base case: if one prefix is empty, return the other length.
    public int distance(String a, String b) {
        calls = 0;
        return solve(a, b, a.length(), b.length());
    }

    private int solve(String a, String b, int i, int j) {
        calls++;

        if (i == 0) return j;
        if (j == 0) return i;

        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return solve(a, b, i - 1, j - 1);
        }

        int insert = solve(a, b, i, j - 1);
        int delete = solve(a, b, i - 1, j);
        int replace = solve(a, b, i - 1, j - 1);

        return 1 + Math.min(insert, Math.min(delete, replace));
    }

    public long getCalls() {
        return calls;
    }
}
