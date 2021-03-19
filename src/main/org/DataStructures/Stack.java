package org.DataStructures;



import java.util.EmptyStackException;


/**
 * Class:
 * <p>
 * .................................................<p>
 * ███████╗████████╗.█████╗..██████╗██╗..██╗███████╗<p>
 * ██╔════╝╚══██╔══╝██╔══██╗██╔════╝██║.██╔╝██╔════╝<p>
 * ███████╗...██║...███████║██║.....█████╔╝.███████╗<p>
 * ╚════██║...██║...██╔══██║██║.....██╔═██╗.╚════██║<p>
 * ███████║...██║...██║..██║╚██████╗██║..██╗███████║<p>
 * ╚══════╝...╚═╝...╚═╝..╚═╝.╚═════╝╚═╝..╚═╝╚══════╝<p>
 * .................................................<p>
 * <p>
 *
 * <p>
 * Implements the Stack of Generic type.  Used in several places throughout
 * the program.
 * <p>
 * Initially I had an error in my generic implementation.  Explicitly I had
 * {@code Stack<T> implements Stack_Interface} when it needed to be {@code
 * Stack<T> implements Stack_Interface<T>} This lead to a situation where you
 * could push any object onto the stack.  So there are all these vestigal
 * elements to account for that- checking uniformity and whatnot.  I sort of
 * left them in, thinking down the road in this class, I might want an
 * implementation where I can push anything onto a stack,  Though, probably
 * the better way to implement that is to do wildcards or object throughout
 * class and implement the generic in `Node` only.
 *
 * @param <T> Generic Type
 */
public class Stack<T>{
  
  private Node<T> head;
  private int      sLength;
  private String   typeString = null;
  private Class<?> type = null;
  /**
   * Constructor Creates a stack- null without any elements
   */
  
  
  public Stack() {
    this.head = null;
    this.sLength = 0;
  }
  
  /**
   * Constructor with an initial elements
   *
   * @param head
   */
  //  @SuppressWarnings("unchecked")
  public Stack( T head ) {
    setType( head );
    //create a new stack, and this time create an initial s_value.
    this.head  = new Node<T>( null, head );
    this.sLength = 1;
  }
  
  
  public void setType( Class<?> newType ){
    
    if(type == null){
      this.type = newType;
      this.typeString =  ( newType.getSimpleName() != null ?
                           newType.getSimpleName() :
                           newType.getCanonicalName()
      ).toString();
     }else if (!newType.equals( this.type )){
      System.err.println("Error: Trying to push Obj of Class "
                         + newType.getSimpleName().toString() + " "
                         + "to a Stack of Class " + typeString);
    }else return; //else its not null and it equals each other.  Do nothing.
}
  
  /**
   * Set the type at first opportunity.
   *
   * Any object which will be the archetype for this class.
   *
   * Replaced this with public setType(Class\<?\> as you may want to set it
   * to a parent type.
   *
   *  @param someT an archetypal object.
   */
  private void setType( T someT ){
    setType( someT.getClass() );
  }
  
  
  /**
   * Push an object onto the stack.  Creates a new node.
   *
   * returns a ref to this class, to allow chaining.
   *
   *
   * @param t and object of specified type T
   */
  //  @SuppressWarnings("unchecked")
   public Stack<T> push( T t ) {
    if(isEmpty())setType( t );
//    else {
      
      Node temp = this.head;
      this.head = new Node<T>( temp, t );
      this.sLength++ ;
      return this;
  }

  /**
   * Pops the next item after the stack.
   *
   * @return the value of the top node.
   *
   * @throws EmptyStackException - thrown if the stack is empty when popped.
   *                             IsEmpty() should be checked first.
   */
  public T pop()
        throws EmptyStackException {
    if( !isEmpty() ) {
      Node<T> temp = this.head;
      this.head = temp.getNext();
      this.sLength--;
      return temp.value;
    } else {
      System.err.println( "The Stack is empty, and cannot be " + "popped!" );
      throw new EmptyStackException();
    }
  }
  
  /**
   * peek Get the top value of the stack without popping it.
   *
   * @return the value (of any type), of the head.
   *
   * @throws EmptyStackException - if the stack is peeked at and empty. This
   *                             perhaps shouldnt be here.
   */
  public T peek()
        throws EmptyStackException {
    if( head != null ) {
      return head.value;
    } else {
      throw new EmptyStackException();
    }
  }
  
  /**
   * isEmpty Checks to see if the stack is empty
   *
   * @return true if empty, false if otherwise.
   */
  public boolean isEmpty() {
    return this.sLength == 0;
  }
  
  /**
   * the inverse of isEmpty()
   *
   * @return !isEmpty() simply.  Basically a wrapper meth for if stmts.
   */
  public boolean hasNext() {
    return !isEmpty();
  }
  
  /**
   * getLength
   *
   * @return (int) the length of the current stack.
   */
  public int getLength() {
    if( isEmpty() ) {
      return 0;
    } else {
      return this.sLength;
    }
  }
  
  
  /**
   * reverse Simple method to return the current stack in reverse order (as a
   * new stack).  Will have to create a reference on the calling side.
   *
   * @return Stack<T> a new stack.
   */
  public Stack<T> reverse() {
    Node<T>  tempNode    = head;
    Stack<T> outputStack = new Stack<>( head.value );
    while( tempNode.getNext() != null ) {
      tempNode = tempNode.getNext();
      outputStack.push( tempNode.value );
    }
    return outputStack;
  }
  
  
  /**
   * This is a somewhat dodgy method to provide access into the inner
   * workings of the stack for the derived classes.
   * @return
   */
  protected Node<T> getHead(){
    return this.head;
  }
  
  
  private void setHead(Node<T> node){
    this.head = node;
  }
  
  /**
   * Copy the current stack.  Basically calls the reverse method twice.
   *
   * @return a new Stack reference.
   */
  public Stack<T> duplicate() {
    Stack<T> outStack = reverse();
    return outStack.reverse();
  }
  
  
/*NODE SUBCLASS*/
  
  /**
   * Private Subclass:<p> .▐.▄.......·▄▄▄▄..▄▄▄...▄▄.·.<p>
   * •█▌▐█▪.....██▪.██.▀▄.▀·▐█.▀..<p> ▐█▐▐▌.▄█▀▄.▐█·.▐█▌▐▀▀▪▄▄▀▀▀█▄<p>
   * ██▐█▌▐█▌.▐▌██..██.▐█▄▄▌▐█▄▪▐█<p> ▀▀.█▪.▀█▄▀▪▀▀▀▀▀•..▀▀▀..▀▀▀▀.<p>
   * <p>
   * <p>
   * A subclass- the individually instantiated Nodes for the Stack.
   * <p>
   * Has just a a default constructor of {@code Node(Node next, <T>Value} and
   * two exposed methods- one to return the stored Value. and one to lookup
   * the string representation of the type.
   */
  private class Node<E> /*implements Comparable<E>*//*extends NullNode*/ {
    
    private E       value= null;;
    private Node<E> next= null;;
    
    Node(){
    }
    //
   Node(Node next){
     this.next = next;
   }
   
   Node(E e){
     this.value = e;
   }
    
    /**
     * Node Constructor
     *
     * @param next  points to the next Node
     * @param value the value, which may potentially be of any type.
     */
    Node( Node<E> next, E value ) {
      
      this.next = next;
      this.value = value;
    }
    
    //         @Override
    protected E getValue() {
      return this.value;
    }
    
    //        @Override
    protected Node<E> getNext() {
      return this.next;
    }
    
    /**
     * getType return the string value of the type
     *
     * @return string val of type
     */
    //       @Override
    protected String getType() {
      return type.getSimpleName();
    }
  
  
  /**
   * Getter to return the type of the head (if params unspecified.
   *
   * @return returns String stateDescriptor of type;
   */
  public String isType() {
    if( head != null ) {
      return head.getType();
    } else {
      return "Not Yet Initialized";
    }
  }
  
  /**
   * Private method- isType
   *
   * @param object - the value of a node
   *
   * @return the object type string.
   */
  private Class<?> isType( Object object ) {
    return ( (Object) object ).getClass();
  }
  
  /**
   * PUBLIC toString String Descriptor.
   *
   * @return A String describing the object.
   */
  public String toString() {
    
    StringBuilder sb = new StringBuilder();
    sb.append( "This is a" ).append( ( isEmpty() ? "n Empty "
                                                 : " " ) ).append(
          "stack, of " ).append( getLength() ).append( " " )
      // sb.append( ( isUniformType() ? " all " : " at least one " ) );
      .append( ( hasNext() ) ? isType() : "Undefined Type" ).append( "(s)." );
    return sb.toString();
  }
  
  /**
   * PUBLIC preview Sequentially print the stack out more in-depth to String
   *
   * @return A string of all the contents.
   */
  public String preview() {
    if( !isEmpty() ) {
      StringBuilder sb       = new StringBuilder( ">>>:" );
      Node<T>          tempNode = head;
      int           i        = 0;
      while( tempNode != null ) {
        sb.append( "Node " ).append( i ).append( ": " ).append( "\t" ).append(
              tempNode.value.toString() ).append( "\n" );
        try {
          tempNode = tempNode.next;
        } catch( NullPointerException ignore ) {
        }
        i++;
      }
      ;
      
      return sb.toString();
    } else {
      return "There is nothing here...";
    }
  }
  
  
}
  /*END NODE SUBCLASS*/

}


 /*       private class NullNode {
           private boolean tailNode = true;
           private NullNode next;

           NullNode(){

           }
           NullNode(NullNode next){
              this.next =next;
           }

           protected boolean isNullNode(){
              return true;
           }

           protected String getType(){
              return "Null Node";
           }

           protected Object getValue(){
              return null;
           }

           protected NullNode getNext(){
              return this.next;
           }

        }
  */

