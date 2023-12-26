package Protocol;

public class LoginRequest extends Protocol {
    public String username;
    public String password;

    public LoginRequest(String username, String password){
        super(Type.LG_IN_RQ);
        this.username=username;
        this.password=password;
    }

}
