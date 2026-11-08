package pinkcafe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Locale;

public class PinkCafeGUI {
    private static final Color BACKGROUND = new Color(255, 248, 251);
    private static final Color PINK = new Color(213, 90, 139);
    private static final Color DARK = new Color(88, 42, 66);
    private static final Color MUTED = new Color(145, 105, 125);
    private static final Color LIGHT_PINK = new Color(255, 234, 243);

    private static final String[] NAMES = {
            "Strawberry Matcha", "Vanilla Latte", "Pink Lemonade",
            "Caramel Macchiato", "Rose Milk Tea", "Berry Cheesecake"
    };
    private static final String[] DESCRIPTIONS = {
            "Matcha with strawberry puree and milk",
            "Coffee with vanilla syrup and milk",
            "Pink lemonade with fresh strawberry",
            "Coffee with caramel and creamy milk",
            "Rose tea with milk and vanilla",
            "Cheesecake with strawberry and raspberry"
    };
    private static final String[] CATEGORIES = {
            "MATCHA", "COFFEE", "FRESH", "COFFEE", "TEA", "DESSERT"
    };

    private final JTextField drinkField = new JTextField("straberry matcha");
    private final JTextField ingredientField = new JTextField("milk");
    private final JLabel drinkResult = new JLabel("Your perfect match will appear here ♡");
    private final JTextArea ingredientResult = new JTextArea("Search an ingredient to discover matching treats.");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PinkCafeGUI().showWindow());
    }

    private void showWindow() {
        JFrame frame = new JFrame("Pink Café | Smart Search Lab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1070, 735);
        frame.setMinimumSize(new Dimension(930, 650));
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(18, 20, 18, 20));
        frame.setContentPane(root);

        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(208, 77, 130),
                        getWidth(), getHeight(), new Color(247, 176, 200)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillOval(getWidth() - 180, -90, 250, 250);
                g2.fillOval(getWidth() - 295, 55, 130, 130);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 130));
        header.setBorder(new EmptyBorder(23, 30, 23, 30));
        JPanel heading = new JPanel(new GridLayout(2, 1, 0, 4));
        heading.setOpaque(false);
        JLabel title = label("PINK CAFÉ  ♡", 33, Color.WHITE, Font.BOLD);
        JLabel subtitle = label("A little sweetness. A little intelligence.  |  Smart Search Lab", 14,
                Color.WHITE, Font.PLAIN);
        heading.add(title);
        heading.add(subtitle);
        header.add(heading, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(16, 0));
        content.setOpaque(false);
        root.add(content, BorderLayout.CENTER);

        RoundedPanel menuPanel = new RoundedPanel(Color.WHITE);
        menuPanel.setLayout(new BorderLayout(0, 15));
        JLabel menuTitle = label("Our sweet menu", 23, DARK, Font.BOLD);
        menuPanel.add(menuTitle, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(3, 2, 12, 12));
        grid.setOpaque(false);
        for (int i = 0; i < NAMES.length; i++) {
            RoundedPanel card = new RoundedPanel(LIGHT_PINK);
            card.setLayout(new BorderLayout(0, 8));
            card.setBorder(new EmptyBorder(14, 15, 14, 15));
            JLabel category = label("✦  " + CATEGORIES[i], 11, PINK, Font.BOLD);
            JLabel name = label(NAMES[i], 16, DARK, Font.BOLD);
            JTextArea description = new JTextArea(DESCRIPTIONS[i]);
            description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            description.setForeground(MUTED);
            description.setOpaque(false);
            description.setEditable(false);
            description.setFocusable(false);
            description.setLineWrap(true);
            description.setWrapStyleWord(true);
            JPanel top = new JPanel(new GridLayout(2, 1, 0, 3));
            top.setOpaque(false);
            top.add(category);
            top.add(name);
            card.add(top, BorderLayout.NORTH);
            card.add(description, BorderLayout.CENTER);
            grid.add(card);
        }
        menuPanel.add(grid, BorderLayout.CENTER);
        content.add(menuPanel, BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(2, 1, 0, 14));
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(342, 0));
        content.add(right, BorderLayout.EAST);

        RoundedPanel drinkBox = new RoundedPanel(Color.WHITE);
        drinkBox.setLayout(new BoxLayout(drinkBox, BoxLayout.Y_AXIS));
        drinkBox.add(label("Find my drink  ♡", 20, DARK, Font.BOLD));
        drinkBox.add(Box.createVerticalStrut(10));
        drinkBox.add(label("Typos are welcome here!", 13, MUTED, Font.PLAIN));
        drinkBox.add(Box.createVerticalStrut(12));
        styleField(drinkField);
        drinkBox.add(drinkField);
        drinkBox.add(Box.createVerticalStrut(10));
        JButton findButton = pinkButton("FIND MY MATCH");
        findButton.addActionListener(e -> findDrink());
        drinkBox.add(findButton);
        drinkBox.add(Box.createVerticalStrut(16));
        drinkResult.setFont(new Font("Segoe UI", Font.BOLD, 14));
        drinkResult.setForeground(DARK);
        drinkBox.add(drinkResult);
        right.add(drinkBox);

        RoundedPanel ingredientBox = new RoundedPanel(Color.WHITE);
        ingredientBox.setLayout(new BoxLayout(ingredientBox, BoxLayout.Y_AXIS));
        ingredientBox.add(label("Ingredient detective", 20, DARK, Font.BOLD));
        ingredientBox.add(Box.createVerticalStrut(10));
        ingredientBox.add(label("Powered by Naive + KMP", 13, MUTED, Font.PLAIN));
        ingredientBox.add(Box.createVerticalStrut(12));
        styleField(ingredientField);
        ingredientBox.add(ingredientField);
        ingredientBox.add(Box.createVerticalStrut(10));
        JButton searchButton = pinkButton("SEARCH INGREDIENT");
        searchButton.addActionListener(e -> searchIngredient());
        ingredientBox.add(searchButton);
        ingredientBox.add(Box.createVerticalStrut(12));
        ingredientResult.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ingredientResult.setForeground(DARK);
        ingredientResult.setEditable(false);
        ingredientResult.setLineWrap(true);
        ingredientResult.setWrapStyleWord(true);
        ingredientResult.setBackground(new Color(255, 247, 250));
        ingredientResult.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(ingredientResult);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        ingredientBox.add(scroll);
        right.add(ingredientBox);

        JLabel footer = label("PINK CAFÉ  •  DAA Assignment 4  •  Edit Distance  /  Naive  /  KMP", 12,
                MUTED, Font.PLAIN);
        root.add(footer, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void findDrink() {
        String input = drinkField.getText().trim().toLowerCase(Locale.ROOT);
        if (input.isEmpty()) {
            drinkResult.setText("Please type a drink name.");
            return;
        }
        TwoRowsEditDistance algorithm = new TwoRowsEditDistance();
        int bestDistance = Integer.MAX_VALUE;
        String bestName = "";
        for (String name : NAMES) {
            int distance = algorithm.distance(input, name.toLowerCase(Locale.ROOT));
            if (distance < bestDistance) {
                bestDistance = distance;
                bestName = name;
            }
        }
        drinkResult.setText("<html>♡ " + bestName + "<br>" +
                "<font color='#A16A85'>Edit distance: " + bestDistance + "</font></html>");
    }

    private void searchIngredient() {
        String pattern = ingredientField.getText().trim().toLowerCase(Locale.ROOT);
        if (pattern.isEmpty()) {
            ingredientResult.setText("Please type an ingredient.");
            return;
        }
        NaiveSearch naive = new NaiveSearch();
        KMPSearch kmp = new KMPSearch();
        long naiveCount = 0;
        long kmpCount = 0;
        StringBuilder found = new StringBuilder();
        for (int i = 0; i < NAMES.length; i++) {
            String description = DESCRIPTIONS[i].toLowerCase(Locale.ROOT);
            List<Integer> a = naive.search(description, pattern);
            List<Integer> b = kmp.search(description, pattern);
            if (!a.equals(b)) throw new IllegalStateException("Search results differ!");
            naiveCount += naive.getComparisons();
            kmpCount += kmp.getComparisons();
            if (!a.isEmpty()) found.append("♡ ").append(NAMES[i]).append("  ").append(a).append("\n");
        }
        if (found.length() == 0) found.append("No matching items found.\n");
        found.append("\nALGORITHM BATTLE\n")
                .append("Naive comparisons: ").append(naiveCount).append("\n")
                .append("KMP comparisons: ").append(kmpCount);
        ingredientResult.setText(found.toString());
        ingredientResult.setCaretPosition(0);
    }

    private static JLabel label(String text, int size, Color color, int weight) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", weight, size));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setForeground(DARK);
        field.setBackground(BACKGROUND);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(242, 202, 222)),
                new EmptyBorder(9, 12, 9, 12)));
    }

    private static JButton pinkButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(PINK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    private static class RoundedPanel extends JPanel {
        private final Color fill;
        RoundedPanel(Color fill) {
            this.fill = fill;
            setOpaque(false);
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
