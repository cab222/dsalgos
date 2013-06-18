package cabkata.soduku;

import java.util.List;

import com.google.common.base.Optional;

public class SodukuSolver {


	public static Optional<SodukuBoard> solve(SodukuBoard initialBoard){
		 return solveStartingAt(initialBoard, 0, 0);
	}

	private static Optional<SodukuBoard> solveStartingAt(SodukuBoard currentBoard, int row, int col) {	
		if (!currentBoard.canMakeMove() && currentBoard.isValidBoard()) {
			return Optional.of(currentBoard);
		}
		else{
			List<SodukuBoard> moves = currentBoard.getPossibleTransitions(row, col);
			col++;
			if(col >= SodukuBoard.DIMENSION){
				col = 0;
				row++;
			}			
			
			for (SodukuBoard move : moves) {
				Optional<SodukuBoard> result = solveStartingAt(move, row, col);
				if(result.isPresent()){
					return result;
				}
			}			
		}
		
		return Optional.absent();
	}	
}
