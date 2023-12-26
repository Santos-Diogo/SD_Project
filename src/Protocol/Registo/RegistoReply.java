package Protocol.Registo;

import Protocol.Protocol;

public class RegistoReply extends Protocol {
    public boolean success;
    public String message;

    public RegistoReply (boolean success, String message){
        super(Type.REG_RP);
        this.success= success;
        this.message= message;
    }
}
