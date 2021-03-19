package org.DataStructures;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InsertionSort_Test{
  
  
//  private final static DataSet[] dataSets =
//        App.parse( App.getDataPath() ).extract();
  private static       DataSet[] dataSets;
  
  @BeforeAll
    public static void getData(){
      GArrayList<DataSet> dataSetList = App.getDataSets_silent();
    dataSets = dataSetList.extract();
      //    dataSets = new GArrayList<>( DataSet.class );
  //    dataSets = App.parse( App.getDataPath() ).extract();
//      return dataSets;
    }
  //
  
  @ParameterizedTest
  @ValueSource( ints = { 0 , 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 , 11 , 12
        , 13 , 14 , 15 , 16 , 17 , 18 , 19 , 20, 21 , 22  /*, 24 , 25 , 26 ,
        27 , 28 , 29 , 30*/} )
//  @Disabled

  void testInsertSort( int ints , TestReporter testReporter ){
    int                 i           = ints;
    System.out.println( "OK" );
    
    //    i += ints;
    final Boolean FAILONDUPLICATE= false;
    DataSet             curSet      = dataSets[ i ];
    testReporter.publishEntry( curSet.getDescriptor() );
    Integer[] result = new Integer[ curSet.getSize() ];
    result = InsertionSort.sort( curSet.extract() );
    int count = 0;
    StringBuilder sb = new StringBuilder();
    for( int j = 1 ; j < result.length ; j++ ){
      if( curSet.getDataType() ==  DataType.DUPLICATE ){
        //remove the equals condition.
        if( ! ( result[ j - 1 ] <= result[ j ] ) ){
  
          sb.append( "1" );
  
  
          testReporter.publishEntry(
                String.format(
                      "Test failed at position data[%d] = %d and data[%d] = " + "%d" ,
                      j - 1 ,
                      result[ j - 1 ] ,
                      j ,
                      result[ j ] ) );
          System.out.println(".");
        }else{
          if(Sys.debugView )sb.append( "." );
        }
        assertTrue( result[ j - 1 ] <= result[ j ] );
      } else{ //not duplicate
        if( ! ( result[ j - 1 ] <= result[ j ] ) ) {
          sb.append( "1" );
          testReporter.publishEntry(
                String.format(
                      "Test failed at position data[%d] = %d and data[%d] = " + "%d" ,
                      j - 1 ,
                      result[ j - 1 ] ,
                      j ,
                      result[ j ] ) );
  
          System.out.println("");
        }else{
          sb.append( "." );
        }
//        if(FAILONDUPLICATE) assertTrue( result[ j - 1 ] < result[ j ] );
//        else  assertTrue( result[ j - 1 ] <  result[ j ] );
      }
      count++ ;
    }
    testReporter.publishEntry( sb.toString() );
//    testReporter.publishEntry( String.format(
//          "All tests passed, all %d values checked for %d " + "comparisons, "
//          + "of %s \n %s" ,
//          result.length ,
//          count ,
//          curSet.getDescriptor() ,
//          Sys.formatIntArray( result )
//                                            ) );
  }
  
  /**
   * TestAll the given datasets
   * @param testReporter - the testReporter object.
   */
  @Test
  void testAll( TestReporter testReporter ){
    final Boolean FAILONDUPLICATE= false;
    GArrayList<DataSet> dataSetList = App.getDataSets_silent();
    DataSet[]           dataSets    = dataSetList.extract();
    int count = 0;
    for( DataSet curSet : dataSets ){
      testReporter.publishEntry( curSet.getDescriptor() );
      Integer[] result = new Integer[ curSet.getSize() ];
      result = InsertionSort.sort( curSet.extract() );
   
      for( int j = 1 ; j < result.length ; j++ ){
        if( curSet.getDataType() == DataType.DUPLICATE ){
          //remove the equals condition.
          if( ! ( result[ j - 1 ] <=  result[ j ] ) ) testReporter.publishEntry(
                String.format(
                      "%s:Test failed at position data[%d] = %d and data[%d] ="
                      + " " + "%d" , curSet.getDescriptor(),
                      j - 1 ,
                      result[ j - 1 ] ,
                      j ,
                      result[ j ] ) );
          assertTrue( result[ j - 1 ] <= result[ j ] );
        } else{
          //notify of illicit duplicates.
          if(result[ j - 1 ] == result[ j ] ) testReporter.publishEntry(
                String.format(
                      "%s: Unexpected Duplicate at position data[%d] = %d and "
                      + "data[%d]"
                      + " = " + "%d...continuing." ,
                      curSet.getDescriptor(), j - 1 ,
                      result[ j - 1 ] ,
                      j ,
                      result[ j ] ) );
  
          if( ! ( result[ j - 1 ] < result[ j ] ) ) testReporter.publishEntry(
                String.format(
                      "%s:Test failed at position data[%d] = %d and data[%d] ="
                      + " " + "%d" , curSet.getDescriptor(),
                      j - 1 ,
                      result[ j - 1 ] ,
                      j ,
                      result[ j ] ) );
          if(FAILONDUPLICATE) assertTrue( result[ j - 1 ] < result[ j ] );
          else  assertTrue( result[ j - 1 ] <=  result[ j ] );
        }
        count++;
      }
  
    }
    testReporter.publishEntry( String.format(
          "All tests passed, all values checked for %d " + "comparisons, "
          + "of" + " %d Sets" ,
          count ,
          dataSets.length) );
  }
  
}
  
