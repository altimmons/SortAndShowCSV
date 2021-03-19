package org.DataStructures;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings( "AssignmentToStaticFieldFromInstanceMethod" )
public class RecordSetTest

{
  public static RecordSet recordSet;
  
  
  @BeforeAll
  static void init(){
    recordSet = new RecordSet( "staticRecord" );
  }
  
  @Test
  void testSize(){
    RecordSet r = new RecordSet( "Test Record." );
    assertEquals( 0 , r.getSize() );
    r.add( 4 );
    assertEquals( 1 , r.getSize() );

  }
  
  
  /*This fails (in Maven) when using the static class field ref.  But
  succeeds when called locally.
  Commenting out the first line below should have no effect.  But it does.*/
  @Test
  void testGetValue(){
    recordSet = new RecordSet( "local" );
    recordSet.add( 4 );
    assertEquals( 4 , recordSet.getValue( 0 ) );
    //RecordSetTest.testGetValue:32 expected: <4> but was: <1>
  }
  
  @Test
        void testGetValue2(){
    recordSet = new RecordSet( "local" );
    
    recordSet.add( 4 );
    recordSet.add( 99 );
    assertEquals( 2 , recordSet.getSize() );
    assertEquals( 99 , recordSet.getValue( 1 ) );
    //RecordSetTest.testGetValue2:40 expected: <2> but was: <8>

  }
  
  @Test
  void chainedAdd(){

    recordSet.add( 5 ).add( 2 ).add( 0 ).add( 19 ).add( 21 ).add( 7 );
    assertEquals( 6 , recordSet.getSize() );
    recordSet = new RecordSet( "test2" ).add( 1 ).add( 2 ).add( 3 ).add( 4 ).add( 5 );
    assertEquals( 5 , recordSet.getSize() );
  }
  
  /**
   * Test is sometimes problematic.  Think its an issue with JUnit5 and
   * parallelizing.
   * Sometimes the pre-initialized dataset with 1 value, and 6 added fails
   * when assertEquals(7, 6+1).  Other scenarios it loses that 1, and 6 is
   * the answer.  Tested this pretty extensively - odd behaviour.  7  is
   * correct.  No bugs running out side of JUnit.
   *
   * @see <code>method:</code>RecordSetTest.testGetValue()
   */
  @Test
  void arrayAdd(){
    Integer[] integerArray = new Integer[]{ 1 , 2 , 3 , 4 , 5 };
    int[] intArray = { 1 , 2 , 3 , 4 , 5 };
    recordSet.add( 5 , 5 , 3 , 2 , 1 , 5 );
    assertEquals( 7 , recordSet.getSize() );
    recordSet = new RecordSet( "test2" ).add( integerArray );
    assertEquals( 5 , recordSet.getSize() );
    recordSet = new RecordSet( "test2" ).add( intArray );
    assertEquals( 5 , recordSet.getSize() );
  }
  
  @Test
  void testRecord(){
    RecordSet r = new RecordSet( "Test Record." );
    assertEquals( 0 , r.getSize() );
    r.add( 4 );
    assertEquals( 1 , r.getSize() );
    assertEquals( 4 , r.getValue( 0 ) );
    r.add( 5 ).add( 2 ).add( 0 ).add( 19 ).add( 21 ).add( 7 );
    assertEquals( 7 , r.getSize());
    r = new RecordSet( "test2" ).add( 1 ).add( 2 ).add( 3 ).add( 4 ).add( 5 );
    assertEquals( 5 , r.getSize() );
  }
  

  @Test
  void testStream(){
    final int SEED = 999,
          MIN = 0,
          MAX = Integer.MAX_VALUE,
          COUNT = 10000;
    RecordSet r = new RecordSet( "testStream" );
//    IntStream intStream = new Random( SEED ).ints( MIN , MAX );
    Random rng = new Random(SEED);
    for( int i = 0 ; i < COUNT ; i++ ){
      r.add( rng.nextInt() );
      
    }
//    r.add(
//    intStream.boxed().toArray( Integer[]::new )
//         );
  
  
  
  }
  
  @Test
  void testDescriptor(){
    final String NAME ="DESCRIPTOR_TEST";
    RecordSet    rr   = new RecordSet( NAME );
    rr.add( 1 );
  }
}
