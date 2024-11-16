import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

class RangersTest {
    @BeforeEach
    public void before() {
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/wildlife_tracker_test", "postgres", "1234");  //Those with linux or windows use two strings for username and password
    }

    //empty animals
    @AfterEach
    public void after() {
        try (Connection con = DB.sql2o.open()) {
            String deletRangerQuery = "DELETE FROM rangers ";
            con.createQuery(deletRangerQuery).executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Test
    public void createInstanceOfRangersClass_true(){
        Rangers ranger= setRanger();
        assertEquals(true,ranger instanceof Rangers);
    }
    @Test
    public void allEntriesAreSaved() {
        Rangers ranger= setRanger();
        ranger.save();
        assertTrue(Rangers.all().get(0).equals(ranger));

    }
    //find the ranger
    @Test
    public void findByid(){
        Rangers ranger = setRanger();
        Rangers newranger = new Rangers("jared","78747","65");
        ranger.save();
        newranger.save();
        Rangers enteredRanger = Rangers.find(ranger.getId());
        assertTrue(enteredRanger.equals(ranger));
    }
    //delete
    @Test
    public void entriesAreDeleted() {
        Rangers ranger= setRanger();
        Rangers otherRanger=new Rangers("komboz","2","0726108898");
        ranger.save();
        otherRanger.save();
        ranger.delete();
        assertEquals(null,Rangers.find(ranger.getId()));

    }
    @Test
    public void NullFieldsNotSaved() {
        Rangers ranger=new Rangers("","","0713245678");
        try{
            ranger.save();
            assertTrue(Rangers.all().get(0).equals(ranger));
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }
//helper
    private Rangers setRanger() {
        return new Rangers("Korir","0729904081","678");
    }

}







