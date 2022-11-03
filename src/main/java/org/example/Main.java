package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final Map<Integer, Integer> sizeToFreq = new HashMap<>();

        Runnable logic = () -> {
            String text = generateRoute("RLRFR", 100);
            int count = calcCharCount(text);
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(count)) {
                    sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                } else {
                    sizeToFreq.put(count, 1);
                }
            }
        };

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        int max = findMax(sizeToFreq);

        System.out.println("Самое частое количество повторений " + max + " (встретилось " + sizeToFreq.get(max) + " раз)");
        System.out.println("Другие размеры:");
        for (int key : sizeToFreq.keySet()) {
            System.out.println("- " + key + " (" + sizeToFreq.get(key) + " раз)");
        }
    }

    public static int findMax(Map<Integer, Integer> sizeToFreq) {
        int max = 0;
        for (int key : sizeToFreq.keySet()) {
            if (max == 0) {
                max = key;
            } else {
                if (sizeToFreq.get(max) < sizeToFreq.get(key)) {
                    max = key;
                }
            }
        }
        return max;
    }

    public static int calcCharCount(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == 'R') {
                count++;
            }
        }
        return count;
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}