import org.sql2o.Sql2oException;
//import java.sql.Connection;
import org.sql2o.Connection;

import java.util.List;
import java.util.Objects;

public class Animals implements DatabaseManagement {
    public String name;
    public String health;
    public String age;
    public String type;
    public int id;
    public static final String ANIMAL_TYPE = "normal";

    public Animals(String name, String type) {
        this.name = name;
        this.type = ANIMAL_TYPE;
        this.age = "";
        this.health = "";
    }

    public String getName() {
        return name;
    }

    public String getHealth() {
        return health;
    }

    public String getAge() {
        return age;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    // save method to save the animal
    public void save() {
        if (this.name.equals("") || this.type.equals("") || this.name.equals(null) || this.type.equals(null)) {
            throw new IllegalArgumentException("Validate all the inputs");
        }
        try (Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO animals (name,type) VALUES (:name,:type)";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .addParameter("type", this.type)
                    .executeUpdate()
                    .getKey();
        }catch (Sql2oException e) {
            System.out.println(e);
        }
    }

    //update
    public void update(int id, String type, String health, String age) {
        try (Connection con = DB.sql2o.open()) {
            if (type.equals("")) {
                throw new IllegalArgumentException("All fields must be filled");
            }
            if (type == "endangered") {
                if (health.equals("") || age.equals("")) {
                    throw new IllegalArgumentException("All fields must be filled");
                }
                String sql = "UPDATE animals SET type=:type,health=:health,age=:age WHERE id=:id";
                con.createQuery(sql)
                        .addParameter("type", type)
                        .addParameter("health", health)
                        .addParameter("age", age)
                        .addParameter("id", this.id)
                        .executeUpdate();
            } else {

                String sql = "UPDATE animals SET type=:type,health=:health,age=:age WHERE id=:id";
                con.createQuery(sql)
                        .addParameter("type", type)
                        .addParameter("health", "")
                        .addParameter("age", "")
                        .addParameter("id", this.id)
                        .executeUpdate();
            }

        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    public static Animals find(int id) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM animals WHERE id=:id";
            Animals animal = con.createQuery(sql)
                    .addParameter("id", id)
                    .throwOnMappingFailure(false)
                    .executeAndFetchFirst(Animals.class);
            return animal;

        }

    }

    public void delete() {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM animals WHERE id=:id";
            con.createQuery(sql)
                    .addParameter("id", this.id)
                    .executeUpdate();

        }catch (Sql2oException e) {
            System.out.println(e);
        }
    }

    public static void deleteAll() {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM animals";
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
    public static List<Animals> all(){
        try (Connection con=DB.sql2o.open()) {
            String sql ="SELECT * FROM animals";
            return con.createQuery(sql)
                    .throwOnMappingFailure(false)
                    .executeAndFetch(Animals.class);

        }
    }
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if (o == null || getClass() !=o.getClass()) return false;
        Animals animals = (Animals) o;
        return name.equals(animals.name)&&
                type.equals(animals.type);
    }
    @Override
    public int hashCode(){
        return Objects.hash(name, type);
    }
}

















