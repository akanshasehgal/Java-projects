package com.shopsphere.network.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import com.shopsphere.network.model.InventoryRequest;
import com.shopsphere.network.model.InventoryResponse;

/**
 * ShopSphere Inventory Server (java.net).
 * Listens on a ServerSocket, accepts clients and answers inventory commands.
 * Requests and responses are Java objects sent with ObjectOutputStream / ObjectInputStream (serialization).
 *
 *   ServerSocket -> accept() -> Socket -> read command -> query MySQL -> send response
 */
public class InventoryServer {

    public static final int PORT = 6000;

    private final ProductDAO productDAO = new ProductDAO();

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;
        new InventoryServer().start(port);
    }

    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("ShopSphere Inventory Server started on port " + port);
            System.out.println("Waiting for clients... (Ctrl+C to stop)");
            while (true) {
                Socket socket = serverSocket.accept();                  // blocks until a client connects
                System.out.println("Client connected: " + socket.getRemoteSocketAddress());
                Thread worker = new Thread(() -> handle(socket));       // one thread per client
                worker.start();
            }
        }
    }

    private void handle(Socket socket) {
        try (Socket s = socket;
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(s.getInputStream())) {
            out.flush();
            while (true) {
                InventoryRequest request = (InventoryRequest) in.readObject();
                System.out.println("[" + s.getRemoteSocketAddress() + "] " + request);
                if (InventoryRequest.QUIT.equals(request.getCommand())) {
                    out.writeObject(new InventoryResponse(true, "Goodbye!"));
                    break;
                }
                out.writeObject(process(request));
                out.reset();                    // always send fresh copies of objects
                out.flush();
            }
        } catch (EOFException e) {
            // client closed the connection
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
        System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
    }

    InventoryResponse process(InventoryRequest request) {
        try {
            switch (request.getCommand()) {
                case InventoryRequest.GET_PRODUCT: {
                    Product p = productDAO.findById(parseId(request.getArgument()));
                    if (p == null) return new InventoryResponse(false, "No product with id " + request.getArgument());
                    InventoryResponse r = new InventoryResponse(true, "Product found");
                    r.setProduct(p);
                    return r;
                }
                case InventoryRequest.GET_STOCK: {
                    Product p = productDAO.findById(parseId(request.getArgument()));
                    if (p == null) return new InventoryResponse(false, "No product with id " + request.getArgument());
                    InventoryResponse r = new InventoryResponse(true, p.getName());
                    r.setStock(p.getStock());
                    return r;
                }
                case InventoryRequest.SEARCH_PRODUCT: {
                    List<Product> list = productDAO.search(request.getArgument(), null, null, null, "name", true);
                    InventoryResponse r = new InventoryResponse(true, list.size() + " product(s) found");
                    r.setProducts(list);
                    return r;
                }
                default:
                    return new InventoryResponse(false, "Unknown command: " + request.getCommand());
            }
        } catch (NumberFormatException e) {
            return new InventoryResponse(false, "Product id must be a number.");
        } catch (RuntimeException e) {
            return new InventoryResponse(false, "Server error: " + e.getMessage());
        }
    }

    private int parseId(String text) {
        return Integer.parseInt(text == null ? "" : text.trim());
    }
}
