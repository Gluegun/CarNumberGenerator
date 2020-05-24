import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer {

    private static AtomicInteger integer = new AtomicInteger(1);

    public void produce(BlockingQueue<String> queue, int amount) throws InterruptedException {

        while (true) {
            for (int i = 0; i < amount; i++) {
                queue.put(generateNumber());
            }
            if (integer.get() == amount + 1) {
                break;
            }
        }
    }

    private static String generateNumber() {
        int regionCode = integer.getAndIncrement();

        char[] letters = {'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'};

        StringBuilder builder = new StringBuilder();

        String zeroToNumber;
        String zeroToRegion;

        for (int number = 1; number < 1000; number++) {
            for (char firstLetter : letters) {
                for (char secondLetter : letters) {
                    for (char thirdLetter : letters) {

                        builder.append(firstLetter);

                        if (number < 10) {
                            zeroToNumber = "00";
                            builder.append(zeroToNumber);
                        }
                        if (number >= 10 && number < 100) {
                            zeroToNumber = "0";
                            builder.append(zeroToNumber);
                        }

                        builder.append(number)
                                .append(secondLetter)
                                .append(thirdLetter);

                        if (regionCode < 10) {
                            zeroToRegion = "0";
                            builder.append(zeroToRegion);
                        }
                        builder.append(regionCode).append("\n");
                    }
                }
            }
        }
        return builder.toString();
    }
}
