package Protocol;


public class Protocol {
    

    public enum Type {
        REG_RQ,
        REG_RP,
        LG_IN_RQ,
        LG_IN_RP,
        EXEC_RQ,
        EXEC_RP,
        STATUS_RQ,
        STATUS_RP
    }

    public Type type;

    public Protocol (Type type)
    {
        this.type = type;
    }

}
