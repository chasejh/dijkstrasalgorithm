/**
 * This class stores information about a particular Vertex of a given weighted graph
 */
public class Vertex implements Comparable<Vertex>{
    int distance, parent, id;

    /**
     * This Vertex object stores information about a vertex (city)
     * @param identity order of the city read in by the file
     * @param parentNode the parent of the vertex given a certain path
     * @param dist distance from the source
     */
    public Vertex(int identity, int parentNode, int dist){
        distance = dist;
        parent = parentNode;
        id = identity;
    }

    @Override
    public String toString(){
        return "id[" + id + "] parent[" + parent + "] distance[" + distance + "]";
    }

    @Override
    public int compareTo(Vertex o) {
        if(distance < o.distance){
            return -1;
        }else if(distance > o.distance){
            return 1;
        }else{
            return 0;
        }
    }//compareTo
}
