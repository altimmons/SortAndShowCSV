package org.DataStructures;


import java.util.Objects;

public class QSort{
  
          Integer[] data;
  private        DataSet   dataset;
//  private static int       kStop = 1;
//  private int dataLength;
//  private int currentK;
  private static DataSet lastDataSet;
//  private final int ENDSIZE = 2;  //1 or 2.  When to call an element sorted.
  static QSort instance;
  private QSParams params;
private Integer pivotF = null;
private Integer highF = null;
private Integer lowF = null;
  
  /**
   * Constructor for the sort method.  Requires a dataset on which to
   * operate, and QuickSortParameters (QSParam)
   * @param dataset- the dataset on which sorting is performed
   * @param p - the param which carries test setup variables.
   */
  QSort( DataSet dataset, QSParams p ) {
    params = p;
   this.dataset = dataset;
   this.data = dataset.extract();
   if(dataset.getSize() != dataset.getCount())
     if(Sys.debugView )System.err.println("Size Mismatch, doublecheck size." );
//    this.dataLength = dataset.getSize();
    lastDataSet = dataset;
    instance = this;
  }
  
  
  /**
   * {@inheritDoc}
   * An over loaded default case,.
   * @param dataset - the dataset on which sorting is performed
   *
   */
  QSort( DataSet dataset  ){
    new QSort( dataset , QSParams.QS_1ST_K_1_NO_IS );
  }
//
  
  /**
   * Getter for property 'instance'.
   *
   * @return Value for property 'instance'.
   */
  public static QSort getInstance(){
    return instance;
  }
 
  /**
   * the starting sort method.  Set the data with the constructor.
   * @return
   */
  public Integer[] sort(){
    if(data != null){
//      this.setkStop( 1 );
      this.sort(  0 , data.length - 1 );
    }
    if(Sys.debugView ) System.out.println(orderFormatter() );
    return data;
  }
  

  
  /**
   * Principle Sorting method here, though inner and private
   * @param low - the low pointer
   * @param high -  the high pointer.
   */
  private  void sort(  int low , int high ){
    if(Sys.debugView ) System.out.println(orderFormatter() );
    int pivotIndex; // Array index for the Pivot element
    int n1; // Number of elements before the Pivot element
    int n2; // Number of elements after the Pivot element
//    getPivot1( data )
    if(Sys.debugView ) highF = high; lowF = low;
    int currentK_sort = high - low;
    if (high - low  <=  params.getkStopValue()-1) return;
    else if(low < high){
      int partIndex = partition( low , high );
      if( Sys.debugView ) pivotF = partIndex;
      
      //this is to take care of loops where we get stuck and overflow.  It
      // forces the method to continue.  Often occurs with duplicates.
      if(partIndex == high) {
        partIndex--;
        if(data[partIndex] > data[partIndex+1]) swap( partIndex ,
              partIndex + 1
                                                    );
      }
//      if(partIndex == low){
//          partIndex++;
//        if(data[partIndex-1] > data[partIndex])swap( partIndex-1, partIndex );
//
//      }
      if(partIndex > low )
      sort( low , partIndex );
      if(high > partIndex+1)
      sort( partIndex + 1 , high );
    }
  }
  

  
  
  /**
   * A more complicated version of partition
   * <p>
   * Basically its whole purpose is to let me really peek inside whats going on
   * for debugging.  Had some irritating glitches I had to chase down.
   * <p>
   * This method has many more spots for breakpoints and watches.
   * <p>
   * It also spits out the array and recombines it.
   * <p>
   * All this adds (negligible) overhead.
   *
   * @param lo
   *       the lo pointer
   * @param hi
   *       the hi pointer.
   *
   * @return integer
   */
  Integer partition( int lo, int hi){
    
    //make a local copy- need this simply for debug.  Hava a pesky error.
    //this is for the formatter class;
    if(Sys.debugView )this.highF = hi;
    if(Sys.debugView )this.lowF = lo;
    
    int start = lo,
          end = hi,
          pivotIndex,
          pivot;
    //todo removeme
    
 /*   QSParams.PivotMethods p  = params.getPivotMethod();
          if( p == QSParams.PivotMethods.MEDIAN_OF_THREE||
              p == QSParams.PivotMethods.MEDIAN_OF_THREE_ALT||
              p == QSParams.PivotMethods.MEDIAN_OF_THREE_WITH_SWAPS){
 
            if(Sys.debugView){
//              Integer[] three;
              int mid = ( lo + hi ) / 2;
//              three= new Integer[]{ data[ lo ] , data[mid ] , data[ hi ] };
              System.out.print(String.format( "[%d , %d , %d ]",
                    data[lo], data[mid], data[hi] ));
            }
              pivotIndex = (lo + hi) /2;
              if(data[lo] > data[pivotIndex]) swap( start , pivotIndex );
              if(data[pivotIndex] > data[hi]) swap( pivotIndex , end );
              if( data[ lo ] > data[pivotIndex] ) swap( start , pivotIndex );
//              if(data[hi] < data[pivotIndex])swap( hi, pivotIndex );
//              if(data[lo] > data[pivotIndex]) swap( start , pivotIndex );
//              if(data[hi] > data[pivotIndex])swap( hi, pivotIndex );
              //this ideally puts three values, in order, A B C in the order
            // A C B, suc
          }else*/ pivotIndex = params.getPivot().pivot( data , lo , hi );
    //lookup Pivot Value.
    pivot = data[ pivotIndex ];
    if(Sys.debugView) System.out.println(pivot);
    //this is for rendering in the IDE.
    if(Sys.debugView ) {
      this.pivotF = pivot;
      String s = this.toString();
      System.out.println(s);
    }
    swap( pivotIndex , start );
    partition: while(true){
      
      while(  data[start] <= pivot && start < hi ) start ++ ;
      
      //move the upper pointer while its more than 'Pivot';
      while(data[end] >pivot /*&& end > lo*/) end -= 1;
      
      //if 1 or 0 elements remain, then all the numbers are partioned.
      if(start >= end) break partition;
      
      //the pointers have met or crossed.
      swap(start , end );
      start += 1;
      end -= 1;
      if(Sys.debugView ) System.out.println(this );
    }
    if(Sys.debugView){
      this.pivotF = null;
      this.highF = null;
      this.lowF = null;
      System.out.print(end + "\t");
    }
//    end--;
    swap(lo, end);
    return( end);
  }
  
  
  
  void swap( int posA, int posB){
    Integer temp = data[ posA ];
    data[ posA ] = data[ posB ];
    data[ posB ] = temp;
//    return data;
  }
  
  /**
   * Static version of swap.  For the pivot functional interface need either
   * a static method (w/ data) or to get the instance with get instance.
   * @param data
   * @param posA
   * @param posB
   * @return
   */
  static Integer[] swap( Integer[] data, int posA, int posB){
    Integer temp = data[ posA ];
    data[ posA ] = data[ posB ];
    data[ posB ] = temp;
    return data;
  }
  
  
  
  /**
   *
   * A more complicated version of partition
   *
   * Basically its whole purpose is to let me really peek inside whats going
   * on for debugging.  Had some irritating glitches I had to chase down.
   *
   * This method has many more spots for breakpoints and watches.
   *
   * It also spits out the array and recombines it.
   *
   * All this adds (negligible) overhead.
   *
   * @param lowBound
   * @param highBound
   * @return
   */
  Integer partitionData( int lowBound, int highBound){
    
    //make a local copy- need this simply for debug.  Hava a pesky error.
    Integer[] old_data = data;
    Integer[] temp = new Integer[highBound- lowBound + 1];
    System.arraycopy( data, lowBound, temp, 0, highBound- lowBound +1 );
    data = temp;
    
    int start = 0,
          end = highBound - lowBound,
          length = end,
          pivotIndex,
          pivotValue; ;
    
    pivotIndex = params.getPivot().pivot( data , lowBound , highBound );
    //  pivotIndex = getPivot1(  highBound, lowBound );
    pivotValue = data[ pivotIndex ];
    
    partition: while(true){
      
      while(start < length &&   data[start] < pivotValue){
        start += 1;
      }
      
      //move the upper pointer while its more than 'Pivot';
      while(data[end] >pivotValue){
        end -= 1;
      }
      //this is for debug watching...
//      currentK = end - start;
      
      //if 1 or 0 elements remain, then all the numbers are partioned.
      if(end - start <=  0) {
        
        break partition;
      }else{
        //the pointers have met or crossed.
        swap(start , end );
        start += 1;
        end -= 1;
      }
    }
    
    System.arraycopy( data, 0, old_data, lowBound, highBound- lowBound +1 );
    data = old_data;
    return( end + lowBound);
  }//end partitionData
  
  /**
   * For viewing in the debugger
   * @param pivot the pivot value
   * @param integers the dataArray.
   * @return a string, showing the arrangement.
   */
  static String renderArray(Integer pivot, Integer[] integers){
    String s = "";
    for(Integer i: integers){
      if(i> pivot)s+="+";
      else if(i==pivot) s += "|";
      else s += "-";
    }
    return s;
  }
  
  /**
   * For viewing in the debugger
   * Now using class vars
   * @return a string, showing the arrangement.
   */
  String render(Integer pivot){
    String s = "";
    for(Integer i: data){
      if(i> pivot)s+="+";
      else if(i==pivot) s += "|";
      else s += "-";
    }
    return s;
  }
  /**
   * For viewing in the debugger
   * @return a string, showing the arrangement.
   */
  String format(){
//    Integer highF, lowF;
    StringBuilder s = new StringBuilder();
    if( Objects.nonNull( highF ) &&  Objects.nonNull( lowF )){
  
      for( int j = 0 ; j < data.length ; j++ ){
        if(j == lowF-1) s.append( "/" );
        else if(j==highF+1) s.append( "\\" );
        else if(j<lowF || j > highF) s.append( "." );
        else if(   Objects.nonNull( pivotF )){
          if(data[j] > pivotF) s.append( "+" );
          else if(data[j]==pivotF) s.append( "|" );
          else if(data[j] < pivotF) s.append( "-" );
        }
      }
      return s.toString();
    } else return "initializing...";
  }
  
  String formatter(){
    StringBuilder s = new StringBuilder();
    if( Objects.isNull( highF ) &&  Objects.isNull( lowF ))
      return "initializing...";
      int c = (lowF == 0)? -1 : 0;
      for( ; c < lowF - 1 ; c++ ) s.append( "." );
      for( ; c == lowF - 1 ; c++ ) s.append(c+1).append( ">" );
      if(Objects.isNull( pivotF )){
        for( ; c >= lowF && c <= highF ; c++ ) s.append( "x" );
      } else{
        for( ; c >= lowF && c <= highF  ; c++ ){
          switch( data[c].compareTo( pivotF ) ){
            case(0): s.append( "|" ); break;
            case(1): s.append( "+" );break;
            default: case(-1):s.append("-");break;
          }
        }
      }
    for(;c==highF+1; c++)s.append( "<" ).append(c-1);
    for( ; c > highF + 1 && c < data.length ; c++ ) s.append( "." );
    return s.toString();
  }
  
  
  String orderFormatter(){
    StringBuilder s = new StringBuilder();
    for( int i = 1 ; i < data.length ; i++ ){
      switch(data[i-1].compareTo( data[i] )){
        case(-1): s.append( "o" );break;
        case(0): s.append( "=" );break;
        case(1): s.append( "X" );break;
        default:break;
      }
    }
    return s.toString();
  }
  
  @Override
  public String toString(){
    return formatter();
  }
  
  String oldFormatter(){
    int           c = 0;
    StringBuilder s = new StringBuilder();
    for(Integer i: data){
      if( c == lowF - 1 ) s.append( "/" );
      else if( c == highF + 1 ) s.append( "\\" );
      else if( c < lowF || c > highF ) s.append( "." );
      else if( i > pivotF ) s.append( "+" );
      else if( i == pivotF ) s.append( "|" );
      else s.append( "-" );
      c++;
    }
    return s.toString();
  }
}//end class


