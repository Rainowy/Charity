package pl.coderslab.testuje;

import org.apache.logging.log4j.util.PropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {

        List<Person> females = getPeople().stream()
                .filter(person -> person.getGender().equals(Gender.FEMALE))
                .collect(Collectors.toList());

//        females
//        .stream()
//                .forEach(System.out::println);

        List<Person> byAge = getPeople().stream()
                .sorted(Comparator.comparing(Person::getAge).thenComparing(Person::getGender))
                .collect(Collectors.toList());

//        byAge.stream()
//                .forEach(System.out::println);

        boolean anyMatch = getPeople().stream()
                .anyMatch(person -> person.getAge() > 121);

        System.out.println(anyMatch);

        boolean antonio = getPeople().stream()
                .noneMatch(person -> person.getName().equals("Zbyszek"));

        System.out.println(antonio);

        System.out.println("WIek");

        Optional<Person> max = getPeople().stream()
                .max(Comparator.comparing(Person::getAge));

        System.out.println(max);

        getPeople().stream()
                .min(Comparator.comparing(Person::getAge))
                .ifPresent(person -> System.out.println(person));

        System.out.println("GROUPING");

        Map<Gender, List<Person>> grouped = getPeople().stream()
                .collect(Collectors.groupingBy(Person::getGender));

        grouped.forEach((gender, people) -> {
            System.out.println(gender);
            people.forEach(System.out::println);
            System.out.println();
        });

        System.out.println("OLDEST FEMALE");

        Optional<String> oldestFemale = getPeople().stream()
                .filter(person -> person.getGender().equals(Gender.FEMALE))
                .max(Comparator.comparing(Person::getAge))
                .map(Person::getName);

        oldestFemale.ifPresent(System.out::println);

        Map<Gender, List<Person>> genderListMap = getPeople().stream()
                .collect(Collectors.groupingBy(Person::getGender));

        genderListMap.forEach((gender, people) -> {
            System.out.println(gender);
            people.forEach(System.out::println);
            System.out.println();
        });

        getPeople().stream()
                .filter(person -> person.getGender().equals(Gender.MALE))
                .max(Comparator.comparing(Person::getAge))
                .map(Person::getName);


        Map<Gender, List<Person>> genderListMap1 = getPeople().stream()
                .collect(Collectors.groupingBy(Person::getGender));

        genderListMap1.forEach((gender, people) -> {
            System.out.println(gender);
            people.forEach(System.out::println);
            System.out.println();
        });

        List<String> withName = getPeople().stream()
                .map(person -> person.getName().toUpperCase())
                .filter(person -> person.startsWith("A"))
                .collect(Collectors.toList());

        withName.stream()
                .forEach(System.out::println);


        getPeople().stream()
                .forEach(person -> person.setAge(12));

        getPeople().stream()
                .forEach(System.out::println);

        System.out.println("STREAM Z PLIKU TXT");
        Stream<String> lines = Files.lines(Paths.get("/home/tomasz/workspace/Portfolio/CharityProject/src/main/java/pl/coderslab/testuje/data.txt"));

        int rowCount = (int) lines
                .map(x -> x.split(","))
                .filter(x -> x.length == 3)
                .count();
        System.out.println(rowCount + " rows");


        Stream<String> lines1 = Files.lines(Paths.get("/home/tomasz/workspace/Portfolio/CharityProject/src/main/java/pl/coderslab/testuje/data.txt"));

        lines1
                .map(x -> x.split(","))
                .filter(x -> x.length == 3)
                .filter(x -> Integer.parseInt(x[1]) > 15)
                .forEach(x -> System.out.println(x[0] + " " + x[1] + " " + x[2]));
        lines1.close();


        System.out.println("MAP");

        Stream<String> lines2 = Files.lines(Paths.get("/home/tomasz/workspace/Portfolio/CharityProject/src/main/java/pl/coderslab/testuje/data.txt"));

        Map<String, Integer> map = new HashMap<>();

        map = lines2
                .map(x -> x.split(","))
                .filter(x -> x.length == 3)
                .filter(x -> Integer.parseInt(x[1]) > 15)
                .collect(Collectors.toMap(
                        x -> x[0],
                        x -> Integer.parseInt(x[1])));
        lines2.close();

        for (String key : map.keySet()) {
            System.out.println(key + " " + map.get(key));



           for(Person p : getPeople()){
               System.out.println(p); }


           getPeople().stream()
                   .forEach(System.out::println);





        }
    }


    private static List<Person> getPeople() {
        return List.of(
                new Person("Antonio", 20, Gender.MALE),
                new Person("Alina Smith", 33, Gender.FEMALE),
                new Person("Helen White", 57, Gender.FEMALE),
                new Person("Alex Boz", 14, Gender.MALE),
                new Person("Jamie Goa", 99, Gender.MALE),
                new Person("Anna Cook", 7, Gender.FEMALE),
                new Person("Zelda Brown", 120, Gender.FEMALE)
        );
    }




}
