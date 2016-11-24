import java.util.Calendar;

public class SumThreadsMain {
    public static void main(String[] args) {

        Calendar cal = Calendar.getInstance();
        int second1, second2;
        second1 = cal.get(Calendar.SECOND);
        int sumNormal = sum();
        second2 = cal.get(Calendar.SECOND);
        System.out.println("normal sum: " + sumNormal + " time it took in sec: " + (second2 - second1));

        second1 = cal.get(Calendar.SECOND);
        Thread[] threads = new Thread[10]; // create an array of threads
        int[] arr = new int[10];
        for(int i = 1; i <= 10000; i+=1000) {
            String threadName = Integer.toString(i);
            threads[i/1000]=new Thread(new SumThreads(i, i+999, arr, i/1000),threadName);
            // create threads
        }
        for (Thread thread : threads) {
            thread.start(); // start the threads
        }
        for (Thread thread : threads) {
            try {
                thread.join(); // wait for the threads to terminate

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        int sum_threads = 0;
        for (int i = 0 ; i < 10; i++)
            sum_threads += arr[i];
        second2 = cal.get(Calendar.SECOND);
        System.out.println("threaded sum: " + sum_threads + " time it took in sec: " + (second2 - second1));
    }

    static int sum(){
        int sum = 0;
        for (int i = 1; i <= 10000; i++) {
            sum+=i;
        }
        return sum;
    }
}

class SumThreads implements Runnable {
    int startIndex, endIndex, index;
    int[] arr;
    SumThreads(int startIndex, int endIndex, int[] arr, int index){
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.index = index;
        this.arr = arr;
    }
    public void run() {
        int sum = 0;
        for (int i = startIndex; i<= endIndex; i++)
            sum +=i;
        arr[index] = sum;
    }
}