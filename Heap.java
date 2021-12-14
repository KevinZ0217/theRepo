/*****************************************************************************
 * Name: Jiaqi Zhang
 * PID: A16418300
 * Course: CSE 12, FA21 
 * Date: December 9th, 2021 
 * USER:cs12fa21fk 
 *                                Assignment 10 
 * File Name:   Heap.java 
 * Description: This program creates a array with the student name and ID that  
 *             can do the operations such as remove or insert.
 *              
 ****************************************************************************/
 /**
 *Class: Heap
 *Description:The class inherits the Base class, with datatype of Whatever.
 *        Create a tracker within the Heap to keep track of 
 *        the memory usage with elements inside the to do operations 
 *        including jettison, check the state of Heap, remove and insert. 
 *Fields: Includes the catastrophy and debug message.
 *        occupancy: The number of elements stored in the Heap array
 *        size: The size of the Heap array 
 *        heap[]: The heap array itself
 *        Tracker: For tracking memory usage.
 *        index: Last location checked in the heap array 
 *        debug: allocation of debug states.
 ***************************************************************************/
@SuppressWarnings("unchecked")
public class Heap<Whatever extends Base> {

	// counters, flags and constants 
	private static boolean debug;           // allocation of debug states

	// data fields
	private long occupancy;     // how many elements are in the heap array
	private int size;           // size of heap array
	private Whatever heap[];       // the heap array itself ==> array of whatever
	private Tracker tracker;    // to track memory

	// initialized by locate function
	private int index;      // last location checked in heap
        
	private int position;	//pointer for the position of element. 
	
	// messages
	private static final String with = " with ";
	private static final String remove = "[Removing \n";
	private static final String and = " and ";
	private static final String swap = "[Swap ";
	private static final String compareParent = "[Comparing parent ";
	private static final String insert = "[Inserting ";
	private static final String reheapup = "[ReheapUp at child index ";
	private static final String CLOSE = "]";
	
	/*
	 * enable debug message
	 */
	public static void debugOn () {
		
		debug = true;
	}

	/*
	 * disable debug message
	 */ 
	public static void debugOff () {
		
		debug = false;
	}
	
	/*
	 * method for alternating the debug message. 
	 *
	 */
	public static void debugControl(){
		//if the current message is true,
		//turn it off
		if(debug == true){
			debug = false;
		}
		//if the current message is false,
		//turn it on.
		else{
			debug = true;
		}
	}

	/**
	* Constructor method of Heap
	* @param sz: The size of the Heap array
	*           
	*/
	public Heap (int sz) {
		//size of the array 
		size = sz;
		//items currently in the array
		occupancy = 0;
		//the heap with input size
		heap = (Whatever[]) new Base[size];
		//Set all the position of the Heap to null. 
		for(int theCount = 0; theCount < size-1; theCount++){
			heap[theCount] = null;
		}
	
		// DO NOT CHANGE THIS PART
		tracker = new Tracker ("Heap", 
			Size.of (index) 
			+ Size.of (occupancy)
			+ Size.of (size)
			+ Size.of (heap)
			+ Size.of (tracker),
			 " calling Heap Ctor");
	}
	/**
	 * Method for jettisoning the Heap. 
	 */
	public void jettison () {
		//Jettison each element in the heap. 
		index--;
		while(index >= 0){
			heap[index].jettison();
			heap[index] = null;
			index--;
		}
		heap = null;
		//Set all the field to null. 
		index = 0;
		occupancy = 0;
		size = 0;
		tracker.jettison();
		tracker = null;
	}
	
	/**
	 * Getter method for parent index
	 * @param: childIndex: The index of the child
	 * @return: int: the parent Index of the child. 
	 */
	private int getParent(int childIndex){
		//to void magic number
		int divisor = 2;
		return (childIndex-1)/divisor;
	}
	
	/**
	 * Getter method for the child index.
	 * @param: parentIndex: The index of the parent.
	 * @paramL whichChild: The left or right child.
	 * @return: The index of the child.
	 *
	 */
	private int getChild(int parentIndex, String whichChild){
		//check if we want left or right child
		if(whichChild.equals("left")){
			//to avoid magic number
			int multiplier = 2;
			//calculate the index
			int leftchild = parentIndex*multiplier+1;
			//if there's no such child return the
			//parent index. 
			if(leftchild >= index){
				return parentIndex;
			}
			return (parentIndex*multiplier)+1;
		}
		//if we want the right child
		else{
			//to avoid magic number
			int multiplier = 2;
			int add = 2;
			//calculate the index of right child
			int rightchild = parentIndex*multiplier+add;
			//if there's no such child return the 
			//parent index.
			if(rightchild >= index){
				return parentIndex;
			}
			return (parentIndex*multiplier)+add;
		}
	}

	/**
	 * public method for performing insertion into the heap array
	 *
	 * @param element: The element to insert. 
	 * @return The element that is inserted
	 */
	public Whatever insert (Whatever element) {

		//Debug message. 
		if(debug){
			System.err.println(insert+element.getName()+CLOSE);
		}
		//check the occupancy and insert the rootnode if empty.  
		if(occupancy == 0){
			heap[0] = element;
			//increase the index for the next insertion. 
			index++;
			occupancy++;
			return element;
		}
		//insert the element 
		heap[index] = element;
		//reheap up the tree 
		reheapUp(element,index);
		//increase the index for the next insertion
		index++;
		//increase the occupancy. 
		occupancy++;
		return element;
	} 

	/**
	 * public method for performing remove from the heap array
	 *
	 * @return The element that is removed. 
	 *
	 */
	public Whatever remove(){
		//debug message
		if(debug){
			System.err.println(remove+heap[0]+CLOSE);
		}
		//finalLeaf is for storing the final leaf node. 
		Whatever finalLeaf = null;
		//decrease the index from extra index in insert method. 
		index--;
		//debug message
		if(debug){
			System.err.println(swap+heap[index].getName()+
			and+heap[0].getName()+CLOSE);
		}
		//store the current node in finalLeaf
		finalLeaf = heap[index];
		//store the rootnode in rootNode
		Whatever rootNode = heap[0];
		//swap the element
		heap[index] = rootNode;
		heap[0] = finalLeaf;
		//decrement the occupancy
		occupancy--;
		//if there'e more than 1 node call reheap.
		if(index > 0)
			reheapDown(0);
		return rootNode;

	}
	
	/**
	 * private method for reheaping the heap tree.
	 * called from insert and start from current index. 
	 * @param element: The element of the node
	 * @param theindex: The index of node to be swaped. 
	 *
	 */
	private void reheapUp(Whatever element, int theindex){
		//debug message
		if(debug){
			System.err.println(reheapup+theindex+CLOSE);
		}
		//get the parent index. 
		int parentIndex = this.getParent(theindex);
		//to check if the parent is more important. 
		if(heap[theindex].importantThan(heap[parentIndex])){
			//debug message
			if(debug){
				System.err.println(compareParent+
						heap[parentIndex].getName()
						+with+heap[theindex].getName()
						+CLOSE);
			}
			//debug message
			if(debug){
				System.err.println(swap+heap[parentIndex].getName()
						+and+heap[theindex].getName()
						+CLOSE);
			}
			//swap the current node and the parent. 
			Whatever exchange = (Whatever)heap[theindex];
			heap[theindex] = heap[parentIndex];
			heap[parentIndex] = exchange; 
			//update the position index. 
			position = parentIndex;
			//reheap up from the current position.
			reheapUp(element, position); 
		}


	}
	/**
	 * The private method for reheaping down.
	 * called from remove method
	 * @param theindex: The index of the starting point. 
	 */
	private void reheapDown(int theindex){
		//get the right child index of the current node
		int rightChildIndex = this.getChild(theindex, "right");
		//get the left child index of the current node
		int leftChildIndex = this.getChild(theindex, "left");
		//check if one of the child is more important than the parent. 
		if(heap[leftChildIndex].importantThan(heap[theindex])||
			heap[rightChildIndex].importantThan(heap[theindex])){
			//figure out the most important between the two child.
			if(heap[leftChildIndex].importantThan(heap[rightChildIndex])){
				//debug message
				if(debug){
					System.err.println(swap+heap[theindex].getName()
					+with+heap[leftChildIndex].getName()
					+CLOSE);
				}
				//swap the current node and the more important child.
				Whatever exchange = (Whatever)heap[theindex];
				heap[theindex] = heap[leftChildIndex];
				heap[leftChildIndex] = exchange;
				//update the position
				position = leftChildIndex;
				//call the reheap down to continue. 
				reheapDown(position);
			}
			//figure out the most important between the two child.
			else if(heap[rightChildIndex].importantThan(heap[leftChildIndex])){
				//debug messge
				if(debug){
					System.err.println(swap+heap[theindex].getName()
					+with+heap[leftChildIndex].getName()
					+CLOSE);
				}
				//swap the current node and the more important child
				Whatever exchange = (Whatever)heap[theindex];
				heap[theindex] = heap[rightChildIndex];
				heap[rightChildIndex] = exchange;
				//update the position
				position = rightChildIndex;
				//call the reheap down to continue. 
				reheapDown(position);
			}
		}
	}
 
	/**
	 * Creates a string representation of the Heap array. The method 
	 * traverses the entire array, adding elements one by one ordered
	 * according to their index in the array. 
	 *
	 * @return  String representation of Heap array
	 */
	public String toString () {
		//start the string
		String string = "The Heap ";
		string += "has " + occupancy + " items: "; 
		//if the array is not empty, create a new line for element.
		if(occupancy != 0){
			string += "\n";
		}
		/* go through all table elements */
		for (int thisindex = 0; thisindex < index; thisindex++) {
			//if the current index is not null, continue printing.
			if (heap[thisindex] != null) {
				string += "At index " + thisindex + ": ";
				string += " " + heap[thisindex];
			}
			//create a new line for next index. 
			if(thisindex<index-1) 
				string += "\n";
		}
		//debug message.
		if(debug)
			System.err.println(tracker);

		return string;
	}
}
