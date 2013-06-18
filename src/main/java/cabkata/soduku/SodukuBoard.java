package cabkata.soduku;

import java.util.List;
import com.google.common.collect.Lists;

public class SodukuBoard {
	
	public static final int DIMENSION = 9;
	private static final int DIMENSION_SECTOR = 3;
	private final int[][] board;
	private static enum ValidationType{
		ROW,
		COLUMN
	}
	
	private static enum Sector{
		FIRST(0, 0),
		SECOND(0, 3),
		THIRD(0, 6),
		FOURTH(3, 0),
		FIFTH(3, 3),
		SIXTH(3, 6),
		SEVENTH(6, 0),
		EIGTH(6, 3),
		NINTH(6, 6);
		
		private int startRow;
		private int startColumn;
		
		private Sector(int startRow, int startColumn){
			this.startRow = startRow;
			this.startColumn = startColumn;
		}
		
		public boolean isValidSector(int[][] board){
			int[] solutionVector = new int[DIMENSION + 1];
			for(int currentRow = startRow; currentRow < startRow + DIMENSION_SECTOR; currentRow++){
				for(int currentColumn = startColumn; currentColumn < startColumn + DIMENSION_SECTOR; currentColumn++){
					if(isInSolutionVector(solutionVector, board[currentRow][currentColumn])){
						return false;
					}
				}				
			}
			return true;
		}

		public static Sector getSector(int row, int col){
			validateCoordinate(row, col);
			if(row < 3){
				if(col  < 3){
					return Sector.FIRST;
				}
				else if(col < 6){
					return Sector.SECOND;
				}
				else{
					return Sector.THIRD;
				}
			}
			else if(row < 6){
				if(col  < 3){
					return Sector.FOURTH;
				}
				else if(col < 6){
					return Sector.FIFTH;
				}
				else{
					return Sector.SIXTH;
				}
			}
			else{
				if(col  < 3){
					return Sector.SEVENTH;
				}
				else if(col < 6){
					return Sector.EIGTH;
				}
				else{
					return Sector.NINTH;
				}			
			}
		}
	}


	public SodukuBoard(){
		this.board = new int[DIMENSION][DIMENSION];
	}
	

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < DIMENSION; i++){
			for(int j = 0; j < DIMENSION; j++){
				sb.append(board[i][j]).append(" ");
				if((j + 1) % 3 == 0){
					sb.append(" ");
				}
			}
			sb.append("\n");
			if((i + 1) % 3 == 0){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public SodukuBoard(int[][] board){
		this.board = board;
	}
	
	public boolean isValidRowColumnSector(int row, int column){
		validateCoordinate(row, column);
		Sector sector = Sector.getSector(row, column);
		return isValid(column, ValidationType.COLUMN) 
				&& isValid(row, ValidationType.ROW)
				&& sector.isValidSector(board);
	}

	private boolean isValid(int fixedIndex, ValidationType validationType) {
		int[] solutionVector = new int[DIMENSION + 1];
		for(int counter = 0; counter < DIMENSION; counter++){
			int valueAtCoordinate = 0;
			if(ValidationType.COLUMN.equals(validationType)){
				valueAtCoordinate = board[counter][fixedIndex];	
			}
			else if(ValidationType.ROW.equals(validationType)){
				valueAtCoordinate = board[fixedIndex][counter];
			}
			else{
				throw new UnsupportedOperationException("Unsupported Validation type");	
			}
			
			if(isInSolutionVector(solutionVector, valueAtCoordinate)){
				return false;
			}
		}
		return true;
	}

	private static boolean isInSolutionVector(int[] solutionVector, int valueAtCoordinate) {
		//Zero isn't really a value, just the default so means empty
		if(valueAtCoordinate != 0){
			solutionVector[valueAtCoordinate] =  solutionVector[valueAtCoordinate] + 1;
			if(solutionVector[valueAtCoordinate] > 1 ){
				return true;
			}				
		}
		return false;
	}

	private static void validateCoordinate(int i, int j) {
		if(i < 0 || j < 0 || i >= DIMENSION || j >= DIMENSION){
			throw new UnsupportedOperationException("Invalid Coordinate");
		}
	}
	
	public boolean isValidBoard(){
		for(int i = 0; i < DIMENSION; i++){
			if(!isValid(i, ValidationType.COLUMN) || !isValid(i, ValidationType.ROW)){
				return false;
			}
		}
		
		for(Sector sector : Sector.values()){
			if(!sector.isValidSector(board))
				return false;
		}
		return true;
	}
	
	public boolean canMakeMove(){
		for(int currentRow = 0; currentRow < DIMENSION; currentRow++){
			for(int currentColumn = 0; currentColumn < DIMENSION; currentColumn++){
				if(board[currentRow][currentColumn] == 0){
					return true;
				}
			}				
		}
		return false;
	}
	
	public int getValue(int row, int column){
		validateCoordinate(row, column);
		return board[row][column];
	}
	
	public boolean isCellEmpty(int row, int column){
		if(getValue(row, column) == 0){
			return true;
		}
		return false;
	}
	
	public List<SodukuBoard> getPossibleTransitions(int row, int col){
		if(!isCellEmpty(row, col)){
			return Lists.newArrayList(this);
		}
		List<SodukuBoard> moves = Lists.newArrayList();
				
		for(int i = 1; i <= DIMENSION; i++){
			int[][] oldGrid = this.board;
			int[][] newGrid = new int[DIMENSION][DIMENSION];
			
			for(int rowIndex = 0; rowIndex < DIMENSION; rowIndex++){
				System.arraycopy(oldGrid[rowIndex], 0, newGrid[rowIndex], 0, oldGrid[rowIndex].length );	
			}
			
			newGrid[row][col] = i;
			SodukuBoard sodukuBoard = new SodukuBoard(newGrid);
			if(sodukuBoard.isValidRowColumnSector(row, col)){
				moves.add(sodukuBoard);	
			}
		}
		
		return moves;
	}

}
