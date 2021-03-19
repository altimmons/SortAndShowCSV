package org.DataStructures;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;



import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

public class FileGenerator_Test{
  //the path to store the files in...
  
  String customPath =
        Sys.joinPath( Sys.USER_DIR , Sys.resourceRootPath , "custom" );
  
  static final String testFile1 = "res/custom/ran100.dat";
  
  
  @Test
  public void integerIterator( TestReporter testReporter ){
    int count = 1000, min = 0, max = 5000;
    
    int             seed             = (int)( Math.random() * 1000 );
    Random          rng              = new Random( seed );
    Stream<Integer> streamofIntegers = rng.ints( min , max ).boxed();
    Iterator<Integer> itr              = streamofIntegers.iterator();
    int               entered;
    
    boolean success;
    
    
    success = false;
    entered = 0;
    
    for( ; ; ){
      if( entered >= count ){
        success = true;
        break;
      }
      itr.next();
      entered += 1;
      if( ! itr.hasNext() ) break;
    }
    testReporter.publishEntry( "Test 1: " + entered );
    
    assertTrue( success );
  }
  
  @Test
  public void integerIterator2( TestReporter testReporter ){
    int count = 1000, min = 0, max = 5000;
    
    int               seed             = (int)( Math.random() * 1000 );
    Random            rng              = new Random( seed );
    Stream<Integer>   streamofIntegers = rng.ints( min , max ).boxed();
    Iterator<Integer> itr              = streamofIntegers.distinct().iterator();
    int               entered;
    
    boolean success;
    
    
    success = false;
    entered = 0;
    
    
    for( ; ; ){
      if( entered >= count ){
        success = true;
        break;
      }
      itr.next();
      entered += 1;
      if( ! itr.hasNext() ){
        testReporter.publishEntry( "No more values at " + entered );
        break;
      }
    }
    streamofIntegers.close();
    testReporter.publishEntry( "Test 2: " + entered );
    assertTrue( success );
  }
  
  @Test
  public void integerIterator3( TestReporter testReporter ){
    int count = 1000, min = 0, max = 5000;
    
    int             seed             = (int)( Math.random() * 1000 );
    Random          rng              = new Random( seed );
    Stream<Integer> streamofIntegers = rng.ints( min , max ).boxed();
    Iterator<Integer> itr = streamofIntegers.sequential().iterator();
    int entered;
    
    boolean success;
    
    
    success = false;
    entered = 0;
    
    
    for( ; ; ){
      if( entered >= count ){
        success = true;
        break;
      }
      itr.next();
      entered += 1;
      if( ! itr.hasNext() ){
        testReporter.publishEntry( "No more values at " + entered );
        break;
      }
    }
    streamofIntegers.close();
    testReporter.publishEntry( "Test 3: " + entered );
    assertTrue( success );
  }
  
  
  @Test
  public void integerIterator4( TestReporter testReporter ){
    int count = 1000, min = 0, max = 5000;
    
    int             seed             = (int)( Math.random() * 1000 );
    Random          rng              = new Random( seed );
    Stream<Integer> streamofIntegers = rng.ints( min , max ).boxed();
    Iterator<Integer> itr = streamofIntegers.sequential().distinct().iterator();
    int entered;
    
    boolean success;
    
    //4
    success = false;
    entered = 0;
    testReporter.publishEntry( "Test 4" );
    
    //set to true to see the values
    final boolean REPORT = true;
    StringBuilder sb     = new StringBuilder();
    //    int x = itr.next();
    //    if(REPORT) sb.append( x ).append( "\t" );
    
    
    for( ; ; ){
      if( entered >= count ){
        success = true;
        break;
      }
      entered += 1;
      int x = itr.next();
      if( REPORT ) sb.append( x ).append( " " );
      if( ! itr.hasNext() ){
        testReporter.publishEntry( "No more values at " + entered );
        break;
      }
    }
    testReporter.publishEntry( "Test 4: " + entered );
    if( REPORT ) testReporter.publishEntry( sb.toString() );
    assertTrue( success );
  }
  
  @Test
  public void integerIterator5( TestReporter testReporter ){
    int count = 1000, min = 0, max = 5000;
    
    int             seed             = (int)( Math.random() * 1000 );
    Random          rng              = new Random( seed );
    Stream<Integer> streamofIntegers = rng.ints( min , max ).boxed();
    Iterator<Integer> itr =
          streamofIntegers.sequential().map( i -> max - i - 1 ).iterator();
    
    int entered;
    
    boolean success;
    
    //5
    
    success = false;
    entered = 0;
    testReporter.publishEntry( "Test 5" );
    
    for( ; ; ){
      if( entered >= count ){
        success = true;
        break;
      }
      itr.next();
      entered += 1;
      if( ! itr.hasNext() ){
        testReporter.publishEntry( "No more values at " + entered );
        break;
      }
    }
    streamofIntegers.close();
    testReporter.publishEntry( "Test 5: " + entered );
    assertTrue( success );
  }
  
  @Test
  public Integer[] integerIterator6( TestReporter testReporter , int count ){
    int /*count = 10,*/  min = 0, max = 400;
    
    int    seed = (int)( Math.random() * 1000 );
    Random rng  = new Random( seed );
    Stream<Integer> streamofIntegers = rng.ints( count * 2 , min , max ).boxed();
    Iterator<Integer> itr =
          //          streamofIntegers.distinct().sequential().map( i -> max - i - 1 ).iterator();
          streamofIntegers.distinct().sorted().map( i -> max - i - 1 ).iterator();
    
    int entered;
    
    boolean success;
    
    //6
  
    Integer[] results = new Integer[ count ];
    success = false;
    entered = 0;
    testReporter.publishEntry( "Test 6" );
    
    for( int i = 0 ; entered <= count ; i++ ){
      if( entered >= count ){
        success = true;
        break;
      }
  
      int x = itr.next();
      results[ i ] = x;
      System.out.print( x + "\t" );
      entered += 1;
      if( ! itr.hasNext() ){
        testReporter.publishEntry( "No more values at " + entered );
        break;
      }
    }
    streamofIntegers.close();
    testReporter.publishEntry( "Test 5: " + entered );
    assertTrue( success );
    return results;
  }
  
  @Test
  public void repeatIterator( TestReporter testReporter ){
  
    int repeat = 10;
    int count  = 10, min = 0, max = 400;
  
    Double[] results = new Double[ count ];
    String out =
          "Mean Expected: " + String.valueOf( ( max + min ) / 2 ) + " ==>";
    for( int j = 0 ; j < repeat ; j++ ){
  
      int    seed = (int)( Math.random() * 1000 );
      Random rng  = new Random( seed );
      Stream<Integer> streamofIntegers =
            rng.ints( count * 2 , min , max ).boxed();
      Iterator<Integer> itr =
            //          streamofIntegers.distinct().sequential().map( i -> max - i - 1 ).iterator();
            streamofIntegers.distinct().sorted().map( i -> max - i - 1 ).iterator();
  
      OptionalDouble optionalDouble = new Random( seed )
            .ints( count , min , max )
            .distinct()
            .sorted()
            .map( i -> max - i - 1 )
            .average();
      if( optionalDouble.isPresent() ){
        Double rslt = optionalDouble.getAsDouble();
        out += j + ": [" + String.valueOf( rslt ) + "]\t  ";
        //        results[ j ] = rslt;
      }
  
  
    }
    testReporter.publishEntry( out );
  
  
  }
/*
            if(dataType == DataType.DUPLICATE){
        return streamofIntegers.iterator();
      }else if(dataType == DataType.RANDOM){
        return streamofIntegers.distinct().iterator();
      }else if(dataType == DataType.ASCENDING){
        if (allowDups){
          return streamofIntegers.sequential().iterator();
        }else{
          return streamofIntegers.sequential().distinct().iterator();
        }
      }else if(dataType == DataType.DESCENDING){

*/
  
  
  @Test
  public void testStream( TestReporter testReporter ){
    IntStream intStream = IntStream.of( 1 );
    int       min       = 1, max = 1000, count = 100;
    int       range     = max - min;
    int       interval  = range / count;
    int       total     = 0;
  
    int seed = 1;
    if( count < range / 2 ){
      //require a safety factor of 2.
  
  
      long      seed2   = Math.round( Math.random() * 1000 );
      Random    rng     = new Random( seed2 );
      Integer[] results = new Integer[ count ];
      
      for( int i = 0 ; i < count ; i++ ){
        double next    = rng.nextGaussian();
        double scaled  = next * ( interval / 2 );
        int    rounded = (int)Math.round( scaled );
        int    intNext = rounded + ( interval );
        //        total = total + (intNext / count);\
        
        if( intNext < 0 ) continue;
  
        total += intNext;
  
        System.out.println( String.format( "Random Value %f, Scaled %f, then " + "rounded: %d, finally shifted: %d" ,
              next ,
              scaled ,
              rounded ,
              intNext ) );
        results[ i ] = intNext;
      }
  
  
      //      Arrays.stream( results ).average();
  
  
      System.out.println( total / count );
    }
    
    
  }
  
  
  @Test
  public void TestGauss( TestReporter testReporter ){
    Gauss         gauss = new Gauss( 1000 , 0 , 10000 , false );
    StringBuilder sb    = new StringBuilder();
    
    for( int i = 0 ; i < 10000 ; i++ ){
      gauss.getNext();
      if( i % 500 == 0 ) sb.append( gauss.getMean() ).append( "\t" );
    }
    testReporter.publishEntry( sb.toString() );
  }
  
//
//  @Test
//  public void TestGaussCloseRange( TestReporter testReporter ){
//    int           count = 80;
//    Gauss         gauss = new Gauss( count , 0 , 100 , false );
//    StringBuilder sb    = new StringBuilder();
//    int           ones  = 0;
//    int           twos  = 0;
//    for( int i = 0 ; i < count ; i++ ){
//      int r = gauss.getNext();
//      System.out.print( r + "\t" );
//      int n = ( r == 1 ) ? ++ ones : ++ twos;
//      if( i % 500 == 0 ) sb.append( gauss.getMean() ).append( "\t" );
//    }
//    float ratio = twos / ones; //ones/twos;
//    testReporter.publishEntry( String.format( "Ones: %d , Twos: %d, Ratio " + "(2's/1's): %f" ,
//          ones ,
//          twos ,
//          ratio ) + sb.toString() );
//
//  }
//
  
  @Test
  @Disabled
  public void testGenerator(){
  
    try{
      assertTrue( FileGenerator.fileDriver( 200 ,
            0 ,
            5000 ,
            false ,
            DataType.RANDOM ) );
    } catch( FileNotFoundException e ){
      e.printStackTrace();
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
}
  
//  class FileGenProvider implements ArgumentsProvider{
//
//    static Stream<Arguments> arguments = Stream.of(
//    for(int value : values)
//    {
//    {
//      for( DataType dataType : dataTypes ){
//        Arguments.of( value , MIN , MAX , )
//      }
//    }
//}
//
//
//            );
//}

    
    /*
*   //

    @NotNull
    private File getFile( int count , DataType dataType ){
      //generate the file name, like the others...
      String fileName,
            prefix,
            countStr = String.valueOf( count ),
            suffix = ".dat";
      //based on datatype...
      switch( dataType ){
        case DESCENDING:
        case REVERSE:
          prefix = "rev";
          break;
        case DUPLICATE:
          prefix = "dup";
        case ASCENDING:
          prefix = "asc";
          break;
        case UNSPECIFIED:
        case RANDOM:
        default: prefix = "ran";
        break;
      }

      fileName = prefix + countStr + suffix;
      String pathName = Sys.joinPath( customPath , fileName );
      return new File( pathName );
    }



    private boolean checkDir(){

      File customDir = new File(
            Path.of(
                  Sys.USER_DIR.toString() ,
                  Sys.resourceRootPath.toString() ,
            "custom" ).toUri()
      );
      if(!customDir.exists() ||  !customDir.isDirectory()){
        customDir.mkdir();
      }
    return customDir.exists();
      }


  //  @Test
  //  void getCustomTestData(){
  //
  //  }

    @Test
    void driver(){

      int count = 100;
      DataType dataType = DataType.RANDOM;
      boolean over_write = true;
      checkDir();
      File newFile = getFile( count , dataType );



      fileGenTester( count , over_write , newFile , 1 , 10000 );

      count = 50;
      newFile = getFile( count , dataType );
      fileGenTester( count , over_write , newFile , 1 , 100 );

    }

    private void fileGenTester( int count ,
                                boolean over_write ,
                                File newFile ,
                                int min , int max ){
      if(!over_write &&  newFile.exists()){
          //if the file name already exists, then this already exists, so we
          // should skip it.
          System.err.println("This file already exists.  Skipping....");
        }else{
          //      try( //FileOutputStream fileOutputStream

          makeFile( count , min , max , true , DataType.RANDOM , newFile );
        }
      DataSet dataSet = new DataSet( newFile.toPath() );
      assertEquals( count, dataSet.getSize() );
    }


    void makeFile( int count, int min, int max, boolean allowDups,
                   DataType dataType, File newFile){

        PrintWriter printWriter;
        try{
            printWriter = new PrintWriter(
              new BufferedWriter(
                    new FileWriter(
                          newFile
                     )
              )
            );

          int    seed = (int)Math.random() * 1000;
          double f    = Math.random() / Math.nextDown( 1.0 );

          if(dataType == DataType.RANDOM){
            if( ! allowDups || count < ( max - min ) ){
              Random rng = new Random( seed );
              //          int range = max - min;
              //          boolean[] usedValues = new boolean[range];
              int entered = 0;


              for( Iterator<Integer> itr =
                   rng.ints( min , max ).distinct().iterator() ; itr.hasNext() ; ){
                if( entered < count ) break;
                printWriter.println( itr.next() );
                count += 1;
              }

            } else{
              Random    rng       = new Random( seed );
              IntStream intStream = rng.ints( count , min , max );
              //        fileWriter.write( );
              Iterable<String> itStr = (Iterable<String>)intStream.mapToObj( String :: valueOf ) :: iterator;
              for( String s : itStr ){
                printWriter.println( s );
              }
              printWriter.close();
            }
          }else if (dataType == DataType.ASCENDING){
            Random random = new Random();
            random.ints( min, max ).sequential();
          }

        } catch( IOException ioE ){
          ioE.printStackTrace();
        }

        //
      }


    boolean fileMaker(Iterator<Integer> itr, File newFile, int count){

      PrintWriter printWriter;
      boolean success = false;

      try{
        printWriter = new PrintWriter(
              new BufferedWriter(
                    new FileWriter(
                          newFile
                    )
              )
        );

                   int entered = 0;


  //          for(  ; itr.hasNext() ; entered += 1){
  //            if( entered < count ) break;
  //            printWriter.println( itr.next() );
  //          }

  //          for(Integer ct = 0,  i = itr.next();
  //              (itr.hasNext() &&  ct < count);
  //            itr.next() , ct++ );
  //          {
  //            printWriter.println( itr.next() );
  //          }

        for(  ;  ; ){
          if( entered >=  count ) {
            success = true;
            break;
          }
          printWriter.println( itr.next() );
          entered += 1;
          if(!itr.hasNext())break; //error state
        }

        //close file
        printWriter.close();
      } catch( IOException ioE ){
        ioE.printStackTrace();
      }
      return success;
      //
    }



    public boolean fileDriver( int count, int min, int max,
                           boolean allowDups,
                           DataType dataType){
      boolean dirRes = !checkDir();
      if(dirRes)return dirRes;

      File newFile = getFile( count , dataType );
      Iterator<Integer> itr =
            integerIterator( count , min , max , allowDups , dataType );
      fileMaker( itr , newFile , count );

     return fileTester( newFile.toPath() , count );
    }


    public boolean fileTester(Path path, int count){
      if(!(new File(path.toUri()).exists())) return false;
      if(!(new File(path.toUri()).canRead())) return false;
      DataSet dataSet = new DataSet( path) ;
      if(dataSet.getCount() != count) return false;
      if(dataSet.getSize() != count) return false;
      return true;
    }

* */

