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

    public int registerMap()
    {
        try
        {
            LinkedBoundedBuffer<Protocol> to_client= new LinkedBoundedBuffer<>();
            map_lock.writeLock().lock();
            this.map_to_client.put(client_inc, to_client);
            return this.client_inc;
        }
        finally
        {
            this.client_inc++;
            this.map_lock.writeLock().unlock();
        }
    }

    public LinkedBoundedBuffer<Protocol> getMap (int submitter)
    {
        try
        {
            this.map_lock.readLock().lock();
            return this.map_to_client.get(submitter);
        }
        finally
        {
            this.map_lock.readLock().unlock();
        }
    }
}
