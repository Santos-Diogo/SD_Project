// Classe qie trata da ocupação de serviço

package Server.MemoryManager;
import java.lang.management.ManagementFactory;  // Interface para a gestão de memória em JAVA 
import java.lang.management.MemoryMXBean;

public class MemoryManager
{
    // Retorna a quantidade de memória disponível na memória heap.
    public static long getAvailableMemory() 
    {
        // Recolhe os dados sobre a memóira
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        // Obtém a quantidade máxima de memória que pode ser usada pela JVM
        long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
        // Obtém a quantidade atual de memória usada
        long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
        return maxMemory - usedMemory;
    }
}

