import java.util.Comparator;

public class NameComparator implements Comparator<Bid> {

    @Override
    public int compare(Bid b1, Bid b2) {
        if (b1.name.compareTo(b2.name) < 0)
            return -1;
        else if (b1.name.compareTo(b2.name) > 0)
            return 1;
        else return 0;
    }
}
