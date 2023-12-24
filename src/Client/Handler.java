package Client;

public class Handler
{
    State state;                // We handle commands and update the state

    public Handler (State state)
    {
        this.state= state;
    }

    void handle (String command)
    {
        switch (command)
        {
            default:
                System.out.println("Command not supported");
        }
    }
}
