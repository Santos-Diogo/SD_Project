package Server.ScalonatorServer;

import Protocol.Protocol;
import Server.Packet.Packet;
import Server.Packet.Task;
import Shared.LinkedBoundedBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class State 
{
    class ClientInfo ()
    {
        int client_num;
        LinkedBoundedBuffer<Protocol> queue;


    }

    public LinkedBoundedBuffer<Packet> to_scalonator;
    public LinkedBoundedBuffer<Task> to_worker;


    private ReadWriteLock map_lock;
    private int client_inc;
    private Map<Integer, LinkedBoundedBuffer<Protocol>> map_to_client;

    State ()
    {
        this.to_scalonator= new LinkedBoundedBuffer<>();
        this.to_worker= new LinkedBoundedBuffer<>();
        this.map_lock= new ReentrantReadWriteLock();
        this.client_inc= 0;
        this.map_to_client= new HashMap<>();
    }


    /**
     * "registers the "
     * @return
     */
    LinkedBoundedBuffer<Protocol> registerMap ()
    {
        try
        {
            map_lock.writeLock().lock();

            LinkedBoundedBuffer<Protocol> to_client= new LinkedBoundedBuffer<>();
            this.map_to_client.put(client_inc++, to_client);
            return to_client;
        }
        finally
        {
            this.map_lock.writeLock().unlock();
        }
    }
}
