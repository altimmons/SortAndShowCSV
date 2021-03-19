package org.DataStructures;



import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * The type Data set.
 */
public class DataSet
      extends RecordSet implements Iterable<Integer>{
//, ListIterator{
private DataType                           dataType;
private Integer                            count;
private Path                               filePathOrigin; //the absolute path
private QSParams[]                         testedParams;
private Long[]                             timeResults;
private String[]                           timerResults;
private EnumMap<QSParams, DataSet.Results> result =
        new EnumMap<QSParams, DataSet.Results>(QSParams.class);
private boolean                            userGenData = false;
  private boolean gaussSequentialData = false;
  private String relativeFN;
  //this doesnt like having declaration and assignment split for whatever
  // reason.  Will have to be initialized with the class.
  

  
  /**
   * Load all the files in a directory, read each one and add the data to
   * an array of Integers.  Returns a wrapper for the array,
   * <p></p>
   * GArrayList really should have been the super for RecordSet -> DataSet,
   * but I wrote them separately and they sort of converged.  I wrote
   * RecordSet first to store the array of data and handle adding, removing,
   * resizing, subsetting, formatting, etc. an array of Integers.
   * <p></p>
   * DataSet extends RecordSet to add 2 important functionalities, the first
   * the File Reading and nameing methods.  RecordSet only handles the actual
   * data, DataSet Loads and applies that data given a fileName or path.
   * </p>
   * In addition, DataSet implements the{@link Iterable}interface and
   * the contains two sub-classes that provide the <Code>
   * {@link java.util.ListIterator} and the {@link Iterator<Integer>}
   * interfaces.
   *
   *
   * GArray List was written after, and is essentially the same thing, many
   * of the ideas in one were implemented in the other.  And in truth, by the
   * end, GArrayList could have completely replaced {@link RecordSet}.
   * RecordSet is an <b>Integer</b> implementation of {@link GArrayList}
   *
   *
   * @return the GArrayList - and arrayList wrapper of DataSet classes.
   *
   * @throws IOException
   *
   * @see public Interface Iterator<E>
   * @see java.util.Iterator
   * @see java.util.ListIterator
   *  @see java.util.ArrayList
   * @see RecordSet
   * @see GArrayList
   *
   */
  DataSet(){
    super();
  }
  
  /**
   * Build the data set.
   * @param path
   */
  DataSet( Path path ) throws FileNotFoundException{
    
    super( path.toString() );
    this.filePathOrigin = path.toAbsolutePath();
    File file = new File( path.toUri() );
    String fileName = file.getName().toString();
    if(!file.exists())
      throw new FileNotFoundException( file.getAbsolutePath() + "is unable to"
                                       + " be found." );
    try{
      relativeFN = Sys.getRelativePath( path );
      this.setDescriptor( ( relativeFN.length() > 4 ) ? relativeFN : fileName );
    }catch(IllegalArgumentException iargE){
//      setDescriptor( fileName );
      //ignore and continue.
    }
    
    //change the default descriptor from
    // (O:\OneDrive\Code\IntelliJ\Timmons\res\input\asc10K.dat)
    //to the simpler
    super.setDescriptor( fileName );
    
    if(fileName.endsWith( ".dat" )){
      fileName = setDataType( fileName );
      String      nextToken = fileName.substring( 3 );
      setCount( nextToken );
      //now read the file.
      
      BufferedReader bufferedReader;
      try{
        bufferedReader =   Files.newBufferedReader( path );
        read( new Scanner( bufferedReader ) );
        if(Sys.DEBUG_EN)    System.out.println(String.format( "%s \t Count: %d \t Size: %d",
              this.getDescriptor() , count, super.getSize() ));
      }catch( IOException e ){
        e.printStackTrace();
      }
    }
    
    //trim the extra space.
    this.trim();
  }
  
  
  /*-----------------------------------------------------------------------------------------------------------------------------------------------*/
  /**
   * The testRunning method.
   *
   * @param qsParams
   *
   * Takes an array of QSParams[] which will be applied to the sort testing.
   */
  public void test(QSParams ... qsParams) throws IOException{
    System.out.println("Testing " +getDescriptor());
    System.out.println("");
   
    if(qsParams == null) qsParams = QSParams.values();
    //just get all of them if null

    // setup our fields and variables
    testedParams = qsParams;
    timerResults = new String[qsParams.length];
    timeResults = new Long[ qsParams.length ];
    int      l            = 0;
    //initialize the EnumMap.
//    timeResults
    
    //start the loop
  for(QSParams param : qsParams){
    System.out.printf( "\t %s starting with parameter %s \n" ,
          getDescriptor() ,
          param.name()
                     );
    DataSet.Results results = new DataSet.Results( param );
    Integer[] data = this.extract(); //this is just to set the size...
    SimpleTimer simpleTimer = SimpleTimer.simpleTimerFactory();
    simpleTimer.start();
    QSort qSort = new QSort( this, param);
    //store the result
    data = qSort.sort();
    if(param.doInsertSort()){
      if(Sys.DEBUG_EN) System.out.println("\t\tStarting Insertion Sort for " + this.getDescriptor());
      data = InsertionSort.sort( data );
    }
    //store the sorted Data
    results.setSortedData( data );
    
    
    simpleTimer.end();
    //store time results
    timerResults[ l ] = simpleTimer.toString();
    timeResults[ l ] = simpleTimer.getResult();
    results.startTime = simpleTimer.getStart();
    results.endTime = simpleTimer.getEnd();
    results.time = simpleTimer.getResult();
    results.setTime_micros(simpleTimer.getResultMicroS()  );
    results.setTime_millis( simpleTimer.getResultMilliS() );
    
    //check the array- since i am not going to check 10,000 elements by hand.
    boolean successful = testSort( data );

    //store our check result
    results.testTestResult = successful;
    
    //todo disable this
  if(Sys.DEBUG_EN){
    String s =
          String.format( "\t\t%s [%d] (%s) Test Parameters: %s, " + "Successful " + "Completion [%B ] \t Time %d\n" ,
                getDescriptor() ,
                getSize() ,
                dataType.name() ,
                param ,
                successful ,
                simpleTimer.getResult() );
    System.out.println( s );
  }
  
    result.put( param, results );  //store the result and continue loop
    Reporter.report( results );  //send to reporter
  }

}
  
  /*-----------------------------------------------------------------------------------------------------------------------------------------------*/
  /**
   * INNER CLASS |RESULTS|
   *
   * Simple class to encapsulate the results of the test.  No enclosing
   * methods, just access the fields directly. I dont need to hide anything
   * here, its just for storing values.
   *
   * For now, the param values are explicitly set, but may want to eventually.
   *     may want to break these out later, but not using them now.  gets
   *      confusing with both.  Can access these elements with
   *       e.g result.param.getPivotMethod.name
   */
  class Results{
    public String time_micros;
    public String time_millis;
    String file = getDescriptor();
    int      dataSize = getSize();
    int      count = getCount();
    DataType dataType = getDataType();
    QSParams params;
    final int HEAD_STRING_VALUE_LENGTH = 30;
//    int                   kStop;
//    QSParams.PivotMethods pivotMethod;
//    boolean insetionSortPerformed;
    long                  startTime = -1;
    long endTime= -1;
    long time=-1;
    Boolean testTestResult = null;
    Integer[] sortedArray,
          presortedArray = extract();
    String sortedHead,
          presortedHead =
          RecordSet.formatSet( HEAD_STRING_VALUE_LENGTH , extract() );
    boolean userGen = userGenData;
    boolean gauss = gaussSequentialData;
    String path =
          Path.of( Sys.USER_DIR ).relativize( filePathOrigin.toAbsolutePath() ).toString();
    String absPath = filePathOrigin.toAbsolutePath().toString();
    private final DecimalFormat microsFormat = new DecimalFormat("######.##");
    private final DecimalFormat millisFormat =
          new DecimalFormat("# ");
    
    Results(){}//empty constructor
    
    
    Results(QSParams params){
      this.params = params;
    }
    
    public void setSortedData(Integer[] data){
      sortedArray = data;
      sortedHead = RecordSet.formatSet( HEAD_STRING_VALUE_LENGTH , data );
    }
    
    public void setTime_micros(double micros){
      this.time_micros = microsFormat.format( micros );
    }
    public void setTime_millis(double millis){
      this.time_millis = microsFormat.format( millis );
    }
    //todo make toString()?
}/*END INNER CLASS |RESULTS|*/
  /*-----------------------------------------------------------------------------------------------------------------------------------------------*/


public boolean testSort(Integer[] result){
  /*Operational Settings*/
  final boolean FAILONDUPLICATE = false;
  final int MSGMAX = 20;
  
  /*Initializers*/
  int comparisonCount = 0, msgCount = 0;
  
  
  for( int j = 1 ; j < result.length ; j++ ){
    if( getDataType() == DataType.DUPLICATE ){
      /*DUPLICATES*/
      //remove the equals condition.
      if( ! ( result[ j - 1 ] <=  result[ j ] ) )
        if(Sys.DEBUG_EN && msgCount < MSGMAX){
  
          System.out.format(
                "\t\t\t%s:Test failed at position data[%d] = %d and "
                + "data[%d] ="
                + " " + "%d(!)\n" , getDescriptor(),
                j - 1 ,
                result[ j - 1 ] ,
                j ,
                result[ j ] );
          msgCount++;
        }
//      if( !(result[ j - 1 ] <= result[ j ]) ) return false;
    } else{
      /*ALL OTHER TYPES*/
      //notify of illicit duplicates.
      if(result[ j - 1 ] == result[ j ] ){
        if( Sys.DEBUG_EN && Sys.verbose && msgCount < MSGMAX ){
          System.out.format(
                "\t\t\t%s: Unexpected Duplicate at position data[%d] = %d and " + "data[%d]" + " = " + "%d...continuing.\n" ,
                getDescriptor() , j - 1 ,
                result[ j - 1 ] ,
                j ,
                result[ j ] );
          msgCount++;
        }
        if(FAILONDUPLICATE) return false;
      }
      if( ! ( result[ j - 1 ] < result[ j ] ) ) {
        if(Sys.DEBUG_EN &&  Sys.verbose &&  msgCount < MSGMAX) {
          System.out.format(
                "\t\t\t%s:Test failed at position data[%d] = %d and data[%d] ="
                + " " + "%d  (@)\n" , getDescriptor(),
                j - 1 ,
                result[ j - 1 ] ,
                j ,
                result[ j ] );
          msgCount++ ;
        }
      }
      if(FAILONDUPLICATE){
        if( !(result[ j - 1 ] < result[ j ]) ) return false;
      }else{
        if( !(result[ j - 1 ] <= result[ j ]) ){
//          System.out.println( "This is returning False" );
          return false;
        }
      }
    }
    comparisonCount++;
  }
  if(Sys.DEBUG_EN && Sys.verbose) System.out.printf( comparisonCount + " comparisons "
                                     + "completed \n"  );
  
        
  return true;
}
  
  
  /**
   * Getter for property 'result'.
   *
   * Returns the EnumMap<QSParams, DataSet.Results> of the result data
   *
   * @see DataSet.Results
   * @return A Map Object
   */
  public EnumMap<QSParams, DataSet.Results> getAllResults(){
    return result;
  }
  
  /**
   * Lookup a particular result
   *
   * @
   */
  public Results getResult(QSParams param){
    return result.get( param );
  }
  
  
  /**
   * Just because we know the keys beforehand, doesnt mean it MUST exist.
   * Method to check if the particular result has been completed.
   * @param params
   * @return
   */
  public boolean hasResult(QSParams params){
    return result.containsKey( params );
  }
  
  /**
   * Set data type Enum given the file name.
   *
   * @param fileName
   *       the file name
   *
   * @return the string
   */
  @NotNull
  private String setDataType( String fileName ){
    fileName = fileName.substring( 0 , fileName.length() - 4 );
    String startsWith = fileName.substring( 0, 3 );
    //right bound is exclusive...
    switch(startsWith){
      case "asc":
        dataType = DataType.ASCENDING;
        break;
      case "dup":
        dataType = DataType.DUPLICATE;
        break;
      case "ran":
        dataType = DataType.RANDOM;
        break;
      case "rev":
        dataType = DataType.REVERSE;
        break;
      case "des":
        dataType = DataType.DESCENDING;
        break;
      default:
        dataType = DataType.UNSPECIFIED;
    }//todo Add Else
    return fileName;
  }
  
  /**
   * Set count. This value is based on the file name;
   *
   * e.g. dup10k.dat = '10K' = 10,000
   *
   * @param countSubString
   *       the next token
   */
  private void setCount( String countSubString ){
    Pattern pattern = Pattern.compile( "([0-9]*)(K*)([-]*)([u]*)([g]*)" );
    Scanner scanner = new Scanner(countSubString);
    //      scanner.findInLine( "([0-9]*)(K*)" );
    scanner.findInLine(pattern );
    MatchResult matchResult1 = scanner.match();
    
    //take the numeric output , then if there is a K in the second match
    // (ternary exp) multiply by 1000, if not by 1.-  This takes the short
    // hand and gets a supposed value.
    if(matchResult1.groupCount() >1){
      
      String group1 = matchResult1.group( 1 );
      String group2 = matchResult1.group( 2 );
      Integer a = Integer.valueOf( group1 );
      int b = ( (matchResult1.group(2).equals( "K" )) ? 1000 : 1) ;
      count = a*b;
      if(matchResult1.group( 4 ).equals( "u" )){
        userGenData = true;
      }
      if(matchResult1.group( 5 ).equals( "g" )){
        gaussSequentialData = true;
      }
    }
  }
  
  
  /**
   * Read from the scanner, perform checks and add.
   *
   *some negative values sneaking in due to  errors in my data generation methods
   * .  This is since fixed, but some parameters are nice for sanity checks.
   *
   * @param scanner
   *       the scanner which is reading the file.
   *
   * void return, writes to Class field 'data'
   */
  private void read( Scanner scanner ) {
    while(scanner.hasNextInt()){
      int next = scanner.nextInt();
      if(next >= 0)add(next ); //s
      else continue;
    }
    
    //    if(Sys.DEBUG_EN) System.out.println(records);
  }
  
  
  /**
   * Get data type data type.
   *
   * @return the data type Enum
   * @see DataType
   *
   */
  public DataType getDataType(){
    return dataType;
  }
  
  /**
   * Count returns the number of values in the file as stated in the file
   * name-- e.g. "asc50k.dat" says in the file name it contains 50k records.
   *
   * Use get size to get the actual count.
   * @return
   */
  public Integer getCount(){
    return count;
  }
  
  public String toString(){
    String superString = super.toString();
    return this.dataType.toString() + " " + superString;
  }
  
  @NotNull
  @Override
  /**
   * {@inheritDoc}
   * Returns an iterator for the file.
   */
  public Iterator<Integer> iterator(){
    return new DataSetIterator();
  }
  
  /**
   * {@inheritDoc}
   */
  public ListIterator<Integer> listIterator() {return new DataListSetIterator( 0 );}
  
  /**
   * Returns a list iterator over the elements in this list (in proper
   * sequence).
   *
   * The underlying type is Integer.  It is not designed to be Syncronized
   * like the built in Java Class ArrayList$Iterator
   *
   * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
   *
   */
  class DataSetIterator extends DataSet implements Iterator<Integer>{
    protected int cur_read_pos;
    int cursor;
    protected int last_read_pos;
    
    DataSetIterator( int startIndex ){
      cur_read_pos = startIndex;
      this.last_read_pos = - 1;
    }
    
    DataSetIterator(){
      cur_read_pos = 0;
      this.last_read_pos = - 1;
    }
    
    @Override
    /**
     * {@inheritDoc}
     */
    public boolean hasNext(){
      return (cursor !=  super.size);
    }
    
    @Override
    /**
     * {@inheritDoc}
     */
    public Integer next(){
      
      Integer returning = data[ cur_read_pos ];
      last_read_pos = cur_read_pos++ ;
      return returning;
    }
    
    @Override
    /**
     * {@inheritDoc}
     */
    public void remove(){
      System.err.println("Not yet implemented");
      return;
      //      if(this.last_read_pos < 0)
      //        throw new IllegalStateException( "No Element " + "to remove." );
      //      remove( this.last_read_pos );
    }
    
    @Override
    /**
     * {@inheritDoc}
     */
    public void forEachRemaining( Consumer<? super Integer> action ){
      Objects.requireNonNull( action );
      final int size = getSize();
      int i = cur_read_pos;
      if( i < size ){
        for(i= cur_read_pos;i < size ; i++   ) {
          action.accept(  elementAt(data , i) );
          cur_read_pos = i;
          last_read_pos = i - 1;
        }
      }
    }
  }

  private class DataListSetIterator extends DataSetIterator
        implements ListIterator<Integer>{
  
    DataListSetIterator( int index ){
      super();
      cursor = index;
    }
    
    @Override
    /**
     * {@inheritDoc}
     */
    public boolean hasPrevious(){
      return (cur_read_pos != 0);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer previous(){
      return data[last_read_pos];
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int nextIndex(){
      return cur_read_pos;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int previousIndex(){
      return last_read_pos;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void set( Integer integer ){
      DataSet.this.setValue( last_read_pos, integer );
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void add( Integer integer ){
      super.insert( cur_read_pos, integer );
    }
  }
  
  
}

/*      MatchResult matchResult = pattern.matcher( nextToken ).toMatchResult();
      MatchResult matchResult2 =
            Pattern.compile( "([0-9]*?)" )
                   .matcher( nextToken )
                   .toMatchResult()
                   .group( 0 );
                  Matcher matcher = new Matcher( pattern , nextToken.toCharArray() );
      
      Integer i = Integer.valueOf(
            Pattern.compile( "([0-9]*?)" )
                             .matcher( nextToken )
                             .toMatchResult()
                             .group( 0 )
                             .toString());
      // result * (group(1)!=null? 1000:1;*/


//see if insertion sort works.
//    if(!successful){
//      data = InsertionSort.sort( data );
//      boolean secondSuc = testSort( data );
//      s = String.format( "\t\t%s [%d] (%s) Test Parameters: %s, "
//                         + "Successful "
//                         + "Completion [%B ] \t Time %d\n" ,
//            getDescriptor() ,
//            getSize() ,
//            dataType.name() ,
//            param ,
//            secondSuc,
//            simpleTimer.getResult()
//                       );
//      System.out.println( s );
//
//    }