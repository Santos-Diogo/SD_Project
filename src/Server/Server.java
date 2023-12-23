package Server;

import Protocol.Status.MemoryManager;
import Protocol.Status.StatusREP;
import Protocol.Status.StatusREQ;
import Protocol.Status.TaskManager;

// Classe Server integra TaskManager e MemoryManager para responder a requisições de status.
public class Server {
    private TaskManager taskManager;      // Instância de TaskManager para gerir as tarefas.
    private MemoryManager memoryManager;  // Instância de MemoryManager para gerir a memória.

    // Método para tratar requisições de status. Retorna uma resposta com o estado atual do serviço.
    public StatusREP handleStatusRequest(StatusREQ request) {
        int pendingTasks = taskManager.getNumberOfPendingTasks();
        long availableMemory = memoryManager.getAvailableMemory();
        return new StatusREP(availableMemory, pendingTasks);
    }
}
