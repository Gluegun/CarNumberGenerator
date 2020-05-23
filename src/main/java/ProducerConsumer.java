import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumer {

    private ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
    private AtomicInteger integer = new AtomicInteger(1);
    private AtomicInteger integerToFile = new AtomicInteger(1);

    public void produce() throws InterruptedException {

        while (true) {
            queue.put(generateNumber());
            if (integer.get() == 11) {
                break;
            }
        }
    }

    public void consume() throws InterruptedException {

        while (true) {

            Thread.sleep(1000);
            String take = queue.take();
            try (FileOutputStream writer = new FileOutputStream(new File("res/String"
                    + integerToFile.getAndIncrement() + ".txt"), true)) {

                writer.write((take + "\n").getBytes());

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("String taken. Queue size is " + queue.size());
            if (queue.size() == 0) {
                break;
            }
        }
    }

    private String generateNumber() {
        int regionCode = integer.getAndIncrement();

        char[] letters = {'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'};

        StringBuilder builder = new StringBuilder();

        for (int number = 1; number < 1000; number++) {
            for (char firstLetter : letters) {
                for (char secondLetter : letters) {
                    for (char thirdLetter : letters) {

                        builder.append(firstLetter)
                                .append(padNumber(number, 3))
                                .append(secondLetter)
                                .append(thirdLetter)
                                .append(padNumber(regionCode, 2))
                                .append("\n");
                    }
                }
            }
        }
        return builder.toString();
    }

    private static String padNumber(int number, int numberLength) { //выяснил, что String.format в 10 раз медленнее,
        // подключал сторонную библиотеку Apach common, там есть метод StringUtils.leftpad(), так же дольше
        String numberStr = Integer.toString(number);
        int padSize = numberLength - numberStr.length();

        for (int i = 0; i < padSize; i++) {
            numberStr = '0' + numberStr;
        }
        return numberStr;
    }

}
