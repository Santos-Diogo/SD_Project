package Protocol.Status;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Protocol;

// StatusREP é usada para enviar a resposta do servidor ao cliente.
// Contém informações sobre a memória disponível e o número de tarefas pendentes.
public class StatusREP extends Protocol {
    public long available_memory; // Memória disponível no momento.
    public int number_of_tasks;   // Número de tarefas pendentes na fila.

    public StatusREP(long available_memory, Integer number_of_tasks) {
        super(Type.STATUS_RP);
        this.available_memory = available_memory;
        this.number_of_tasks = number_of_tasks;
    }

    @Override
    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeLong(available_memory);
        out.writeInt(number_of_tasks);
        out.flush();
    }

    public static StatusREP deserialize (DataInputStream in) throws IOException
    {
        Protocol.deserialize(in);
        long available_memory = in.readLong();
        int number_of_tasks = in.readInt();
        return new StatusREP(available_memory, number_of_tasks);
    }
}

