package org.DataStructures;


/**
 * enum for the 4 sorting methods in the assignment
 * <p>
 * Select the first item of the partition as the Pivot. Treat a partition of
 * size one and two as a stopping case. • Select the first item of the partition
 * as the Pivot. Process down to a stopping case of a partition of size k = 100.
 * Use an insertion sort to finish. • Select the first item of the partition as
 * the Pivot. Process down to a stopping case of a partition of size k = 50. Use
 * an insertion sort to finish. • Select the median-of-three as the Pivot. Treat
 * a partition of size one and two as a stopping case.
 */
public enum QSParams{
  //numberMethod(pivotMethod, Kstop)
  QS_1ST_K_1_NO_IS( PivotMethods.FIRST_ELEMENT , 1 , false ),
  QS_1ST_K_100_IS( PivotMethods.FIRST_ELEMENT , 100 , true ),
  QS_1ST_K_50_IS( PivotMethods.FIRST_ELEMENT , 50 , true ),
  QS_MID_K_1_NO_IS( PivotMethods.MEDIAN_OF_THREE , 1 , false ),
  QS_MID_K_1_NO_IS_ALT( PivotMethods.MEDIAN_OF_THREE_WITH_SWAPS , 1 , false ),
  QS_MID_WSWAP_K_1_NO_IS( PivotMethods.MEDIAN_OF_THREE_ALT , 1 , false );
  
  /*Functional Interface Pivot*/
  public interface Pivot{
    int pivot( Integer[] data , int low , int high );
  }
  
  /**
   * This is the functional interface implementation that always returns the
   * first value of the array.
   */
  Pivot func_pivotOnFirstElement = ( Integer[] data , int low , int high) ->  {
    return low;
  };
  
  /**
   * This is the functional interface implementation that calculates which of
   * the three values are the median, and returns that index.  The index has
   * to be turned back into a pivot value on the other end to pivot around.
   * This was, in retrospect, a poor design choice, as its un-necessary and
   * redundant.  I thought I would need the index of the pivot, but I never did.
   */
  Pivot func_MedofThree_calcNGo =  ( Integer[] dd , int lo , int hi ) -> {
    int mid = ( lo + hi ) / 2,
          h = dd[ hi ],
          m = dd[ mid ],
          l = dd[ lo ],
          x = Math.max( h , m ),
          y = Math.max( l , m ),
          z =( x == y ) ? Math.max( h , l ) : Math.min( x , y );
    return ( z == h ) ? hi : ( ( z == m ) ? mid : lo );
  };
  
  /**
   * Functional Endpoint of Pivot interface
   *
   * @see Pivot
   * @see  Pivot func_MedofThree_calcNGo
   * This is the functional interface implementation that calculates which of
   * the three values are the median, and returns that index. when I was looking
   * into some Quicksort history, I saw some discussion on the pivot, stating
   * with the *pseudomedian of nine*, which converted into the median of three.
   *
   * There they went ahead and made the swaps.  If you are going to do the
   * comparisons, you may as well swap now. This is just a few fewer swaps to
   * make in the long run.
   *
   * Then the question was, do you swap the median to be in the middle:
   * as this method (function?) does, or do you put it at the end?
   *
   * Cont {@link QSParams.Pivot}
   */
  Pivot func_MedofThree_swapMid = ( Integer[] data , int low , int high ) ->  {
    QSort qsInstance = QSort.getInstance();
    int mid = ( low + high ) / 2;
    if(data[mid] < data[low]) qsInstance.swap( low , mid );
    if(data[high] < data[low]) qsInstance.swap(low , high );
    if(data[high] < data[mid]) qsInstance.swap(mid , high );
    return mid;
  };
  
  /**
   * This discussion begins wiht the other functional methods.
   *
   * We are swapping elements now for efficiency.  Calculate the median and
   * swap at the same time.
   *
   * It seemed at first to store the median in the middle of the data
   * array- which makes intuitive sense, but at the same  time the placement is
   * rather ambiguous (what if > 50% of the elements are less than the pivot),
   * something that should presumably happen more than half the time (since
   * the pivot is >= and the low side is just < ).
   *
   * The solution to that, is to go ahead an put the pivot on the high side,
   * where it will end up in the vast majority of cases.
   *
   * I believe that this makes sense as the most efficient method.
   *
   * @see  Pivot func_MedofThree_calcNGo
   * @see   Pivot func_MedofThree_swapMid
   *
   */
  Pivot func_MedofThree_swapHi =
        ( Integer[] data , int low ,  int high ) ->
        {
          QSort qsInstance = QSort.getInstance();
          int mid = ( low + high ) / 2;
          if( qsInstance.data[low] >  qsInstance.data[mid])qsInstance.swap( low , mid );
          if(qsInstance.data[mid] > qsInstance.data[high]) qsInstance.swap( mid, high );
          if( qsInstance.data[low] >  qsInstance.data[mid])qsInstance.swap( low , mid );
  
//          if(qsInstance.data[mid] < qsInstance.data[low]) qsInstance.swap( low , mid );
//          if(qsInstance.data[high] < qsInstance.data[low]) qsInstance.swap(low , high );
//          if(qsInstance.data[mid] < qsInstance.data[high]) qsInstance.swap(mid , high );
          return mid;
          
        };
  
        

  /*Enum Pivot Methods-
  * The defined Pivot Types*/
  enum PivotMethods{
    FIRST_ELEMENT, MEDIAN_OF_THREE, MEDIAN_OF_THREE_WITH_SWAPS,
    MEDIAN_OF_THREE_ALT;
  }
  
  
  //  private final int pivotMethod;
  private final int          kStopValue;
  private final boolean      doInsertSort;
  private final PivotMethods pivotMethod;
  
  
  /**
   * The default Constructor for this Enum.
   *
   * @param pivotMethod
   * @param kStopValue
   * @param doInsertionSort
   */
  private QSParams( PivotMethods pivotMethod ,
                    int kStopValue ,
                    boolean doInsertionSort )
  {
    this.pivotMethod  = pivotMethod;
    this.kStopValue   = kStopValue;
    this.doInsertSort = doInsertionSort;
  }
  
 PivotMethods getPivotMethod(){
   return pivotMethod;
 }
  
  
  Pivot getPivot(){
    switch( pivotMethod ){
      case MEDIAN_OF_THREE:
        return func_MedofThree_swapHi;
      case MEDIAN_OF_THREE_ALT:
        return func_MedofThree_swapMid;
        
      case MEDIAN_OF_THREE_WITH_SWAPS:
        return func_MedofThree_calcNGo;
      default:
      case FIRST_ELEMENT:
        return func_pivotOnFirstElement;
    }
  }
  
  
  
  int getkStopValue(){
    return kStopValue;
  }
  
  
  /**
   * Getter for property 'doInsertSort'.
   *
   * @return Value for property 'doInsertSort'.
   */
  public boolean doInsertSort(){
    return doInsertSort;
  }
  
  public String toString(){
    return String.format( "Pivot: %s, K-limit %d, Insertion Sort: %B:" ,
          this.pivotMethod.name().toString() ,
          this.kStopValue ,
          this.doInsertSort
                 );
//    switch(this){
//      case ONE: return "Pivot on First Element, k limit = 1, Insertion Sort ="
//                       + " false";
//      case TWO: return "";
//      case THREE: return "";
//      case FOUR: return "";
//      case FOURWSWAPS: return "";
//    }
  }
}