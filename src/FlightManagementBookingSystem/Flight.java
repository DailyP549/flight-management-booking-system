package FlightManagementBookingSystem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Flight {
  private  final String id;
  private   String source;
  private   String destination;
  private LocalDateTime departureTime;
  private  LocalDateTime arrivalTime;
  private   double price;
  private  int availableSeats;
  private int totalSeats;
  private static final DateTimeFormatter fmt=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  public Flight(String id,String source,String destination,LocalDateTime departureTime,LocalDateTime arrivalTime,int totalSeats,double price){
      this.id=id;
      this.source=source;
      this.destination=destination;
      this.departureTime=departureTime;
      this.arrivalTime=arrivalTime;
      this.totalSeats=totalSeats;
      this.availableSeats=totalSeats;
      this.price=price;

  }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
    public void setAvailableSeats(int availableSeats){
      this.availableSeats=availableSeats;
    }
    @Override
    public String toString(){
      return "Flight{"+"id='"+'\''+", source"+source+
                ", destination='"+destination+'\''+
              ", departureTime="+departureTime.format(fmt)+
              ", arrivalTime="+arrivalTime.format(fmt)+"," +
              ",totalSeats="+totalSeats+
              " price="+price+
              ", availableSeats="
              +availableSeats+'}';
    }
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if(!(o instanceof Flight)) return false;
      Flight flight = (Flight) o;
      return Objects.equals(id,flight.id);
    }
    @Override
    public int hashCode() {
      return Objects.hash(id);
    }
}
