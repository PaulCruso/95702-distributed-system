
import java.util.*;

class QueueWithWaitAndNotify {

    // A linked list is used to hold the queue.
    LinkedList q = new LinkedList();

    // Add an element to the end of the line
    public synchronized void addAtEnd(Object o) {
        q.addLast(o);
    }

    // Remove the first in line
    public synchronized Object removeFromFront() {

        return q.removeFirst();
    }
}

public class WaitAndNotify {

    int n = 100;

    QueueWithWaitAndNotify myQueue = new QueueWithWaitAndNotify();

    // add at end of queue 1,2,...,n
    Thread t1 = new Thread( new Runnable() {
        public void run() {
            for(int j = 1; j <= n-1; j++) {
                myQueue.addAtEnd(j);
            }
            try{ java.lang.Thread.sleep(5000); } catch(Exception e) {}
            myQueue.addAtEnd(n);

        }
    });

    // remove n elements from the queue
    Thread t2 = new Thread( new Runnable() {
        public void run() {
            for(int j = 1; j <= n; j++) {
                System.out.println(myQueue.removeFromFront());
            }
        }
    });

    public void playWithQueue()
    {
        t1.start();
        t2.start();
        // Wait for both threads to complete.


    }

    public static void main(String args[]) {
        WaitAndNotify w = new WaitAndNotify();
        w.playWithQueue();
        System.out.println("The wait and notify demonstration is complete");
    }
}

