package Server.ScalonatorServer;

import Protocol.Protocol;
import Server.Packet.Packet;
import Server.Packet.Task;
import Shared.LinkedBoundedBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class State 
{
    public class ClientInfo
    {
        public int client_num;
        public LinkedBoundedBuffer<Protocol> queue;

        public ClientInfo (int num, LinkedBoundedBuffer<Protocol> queue)
        {
            this.client_num= num;
            this.queue= queue;
        }
    }

    public LinkedBoundedBuffer<Packet> to_scalonator;
    public LinkedBoundedBuffer<Task> to_worker;


    private ReadWriteLock map_lock;
    private int client_inc;
    private Map<Integer, LinkedBoundedBuffer<Protocol>> map_to_client;

    public State ()
    {
        this.to_scalonator= new LinkedBoundedBuffer<>();
        this.to_worker= new LinkedBoundedBuffer<>();
        this.map_lock= new ReentrantReadWriteLock();
        this.client_inc= 0;
        this.map_to_client= new HashMap<>();
    }

    public ClientInfo registerMap ()
    {
        try
        {
            LinkedBoundedBuffer<Protocol> to_client= new LinkedBoundedBuffer<>();
            map_lock.writeLock().lock();
            this.map_to_client.put(client_inc, to_client);
            return new ClientInfo(client_inc, to_client);
        }
        finally
        {
            this.client_inc++;
            this.map_lock.writeLock().unlock();
        }
    }
}
