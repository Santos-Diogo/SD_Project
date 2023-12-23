package Protocol;

public class LoginReply extends Protocol{
    private boolean success;
    private String message;

    public LoginReply(boolean success, String message){
        super(Type.LG_IN_RP);
        this.success=success;
        this.message=message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
