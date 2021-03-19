package org.DataStructures;


import org.DataStructures.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class QSort_Test{
  
  
  @Test
  void testQsort(){
    GArrayList<DataSet> sets = App.getDataSets_silent();
    //    sets.spliterator().
    //    HashSet<DataSet> setToCheck = {
    //
    //    }
    for( DataSet set : sets ){
      //      if()
    }
  
  }
  
  @ParameterizedTest
  @Disabled
  @ValueSource( strings = {"res/short/ran20.dat",
        "res\\custom\\asc50-u.dat",
        "res\\custom\\asc25-u.dat",
        
  } )
  public void testSort(){
    String   filename   = "res/short/rev50.dat";
    Path     p          = Path.of( filename );
    try{
      DataSet data = new DataSet( p );
//    QSParams paramToUse = QSParams.values()[ 1 ];
      QSParams[] paramToUse = {
            QSParams.QS_MID_K_1_NO_IS
      };
      for(QSParams params : paramToUse){
        QSort qSort = new QSort( data , params);
      }
    }catch( FileNotFoundException fnfE ){
      System.err.println( fnfE.getMessage() );
      fail( "File not found");
    }
    //    Reporter.getResultStore().extract()[1]
  }
  
  
  
  @Test
  public void testSortM0( TestReporter testReporter ){
    Sys.debugView = true;
//    String filename = "res/short/ran20.dat";
//    String filename = "res\\custom\\asc50-u.dat";
//    String filename = "res\\custom\\asc25-u.dat";
//    String filename = "res\\custom\\asc25-u.dat";
    String filename = "res\\input\\dup500.dat";
    Path f = Path.of( filename );
    
    try{
      DataSet data = new DataSet( f );

//    QSParams[] paramToUse = QSParams.values();
    QSParams[] paramToUse = {
          QSParams.QS_MID_K_1_NO_IS,
          QSParams.QS_MID_K_1_NO_IS_ALT,
          QSParams.QS_MID_WSWAP_K_1_NO_IS
    };
    boolean testResult = false;
    for(QSParams p : paramToUse){
      
      QSort qSort = new QSort( data, p );
      Integer[] result = qSort.sort();
  
      testReporter.publishEntry( Sys.formatSet( result ) );
      if(p.doInsertSort()){
        Integer[] result2 = InsertionSort.sort( result );
        testReporter.publishEntry( Sys.formatSet( result2 ) );
        assertTrue(
              verifyResult( result2 , data));
      }else {
        //dont do insetion sort
        assertTrue(
        verifyResult( result , data ));
      }
      
    }
  
    }catch( FileNotFoundException fnfE ){
      System.err.println( fnfE.getMessage() );
      fail( "File not found");
    }
    
    //    Reporter.getResultStore().extract()[1]
  }
  
  

  
  
  public static void testSortM3( int n ){
    
    String filename;
    switch( n ){
      case 1: filename = "res/custom/ran20.dat";
        break;
      default: filename = "res/custom/ran200.dat";
      
    }
    Path   f    = Path.of( filename );
    try{
      DataSet data = new DataSet( f );
    
    InsertionSort.sort( data.extract() );
    //    QSort   qSort    = new QSort( data );
    //    qSort.sort();
  }catch( FileNotFoundException fnfE ){
    System.err.println( fnfE.getMessage() );
    fail( "File not found");
  }
  }
  
  
  public boolean verifyResult( Integer[] result , DataSet dataSet ){
    /*Operational Settings*/
    final boolean FAILONDUPLICATE = false;
    final int     MSGMAX          = 20;
    
    /*Initializers*/
    int comparisonCount = 0, msgCount = 0;
    
    
    for( int j = 1 ; j < result.length ; j++ ){
      if( dataSet.getDataType() == DataType.DUPLICATE ){
        /*DUPLICATES*/
        //remove the equals condition.
        if( ! ( result[ j - 1 ] <= result[ j ] ) )
          if( Sys.DEBUG_EN && msgCount < MSGMAX ){
            
            System.out.format(
                  "\t\t\t%s:Test failed at position data[%d] = %d and " + "data[%d] =" + " " + "%d(!)\n" ,
                  dataSet.getDescriptor() ,
                  j - 1 ,
                  result[ j - 1 ] ,
                  j ,
                  result[ j ] );
            msgCount++;
          }
        if( ! ( result[ j - 1 ] <= result[ j ] ) ) return false;
      } else{
        /*ALL OTHER TYPES*/
        //notify of illicit duplicates.
        if( result[ j - 1 ] == result[ j ] ){
          if( Sys.DEBUG_EN && Sys.verbose && msgCount < MSGMAX ){
            System.out.format(
                  "\t\t\t%s: Unexpected Duplicate at position data[%d] = %d and " + "data[%d]" + " = " + "%d...continuing.\n" ,
                  dataSet.getDescriptor() ,
                  j - 1 ,
                  result[ j - 1 ] ,
                  j ,
                  result[ j ] );
            msgCount++;
          }
          if( FAILONDUPLICATE ) return false;
        }
        if( ! ( result[ j - 1 ] < result[ j ] ) ){
          if( Sys.DEBUG_EN && Sys.verbose && msgCount < MSGMAX ){
            System.out.format(
                  "\t\t\t%s:Test failed at position data[%d] = %d and data[%d] =" + " " + "%d  (@)\n" ,
                  dataSet.getDescriptor() ,
                  j - 1 ,
                  result[ j - 1 ] ,
                  j ,
                  result[ j ] );
            msgCount++;
          }
        }
        if( FAILONDUPLICATE ){
          if( ! ( result[ j - 1 ] < result[ j ] ) ) return false;
        } else{
          if( ! ( result[ j - 1 ] <= result[ j ] ) ){
            //          System.out.println( "This is returning False" );
            return false;
          }
        }
      }
      comparisonCount++;
    }
    if( Sys.DEBUG_EN && Sys.verbose ) System.out.printf( comparisonCount + " comparisons " + "completed \n" );
    
    
    return true;
  }
}
  
