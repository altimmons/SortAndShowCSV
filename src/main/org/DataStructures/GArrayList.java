package org.DataStructures;



import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class GArrayList<T> implements Iterable<T>{
  T        t;
  Class<?> type;
  String   typeString;
  private              T[]    data;
  private              int start            = 0;
  private              int nextWrite_Cursor = 0;
  private final static int INITIAL_SIZE     = 10;
  private static final int    PREVIEW_LENGTH      = 5;
  private static final String VALUE_SEP           = " ";
  private static final String NULL_REPRESENTATION = "<null>";
  
  
  /**
   * This is an implementation that matches many of the {@link List}
   * implementations in Java.  At one point it was mentioned that we should
   * not be using the built in Collections, lists, tree, and stack classes,
   * and are supposed to implement our own.  SO I have done that here.
   *
   * {@link GArrayList} was written after, and is essentially the same thing,
   * many of the ideas in one were implemented in the other. And in truth, by
   * the end, [[GArrayList]] could have completely replaced RecordSet.
   * RecordSet is an Integer implementation of GArrayList  The class was
   * really an experimentation with Generics, as I wanted to include some
   * practice with Generic typing in this assignment.  So the implementation
   * is fully Generic.  Which, incidentally, is where the name comes from
   * _**G**_eneric_**ArrayList**_ and *ArrayList* was already taken.
   *
   * Cumbersome instantiation, but on looking at it on Stack Overflow etc.
   * the answers there have only done it the way I've done it.  Rather
   * disappointing, seems like there must be another way.
   *
   * <code>GArrayList<Integer> integerGArrayList = new GArrayList<>(Integer.class);</code>
   * @param classType
   */
  public GArrayList(  Class<T> classType ){
    type            = classType;
    this.typeString = ( classType.getSimpleName() != null ?
                        classType.getSimpleName() :
                        classType.getCanonicalName()
    ).toString();
    @SuppressWarnings( "unchecked" )

    final T[] dataArray = (T[])Array.newInstance( classType , INITIAL_SIZE );
    this.data = dataArray;
  }
  

/*
  public <E> GArrayList(E e){
    if(Objects.nonNull( e )){
      type = e.getClass();
      this.typeString = ( type.getSimpleName() != null ?
                          type.getSimpleName() :
                          type.getCanonicalName()
      );
  
      @SuppressWarnings( "unchecked" ) new GArrayList<E>( E.class ).data;
      E[] eA = (E[])Array.newInstance( type , INITIAL_SIZE );
      this.data = eA;
    }
  }
  */
  
  
  /**
   * Return an array of the data.  Intended to be an "Unboxing" type
   * operation.  Should return a <i>new</i> array, not a reference to the
   * internal array, such that operations on the returned data do not affect
   * the data here.
   *
   * This prevents side-effects and unexpected behaviors.
   *
   * To get a reference to <i>this</i>.data < T[] >
 *   @see public T[] getData()
   * @param lowInclusive
   * @param highExclusive
   * @return
   */
  public T[] extract(int lowInclusive, int highExclusive){
    int len = highExclusive - lowInclusive;
    T[] tempArray = (T[])Array.newInstance( this.type , len );
    System.arraycopy( data , lowInclusive , tempArray , 0 , len );
    return tempArray;
  }
  
  /**
   * Overloaded Default Method for Extract, sets bounds to (0, Length)
   * @return
   */
  public T[] extract(){
    return  extract( 0 , this.getSize() );
  }
  
  
  /**
   * Returns a pointer to the internal array that this Class essentially wraps.
   * @see private T[] data
   * @return
   */
  public T[] getData(){
    return data;
  }
  
  
  /**
   * add new values, this method is written such that it allows chained method
   * calls.
   * <p>
   * This also allows multiple parameters, and each will be added.  Including
   * arrays of this type.
   *
   * @param newValues
   *
   * @return
   */
  public GArrayList<T> add( T... newValues ){
    for( T t : newValues ){
      
      //needs to be checked each time. Otherwise adding a large array will fail
      if( nextWrite_Cursor >= ( data.length * .9 ) ) extendArray();

      data[ this.nextWrite_Cursor ] = t;
                                      nextWrite_Cursor += 1;
    }
    return this;
  }
  
  
  /**
   * public void add(int position,
   *                 T element)
   *
   * @param position
   *       the position to add it at.
   * @param element
   *       the element which to add.
   */
  public void add(int position, T element){
    if (checkSize( position )) return;
    set( nextWrite_Cursor , element );
  }
  
  public T get( int i ){
    return data[ i ];
  }
  
  
  public T remove( int i ){
    T temp = data[ i ];
    System.arraycopy( data , i + 1 , data , i , nextWrite_Cursor - i );
    return temp;
  }
  
  public boolean contains( T o ){
    for( T v : data ){
      if( v.equals( o ) ) return true;
    }
    return false;
  }
  
  /**
   * A default overloaded method for head.
   *
   * @return the head(PREVIEW_LENGTH) method- e.g. returns the first <i>n</i>
   * values of the data array.
   */
  String head(){
    return head( PREVIEW_LENGTH );//default value
  }
  
  /**
   * Making sure we cover all possible values of n, n cannot be greater than
   * size, if n>size, n==size. if n is less than 0 start at the other end.
   *
   * @param n
   *       if negative, calls tail - like the wrap around arrays of python. It
   *       should be noted this would be a single line in python.
   *
   * @return returns the first <i>n</i> values of the data array.
   */
  public String head( int n ){
    if( nextWrite_Cursor < n ) n = nextWrite_Cursor; //make sure we dont overflow. This caps n at size.
    if( n < 0 ) return tail( - n );
    
    StringBuilder sb = new StringBuilder();
    for( int i = 0 ; i < n ; i++ ){
      sb.append( VALUE_SEP );
      sb.append( ( data[ i ] != null ) ? data[ i ] : NULL_REPRESENTATION );
    }
    return sb.toString();
  }
  
  
  /**
   * Making sure we cover all possible values of n, n cannot be greater than
   * size, if n>size, n==size. if n is less than 0 start at the other end.
   *
   * @param n
   *       if negative, calls head - like the wrap around arrays of python. It
   *       should be noted this would be a single line in python.
   *
   * @return returns the first <i>n</i> values of the data array.
   */
  private String tail( int n ){
    if( nextWrite_Cursor < n ) n = nextWrite_Cursor; //make sure we dont overflow. This caps n at size.
    if( n < 0 ) return head( - n );
    
    StringBuilder sb = new StringBuilder();
    for( int i = nextWrite_Cursor ; i > ( nextWrite_Cursor - n ) ; i-- ){
      sb.append( VALUE_SEP );
      sb.append( ( data[ i ] != null ) ? data[ i ] : NULL_REPRESENTATION );
    }
    return sb.toString();
  }
  
  /**
   * A default overloaded method for tail.
   *
   * @return the tail(PREVIEW_LENGTH) method- e.g. returns the first <i>n</i>
   * values of the data array.
   */
  String tail(){
    return tail( PREVIEW_LENGTH );
  }
  
  
  /**
   * Gets the size of the data array
   *
   * @return the size of the data set.
   */
  public int getSize(){
    return this.nextWrite_Cursor;
  }
  
  
  /**
   * Swap the values at index A and index B. (Chainable) this method returns a
   * reference to the object, allowing chaining, or the return value can be
   * ignored.
   *
   * @param posA
   * @param posB
   */
  public GArrayList<T> swap( int posA , int posB ){
    checkSize( posA );
    checkSize( posB );
    T temp = data[ posA ];
    data[ posA ] = data[ posB ];
    data[ posB ] = temp;
    return this;
  }
  
  
  /**
   * If the array gets to large, double the size of the array.
   */
  private void extendArray(){
    @SuppressWarnings( "unchecked" ) T[] newArray = (T[])new Object[ data.length * 2 ];
    System.arraycopy( data , 0 , newArray , 0 , data.length );
    data = newArray;
  }
  
  /**
   * Returns the string descriptor of this array.
   *
   * @return
   */
  public String toString(){
    return String.format( "data(%s)-{%s...}[%d elements]" ,
          typeString ,
          head() ,
          getSize() );
  }
  
  /**
   * Getter for property 'empty'.
   *
   * @return Value for property 'empty'.
   */
  public boolean isEmpty(){
    return ( nextWrite_Cursor == 0 );
  }
  
  /**
   * Set g array list.
   *
   * @param position
   *       the position
   * @param element
   *       the element
   *
   * @return the g array list
   */
  public GArrayList<T> set(int position, T element){
    if( position > nextWrite_Cursor ){
      System.err.println("Index out of bounds");
    }else {
      data[ position ] = element;
      if( nextWrite_Cursor == position) nextWrite_Cursor += 1;
    }
    return this;
  }
  
  
  /**
   *  private boolean checkSize(int index)
   * @param index
   * @return true if the size is within bounds.
   * @exception IndexOutOfBoundsException - occurs when the queried index
   * isnt possible.
   * 
   * Does not return false.  Instead throws an Error.
   */
  private boolean checkSize(int index){
    if ( index > nextWrite_Cursor - 1 || index < 0)
      throw new IndexOutOfBoundsException(String.format("Out of Bounds, " 
                                                        + "attempted to " 
                                                        + "access position " 
                                                        + "%d, with size %d",
            index, nextWrite_Cursor
                                                       ));
    
    
    else return true;
  }
  
  
  /** {@inheritDoc} */
  @NotNull
  @Override
  public Iterator<T> iterator(){
    return new Itr();
  }
  
  /** {@inheritDoc} */
  @Override
  public void forEach( Consumer<? super T> action ){
  
  }
  
  /** {@inheritDoc} */
  @Override
  public Spliterator<T> spliterator(){
    return Arrays.spliterator( data );
  }
  
  

  /**
   * An optimized version of AbstractList.Itr
   */
  private class Itr implements Iterator<T> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such
    
    /** Constructs a new Itr. */ // prevent creating a synthetic constructor
    Itr() {
      cursor = start;
    }
    
    /** {@inheritDoc} */
    public boolean hasNext() {
      return cursor != nextWrite_Cursor;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public T next() {
      if(hasNext()){
        lastRet = cursor;
        return data[ cursor++ ];
      } else throw new IndexOutOfBoundsException(
            String.format( "Index out of bounds, attempted to access %d, in "
                           + "size %d", cursor, nextWrite_Cursor )
      ) ;
    }
    
    /** {@inheritDoc} */
    public void remove() {
      if (lastRet < 0)
        throw new IllegalStateException();
      
        GArrayList.this.remove(lastRet);
        cursor = lastRet;
        lastRet = -1;
    }
    
    /** {@inheritDoc} */
    @Override
    public void forEachRemaining(Consumer<? super T> action) {
      Objects.requireNonNull(action);
      final int size = nextWrite_Cursor;
      int i = cursor;
      if (i < size) {
        final Object[] es = data;
        if (i >= es.length)
          throw new IndexOutOfBoundsException(
                String.format( "Index out of bounds, attempted to access %d, in "
                               + "size %d", i, nextWrite_Cursor )
          ) ;
        for (; i < size ; i++)
             action.accept(elementAt(es, i));
        // update once at end to reduce heap write traffic
        cursor = i;
        lastRet = i - 1;
      }
    }
    
  
    public T elementAt( Object[] es , int i ){
      return (T)es[ i ];
    }
  }

  
  /**
   * An optimized version of AbstractList.ListItr
   */
  private class ListItr extends GArrayList<T>.Itr implements ListIterator<T> {
    ListItr(int index) {
      super();
      cursor = index;
    }
    
    /** {@inheritDoc} */
    public boolean hasPrevious() {
      return cursor != 0;
    }
    
    /** {@inheritDoc} */
    public int nextIndex() {
      return cursor;
    }
    
    /** {@inheritDoc} */
    public int previousIndex() {
      return cursor - 1;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public T previous() {
      int i = cursor - 1;
      if (i < 0)
        throw new NoSuchElementException();
      Object[] elementData = GArrayList.this.data;
      if (i >= elementData.length)
        throw new ConcurrentModificationException();
      cursor = i;
      return (T) elementData[lastRet = i];
    }
    
    /** {@inheritDoc} */
    public void set( T t ) {
      if (lastRet < 0)
        throw new IllegalStateException();
        GArrayList.this.set(lastRet, t);
    }
    
    /** {@inheritDoc} */
    public void add( T t ) {
      int i = cursor;
      GArrayList.this.add(i, t);
      cursor = i + 1;
      lastRet = -1;
    }
  }
}


