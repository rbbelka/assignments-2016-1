package ru.spbau.mit;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence){
        return paths.stream().flatMap(p -> {
            try {
                return Files.lines(Paths.get(p));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }).filter(s -> s.contains(sequence)).collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random random = new Random();
        final double cntr = 0.5, rds = 0.5;
        final int cnt = 1000000;
        return Stream.generate(() -> Math.pow(random.nextDouble() - cntr, 2) +
                                     Math.pow(random.nextDouble() - cntr, 2)).limit(cnt).filter(r -> r <= Math.pow(rds, 2)).count() / (double)cnt;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        throw new UnsupportedOperationException();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        throw new UnsupportedOperationException();
    }
}
