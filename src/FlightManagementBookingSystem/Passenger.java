package FlightManagementBookingSystem;

import java.util.Objects;

public class Passenger {
    private final String id;
    private  String name;
    private String email;
    private String phone;
    public Passenger(String id,String name,String email,String phone){
this.id=id;
this.name=name;
this.email=email;
this.phone=phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    public String getEmail() {
        return email;
    }


    public String getPhone() {
        return phone;
    }
    @Override
    public String toString() {
        return "Passenger{"+
                "id='"+id+'\''+", name='"+name+'\''+", email='"+email+'\''+", phone='"+phone+'\''+'}';

    }
    @Override
    public boolean equals(Object o){
        if(this ==o) return true;
if(!(o instanceof Passenger)) return false;

Passenger  that=(Passenger) o;
return this.id.equals(that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
