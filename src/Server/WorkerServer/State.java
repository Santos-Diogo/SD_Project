package Server.WorkerServer;

import Protocol.Exec.Response;
import Server.Packet.Task;
import Shared.LinkedBoundedBuffer;

class State
{
    LinkedBoundedBuffer<Task> input_queue;
    LinkedBoundedBuffer<Response> output_queue;
    long max_mem;

    State (long mem)
    {
        this.input_queue= new LinkedBoundedBuffer<>();
        this.output_queue= new LinkedBoundedBuffer<>();
        this.max_mem= mem;
    }
}
