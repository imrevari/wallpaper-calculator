import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Main {
    public static void main(String[] args) {
        String textFileContent = readFile();
        if (textFileContent.isEmpty()) {
            System.out.println("Empty file");
        }
        Calculation result = makeCalculations(textFileContent);

        System.out.printf("Total square feet of wallpaper to order is: %s%n", String.format("%,d", result.getTotalToOrder()) );
        printCubicRooms(result.getSideOfCubicRooms());
        System.out.println("Room sizes appearing more than once:");
        result.getRoomsMap().forEach( (key, value) -> { if (value > 1) System.out.println(key);});

    }

    static Calculation makeCalculations(String textFileContent){
        List<Integer> sideOfCubicRooms = new ArrayList<>();
        Map<String, Integer> roomsMap = new HashMap<>();
        String[] inputs = splitTextToArray(textFileContent);
        int totalToOrder = 0;
        for (String input : inputs) {
            String[] stringMeasures = input.split("x");
            if (stringMeasures.length == 3) {
                countOccurrenceOfValue(input, roomsMap);
                int sideA =  Integer.parseInt(stringMeasures[0]);
                int sideB =  Integer.parseInt(stringMeasures[1]);
                int sideC =  Integer.parseInt(stringMeasures[2]);
                totalToOrder += calculateOrderForRoom(sideA, sideB, sideC);
                if (sideA == sideB && sideA == sideC) {
                    sideOfCubicRooms.add(sideA);
                }
            }
        }
        return new Calculation(totalToOrder, sideOfCubicRooms, roomsMap);
    }

    static void countOccurrenceOfValue(String value, Map<String, Integer> valueMap){
        if (Objects.isNull(valueMap.get(value))){
            valueMap.put(value, 1);
        } else {
            Integer count = valueMap.get(value);
            valueMap.put(value, count + 1);
        }
    }

    static void printCubicRooms(List<Integer> cubicRooms){
        if(!cubicRooms.isEmpty()){
            System.out.println("Rooms with cubic shape:");
            cubicRooms.sort(Comparator.reverseOrder());
            for (Integer measurement : cubicRooms) {
                System.out.printf("%1$sx%1$sx%1$s%n", measurement);
            }
        } else {
            System.out.println("No rooms with cubic shape found.");
        }
    }

    static int calculateOrderForRoom(int sideA, int sideB, int sideC){
        int wallA = sideA * sideB;
        int wallB = sideB * sideC;
        int wallC = sideA * sideC;
        int totalArea = (wallA + wallB + wallC) * 2;
        return totalArea + Math.min(wallA, Math.min(wallB, wallC));
    }

    static String[] splitTextToArray(String text){
        return text.split("\n");
    }

    static String readFile(){
        try {
            return Files.readString(Path.of("rescources/sample-input.txt"));
        } catch (IOException e) {
            System.out.println("Error " + e.getLocalizedMessage());
        }
        return "";
    }

    public static class Calculation {
        int totalToOrder;
        List<Integer> sideOfCubicRooms;
        Map<String, Integer> roomsMap;

        public Calculation(int totalToOrder, List<Integer> sideOfCubicRooms, Map<String, Integer> roomsMap) {
            this.totalToOrder = totalToOrder;
            this.sideOfCubicRooms = sideOfCubicRooms;
            this.roomsMap = roomsMap;
        }

        public int getTotalToOrder() {
            return totalToOrder;
        }

        public List<Integer> getSideOfCubicRooms() {
            return sideOfCubicRooms;
        }

        public Map<String, Integer> getRoomsMap() {
            return roomsMap;
        }
    }
}