
package Server;

// Classe Task define a estrutura básica de uma tarefa no sistema.
public class Task 
{
    public int num;                // Número identificador da tarefa.
    public byte[] arg;            // Task's arguments

    public Task(int num, byte[] arg) 
    {
        this.num= num;
        this.arg= arg;
    }
}

