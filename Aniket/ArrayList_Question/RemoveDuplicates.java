
import java.util.*;

public class RemoveDuplicates {
    public static void removeDuplicates(ArrayList<Integer> list) {
        Set<Integer> seen = new HashSet<>();
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            Integer current = it.next();
            if (!seen.add(current)) {
                it.remove(); // remove if already in set
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 2, 3, 1, 4, 3));
        removeDuplicates(list);
        System.out.println(list);
    }
}
