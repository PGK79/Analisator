import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    final static int numberOfTexts = 10_000;
    final static int textLength = 100_000;

    public static BlockingQueue<String> searchQueueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> searchQueueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> searchQueueC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {

        new Thread(() -> { //наполняем очередь
            for (int i = 0; i < numberOfTexts; i++) {
                String text = generateText("abc", textLength);
                try {
                    searchQueueA.put(text);
                    searchQueueB.put(text);
                    searchQueueC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> { // ищем строку где, максимальное количество 'a'
            threadBody(searchQueueA, 'a');
        }).start();


        new Thread(() -> { // ищем строку где, максимальное количество 'b'
            threadBody(searchQueueB, 'b');
        }).start();


        new Thread(() -> { // ищем строку где, максимальное количество 'c'
            threadBody(searchQueueC, 'c');
        }).start();
    }

    public static void threadBody(BlockingQueue<String> searchQueueChar, char charName) {
        int counterMax = 0;
        String textResult = null;
        String text;

        for (int i = 0; i < numberOfTexts; i++) {
            try {
                text = searchQueueChar.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int counter = 0;
            for (int j = 0; j < text.length(); j++) {
                if (text.charAt(j) == charName) {
                    counter++;
                }
            }
            if (counter > counterMax) {
                textResult = text;
                counterMax = counter;
            }
        }
        System.out.println("\nТекст в котором содержится максимальное количество символов '" + charName + "'" + "\n" + textResult);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
