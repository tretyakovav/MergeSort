package org.tretyakov;
import java.io.*;
import java.util.*;

public class FileMergeSort {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java FileMergeSort <-a | -d> <-i | -s> <output_file> <input_file1> <input_file2> ...");
            return;
        }

        boolean ascending = true; // По умолчанию в порядке возрастания
        boolean isString = false; // По умолчанию используется целочисленный тип данных.

        // Разбор аргументов командной строки
        int index = 0;
        if (args[index].equals("-d")) {
            ascending = false;
            index++;
        } else if (args[index].equals("-a")) {
            index++;
        }

        if (args[index].equals("-s")) {
            isString = true;
            index++;
        } else if (args[index].equals("-i")) {
            index++;
        }

        String outputFileName = args[index++];
        String[] inputFiles = Arrays.copyOfRange(args, index, args.length);

        try {
            List<BufferedReader> readers = new ArrayList<>();
            for (String inputFile : inputFiles) {
                File file = new File(inputFile);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                readers.add(reader);
            }

            // Объединить данные из входных файлов в один отсортированный список
            List<String> mergedData = new ArrayList<>();
            for (BufferedReader reader : readers) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue; // Пропускать пустые строки
                    }
                    mergedData.add(line);
                }
                reader.close();
            }

            // Сортировка mergedData с использованием сортировки слиянием
            if (isString) {
                mergeSortStrings(mergedData, ascending);
            } else {
                mergeSortIntegers(mergedData, ascending);
            }

            // Запишите отсортированные данные в выходной файл
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
            for (String data : mergedData) {
                writer.write(data);
                writer.newLine();
            }
            writer.close();

            System.out.println("Сортировка успешно завершена!");
        } catch (IOException e) {
            System.err.println("Произошла ошибка при сортировке файлов: " + e.getMessage());
        }
    }

    // Реализация сортировки слиянием для строк
    private static void mergeSortStrings(List<String> arr, boolean ascending) {
        if (arr.size() <= 1) {
            return;
        }

        int mid = arr.size() / 2;
        List<String> left = new ArrayList<>(arr.subList(0, mid));
        List<String> right = new ArrayList<>(arr.subList(mid, arr.size()));

        mergeSortStrings(left, ascending);
        mergeSortStrings(right, ascending);

        mergeStrings(arr, left, right, ascending);
    }

    private static void mergeStrings(List<String> arr, List<String> left, List<String> right, boolean ascending) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if ((ascending && left.get(i).compareTo(right.get(j)) <= 0) ||
                    (!ascending && left.get(i).compareTo(right.get(j)) >= 0)) {
                arr.set(k++, left.get(i++));
            } else {
                arr.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            arr.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            arr.set(k++, right.get(j++));
        }
    }

    // Реализация сортировки слиянием для целых чисел
    private static void mergeSortIntegers(List<String> arr, boolean ascending) {
        if (arr.size() <= 1) {
            return;
        }

        int mid = arr.size() / 2;
        List<String> left = new ArrayList<>(arr.subList(0, mid));
        List<String> right = new ArrayList<>(arr.subList(mid, arr.size()));

        mergeSortIntegers(left, ascending);
        mergeSortIntegers(right, ascending);

        mergeIntegers(arr, left, right, ascending);
    }

    private static void mergeIntegers(List<String> arr, List<String> left, List<String> right, boolean ascending) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            int leftValue = Integer.parseInt(left.get(i));
            int rightValue = Integer.parseInt(right.get(j));

            if ((ascending && leftValue <= rightValue) || (!ascending && leftValue >= rightValue)) {
                arr.set(k++, left.get(i++));
            } else {
                arr.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            arr.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            arr.set(k++, right.get(j++));
        }
    }
}
