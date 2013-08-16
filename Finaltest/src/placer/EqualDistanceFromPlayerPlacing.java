package placer;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import grid.Direction;
import grid.Grid;
import grid.OnSquareGenerator;
import grid.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import javax.naming.directory.InvalidAttributesException;

import square.Effect;
import square.EffectType;
import square.OnSquare;
import square.OnSquareType;
import square.Player;
import square.Square;
import square.StartingPosition;

public class EqualDistanceFromPlayerPlacing extends PlacingPolicy{
	private OnSquareType type;
	private int number;
	public EqualDistanceFromPlayerPlacing(OnSquareType type,int number){
		this.type = type;
		this.number = number;
	}
	
	@Override
	public void place(Grid grid, ArrayList<OnSquare> onSquares) {
		if (grid == null)
			throw new IllegalArgumentException();
		ArrayList<OnSquare> usables = getUsables(onSquares);  
		Player temp= (Player) OnSquareGenerator.getInstance().makeOnSquare(OnSquareType.PLAYER, new HashSet<EffectType>(),null);
		OnSquare on = usables.get(0);
		ArrayList<Square> availableSquares = null;
		availableSquares = getAvailableSquares(grid, new Position(0,0), new Position(grid.getDimension().getX()-1, grid.getDimension().getY()-1), temp);
		
		ArrayList<Square> out = getPositionsCharged(availableSquares, grid);
        int nb=1;
		while (nb != 0 && !out.isEmpty()) {
			Square squareToPutOn = getRandomSquare(out);
			try {
				squareToPutOn.putOnSquare(on);
				onSquares.remove(on);
				nb--;
			} catch (IllegalOnSquareException e) {
			} catch (InvalidAttributesException e) {
			} catch (IllegalPutException e) {
			}
			out.remove(squareToPutOn);
		}
		
	}


	
	private ArrayList<OnSquare> getUsables(ArrayList<OnSquare> onSquares) {
		ArrayList<OnSquare> usables = new ArrayList<OnSquare>();
		for(OnSquare on:onSquares)
			if(on.getType()==type && number>0){
				usables.add(on);
				number--;
			}
		return usables;
	}
	



	
	private static ArrayList<Square> getPositionsCharged(ArrayList<Square> availableSquares, Grid grid) {
		int[][] matrix = new int[grid.getDimension().getX()][grid.getDimension().getY()];
		int[][] copy = new int[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = 999;
				copy[i][j] = 999;
			}
		}
		
		for (Square s : availableSquares) {
			int x = s.getPosition().getX();
			int y = s.getPosition().getY();
			x = matrix.length-1 - s.getPosition().getX();
			matrix[x][y] = -1;
			copy[x][y] = -1;
		}
		
		Position pos = new Position(matrix.length-1, 0);
		matrix = floodFill(matrix, pos);
		Position pos2 = new Position(0,copy[0].length-1);
		copy = floodFill(copy, pos2);
		int[][] solution = new int [matrix.length][matrix[0].length];
        
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[0].length;j++){
            	if(matrix[i][j] != 999) {
            		solution[i][j] = copy[i][j] - matrix[i][j];
            	} else {
            		solution[i][j] = 999;
            	}
            }
        }
        
        boolean[] bool = loop(solution);
        
        ArrayList<Square> out = new ArrayList<Square>();
        
        if(bool[0]) {
        	out = getAvailableSquaresForOnSq(solution, 0, grid);
        	return out;
        } else if (bool[1]) {
        	out = getAvailableSquaresForOnSq(solution, 1, grid);
        	return out;
        } else if (bool[2]) {
        	out = getAvailableSquaresForOnSq(solution, 2, grid);
        	return out;
        } else {
        	return out;
        }
	}
	
	/**
	 * Apply 8-way flood fill algorithm on given 2d-array which contains the positions 
	 * where the player can go to.
	 * @param 	matrix
	 * @param 	startPos
	 * 			Position where players starts thus from here on apply flood fill algorithm 
	 * @return 	2d-array to represent distance graph of the given param matrix
	 * 			All positions are 
	 */
	private static int[][] floodFill(int[][] matrix, Position startPos) {
		Queue<Position> queue = new LinkedList<Position>();
		queue.add(startPos);
		matrix[startPos.getX()][startPos.getY()] = 0;
		Direction[] generated = Direction.generateOrdered(true);
		
		//System.out.println("dimensies matrix = " + matrix.length + "," + matrix[0].length);

		while (queue.peek() != null) {
			Position pos = queue.remove();

			for(int i=0; i<generated.length; i++) {
				int difx = generated[i].getX();
				int dify = generated[i].getY();
				if ((pos.getX() - difx > -1 && pos.getX() - difx < matrix.length
						&& pos.getY() - dify > -1 && pos.getY() - dify < matrix[0].length) && matrix[pos.getX() - difx][pos.getY() - dify] == -1) {
					matrix[pos.getX()-difx][pos.getY()-dify] = matrix[pos.getX()][pos.getY()] + 1;
					Position test = new Position(pos.getX()-difx, pos.getY()-dify);
					queue.add(test);
				}
			}
		}
		return matrix;
	}
	
	/**
	 * Finds out if a position can be reached such that the length of the shortest path 
	 * from each player to the disc differs by at most 2 squares.
	 * @param 	solution
	 * @return	Boolean array which contains true if the difference in distance of the shortest path 
	 * 			between two players to a given position is either zero, one or two.
	 * 			Returns false array if the shortest path differs more then 2 moves.
	 */
	private static boolean[] loop(int[][] solution) {
		boolean[] bool = new boolean[3];

		int rows = solution.length;
		int columns = solution[0].length;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (solution[i][j] == 0) {
					bool[0] = true;
					return bool;
				} else if (solution[i][j] == 1 || solution[i][j] == -1) {
					bool[1] = true;
					return bool;
				} else if (solution[i][j] == 2 || solution[i][j] == -2) {
					bool[2] = true;
					return bool;
				}
			}
		}
		return bool;
	}
	
	/**
	 * Gives an ArrayList of Squares that contains all the possible Squares where the ChargedIdentityDisk
	 * can be placed.
	 * @param 	solution
	 * 			2d-array containing the difference of the 2 different flood filled array.
	 * @param 	diff
	 * 			gives the difference in distance between two players to a given position
	 * @param 	grid
	 * 			Given grid where ChargedIdentityDisk must be placed on.
	 * @return	An ArrayList of Squares that contains all the possible Squares where the ChargedIdentityDisk
	 * 			can be placed.
	 */
	private static ArrayList<Square> getAvailableSquaresForOnSq(int[][] solution, int diff, Grid grid) {
		ArrayList<Square> out = new ArrayList<Square>();
		
		for (int i = 0; i < solution.length; i++) {
			for (int j = 0; j < solution[0].length; j++) {
				if (solution[i][j] == diff || solution[i][j] == -diff) {
					Position position = new Position(i,j);
					try {
						out.add(grid.getSquare(position));
					} catch (IllegalPositionException e) {
					}
				}
			}
		}
		return out;
	}
	
}
