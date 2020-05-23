import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        if (!Files.exists(Paths.get("res/multi"))) {
            try {
                Files.createDirectories(Paths.get("res/multi"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ProducerConsumer pc = new ProducerConsumer();

        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        executorService.shutdown();


        Thread thread2 = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread2.start();
        thread2.join();

        System.out.println("Время записи: " + (System.currentTimeMillis() - start) + " мс");
    }

}
