package FlightManagementBookingSystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightService {
   private final InMemoryData flightsData;

    public FlightService(InMemoryData flightsData) {
        this.flightsData = flightsData;
    }

   public Flight addFlight(String  source,String destination, LocalDateTime departure,LocalDateTime arrival,int totalSeats,double pricePerSeats) {
        String id= flightsData.nextFlightId();
        Flight  flight=new Flight(id,source.trim().toUpperCase(),destination.trim().toUpperCase(),departure,arrival,totalSeats,pricePerSeats);
        flightsData.getFlights().put(id,flight);
        return flight;
   }
   public Optional<Flight> getById(String id){
        return Optional.ofNullable(flightsData.getFlights().get(id));
   }
   public List<Flight> getFlights(){return new ArrayList<>(flightsData.getFlights().values());}
    public boolean reduceService(String flightId,int seats){
        Flight flight=flightsData.getFlights().get(flightId);
        if(flight==null){
            return false;
        }
        if(seats<=0|| flight.getAvailableSeats()<seats){
            return false;

        }
        flight.setAvailableSeats(flight.getAvailableSeats()-seats);
        return true;
    }
    public  void increaseSeats(String flightId,int seats){
        Flight flight=flightsData.getFlights().get(flightId);
        if(flight!=null && seats>0){
           int newAvailableSeats=Math.min(flight.getAvailableSeats(),flight.getAvailableSeats()+seats);
            flight.setAvailableSeats(newAvailableSeats);

        }

    }
    public List<Flight> search(String source,String destination,LocalDateTime date){
        String src=source.trim().toUpperCase();
        String dest=destination.trim().toUpperCase();
        return flightsData.getFlights().values().stream()
                .filter(f->f.getSource().equals(src))
                .filter(f->f.getDestination().equals(src))
                .filter(f->f.getDepartureTime().toLocalDate().equals(date))
                .sorted(Comparator.comparing(Flight::getDepartureTime))
                .collect(Collectors.toList());
    }

}
