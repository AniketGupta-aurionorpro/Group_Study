
import java.util.*;

public class RotateList {
    public static void rotateRight(ArrayList<Integer> list, int k) {
        int n = list.size();
        if (n == 0) return;

        k = k % n;
        Collections.reverse(list);
        reverseSubList(list, 0, k - 1);
        reverseSubList(list, k, n - 1);
    }

    private static void reverseSubList(ArrayList<Integer> list, int start, int end) {
        while (start < end) {
            int temp = list.get(start);
            list.set(start, list.get(end));
            list.set(end, temp);
            start++;
            end--;
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50));
        rotateRight(list, 2);
        System.out.println(list);
    }
}
