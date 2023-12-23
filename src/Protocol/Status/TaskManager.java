

package Protocol.Status;

import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

// TaskManager gere a fila de tarefas pendentes, adicionando tarefas e obtendo as tarefas da fila.

public class TaskManager {
    private Queue<Task> taskQueue; // Fila para armazenar as tarefas. Usei Queue que encontrei na net e as suas funcionalidades já definidas.
    private ReentrantLock queueLock = new ReentrantLock(); 

    // Adiciona uma tarefa à fila.
    public void addTask(Task task) {
        queueLock.lock();
        try {
            taskQueue.offer(task);
        } finally {
            queueLock.unlock();
        }
    }

    // Retorna e remove a próxima tarefa da fila.
    public Task getNextTask() {
        queueLock.lock();
        try {
            return taskQueue.poll();
        } finally {
            queueLock.unlock();
        }
    }

    // Retorna o número de tarefas pendentes na fila. 
    public int getNumberOfPendingTasks() {
        queueLock.lock();
        try {
            return taskQueue.size();
        } finally {
            queueLock.unlock();
        }
    }
}

