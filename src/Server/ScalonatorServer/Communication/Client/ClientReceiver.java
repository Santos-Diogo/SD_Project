package Server.ScalonatorServer.Communication.Client;

import ThreadTools.ThreadControl;

import java.io.DataInputStream;
import java.io.IOException;

import Protocol.Protocol;
import Protocol.Authentication.LoginRequest;
import Protocol.Authentication.RegistoRequest;
import Protocol.Exec.Request;
import Protocol.Status.StatusREQ;
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

    public void deserializePackets (Protocol packet)
    {
        try {
            switch (packet.type) {
                case EXEC_RQ:
                {
                    Request request = Request.deserialize(input);
                    this.output.put(new Packet(request, my_num));
                    break;
                }
                case REG_RQ:
                {   
                    RegistoRequest request = RegistoRequest.deserialize(input);
                    this.output.put(new Packet(request, my_num));
                    break;
                }
                case LG_IN_RQ:
                {
                    LoginRequest request = LoginRequest.deserialize(input);
                    this.output.put(new Packet(request, my_num));
                    break;
                }
                case STATUS_RQ:
                {
                    this.output.put(new Packet(new StatusREQ(), my_num));
                    break;
                }
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                System.out.println("1");
                deserializePackets(Protocol.deserialize(input));
                System.out.println("2");
            }
            catch (IOException e) { break;}
        }
    }    
}
