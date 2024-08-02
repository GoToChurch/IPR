package test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamApiExamples {
    private static final List<String> names = List.of("David", "AJ", "Donald", "Alexa", "Brandon", "Michelle");
    private static final List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 0, 1);

    public static void main(String[] args) {
        List<String> names2 = names.stream()
                .filter(name -> name.length() == 5) // ["David", "Alexa"]
                .map(name -> name + " Akkerman") // ["David Akkerman", "Alexa Akkerman"]
                .peek(System.out::println)
                .limit(1) // ["David Akkerman"]
                .collect(Collectors.toList());

        Boolean allMatch = names.stream()
                .allMatch(name -> name.endsWith("d")); // false

        Boolean anyMatch = names.stream()
                .anyMatch(name -> name.equals("Tagir")); // false

        Boolean noneMatch = names.stream()
                .noneMatch(String::isEmpty); // true

        Integer sum = names.stream()
                .takeWhile(name -> name.startsWith("B")) // ["David", "AJ", "Donald", "Alexa"]
                .skip(1) // ["AJ", "Donald", "Alexa"]
                .sorted(Comparator.reverseOrder()) // ["Donald", "Alexa", "AJ"]
                .flatMap(name -> numbers.stream()) // [1, 2, 3]
                .reduce(0, Integer::sum); // 6

        names.stream()
                .dropWhile(name -> name.contains("x")) // ["Alexa", "Brandon", "Michelle"]
                .forEach(System.out::println);

        String firstFounded = names.stream()
                .findFirst()
                .get(); // "David"

        String anyFounded = names.stream()
                .findAny()
                .get();

        Integer min = numbers.stream()
                .min(Comparator.naturalOrder())
                .get(); // 0

        Integer max = numbers.stream()
                .max(Comparator.naturalOrder())
                .get(); // 7

        Long count = numbers.stream()
                .distinct()
                .count(); // 8

        String joining = names.stream()
                .limit(2)
                .collect(Collectors.joining()); // "DavidAJ"





        @Data
        @AllArgsConstructor
        class Player {
            private String team;
            private Integer goalsCount;
            private Boolean isMainRosterPlayer;
        }



        List<Player> players = List.of(
                new Player("Chelsea", 10 , true),
                new Player("Arsenal", 1 , true),
                new Player("Manchester City", 4 , false),
                new Player("Chelsea", 0 , false),
                new Player("Arsenal", 14 , true),
                new Player("Manchester City", 21 , true),
                new Player("Chelsea", 7 , false)
        );

        Map<String, List<Player>> playersByTeam = players.stream()
                .collect(Collectors.groupingBy(Player::getTeam));

        Map<String, Integer> goalsByTeam = players.stream()
                .collect(Collectors.groupingBy(Player::getTeam, Collectors.summingInt(Player::getGoalsCount)));

        Map<Boolean, Optional<Player>> maxGoalsByMainRoster = players.stream()
                .collect(Collectors.groupingBy(Player::getIsMainRosterPlayer, Collectors.maxBy(Comparator.comparing(Player::getGoalsCount))));

        Map<Boolean, List<Player>> partitioningBy = players.stream()
                .collect(Collectors.partitioningBy(player -> player.isMainRosterPlayer.equals(true)));

        System.out.println(playersByTeam);
        System.out.println(goalsByTeam);
        System.out.println(maxGoalsByMainRoster);
        System.out.println(partitioningBy);
    }

}
