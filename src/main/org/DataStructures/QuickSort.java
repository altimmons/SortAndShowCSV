package org.DataStructures;


public class QuickSort{
  
  Integer[] data;
  
  public QuickSort( RecordSet newData ){
//    this.data = newData;
    int high;
    int low;
    this.data = newData.extract();
    
    Pivot pivot = new PivotK1( newData, 0, newData.getSize() -1 );
    
    //move objects until they are on the right side of the Pivot.
    int newPivotIndex = it_partition( (DataSet)newData , pivot );
//    new QuickSort( new RecordSet( newData.getDescriptor() + " -LOW").add( newData.extract(lowPtr, newPivotIndex) ); )
  
    
  }
  
  public static int it_partition( DataSet dataSet , Pivot pivot ){
    
    
    int pivotValue, start = 0, end= dataSet.getSize() - 1;
    //call Pivot method and get the Pivot.
    
    int pivotIndex = pivot.getPivot( );
    pivotValue = dataSet.getValue( pivotIndex );
    
    
//    Integer[] dataSet = records.getData();
    quicksort: while(true){
      
      //move the lower pointer while its less than 'Pivot';
      while( dataSet.getValue( start ) < pivotValue){
        start += 1;
      }
      
      //move the upper pointer while its more than 'Pivot';
      while( pivotValue < dataSet.getValue(end )){
        end -= 1;
      }
      
      //if 1 or 0 elements remain, then all the numbers are partioned.
      //return 'end'
      if(start >=  end) break quicksort;
        //the pointers have met or crossed.
      else{
        dataSet.swap( start , end );
        start += 1;
        end -= 1;
      }
    }
    return end;
  }

  
  
  /**
   * @param data
   * @param low
   * @param high
   */
  public static void recursivePartition( Integer[] data , int low , int high ){
    int pivotIndex=999; // Array index for the Pivot element
    int n1; // Number of elements before the Pivot element
    int n2; // Number of elements after the Pivot element
    //todo change vars and layout.
    
    if( high > 1 ){
      // Partition the array, and set the Pivot index.
      //todo replace partition method.
//      pivotIndex = partition( data , low , high );
      // Compute the sizes of the two pieces.
      n1 = pivotIndex - low;
      n2 = high - pivotIndex - 1;
      // Recursive calls will now sort the two pieces.
      sortRecursive( data , low , n1 );
      sortRecursive( data , pivotIndex + 1 , n2 );
    }
  }
 

    
    private static void sortRecursive(Integer[] data, int low, int high){
      if(low>= high) return;
//      int lowEnd = partition( data , low , high );
      int lowEnd = ( low + high ) / 2;
      sortRecursive( data , low , lowEnd );
      sortRecursive( data , lowEnd + 1 , high );
    }
  
/*  public static void recursiveQuicksort( Integer[] data, Pivot Pivot){
    //check our partition size, see if indicies have crossed or completed.
    if (highPtr - lowPtr <= 1) return;
    int lowEnd = partitionPlaceholder(data);
    //todo implement me.
  
    sortRecursive( data , lowPtr , lowEnd );
    sortRecursive( data, lowEnd+1, highPtr );
  }
  
 */

  
  
  
    public interface Pivot{
  //    Pivot(Integer[] values , int lowIndex , int highIndex );
      int getPivot( );
    }
    
    public class PivotK1 implements Pivot{
    
      private final int lowPtr;
      private final Integer[] values;
      private final int highPtr;
    
      PivotK1(RecordSet records, int lowBound, int highBound ){
        values      = records.getData();
        lowPtr      = lowBound;
        highPtr     = highBound;
      }
      
      public int getPivot(){
        return lowPtr;
      }
    
  
    }
  }
  
//
//  private static int partition( Integer[] data , int first , int n ){
//    return 0;
//  }
//
//  private static int partitionPlaceholder(Integer[] data){
//    return data.length / 2;
//  }
//  private static int partitionPlaceholder(RecordSet records){
//    return records.getSize() / 2;
//  }
//
//  private static int partitionMid( Integer[] data , int lowPos , int highPos ){
//    int mid = lowPos + ( highPos - lowPos ) / 2;
//    return data[ mid ];
//  }