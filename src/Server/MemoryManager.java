// Classe qie trata da ocupação de serviço

package Server;
import java.lang.management.ManagementFactory;  // Interface para a gestão de memória em JAVA 
import java.lang.management.MemoryMXBean;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryManager {
    private ReentrantLock memoryLock = new ReentrantLock();
    // Retorna a quantidade de memória disponível na memória heap.
    public long getAvailableMemory() {
        memoryLock.lock();
        try {
            // Recolhe os dados sobre a memóira
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            // Obtém a quantidade máxima de memória que pode ser usada pela JVM
            long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
            // Obtém a quantidade atual de memória usada
            long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
            return maxMemory - usedMemory;
        } finally {
            memoryLock.unlock();
        }
    }
}

