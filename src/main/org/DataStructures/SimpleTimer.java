package org.DataStructures;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.DataStructures.Sys.DEBUG_EN;
import static org.DataStructures.Sys.LOG_EN;

/**
 * Vastly over simplified timer from the first I wrote.  KISS.
 */
public class SimpleTimer{
  
  private long        start;
  private long        sys_start;
  private long        end;
  private long        sys_end;
  private boolean     started;
  private boolean     ended;
  private boolean     nanos;
  private Logger      log = Logger.getLogger( "simpletimer" );
  static  SimpleTimer lastInstance;
  
  
  /**
  * Constructor Method
  */
  private SimpleTimer(){
    if (LOG_EN)  log.finer( String.format(" Simple Timer instantiated :(C)%s " 
                                        + "(m)%s",
                             "SimpleTimer", "SimpleTimer" ));
    started = false;
    ended = false;
    nanos = true;
  }
  
  /**
   *
   * Factory Method to generate a timer.
   * Create simple timer simple timer fact.
   *
   * @return the simple timer fact
   */
  static SimpleTimer simpleTimerFactory(){
  
    lastInstance = new SimpleTimer();
    return lastInstance;
  }
  
  /**
   * Get the most recent timer.
   */
  static SimpleTimer getRecent(){
    return lastInstance;
  }
  
  /**
   * Start.
   * @return
   */
  public SimpleTimer start(){
    if (LOG_EN) log.fine( String.format("Timer started at %d  :(C)%s (m)%s",start, "start",
                            "SimpleTimer" ));
    started = true;
    start = System.nanoTime();
    sys_start = System.currentTimeMillis();
    return this;
  }
  
  /**
   * End.
   */
  public void end(){
    if (LOG_EN) log.fine( String.format("Timer Ended at %d  :(C)%s (m)%s",end, "end",
                            "SimpleTimer" ));
    if (!started) System.err.println( "The timer was stopped before it was " +
                                      "started." );
    end = System.nanoTime();
    sys_end = System.currentTimeMillis();
    ended = true;
    
  }
  
  /**
   * Returns a long of the nano-time from the JVM
   *
   * @return long
   */
  public long getResult(){
    if(!started) {
      if(DEBUG_EN) System.err.format( "Attempeted to get result without " +
                                 "starting the timer first" );
      if (LOG_EN) log.log( Level.SEVERE, "Tried to get the result of a timer " +
                                      "without  starting it first." );
    }
    if(!ended){
      if (LOG_EN) log.warning( String.format(
          "Attempted to get the result of the timer without ending it first" +
              ".  It has been ended now.  :(C)%s (m)%s",
          "getResult",
          "SimpleTimerFact" ) );
      this.end();
    }
    return end - start;
  }
  
  /**
   * returns the result in Microseconds.
   * @return the result in uS (ns/1000)
   */
  public double getResultMicroS(){
    return getResult()/1000;
  }
  
  /**
   * returns the result in Milliseconds.
   * @return the result in mS (us/1000)
   */
  public double getResultMilliS(){
    return getResultMicroS()/1000;
  }
  
  /**
   * returns the result in Seconds.
   * @return the result in S (ms/1000)
   */
  public double getResultSeconds(){
    return getResultMilliS()/1000;
  }
  
  /**
   * Get nano time result long.
   *
   * @return the long
   */
  public long getNanoTimeResult(){
    return getResult();
  }
  
  /**
   * Get system result long.
   *
   * @return the long
   */
  public long getSystemResult(){
    getResult(); //we can just piggy back off the error handling for this.
    return sys_end - sys_start;
  }
  
  /**
   * toString Overloaded method;
   * @return String reflecting the state of the timer.
   */
  public String toString(){
    if(!started &&  !ended){ return "The timer has not been started";}
    else if(started &&  !ended){
      if( nanos ){
        return String.format( "The timer was started" +
                              " " +
                              "and " +
                              "is currently running. \n " +
                              "Start Time: %d \n " +
                              "Elapsed(Now): %d",
                              start,
              System.nanoTime() - start );
      }else{
        return String.format( "The timer was started" +
                              " " +
                              "and " +
                              "is currently running. \n " +
                              "Start Time: %d \n " +
                              "Elapsed(Now): %d",
                              sys_start,
              System.currentTimeMillis() - sys_start );
      }
    }/*else if (started &&  ended ){*/
    StringBuilder resultBuilder = new StringBuilder();
//      if( nanos ){
    //figure out if we are doing
    float res = (float)( ( nanos ) ? getResult() : getSystemResult() );
//        resultBuilder.append( "\n\t" )
//            .append("Start time(nS):")
//                     .append(start)
//                     .append( "\n\tEnd time(nS):" )
//
//                     .append( end )
//                     .append( "\n\tElapsed (nS): " )
//                     .append( res )
//                     .append("\n\tconversion:")
//                     .append( "\n\t\tMicroSecond (uS):" )
//                     .append( res/1000)
//                     .append( "\n\t\tMilliSeconds (mS):" )
//                     .append( (res/1000)/1000 )
//                     .append( "\n\t\tSeconds(S): " )
//                     .append( ((res/1000)/1000)/1000 );
      if( nanos ){
resultBuilder.append( "\n\t" )
  .append("Start time(nS):")
           .append(start)
           .append( "\n\tEnd time(nS):" )

           .append( end )
           .append( "\n\tElapsed (nS): " )
           .append( getResult() )
           .append("\n\tconversion:")
           .append( "\n\t\tMicroSecond (uS):" )
           .append( getResult()/1000.)
           .append( "\n\t\tMilliSeconds (mS):" )
           .append( (getResult()/1000.)/1000. )
           .append( "\n\t\tSeconds(S): " )
           .append( ((getResult()/1000.)/1000.)/1000. );
//        ((nanos) ? getResult() : getSystemResult() )
      }else{
                  //todo test this.  Find out if it returns 10^-6 S or 10^-3
                  // S on system time. Youd assume higer res. but millis
                  // implies 10^-3....
        return String.format( "Start time: %d \n End time: %d \n Elapsed: %d",
                              sys_start, sys_end, getSystemResult() );
//        resultBuilder.append("THIS METHOD IS CURRENTLY NOT WORKING AND NOT " +
//                                 "RELIABLE.  TIME IS IN MILLIS, AND BASED ON " +
//                                 "SYSTEM CLOCK TIME.").
//
//                         append( "\n\t" )
//                     .append("Start time(mS):")
//                     .append(start)
//                     .append( "\n\t End time(mS):" )
//                     .append( end )
//                     .append( "\n\tElapsed (mS): " )
//                     .append( getSystemResult() )
//                     .append( "\n\tmicroSecond (uS):" )
//                     .append( getSystemResult()/1000)
//                     .append( "\n\tmSeconds (mS):" )
//                     .append( (getSystemResult()/1000)/1000 )
//                     .append( "\n\tSeconds(S): " )
//                     .append( ((getSystemResult()/1000)/1000)/1000 );
//        return resultBuilder.toString();
      }
    return resultBuilder.toString();
  
  }
    
    
    
  /**
   * Get start long.
   *
   * @return the long
   */
  public long getStart(){
    return start;
  }
  
  /**
   * Set start.
   *
   * @param start the start
   */
  public void setStart( long start ){
    this.start = start;
  }
  
  /**
   * Get sys start long.
   *
   * @return the long
   */
  public long getSys_start(){
    return sys_start;
  }
  

  
  /**
   * Get end long.
   *
   * @return the long
   */
  public long getEnd(){
    return end;
  }
 
  
  /**
   * Get sys end long.
   *
   * @return the long
   */
  public long getSys_end(){
    return sys_end;
  }

  
  /**
   * Is started boolean.
   *
   * @return the boolean
   */
  public boolean isStarted(){
    return started;
  }
  

  
  /**
   * Is ended boolean.
   *
   * @return the boolean
   */
  public boolean isEnded(){
    return ended;
  }
  

  
  /**
   * Is nanos boolean.
   *
   * @return the boolean
   */
  public boolean isNanos(){
    return nanos;
  }
  
  /**
   * Set nanos.
   *Where false is System millis, and true is JVM nano time.
   * @param nanos the nanos
   */
  public void setNanos( boolean nanos ){
    this.nanos = nanos;
  }
}
///*
//  */
///**
//   * Set sys start.
//   *
//   * @param sys_start the sys start
//   *//*
//
//  public void setSys_start( long sys_start ){
//    this.sys_start = sys_start;
//  }
//  */
///**
//   * Set end.
//   *
//   * @param end the end
//   *//*
//
//  public void setEnd( long end ){
//    this.end = end;
//  }
//  */
///**
//   * Set ended.
//   *
//   * @param ended the ended
//   *//*
//
//  public void setEnded( boolean ended ){
//    this.ended = ended;
//  }
//  */
///**
//   * Set started.
//   *
//   * @param started the started
//   *//*
//
//  public void setStarted( boolean started ){
//    this.started = started;
//  }
//
//  */
///**
//   * Set sys end.
//   *
//   * @param sys_end the sys end
//   *//*
//
//  public void setSys_end( long sys_end ){
//    this.sys_end = sys_end;
//  }*/


/*
else if (started &&  ended ){
StringBuilder resultBuilder = new StringBuilder();
  //      if( nanos ){
  //figure out if we are doing
  long res = ( ( nanos ) ? getResult() : getSystemResult() );
        resultBuilder.append( "\n\t" )
            .append("Start time(nS):")
            .append(start)
            .append( "\n\tEnd time(nS):" )
    
            .append( end )
            .append( "\n\tElapsed (nS): " )
            .append( res )
            .append("\n\tconversion:")
            .append( "\n\t\tMicroSecond (uS):" )
            .append( res/1000)
            .append( "\n\t\tMilliSeconds (mS):" )
            .append( (res/1000)/1000 )
            .append( "\n\t\tSeconds(S): " )
            .append( ((res/1000)/1000)/1000 );
            return resultBuilder.toString();
            //        if( nanos ){
            //        resultBuilder.append( "\n\t" )
            //            .append("Start time(nS):")
            //                     .append(start)
            //                     .append( "\n\tEnd time(nS):" )
            //
            //                     .append( end )
            //                     .append( "\n\tElapsed (nS): " )
            //                     .append( getResult() )
            //                     .append("\n\tconversion:")
            //                     .append( "\n\t\tMicroSecond (uS):" )
            //                     .append( getResult()/1000)
            //                     .append( "\n\t\tMilliSeconds (mS):" )
            //                     .append( (getResult()/1000)/1000 )
            //                     .append( "\n\t\tSeconds(S): " )
            //                     .append( ((getResult()/1000)/1000)/1000 );
            //        ((nanos) ? getResult() : getSystemResult() )
            //      }else{
            //        return String.format( "Start time: %d \n End time: %d \n Elapsed: %d",
            //                              sys_start, sys_end, getSystemResult() );
            //        resultBuilder.append( "\n\t" )
            //                     .append("Start time(nS):")
            //                     .append(start)
            //                     .append( "\n\t End time(nS):" )
            //                     .append( end )
            //                     .append( "\n\tElapsed (nS): " )
            //                     .append( getSystemResult() )
            //                     .append( "\n\tmicroSecond (uS):" )
            //                     .append( getSystemResult()/1000)
            //                     .append( "\n\tmSeconds (mS):" )
            //                     .append( (getSystemResult()/1000)/1000 )
            //                     .append( "\n\tSeconds(S): " )
            //                     .append( ((getSystemResult()/1000)/1000)/1000 );
            //        return resultBuilder.toString();
            //      }
 */
