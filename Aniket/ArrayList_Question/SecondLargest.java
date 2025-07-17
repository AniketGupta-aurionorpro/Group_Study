
import java.util.*;

public class SecondLargest {
    public static Integer findSecondLargest(ArrayList<Integer> list) {
        Set<Integer> unique = new TreeSet<>(list);
        if (unique.size() < 2) return null;

        List<Integer> sorted = new ArrayList<>(unique);
        return sorted.get(sorted.size() - 2); // second last
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(3, 5, 2, 9, 5, 9));
        System.out.println(findSecondLargest(list)); // Output: 5
    }
}

