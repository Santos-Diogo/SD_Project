package Server.ScalonatorServer;

import Protocol.Protocol;
import Server.Packet.Packet;
import Shared.LinkedBoundedBuffer;

import java.util.Collection;
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
    public LinkedBoundedBuffer<Packet> to_worker;
    public UserManager usermanager;

    private ReadWriteLock map_client_lock;
    private ReadWriteLock map_worker_lock;
    private int client_inc;
    private int worker_inc;
    private Map<Integer, LinkedBoundedBuffer<Protocol>> map_to_client;
    private Map<Integer, Integer> map_to_workerMem;

    public State ()
    {
        this.to_scalonator= new LinkedBoundedBuffer<>();
        this.to_worker= new LinkedBoundedBuffer<>();
        this.usermanager = new UserManager();
        this.map_client_lock= new ReentrantReadWriteLock();
        this.client_inc= 0;
        this.worker_inc = 0;
        this.map_to_client= new HashMap<>();
    }

    public int registerMap ()
    {
        try
        {
            LinkedBoundedBuffer<Protocol> to_client= new LinkedBoundedBuffer<>();
            map_client_lock.writeLock().lock();
            this.map_to_client.put(client_inc, to_client);
            return this.client_inc;
        }
        finally
        {
            this.client_inc++;
            this.map_client_lock.writeLock().unlock();
        }
    }

    public int registerMapWorker (int mem)
    {
        map_worker_lock.writeLock().lock();
        try
        {
            map_to_workerMem.put(worker_inc, mem);
            return this.worker_inc;
        } 
        finally 
        {
            this.worker_inc++;
            map_worker_lock.writeLock().unlock();
        }
    }

    public int getMemoryAvailable ()
    {
        map_worker_lock.readLock().lock();
        Collection<Integer> mems = map_to_workerMem.values();
        map_worker_lock.readLock().unlock();
        int sum = 0;
        for(Integer i : mems)
            sum += i;
        return sum;
    }

    public LinkedBoundedBuffer<Protocol> getMap (int submitter)
    {
        try
        {
            this.map_client_lock.readLock().lock();
            return this.map_to_client.get(submitter);
        }
        finally
        {
            this.map_client_lock.readLock().unlock();
        }
    }
}
