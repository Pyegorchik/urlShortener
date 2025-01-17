import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static LinkService linkService;
    private static final UserService userService = new UserService();
    private static UUID currentUserUuid;

    public static void main(String[] args) {
        ConfigLoader configLoader;
        try {
            configLoader = new ConfigLoader("config");
            linkService = new LinkService(configLoader);
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> createLink(scanner);
                case 2 -> followLink(scanner);
                case 3 -> validateAndSetUserUuid(scanner);
                case 4 -> listUserLinks();
                case 5 -> editLink(scanner);
                case 6 -> deleteLink(scanner);
                case 7 -> signOut(scanner);
                case 8 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nCurrent User UUID: " + (currentUserUuid != null ? currentUserUuid : "None"));
        System.out.println("\n1. Create short link\n2. Follow short link\n3. Set current UUID\n4. List all links\n5. Edit a link\n6. Delete a link\n7. Sign Out\n8. Exit");
    }

    private static void createLink(Scanner scanner) {
        System.out.println("Enter the original URL:");
        String originalUrl = scanner.next();

        System.out.println("Enter the max number of allowed clicks:");
        int maxClicks = scanner.nextInt();

        System.out.println("Enter how long the link should be available (in seconds):");
        long expirationTime = scanner.nextLong();

        if (currentUserUuid == null) {
            currentUserUuid = userService.getOrCreateUserUuid();
        }

        String shortLink = linkService.createShortLink(originalUrl, currentUserUuid, maxClicks, expirationTime);
        System.out.println("Short link created: " + shortLink);
    }

    private static void followLink(Scanner scanner) {
        System.out.println("Enter the short link:");
        String shortLink = scanner.next();


        String originalUrl;
        try {
            originalUrl = linkService.getOriginalUrl(shortLink);
            if (originalUrl == null) {
                System.out.println("The link is invalid or expired. Please try another.");
                return;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("An error occurred: " + e.getMessage());
            return;
        }
        
        System.out.println("Redirecting to: " + originalUrl);
        try {
            Desktop.getDesktop().browse(new URI(originalUrl));
        } catch (Exception e) {
            System.err.println("Failed to open the URL: " + e.getMessage());
        }
    }

    private static void validateAndSetUserUuid(Scanner scanner) {
        System.out.println("Enter a UUID to set as the current user:");
        String inputUuid = scanner.next();

        try {
            UUID uuid = UUID.fromString(inputUuid);
            if (userService.isUserUuidValid(uuid)) {
                currentUserUuid = uuid;
                System.out.println("UUID set successfully.");
            } else {
                System.out.println("Invalid or non-existent UUID.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        }
    }

    private static void listUserLinks() {
        List<Link> links = linkService.getLinksByUser(currentUserUuid);
        if (links.isEmpty()) {
            System.out.println("No links found for this user.");
        } else {
            System.out.println("Links associated with this user:");
            for (Link link : links) {
                System.out.println("- " + link.getShortUrl() + " -> " + link.getOriginalUrl());
            }
        }
    }

    private static void editLink(Scanner scanner) {
        System.out.println("Enter the short link to edit:");
        String shortLink = scanner.next();

        Link link = linkService.getLinkByShortUrl(shortLink);
        if (link != null && link.getUserUuid().equals(currentUserUuid)) {
            System.out.println("Enter new max clicks (current: " + link.getMaxClicks() + "):");
            int maxClicks = scanner.nextInt();

            System.out.println("Enter new expiration time in seconds (current: " + link.getExpiryTime().getSecond() + "):");
            long expirationTime = scanner.nextLong();

            linkService.updateLink(link, maxClicks, expirationTime);
            System.out.println("Link updated successfully.");
        } else {
            System.out.println("Link not found or does not belong to the current user.");
        }
    }

    private static void deleteLink(Scanner scanner) {
        System.out.println("Enter the short link to delete:");
        String shortLink = scanner.next();

        Link link = linkService.getLinkByShortUrl(shortLink);
        if (link != null && link.getUserUuid().equals(currentUserUuid)) {
            linkService.deleteLink(shortLink);
            System.out.println("Link deleted successfully.");
        } else {
            System.out.println("Link not found or does not belong to the current user.");
        }
    }

    private static void signOut(Scanner scanner) {
        currentUserUuid = null;
        System.out.println("You have sign out.");
    }
}
