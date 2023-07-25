import java.util.*;


public class maze {
    static public class DisjSet {
        private int[] s;  //set
        public DisjSet(int numElements) {
            s = new int[numElements];
            Arrays.fill(s,-1);
        }
        public void union(int root1, int root2) { //sets root2's parent to be root1
            s[root2] = root1;
        }
        public int find(int x) {   //returns root of element being searched for (The head of set containing x)
            if (s[x] < 0)
                return x;          //in this case, x is the root.
            else
                return find(s[x]);
        }
    }
    static boolean search(String[] inputArr, String key) {               //helper search function, used to find walls
        for(int i = 0; i < inputArr.length; i++) {
            if(inputArr[i].equals(key))
                return true;
        }
        return false;
    }
    //NEXT FOUR FUNCTIONS ARE HELPERS FOR PATH FINDING. THEY SEARCH IN DIFFERENT DIRECTIONS. IF CONDITIONS AREN'T MET, FALSE IS RETURNED

    static boolean searchDown(int cellValue, int columnSize, String[] walls, Stack<Integer> path, Stack<Integer> knownCells) {
        int nextCell = cellValue + columnSize;
        String boundaryName = cellValue + "," + nextCell;
        if(!search(walls,boundaryName) && knownCells.search(nextCell) == -1) {                    //if there isn't a wall between cellValue and nextCell AND if nextCell isn't known
            path.push(nextCell);
            knownCells.push(nextCell);
            return true;
        }
        else
            return false;
    }
    static boolean searchRight(int cellValue, String[] walls, Stack<Integer> path, Stack<Integer> knownCells) {
        int nextCell = cellValue + 1;
        String boundaryName = cellValue + "," + nextCell;
        if(!search(walls,boundaryName) && knownCells.search(nextCell) == -1) {                    //if there isn't a wall between cellValue and nextCell AND if nextCell isn't known
            path.push(nextCell);
            knownCells.push(nextCell);
            return true;
        }
        else
            return false;
    }
    static boolean searchUp(int cellValue, int columnSize, String[] walls, Stack<Integer> path, Stack<Integer> knownCells) {
        int nextCell = cellValue - columnSize;
        String boundaryName = nextCell + "," + cellValue;           //reversed to match with wall
        if(!search(walls,boundaryName) && knownCells.search(nextCell) == -1) {                    //if there isn't a wall between cellValue and nextCell AND if nextCell isn't known
            path.push(nextCell);
            knownCells.push(nextCell);
            return true;
        }
        else
            return false;
    }
    static boolean searchLeft(int cellValue, String[] walls, Stack<Integer> path, Stack<Integer> knownCells) {
        int nextCell = cellValue - 1;
        String boundaryName = nextCell + "," + cellValue;           //reversed to match with wall
        if(!search(walls,boundaryName) && knownCells.search(nextCell) == -1) {                    //if there isn't a wall between cellValue and nextCell AND if nextCell isn't known
            path.push(nextCell);
            knownCells.push(nextCell);
            return true;
        }
        else
            return false;
    }

    //drawMaze converts the imaginary grid the list of walls uses into a 2D array that can have characters placed at specific indexes to represent walls
    static void drawMaze(int rows, int columns, String[] walls, String path) { //draws the array based on information from path and walls
        int drawnRows = rows+2+(rows-1);                            //Essentially, each square in the maze now has a square in between each other, to make room to draw the wall in the console
        int drawnColumns = columns+2+(columns-1);                   //This extra space is the (rows/columns -1) part. The +2 accounts for the boundary walls on the top, bottom, left, right

        char[][] drawnMaze = new char[drawnRows][drawnColumns];        //puts a space as the characters in drawnArray

        for(int i = 0; i < drawnRows; i++) {
            for (int j = 0; j < drawnColumns; j++)

                drawnMaze[i][j] = ' ';
        }


        Arrays.fill(drawnMaze[0], '#');             //fills in the borders, and creates an open space for entrance and exit
        Arrays.fill(drawnMaze[drawnRows-1], '#');
        for(int i = 0; i < drawnRows; i++) {
            if (i != 1)
                drawnMaze[i][0] = '#';
            if (i != drawnRows-2)
                drawnMaze[i][drawnColumns-1] = '#';
        }

        for(int i = 0; i < walls.length; i++) {                           // this section draws in the walls according to the walls array
            int border = walls[i].indexOf(',');
            int gridPos = Integer.parseInt(walls[i].substring(0,border));               //gridPos will be used to find location of corresponding area in drawnMaze.
            int directionNum = Integer.parseInt(walls[i].substring(border+1)); //directionNum, in relation to gridPos, will determine the direction of the wall

            if (gridPos + 1 == directionNum) { //wall is to the right
                drawnMaze[2*(gridPos/columns) + 1][2 * (gridPos % columns) + 2] = '#';
            }
            else if (gridPos - 1 == directionNum) { // wall is to the left
                drawnMaze[2*(gridPos/columns) + 1][2 * (gridPos % columns)] = '#';
            }
            else if (gridPos + columns == directionNum) { //wall is beneath current cell
                drawnMaze[2*(gridPos/columns) + 2][2 * (gridPos % columns) + 1] = '#';
            }
            else if (gridPos - columns == directionNum) { //wall is above current cell
                drawnMaze[2*(gridPos/columns)][2 * (gridPos % columns) + 1] = '#';
            }
            else
                System.out.println("IMPOSSIBLE WALL IN LIST");
        }



        for(int i = 0; i < drawnRows; i++) {        //prints contents of array
            for (int j = 0; j < drawnColumns; j++) {
                if ((i % 2 == 0) && (j % 2 == 0))   //fills in squares that are in the corners of locations found using gridPos
                    drawnMaze[i][j] = '#';
                System.out.print(drawnMaze[i][j]);
            }
            System.out.println();
        }
        System.out.println("Press 'p' to display the path taken. Press anything else to end the program.");
        Scanner input = new Scanner(System.in);
        int curX = 1;
        int curY = 1;
        if (input.next().equals("p")) {
            for(int i = 0; i < path.length(); i++) {
                if (path.charAt(i) == 'N') {
                    drawnMaze[curX][curY] = '*';
                    curX--;
                    drawnMaze[curX][curY] = '*';
                    curX--;
                }
                if (path.charAt(i) == 'E') {
                    drawnMaze[curX][curY] = '*';
                    curY++;
                    drawnMaze[curX][curY] = '*';
                    curY++;
                }
                if (path.charAt(i) == 'S') {
                    drawnMaze[curX][curY] = '*';
                    curX++;
                    drawnMaze[curX][curY] = '*';
                    curX++;
                }
                if (path.charAt(i) == 'W') {
                    drawnMaze[curX][curY] = '*';
                    curY--;
                    drawnMaze[curX][curY] = '*';
                    curY--;
                }
            }
            for(int i = 0; i < drawnRows; i++) {        //prints contents of array
                for (int j = 0; j < drawnColumns; j++) {
                    System.out.print(drawnMaze[i][j]);
                }
                System.out.println();
            }

        }

    }




    public static void main(String[] args) {
        int rows;
        int columns;
        System.out.println("Enter row count, then column count");
        Scanner input = new Scanner(System.in);
        rows = input.nextInt();
        columns = input.nextInt();

        if (rows < 2 || columns < 2) {
            System.out.println("Maze dimensions too narrow, please enter values greater than 1");
            return;
        }

        DisjSet cells = new DisjSet(rows*columns);

        int rootCount = rows*columns;              //keeps track of how many completely walled off sections there are
        String[] wallArr = new String [(2*rows*columns)-rows-columns];              //list of every wall in grid. 2*rows*columns-rows-columns is # of interior walls
        int curWallIndex = 0;                                                               //stored as string value of adjacent cells, separated by comma

        for(int i = 0; i < rows; i++) {               //This loop creates a list of all inner walls in the grid. Outside borders aren't included
            for(int j = 0; j < columns; j++) {
                if (j != columns-1) {
                    if(i != rows-1) { //if not on final row, pair with cells to the right and beneath
                        wallArr[curWallIndex] = (i*columns+j) + "," + (i*columns+j+1); //right cell
                        curWallIndex++;
                        wallArr[curWallIndex] = (i*columns+j) + "," + (i*columns+j+columns); //lower cell
                        curWallIndex++;
                    }
                    else {//if on final row, pair only with right cell
                        wallArr[curWallIndex] = (i * columns + j) + "," + (i*columns+j+1); //right cell
                        curWallIndex++;
                    }
                }
                else {        //if on final column
                    if (i != rows-1) { //if not on final row, pair only with cell beneath
                        wallArr[curWallIndex] = (i*columns+j) + "," + (i*columns+j+columns); //lower cell
                        curWallIndex++;
                    }
                }
            }
        }
        Random rand = new Random();
        for(int i = 0; i < wallArr.length; i++) {           //shuffles array of individual walls, so they are chosen randomly
            int index = rand.nextInt(wallArr.length);
            String temp = wallArr[index];
            wallArr[index] = wallArr[i];
            wallArr[i] = temp;
        }

        int removedWallCount = 0;
        for (int i = 0; i < wallArr.length; i++) {              //removes walls until all cells are connected
            String curWall = wallArr[i];
            int border = curWall.indexOf(',');
            int cell1 = Integer.parseInt(curWall.substring(0,border));
            int cell2 = Integer.parseInt(curWall.substring(border+1));

            if (cells.find(cell1) != cells.find(cell2)) {       //if cell1 and cell2 aren't in the same set, remove the wall and union the sets
                cells.union(cells.find(cell1), cells.find(cell2));
                rootCount--;
                wallArr[i] = "-";
                removedWallCount++;
            }
            if (rootCount == 1)                                 //if all cells are connected, break
                break;
        }

        int newIndex = 0;
        String[] actualWallArr = new String[wallArr.length-removedWallCount];      //creates array of valid walls. Previous wallArr has many useless positions
        for(int i = 0; i < wallArr.length; i++) {
            if(wallArr[i] != "-") {
                actualWallArr[newIndex] = wallArr[i];
                newIndex++;
            }
        }

        Stack<Integer> path = new Stack<Integer>();                               //this stack will contain the path through the maze
        Stack<Integer> knownCells = new Stack<Integer>();                         //this stack will contain all the cells that have been searched
        path.push(0);
        knownCells.push(0);
        while (true) {                                                            //searches for maze path, breaks when (rows*columns-1) is pushed.
            int cellValue = path.peek();
            if(cellValue == (rows*columns-1))
                break;

            boolean inFirstRow = cellValue < columns;
            boolean inFirstColumn = cellValue % columns == 0;
            boolean inFinalRow = cellValue > (rows*columns-1) - columns;
            boolean inFinalColumn = (cellValue % columns) == columns - 1;

            if(inFirstRow || (inFirstColumn && !inFinalRow)) {                                    //if in first row or (first column and not final row), only look down and right. If either one works, continue
                boolean down = searchDown(cellValue, columns, actualWallArr, path, knownCells);   //we don't want final row because we shouldn't look down if in the first column and final row
                if (down) continue;

                boolean right = searchRight(cellValue, actualWallArr, path, knownCells);
                if (right) continue;

                                                              //if neither direction can be performed, pop the cell from path;
                path.pop();                                   //the popped cell remains in knownCells, so it won't be chosen again
            }
            else if (inFinalRow) {                       //if in bottom row, only look up and right. If either one works, continue
                boolean up = searchUp(cellValue, columns, actualWallArr, path, knownCells);
                if (up) continue;

                boolean right = searchRight(cellValue, actualWallArr, path, knownCells);
                if (right) continue;


                path.pop();                                                      //if neither direction can be performed, pop the cell from path;
            }
            else if(inFinalColumn) {                          //if in final column and not final row, only look down and left. If either one works, continue
                boolean down = searchDown(cellValue, columns, actualWallArr, path, knownCells);
                if (down) continue;

                boolean left = searchLeft(cellValue, actualWallArr, path, knownCells);
                if (left) continue;

                path.pop();                                                     //if neither direction can be performed, pop the cell from path;

            }
            else {                                                        //the cell isn't on an edge, so we search in all four directions
                boolean down = searchDown(cellValue, columns, actualWallArr, path, knownCells);
                if (down) continue;

                boolean left = searchLeft(cellValue, actualWallArr, path, knownCells);
                if (left) continue;

                boolean up = searchUp(cellValue, columns, actualWallArr, path, knownCells);
                if (up) continue;

                boolean right = searchRight(cellValue, actualWallArr, path, knownCells);
                if (right) continue;

                path.pop();

            }
        }
        int[] cellPath = new int[path.size()];
        String strCellPath = "";
        for(int i = path.size()-1; i >= 0; i--) {       //repeated stack pops will fill in an array in reverse order to get the final path
            cellPath[i] = path.pop();
        }
        for(int i = 0; i < cellPath.length-1; i++) {              //prints path taken in terms of direction taken
            if(cellPath[i]+1 == cellPath[i+1])        // east
                strCellPath += "E";
            else if (cellPath[i]+columns == cellPath[i+1]) //south
                strCellPath += "S";
            else if(cellPath[i]-1 == cellPath[i+1])       //west
                strCellPath += "W";
            else if (cellPath[i]-columns == cellPath[i+1])
                strCellPath += "N";         //north
            else
                System.out.println("CELL PATH MAKES IMPOSSIBLE MOVE");
        }
        System.out.println(strCellPath);

        drawMaze(rows, columns, actualWallArr, strCellPath);
        System.out.println();
    }
}
