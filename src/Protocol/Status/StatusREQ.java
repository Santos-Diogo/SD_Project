package Protocol.Status;

import Protocol.Protocol;


public class StatusREQ extends Protocol{                        // O servidor deve responder com a memória disponiveis e as tarefas. Isto só pede mesmo o status, não "dá" nada!
    public StatusREQ(){
        super(Type.STATUS_RQ);
    }
}
