package Shared;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LinkedBoundedBuffer<E>{

    private Queue<E> queue;
    private Lock l;
    private Condition notEmpty;
    private Condition notFull;
    private int maximumCapacity;

    
    public LinkedBoundedBuffer ()
    {
        this.queue = new LinkedList<>();
        this.l = new ReentrantLock();
        this.notEmpty = l.newCondition();
        this.notFull = l.newCondition();
        this.maximumCapacity = 100;
    }

    public LinkedBoundedBuffer (int maximumCapacity)
    {
        this.queue = new LinkedList<>();
        this.l = new ReentrantLock();
        this.notEmpty = l.newCondition();
        this.notFull = l.newCondition();
        this.maximumCapacity = maximumCapacity;
    }

    public void put (E element) throws InterruptedException
    {
        l.lock();
        try {
            while (queue.size() >= maximumCapacity)
                notFull.await();
            queue.add(element);
            notEmpty.signal();
        } finally {
            l.unlock();
        }
    }

    public E take () throws InterruptedException
    {
        l.lock();
        try {
            while (queue.isEmpty())
                notEmpty.await();
            E element = queue.remove();
            notFull.signal();
            return element;
        } finally {
            l.unlock();
        }
    }
    
    public boolean isEmpty ()
    {
        l.lock();
        try {
            return queue.isEmpty();
        } finally {
            l.unlock();
        }
    }

    public int size ()
    {
        l.lock();
        try {
            return queue.size();
        } finally {
            l.unlock();
        }
    }

    public int drainTo(Collection<? super E> targetCollection) {
        l.lock();
        try {
            int elementsDrained = 0;
            while (!queue.isEmpty()) {
                targetCollection.add(queue.remove());
                elementsDrained++;
            }
            notFull.signalAll(); 
            return elementsDrained;
        } finally {
            l.unlock();
        }
    }
}
