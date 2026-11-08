
package pinkcafe;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class ExperimentRunner {

    private static final int RUNS = 5;

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Path.of("results"));

        try (
                PrintWriter results = new PrintWriter(
                        Files.newBufferedWriter(Path.of("results/results.csv")));
                PrintWriter summary = new PrintWriter(
                        Files.newBufferedWriter(Path.of("results/summary.csv")))
        ) {
            results.println("algorithm,input,n,run,time_ms,counter");
            summary.println("algorithm,input,n,median_time_ms,counter");

            System.out.println("PINK CAFE - ALGORITHM LAB");
            System.out.println("Running experiments...");

            runEditDistance(results, summary);
            runStringSearch(results, summary);
        }

        System.out.println("Experiments completed!");
        System.out.println("Results saved in results folder.");
    }

    private static void runEditDistance(
            PrintWriter results, PrintWriter summary) {

        String[] algorithms = {"Recursive", "Table", "TwoRows"};

        for (String name : algorithms) {
            int[] sizes = name.equals("Recursive")
                    ? new int[]{2, 4, 6, 8}
                    : new int[]{200, 400, 800, 1600};

            for (int n : sizes) {
                for (String input : new String[]{"similar", "different"}) {

                    String a = "a".repeat(n);
                    String b = input.equals("similar")
                            ? "a".repeat(n) : "b".repeat(n);

                    double[] times = new double[RUNS];
                    long counter = 0;

                    RecursiveEditDistance recursive =
                            new RecursiveEditDistance();
                    TableEditDistance table =
                            new TableEditDistance();
                    TwoRowsEditDistance twoRows =
                            new TwoRowsEditDistance();

                    for (int run = 0; run < RUNS; run++) {
                        long start = System.nanoTime();

                        switch (name) {
                            case "Recursive" -> recursive.distance(a, b);
                            case "Table" -> table.distance(a, b);
                            case "TwoRows" -> twoRows.distance(a, b);
                        }

                        times[run] = (System.nanoTime() - start) / 1_000_000.0;

                        counter = switch (name) {
                            case "Recursive" -> recursive.getCalls();
                            case "Table" -> table.getOperations();
                            default -> twoRows.getOperations();
                        };

                        save(results, name, input, n, run + 1,
                                times[run], counter);
                    }

                    saveMedian(summary, name, input, n, times, counter);
                }
            }
        }
    }

    private static void runStringSearch(
            PrintWriter results, PrintWriter summary) {

        int[] sizes = {10000, 40000, 160000, 640000};

        for (String name : new String[]{"Naive", "KMP"}) {
            for (int n : sizes) {
                for (String input : new String[]{"random", "repeated"}) {

                    String text = input.equals("random")
                            ? randomText(n)
                            : "a".repeat(n);

                    String pattern = input.equals("random")
                            ? "abcdabcd"
                            : "aaaaaaaaaaaaaaab";

                    double[] times = new double[RUNS];
                    long counter = 0;

                    NaiveSearch naive = new NaiveSearch();
                    KMPSearch kmp = new KMPSearch();

                    for (int run = 0; run < RUNS; run++) {
                        long start = System.nanoTime();

                        if (name.equals("Naive")) {
                            naive.search(text, pattern);
                            counter = naive.getComparisons();
                        } else {
                            kmp.search(text, pattern);
                            counter = kmp.getComparisons();
                        }

                        times[run] = (System.nanoTime() - start) / 1_000_000.0;

                        save(results, name, input, n, run + 1,
                                times[run], counter);
                    }

                    saveMedian(summary, name, input, n, times, counter);
                }
            }
        }
    }

    private static String randomText(int n) {
        Random random = new Random(42 + n);
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < n; i++) {
            text.append((char) ('a' + random.nextInt(4)));
        }

        return text.toString();
    }

    private static void save(
            PrintWriter out, String algorithm, String input,
            int n, int run, double time, long counter) {

        out.printf(Locale.US, "%s,%s,%d,%d,%.6f,%d%n",
                algorithm, input, n, run, time, counter);
    }

    private static void saveMedian(
            PrintWriter out, String algorithm, String input,
            int n, double[] times, long counter) {

        Arrays.sort(times);

        out.printf(Locale.US, "%s,%s,%d,%.6f,%d%n",
                algorithm, input, n, times[RUNS / 2], counter);
    }
}
