package api.utils;
import api.payload.User;
import com.github.javafaker.Faker;
import java.util.Random;

public class RandomGenerator {
    static Faker faker = new Faker();
    static Random random = new Random();
    public static void generateRandomVariables(){
        generateRandomName();
        generateRandomMail();
        generateRandomGender();
        generateRandomStatus();
    }
    public static void generateRandomMail(){
        Config.email= (faker.internet().emailAddress());
    }
    public static void generateRandomName(){
        Config.name= (faker.name().username());
    }
    public static void generateRandomGender(){
        // Randomly select a gender from the array
        String[] genders = {"Male", "Female"};
        int randomGenderIndex = random.nextInt(genders.length);
        Config.gender= (genders[randomGenderIndex]);
    }
    public static void generateRandomStatus(){
        // Randomly select a gender from the array
        String[] statusList = {"active","inactive"};
        int randomStatusIndex = random.nextInt(statusList.length);
        Config.status= (statusList[randomStatusIndex]);
    }
    public static User generateUser() {
        User randomUser = new User();
        randomUser.setName(Config.name);
        randomUser.setGender(Config.gender);
        randomUser.setEmail(Config.email);
        randomUser.setStatus(Config.status);
        return randomUser;
    }
}
