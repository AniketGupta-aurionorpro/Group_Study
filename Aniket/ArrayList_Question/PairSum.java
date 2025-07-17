
import java.util.*;

public class PairSum {
    public static List<List<Integer>> findPairs(ArrayList<Integer> list, int target) {
        Set<Integer> seen = new HashSet<>();
        Set<String> addedPairs = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();

        for (int num : list) {
            int complement = target - num;
            if (seen.contains(complement)) {
                int min = Math.min(num, complement);
                int max = Math.max(num, complement);
                String pairKey = min + "," + max;
                if (!addedPairs.contains(pairKey)) {
                    result.add(Arrays.asList(min, max));
                    addedPairs.add(pairKey);
                }
            }
            seen.add(num);
        }
        return result;
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 3, 2, 2, 4, 5));
        System.out.println(findPairs(list, 5)); 
    }
}

