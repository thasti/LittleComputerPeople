package com.example.Demo_Subj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
//import android.util.Log;

public class Dijkstra {

    /**
     * Dijkstra algorithm
     * by Johannes Schoder
     */

    private List<Room> Result;
    private List<Room> Nodes;		//List of all nodes

    private float [][] way;
    private int [] path;				//the shortest path by indizes
    private boolean[] q;
    private float [][] matrix;

    private int start;
    private int end;

    private final float INF = Float.MAX_VALUE;		// constant INF has largest Integer value
    private final float ND = -1; 					// ND = not defined


    //constructor
    public Dijkstra (){

        Nodes = new ArrayList<Room>();
        //Soll leere Liste abfangen
        try{
            Nodes = World.getAllRooms();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        matrix = new float[Nodes.size()][Nodes.size()];
        way = new float [getNodeNumber()][2];
        path = new int[getNodeNumber()];
        q = new boolean[getNodeNumber()];
        Result = new ArrayList<Room>();
        fillMatrix();
    }

    private void fillMatrix(){
        int count = 0;
        int count2 = 0;
        int neighbour_id;
        Room n;
        List<Integer> neighbours_ids;

        //set all matrix elements to INF

        for (count = 0; count < matrix.length; count++){
            for (count2 = 0; count2 < matrix.length; count2++){
                matrix[count][count2] = INF;
            }
        }

        //set all elements of the main diagonal of the matrix to 0

        for (count = 0; count < matrix.length; count++){
            matrix[count][count] = 0;
        }

        //rest f the matrix filled with distance between nodes

        for (count = 0; count < matrix.length; count++){

            //in case try catch fails
            n = World.getRoomById(GlobalInformation.getCurrentRoom());

            try{
                n = Nodes.get(count);
            }catch(NullPointerException e){
                e.printStackTrace();
            }

            neighbours_ids = n.getAttachedRooms();
            for (count2 = 0; count2 < neighbours_ids.size(); count2++){
                neighbour_id = neighbours_ids.get(count2);
                matrix[count][neighbour_id] = 1;
                matrix[neighbour_id][count] = 1;
            }
        }
    }

    public int getNodeNumber(){
        return matrix.length;
    }

    private int smallestNode(){
        int temp = -1;
        for (int i = 0; i < getNodeNumber(); i++) {
            if (q[i] && ((temp==-1)||(way[i][0] < way [temp][0]) ) ){
                temp = i;
            }
        }
        return temp;
    }

    private void shortestPaths (){
        init();
        for (int i = 0; i < getNodeNumber(); i++){
            int u = smallestNode();
            q[u] = false;
            // we are just looking for the way to one single target - so if we have the target we can abort here
            if (u == end){
                return;					//abortion
            }
            for (int v = 0; v < getNodeNumber(); v++){
                if (matrix[u][v] < INF && u != v){
                    if (q[v]){
                        distanceUpdate(u,v);
                    }
                }
            }
        }
        return;
    }

    private void init (){
        for (int v = 0; v < getNodeNumber(); v++){
            way [v][0] = INF;
            way [v][1] = ND;
            q[v] = true;
        }
        way[start][0] = 0;
    }

    private void distanceUpdate (int u, int v){
        float alternativ = way [u][0] + matrix [u][v];
        if (alternativ < way [v][0]){
            way[v][0] = alternativ;		//way[][0] equals abstand[] in pseudocode
            way[v][1] = u;				//way[][1] equals vorgänger[] in pseudocode
        }
    }

    private void createShortestPath (){
        int count = 1;
        path[0] = end;
        int u = end;
        while (way[u][1] != -1){
            u = (int) way[u][1];  //Typecast, u is just the index, but way is float array
            path[count] = u;
            count++;
            //care - path has reversed path from end to start in it!
        }
    }

    public List<Room> dijkstra(Room Start, Room End){

        int elements = 0;
        Result.clear();

        for (int i = 0; i < path.length; i++){
            path[i] = -1;
        }

        start = Start.getID();
        end = End.getID();

        shortestPaths();
        createShortestPath();

        //array has to be shortened and transferred into a list
        //Result.add(Start);

        while ((path[elements] != -1)){
            Result.add(Nodes.get(path[elements]));
            elements++;
            if (elements == path.length){
                break;
            }
        }
/*        for (elements = 0; ((path[elements] != -1) && (elements < path.length)); elements++){
            Result.add(Nodes.get(path[elements]));
        }*/

        // List includes the inverse path
        // inversion of the path

        Collections.reverse(Result);

        return Result;
    }
}

//Anmerkungen: Damit das ganze Funktioniert, muss die Raumliste numerisch geordnet sein und mit ElementID 0 anfangen:
//Sprich das erste Element der Raumliste ist auch der Raum mit der ID "0".
//Ansonsten muss ich den Algorithmus umbauen (fillMatrix).
