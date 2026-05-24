package FlightManagementBookingSystem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryData {
    private Map<String,Passenger> passengersMap=new LinkedHashMap<>();
    private Map<String,Flight> flights=new LinkedHashMap<>();
    private final AtomicInteger passengerId=new AtomicInteger(0);
private Map<String ,Booking> bookings=new LinkedHashMap<>();
    private final AtomicInteger flightId=new AtomicInteger(0);
    private final AtomicInteger bookingId=new AtomicInteger(0);

    public String nextPassengerId(){
        return "P-"+
                passengerId.getAndIncrement();
    }public String nextFlightId(){
        return "F-"+
                flightId.getAndIncrement();
    }
    public String nextBookingId(){
        return "B-"+
                bookingId.getAndIncrement();
    }
    public Map<String,Passenger> getPassengers(){
        return passengersMap;
    }
    public Map<String,Flight> getFlights(){
        return flights;
    }

    public Map<String, Booking> getBookings() {
        return bookings;
    }

    public AtomicInteger getBookingId() {
        return bookingId;
    }
}
