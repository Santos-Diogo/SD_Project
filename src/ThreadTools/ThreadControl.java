package ThreadTools;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * Class we use to controll a group of threads
 */
public class ThreadControl
{
    private boolean running;
    private ReentrantReadWriteLock rwl;
    private Lock condition_lock; 
    private Condition c= condition_lock.newCondition ();
    
    public ThreadControl ()
    {
        running= true;
        this.rwl= new ReentrantReadWriteLock();
        this.condition_lock= new ReentrantLock();
        this.c= this.condition_lock.newCondition();
    }

    public void setRunning (boolean b)
    {
        try
        {
            rwl.writeLock().lock();
            condition_lock.lock();
            running= b;
            //signal status change in running
            c.signalAll();
        }
        finally
        {
            rwl.writeLock().unlock();
            condition_lock.unlock();
        }
    }

    public boolean getRunning ()
    {
        try
        {
            rwl.readLock().lock();
            return running;
        }
        finally
        {
            rwl.readLock().unlock();
        }
    }

    /**
     * Used to wait for termination
     */
    public void waitTerm ()
    {
        try
        {
            this.condition_lock.lock();
            while (this.running)
            {
                try
                {
                    this.c.await();
                }
                catch (InterruptedException e){}
            }
        }
        finally
        {
            this.condition_lock.unlock();
        }
    }
}