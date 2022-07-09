package ru.otus.hw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcurrentIntPrinter {
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentIntPrinter.class);
    private int counter = 0;
    private boolean lastIsReader = true;
    private Direction direction = Direction.UP;

    private synchronized void action(boolean increment) {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                while (increment && !this.lastIsReader) {
                    this.wait();
                }
                while (!increment && this.lastIsReader) {
                    this.wait();
                }
                if (increment) {
                    if (direction.equals(Direction.UP)) {
                        this.counter++;
                    } else {
                        this.counter--;
                    }
                    if (this.counter == 10) {
                        direction = Direction.DOWN;
                    }
                    if (this.counter == 1) {
                        direction = Direction.UP;
                    }
                    this.lastIsReader = false;
                } else {
                    this.lastIsReader = true;
                }

                logger.info(Integer.toString(this.counter));
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        ConcurrentIntPrinter concurrentIntPrinter = new ConcurrentIntPrinter();
        new Thread(() -> concurrentIntPrinter.action(true)).start();
        new Thread(() -> concurrentIntPrinter.action(false)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private enum Direction {
        UP,
        DOWN
    }
}
