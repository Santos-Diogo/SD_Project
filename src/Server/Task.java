
package Server;

// Classe Task define a estrutura básica de uma tarefa no sistema.
public class Task 
{
    private int num;                // Número identificador da tarefa.
    private byte[] arg;            // Task's arguments

    public Task(int num, byte[] arg) 
    {
        this.num= num;
        this.arg= arg;
    }
}

