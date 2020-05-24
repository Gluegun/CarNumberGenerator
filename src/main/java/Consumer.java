import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer {

    private static AtomicInteger integerToFile = new AtomicInteger(1);

    public void consume(BlockingQueue<String> queue) throws InterruptedException {

        while (true) {

            Thread.sleep(1000);
            String take = queue.take();
            try (FileOutputStream writer = new FileOutputStream(new File("res/Number_"
                    + integerToFile.getAndIncrement() + ".txt"), true)) {

                writer.write((take + "\n").getBytes());

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Queue size is " + queue.size());
            if (queue.size() == 0) {
                break;
            }
        }
    }
}
