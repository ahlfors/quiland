package feuyeux.algorithm.combination.mars;

import java.util.ArrayList;

public class CombinationTaster {

    public static void main(final String[] args) {
        final Combination comb = new RecursionCombination();
        //Combination comb = new NonRecursionCombination();
        final ArrayList<int[]> result = comb.get(new int[] { 1, 2, 3, 4, 5, 6, 7 });

        for (int i = 0; i < result.size(); i++) {
            final int[] combinationArray = result.get(i);
            for (int j = 0; j < combinationArray.length; j++) {
                System.out.print(combinationArray[j] + " ");
            }
            System.out.println();
        }
    }
}