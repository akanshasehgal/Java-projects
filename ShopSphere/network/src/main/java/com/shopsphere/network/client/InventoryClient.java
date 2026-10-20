package com.shopsphere.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.shopsphere.model.Product;
import com.shopsphere.network.model.InventoryRequest;
import com.shopsphere.network.model.InventoryResponse;
import com.shopsphere.network.server.InventoryServer;

/**
 * Console client for the Inventory Server.
 *   Socket -> send command -> receive response
 *
 * Usage: java ...InventoryClient [host] [port]
 */
public class InventoryClient {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : InventoryServer.PORT;

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to ShopSphere Inventory Server at " + host + ":" + port);
            while (true) {
                System.out.println();
                System.out.println("1. GET_PRODUCT   (by id)");
                System.out.println("2. GET_STOCK     (by id)");
                System.out.println("3. SEARCH_PRODUCT (by keyword)");
                System.out.println("4. Quit");
                System.out.print("Choose: ");
                String choice = sc.nextLine().trim();

                InventoryRequest request;
                switch (choice) {
                    case "1":
                        System.out.print("Product id: ");
                        request = new InventoryRequest(InventoryRequest.GET_PRODUCT, sc.nextLine());
                        break;
                    case "2":
                        System.out.print("Product id: ");
                        request = new InventoryRequest(InventoryRequest.GET_STOCK, sc.nextLine());
                        break;
                    case "3":
                        System.out.print("Keyword: ");
                        request = new InventoryRequest(InventoryRequest.SEARCH_PRODUCT, sc.nextLine());
                        break;
                    case "4":
                        request = new InventoryRequest(InventoryRequest.QUIT, null);
                        break;
                    default:
                        System.out.println("Please enter 1-4.");
                        continue;
                }

                out.writeObject(request);           // serialize the request
                out.flush();
                InventoryResponse response = (InventoryResponse) in.readObject();   // deserialize the answer
                print(request, response);
                if (InventoryRequest.QUIT.equals(request.getCommand())) break;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not talk to the server: " + e.getMessage());
            System.out.println("Start it first with: mvn -pl network exec:java");
        }
    }

    private static void print(InventoryRequest request, InventoryResponse response) {
        if (!response.isSuccess()) {
            System.out.println("Error: " + response.getMessage());
            return;
        }
        switch (request.getCommand()) {
            case InventoryRequest.GET_PRODUCT:
                Product p = response.getProduct();
                System.out.println("Product  : " + p.getName() + " (" + p.getBrand() + ")");
                System.out.println("Category : " + p.getCategoryName());
                System.out.printf("Price    : Rs.%.2f (MRP Rs.%.2f, %.0f%% off)%n", p.getFinalPrice(), p.getPrice(), p.getDiscount());
                System.out.println("Stock    : " + p.getStock());
                break;
            case InventoryRequest.GET_STOCK:
                System.out.println(response.getMessage() + " -> stock: " + response.getStock());
                break;
            case InventoryRequest.SEARCH_PRODUCT:
                System.out.println(response.getMessage());
                for (Product item : response.getProducts()) System.out.println("  " + item);
                break;
            default:
                System.out.println(response.getMessage());
        }
    }
}
