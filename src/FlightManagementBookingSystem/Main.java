package FlightManagementBookingSystem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    // Available airports/cities
    private static final String[] AVAILABLE_SOURCES = {
        "New York", "Los Angeles", "Chicago", "Miami", "Houston",
        "San Francisco", "Seattle", "Boston", "Denver", "Atlanta",
        "Dallas", "Phoenix", "Las Vegas", "Orlando", "Detroit"
    };

    public static void main(String[] args) {
        // Initialize shared data and services
        InMemoryData data = new InMemoryData();
        FlightService flightService = new FlightService(data);
        PassengerService passengerService = new PassengerService(data);
        BookingService bookingService = new BookingService(data, passengerService, flightService);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Flight Management Booking System ===\n");

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add Flight");
            System.out.println("2. Add Passenger");
            System.out.println("3. Create Booking");
            System.out.println("4. View All Flights");
            System.out.println("5. View All Passengers");
            System.out.println("6. View All Bookings");
            System.out.println("7. Cancel Booking");
            System.out.println("8. Search Flights");
            System.out.println("0. Exit");
            System.out.print("\nEnter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("\n--- Add New Flight ---");
                    System.out.println("\nAvailable Sources:");
                    for (int i = 0; i < AVAILABLE_SOURCES.length; i++) {
                        System.out.println((i + 1) + ". " + AVAILABLE_SOURCES[i]);
                    }
                    System.out.print("\nSource (from list above or enter custom): ");
                    String source = scanner.nextLine();
                    System.out.println("\nAvailable Destinations:");
                    for (int i = 0; i < AVAILABLE_SOURCES.length; i++) {
                        System.out.println((i + 1) + ". " + AVAILABLE_SOURCES[i]);
                    }
                    System.out.print("\nDestination (from list above or enter custom): ");
                    String destination = scanner.nextLine();
                    
                    System.out.print("\nDeparture Date (yyyy-MM-dd): ");
                    String departureDate = scanner.nextLine();
                    System.out.print("Departure Time (HH:mm): ");
                    String departureTime = scanner.nextLine();
                    
                    System.out.print("\nArrival Date (yyyy-MM-dd): ");
                    String arrivalDate = scanner.nextLine();
                    System.out.print("Arrival Time (HH:mm): ");
                    String arrivalTime = scanner.nextLine();
                    
                    System.out.print("\nTotal Seats: ");
                    int totalSeats = scanner.nextInt();
                    System.out.print("Price per Seat: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();

                    LocalDateTime departure = LocalDateTime.parse(departureDate + "T" + departureTime);
                    LocalDateTime arrival = LocalDateTime.parse(arrivalDate + "T" + arrivalTime);

                    Flight newFlight = flightService.addFlight(source, destination, departure, arrival, totalSeats, price);
                    System.out.println("\nFlight added successfully: " + newFlight);
                    break;

                case 2:
                    System.out.println("\n--- Add New Passenger ---");
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Phone: ");
                    String phone = scanner.nextLine();

                    Passenger newPassenger = passengerService.addPassenger(name, email, phone);
                    System.out.println("\nPassenger added successfully: " + newPassenger);
                    break;

                case 3:
                    System.out.println("\n--- Create Booking ---");
                    System.out.print("Flight ID (e.g., F-0): ");
                    String flightId = scanner.nextLine();
                    System.out.print("Passenger ID (e.g., P-0): ");
                    String passengerId = scanner.nextLine();
                    System.out.print("Number of Seats: ");
                    int seats = scanner.nextInt();
                    scanner.nextLine();

                    Optional<Booking> booking = bookingService.createBooking(flightId, passengerId, seats);
                    if (booking.isPresent()) {
                        System.out.println("\nBooking created successfully: " + booking.get());
                    } else {
                        System.out.println("\nFailed to create booking! Check flight ID, passenger ID, or seat availability.");
                    }
                    break;

                case 4:
                    System.out.println("\n--- All Flights ---");
                    List<Flight> flights = flightService.getFlights();
                    if (flights.isEmpty()) {
                        System.out.println("No flights available.");
                    } else {
                        System.out.printf("%-6s | %-15s | %-15s | %-16s | %-16s | %-8s | %-10s | %-8s%n",
                                "ID", "Source", "Destination", "Departure", "Arrival", "Seats", "Price($)", "Available");
                        System.out.println("-------|-----------------|-----------------|------------------|------------------|----------|------------|----------");
                        flights.forEach(f -> System.out.printf("%-6s | %-15s | %-15s | %-16s | %-16s | %-8d | %-10.2f | %-8d%n",
                                f.getId(), f.getSource(), f.getDestination(),
                                f.getDepartureTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                                f.getArrivalTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                                0, f.getPrice(), f.getAvailableSeats()));
                    }
                    break;

                case 5:
                    System.out.println("\n--- All Passengers ---");
                    List<Passenger> passengers = passengerService.getAllPassengers();
                    if (passengers.isEmpty()) {
                        System.out.println("No passengers registered.");
                    } else {
                        System.out.printf("%-6s | %-20s | %-25s | %-15s%n",
                                "ID", "Name", "Email", "Phone");
                        System.out.println("-------|----------------------|---------------------------|----------------");
                        passengers.forEach(p -> System.out.printf("%-6s | %-20s | %-25s | %-15s%n",
                                p.getId(), p.getName(), p.getEmail(), p.getPhone()));
                    }
                    break;

                case 6:
                    System.out.println("\n--- All Bookings ---");
                    List<Booking> bookings = bookingService.getBookings();
                    if (bookings.isEmpty()) {
                        System.out.println("No bookings found.");
                    } else {
                        System.out.printf("%-6s | %-6s | %-6s | %-6s | %-10s | %-16s | %-12s%n",
                                "ID", "Flight", "Passngr", "Seats", "Amount($)", "Date/Time", "Status");
                        System.out.println("-------|--------|--------|--------|------------|------------------|-------------");
                        bookings.forEach(b -> System.out.printf("%-6s | %-6s | %-6s | %-6d | %-10.2f | %-16s | %-12s%n",
                                b.getId(), b.getFlightId(), b.getPassengerId(),
                                b.getSeatsBooked(), b.getAmount(),
                                b.getDateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                                b.getStatus()));
                    }
                    break;

                case 7:
                    System.out.println("\n--- Cancel Booking ---");
                    System.out.print("Booking ID (e.g., B-0): ");
                    String bookingId = scanner.nextLine();
                    boolean cancelled = bookingService.cancelBooking(bookingId);
                    if (cancelled) {
                        System.out.println("Booking cancelled successfully!");
                    } else {
                        System.out.println("Failed to cancel booking! Booking may not exist or already cancelled.");
                    }
                    break;

                case 8:
                    System.out.println("\n--- Search Flights ---");
                    System.out.println("\nAvailable Sources:");
                    for (int i = 0; i < AVAILABLE_SOURCES.length; i++) {
                        System.out.println((i + 1) + ". " + AVAILABLE_SOURCES[i]);
                    }
                    System.out.print("\nSource (from list above or enter custom): ");
                    String searchSource = scanner.nextLine();
                    
                    System.out.println("\nAvailable Destinations:");
                    for (int i = 0; i < AVAILABLE_SOURCES.length; i++) {
                        System.out.println((i + 1) + ". " + AVAILABLE_SOURCES[i]);
                    }
                    System.out.print("\nDestination (from list above or enter custom): ");
                    String searchDest = scanner.nextLine();
                    System.out.print("Date (yyyy-MM-dd): ");
                    String dateStr = scanner.nextLine();
                    LocalDateTime searchDate = LocalDateTime.parse(dateStr + "T00:00:00");

                    List<Flight> searchResults = flightService.search(searchSource, searchDest, searchDate);
                    if (searchResults.isEmpty()) {
                        System.out.println("No flights found for the given criteria.");
                    } else {
                        searchResults.forEach(f -> System.out.println(f));
                    }
                    break;

                case 0:
                    System.out.println("\nThank you for using Flight Management Booking System. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("\nInvalid choice! Please try again.");
            }
        }
    }
}
