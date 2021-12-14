/******************************************************************************
 * Name: Jiaqi Zhang
 * Date: December 9, 2021 
 * File Name:   Driver.java 
 * Description: This file has the elements that are contained in the
 *              Heap, and define the properties or them. 
 *              
 *****************************************************************************/
import java.io.*;
/**
 *Class: UCSDStudent
 *Description: the class that inherits the Base class. 
 *        create a tracker within the UCSDStudent to keep track of 
 *        the memory usage with elements inside the to do operations 
 *        including jettison, check the state of element, and getter method.
 *Fields: name: The name of the student.
 *        studentNum: The ID of the student.  
 ****************************************************************************/

class UCSDStudent extends Base {
	//the public field of student.
	public String name;
	public long studentNum;
	private Tracker tracker;
	/**
	* Constructor method of UCSDStudent
	* @param nm: The name of the student
	* @param studentn: The ID of the student.  
	*/
	public UCSDStudent (String nm, long studentn){
		//initialize the field
		name = nm;
		studentNum = studentn;
		tracker = new Tracker("UCSDStudent", 
			Size.of(name) + Size.of(studentNum) + Size.of(tracker),
			" calling UCSDStudent ctor");
	    
	}

	/**
	 * Method for jettisoning the UCSDStudent object. 
	 */
	public void jettison(){
		name = null;
		studentNum = 0;
		tracker.jettison();
		tracker = null;	
	}

	/*
	* Getter method for getting the student name. 
	* @return String: return the name of the student. 
	*             
	*/
	public String getName(){
		return name;
	}

	/**
	* Boolean method for comparing the object. 
	* @param object: The object to  be compared. 
	* @return boolean: True for equal and false for unequal. 
	*            
	*/
	public boolean equals(Object object){
		//if two objects are the same, return true
		if(this == object)
			return true;
		//if the object is not UCSDStudent type, 
		//return false
		if(!(object instanceof UCSDStudent))
			return false;
		UCSDStudent otherStud = (UCSDStudent) object;
		return name.equals(otherStud.getName());
	}

	/**
	* Method for comparing the importance of the object. 
	* @param base: The object to compare. 
	* @return boolean: True for more important
	* 	 and false for less important.              
	*/
	public boolean importantThan(Base base){
		if(this.name.compareTo(base.getName()) < 0){
			return true;
		}
		return false;
	}

	/**
	* Method of converting to String. 
	* @return: String: The String of the student information. 
	*/	
	public String toString () {
		return "name:  " + name +" with "+ "studentNum:  " + studentNum+".";
	}
}
/**
 * Class:       Driver
 * Description: In this Driver class, it contains the public main method to
 *              prompt the user to run through the program. It creates a while
 *              loop to continuously allow the users to enter choices for what
 *              they want to do for the heap implementations. It will also call
 *              the methods in Heap.java to execute each specific functions.
 * Fields:      NULL - final int of number zero for NULL
 * Public functions: main - main method for allowing users to
 *                          prompt to run programs by entering
 *                          specific arguments
 */
public class Driver {
	private static final short NULL = 0;

	public static void main (String [] args) {

	// initialize debug states
	Heap.debugOff ();

		// The real start of the code
		// get the size of the array from user
		int size = 0;
		System.out.print("\nPlease enter the number of objects "
				+"to be able to store: ");

		try{
			//read the size using decin()
			size = (int)MyLib.decin();
		}
		catch(EOFException eof){
			System.err.print("EOFExecption in Driver main");
		}
		//initialize symtab and buffer
		SymTab<UCSDStudent> symtab = new SymTab<UCSDStudent>(size);
		String buffer = null;
		char command;
		//the number for insertion 
		long number = 0;
		UCSDStudent stu = new UCSDStudent (buffer, 0); 
		//the sign for printing prompt
		int signforprint = 0;
		//the sign for printing prompt
		int insertCounter = 0;
					
		while (true){
			command = NULL; // reset command each time in loop
			try {
		
			command = MyLib.getchar ();
			MyLib.clrbuf (command); // get rid of return

			switch (command) {
			//the case for check memory
			case 'c':
				//set the sign to 1
				signforprint = 1;
				Tracker.checkMemoryLeaks();
				System.out.println();

				break;
			//the case for toggle debug 
			case 't':
				//set the sign to 1
				signforprint = 1;
				//switch the debug message
				Heap.debugControl();

				break;
			//the case for insertion
			case 'i':
				//set the sign to 1
				signforprint = 1;
				//if the array is full,
				//skipping insertion 
				if(insertCounter == size){
					break;
				}
				System.out.print ("Please enter UCSD student"
				+ " name to insert:  ");

				buffer = MyLib.getline (); // formatted input

				System.out.print ("Please enter UCSD student"
				+ " number:  ");

				number = MyLib.decin ();
				MyLib.clrbuf (command); // get rid of return

				// create student and place in symbol table
				symtab.insert (new UCSDStudent (buffer, number));
				insertCounter++;

				break;
			//the case for remove
			case 'r':
				//set the sign to 1
				signforprint = 1;
				//if the array is empty, skipping to remove
				if(insertCounter == 0){
					break;
				}
				UCSDStudent removed; // data to be removed
				removed = symtab.remove ();

				if (removed != null) {
					System.out.println ("Student removed!"); 
					System.out.println (removed);
				}
				//jettison the element removed. 
				removed.jettison();
				removed = null;
				insertCounter--;
				
				break;
			//the case for writing the array. 
			case 'w':
				//set the sign to 1
				signforprint = 1;
				System.out.println ( symtab);
			}	


			//the initial prompt
			if(signforprint == 0){
				System.out.print("Initial heap:\n"
				+ "The Heap has 0 items\n");
			}
			//the prompt after the array is full
			if(insertCounter == size){
				System.out.print ("Please enter a command: "
				+ " (c)heck memory, "
				+ " (r)emove,"
				+ " (t)oggle debug, "
				+ " (w)rite:  ");
			}	
			//the prompt when array is empty
			else if(insertCounter == 0){
				System.out.print ("Please enter a command: "
				+ " (c)heck memory,"
				+ " (i)nsert,"
				+ " (t)oggle debug,"
				+ " (w)rite:  ");
			}
			//the complete prompt
			else {
				System.out.print ("Please enter a command: "
				+ " (c)heck memory,"
				+ " (i)nsert, (r)emove,"
				+ " (t)oggle debug,"
				+ " (w)rite:  ");
			}

			}
			catch (EOFException eof) {
				break;
			}
		}

		System.out.println ("\nFinal Heap:\n" + symtab);
		//jettison and track memory.	
		stu.jettison ();
		symtab.jettison ();
		Tracker.checkMemoryLeaks ();
	}
}
