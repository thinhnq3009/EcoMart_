package eco.app.entity;

/**
 * This class supports customer ratings by spending level, and offers discounts
 * for each level
 *
 * @author ThinhNQ
 */
public class Rank {

    /**
     * Name of this rank
     */
    private final String nameRank;

    /**
     * Minimum amount spent to reach this rank
     */
    private final int minSpent;

    /**
     * Conversion rate to calculate coin will add to customer every complete
     * order
     */
    private final float convertRate;

    public Rank(String nameRank, int minSpent, float convertRate) {
        this.nameRank = nameRank;
        this.minSpent = minSpent;
        this.convertRate = convertRate;
    }

    public String getNameRank() {
        return nameRank;
    }

    public float getConvertRate() {
        return convertRate;
    }

    public int getMinSpent() {
        return minSpent;
    }

    @Override
    public String toString() {
        return this.nameRank;
    }

    public int getCoin(int totalBill) {
        return (int) (totalBill * convertRate);
    }

}
