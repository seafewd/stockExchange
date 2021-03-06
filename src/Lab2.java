
import java.io.*;
import java.util.*;

public class Lab2 {
	public static String pureMain(String[] commands) {
		// two PQs - for sell and buy orders
		PriorityQueue<Bid> sell_pq = new PriorityQueue<>(new SellComparator());
		PriorityQueue<Bid> buy_pq = new PriorityQueue<>(new BuyComparator());

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
			// check action and put new trading orders
			switch (action) {
				// add new buy order
				case "K" -> buy_pq.add(bid);
				// add new sell order
				case "S" -> sell_pq.add(bid);
				// update existing buy order
				case "NK" -> buy_pq.update(bid, new Bid(name, Integer.parseInt(parts[3])));
				// update existing sell order
				case "NS" -> sell_pq.update(bid, new Bid(name, Integer.parseInt(parts[3])));
				default -> throw new RuntimeException(
						"line " + line_no + ": invalid action");
			}

			if( sell_pq.size() == 0 || buy_pq.size() == 0 )continue;
			
			// compare the bids of highest priority from each of
			// each priority queues.
			// if the lowest seller price is lower than or equal to
			// the highest buyer price, then remove one bids.txt from
			// each priority queue and add a description of the
			// transaction to the output.
			Bid minSellBid = sell_pq.minimum();
			Bid minBuyBid = buy_pq.minimum();

			if (minSellBid.bid <= minBuyBid.bid) {
				System.out.println(minBuyBid.name + " buys from " + minSellBid.name + " for " + minSellBid.bid + "kr");
				buy_pq.deleteMinimum();
				sell_pq.deleteMinimum();
			}
		}

		// print complete order book of remaining bids
		sb.append("\nOrder book:\n");

		// print remaining sellers in sorted order
		sb.append("Sellers: ");
		while(sell_pq.size() > 0) {
			String separator = ", ";
			if (sell_pq.size() == 1)
				separator = "";
			sb.append(sell_pq.minimum()).append(separator);
			sell_pq.deleteMinimum();
		}

		//print remaining buyers in sorted order
		sb.append("\nBuyers: ");
		while(buy_pq.size() > 0) {
			String separator = ", ";
			if (buy_pq.size() == 1)
				separator = "";
			sb.append(buy_pq.minimum()).append(separator);
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

		List<String> lines = new LinkedList<>();
		while(true){
			String line = actions.readLine();
			if( line == null)break;
			lines.add(line);
		}
		actions.close();

		System.out.println(pureMain(lines.toArray(new String[0])));
	}
}
