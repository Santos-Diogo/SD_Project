package ThreadTools;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class we use to controll a group of threads
 */
public class ThreadControl
{
    private static boolean running;
    private final ReentrantReadWriteLock l = new ReentrantReadWriteLock();
    
    public ThreadControl ()
    {
        running= true;
    }

    public void setRunning (boolean b)
    {
        l.writeLock().lock();
        try
        {
            running= b;
        }
        finally
        {
            l.writeLock().unlock();
        }
    }

    public boolean getRunning ()
    {
        l.readLock().lock();
        try
        {
            return running;
        }
        finally
        {
            l.readLock().unlock();
        }
    }
}