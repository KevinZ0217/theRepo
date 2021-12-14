/** 
 * class: Base
 * Description: The abstract class Base, with three
 * 	abstract methods.
 */
public abstract class Base {
	//the getter method for getting
	//names of the element
	public String getName () {
		return "";
	}
	//the method for comparing importance
	public boolean importantThan (Base base) {
		return true;
	}
	//the jettison method.
	public void jettison () {}
}
