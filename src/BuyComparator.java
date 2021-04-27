import java.util.Comparator;

/**
 * Comparator used for buy orders
 */
public class BuyComparator implements Comparator<Bid> {

    @Override
    public int compare(Bid b1, Bid b2) {
        if (b1.bid == b2.bid)
            return 0;
        else
            return b1.bid < b2.bid ? 1 : -1;
    }
}
