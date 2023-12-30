package Server.ScalonatorServer;

import Protocol.Protocol;
import Server.Packet.Packet;
import Shared.LinkedBoundedBuffer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class State 
{
    public class WorkerData
    {
        public int available_mem;
        public final int maxCapacity;
        public List<Integer> jobs;
        public LinkedBoundedBuffer<Packet> queue;

        WorkerData (int available_mem)
        {
            this.available_mem= available_mem;
            this.maxCapacity = available_mem;
            this.queue= new LinkedBoundedBuffer<>();
        }
    }

    public LinkedBoundedBuffer<Packet> to_scalonator;
    public UserManager usermanager;

    private ReadWriteLock map_client_lock;
    private ReadWriteLock map_worker_lock;
    private int client_inc;
    private int worker_inc;
    private Map<Integer, LinkedBoundedBuffer<Protocol>> map_to_client;
    private Map<Integer, WorkerData> map_to_worker;

    public State ()
    {
        this.to_scalonator= new LinkedBoundedBuffer<>();
        this.usermanager = new UserManager();
        this.map_client_lock= new ReentrantReadWriteLock();
        this.client_inc= 0;
        this.worker_inc = 0;
        this.map_to_client= new HashMap<>();
    }

    public int registerMapClient ()
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
        try
        {
            map_worker_lock.writeLock().lock();
            map_to_worker.put(worker_inc, new WorkerData(mem));
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
        Collection<WorkerData> mems = map_to_worker.values();
        map_worker_lock.readLock().unlock();
        int sum = 0;
        for(WorkerData wd : mems)
            sum += wd.available_mem;
        return sum;
    }

    public Set<Map.Entry<Integer, WorkerData>> getWorkersWithMem(int mem)
    {
        map_worker_lock.readLock().lock();
        Set<Map.Entry<Integer, WorkerData>> mems = map_to_worker.entrySet();
        map_worker_lock.readLock().unlock();
        mems.stream().filter((e) -> e.getValue().available_mem >= mem);
        return mems;
    }

    public void removeWorkerMem (int worker, int mem_taken)
    {
        map_worker_lock.writeLock().lock();
        WorkerData w = map_to_worker.get(worker);
        w.available_mem -= mem_taken;
        map_worker_lock.writeLock().unlock();
    }

    public LinkedBoundedBuffer<Protocol> getQueueClient (int submitter)
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

    public LinkedBoundedBuffer<Packet> getQueueWorker (int worker)
    {
        try
        {
            this.map_worker_lock.readLock().lock();
            return this.map_to_worker.get(worker).queue;
        }
        finally
        {
            this.map_worker_lock.readLock().unlock();
        }
    }
}
