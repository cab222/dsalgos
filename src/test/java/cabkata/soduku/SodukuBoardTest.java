package cabkata.soduku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Optional;

public class SodukuBoardTest {
	private static final int[][] SAMPLE_BOARD = new int[][]{
		{0, 0, 0, 	0, 0, 0, 	0, 1, 2},
		{0, 0, 0, 	0, 3, 5, 	0, 0, 0},
		{0, 0, 0, 	6, 0, 0, 	0, 7, 0},
		
		{7, 0, 0, 	0, 0, 0, 	3, 0, 0},
		{0, 0, 0, 	4, 0, 0, 	8, 0, 0},
		{1, 0, 0, 	0, 0, 0, 	0, 0, 0},
		
		{0, 0, 0, 	1, 2, 0, 	0, 0, 0},
		{0, 8, 0, 	0, 0, 0, 	0, 4, 0},
		{0, 5, 0, 	0, 0, 0, 	6, 0, 0}
	};
	
	private static final String EXPECTED_BOARD = 
			"6 7 3  8 9 4  5 1 2  \n" +  
			"9 1 2  7 3 5  4 8 6  \n" +  
			"8 4 5  6 1 2  9 7 3  \n\n"  +
			
		  	"7 9 8  2 6 1  3 5 4  \n"  + 
			"5 2 6  4 7 3  8 9 1  \n"  +
			"1 3 4  5 8 9  2 6 7  \n\n"  +
			
			"4 6 9  1 2 8  7 3 5  \n"  +
			"2 8 7  3 5 6  1 4 9  \n"  +
			"3 5 1  9 4 7  6 2 8  \n\n";
	
	@Test
	public void testValidation(){
		SodukuBoard board = new SodukuBoard(SAMPLE_BOARD);
		assertTrue(board.isValidRowColumnSector(0, 0));
		SAMPLE_BOARD[0][0] = 7;
		board = new SodukuBoard(SAMPLE_BOARD);
		assertFalse(board.isValidRowColumnSector(0, 0));
	}
	
	@Test
	public void testSolving(){
		SodukuBoard board = new SodukuBoard(SAMPLE_BOARD);
		long before = System.currentTimeMillis();
		Optional<SodukuBoard> result = SodukuSolver.solve(board);
		long after = System.currentTimeMillis();
		long milliDuration = after - before;
		assertTrue(TimeUnit.MILLISECONDS.toSeconds(milliDuration) < 60);
		assertTrue(result.isPresent());
		assertEquals(EXPECTED_BOARD, result.get().toString());
	}
	
}
