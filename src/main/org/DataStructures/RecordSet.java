package org.DataStructures;


import java.util.stream.IntStream;

/**
 * Holds the values;
 */
public class RecordSet{
  protected int size = 0;
  private static final int INITIAL_SIZE = 1000;
  private static final int PREVIEW_LENGTH = 5;
  private static final String VALUE_SEP = " ";
  private static final String NULL_REPRESENTATION = "<null>";
  
  private String descriptor;
  Integer[] data;
  protected int cur_write_pos;
  
  
  private Integer[] emptyPositions;//if needed to remove spots.
  
  public RecordSet(){
   data = new Integer[ INITIAL_SIZE ];
    cur_write_pos = 0;
  }
  
  public RecordSet( String description ){
    this.descriptor = description;
    data = new Integer[ INITIAL_SIZE ];
    cur_write_pos = 0;
  }
  
  /**
   * The size of the array is reading as the initial size when I load the
   * sort method.  It is obviously picking up the extra null values at the
   * end.  The extract method does basically the same thing.  So we can use
   * that here.
   *
   */
  public void finalize(){
    if(data.length !=  this.getSize()){
      Integer[] copy = extract();
      data = extract();
    }
  }
  
  /**
   * Couldnt decide which was a better name,  Probably this one as finalize
   * means something else in java.
   */
  public void trim(){
    finalize();
  }
  
  
  /**
   * A default overloaded method for head.
   * @return the head(PREVIEW_LENGTH) method- e.g. returns the first <i>n</i>
   * values of the data array.
   */
  String head(){
    return head( PREVIEW_LENGTH );//default value
  }
  

  
  public void setDescriptor( String descriptor ){
    this.descriptor = descriptor;
  }
  
  /**
   * Making sure we cover all possible values of n, n cannot be greater than
   * size, if n>size, n==size.
   * if n is less than 0 start at the other end.
   *
   * This now adds markers at each 5, to help locate values rapidly.
   *
   * @param n if negative, calls tail - like the wrap around arrays of python.
   *          It should be noted this would be a single line in python.
   * @return returns the first <i>n</i> values of the data array.
   */
  public String head(int n){
    if (n > size ) n = size; //make sure we dont overflow. This caps n at size.
    if ( n < 0 ) return tail( - n );
  
    StringBuilder sb = new StringBuilder();
    for( int i = 0 ; i <n  ; i++ ){
      sb.append( VALUE_SEP );
      if(n>5 && i%5== 0) sb.append( String.format( "(%d)" , i ) );
      sb.append(
            ( data[ i ] != null )
            ? data[ i ]
            : NULL_REPRESENTATION
               );
    }
    return sb.toString();
  }
  
  
  /**
   * Convienence method to get the formatting string of this object, without
   * having to create an object just to get it.  Just pass an Integer array
   * @param ints - an Integer Array.
   *             prints the first `MAX` values.  Which is set to 100 by default.
   *
   * @return a formatted string of the array with guideposts.
   */
  public static String formatSet(int length, Integer ... ints){
    int maxLength = length;
    int n = ints.length;
    StringBuilder sb = new StringBuilder();
    for( int i = 0 ; i <n  ; i++ ){
      sb.append( VALUE_SEP );
      if(n>5 && i%5== 0) sb.append( String.format( "(%d)" , i ) );
      sb.append(
            ( ints[ i ] != null )
            ? ints[ i ]
            : NULL_REPRESENTATION
               );
        if(i>= maxLength) break;
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
  public String tail( int n ){
    if (size < n) n = size; //make sure we dont overflow. This caps n at size.
    if ( n < 0 ) return head( - n );
  
    StringBuilder sb = new StringBuilder();
    for( int i = size -1 ; i > (size - n)  ; i-- ){
      sb.append( VALUE_SEP );
      sb.append(
            ( data[ i ] != null )
            ? data[ i ]
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
  public RecordSet swap( int posA, int posB ){
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
  RecordSet add( Integer ... datum ){
    for(Integer i: datum){
    
    if(cur_write_pos >= (data.length * .9))extendArray();
    data[ cur_write_pos ] = i;
    cur_write_pos += 1;
    size += 1;
    }
  
    return this;
  }
  
  /**
   * Returns a new Integer[] of the values between start and end EXCLUSIVE,
   * and 0 indexed, such that end, must be less than or equal this.size();
   * @param start start index
   * @param end index
   * @return a new array
   */
  public Integer[] extract(int start, int end){
   //check bounds
   if(start < 0) throw new IllegalArgumentException("Start bound was less "
                                                    + "than 0");
   if(end > size  )  throw new IllegalArgumentException("End bound was "
                                                          + "greater than "
                                                          + "size");
  
  
    int outSize = end - start;
    
    Integer[] tempOut = new Integer[outSize];
    System.arraycopy( data , start , tempOut , 0 , outSize );
    return tempOut;
  }
  

  
  /**
   * Overloaded method to extract all the data.
   * @return
   */
  public Integer[] extract(){
    return extract( 0 , size );
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
   * Make sure we dont alter our initial data. This returns a COPY of the
   * original data set.
   *
   * also make sure we strip the null values at the end.
   * @return a copy of the stored data.
   */
  Integer[] getData(){
    Integer[] outData = new Integer[ size ];
    System.arraycopy( data , 0 , outData , 0 , size );
    return outData;
  }
  
  
  /**
   * This method, as opposed to the above, actually returns a pointer to the
   * actual data that is used by the data.
   * @return
   */
  Integer[] getAccess(){
    return this.data;
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
  
  public String getDescriptor(){
    return descriptor;
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
   * A method to insert a value, as yet untested, for use with the List
   * iterator.
   * @param pos - the position to add into
   * @param value - the value placed.
   */
  public void insert(int pos, Integer value){
    if(cur_write_pos >= (data.length  + 1))extendArray();
    
    Integer[] newArray = new Integer[ data.length * 2 ];
    int len = this.getSize() - pos;
    System.arraycopy( data , pos , data , pos +1 , len );
    cur_write_pos += 1;
    data[ pos ] = value;
  }
  
  
  /**
   * Source for this delightfully obtuse bit of code:
   * https://www.techiedelight.com/convert-int-array-integer-array-java/
   * @param intArray
   * @return
   */
  public RecordSet add( int[] intArray ){
    add( IntStream.of( intArray ).boxed().toArray( Integer[] :: new ) );
    return this;
  }
  
  
  
  /**
   * Static method - required for iterator.
   * @param data
   * @param i
   * @return
   */
  public static Integer elementAt( Integer[] data , int i ){
    return data[ i ];
  }
  
  
  
  
  /**
   * Static method - required for iterator.
   *
   * @param dataSet - the object.
   * @param i
   * @return
   */
  public static Integer elementAt( RecordSet dataSet , int i )
  {
    return dataSet.getValue( i );
  }
  
}
