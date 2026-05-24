package FlightManagementBookingSystem;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.json.JSONArray;

public class FlightServer {
    private static Connection connection;
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            // Initialize database
            initializeDatabase();
            
            // Create HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // Set up routes
            server.createContext("/api/flights", new FlightHandler());
            server.createContext("/api/passengers", new PassengerHandler());
            server.createContext("/api/bookings", new BookingHandler());
            server.createContext("/api/stats", new StatsHandler());
            server.createContext("/", new StaticFileHandler());
            
            System.out.println("Server started on port " + PORT);
            System.out.println("Open http://localhost:" + PORT + " in your browser");
            
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() {
        try {
            // Load SQLite driver
            Class.forName("org.sqlite.JDBC");
            
            // Connect to SQLite database
            connection = DriverManager.getConnection("jdbc:sqlite:flight_booking.db");
            
            Statement stmt = connection.createStatement();
            
            // Create tables if they don't exist
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS flights (
                    id TEXT PRIMARY KEY,
                    source TEXT NOT NULL,
                    destination TEXT NOT NULL,
                    departure_time TEXT NOT NULL,
                    arrival_time TEXT NOT NULL,
                    total_seats INTEGER NOT NULL,
                    available_seats INTEGER NOT NULL,
                    price REAL NOT NULL
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS passengers (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    phone TEXT NOT NULL
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bookings (
                    id TEXT PRIMARY KEY,
                    flight_id TEXT NOT NULL,
                    passenger_id TEXT NOT NULL,
                    seats_booked INTEGER NOT NULL,
                    amount REAL NOT NULL,
                    date_time TEXT NOT NULL,
                    status TEXT NOT NULL,
                    FOREIGN KEY (flight_id) REFERENCES flights(id),
                    FOREIGN KEY (passenger_id) REFERENCES passengers(id)
                )
            """);
            
            System.out.println("Database initialized successfully!");
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Flight Handler
    static class FlightHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            
            try {
                if ("GET".equals(method)) {
                    getFlights(exchange);
                } else if ("POST".equals(method)) {
                    addFlight(exchange);
                } else if ("OPTIONS".equals(method)) {
                    sendCorsHeaders(exchange);
                    exchange.sendResponseHeaders(200, -1);
                }
            } catch (Exception e) {
                sendError(exchange, 500, e.getMessage());
            }
        }

        private void getFlights(HttpExchange exchange) throws Exception {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM flights");
            
            JSONArray flights = new JSONArray();
            while (rs.next()) {
                JSONObject flight = new JSONObject();
                flight.put("id", rs.getString("id"));
                flight.put("source", rs.getString("source"));
                flight.put("destination", rs.getString("destination"));
                flight.put("departureTime", rs.getString("departure_time"));
                flight.put("arrivalTime", rs.getString("arrival_time"));
                flight.put("totalSeats", rs.getInt("total_seats"));
                flight.put("availableSeats", rs.getInt("available_seats"));
                flight.put("price", rs.getDouble("price"));
                flights.put(flight);
            }
            
            rs.close();
            stmt.close();
            
            sendResponse(exchange, 200, flights.toString());
        }

        private void addFlight(HttpExchange exchange) throws Exception {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(body);
            
            String id = generateId("F");
            String source = json.getString("source").toUpperCase();
            String destination = json.getString("destination").toUpperCase();
            String departureTime = json.getString("departureTime");
            String arrivalTime = json.getString("arrivalTime");
            int totalSeats = json.getInt("totalSeats");
            double price = json.getDouble("price");
            
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO flights (id, source, destination, departure_time, arrival_time, total_seats, available_seats, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            pstmt.setString(1, id);
            pstmt.setString(2, source);
            pstmt.setString(3, destination);
            pstmt.setString(4, departureTime);
            pstmt.setString(5, arrivalTime);
            pstmt.setInt(6, totalSeats);
            pstmt.setInt(7, totalSeats);
            pstmt.setDouble(8, price);
            pstmt.executeUpdate();
            pstmt.close();
            
            JSONObject response = new JSONObject();
            response.put("id", id);
            response.put("message", "Flight added successfully");
            
            sendResponse(exchange, 201, response.toString());
        }
    }

    // Passenger Handler
    static class PassengerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            
            try {
                if ("GET".equals(method)) {
                    getPassengers(exchange);
                } else if ("POST".equals(method)) {
                    addPassenger(exchange);
                } else if ("OPTIONS".equals(method)) {
                    sendCorsHeaders(exchange);
                    exchange.sendResponseHeaders(200, -1);
                }
            } catch (Exception e) {
                sendError(exchange, 500, e.getMessage());
            }
        }

        private void getPassengers(HttpExchange exchange) throws Exception {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM passengers");
            
            JSONArray passengers = new JSONArray();
            while (rs.next()) {
                JSONObject passenger = new JSONObject();
                passenger.put("id", rs.getString("id"));
                passenger.put("name", rs.getString("name"));
                passenger.put("email", rs.getString("email"));
                passenger.put("phone", rs.getString("phone"));
                passengers.put(passenger);
            }
            
            rs.close();
            stmt.close();
            
            sendResponse(exchange, 200, passengers.toString());
        }

        private void addPassenger(HttpExchange exchange) throws Exception {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(body);
            
            String id = generateId("P");
            String name = json.getString("name");
            String email = json.getString("email");
            String phone = json.getString("phone");
            
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO passengers (id, name, email, phone) VALUES (?, ?, ?, ?)"
            );
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.executeUpdate();
            pstmt.close();
            
            JSONObject response = new JSONObject();
            response.put("id", id);
            response.put("message", "Passenger added successfully");
            
            sendResponse(exchange, 201, response.toString());
        }
    }

    // Booking Handler
    static class BookingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            
            try {
                if ("GET".equals(method)) {
                    getBookings(exchange);
                } else if ("POST".equals(method)) {
                    createBooking(exchange);
                } else if ("PUT".equals(method)) {
                    cancelBooking(exchange);
                } else if ("OPTIONS".equals(method)) {
                    sendCorsHeaders(exchange);
                    exchange.sendResponseHeaders(200, -1);
                }
            } catch (Exception e) {
                sendError(exchange, 500, e.getMessage());
            }
        }

        private void getBookings(HttpExchange exchange) throws Exception {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM bookings ORDER BY date_time DESC");
            
            JSONArray bookings = new JSONArray();
            while (rs.next()) {
                JSONObject booking = new JSONObject();
                booking.put("id", rs.getString("id"));
                booking.put("flightId", rs.getString("flight_id"));
                booking.put("passengerId", rs.getString("passenger_id"));
                booking.put("seatsBooked", rs.getInt("seats_booked"));
                booking.put("amount", rs.getDouble("amount"));
                booking.put("dateTime", rs.getString("date_time"));
                booking.put("status", rs.getString("status"));
                bookings.put(booking);
            }
            
            rs.close();
            stmt.close();
            
            sendResponse(exchange, 200, bookings.toString());
        }

        private void createBooking(HttpExchange exchange) throws Exception {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(body);
            
            String flightId = json.getString("flightId");
            String passengerId = json.getString("passengerId");
            int seats = json.getInt("seats");
            
            // Check flight availability
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM flights WHERE id = ?"
            );
            pstmt.setString(1, flightId);
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.next()) {
                sendError(exchange, 404, "Flight not found");
                rs.close();
                pstmt.close();
                return;
            }
            
            int availableSeats = rs.getInt("available_seats");
            double price = rs.getDouble("price");
            rs.close();
            pstmt.close();
            
            if (availableSeats < seats) {
                sendError(exchange, 400, "Not enough available seats");
                return;
            }
            
            // Create booking
            String bookingId = generateId("B");
            double totalAmount = seats * price;
            String dateTime = new java.util.Date().toString();
            
            connection.setAutoCommit(false);
            
            pstmt = connection.prepareStatement(
                "INSERT INTO bookings (id, flight_id, passenger_id, seats_booked, amount, date_time, status) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            pstmt.setString(1, bookingId);
            pstmt.setString(2, flightId);
            pstmt.setString(3, passengerId);
            pstmt.setInt(4, seats);
            pstmt.setDouble(5, totalAmount);
            pstmt.setString(6, dateTime);
            pstmt.setString(7, "CONFIRMED");
            pstmt.executeUpdate();
            pstmt.close();
            
            // Update available seats
            pstmt = connection.prepareStatement(
                "UPDATE flights SET available_seats = available_seats - ? WHERE id = ?"
            );
            pstmt.setInt(1, seats);
            pstmt.setString(2, flightId);
            pstmt.executeUpdate();
            pstmt.close();
            
            connection.commit();
            connection.setAutoCommit(true);
            
            JSONObject response = new JSONObject();
            response.put("id", bookingId);
            response.put("message", "Booking created successfully");
            
            sendResponse(exchange, 201, response.toString());
        }

        private void cancelBooking(HttpExchange exchange) throws Exception {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(body);
            String bookingId = json.getString("bookingId");
            
            // Get booking details
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM bookings WHERE id = ?"
            );
            pstmt.setString(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.next()) {
                sendError(exchange, 404, "Booking not found");
                rs.close();
                pstmt.close();
                return;
            }
            
            String status = rs.getString("status");
            if ("CANCELLED".equals(status)) {
                sendError(exchange, 400, "Booking already cancelled");
                rs.close();
                pstmt.close();
                return;
            }
            
            String flightId = rs.getString("flight_id");
            int seatsBooked = rs.getInt("seats_booked");
            rs.close();
            pstmt.close();
            
            connection.setAutoCommit(false);
            
            // Update booking status
            pstmt = connection.prepareStatement(
                "UPDATE bookings SET status = ? WHERE id = ?"
            );
            pstmt.setString(1, "CANCELLED");
            pstmt.setString(2, bookingId);
            pstmt.executeUpdate();
            pstmt.close();
            
            // Restore seats
            pstmt = connection.prepareStatement(
                "UPDATE flights SET available_seats = available_seats + ? WHERE id = ?"
            );
            pstmt.setInt(1, seatsBooked);
            pstmt.setString(2, flightId);
            pstmt.executeUpdate();
            pstmt.close();
            
            connection.commit();
            connection.setAutoCommit(true);
            
            JSONObject response = new JSONObject();
            response.put("message", "Booking cancelled successfully");
            
            sendResponse(exchange, 200, response.toString());
        }
    }

    // Stats Handler
    static class StatsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                Statement stmt = connection.createStatement();
                
                ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM flights");
                rs1.next();
                int totalFlights = rs1.getInt(1);
                rs1.close();
                
                ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM passengers");
                rs2.next();
                int totalPassengers = rs2.getInt(1);
                rs2.close();
                
                ResultSet rs3 = stmt.executeQuery("SELECT COUNT(*) FROM bookings WHERE status = 'CONFIRMED'");
                rs3.next();
                int totalBookings = rs3.getInt(1);
                rs3.close();
                
                ResultSet rs4 = stmt.executeQuery("SELECT SUM(available_seats) FROM flights");
                rs4.next();
                int totalSeats = rs4.getInt(1);
                rs4.close();
                
                stmt.close();
                
                JSONObject stats = new JSONObject();
                stats.put("totalFlights", totalFlights);
                stats.put("totalPassengers", totalPassengers);
                stats.put("totalBookings", totalBookings);
                stats.put("totalSeats", totalSeats);
                
                sendResponse(exchange, 200, stats.toString());
            } catch (Exception e) {
                sendError(exchange, 500, e.getMessage());
            }
        }
    }

    // Static File Handler
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if ("/".equals(path)) {
                path = "/index.html";
            }
            
            // Try multiple possible paths
            File file = null;
            String[] possiblePaths = {
                "src/FlightManagementBookingSystem" + path,
                "FlightManagementBookingSystem" + path,
                System.getProperty("user.dir") + "/src/FlightManagementBookingSystem" + path
            };
            
            for (String filePath : possiblePaths) {
                File tempFile = new File(filePath);
                if (tempFile.exists()) {
                    file = tempFile;
                    break;
                }
            }
            
            if (file == null || !file.exists()) {
                String errorMsg = "File not found: " + path + "\n";
                errorMsg += "Tried paths: \n";
                errorMsg += "1. src/FlightManagementBookingSystem" + path + "\n";
                errorMsg += "2. FlightManagementBookingSystem" + path + "\n";
                errorMsg += "3. " + System.getProperty("user.dir") + "/src/FlightManagementBookingSystem" + path;
                sendError(exchange, 404, errorMsg);
                return;
            }
            
            String contentType = getContentType(path);
            byte[] response = file.toURI().toURL().openStream().readAllBytes();
            
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        }

        private String getContentType(String path) {
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".png")) return "image/png";
            if (path.endsWith(".jpg")) return "image/jpeg";
            return "text/plain";
        }
    }

    // Helper methods
    private static String generateId(String prefix) {
        return prefix + "-" + System.currentTimeMillis() % 10000;
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        sendCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }

    private static void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        JSONObject error = new JSONObject();
        error.put("error", message);
        sendResponse(exchange, statusCode, error.toString());
    }

    private static void sendCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }
}
