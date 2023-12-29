package Server.WorkerServer;

import Protocol.Exec.Response;
import Server.Packet.Task;
import Shared.LinkedBoundedBuffer;

class State
{
    LinkedBoundedBuffer<Task> manager_queue;
    LinkedBoundedBuffer<Task> worker_queue;
    LinkedBoundedBuffer<Response> outputQueue;
    long max_mem;

    State (long mem)
    {
        this.manager_queue= new LinkedBoundedBuffer<>();
        this.worker_queue= new LinkedBoundedBuffer<>();
        this.outputQueue= new LinkedBoundedBuffer<>();
        this.max_mem= mem;
    }
}
