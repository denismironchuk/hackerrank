package utils;

import java.util.Arrays;

public class Heap {
    private int[] elements;
    private int maxSize;
    private int lastElement = 0;

    public Heap(int maxSize) {
        this.maxSize = maxSize;
        elements = new int[this.maxSize + 1];
        Arrays.fill(elements, Integer.MIN_VALUE);
    }

    private void printHeap() {
        for (int i = 1; i <= lastElement; i++) {
            System.out.printf("%s ", elements[i]);
        }
        System.out.println();
    }

    public int getTop() {
        return elements[1];
    }

    public void add(int e) {
        lastElement++;
        elements[lastElement] = e;
        heapifyUp(lastElement);
    }

    public int getTopAndRemove() {
        int result = getTop();
        elements[1] = elements[lastElement];
        elements[lastElement] = Integer.MIN_VALUE;
        lastElement--;
        heapifyDown(1);
        return result;
    }

    private void heapifyDown(int position) {
        if (2 * position > lastElement) {
            return;
        }

        if (elements[position] < elements[position * 2] || elements[position] < elements[position * 2 + 1]) {
            if (elements[position * 2] >= elements[position * 2 + 1]) {
                int tmp = elements[position];
                elements[position] = elements[position * 2];
                elements[position * 2] = tmp;

                heapifyDown(position * 2);
            } else {
                int tmp = elements[position];
                elements[position] = elements[position * 2 + 1];
                elements[position * 2 + 1] = tmp;

                heapifyDown(position * 2 + 1);
            }
        }
    }

    private void heapifyUp(int position) {
        if (position == 1) {
            return;
        }

        if (elements[position] > elements[position / 2]) {
            int tmp = elements[position];
            elements[position] = elements[position / 2];
            elements[position / 2] = tmp;
            heapifyUp(position / 2);
        }
    }
}
