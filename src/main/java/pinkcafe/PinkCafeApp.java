
package pinkcafe;

import java.util.List;
import java.util.Scanner;

public class PinkCafeApp {

    private static final String[] drinks = {
            "Strawberry Matcha",
            "Vanilla Latte",
            "Pink Lemonade",
            "Caramel Macchiato",
            "Rose Milk Tea",
            "Berry Cheesecake"
    };

    private static final String[] descriptions = {
            "Matcha with strawberry puree and milk",
            "Coffee with vanilla syrup and milk",
            "Pink lemonade with fresh strawberry",
            "Coffee with caramel and creamy milk",
            "Rose tea with milk and vanilla",
            "Cheesecake with strawberry and raspberry"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=================================");
            System.out.println("        PINK CAFE  ♡");
            System.out.println("   Sweet Drinks, Smart Search");
            System.out.println("=================================");
            System.out.println("1. Explore Our Menu");
            System.out.println("2. Find My Drink");
            System.out.println("3. Ingredient Detective");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showMenu();
                case "2" -> findDrink(scanner);
                case "3" -> searchIngredient(scanner);
                case "4" -> {
                    System.out.println("Thank you for visiting Pink Cafe!");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n--- OUR SWEET MENU ---");

        for (int i = 0; i < drinks.length; i++) {
            System.out.println((i + 1) + ". " + drinks[i]);
            System.out.println("   " + descriptions[i]);
        }
    }

    private static void findDrink(Scanner scanner) {
        System.out.print("\nEnter your drink: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("Please enter a drink name.");
            return;
        }

        TwoRowsEditDistance algorithm = new TwoRowsEditDistance();

        String bestMatch = "";
        int minDistance = Integer.MAX_VALUE;

        for (String drink : drinks) {
            int distance = algorithm.distance(
                    input.toLowerCase(),
                    drink.toLowerCase()
            );

            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = drink;
            }
        }

        System.out.println("\nDid you mean: " + bestMatch + "?");
        System.out.println("Edit Distance: " + minDistance);
    }

    private static void searchIngredient(Scanner scanner) {
        System.out.print("\nEnter an ingredient: ");
        String pattern = scanner.nextLine().trim().toLowerCase();

        if (pattern.isEmpty()) {
            System.out.println("Please enter an ingredient.");
            return;
        }

        NaiveSearch naive = new NaiveSearch();
        KMPSearch kmp = new KMPSearch();

        long naiveTotal = 0;
        long kmpTotal = 0;
        boolean found = false;

        for (int i = 0; i < drinks.length; i++) {
            String text = descriptions[i].toLowerCase();

            List<Integer> naiveResult = naive.search(text, pattern);
            List<Integer> kmpResult = kmp.search(text, pattern);

            naiveTotal += naive.getComparisons();
            kmpTotal += kmp.getComparisons();

            if (!naiveResult.isEmpty()) {
                found = true;
                System.out.println("\n" + drinks[i]);
                System.out.println("Found at: " + naiveResult);
            }

            if (!naiveResult.equals(kmpResult)) {
                throw new IllegalStateException("Search results differ!");
            }
        }

        if (!found) {
            System.out.println("No matching ingredients found.");
        }

        System.out.println("\n--- ALGORITHM BATTLE ---");
        System.out.println("Naive comparisons: " + naiveTotal);
        System.out.println("KMP comparisons: " + kmpTotal);
    }
}
