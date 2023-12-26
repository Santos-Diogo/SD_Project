package Server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import Protocol.Status.StatusREP;
import Protocol.Status.StatusREQ;
import Protocol.Registo.RegistoRequest;
import Protocol.Registo.RegistoReply;
import Protocol.Login.LoginRequest;
import Protocol.Login.LoginReply;

// Classe Server integra a classe MemoryManager e usa blockingqueue para o resolver o taskmanager para responder a requisições de status.
public class Server {

    private MemoryManager memoryManager;  // Instância de MemoryManager para gerir a memória.
    private Map<String,String> registeredUsers = new HashMap<>();
    private ReentrantLock userLock = new ReentrantLock();

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


    public RegistoReply handleRegistoRequest(RegistoRequest request) {
        String username = request.username;
        String password = request.password;

        userLock.lock();
        try {
            // Verificação de utilizador único usando o lock
            if (!registeredUsers.containsKey(username)) {
                // Adiciona o utilizador
                registeredUsers.put(username, password);
                return new RegistoReply(true, "Registo bem-sucedido.");
            } else {
                return new RegistoReply(false, "Utilizador já registado.");
            }
        } finally {
            userLock.unlock();
        }
    }

    public LoginReply handleLoginReply(LoginRequest request){
        String username = request.username;
        String password = request.password;

        if (credenciaisValid(username,password)){
            return new LoginReply(true, "Login bem sucedido.");
        } else {
            return new LoginReply(false, "Credenciais invalidas.");
        }
    }

    // Implemente a verificação de credenciais
    public boolean credenciaisValid(String username, String password){
        return registeredUsers.containsKey(username) && registeredUsers.get(username).equals(password);
    }
}
