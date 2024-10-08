package Server;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.HashMap;
import java.util.Map;
import Protocol.Exec.Response;
import Server.MemoryManager.MemoryManager;
import Server.Task.Task;
import Shared.LinkedBoundedBuffer;


public class State 
{
    private ReentrantReadWriteLock mem_lock;                    // memory lock
    private ReentrantReadWriteLock task_res_lock;               // locks the taks results map
    private UserManager usermanager;                            // keeps the information about the users
    private Map<Integer, LinkedBoundedBuffer<Response>> task_results; // maps a submitter to his tasks results
    public LinkedBoundedBuffer<Task> taskQueue;                       // task queue

    public State ()
    {
        this.mem_lock= new ReentrantReadWriteLock();
        this.task_res_lock= new ReentrantReadWriteLock();
        this.usermanager = new UserManager();
        this.task_results= new HashMap<>();
        this.taskQueue= new LinkedBoundedBuffer<>();
    }

    /**
     * Adds the result of a task to the submitter's queue
     * @param submitter
     * @param p
     */
    public void addResult (int submitter, Response p)
    {
        try
        {
            this.task_res_lock.readLock().lock();
            this.task_results.get(submitter).put(p);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            this.task_res_lock.readLock().unlock();
        }
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

    /**
     * Registers a submitter and returns the submitter's response queue
     * @param submitter
     * @return submitter's response queue
     */
    public LinkedBoundedBuffer<Response> registerSubmitter (int submitter)
    {
        try
        {
            LinkedBoundedBuffer<Response> queue= new LinkedBoundedBuffer<>();
            this.task_res_lock.writeLock().lock();
            this.task_results.put(submitter, queue);
            return queue;
        }
        finally
        {
            this.task_res_lock.writeLock().unlock();
        }
    }

    public long getAvailableMemory ()
    {
        try
        {
            this.mem_lock.readLock().lock();
            return MemoryManager.getAvailableMemory();
        }
        finally
        {
            this.mem_lock.readLock().unlock();
        }
    }
}
