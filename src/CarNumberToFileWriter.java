import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class CarNumberToFileWriter extends Thread {

    private int startedRegion;
    private int endedRegion;
    private static AtomicInteger integer = new AtomicInteger();

    public CarNumberToFileWriter(int startedRegion, int endedRegion) {
        this.startedRegion = startedRegion;
        this.endedRegion = endedRegion;
        integer.getAndIncrement();
    }


    @Override
    public void run() {
        writeToFiles(startedRegion, endedRegion);
    }




    private synchronized static void writeToFiles(int startedRegion, int endedRegion) {

        FileOutputStream writer = null;

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

                writer = new FileOutputStream("res/multi/numbers_" + regionCode + ".txt");
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

    @Override
    public String toString() {
        return "CarNumberToFileWriter{" +
                "startedRegion=" + startedRegion +
                ", endedRegion=" + endedRegion +
                '}';
    }
}
