package FlightManagementBookingSystem;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PassengerService
{
    private final InMemoryData passengerData;
    public PassengerService(InMemoryData passengerData)
    {
        this.passengerData = passengerData;

    }
    public  Passenger addPassenger(String name,String email,String phone)
    {
        String id=passengerData.nextPassengerId();
        Passenger passenger=new Passenger(id,name.trim(),email.trim(),phone.trim());
        passengerData.getPassengers().put(id,passenger);
        return passenger;
    }
    public Optional<Passenger> getById(String id){
        return Optional.ofNullable(passengerData.getPassengers().get(id));
    }
    public List<Passenger> getAllPassengers(){
        return new ArrayList<>(passengerData.getPassengers().values());
    }
}
