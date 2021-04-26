
import java.io.*;
import java.util.*;

public class Lab2 {
	public static String pureMain(String[] commands) {
		// TODO: declaration of two priority queues

		PriorityQueue<Bid> sell_pq = new PriorityQueue<>(new BidComparator());
		PriorityQueue<Bid> buy_pq = new PriorityQueue<>(new BidComparator());

		StringBuilder sb = new StringBuilder();

		for(int line_no=0;line_no<commands.length;line_no++){
			String line = commands[line_no];
			if( line.equals("") )continue;

			String[] parts = line.split("\\s+");
			if( parts.length != 3 && parts.length != 4)
				throw new RuntimeException("line " + line_no + ": " + parts.length + " words");
			String name = parts[0];
			if( name.charAt(0) == '\0' )
				throw new RuntimeException("line " + line_no + ": invalid name");
			String action = parts[1];
			int price;
			try {
				price = Integer.parseInt(parts[2]);
			} catch(NumberFormatException e){
				throw new RuntimeException(
						"line " + line_no + ": invalid price");
			}

			Bid bid = new Bid(name, price);
			if( action.equals("K") ) {
				// TODO: add new buy bids.txt
				buy_pq.add(bid);
				//System.out.println("buy order placed by " + bid.name + " at " + bid.bid + "kr");
			} else if( action.equals("S") ) {
				// TODO: add new sell bids.txt
				sell_pq.add(bid);
				//System.out.println("sell order placed by " + bid.name + " at " + bid.bid + "kr");
			} else if( action.equals("NK") ){
				// TODO: update existing buy bids.txt. use parts[3].
				buy_pq.update(bid, new Bid(name, Integer.parseInt(parts[3])));
				//System.out.println(((Bid) oldVal).name + " changed bid from " + ((Bid) oldVal).bid + " to " + ((Bid) newVal).bid + "kr");
			} else if( action.equals("NS") ){
				// TODO: update existing sell bids.txt. use parts[3].
				sell_pq.update(bid, new Bid(name, Integer.parseInt(parts[3])));
			} else {
				throw new RuntimeException(
						"line " + line_no + ": invalid action");
			}

			if( sell_pq.size() == 0 || buy_pq.size() == 0 )continue;
			
			// TODO:
			// compare the bids of highest priority from each of
			// each priority queues.
			// if the lowest seller price is lower than or equal to
			// the highest buyer price, then remove one bids.txt from
			// each priority queue and add a description of the
			// transaction to the output.
			if (sell_pq.minimum().bid <= buy_pq.minimum().bid) {
				String buyerName = buy_pq.minimum().name;
				String sellerName = sell_pq.minimum().name;
				int sellValue = sell_pq.minimum().bid;
				buy_pq.deleteMinimum();
				sell_pq.deleteMinimum();
				System.out.println(buyerName + " buys from " + sellerName + " for " + sellValue + "kr");
			}
		}

		sb.append("\nOrder book:\n");

		sb.append("Sellers: ");

		while(sell_pq.size() > 0) {
			sb.append(sell_pq.minimum()).append(", ");
			sell_pq.deleteMinimum();
		}

		sb.append("\nBuyers: ");
		// TODO: print remaining buyers
		//       can remove from priority queue until it is empty.
		while(buy_pq.size() > 0) {
			sb.append(buy_pq.minimum() + ", ");
			buy_pq.deleteMinimum();
		}

		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		final BufferedReader actions;
		if( args.length != 1 ){
			actions = new BufferedReader(new InputStreamReader(System.in));
		} else {
			actions = new BufferedReader(new FileReader(args[0]));
		}

		List<String> lines = new LinkedList<String>();
		while(true){
			String line = actions.readLine();
			if( line == null)break;
			lines.add(line);
		}
		actions.close();

		System.out.println(pureMain(lines.toArray(new String[lines.size()])));
	}
}
