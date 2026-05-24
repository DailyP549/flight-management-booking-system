package FlightManagementBookingSystem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Demo {
    public static void main(String[] args) {
        InMemoryData data = new InMemoryData();
        FlightService flightService = new FlightService(data);
        PassengerService passengerService = new PassengerService(data);
        BookingService bookingService = new BookingService(data, passengerService, flightService);

        System.out.println("=== Flight Management Booking System - DEMO ===\n");

        // Add flights
        System.out.println("1. ADDING FLIGHTS...");
        Flight f1 = flightService.addFlight("New York", "Los Angeles",
                LocalDateTime.of(2026, 12, 25, 10, 0),
                LocalDateTime.of(2026, 12, 25, 15, 30),
                150, 250.0);
        System.out.printf("  Flight ID: %s | Route: %s -> %s | Date: %s to %s | Seats: %d | Price: $%.2f%n",
                f1.getId(), f1.getSource(), f1.getDestination(),
                f1.getDepartureTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                f1.getArrivalTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                150, 250.0);

        Flight f2 = flightService.addFlight("Chicago", "Miami",
                LocalDateTime.of(2026, 12, 26, 8, 0),
                LocalDateTime.of(2026, 12, 26, 11, 30),
                100, 180.0);
        System.out.printf("  Flight ID: %s | Route: %s -> %s | Date: %s to %s | Seats: %d | Price: $%.2f%n",
                f2.getId(), f2.getSource(), f2.getDestination(),
                f2.getDepartureTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                f2.getArrivalTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                100, 180.0);

        // Add passengers
        System.out.println("\n2. ADDING PASSENGERS...");
        Passenger p1 = passengerService.addPassenger("John Doe", "john@email.com", "1234567890");
        System.out.printf("  ID: %s | Name: %s | Email: %s | Phone: %s%n",
                p1.getId(), p1.getName(), p1.getEmail(), p1.getPhone());

        Passenger p2 = passengerService.addPassenger("Jane Smith", "jane@email.com", "0987654321");
        System.out.printf("  ID: %s | Name: %s | Email: %s | Phone: %s%n",
                p2.getId(), p2.getName(), p2.getEmail(), p2.getPhone());

        // Create bookings
        System.out.println("\n3. CREATING BOOKINGS...");
        Optional<Booking> b1 = bookingService.createBooking(f1.getId(), p1.getId(), 2);
        if (b1.isPresent()) {
            Booking b = b1.get();
            System.out.printf("  Booking ID: %s | Flight: %s | Passenger: %s | Seats: %d | Total: $%.2f | Status: %s%n",
                    b.getId(), b.getFlightId(), b.getPassengerId(),
                    b.getSeatsBooked(), b.getAmount(), b.getStatus());
        }

        Optional<Booking> b2 = bookingService.createBooking(f2.getId(), p2.getId(), 3);
        if (b2.isPresent()) {
            Booking b = b2.get();
            System.out.printf("  Booking ID: %s | Flight: %s | Passenger: %s | Seats: %d | Total: $%.2f | Status: %s%n",
                    b.getId(), b.getFlightId(), b.getPassengerId(),
                    b.getSeatsBooked(), b.getAmount(), b.getStatus());
        }

        // View all flights
        System.out.println("\n4. ALL FLIGHTS:");
        List<Flight> flights = flightService.getFlights();
        System.out.printf("  %-6s | %-15s | %-15s | %-16s | %-16s | %-8s | %-10s | %-8s%n",
                "ID", "Source", "Destination", "Departure", "Arrival", "Seats", "Price($)", "Available");
        System.out.println("  -------|-----------------|-----------------|------------------|------------------|----------|------------|----------");
        flights.forEach(f -> System.out.printf("  %-6s | %-15s | %-15s | %-16s | %-16s | %-8d | %-10.2f | %-8d%n",
                f.getId(), f.getSource(), f.getDestination(),
                f.getDepartureTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                f.getArrivalTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                0, f.getPrice(), f.getAvailableSeats()));

        // View all passengers
        System.out.println("\n5. ALL PASSENGERS:");
        List<Passenger> passengers = passengerService.getAllPassengers();
        System.out.printf("  %-6s | %-20s | %-25s | %-15s%n",
                "ID", "Name", "Email", "Phone");
        System.out.println("  -------|----------------------|---------------------------|----------------");
        passengers.forEach(p -> System.out.printf("  %-6s | %-20s | %-25s | %-15s%n",
                p.getId(), p.getName(), p.getEmail(), p.getPhone()));

        // View all bookings
        System.out.println("\n6. ALL BOOKINGS:");
        List<Booking> bookings = bookingService.getBookings();
        System.out.printf("  %-6s | %-6s | %-6s | %-6s | %-10s | %-16s | %-12s%n",
                "ID", "Flight", "Passngr", "Seats", "Amount($)", "Date/Time", "Status");
        System.out.println("  -------|--------|--------|--------|------------|------------------|-------------");
        bookings.forEach(b -> System.out.printf("  %-6s | %-6s | %-6s | %-6d | %-10.2f | %-16s | %-12s%n",
                b.getId(), b.getFlightId(), b.getPassengerId(),
                b.getSeatsBooked(), b.getAmount(),
                b.getDateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                b.getStatus()));

        // Cancel a booking
        System.out.println("\n7. CANCELING BOOKING " + b1.get().getId() + "...");
        boolean cancelled = bookingService.cancelBooking(b1.get().getId());
        System.out.println("  Booking cancelled: " + cancelled);

        // Show updated flight
        System.out.println("\n8. UPDATED FLIGHT SEATS AFTER CANCELLATION:");
        Optional<Flight> updatedFlight = flightService.getById(f1.getId());
        updatedFlight.ifPresent(f -> System.out.printf("  Flight %s: %d seats available (restored from cancellation)%n",
                f.getId(), f.getAvailableSeats()));

        System.out.println("\n=== DEMO COMPLETED SUCCESSFULLY! ===");
    }
}
