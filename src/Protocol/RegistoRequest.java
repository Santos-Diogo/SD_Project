package Protocol;

public class RegistoRequest extends Protocol{
    private String username;
    private String password;

    public RegistoRequest(String username, String password){
        super(Type.REG_RQ);
        this.username = username;
        this.password =password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
