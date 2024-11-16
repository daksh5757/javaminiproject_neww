import org.sql2o.Connection;

public class EndangeredAnimals extends Animals implements DatabaseManagement {
    private String health;
    private String age;

    //constants age
    public static final String AGE_NEWBORN="newborn";
    public static final String AGE_YOUNG="young";
    public static final String AGE_ADULT="adult";

    //constants health
    public static final String HEALTH_HEALTHY="healthy";
    public static final String HEALTH_ILL="ill";
    public static final String HEALTH_OKAY="okay";

    //constant type
    public static final String ANIMAL_TYPE="endangered";
public EndangeredAnimals(String name, String type, String health, String age){
    super(name, type); // call superclass(Animals) methods, and to access the superclass constructor
    this.health=health;
    this.age=age;
    this.type = type;
}
//getter for age and health
    public String getHealth() {
        return health;
    }
    public String getAge() {
        return age;
    }
    //save method
    @Override
    public void save() {
        if(this.name.equals("")||this.type.equals("")||this.health.equals("")||this.age.equals("")){
            throw new IllegalArgumentException("Fields cannot be empty");
        }
        try (Connection con=DB.sql2o.open()){
            String sql ="INSERT INTO animals (name,type,health,age) VALUES (:name,:type,:health,:age)";
            this.id=(int) con.createQuery(sql,true)
                    .addParameter("name",this.name)
                    .addParameter("type",this.type)
                    .addParameter("health",this.health)
                    .addParameter("age",this.age)
                    .executeUpdate()
                    .getKey();
        }

    }
}
