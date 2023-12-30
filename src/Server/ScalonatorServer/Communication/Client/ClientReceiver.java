package Server.ScalonatorServer.Communication.Client;

import ThreadTools.ThreadControl;

import java.io.DataInputStream;
import java.io.IOException;

import Protocol.Protocol;
import Server.Packet.Packet;
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
                this.output.put(new Packet(Protocol.deserialize(input), my_num));
            }
            catch (InterruptedException e) {}
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }    
}
