package Protocol;

public class LoginRequest extends Protocol {
    private String username;
    private String password;

    public LoginRequest(String user, String pass){
        super(Type.LG_IN_RQ);
        this.username=user;
        this.password=pass;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
