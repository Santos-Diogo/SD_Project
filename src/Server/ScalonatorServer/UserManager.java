package Server.ScalonatorServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserManager {
    
    Map<String, String> users_password;
    ReentrantReadWriteLock rwl;


    public UserManager ()
    {
        users_password = new HashMap<>();
        rwl = new ReentrantReadWriteLock();
    }

    public boolean existsUser (String username)
    {
        rwl.readLock().lock();
        try{
            return users_password.containsKey(username);
        } finally {
            rwl.readLock().unlock();
        }
    }

    public void addUser (String username, String password)
    {
        rwl.writeLock().lock();
        try{
            users_password.put(username, password);
        } finally {
            rwl.writeLock().unlock();
        }
    }

    public boolean checkPassword (String username, String password)
    {
        rwl.readLock().lock();
        try{
            return users_password.get(username).equals(password);
        } finally {
            rwl.readLock().unlock();
        }
    }    
}
