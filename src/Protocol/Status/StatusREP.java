package Protocol.Status;

import Protocol.Protocol;

// StatusREP é usada para enviar a resposta do servidor ao cliente.
// Contém informações sobre a memória disponível e o número de tarefas pendentes.
public class StatusREP extends Protocol {
    public long available_memory; // Memória disponível no momento.
    public int number_of_tasks;   // Número de tarefas pendentes na fila.

    public StatusREP(long available_memory, int number_of_tasks) {
        super(Type.STATUS_RP);
        this.available_memory = available_memory;
        this.number_of_tasks = number_of_tasks;
    }
}

