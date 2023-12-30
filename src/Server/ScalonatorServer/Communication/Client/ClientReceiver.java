package Server.ScalonatorServer.Communication.Client;

import ThreadTools.ThreadControl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import Protocol.Protocol;
import Protocol.Exec.Request;
import Server.Packet.Packet;
import Server.ScalonatorServer.State;
import Shared.LinkedBoundedBuffer;

class ClientReceiver implements Runnable
{
    private ThreadControl tc;
    private DataInputStream input;
    private LinkedBoundedBuffer<Packet> output;
    private int my_num;

    ClientReceiver (ThreadControl tc, DataInputStream input, LinkedBoundedBuffer<Packet> output , int my_num)
    {
        this.tc= tc;
        this.input= input;
        this.output= output;
        this.my_num= my_num;
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                // !!! TODO
                hfkjsdfhks hdkjf kdjshfkj hdsf ;
                this.state.to_scalonator.put(Protocol.deserialize(input));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }    
}
