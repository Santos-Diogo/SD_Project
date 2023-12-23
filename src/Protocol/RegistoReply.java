package Protocol;

public class RegistoReply extends Protocol{
    private boolean success;
    private String message;

    public RegistoReply (boolean success, String message){
        super(Type.REG_RP);
        this.success= success;
        this.message= message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
