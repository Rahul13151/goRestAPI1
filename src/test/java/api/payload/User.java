package api.payload;

//Using POJO (Plain Old Java Object) model to store and pass data for payload
public class User {
    private String name;
    private String gender;
    private String email;
    private String status;

    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() { return email; }
    public String getName(){ return name; }
    public String getGender() {
        return gender;
    }
    public String getStatus() {
        return status;
    }
}
