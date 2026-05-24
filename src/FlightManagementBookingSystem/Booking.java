package FlightManagementBookingSystem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
private final String  id;
private final String  flightId;
private  final String passengerId;
private  final int seatsBooked;
private final double amount;
private final LocalDateTime dateTime;
private BookingStatus status;
private static final DateTimeFormatter fmt=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
public Booking(String id, String flightId, String passengerId, int seatsBooked, double amount, LocalDateTime dateTime, BookingStatus bookingStatus) {

    this.id = id;
    this.flightId = flightId;
    this.passengerId = passengerId;
    this.seatsBooked = seatsBooked;
    this.amount = amount;
    this.dateTime = dateTime;
    this.status = bookingStatus;
}


    public String getId() {
        return id;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    @Override
    public String toString() {
    return "Booking{" +
            "id='" + id + '\'' +
            ", flightId='" + flightId + '\'' +
            ", passengerId='" + passengerId + '\'' +
            ", seatsBooked=" + seatsBooked +
            ", amount=" + amount +
            ", dateTime=" + dateTime.format(fmt) +
            ", status=" + status +
            '}';
    }

}
