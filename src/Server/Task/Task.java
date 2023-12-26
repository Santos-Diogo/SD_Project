package Server.Task;

// Classe Task define a estrutura básica de uma tarefa no sistema.
public class Task 
{
    public int submitter;         // task submitter id
    public int num;               // task id 
    public byte[] arg;            // Task's arguments

    /**
     * Don't use this constructor to get a new task. Use the TaskMaker method instead
     * @param num
     * @param arg
     */
    public Task(int submitter, int num, byte[] arg) 
    {
        this.num= num;
        this.arg= arg;
    }
}

