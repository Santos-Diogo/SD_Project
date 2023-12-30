package Server.WorkerServer;

import Protocol.Exec.Request;
import Protocol.Exec.Response;
import Server.Packet.Packet;
import Shared.LinkedBoundedBuffer;

class State
{
    class Output
    {
        int mem;
        Packet p;

        Output (int mem, Packet p)
        {
            this.mem= mem;
            this.p= p;
        }
    }

    LinkedBoundedBuffer<Packet> input_queue;
    LinkedBoundedBuffer<Output> output_queue;
    long max_mem;

    State (long mem)
    {
        this.input_queue= new LinkedBoundedBuffer<>();
        this.output_queue= new LinkedBoundedBuffer<>();
        this.max_mem= mem;
    }
}
