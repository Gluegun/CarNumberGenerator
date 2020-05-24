import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loader {

    static {
        if (!Files.exists(Paths.get("res"))) {
            try {
                Files.createDirectories(Paths.get("res"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        int queueLimit = 10;
        int amountOfRegions = 10;
        int amountOfThreads = Runtime.getRuntime().availableProcessors();

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(queueLimit);

        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(amountOfThreads);
        executorService.submit(new Thread(() -> {
            try {
                producer.produce(queue, amountOfRegions);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        executorService.shutdown();


        Thread consumerThread = new Thread(() -> {
            try {
                consumer.consume(queue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        consumerThread.start();
        consumerThread.join();

        System.out.println("Время записи: " + (System.currentTimeMillis() - start) + " мс");
    }

}
