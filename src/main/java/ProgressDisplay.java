
import java.io.PrintStream;

/**
 * class ProgressDisplay
 * adapted from the boost c++ library class of the same name,
 * which bears the following notice:
 //  Copyright Beman Dawes 1994-99.  Distributed under the Boost
 //  Software License, Version 1.0. (See accompanying file
 //  LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt) 
 */

public final class ProgressDisplay {
	private PrintStream m_os;
	private String  m_s1; 
	private String  m_s2;
	private String  m_s3;

	private long _count, _expected_count, _next_tic_count;
	private int  _tic;

	///////////////////////////////

	public ProgressDisplay( long expectedCount ) {
		this( expectedCount, 
			new PrintStream( System.out ), 
				"\n", "", "" );
	}

	/**
	 * @param os -- hint: implementation may ignore, 
	 * particularly in embedded systems
	 */ 
	
	public ProgressDisplay( long expectedCount, 
		PrintStream os, String s1, String s2, String s3 ) {
		m_os = os;
		m_s1 = s1;
		m_s2 = s2;
		m_s3 = s3;
		restart( expectedCount ); 
	}

	///////////////////////////////
	
	public void restart( long expected_count ) {
	//  Effects: display appropriate scale
	//  Postconditions: count()==0, expected_count()==expected_count
		_count = _next_tic_count = _tic = 0;
		_expected_count = expected_count;

		m_os.println( m_s1 + "0%   10   20   30   40   50   60   70   80   90   100%" );
		m_os.println( m_s2 + "|----|----|----|----|----|----|----|----|----|----|" );
		m_os.flush();
		m_os.print( m_s3 );
		if( _expected_count == 0 ) 
			_expected_count = 1;  // prevent divide by zero
	}

	public long incrementBy( long increment ) {
	//  Effects: Display appropriate progress tic if needed.
	//  Postconditions: count()== original count() + increment
	//  Returns: count().
		if( ( _count += increment ) >= _next_tic_count )  
			displayTic(); 

		return _count;
	}

	public long increment() { return incrementBy( 1 ); }
	public long getCount() { return _count; }
	public long getExpectedCount() { return _expected_count; }
	
	///////////////////////////////

	private void displayTic() {
		// use of floating point ensures that both large and small counts
		// work correctly.  static_cast<>() is also used several places
		// to suppress spurious compiler warnings. 
		final int tics_needed = (int)( ((double)(_count)/_expected_count)*50.0 );
		do 
		{ 
			m_os.print( '*' );
			m_os.flush();
		} while ( ++_tic < tics_needed );
    
		_next_tic_count = (long)( ( _tic / 50.0 ) * _expected_count );
    
		if( _count == _expected_count ) {
			if( _tic < 51 ) 
				m_os.print( '*' );
			m_os.println();
			m_os.flush();
		}
	}
	
	///////////////////////////////
	
	private static void sleep( long milliseconds ) {
		try {     
			Thread.sleep( milliseconds ); 
		} 
		catch(InterruptedException ex) {     
			Thread.currentThread().interrupt(); 
		}			
	}
	
	public static void main( String [] args ) {
		ProgressDisplay progress = new ProgressDisplay( 100 );
		for( int i=0; i<100; ++i ) {
			sleep( 100 );
			progress.increment();
		}
	}
}

// End ///////////////////////////////////////////////////////////////
