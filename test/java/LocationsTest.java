import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

class LocationsTest {
    @BeforeEach
    public void before(){
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/wildlife_tracker_test", "postgres", "1234");  //Those with linux or windows use two strings for username and password
    }
    //empty animals
    @AfterEach
    public void after(){
        try(Connection con = DB.sql2o.open()) {
            String deleteLOcationQuery="DELETE FROM locations ";
            con.createQuery(deleteLOcationQuery).executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }
    }
   // instance
   @Test
    public void createInstance(){
        Locations location = setLocation();
        assertEquals(true, location instanceof Locations);
   }
   //save locations
   @Test
   public void allLocationsSaved(){
       Locations location = setLocation();
       Locations newLocation = new Locations("");
       try{
           location.save();
           assertTrue(Locations.all().get(0).equals(location));
       }catch (IllegalArgumentException e){
           System.out.println(e);
       }
   }
   //delete
   @Test
   public void entryIsDeletedSuccessfully() {
       Locations location=setLocation();
       Locations newLocation=new Locations("side Hill");
       location.save();
       newLocation.save();
       location.delete();
       assertEquals(null,Locations.find(location.getId()));
   }

   //help
    private Locations setLocation() {
        return new Locations("Valley Rock");
    }

}








