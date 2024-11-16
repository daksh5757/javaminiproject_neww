import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

class SightingsTest {
    @BeforeEach
    public void before() {
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/wildlife_tracker_test", "postgres", "1234");  //Those with linux or windows use two strings for username and password
    }

    //empty animals
    @AfterEach
    public void after() {
        try (Connection con = DB.sql2o.open()) {
            String deleteSightingsQuery = "DELETE FROM sightings ";
            con.createQuery(deleteSightingsQuery).executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Test
    public void createInstanceOfSightingsClass_true() {
        Sightings sighting= setSighting();
        assertEquals(true,sighting instanceof Sightings);
    }
    //save

    @Test
    public void allInstancesAreSaved() {
        Sightings sightings=setSighting();
        Sightings otherSighting=new Sightings(2,1,1);
        try {
            sightings.save();
            otherSighting.save();
            assertTrue(Sightings.find(sightings.getId()).equals(sightings));
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }
    //find the sighting;
    @Test
    public void findSightingByID() {
        Sightings sighting=setSighting();
        sighting.save();
        Sightings foundSighting=Sightings.find(sighting.getId());
        assertTrue(foundSighting.equals(sighting));

    }
    //delete sighting
    @Test
    public void deleteSightingByID() {
        Sightings sighting=setSighting();
        sighting.save();
        sighting.delete();
        assertEquals(null,Sightings.find(sighting.getId()));

    }
    //helper

    private Sightings setSighting() {
        return new Sightings(1,1,1);
    }
}