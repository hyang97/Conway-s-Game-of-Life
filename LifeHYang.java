/*Henry Yang, Period 3, 1/30/14
 * Around 1.5 hours.
 * Code was not too challenging because I broke it up into lots of
 * helper methods. In my opinion, the hardest part of these labs
 * is writing the method to print out the array with the proper 
 * formatting. Everything else is straightforward, as long as the
 * code is organized.
 */
import java.util.*;
import java.io.*;
import java.awt.*;
public class LifeHYang{
    
    Scanner in;
    int[][] board;
    int[][] tempBoard;
    int numRow;
    int numCol;
    
    
    // Constructor that initializes a game of Life from the given data file
    public LifeHYang(String fileName) {
        try{
            in = new Scanner(new File(fileName));
            numRow = in.nextInt();
            numCol = in.nextInt();
            
            board = new int[numRow][numCol];
            while(in.hasNext()){
                board[in.nextInt()][in.nextInt()] = 1;
            }
        }catch(IOException i){
            System.out.println(i.getMessage());
        }
        tempBoard = new int[numRow][numCol];
    }
    
    public LifeHYang(Color[][] array) {
        numRow = array.length;
        numCol = array[0].length;
        board = new int[numRow][numCol];
        for(int r = 0; r < numRow; r++){
            for(int c = 0; c<numCol; c++){
                if(array[r][c] == Color.white){
                    board[r][c] = 0;
                }
                else{
                    board[r][c] = 1;
                }
            }
        }
        tempBoard = new int[numRow][numCol];
    }
    
    
    
    
    
    
    // Method that runs the Life simulation through the given generation
    // Generation 0 is the initial position, Generation 1 is the position after one round of Life, etc...
    public void runLife(int numGenerations) {
        for(int i = 1; i <= numGenerations; i++){
            nextGeneration();
            printBoard();
        }
    
    }
    
    // Advances Life forward one generation
    public void nextGeneration() {
        for(int i = 0; i<numRow; i++){
            for(int j = 0; j<numCol; j++){
                int count = check(j, i);
                if(count > 3 || count == 0 || count == 1){
                    tempBoard[i][j] = 0;
                }
                else if(count == 3){
                    tempBoard[i][j] = 1;
                }
                else if(count == 2 && board[i][j] == 1){
                    tempBoard[i][j] = 1;
                }
            }
        }
        
        
        for(int i = 0; i<numRow; i++){
            for(int j = 0; j<numCol; j++){
                board[i][j] = tempBoard[i][j];
            }
        }
    }
    
    
    public Color[][] nextGeneration(Color[][] array, Color col) {
        
        numRow = array.length;
        numCol = array[0].length;
        Color[][] result = new Color[numRow][numCol];
        for(int r = 0; r < numRow; r++){
            for(int c = 0; c<numCol; c++){
                if(array[r][c] == Color.white){
                    board[r][c] = 0;
                }
                else{
                    board[r][c] = 1;
                }
            }
        }
        tempBoard = new int[numRow][numCol];
        
        for(int i = 0; i<numRow; i++){
            for(int j = 0; j<numCol; j++){
                int count = check(j, i);
                if(count > 3 || count == 0 || count == 1){
                    tempBoard[i][j] = 0;
                }
                else if(count == 3){
                    tempBoard[i][j] = 1;
                }
                else if(count == 2 && board[i][j] == 1){
                    tempBoard[i][j] = 1;
                }
            }
        }
        
        
        for(int i = 0; i<numRow; i++){
            for(int j = 0; j<numCol; j++){
                board[i][j] = tempBoard[i][j];
            }
        }
        
        for(int i = 0; i<numRow; i++){
            for(int j = 0; j<numCol; j++){
                if(board[i][j] == 1){
                    result[i][j] = col;
                }
                else{
                    result[i][j] = Color.white;
                }
            }
        }
        
        return result;
    }
    
    public int check(int x, int y){
        int count = 0;
        if(isInBounds(x+1,y+1)){
            count += board[y+1][x+1];
        }
        if(isInBounds(x-1,y+1)){
            count += board[y+1][x-1];
        }
        if(isInBounds(x,y+1)){
            count += board[y+1][x];
        }
        if(isInBounds(x+1,y-1)){
            count += board[y-1][x+1];
        }
        if(isInBounds(x-1,y-1)){
            count += board[y-1][x-1];
        }
        if(isInBounds(x,y-1)){
            count += board[y-1][x];
        }
        if(isInBounds(x+1,y)){
            count += board[y][x+1];
        }
        if(isInBounds(x-1,y)){
            count += board[y][x-1];
        }
        return count;
    }
    
    public boolean isInBounds(int x, int y){
        if(x >= 0 && y >= 0 && x < numCol && y < numRow){
            return true;
        }
        else{
            return false;
        }
    }
    
    // Method that returns the number of living cells in the given row
    // or returns -1 if row is out of bounds.  The first row is row 1.
    public int rowCount(int row) {
        int count = 0;
        for(int i = 0; i < numCol; i++){
            if(board[row][i] == 1){
                count++;
            }
        }
        return count;
    
    }
    
    // Method that returns the number of living cells in the given column
    // or returns -1 if column is out of bounds.  The first column is column 1.
    public int colCount(int col) {
        int count = 0;
        for(int i = 0; i < numRow; i++){
            if(board[i][col] == 1){
                count++;
            }
        }
        return count;
    
    }
    
    // Method that returns the total number of living cells on the board
    public int totalCount() {
        int count = 0;
        for(int i = 0; i < numRow; i++){
            count += rowCount(i);
        }
        return count;
    
    }
    
    // Prints out the Life array with row and column headers as shown in the example below.
    public void printBoard() {
        for(int i = 0; i < (numRow+1)*(numCol+1); i++){
            if(i == 0){//upper left empty
                System.out.println();
                System.out.printf("%3s", "");
            }
            else if(i%(numCol+1) == 0){//left side numbers
                System.out.println();
                System.out.printf("%3d", i/numCol - 1);
                
            }
            else if(i < numCol+1){//top numbers
                
                if(i==numCol){
                    System.out.printf("%3d", i-1);
                    System.out.println();
                }
                else{
                    System.out.printf("%3d", i-1);
                }
            }
            else{
                if(board[(i)/(numRow+1)-1][(i)%(numRow+1)-1]==1){
                    System.out.printf("%3s", "*");
                }
                else{
                    System.out.printf("%3s", " ");
                }
                
            }
        }
        //System.out.println(Arrays.deepToString(chessboard));
        System.out.println();
        //System.out.println(visited + " squares were visited.");
        System.out.println("Living cells in Row 10: " + rowCount(9));
        System.out.println("Living cells in Column 10: " + colCount(9));
        System.out.println("Total living cells: " + totalCount());
    }
    
    

    
}