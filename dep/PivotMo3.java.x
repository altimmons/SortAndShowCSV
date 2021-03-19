package org.DataStructures;

public class PivotMo3 implements Pivot{
    /**
     * The more obvious Median of Three method.
     *
     * @param hi
     * @param lo
     *
     * @return
     */
    
    
  //  static class PivAlternate extends PivotMo3 implements  Pivot {
    @Override
    public int pivot( Integer[] dd , int lo , int hi ){
    int mid = ( lo + hi ) / 2,
          h   = dd[ hi ],
          m = dd[ mid ],
          l = dd[ lo ],
          x =  Math.max( h , m ),
          y =  Math.max( l , m ),
          z = (x==  y) ? Math.max(h,l) :Math.min(x,y);
    return ( z == h ) ? hi : ( ( z == m ) ? mid : lo );

  }
  
//  static int pivot( Integer[] dd , int lo , int hi ){
//    int mid = ( lo + hi ) / 2,
//          h   = dd[ hi ],
//          m = dd[ mid ],
//          l = dd[ lo ],
//          x =  Math.max( h , m ),
//          y =  Math.max( l , m ),
//          z = (x==  y) ? Math.max(h,l) :Math.min(x,y);
//    return ( z == h ) ? hi : ( ( z == m ) ? mid : lo );
//
//  }

  
    
    class PivAlt extends PivotMo3 implements Pivot  {
    @Override
    public int pivot( Integer[] data , int low , int high ){
//      QSort qsInstance = QSort.getInstance();
      int mid = ( low + high ) / 2;
      if(data[mid] < data[low]) QSort.swap(data, low , mid );
      if(data[high] < data[low]) QSort.swap(data, low , high );
      if(data[mid] < data[high]) QSort.swap(data,  mid , high );
      return high;
    }
  }
  
  
  
  /**
   * This one operates on the instance.  It goes ahead with the swaps, but
   * puts the pivot in the middle to start.
   */
  class PivAlt2 extends  PivAlt implements Pivot {
    @Override
    public int pivot( Integer[] data , int low , int high ){
      QSort qsInstance = QSort.getInstance();
      int mid = ( low + high ) / 2;
      if(data[mid] < data[low]) qsInstance.swap( low , mid );
      if(data[high] < data[low]) qsInstance.swap(low , high );
      if(data[high] < data[mid]) qsInstance.swap(mid , high );
      return mid;
    }
  }
  
  /**
   * This one proceeds with the pivot swaps.  but it puts the pivots in the
   * middle./
   */
  class PivAlt3 extends PivotMo3 implements Pivot {
    @Override

  }
  

}

//    if(z== h) return high;
//    else if(z == m) return mid;
//    else return low;
//    Math.max( high , Math.max( mid , low ) );
//    int a = ( high >= mid ) ? high : mid;
//    int b = ( low >= mid ) ? low : mid;
//    return ( a <= b ) ? a : b;


//    if(data[mid] < data[low]) QSort.swap(data, low , mid );
//    if(data[high] < data[low]) QSort.swap(data, low , high );
//    if(data[mid] < data[high]) QSort.swap(data,  mid , high );