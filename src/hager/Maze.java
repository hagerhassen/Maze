
package hager;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Maze {

	public Maze() {
		
	}
	
	private static int x, y, startX, startY, endX, endY;
	private static String [][] map = null;
	private static final String WALL = "1", SPACE = "0", NOT_VISITED = "nv", VISITING = "vg", VISITED = "vd";
	
	
	private static boolean loadMap() throws IOException {
            
	BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		boolean validMap = true;
		
		String XY = br.readLine();
		String startXY = br.readLine();
		String endXY = br.readLine();
		
		String [] XYLine = XY.split(" ");
		String [] startXYLine = startXY.split(" ");
		String [] endXYLine = endXY.split(" ");
		
		x = Integer.parseInt(XYLine[0]);					
		y  = Integer.parseInt(XYLine[1]);					
		
		startX  = Integer.parseInt(startXYLine[0]);			
		startY  = Integer.parseInt(startXYLine[1]);			
		
		endX  = Integer.parseInt(endXYLine[0]);				
		endY  = Integer.parseInt(endXYLine[1]);					
		
		
		map = new String [x][y];
		
		
		int count = 0;
		
		
		while (true) {

			String line = br.readLine();
			
			
			if (line == null) {
				br.close();
				break;
			}
			String [] s = line.split(" ");
			for(int i=0; i < s.length; i++) {
				map[i][count] = s[i];
			}
			
			count++;
		}
		
		
		if(!map[startX][startY].equals(WALL)) {
			map[startX][startY] = "C";
		}
		else {
			System.out.println("Invalid source co-ordinates");
			validMap = false;
		}
		if(!map[endX][endY].equals(WALL)) {
			map[endX][endY] = "E";
		}
		else {
			System.out.println("Invalid destination co-ordinates");
			validMap = false;
		}
		     
	    return validMap;
	}
	
	public static void solveMaze() throws IOException {
		
		boolean printMaze = false;
		boolean validMap = false;
		
		try {
			validMap = loadMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(validMap) {
			printMaze = BFS();
		}
		
		if(printMaze) {

		    PrintWriter pw = new PrintWriter(new FileWriter("maze_solution.txt"));
		     
		    for (int i=0; i < x; i++) {
		    	
		    	for (int j=0; j < y; j++) {
		    		
		    		if(map[i][j].equals(WALL)) {
		    			pw.write("*");
		    		}
		    		else if(map[i][j].equals(SPACE)) {
		    			pw.write(".");
		    		}
		    		else {
		    			pw.write(map[i][j]);
		    		}
		    		
		    	}
		    	pw.write(System.getProperty("line.separator"));
		    }
		    pw.close();
		}
		else {
			System.err.println("No solution for maze exists");
		}
	}
	
	public static boolean BFS() {
		
		Queue<int []> q = new LinkedList<int []>();
		HashMap<int [], int[]> parent =  new HashMap<int[], int[]>();
		String state [][] = new String [x][y];
		int [] start = {startX, startY};
		int i, j;
		int [] u;
		
		boolean found = false;						
		
		for (i = 0; i < x; i++) {
			for(j = 0; j < y; j++) {
				state[i][j] = NOT_VISITED;
			}
		}
		state[startX][startY] = VISITING;
		
		q.add(start);
		
		while (!q.isEmpty()) {
			u = q.peek();
			
			int nodeX = u[0];
			int nodeY = u[1];
			
			
			if(nodeX > 0 &&
			   state[nodeX-1][nodeY].equals(NOT_VISITED) &&
			   !map[nodeX-1][nodeY].equals(WALL)) {
			   state[nodeX-1][nodeY] = VISITING;
			   int [] currNode = {nodeX-1, nodeY};
			   q.add(currNode);
			   parent.put(currNode, u);
			}
			
                        
			if(nodeY > 0 &&
			   state[nodeX][nodeY-1].equals(NOT_VISITED) &&
			   !map[nodeX][nodeY-1].equals(WALL)) {
			   state[nodeX][nodeY-1] = VISITING;
			   int [] currNode = {nodeX, nodeY-1};
			   q.add(currNode);
			   parent.put(currNode, u);
			}
                        
			
			if(y-nodeY > 1 &&
			   state[nodeX][nodeY+1].equals(NOT_VISITED) &&
			   !map[nodeX][nodeY+1].equals(WALL)) {
			   state[nodeX][nodeY+1] = VISITING;
			   int [] currNode = {nodeX, nodeY+1};
			   q.add(currNode);
			   parent.put(currNode, u);
			}
                        
			
			if(x-nodeX > 1 &&
			   state[nodeX+1][nodeY].equals(NOT_VISITED) &&
			   !map[nodeX+1][nodeY].equals(WALL)) {
			   state[nodeX+1][nodeY] = VISITING;
			   int [] currNode = {nodeX+1, nodeY};
			   q.add(currNode);
			   parent.put(currNode, u);
			}
                        
			state[nodeX][nodeY] = VISITED;

			q.poll();
			
			
			if(map[nodeX][nodeY].equals("E")) {
				found = true;
				break;
			}
		}
		
		if(found) {
			findPath(parent);
		}
		return found;
		
	}
	
	public static void findPath (HashMap<int [], int[]> parent) {
		
		Iterator<Map.Entry<int[], int[]>> it = parent.entrySet().iterator();
		int [] currNode = {endX, endY};
		
		while(it.hasNext()) {
			
			Map.Entry<int[], int[]> me = (Map.Entry<int[], int[]>)it.next();
			if(Arrays.equals(currNode, (int[]) me.getKey())) {
				currNode = (int[]) me.getValue();
				it = parent.entrySet().iterator();
				if(!map[currNode[0]][currNode[1]].equals("C")) {
					map[currNode[0]][currNode[1]] = "X";
				}
				
				if(currNode[0] == startX && currNode[1] == startY) {
					break;
				}
			}
		}		
		
	}
        public static void main (String [] args) throws FileNotFoundException, IOException {
		
   InputStream input = new BufferedInputStream(new FileInputStream("maze_solution.txt"));
        byte[] buffer = new byte[8192];

      try {
          solveMaze();
    for (int length = 0; (length = input.read(buffer)) != -1;) {
        System.out.write(buffer, 0, length);
    }
} finally {
    input.close();
}
        }}
		
		
		

		

		
		
		

      
