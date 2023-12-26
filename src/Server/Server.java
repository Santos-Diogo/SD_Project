package Server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Protocol.Status.StatusREP;
import Protocol.Status.StatusREQ;

// Classe Server integra a classe MemoryManager e usa blockingqueue para o resolver o taskmanager para responder a requisições de status.
public class Server {

    private MemoryManager memoryManager;  // Instância de MemoryManager para gerir a memória.

    // Substituindo TaskManager por uma BlockingQueue
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    // Métodos para manipular a fila de tarefas
    public void addTask(Task task) throws InterruptedException {
        taskQueue.put(task);
    }

    public Task getNextTask() throws InterruptedException {
        return taskQueue.take();
    }

    public int getNumberOfPendingTasks() {
        return taskQueue.size();
    }

    // Método para tratar requisições de status. Retorna uma resposta com o estado atual do serviço.
    public StatusREP handleStatusRequest(StatusREQ request) {
        long availableMemory = memoryManager.getAvailableMemory();
        int pendingTasks = getNumberOfPendingTasks();
        return new StatusREP(availableMemory, pendingTasks);
    }
}