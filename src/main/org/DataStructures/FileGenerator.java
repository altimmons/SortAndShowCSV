package org.DataStructures;



import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FileGenerator{
 
 static String customPath = Sys.joinPath( Sys.USER_DIR , Sys.resourceRootPath ,
       "custom" );
  static       Stream<Integer> stream;
  static final int MIN = 0;
  static final int MAX = Integer.MAX_VALUE;
  

  
  /**
   * Generates the files needed for homework, given here:
   *
   * Create input files of four sizes: 50, 500, 1000, 2000 and 5000 integers.
   * For each size file make 3 versions. On the first use a randomly ordered
   * data set. On the second use the integers in reverse order. On the third
   * use the
   * integers in normal ascending order. (You may use a random number
   * generator to create the randomly ordered file, but it is important to
   * limit the duplicates to <1%. Alternatively, you may write a shuffle
   * function to randomize one of your ordered files.) This means you have an
   * input set of 15 files plus whatever you deem necessary and reasonable.
   * The size 50 files are to be turned in. The other sizes are only for
   * timing purposes. Your code needs to print out the sorted values. Files
   * are available at on the course web page if you want to copy them. Your
   * data should be formatted so that each number is on a separate line with
   * no leading blanks. There should be no blank lines in the file. Each sort
   * must be run against all the input files. With five sorts and 15 input
   * sets, you will have 75 required runs.
   *
   * four sizes: 50, 500, 1000, 2000 and 5000
   */
 public static void main() throws FileNotFoundException{
 
//   int[] sizes = { 20 , 50 , 100 , 500 , 1000 , 5000 , 10000 , 50000 };
   int[] sizes = { 50 , 500 , 1000 , 2000 , 5000 };
   DataType[] tps = { DataType.RANDOM , DataType.REVERSE , DataType.ASCENDING };
   for(DataType type : DataType.values()){
     for(int sz : sizes){
      //MAKE FILE...
       fileDriver( sz , MIN , MAX , ( type == DataType.DUPLICATE ) , type );
     }
   }
 }
  
 
 
 
  
  /**
   * File maker boolean.
   *
   * @param itr
   *       the stream which will produce our values.
   * @param newFile
   *       the new file to write to
   * @param count
   *       the count of the values to write
   *
   * @return the boolean if successful (true)
   */
  public static boolean fileMaker( Iterator<Integer> itr, File newFile, int count ){
    
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
  
  
  
  
  /**
   * The file creation method, drives the creation of the file.
   * @param count - the count of records to generate
   * @param min minimum allowed values
   * @param max - maximum allowed values
   * @param dataType - enum DataType
   * @return returns true if successful.
   * returns false BOTH if the file already exists, and if the file creation
   * fails.
   */
  public static boolean fileDriver( int count, int min, int max,
                             boolean allowDups,
                             DataType dataType) throws FileNotFoundException
  {
    boolean dirRes = !checkDir();
    if(dirRes)return dirRes;
    
    File newFile = getFile( count , dataType, "-u" );
    if(!newFile.exists()){
      Iterator<Integer> itr = getSpecifiedIntegerIterator( count ,
            min ,
            max ,
            allowDups ,
            dataType );
      fileMaker( itr , newFile , count );
      return fileTester( newFile.toPath() , count );
    }else{
      return false;
    }
  }
  
  
  /**
   * The file creation method, drives the creation of the file.
   * @param count - the count of records to generate
   * @param min minimum allowed values
   * @param max - maximum allowed values
   * @param dataType - enum DataType
   * @return returns true if successful.
   * returns false BOTH if the file already exists, and if the file creation
   *    fails.
   */
  public static boolean gaussFileDriver( int count, int min, int max,
                                    DataType dataType)
        throws FileNotFoundException
  {
    boolean dirRes = !checkDir();
    if(dirRes)return dirRes;
    
    Gauss gauss;
    File newFile = getFile( count , dataType, "-ug" );
    if(!newFile.exists()){
      int    runCount = 0;
      double alpha    = 1.0;
      do{
        gauss = new Gauss( count , 0 , max , false );
        gauss.setAlpha( alpha -= 0.01 ).allowSoftMax();
        runCount += 1;
        int out = 0;
    
        while( gauss.hasNext() ){
          gauss.getNext();
          ;
        }
      } while( ! gauss.successfulResult );
  
      Iterator<Integer> itr = Arrays
            .stream( ( dataType == DataType.REVERSE ) ?
                     gauss.getReverseArray() :
                     gauss.getArray() )
            .iterator();
  
  
      fileMaker( itr , newFile , count );
      return fileTester( newFile.toPath() , count );
  
    }else return false; //the file already exists.
  }
  
  
  /**
   * fileTesting method
   * @param path - path name
   * @param count - the count expected
   * @return true if the values are what we expect.
   */
  public static boolean fileTester(Path path, int count)
        throws FileNotFoundException
  {
    if(!(new File(path.toUri()).exists())) return false;
    if(!(new File(path.toUri()).canRead())) return false;
    
    DataSet dataSet = new DataSet( path) ;
    if(dataSet.getCount() != count) return false;
    if(dataSet.getSize() != count) return false;
    return true;
  }
  
  
  /**
   * Generates a balanced sorted array across the specified range based on
   * gaussian distributions of a specified interval.
   *
   * @param count
   * @param min
   * @param max
   * @param allowDups
   * @param dataType
   * @return
   */
  public static Iterator<Integer> getGaussianIterator( int count, int min,
                                                       int max,
                                                       boolean allowDups,
                                                       DataType dataType ){
    Gauss gauss = new Gauss( count , min , max , allowDups );
    return gauss;
  }
  
  /**
   * Gets a stream for the specified value.
   * @param count
   * @param min
   * @param max
   * @param allowDups
   * @param dataType
   * @return
   */
  public static Iterator<Integer> getSpecifiedIntegerIterator( int count, int min, int max,
                                                               boolean allowDups,
                                                               DataType dataType ){
    
    int    seed = (int)(Math.random() * 1000);
    Random          rng              = new Random( seed );
//    Stream<Integer> streamofIntegers = rng.ints( count,min , max ).boxed();
    //Note- cant do count, as I thought this is the initial values, not the
    // final value count.  So that, when duplicates are removed. etc counts
    // are lowered...
    
    Stream<Integer> streamofIntegers = rng.ints( count,min , max ).boxed();
    //store ref to close this.
//    stream = streamofIntegers;
    switch( dataType ){
      case DESCENDING:
      case REVERSE:
        if(allowDups){
          // to reverse the stream we cab map, or we can convert to
          // Stream<Integ> first and then Reverse(), and then -> IntStream();
          count *= 1.5;
          return rng
                .ints( count,min , max )
                .boxed()
                .sorted()
                .map( i -> max - i - 1 )
                .iterator();
        }else{
          return rng
                .ints( count,min , max )
                .boxed()
                .distinct()
                .sorted()
                .map( i -> max - i - 1 )
                .iterator();
        }
      case ASCENDING:
        if (allowDups){
          return rng
                .ints( count,min , max )
                .boxed()
                .sorted()
                .iterator();
        }else{
          return rng
                .ints( count,min , max )
                .boxed()
                .sorted()
                .distinct()
                .iterator();
        }
      case DUPLICATE:
        return rng
              .ints( min , max )
              .boxed()
              .iterator();
      case UNSPECIFIED:
      case RANDOM:
      default:
        //implies no Duplicates?
        return rng
              .ints( min , max )
              .boxed()
              .distinct()
              .iterator();
    }

  }
  
  

  
  /**
   * Overloaded method - to allow plugging in Gauss
   * @param count - the number of files to generate file name
   * @param dataType - enum, File Type.
   * @return the file name
   */
  public static File getFile( int count , DataType dataType){
    return getFile( count , dataType , "" );
  }
  /**
   * Generate the filename of the type used in the examples. We need the
   * following types - the count, which should be either < 1000, or > than
   * 1000, and evenly divisible by 1000, e.g.
   * ok: 888, 1, 32, 1000, 2000, 5532000
   *
   * not ok: 2300, 4300, 2343, 1001 etc.
   *
   * if we want to match the naming format.
   *
   * Which isnt explicitly required but helps.
   * @param count the number.
   * @param dataType
   * of Enum.DataType- One of:
   *     ASCENDING,
   *     DESCENDING,
   *     DUPLICATE,
   *     RANDOM,
   *     REVERSE,
   *   UNSPECIFIED;
   * @return - the file name in the format
   * ran100.dat
   */
  public static File getFile( int count , DataType dataType,
                              String annotation ){
    //generate the file name, like the others...
    String fileName,
          prefix,
          countStr,
          suffix = ".dat";
    //based on datatype...
    switch( dataType ){
      case REVERSE:
        prefix = "rev";
        break;
      case DUPLICATE:
        prefix = "dup";
      case ASCENDING:
        prefix = "asc";
        break;
      case DESCENDING:
        prefix = "des";
        break;
      case UNSPECIFIED:
      case RANDOM:
      default: prefix = "ran";
        break;
    }
    if(count > 1000 &&   count % 1000 ==  0){
      countStr = String.valueOf( count / 1000 ) + "K";
    }else{
      countStr = String.valueOf( count );
    }
    fileName = prefix +
               countStr +
               annotation +
               suffix;
//    ((alternate)? "-2":"")
    String pathName = Sys.joinPath( customPath , fileName );
    return new File( pathName );
  }
  
  
  public static boolean checkDir(){
    
    File customDir = new File(
          Path.of(
                Sys.USER_DIR.toString() ,
                Sys.resourceRootPath.toString() , "custom" ).toUri()
    );
    if(!customDir.exists() ||  !customDir.isDirectory()){
      customDir.mkdir();
    }
    
    return customDir.exists();
  }
  
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


//  static void makeFile( int count, int min, int max, boolean allowDups,
//                        DataType dataType, File newFile ){
//
//    PrintWriter printWriter;
//    try{
//      printWriter = new PrintWriter(
//            new BufferedWriter(
//                  new FileWriter(
//                        newFile
//                  )
//            )
//      );
//
//      int    seed = (int)(Math.random() * 1000);
//      double f    = Math.random() / Math.nextDown( 1.0 );
//
//      if( allowDups || count < ( max - min ) ){
//        Random    rng       = new Random( seed );
//        IntStream intStream = rng.ints( count , min , max );
//        //        fileWriter.write( );
//        Iterable<String> itStr =
//              (Iterable<String>)intStream.mapToObj( String :: valueOf ) :: iterator;
//        for(String s : itStr){
//          printWriter.println( s );
//        }
//        printWriter.close();
//      }
//
//
//    } catch( IOException ioE ){
//      ioE.printStackTrace();
//    }
//
//    //
//  }