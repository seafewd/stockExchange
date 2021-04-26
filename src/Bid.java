public class Bid {
	final public String name;
	final public int bid;

	public Bid(String name, int bid) {
		this.name = name;
		this.bid = bid;
	}

	public int hashCode() {
		return 1 + 23*bid + 31*name.hashCode();
	}

	public boolean equals(Object obj){
		if (obj == null || !(obj instanceof Bid)) return false;

		Bid bid = (Bid) obj;
		return this.name.equals(bid.name) && this.bid == ((Bid) obj).bid;
	}
	
	public String toString(){
		return this.name + ", " + this.bid;
	}
}

