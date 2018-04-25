/*
Author: Jessica Chase
Program Description: This program implements Dijkstra's shortest
path closest pair algorithm to find the optimal detour to circumvent
road closures.
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class OptimalDetour {
    
    //members
    int numCities; // number of cities in the map
    final int MAX = Integer.MAX_VALUE; // to initialize array of potential verticies
    int[][] mtrx; // adjacency matrix
    HashMap<String, Integer> cities; // map the cities to indexable integers
    String[] citiesArray; // array of city names
    PriorityQueue<Vertex> q; // the priority of the verticies

    /**
     * The constructor
     * @param roads the file to pass to the reader
     */
    public OptimalDetour(String roads){
        cities = new HashMap<>();
        read(roads);
    }

    /**
     * Get the optimal detour using Dijkstra's algorithm
     * @param city1 beginning city
     * @param city2 end city
     */
    private void getOptimalDetour(int city1, int city2) {
        boolean[] intree = new boolean[numCities];
        Vertex v = null;
        ArrayList<Vertex> used = new ArrayList<>();
        ArrayList<Vertex> visited = new ArrayList<>(numCities);

        int finalDist = 0;

        Vertex source = new Vertex(city1, -1, 0);
        q.add(source);

        //while the end city isn't in the tree
        while(!intree[city2]){
            v = q.poll(); //look at the first thing in the queue
            //System.out.println(v);
            used.add(v);

            //update all the distances of the neighbors of the current vertex
            for(int i = 0; i < numCities; i++){
                //if there's a path and the given city isn't in the tree
                if(mtrx[v.id][i] != 0 && !intree[i]){
                    int newDist = mtrx[v.id][i] + v.distance;
                    //add it to the queue - compare the distances
                    q.add(new Vertex(i, v.id, newDist));
                }//if
            }//for

            intree[v.id] = true;

            if(intree[city2]){
                finalDist = v.distance;
            }

        }//while

        //backtrack through the parents
        visited.add(v);
        int vParent = v.parent;
        int d = 0;
        while(!used.isEmpty() && d < used.size()){
            if(used.get(d).id == vParent){
                visited.add(used.get(d));
                vParent = used.get(d).parent;
                used.remove(d);
                d = -1;
            }
            d++;
        }

        //print the output
        for(int c = 0; c < visited.size(); c++) {
            System.out.print(citiesArray[visited.get(c).id]);
            if(c != visited.size()-1) System.out.print(" <--> ");
        }
        System.out.println("\n" + "Total distance: " + finalDist + " miles");

    }

    /**
     * Reads the file and constructs the adjacency matrix
     * @param file to read
     */
    public void read(String file){
        Scanner scan;
        try {
            scan = new Scanner(new File(file));
            numCities = scan.nextInt(); // get # of cities
            citiesArray = new String[numCities];
            mtrx = new int[numCities][numCities]; // initialize the size


            scan.nextLine(); //clear the buffer

            // map cities to ints through an array
            for(int i = 0; i < numCities; i++){
                String city = scan.nextLine();
                cities.put(city, i); // map the cities to ints
                citiesArray[i] = city; // map ints to cities

            }//for

            // initialize the adjacency matrix with the lengths of paths between cities
            while(!scan.hasNext(".")){
                //mark the road with the weight
                String road = scan.nextLine();
                String[] s = road.split("\\s*,\\s*");
                int city1 = cities.get(s[0]);
                int city2 = cities.get(s[1]);
                int dist = Integer.parseInt(s[2]);
                mtrx[city1][city2] = dist;
                mtrx[city2][city1] = dist;
            }//while

            scan.nextLine(); //skip the period

            // find optimal detour at a given closure
            while (scan.hasNext()){
                q = new PriorityQueue<>(numCities);
                String closures = scan.nextLine();
                String[] closed;
                closed = closures.split(",\\s*");

                String startCity = closed[0], endCity = closed[1];
                boolean c1 = cities.containsKey(startCity), c2 = cities.containsKey(endCity);

                //make sure its a recognized city
                if(c1 && c2) {
                    int city1 = cities.get(closed[0]);
                    int city2 = cities.get(closed[1]);
                    int beforeClosure = mtrx[city1][city2];
                    mtrx[city1][city2] = 0; //mark road as closed

                    //initialize Priority Queue
                    for(int i = 0; i < numCities; i++){
                        int id = cities.get(citiesArray[i]);
                        q.add(new Vertex(id, -1, MAX));
                    }//for


                    //find optimal detour
                    getOptimalDetour(city1, city2);
                    System.out.println();


                    //reopen closed roads
                    mtrx[city1][city2] = beforeClosure;

                }else{
                    if(!c1){
                        System.out.println(startCity + " is not a recognized city." + "\n");
                    }else{
                        System.out.println(endCity + " is not a recognized city." + "\n");
                    }
                }//else

            }//while
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }//catch
    }//read



}//class
