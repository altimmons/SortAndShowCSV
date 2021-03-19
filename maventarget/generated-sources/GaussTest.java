package org.DataStructures;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Gauss Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>May 18, 2020</pre>
 */
public class GaussTest{
  final int[] values = { 50 , 500 , 1000 , 2000 , 5000 };
  final int   MIN    = 0, MAX = Integer.MAX_VALUE;
  final DataType[] dataTypes = {
                      DataType.DUPLICATE ,
                      DataType.ASCENDING ,
                      DataType.RANDOM ,
                      DataType.REVERSE
       };
  private static Gauss gauss;
  static TestReporter testReporter;
  
  //  Gauss(int ct, int min, int max, boolean dups){
  
  
@Test
//@Disabled

public  void initalTest(TestReporter testReporter){
  int count = 100;
  int max =1000;
  boolean successful = false;
  while(!successful){
    int[] value = new int[ count ];
    int[] delta = new int[ count ];
    gauss = new Gauss( count , 0 , max , false );
    int out = 0;
    for( int i = 0 ; i < count && gauss.hasNext() ; i++ ){
      int newVal = gauss.getNextIncrement();
      out += newVal;
                   
               
      value[ i ] = out;

//      if(gauss.hasNext())
//      successful = gauss.hasNext() || (out < max) || (i >= count);
      successful = (gauss.hasNext() || (i >= count -1)) && (out < max);
      
    }
    System.out.println( "At 90" );
  
    testReporter.publishEntry( Sys.formatSet( count, value ) );
  }
  assertEquals( 1 , 1 );
}
  

  
  
  @Test
@DisplayName( "Gaussian Generator with Progressive ALPHA decrement "
              + "(Production Technique)" )
  public  void initalTest2WithAlpha(TestReporter testReporter){
    boolean view = true;
    int count = 1000;
    int max =10000;
    int runCount = 0;
    boolean successful = false;
    double alpha = 1.0;
    int[] value=new int[50];
    do{
      value = new int[ 50 ];
      alpha -= 0.01;
      runCount += 1;
      if( alpha < 0 ) fail( "More Attempts than Expected" );
    
      gauss = new Gauss( count , 0 , max , false );
      int out = 0;
      gauss.allowSoftMax();
      while( gauss.hasNext() ){
        int next = gauss.getNext();
       if(count > 950) value[ ( gauss.returnedCount -1 ) % 50 ] = next;
      }
    } while( gauss.isUnsuccessfulResult() );
//      for( int i = 0 ; i < count && gauss.hasNext() ; i++ ){
//        int newVal = gauss.getNext();
//        if(i> (count -50)) {
//          value[ ( i - ( count -50)) ] = newVal;
//        }
//        successful = (gauss.hasNext() || (i >= count -1)) && (newVal < max);
//      }
      if(view) testReporter.publishEntry( String.format( "Run Count: %d, "
                                                         + "Ending "
                                                         + "Alpha"
                                                         + " %f",
            runCount, alpha
                                                       ) );
      testReporter.publishEntry( Sys.formatSet( 50, value ) );
    testReporter.publishEntry( String.format( "Run Count: %d, Ending Alpha %f",
          runCount, alpha
                                            ) );
  
    for( int i = 1 ; i < value.length ; i++ ){
      assertTrue( value[i] != 0 );
//      assertTrue( value[i] <=max );
      assertTrue( value[i-1]<value[i] );
    }
    
}
  
  
  @Test


  public  void modifiedGetGauss(TestReporter testReporter){
    int count = 10000;
    int max =100000;
    int runCount = 0;
    double alpha = 1.0;
    int[] value = new int[ (int)( count * 0.01) ];
    do{
      if(alpha < 0) fail( "More Attempts than Expected" );
      gauss = new Gauss( count , 0 , max , false );
      gauss.setAlpha( alpha-=0.01 ).setFirmMax();
      runCount += 1;
      int out = 0;
      while(gauss.hasNext()){
        out += gauss.getNextIncrement();
        //          value[ (gauss.returnedCount % (count/10)) ] = if(i> (count - count/10)) value[ (int)( i - ( count * 0.9)) ] = newVal;;;
        if(gauss.returnedCount> ((int)( count * 0.9)))
          value[ (int)( gauss.returnedCount % (count/100) ) ] = out;
          
      }
  
      testReporter.publishEntry( String.format( "[%B] - Run Count: %d, Ending"
                                                + " " + "Alpha %f" ,
            gauss.isSuccessfulResult(),
            runCount ,
            alpha ) );
      testReporter.publishEntry( String.valueOf(gauss.isSuccessfulResult()) );
      
      testReporter.publishEntry( Sys.formatSet( count, value ) );
      
    } while( !gauss.isSuccessfulResult() );
//
//    testReporter.publishEntry( Sys.formatSet( count , gauss.getArray() ) );
//    testReporter.publishEntry( Sys.formatSet( count ,
//          gauss.getReverseArray() ));
    
    for( int i = 0 ; i < value.length ; i++ ){
      assertNotEquals( 0, value[i] );
    }
  }
  
  @Test

@Disabled
  public void testGenerator2(){
    try{
      assertTrue( FileGenerator.fileDriver( 20 ,
            0 ,
            100 ,
            false ,
            DataType.RANDOM ) );
    } catch( FileNotFoundException e ){
      e.printStackTrace();
    }
  }

//  public void assignmentGenerator(){
//
//    int[] values = { 50 , 500 , 1000 , 2000 , 5000 };
//    int   MIN    = 0, MAX = Integer.MAX_VALUE;
//    DataType[] dataTypes = {
//          DataType.DUPLICATE ,
//          DataType.ASCENDING ,
//          DataType.RANDOM ,
//          DataType.REVERSE;
  
  /**
   * Test adjust.
   */
  @Disabled
  void testAdjust(){
  
    Gauss gauss = new Gauss( 1000 , 0 , 10000 , true , 0.95 );
    for( int i = 0 ; i < gauss.returnedCount ; i++ ){
      if(Math.round(  gauss.allDoubleVales[i] )!=  Math.round(  gauss.allDoubleVales_PS[i] ) ){
        System.out.format( "On number %d, %d changed to %d" ,
              i,
              Math.round(  gauss.allDoubleVales[i] ),
              Math.round(  gauss.allDoubleVales_PS[i] ));
      }
    }
  }
  }
