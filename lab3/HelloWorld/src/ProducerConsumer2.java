import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer2 {
    Queue<Integer> workingQueue = new LinkedList<Integer>();

    public synchronized void produce(int num) throws InterruptedException {
        while(workingQueue.size() >= 10){
            wait();
        }
        workingQueue.add(num);
        notifyAll();
    }

    public synchronized Integer consume() throws InterruptedException {
        while (workingQueue.isEmpty()) {
            wait();
        }
        Integer ret = workingQueue.poll();
        notifyAll();
        return ret;
    }
}
