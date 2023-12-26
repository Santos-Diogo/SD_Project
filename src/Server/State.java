package Server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class State 
{
    public MemoryManager memoryManager;
    public BlockingQueue<Task> taskQueue;

    public State ()
    {
        this.memoryManager= new MemoryManager();
        this.taskQueue= new LinkedBlockingQueue<>();
    }
}
