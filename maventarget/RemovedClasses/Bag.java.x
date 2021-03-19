package org.DataStructures;


import java.util.stream.IntStream;


/**
 * Holds the values;
 */
public class Bag{
  
  private int size = 0;
  private static final int INITIAL_SIZE = 1000;
  private static final int PREVIEW_LENGTH = 5;
  private static final String VALUE_SEP = " ";
  private static final String NULL_REPRESENTATION = "<null>";
  
  private String descriptor;
  Integer[] data;
  private int cur_write_pos;
  private int cur_read_pos;
  
  private Integer[] emptyPositions;//if needed to remove spots.
  
  public Bag(){
    data = new Integer[ INITIAL_SIZE ];
    cur_write_pos = 0;
  }
  
  public Bag(String description){
    this.descriptor = description;
    data = new Integer[ INITIAL_SIZE ];
    cur_write_pos = 0;
  }
  
  
  /**
   * A default overloaded method for head.
   * @return the head(PREVIEW_LENGTH) method- e.g. returns the first <i>n</i>
   * values of the data array.
   */
  String head(){
    return head( PREVIEW_LENGTH );//default value
  }
  
  /**
   * Making sure we cover all possible values of n, n cannot be greater than
   * size, if n>size, n==size.
   * if n is less than 0 start at the other end.
   *
   * @param n if negative, calls tail - like the wrap around arrays of python.
   *          It should be noted this would be a single line in python.
   * @return returns the first <i>n</i> values of the data array.
   */
  public String head(int n){
    if (size < n) n = size; //make sure we dont overflow. This caps n at size.
    if ( n < 0 ) return tail( - n );
    
    StringBuilder sb = new StringBuilder();
    for( int i = 0 ; i <n  ; i++ ){
      sb.append( VALUE_SEP );
      sb.append(
            ( data[ n ] != null )
            ? data[ n ]
            : NULL_REPRESENTATION
               );
    }
    return sb.toString();
  }
  
  
  /**
   * Making sure we cover all possible values of n, n cannot be greater than
   * size, if n>size, n==size.
   * if n is less than 0 start at the other end.
   *
   * @param n if negative, calls head - like the wrap around arrays of python.
   *          It should be noted this would be a single line in python.
   * @return returns the first <i>n</i> values of the data array.
   */
  private String tail( int n ){
    if (size < n) n = size; //make sure we dont overflow. This caps n at size.
    if ( n < 0 ) return head( - n );
    
    StringBuilder sb = new StringBuilder();
    for( int i = size ; i > (size - n)  ; i-- ){
      sb.append( VALUE_SEP );
      sb.append(
            ( data[ n ] != null )
            ? data[ n ]
            : NULL_REPRESENTATION
               );
    }
    return sb.toString();
  }
  
  /**
   * Swap the values at index A and index B.
   * (Chainable) this method returns a reference to the object,
   * allowing chaining, or the return value can be ignored.
   *
   * @param posA
   * @param posB
   */
  public Bag swap(int posA, int posB){
    int temp = data[ posA ];
    data[ posA ] = data[ posB ];
    data[ posB ] = temp;
    return this;
  }
  
  
  /**
   * Gets the size of the data array
   * @return the size of the data set.
   */
  public int getSize(){
    return this.size;
  }
  
  /**
   * replace the data contained within.
   * @param newData
   * @param overrideSize
   * @return
   */
  public boolean replaceData(Integer[] newData, boolean overrideSize){
    if(overrideSize){
      this.data = newData;
      size = newData.length;
      return true;
    }else if (newData.length == size){
      data = newData;
      return true;
    }else{
      return false;
    }
  }
  
  public void setData(Integer[] newData){
    replaceData( newData , true );
  }
  
  
  /**
   * A default overloaded method for tail.
   * @return the tail(PREVIEW_LENGTH) method- e.g. returns the first <i>n</i>
   * values of the data array.
   */
  String tail(){
    return tail( PREVIEW_LENGTH );
  }
  
  /**
   * add the value to the array for storage.
   *
   * (Chainable) this method returns a reference to the object,
   * allowing chaining, or the return value can be ignored.
   *
   * @param datum
   */
  Bag add(Integer ... datum){
    for(Integer i: datum){
      
      if(cur_write_pos >= (data.length * .9))extendArray();
      data[ cur_write_pos ] = i;
                              cur_write_pos += 1;
                              size += 1;
    }
    
    return this;
  }
  
  
  
  
  
  /**
   * get the value a position[<b>pos</b>]
   * @param pos
   * @return
   */
  Integer getValue(int pos){
    if(pos > size){
      System.err.println("Tried to get a value that is outside the range of "
                         + "this array.");
      return null;
    }
    return data[ pos ];
  }
  
  /**
   * Make sure we dont alter our initial data.
   *
   * also make sure we strip the null values at the end.
   * @return a copy of the stored data.
   */
  Integer[] getData(){
    Integer[] outData = new Integer[ size ];
    System.arraycopy( data , 0 , outData , 0 , size );
    return outData;
  }
  
  
  
  void setValue(int pos, Integer value){
    if(pos > size){
      System.err.println("Tried to get a value that is outside the range of "
                         + "this array.");
      return;
    }
    data[ pos ] = value;
    
  }
  
  private void extendArray(){
    Integer[] newArray = new Integer[ data.length * 2 ];
    System.arraycopy( data , 0 , newArray , 0 , data.length );
    data = newArray;
  }
  
  /**
   * Returns the string descriptor of this array.
   * @return
   */
  public String toString(){
    return String.format( "data(%s)-{%s...}[%d elements]" ,
          descriptor ,
          head() ,
          size
                        );
  }
  
  
  /**
   * Source for this delightfully obtuse bit of code:
   * https://www.techiedelight.com/convert-int-array-integer-array-java/
   * @param intArray
   * @return
   */
  public Bag add( int[] intArray ){
    add( IntStream.of( intArray ).boxed().toArray( Integer[] :: new ) );
    return this;
  }
}
