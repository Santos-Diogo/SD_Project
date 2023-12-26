package Client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Handler
{
    State state;                // We handle commands and update the state
    DataInputStream in;
    DataOutputStream out;

    public Handler (State state, Socket socket) throws IOException
    {
        this.state= state;
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out
    }

    private byte[] getContent(String file) throws IOException
    {
        Path file_path = Path.of(file);
        return Files.readAllBytes(file_path);
    }


    //primeira versão; numa próxima provavelmente o melhor será ter isto numa thread para evitar casos em q ler o ficheiro demora demasiado tempo
    private void handleExec(String file)
    {
        try {
            byte[] job = getContent(file);

        }
    }



    void handle (String command)
    {
        switch (command.split("\\s+")[0])
        {
            case "exec":
            {
                handleExec(command.split("\\s+")[1]);
            }
            default:
                System.out.println("Command not supported");
        }
    }
}
