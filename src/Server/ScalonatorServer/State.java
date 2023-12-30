package Server.ScalonatorServer;

import Protocol.Protocol;
import Server.Packet.Packet;
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
    public LinkedBoundedBuffer<Packet> to_worker;
    private UserManager usermanager;

    private ReadWriteLock map_lock;
    private int client_inc;
    private Map<Integer, LinkedBoundedBuffer<Protocol>> map_to_client;

    public State ()
    {
        this.to_scalonator= new LinkedBoundedBuffer<>();
        this.to_worker= new LinkedBoundedBuffer<>();
        this.usermanager = new UserManager();
        this.map_lock= new ReentrantReadWriteLock();
        this.client_inc= 0;
        this.map_to_client= new HashMap<>();
    }

    public boolean existsUser (String username)
    {
        return usermanager.existsUser(username);
    }

    public void addUser (String username, String password)
    {
        usermanager.addUser(username, password);
    }

    public boolean checkPassword (String username, String password)
    {
        return usermanager.checkPassword(username, password);
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
