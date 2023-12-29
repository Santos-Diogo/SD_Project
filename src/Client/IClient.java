package Client;

import java.util.Optional;

public interface IClient {
    
    public Optional<String> register (String username, String password);

    public Optional<String> login(String username, String password);

    public void handle (String command);
}
