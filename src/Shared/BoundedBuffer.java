package Shared;

import java.util.Collection;
import java.util.Queue;

public interface BoundedBuffer<E> extends Queue<E>
{

    void put(E var1) throws InterruptedException;

    E take() throws InterruptedException;

    int remainingCapacity();

    int drainTo(Collection<? super E> var1);

    int drainTo(Collection<? super E> var1, int var2);   
}
