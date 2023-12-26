package Server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import Protocol.Exec.Request;
import Protocol.Login.LoginReply;
import Protocol.Login.LoginRequest;
import Protocol.Registo.RegistoReply;
import Protocol.Registo.RegistoRequest;

public class Handler 
{
    private Map<String,String> registeredUsers = new HashMap<>();
    private ReentrantLock userLock = new ReentrantLock();

    public void handleExec (Request packet)
    {
        try 
        {
            // execute task and get output
            byte[] output = JobFunction.execute(packet.job);
        }
        catch (JobFunctionException e)
        {
            System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
            // send error to client (TODO)
        }
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

    // Implementação da verificação de credenciais
    public boolean credenciaisValid(String username, String password){
        return registeredUsers.containsKey(username) && registeredUsers.get(username).equals(password);
    }
}
