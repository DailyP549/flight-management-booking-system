package FlightManagementBookingSystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingService {
    private final InMemoryData  bookingData;
    private final PassengerService passengerService;
    private final FlightService flightService;
    public BookingService(InMemoryData bookingData, PassengerService passengerService, FlightService flightService) {
        this.bookingData = bookingData;
        this.passengerService = passengerService;
        this.flightService = flightService;
    }
    public List<Booking> getBookings()
    {
        return new ArrayList<>(bookingData.getBookings().values());
    }
    public Optional<Booking> getBooking(String id){
        return Optional.ofNullable(bookingData.getBookings().get(id));
    }
    public Optional<Booking> createBooking(String flightId,String passengerId,int seats){
        Optional<Flight>flight=flightService.getById(flightId);
        Optional<Passenger> passenger=passengerService.getById(passengerId);
        if(flight.isEmpty()||passenger.isEmpty()||seats<=0){
            return Optional.empty();

        }
        Flight f=flight.get();
        if(!flightService.reduceService(flightId,seats)){
            return Optional.empty();

        }double totalAmount=seats*f.getPrice();
        String id=bookingData.nextBookingId();
        Booking booking=new Booking(id,flightId,passengerId,seats,totalAmount, LocalDateTime.now(),BookingStatus.CONFIRMED);
        bookingData.getBookings().put(id,booking);
        return Optional.of(booking);
    }
  public boolean cancelBooking(String bookingId){
        Booking booking=bookingData.getBookings().get(bookingId);
        if(booking==null ||booking.getStatus()==BookingStatus.CANCELLED) return false;

        booking.setStatus(BookingStatus.CANCELLED);
        flightService.increaseSeats(booking.getFlightId(),booking.getSeatsBooked());
        return true;
    }
}
