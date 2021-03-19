package org.DataStructures;


public class InsertionSort{
  
  
  
  public static Integer[] sort (Integer[] data   ){
    int l = data.length - 1;
    return sort(data, 0, l );
  }
  
  
//  /**
//   * A terse version of below
//   * @param data
//   * @param f
//   * the initial value- first value in the set, f= forward cursor
//   * @param l
//   * - the length of the data to sort.
//   * <code>b</code> = backwards cursor.
//   */
//  private static void sort( Integer[] data , int f , int l ){
//    for ( int t,b ; f <=  l; f++ ) {
//      for( t = data[f], b = f; b > 0 &&  data[b-1] > data[f]; b-- ){
//        data[b] = data[b-1];
//      }
//
//      data[b] = t;
//    }
//  }
  
  
  /** sort input data
   *@param data - input array to be sorted in-place
   */
  public static Integer[] sort(Integer[] data, int initial, int endpoint){
    for (int forwardPtr=initial; forwardPtr <= endpoint; forwardPtr++ ) {
      int selectedValue = data[forwardPtr];
      int backCursor = forwardPtr;

      while(backCursor > initial && data[backCursor-1] > selectedValue) {
        data[backCursor] = data[backCursor-1];
        backCursor--;
      }
      data[backCursor] = selectedValue;
    }
    return data;
  }
  
}
