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
                sizeToFreq.notify();
            }
        };

        Runnable logic2 = () -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                    int max = findMax(sizeToFreq);
                    System.out.println("Самое частое количество повторений " + max + " (встретилось " + sizeToFreq.get(max) + " раз)");
                }
            }
        };

        Thread thread2 = new Thread(logic2);
        thread2.start();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        thread2.interrupt();
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