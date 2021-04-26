import java.util.Comparator;

public class BidComparator implements Comparator<Bid> {

    @Override
    public int compare(Bid b1, Bid b2) {
        return Integer.compare(b1.bid, b2.bid);
    }
}
