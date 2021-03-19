package org.DataStructures;


import java.util.Iterator;
import java.util.Random;

/**
 * The type Gauss.
 * <p>
 * Simply wraps the Random.nextGauss() method, implementing the Iterator class,
 * while mixing in some elements of the bounds.
 * <br>
 * This isnt perfect.  Adjusting ALPHA and BETA will effect the results in a
 * significant way.
 * <br>
 * These are values around 1. A value of 0.91 would be a significant skew.
 * Perhaps I should have made these integer values, and divided by 100 and
 * subtracted from one, but no one else will use this.
 * <br>
 * The internal adjust method will keep the counter on track.
 * <br>
 * The method (isSuccessfulResult()) will return if successful if the results
 * are within the bounds.
 * <br>
 * By default softMax == false.  This means that if the max is 1000, then the
 * method fails if it randomly doesnt succeed in generating the desired
 * numbers of values without going over max.
 * <br>
 * By default, in my testing, the generator goes over.
 * <br>
 * Setting softMax = true, then the success rate will be 100%.
 *
 */
public class Gauss implements Iterator<Integer>{
  int range,
      count,
      min,
      max,
      lastInt,
      curTotal;
  
  double interval,
        scaleFactor,
        spreadFactor,
        total,
        lastDouble,
        diff,
        next_Sum=0,
        nextavg,
        curDblVal,
        ALPHA = 1.0,
        BETA = 1.0; //tweaks the scale;
 
  Integer[] allValues,  allValues_PS;
  Double[] allDoubleVales;
  Double[] allDoubleVales_PS;
  boolean softMax;
  boolean successfulResult;
  boolean completed = false;
  int returnedCount = 0;
  
  private  Random rng;
  private String str = "";
  private boolean allowDuplicates = false;
  
  /**
   * Instantiates a new Gauss.
   *
   * @param ct
   *       the number of generated values.
   * @param min
   *       the min value
   * @param max
   *       the max value
   * @param duplicates
   *       whether to allow duplicates.
   * @param alpha
   *       the alpha -  the adjustment factor for the scaling factor.  Values
   *          close to one will tweak the scaling of the array.  e.g. in
   *       testing, with the values I was testing at that time, trying to get
   *       100 values, I was consistently getting 95, 96, 97 values.       By
   *       adding the alpha factor, and slowly scaling it down - (1.0, 0
   *       .99, 0.98, 0.97-- 0 .9 ) etc. it changes the retry hit rate from 92
   *       retries, to 6.       I recommend slowly scaling this value down as in
   *       <code>         while(unsuccessful){ alpha -= 0.01;           gauss =
   *       new Gauss(...) }       </code>
   */
  Gauss(int ct, int min, int max, boolean duplicates, double alpha){
    init( ct , min , max, duplicates );
  }
  

  
  /**
   * Overloaded method without alpha.
   *
   * @param ct
   *       - the number of values to generate.
   * @param min
   *       min value
   * @param max
   *       max value.
   * @param dups
   *       the dups
   */
  Gauss(int ct, int min, int max, boolean dups){
    setAlpha( 1.0 );
    init( ct , min , max ,dups);
  }
  
  /**
   * Another overloaded constructor.
   *
   * @param ct
   *       - the number of values to generate.
   * @param min
   *       min value
   * @param max
   *       max value.
   */
  Gauss(int ct, int min, int max ){
    setAlpha( 1.0 );
    init( ct , min , max ,false);
  }
  
  
  /**
   * The common aspect of the Constructors.
   *
   * @param ct
   *       - the number of values to generate.
   * @param min
   *       min value
   * @param max
   *       max value.
   * @param dups
   *       the dups
   */
  private void init( int ct , int min , int max, boolean dups ){
    this.max           = max;
    this.min           = min;
    this.count         = ct;
    this.rng           = new Random( Math.round( Math.random() * 1000 ) );
    this.range         = max - min;
    this.interval      = range  / (double)count;
//    this.scaleFactor   = interval;
//    this.spreadFactor  = ( interval / 2 );
    this.scaleFactor   = interval * ALPHA;
    this.spreadFactor  = ( interval / 2 ) * BETA;
    this.returnedCount = 0;
    this.total         = 0;
    this.curTotal      = min;
    allValues          = new Integer[ count ];
    allValues_PS       = new Integer[ count ];
    allDoubleVales     = new Double[ count ];
    allDoubleVales_PS  = new Double[ count ];
    successfulResult   = false;
    if(range <= ct) System.err.printf("It is unlikely this will complete "
                                     + "successfully with range %d and count "
                                       + "%d \n", range, ct);
    else if(range / ct < 10) System.out.println("It is recommended that the "
                                             + "ratio of max and range be 10: 1");
  }
  
  /**
   * This is the method that sets the term whether the result is successful
   * or not based on whether we have met conditions.
   * @return
   */
  @Override
  public boolean hasNext(){
    completed = (returnedCount >= this.count);
    if(completed){ //returned count == count , we are done...
      successfulResult = (softMax) || ( curTotal <= max);//we can go a bit over max
      return false;
    }else return ( curTotal <= max); //this makes sure hasNext turns false if
    // we go too high.
  }
  
  /**
   * same as getMext
   * @return
   */
  public Integer next(){
    this.getNextIncrement();
    return curTotal;
  }
  
  /**
   * Next db double.
   *
   * @return the double
   */
  public Double nextDb(){
    this.getNextDouble();
    return curDblVal;
  }
  
  
  /**
   * Get next int.
   *
   * @return the int
   *
   * Functional output here, but internally calls
   *
   * hasNext() - getNextDouble() - getNext()
   *
   * Returns the incremental total
   *
   * e.g. it generates a value and adds it to a total.
   */
  public int getNext(){
    this.getNextIncrement();
    return curTotal;
  }
  
  /**
   * Gets the next value, the increment
   * @return the next gaussian value,
   * for the total use getNext();
   */
  public int getNextIncrement(){
    double nextVal = getNextDouble();
    int rounded = (int)Math.round( nextVal );
                                     lastInt = rounded;
                                     curTotal += rounded;
    allValues[ returnedCount - 1 ] = curTotal;
    return rounded;
  }
  
  /**
   * The actual functional method.
   *
   * @return double
   */
  public double getNextDouble(){
    if(hasNext()){
      double scaled;
      int limit = ( allowDuplicates ) ? 0 : 1;
      do{
        double next = rng.nextGaussian();
        next_Sum += next;
        nextavg = next_Sum / returnedCount;
        //        scaled = ( next * spreadFactor * BETA) + (scaleFactor * ALPHA);
        scaled = ( next * spreadFactor ) + (scaleFactor );
        allDoubleVales_PS[ returnedCount ] = scaled;
        scaled = adjust( scaled );
        scaled *= ALPHA;
      } while( scaled < limit );
      lastDouble = scaled;
      allDoubleVales[ returnedCount ] =    scaled;
      curDblVal += scaled;
      returnedCount++;
      lastInt = - 1;
      total += scaled;
      return scaled;
    }else return - 1.0;
  }
  
  
  /**
   *
   * Need to keep us close to the expected value.
   *
   * We take the running mean and subtract it from the synthetic,
   * non-randomized mean (the interval).
   *
   * (returned count * interval) -  (total /    * returned count)
   * @param dblPreScaled
   * @return the adjusted value on the basis of the mean
   *
   *
   */
  private double adjust(double dblPreScaled){
  
    //dont start before the mean is established.
    if(returnedCount >count * 0.2 &&  returnedCount > 20 ){
      
//      diff = interval - getEffectiveMean();
      diff = range - getMean();
      while( diff > 1 ){ diff /= 10;} //make sure the effect isnt too large.
      //were running too high,  effect a lower val..
      if(diff < 0) diff *= 4;
      return ( dblPreScaled + diff );
    }else{
      return dblPreScaled;
    }
  }
  
  /**
   * Get mean.
   *
   * @return the running average.
   */
  public double getMean(){
    return ( total / returnedCount );
  }
  
  /**
   * @deprecated use alpha and beta instead.
   *
   * This changed the effective count to 105%  But it did not have nearly
   * trhe effect I wanted.
   *
   * Get mean. adjusted by a scaling factor.
   *
   * @return the running average.
   */
  public double getEffectiveMean(){
    return ( total / returnedCount );// *modifier;
  }
  
  public String toString(){
    return String.format( "Max: %d Min: %d, Value %d  of %d. Current Average:"
                          + " %.3f, Last Values were %.3f (%d) adjust-%.3f"
                          ,max, min, returnedCount, count, getMean(),
          lastDouble, lastInt, diff );
    
  }
  
  /**
   * Get range int.
   *
   * @return the int
   */
  public int getRange(){
    return range;
  }
  
  
  /**
   * Getter for property 'completed'.
   *
   * @return Value for property 'completed'.
   */
  public boolean isCompleted(){
    return completed;
  }
  
  /**
   * Get array integer [ ].
   *
   * @return the integer [ ]
   */
  public Integer[] getArray(){
    if(returnedCount == count) return allValues;
    else {
      Integer[] newArray = new Integer[ returnedCount ];
      System.arraycopy( allValues , 0 , newArray , 0 , returnedCount - 1 );
      return newArray;
    }
  }
  
  /**
   * Get reverse array integer [ ].
   *
   * @return the integer [ ]
   */
  public Integer[] getReverseArray(){
    Integer[] data = getArray();
    int l = data.length;
    Integer[] out = new Integer[ l];
    for( int i = 0 ; i < l ; i++ ){
      out[ i ] = data[ l - 1 - i ];
    }
    return out;
  }
  

  
  /**
   * Returns the max count.
   *
   * @return int
   */
  public int getCount(){
    return count;
  }
  
  
  /**
   * Get alpha double.
   *
   * The alpha value scales the Interval as a multiplier of Interval.
   *
   * Such that Interval *= alpha, and 1 is no effect.
   *
   * @return the double
   */
  public double getALPHA(){
    return ALPHA;
  }
  
  /**
   * Set alpha.
   *
   * The alpha value scales the Interval as a multiplier of Interval.
   *
   * and thus effects the Scale Factor in the same way.  Since ScaleFactor
   * and Interval are the same in this implementation.
   *
   * Such that Interval *= alpha, and 1 is no effect.
   *
   * @param alpha
   *       the alpha value
   */
  public Gauss setAlpha( double alpha ){
    this.ALPHA = alpha;
    return this;
  }
  
  /**
   * Get beta double.
   *
   * The beta value scales the SpreadFactor as a multiplier
   *
   * Such that SpreadFactor *= beta, and 1 is no effect.
   *
   * @return the double value of beta
   */
  public double getBeta(){
    return BETA;
  }
  
  /**
   * Set beta.
   *
   * @param beta
   *       the beta
   */
  public Gauss setBeta( double beta ){
    this.BETA = beta;
    return this;
  }
  
  /**
   * Is soft max boolean.
   *
   * @return the boolean
   */
  public boolean isSoftMax(){
    return softMax;
  }
  
  /**
   * Set soft max.
   *
   * @param softMax
   *       the soft max
   */
  public Gauss setSoftMax( boolean softMax ){
    this.softMax = softMax;
    if(softMax) {
      setAlpha( 0.96 );
      //pull the max down a bit for room
      if(max == Integer.MAX_VALUE) max /= 2;
      //recalculate the other values.
      init( count , min , max , allowDuplicates );
      max = Integer.MAX_VALUE;
    }
    return this;
  }

  
  /**
   * Set firm max.
   * Equivalent to setSoftMax(false)
   */
  public Gauss setFirmMax( ){
    setSoftMax( false );
    return this;
  }
  /**
   * Set soft max to true.  Equivalent to setSoftMax(true)
   *
   */
  public Gauss allowSoftMax( ){
    setSoftMax( true );
    return this;
  }
  
  /**
   * Is successful result boolean.
   *  The check is done in hasNext.  So we need to first run the method,  The
   *  check will tell us if we are done in the first place.  If we arent done
   *  it cant be successful.
   * @return the boolean value- conditions- weve returned all the values,
   * weve either been under the max, or allowed excursions with soft max
   *
   * see also Gauss.hasNext(), Gauss.isUnSuccessfulResult()
   */
  public boolean isSuccessfulResult(){
    if(!hasNext()) return successfulResult;
    else return false;
  }
  
  /**
   * Returns the inverse of successful result.
   * @return conditions- weve returned all the values,
   *    * weve either been under the max, or allowed excursions with soft max
   */
  public boolean isUnsuccessfulResult(){
    return ! isSuccessfulResult();
  }
  
 
  
  /**
   * Get min int.
   *
   * @return the int
   */
  public int getMin(){
    return min;
  }
  
  /**
   * Get max int.
   *
   * @return the maximum bound value that was set at the outset.
   */
  public int getMax(){
    return max;
  }
  
  
  /**
   * Get last int integer.
   *
   * @return the integer
   */
  public Integer getLastInt(){
    if(lastInt < 0) return null;
    else return lastInt;
  }
  
  /**
   * Get cur value int.
   *
   * @return the int
   */
  public int getCurTotal(){
    return curTotal;
  }
  
  /**
   * Get interval double.
   *
   * @return the double
   */
  public double getInterval(){
    return interval;
  }
  
  /**
   * Get scale factor double.
   *
   * This is the untweaked value, (before it is effected by alpha)
   *
   * The end result that the method sees is Scale Factor * alpha
   *
   * @return the double
   */
  public double getScaleFactor(){
    return scaleFactor;
  }
  
  /**
   * Get spread factor double.
   *
   * @return the double
   */
  public double getSpreadFactor(){
    return spreadFactor;
  }
  
  /**
   * Get total double.
   *
   * @return the double
   */
  public double getTotal(){
    return total;
  }
  
  /**
   * Get last double double.
   *
   * @return the double
   */
  public double getLastDouble(){
    return lastDouble;
  }
  
  /**
   * Get returned count int.
   *
   * @return the int
   */
  public int getReturnedCount(){
    return returnedCount;
  }
  
  /**
   * Get diff double.
   *
   * @return the double
   */
  public double getDiff(){
    return diff;
  }
  
 
  
  
}