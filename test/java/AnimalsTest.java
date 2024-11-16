import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

class AnimalsTest {
    @BeforeEach
    public void before(){
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/wildlife_tracker_test", "postgres", "1234");  //Those with linux or windows use two strings for username and password
    }
    //empty animals
    @AfterEach
    public void after(){
        try(Connection con = DB.sql2o.open()) {
            String deleteAnimalQuery="DELETE FROM animals ";
            con.createQuery(deleteAnimalQuery).executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    @Test
    public void testInstanceofAnimals_true(){
        Animals testAnimals = setUpAnimal();
        assertEquals(true, testAnimals instanceof Animals);
    }
    // check if saving
    @Test
    public void instanceSaved(){
        Animals testAnimals = setUpAnimal();
        testAnimals.save();
        assertTrue(Animals.all().get(0).equals(testAnimals));
    }

    //deleted
    @Test
    public void deleteById(){
        Animals testAnimals = setUpAnimal();
        testAnimals.save();
        testAnimals.delete();
        assertEquals(null, Animals.find(testAnimals.getId()));
    }
    // catch empty error
    @Test
    public void checkIfempty(){
        Animals testAnimal = new Animals("","normal");
        try{
            testAnimal.save();
        }catch(IllegalArgumentException e){

        }
    }
    // clear all animals
    @Test
    public void deleteAll(){
        Animals animal1 = setUpAnimal();
        Animals animal2 =setUpAnimal();
        animal1.save();
        animal2.save();
        Animals.deleteAll();
        List<Animals> animals = Animals.all();
        assertEquals(0,animals.size());
    }
    //return correct data of animal
    @Test
    public void findbyId(){
        Animals testAnimal = setUpAnimal();
        testAnimal.save();
        Animals found = Animals.find(testAnimal.getId());
        assertTrue(found.equals(testAnimal));
    }
    // correct update
    @Test
    public void ensureEntryIsUpdatedCorrectly() {
        Animals testAnimal=setUpAnimal();
        Animals otherAnimal=testAnimal;
        testAnimal.save();
        try {
            testAnimal.update(testAnimal.getId(),"endangered","ill","young");
            Animals updatedAnimal=  Animals.find(testAnimal.getId());
            assertEquals(updatedAnimal.getId(),otherAnimal.getId());
            assertNotEquals(updatedAnimal.getHealth(),otherAnimal.getHealth());
        }catch (IllegalArgumentException e){

        }
    }

    //helper
    private Animals setUpAnimal() {
        return new Animals("deer", "endangered");
    }
}








