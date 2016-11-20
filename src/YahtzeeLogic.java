/**
 * Created by Michael on 11/19/2016.
 */
public class YahtzeeLogic implements YahtzeeConstants {
    public static boolean checkCategory(int[] dice, int category) {
        switch(category) {
            case ONES:
            case TWOS:
            case THREES:
            case FOURS:
            case FIVES:
            case SIXES:
            case CHANCE:
                return true;
            case THREE_OF_A_KIND:
                return checkCount(dice, 3);
            case FOUR_OF_A_KIND:
                return checkCount(dice, 4);
            case FULL_HOUSE:
                return checkFullHouse(dice);
            case SMALL_STRAIGHT:
                return checkSmStraight(dice);
            case LARGE_STRAIGHT:
                return checkLgStraight(dice);
            case YAHTZEE:
                return checkCount(dice, 5);
            default:
                return false;
        }
    }

    private static boolean checkSmStraight(int[] dice) {
        boolean[] b = booleanPips(dice);
        return  (b[1] && b[2] && b[3] && b[4]) ||
                (b[2] && b[3] && b[4] && b[5]) ||
                (b[3] && b[4] && b[5] && b[6]) ;
    }

    private static boolean checkLgStraight(int[] dice) {
        boolean[] b = booleanPips(dice);
        return  (b[2] && b[3] && b[4] && b[5]) && (b[1] || b[6]);
    }

    private static boolean checkFullHouse(int[] dice) {
        int[] counts = countPips(dice);
        boolean threes = false;
        boolean twos = false;
        for(int i= 1; i <= 6; i++) {
            if(counts[i] == 3) threes = true;
            if(counts[i] == 2) twos = true;
        }
        return threes && twos;
    }

    private static boolean checkCount(int[] dice, int min) {
        int[] counts = countPips(dice);
        for(int i = 1; i <= 6; i++) {
            if(counts[i] >= min) return true;
        }
        return false;
    }

    private static int[] countPips(int[] dice) {
        int[] counts = new int[7];
        for(int i = 0; i < N_DICE; i++) {
            counts[dice[i]]++;
        }
        return counts;
    }

    private static boolean[] booleanPips(int[] dice) {
        boolean[] counts = new boolean[7];
        for(int i = 0; i < N_DICE; i++) {
            counts[dice[i]] = true;
        }
        return counts;
    }

}
