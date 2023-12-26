package Protocol.Registo;

import Protocol.Protocol;

public class RegistoRequest extends Protocol {
    public String username;
    public String password;

    public RegistoRequest(String username, String password){
        super(Type.REG_RQ);
        this.username = username;
        this.password =password;
    }

}
