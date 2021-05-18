import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class LevelChecker {

    public static boolean check(int[] level) {
        // We are on the exit - last springboard
        if (level.length == 1 && level[0] > 0) {
            return true;
        }
        // We landed on a mine
        if (level[0] == 0) {
            return false;
        }
        // Calculate max possible jump from current springboard
        int maxJump = java.lang.Math.min(level[0], level.length-1);
        // Try each possible jump length
        for (int i = 1; i <= maxJump; i++) {
            // Create level resultant from making the jump
            int[] A = Arrays.copyOfRange(level, i, level.length);
            if (check(A)) {
                return true;
            }
        }
        return false;
    }

    public static List<Integer> getJumps(int[] level) {
        // Set storing valid solutions
        Set<List<Integer>> solutions = new HashSet<List<Integer>>();
        getJumpsAux(level, new LinkedList<Integer>(), solutions);
        try {
            // Return the solution with the smallest number of jumps
            return Collections.min(solutions, Comparator.comparingInt(List::size));
        } catch (NoSuchElementException e) {
            // No solution possible
            return new ArrayList<Integer>();
        }
    }

    public static boolean getJumpsAux(int[] level, LinkedList<Integer> jumps, Set<List<Integer>> solutions) {
        // We are on the exit - last springboard
        if (level.length == 1 && level[0] > 0) {
            solutions.add(new ArrayList<Integer>(jumps));
            return true;
        }
        // We landed on a mine
        if (level[0] == 0) {
            return false;
        }
        // Calculate max possible jump from current springboard
        int maxJump = java.lang.Math.min(level[0], level.length-1);
        for (int i = 1; i <= maxJump; i++) {
            // Create level resultant from making the jump
            int[] A = Arrays.copyOfRange(level, i, level.length);
            jumps.addLast(i);
            getJumpsAux(A, jumps, solutions);
            jumps.removeLast();
        }
        return false;
    }

    public static boolean betterCheck(int[] level) {
        // Convert level from int[] to List, because we cannot index
        // the memoization map using arrays as keys
        List<Integer> l = new ArrayList<Integer>();
        for (int i : level) {
            l.add(i);
        }
        // Memoizes solutions for partial levels
        Map<List<Integer>, Boolean> memo = new HashMap<List<Integer>, Boolean>();
        return betterCheckMemoized(l, null, memo);
    }

    public static boolean betterCheckMemoized(List<Integer> level, List<Integer> previousLevel, Map<List<Integer>, Boolean> memo) {
        // We are on the exit - last springboard
        if (level.size() == 1 && level.get(0) > 0) {
            memo.put(previousLevel, true);
            return true;
        }
        // We landed on a mine
        if (level.get(0) == 0) {
            memo.put(previousLevel, false);
            return false;
        }
        // Calculate max possible jump from current springboard
        int maxJump = java.lang.Math.min(level.get(0), level.size()-1);
        for (int i = 1; i <= maxJump; i++) {
            // Create level resultant from making the jump
            List<Integer> A = level.subList(i, level.size());
            if (memo.containsKey(A)) {
                if (memo.get(A)) {
                    return true;
                }
            } else {
                if (betterCheckMemoized(A, level, memo)) {
                    return true;
                }
            }
        }
        memo.put(previousLevel, false);
        return false;
    }

}