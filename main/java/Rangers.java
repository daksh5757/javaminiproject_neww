//import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rangers {
    private String name;
    private int id;
    private String phone_number;
    private String badge_number;

    public Rangers(String name, String phone_number, String badge_number) {
        this.name = name;
        this.phone_number = phone_number;
        this.badge_number = badge_number;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getBadge_number() {
        return badge_number;
    }
    public static List<Rangers> all(){
        try(Connection con = DB.sql2o.open()){
            String sql="SELECT * FROM rangers";
            return con.createQuery(sql)
                    .throwOnMappingFailure(false)
                    .executeAndFetch(Rangers.class);
        }
    }
    // save the ranger
    public void save(){
        try(Connection con = DB.sql2o.open()){
            String sql = "INSERT INTO rangers (name,badge_number,phone_number) VALUES (:name,:badge_number,:phone_number)";
            if(name.equals("")||badge_number.equals("")||phone_number.equals("")){
                throw new IllegalArgumentException("Check all the fields");
            }
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .addParameter("badge_number", this.badge_number)
                    .addParameter("phone_number", this.phone_number)
                    .executeUpdate()
                    .getKey();
        }catch (Sql2oException e) {
            System.out.println(e);
        }
    }
    public static Rangers find(int id){
        try(Connection con = DB.sql2o.open()){
            String sql = "SELECT * FROM rangers WHERE id=:id";
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Rangers.class);
        }
    }
    //update
    public void update(int id,String name,String phone_number){
        try (Connection con=DB.sql2o.open()){
            String sql="UPDATE rangers SET name=:name,phone_number=:phone_number WHERE id=:id";
            if(name.equals("")||phone_number.equals("")){
                throw new IllegalArgumentException("All fields must be filled");
            }
            con.createQuery(sql)
                    .addParameter("id",this.id)
                    .addParameter("name",name)
                    .addParameter("phone_number",phone_number)
                    .executeUpdate();
        }catch (Sql2oException e) {
            System.out.println(e);
        }
    }
    //delete ranger
    public void delete(){
        try(Connection con = DB.sql2o.open()){
            String sql = "DELETE FROM rangers WHERE id=:id";
            con.createQuery(sql)
                    .addParameter("id",this.id)
                    .executeUpdate();
        }catch (Sql2oException e) {
            System.out.println(e);
        }
    }
    //get ranger sightings
    public List<Sightings> getRangerSightings(){
        try (Connection con=DB.sql2o.open()){
            String sql="SELECT sighting_id FROM rangers_sightings WHERE ranger_id=:ranger_id";
            List<Integer> sightings_ids=con.createQuery(sql)
                    .addParameter("ranger_id",this.getId())
                    .executeAndFetch(Integer.class);
            List<Sightings> sightings=new ArrayList<Sightings>();

            for(Integer sighting_id:sightings_ids){
                String sightingsQuery="SELECT * FROM sightings WHERE id=:sighting_id";
                Sightings sighting=con.createQuery(sightingsQuery)
                        .addParameter("sighting_id",sighting_id)
                        .executeAndFetchFirst(Sightings.class);
                sightings.add(sighting);
            }
            if(sightings.size()==0){
                throw new IllegalArgumentException("Ranger has no sighting");
            }
            else {return sightings;}
        }
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rangers rangers = (Rangers) o;
        return id == rangers.id &&
                name.equals(rangers.name) &&
                badge_number.equals(rangers.badge_number) &&
                phone_number.equals(rangers.phone_number);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, badge_number, phone_number);
    }
}























