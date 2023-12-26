package Server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class State 
{
    private ReentrantReadWriteLock mem_lock;                     // memory lock
    public BlockingQueue<Task> taskQueue;                        // task queue

    public State ()
    {
        this.taskQueue= new LinkedBlockingQueue<>();
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
