package Server.ScalonatorServer;

import Server.Packet.Packet;
import ThreadTools.ThreadControl;

public class Scalonator {
    
    private State state;
    private ThreadControl tc;


    public Scalonator (ThreadControl tc, State state)
    {
        this.state = state;
        this.tc = tc;
    }


    private void handle (Packet packet) throws InterruptedException
    {
        switch (packet.protocol.type) {
            case REG_RQ:
                handleRegReq();
                break;
            case LG_IN_RQ:
                handleLogInReq();
                break;
            case EXEC_RQ:
                state.to_worker.put(packet);
                break;
            default:
                break;
        }
    }

    public void run()
    {
        while (tc.getRunning())
        {
            try {
                Packet packet = state.to_scalonator.take();
                handle(packet);
            } catch (InterruptedException e) {
                System.err.println("ERROR");
                e.printStackTrace();
            }
        }
    }


}


