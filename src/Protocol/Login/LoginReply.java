package Protocol.Login;

import Protocol.Protocol;

public class LoginReply extends Protocol {
    public boolean success;
    public String message;

    public LoginReply(boolean success, String message){
        super(Type.LG_IN_RP);
        this.success=success;
        this.message=message;
    }

}
