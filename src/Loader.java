import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        writeToFiles(1, 100);
        System.out.println("На запись всех номеров для каждого региона в отдельный файл ушло в " +
                "однопоточной среде ушло: " + (System.currentTimeMillis() - start) + " ms");


        long startSingleFile = System.currentTimeMillis();

        writeToFile(100);
        System.out.println("На запись всех номеров для каждого региона в один файл ушло: " +
                (System.currentTimeMillis() - startSingleFile) + " ms");


        int[] regionsToShare = shareNumbersAmongThreads(Runtime.getRuntime().availableProcessors(), 100);
        List<Integer> regions = regions(regionsToShare);


        List<CarNumberToFileWriter> writers = new ArrayList<>();

        for (int i = 1; i < regions.size(); i++) {
            if (i == 1) {
                writers.add(new CarNumberToFileWriter(regions.get(i - 1), regions.get(i)));
            } else {
                writers.add(new CarNumberToFileWriter(regions.get(i - 1) + 1, regions.get(i)));
            }
        }

        long startMultiThreadingWriters = System.currentTimeMillis();
        for (CarNumberToFileWriter writer : writers) {
            writer.start();
            try {
                writer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("На запись всех номеров для каждого региона в отдельный файл ушло во многопоточном " +
                "режиме ушло: " + (System.currentTimeMillis() - startMultiThreadingWriters) + " ms");


    }

    private synchronized static void writeToFiles(int startedRegion, int endedRegion) {

        FileOutputStream writer = null;

        AtomicInteger integer = new AtomicInteger(1);

        char[] letters = {'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'};


        for (int regionCode = startedRegion; regionCode <= endedRegion; regionCode++) {
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

            try {

                writer = new FileOutputStream("res/numbers_" + integer.getAndIncrement() + ".txt");
                writer.write(builder.toString().getBytes());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized static void writeToFile(int regionsAmount) {

        FileOutputStream writer = null;

        try {
            writer = new FileOutputStream("res/numbers.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        AtomicInteger integer = new AtomicInteger(1);

        char[] letters = {'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'};

        for (int regionCode = 0; regionCode <= regionsAmount; regionCode++) {
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

            try {
                writer.write(builder.toString().getBytes());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String padNumber(int number, int numberLength) {
        String numberStr = Integer.toString(number);
        int padSize = numberLength - numberStr.length();

        for (int i = 0; i < padSize; i++) {
            numberStr = '0' + numberStr;
        }

        return numberStr; // вариант со строкой быстрее, чем с builder'ом

//        StringBuilder builder = new StringBuilder();
//
//        for (int i = 0; i < padSize; i++) {
//            builder.append('0');
//        }
//        builder.append(numberStr);
//
//        return builder.toString();
    }

    private static int[] shareNumbersAmongThreads(int threadAmount, int amountToShare) {

        int shares = amountToShare / threadAmount;

        int[] sharesArray = new int[threadAmount];

        Arrays.fill(sharesArray, shares);

        int arraySum = Arrays.stream(sharesArray).reduce(Integer::sum).getAsInt();
        int rest = amountToShare - arraySum;

        if (rest != 0) {
            for (int i = 0; i < rest; i++) {
                sharesArray[i]++;
            }
        }
        return sharesArray;
    }

    private static List<Integer> regions(int[] arr) {

        List<Integer> numbers = new ArrayList<>();
        numbers.add(0, 0);


        for (int i = 1; i < arr.length; i++) {

            numbers.add(numbers.get(i - 1) + arr[i]);
        }

        numbers.set(numbers.size() - 1, numbers.get(numbers.size() - 1) + arr[arr.length - 1] + 1);

        return numbers;
    }


}
