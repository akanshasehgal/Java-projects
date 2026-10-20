package com.shopsphere.rmi.client;

import java.rmi.Naming;
import java.util.Scanner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.shopsphere.model.Product;
import com.shopsphere.rmi.remote.InventoryService;

/**
 * Console client for the remote inventory service.
 * Finds the service in two ways:
 *   1. java.rmi.Naming.lookup()          (RMI naming)
 *   2. javax.naming.InitialContext       (JNDI with the rmi: URL scheme)
 * and then calls remote methods as if they were local.
 */
public class RmiClient {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        String url = "rmi://" + host + ":1099/" + InventoryService.NAME;

        try {
            InventoryService service = (InventoryService) Naming.lookup(url);
            System.out.println("Found " + url + " with Naming.lookup()");
            jndiLookup(url);

            try (Scanner sc = new Scanner(System.in)) {
                while (true) {
                    System.out.println();
                    System.out.println("1. getProduct(id)");
                    System.out.println("2. getStock(id)");
                    System.out.println("3. updateStock(id, quantity)");
                    System.out.println("4. getLowStockProducts(threshold)");
                    System.out.println("5. Quit");
                    System.out.print("Choose: ");
                    String choice = sc.nextLine().trim();
                    try {
                        switch (choice) {
                            case "1": {
                                Product p = service.getProduct(readInt(sc, "Product id: "));
                                System.out.println(p == null ? "Not found." : p.toString());
                                break;
                            }
                            case "2": {
                                int stock = service.getStock(readInt(sc, "Product id: "));
                                System.out.println(stock < 0 ? "Not found." : "Stock = " + stock);
                                break;
                            }
                            case "3": {
                                int id = readInt(sc, "Product id: ");
                                int qty = readInt(sc, "New stock: ");
                                System.out.println(service.updateStock(id, qty) ? "Stock updated." : "Update failed.");
                                break;
                            }
                            case "4":
                                for (Product p : service.getLowStockProducts(readInt(sc, "Threshold: "))) {
                                    System.out.println("  " + p);
                                }
                                break;
                            case "5":
                                return;
                            default:
                                System.out.println("Please enter 1-5.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a whole number.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not reach the RMI server: " + e.getMessage());
            System.out.println("Start it first with: mvn -pl rmi exec:java");
        }
    }

    /** The same remote object located through JNDI. */
    private static void jndiLookup(String url) {
        try {
            Context ctx = new InitialContext();
            InventoryService viaJndi = (InventoryService) ctx.lookup(url);
            System.out.println("Found the same service through JNDI InitialContext: " + viaJndi.getClass().getSimpleName());
        } catch (NamingException e) {
            System.out.println("(JNDI lookup not available: " + e.getMessage() + ")");
        }
    }

    private static int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(sc.nextLine().trim());
    }
}
