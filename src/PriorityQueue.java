import java.util.*;

// A priority queue.
public class PriorityQueue<E> {
	private ArrayList<E> heap = new ArrayList<E>();
	private Comparator<E> comparator;
	private Map<E, Integer> bidMap = new HashMap<>();

	public PriorityQueue(Comparator<E> comparator) {
		this.comparator = comparator;
	}

	// Returns the size of the priority queue.
	public int size() {
		return heap.size();
	}
     
	// Adds an item to the priority queue.
	public void add(E x) {
		heap.add(x);
		bidMap.put(x, heap.size() - 1);
		//System.out.println("added\n" + x.toString() + "\nto map\n");
		siftUp(size() - 1);
	}

	// Returns the smallest item in the priority queue.
	// Throws NoSuchElementException if empty.
	public E minimum() {
		if (size() == 0)
			throw new NoSuchElementException();
		return heap.get(0);
	}

	// update a bid
	public void update(E oldVal, E newVal) {
		// find array index of the old bid - O(1)
		Integer originalBidIndex = bidMap.get(oldVal);
		if (originalBidIndex == null)
			throw new NoSuchElementException();
		// replace the old bid with the new bid at that index
		heap.set(originalBidIndex, newVal);
		// update map with correct bid
		bidMap.replace(newVal, originalBidIndex);
		//if (((Bid) heap.get(parent(originalBidIndex))).bid > ((Bid) heap.get(originalBidIndex)).bid)
		// sift up if the new bid is smaller than its parent, otherwise sift down
		if (comparator.compare(newVal, heap.get(parent(originalBidIndex))) < 0)
			siftUp(originalBidIndex - 1);
		else
			siftDown(originalBidIndex);
	}

	/* SCUFFED
	public void update(E oldVal, E newVal) {
		int oldIndex = heap.indexOf(oldVal);
		for (int i = 0; i < heap.size(); i++) {
			if (((Bid) heap.get(i)).bid == ((Bid) heap.get(oldIndex)).bid) {
				System.out.println("found same bid yay");
				heap.set(heap.indexOf(oldVal), newVal);
				if (comparator.compare(newVal, heap.get(parent(oldIndex))) > 0)
					siftUp(oldIndex);
				else
					siftDown(oldIndex);
			}
		}
	}
	*/

	// Removes the smallest item in the priority queue.
	// Throws NoSuchElementException if empty.
	public void deleteMinimum() {
		if (size() == 0)
			throw new NoSuchElementException();

		heap.set(0, heap.get(heap.size()-1));
		heap.remove(heap.size()-1);
		if (heap.size() > 0) siftDown(0);
	}

	private void swapMapIndex(E childItem, E parentItem) {
		Integer childIndex = bidMap.get(childItem);
		Integer parentIndex = bidMap.get(parentItem);
		bidMap.replace(childItem, parentIndex);
		bidMap.replace(parentItem, childIndex);
	}

	// swap nodes
	private void swap(int index, int parentIndex) {
		E tmp = heap.get(index);
		swapMapIndex(heap.get(index), heap.get(parentIndex));
		heap.set(index, heap.get(parentIndex));
		heap.set(parentIndex, tmp);
	}

	// Sifts a node up.
	// siftUp(index) fixes the invariant if the element at 'index' may
	// be less than its parent, but all other elements are correct.
	private void siftUp(int index) {
		int parentIndex = parent(index);
		while (index > 0 && comparator.compare(heap.get(index), heap.get(parentIndex)) < 0) {
			swapMapIndex(heap.get(index), heap.get(parentIndex));
			swap(index, parentIndex);
			index = parentIndex;
			parentIndex = parent(index);
		}
	}
     
	// Sifts a node down.
	// siftDown(index) fixes the invariant if the element at 'index' may
	// be greater than its children, but all other elements are correct.
	private void siftDown(int index) {
		E value = heap.get(index);
		
		// Stop when the node is a leaf.
		while (leftChild(index) < heap.size()) {
			int left    = leftChild(index);
			int right   = rightChild(index);

			// Work out whether the left or right child is smaller.
			// Start out by assuming the left child is smaller...
			int child = left;
			E childValue = heap.get(left);

			// ...but then check in case the right child is smaller.
			// (We do it like this because maybe there's no right child.)
			if (right < heap.size()) {
				E rightValue = heap.get(right);
				if (comparator.compare(childValue, rightValue) > 0) {
					child = right;
					childValue = rightValue;
				}
			}

			// If the child is smaller than the parent,
			// carry on downwards.
			if (comparator.compare(value, childValue) > 0) {
				swapMapIndex(heap.get(index), heap.get(parent(index)));
				heap.set(index, childValue);
				index = child;
			} else break;
		}

		heap.set(index, value);
	}

	// Helper functions for calculating the children and parent of an index.
	private final int leftChild(int index) {
		return 2*index+1;
	}

	private final int rightChild(int index) {
		return 2*index+2;
	}

	private final int parent(int index) {
		return (index-1)/2;
	}
}
