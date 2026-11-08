package pinkcafe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class PlotGenerator {
    private static final int W = 1120, H = 680;
    private static final int LEFT = 140, RIGHT = 1010, TOP = 155, BOTTOM = 505;
    private static final Color INK = new Color(91, 37, 67);
    private static final Color PINK = new Color(211, 73, 128);
    private static final Color PURPLE = new Color(124, 93, 182);
    private static final Color PALE = new Color(255, 245, 249);
    private static final Color GRID = new Color(237, 219, 227);

    private record Result(String algorithm, String input, int n, double ms, long count) {}

    public static void main(String[] args) throws IOException {
        Path csv = Path.of("results", "summary.csv");
        if (!Files.exists(csv)) {
            System.out.println("Run ExperimentRunner first: results/summary.csv is missing.");
            return;
        }
        List<Result> data = read(csv);
        Path folder = Path.of("results", "plots");
        Files.createDirectories(folder);

        int[] editSizes = {2, 4, 6, 8};
        double[] similar = new double[editSizes.length];
        double[] different = new double[editSizes.length];
        for (int i = 0; i < editSizes.length; i++) {
            similar[i] = find(data, "Recursive", "similar", editSizes[i]).count;
            different[i] = find(data, "Recursive", "different", editSizes[i]).count;
        }
        String note1 = String.format(Locale.US,
                "Different strings: %.0f calls at n=2; %.0f calls at n=8.",
                different[0], different[3]);
        draw(folder.resolve("01_recursive_calls.png"),
                "Recursive Edit Distance: Call Growth",
                "String length n (characters)", "Recursive calls",
                editSizes, new double[][]{similar, different},
                new String[]{"Similar strings", "Different strings"}, note1);

        int[] dpSizes = {200, 400, 800, 1600};
        double[] table = new double[dpSizes.length];
        double[] twoRows = new double[dpSizes.length];
        for (int i = 0; i < dpSizes.length; i++) {
            table[i] = find(data, "Table", "different", dpSizes[i]).ms;
            twoRows[i] = find(data, "TwoRows", "different", dpSizes[i]).ms;
        }
        String note2 = String.format(Locale.US,
                "At n=1600: Table %.3f ms; Two Rows %.3f ms (median of 5).",
                table[3], twoRows[3]);
        draw(folder.resolve("02_dp_median_time.png"),
                "Edit Distance: Table vs Two Rows",
                "String length n (characters)", "Median execution time (ms)",
                dpSizes, new double[][]{table, twoRows},
                new String[]{"Full table", "Two rows"}, note2);

        int[] searchSizes = {10000, 40000, 160000, 640000};
        double[] naive = new double[searchSizes.length];
        double[] kmp = new double[searchSizes.length];
        for (int i = 0; i < searchSizes.length; i++) {
            naive[i] = find(data, "Naive", "repeated", searchSizes[i]).count;
            kmp[i] = find(data, "KMP", "repeated", searchSizes[i]).count;
        }
        double ratio = kmp[3] == 0 ? 0 : naive[3] / kmp[3];
        String note3 = String.format(Locale.US,
                "At n=640,000: Naive made %.2fx as many comparisons as KMP.", ratio);
        // Keep the existing filename so links in REPORT.md still work.
        draw(folder.resolve("03_naive_kmp_ratio.png"),
                "String Search: Naive vs KMP",
                "Text length n (characters)", "Character comparisons",
                searchSizes, new double[][]{naive, kmp},
                new String[]{"Naive", "KMP"}, note3);

        Files.writeString(Path.of("results", "plot_notes.txt"),
                "Chart 1: " + note1 + System.lineSeparator()
                        + "Chart 2: " + note2 + System.lineSeparator()
                        + "Chart 3: " + note3 + System.lineSeparator());
        System.out.println("PINK CAFE - 3 plots created in results/plots/");
    }

    private static List<Result> read(Path path) throws IOException {
        List<Result> rows = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);
        for (int i = 1; i < lines.size(); i++) {
            String[] c = lines.get(i).trim().split(",");
            if (c.length < 5) continue;
            rows.add(new Result(c[0], c[1], Integer.parseInt(c[2]),
                    Double.parseDouble(c[3]), Long.parseLong(c[4])));
        }
        return rows;
    }

    private static Result find(List<Result> rows, String algorithm, String input, int n) {
        for (Result r : rows) {
            if (r.algorithm.equals(algorithm) && r.input.equals(input) && r.n == n) {
                return r;
            }
        }
        throw new IllegalStateException("Missing row: " + algorithm + " " + input + " " + n);
    }

    private static void draw(Path file, String title, String xLabel, String yLabel,
                             int[] xs, double[][] series, String[] names,
                             String note) throws IOException {
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(PALE);
        g.fillRect(0, 0, W, H);
        g.setColor(Color.WHITE);
        g.fillRoundRect(55, 60, 1010, 565, 30, 30);
        g.setColor(INK);
        g.setFont(new Font("SansSerif", Font.BOLD, 27));
        g.drawString(title, 90, 111);
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.drawString("PINK CAFE / ALGORITHM PERFORMANCE LAB", 715, 45);
        g.setColor(new Color(246, 226, 236));
        g.fillRoundRect(LEFT, TOP, RIGHT - LEFT, BOTTOM - TOP, 15, 15);

        double max = 0;
        for (double[] values : series) {
            for (double v : values) max = Math.max(max, v);
        }
        double yMax = max <= 0 ? 1 : max * 1.12;
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        for (int k = 0; k <= 5; k++) {
            double value = yMax * k / 5.0;
            int y = BOTTOM - (int) Math.round((BOTTOM - TOP) * k / 5.0);
            g.setColor(GRID);
            g.setStroke(new BasicStroke(1));
            g.drawLine(LEFT, y, RIGHT, y);
            g.setColor(INK);
            String label = formatNumber(value);
            g.drawString(label, LEFT - 15 - g.getFontMetrics().stringWidth(label), y + 5);
        }
        g.setColor(INK);
        g.setStroke(new BasicStroke(2));
        g.drawLine(LEFT, TOP, LEFT, BOTTOM);
        g.drawLine(LEFT, BOTTOM, RIGHT, BOTTOM);

        // Even spacing makes all four input sizes readable.
        for (int i = 0; i < xs.length; i++) {
            int x = LEFT + (RIGHT - LEFT) * i / (xs.length - 1);
            String label = String.format(Locale.US, "%,d", xs[i]);
            g.drawString(label, x - g.getFontMetrics().stringWidth(label) / 2, BOTTOM + 28);
        }

        Color[] colors = {PINK, PURPLE};
        for (int j = 0; j < series.length; j++) {
            g.setColor(colors[j % colors.length]);
            g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int lastX = -1, lastY = -1;
            for (int i = 0; i < xs.length; i++) {
                int x = LEFT + (RIGHT - LEFT) * i / (xs.length - 1);
                int y = BOTTOM - (int) Math.round((BOTTOM - TOP) * series[j][i] / yMax);
                if (lastX >= 0) g.draw(new Line2D.Double(lastX, lastY, x, y));
                g.fill(new Ellipse2D.Double(x - 6, y - 6, 12, 12));
                lastX = x;
                lastY = y;
            }
        }

        g.setColor(INK);
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.drawString(yLabel, LEFT, 144);
        g.drawString(xLabel, (LEFT + RIGHT - g.getFontMetrics().stringWidth(xLabel)) / 2, 565);

        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        int legendX = LEFT;
        for (int j = 0; j < names.length; j++) {
            g.setColor(colors[j % colors.length]);
            g.fillRoundRect(legendX, 579, 20, 9, 5, 5);
            g.setColor(INK);
            g.drawString(names[j], legendX + 29, 590);
            legendX += g.getFontMetrics().stringWidth(names[j]) + 68;
        }
        g.setColor(INK);
        g.drawString("Takeaway: " + note, 90, 613);
        g.dispose();
        ImageIO.write(image, "png", file.toFile());
    }

    private static String formatNumber(double v) {
        if (v >= 1000) return String.format(Locale.US, "%,.0f", v);
        if (v >= 10) return String.format(Locale.US, "%.0f", v);
        return String.format(Locale.US, "%.2f", v);
    }
}
