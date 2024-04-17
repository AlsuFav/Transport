package transport;


import com.fasterxml.jackson.databind.ObjectMapper;
import transport.model.TrasportDataBase;
import transport.model.Vehicle;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class MainTransport {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TrasportDataBase dataBase =
                mapper.readValue(new File("C:\\Users\\182\\IdeaProjects\\Transport\\src\\main\\java\\transport\\transport.json"), TrasportDataBase.class);

        // 1 Определить какой транспорт есть в базе данных
        Set<String> vehicleTypes = dataBase.getData().getVehicles().stream()
                .map(n -> n.getProperties().getVehicleMetaData().getTransport().getType())
                .collect(Collectors.toSet());
        System.out.println(vehicleTypes);

        // 2 Определить количество транспорта определенного маршрута на линии
        long number = dataBase.getData().getVehicles().stream()
                .filter(v -> v.getProperties().getVehicleMetaData().getTransport().getName().equals("47") &&
                       v.getProperties().getVehicleMetaData().getTransport().getType().equals("bus"))
                .count();
        System.out.println(number);

        //3. Создать Map<>, показывающий транспортное средство и количество сообщений о своих координатах

        dataBase.getData().getVehicles().stream().collect(Collectors.toMap(
                v -> v.getProperties().getVehicleMetaData().getId(),
                v -> v.getFeatures().stream().mapToInt(feature -> feature.getGeometry().getCoordinates().length).sum()
                )).forEach((key, value) -> System.out.println(key + ": " + value));

        //4. Вывести номер, тип, id транспорта, отсортировав в порядке возрастания номера маршрута
        dataBase.getData().getVehicles().stream()
                .sorted(Comparator.comparing(v -> v.getProperties().getVehicleMetaData().getTransport().getName()))
                .forEach(v -> System.out.println("номер: " + v.getProperties().getVehicleMetaData().getTransport().getName() +
                        ", тип: " + v.getProperties().getVehicleMetaData().getTransport().getType() +
                        ", id: " + v.getProperties().getVehicleMetaData().getTransport().getId()));

        //5. вывести для каждого транспортного средства массив из времени  (dd.MM.yyyy HH:mm:ss) (Properties)
        dataBase.getData().getVehicles().stream()
                .map(v -> v.getFeatures().stream()
                        .map(f -> f.getProperties().getTrajectorySegmentMetaData().getTime())
                        .map(time -> new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                                .format(Date.from(Instant.ofEpochSecond(time))))
                        .collect(Collectors.toList()))
                .forEach(System.out::println);

        //6. Вывести id только трамваев, автобусов, троллейбусов
        System.out.println("ID автобусов:");
        dataBase.getData().getVehicles().stream()
                .filter(v -> v.getProperties().getVehicleMetaData().getTransport().getType().equals("bus"))
                .forEach(v -> System.out.println(v.getProperties().getVehicleMetaData().getId()));

        System.out.println("ID троллейбусов:");
        dataBase.getData().getVehicles().stream()
                .filter(v -> v.getProperties().getVehicleMetaData().getTransport().getType().equals("trolleybus"))
                .forEach(v -> System.out.println(v.getProperties().getVehicleMetaData().getId()));

        System.out.println("ID трамваев:");
        dataBase.getData().getVehicles().stream()
                .filter(v -> v.getProperties().getVehicleMetaData().getTransport().getType().equals("tramway"))
                .forEach(v -> System.out.println(v.getProperties().getVehicleMetaData().getId()));
    }
}
