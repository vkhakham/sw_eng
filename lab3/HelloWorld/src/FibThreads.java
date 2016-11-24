import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FibThreads {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int lineLength = scanner.nextInt();
        int arrLength = scanner.nextInt();
        BlockingQueue<Integer> workingQueue = new LinkedBlockingQueue<>(arrLength);
        new Thread(new Producer(workingQueue, n)).start();
        new Thread(new Consumer(workingQueue, n, lineLength)).start();
    }
}

class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int numElements;
    Producer(BlockingQueue<Integer> q, int n) { queue = q; numElements = n; }
    public void run() {
        try {
            for(int i = 1; i <= numElements; i ++) {
                queue.put(fib(i));
            }
        } catch (InterruptedException ignored) { }
    }
    int fib(int n) {
        if (n <= 2)
            return 1;
        return fib(n-1) + fib(n-2);
    }
}

class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int lineLength, numElements;
    Consumer(BlockingQueue<Integer> q, int n, int lineLength) { queue = q; this.lineLength = lineLength; numElements = n; }
    public void run() {
        for(int i = 1; i <= numElements; i++) {
            int val = 0;
            try {
                val = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(val + ((i % lineLength) !=0 ? " " : "\n" ));
        }
    }
}
