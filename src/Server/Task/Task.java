package Server.Task;

// Classe Task define a estrutura básica de uma tarefa no sistema.
public class Task 
{
    public int submitter;         // task submitter id
    public int num;               // task id
    public int mem;
    public byte[] arg;            // Task's arguments

    /**
     * Don't use this constructor to get a new task. Use the TaskMaker method instead
     * @param num
     * @param arg
     */
    public Task(int submitter, int num, int mem, byte[] arg) 
    {
        this.submitter = submitter;
        this.num= num;
        this.mem = mem;
        this.arg= arg;
    }
}

