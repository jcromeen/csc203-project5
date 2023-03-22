import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy
{
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors)
    {
        Point current; //the point being iterated upon
        List<Point> path = new ArrayList<>(); //return the path
        List<Point> openset = new ArrayList<>(); //points to iterate upon; current is chosen from this set
        List<Point> closedset = new ArrayList<>(); //already iterated upon points
        HashMap<Point, Integer> Gcost = new HashMap<>(); //A mapping from points to their respective g values
        HashMap<Point, Integer> Fcost = new HashMap<>(); //A mapping from points to their respective f values
        HashMap<Point, Point> previous = new HashMap<>(); //A mapping from a point to its previous point; used to reconstruct the final path

        openset.add(start); //Add start point to the openset
        Gcost.put(start, 0); //g of start
        Fcost.put(start, getDist(start, end)); //f of start

        while(openset.size() != 0){
            Point min = openset.get(0);
            for(Point p : openset){
                if(Fcost.get(p) < Fcost.get(min)){
                    min = p;
                }
            }
            current = min; //Choose a current point from the openset set with the smallest f value
            if(withinReach.test(current, end)){
                //Reconstruct the path using the previous points mapping
                while(current != start){
                    path.add(0, current);
                    current = previous.get(current);
                }
                break; //If the current point is the end point, go to step 8
            }

            //Otherwise, analyze all valid neighbor points that are not in the closed set
            List<Point> neighbors = potentialNeighbors.apply(current).filter(canPassThrough).filter(p -> !p.equals(start) && !p.equals(end) && !closedset.contains(p)).collect(Collectors.toList());
            for(Point np : neighbors){ //a valid point adjacent to the current point
                if(!closedset.contains(np)){ //Otherwise, analyze all valid neighbor points that are not in the closed set
                    if(!openset.contains(np)){
                        openset.add(np); //If the neighbor is not in the open set, add it to the open set
                    }
                }
                int gtemp = Gcost.get(current) + 1; //Calculate the neighbor’s g value
                if(!Gcost.containsKey(gtemp) || gtemp < Gcost.get(np)){ //If the neighbor’s g value is better than a previously calculated g value (or is the first one calculated)
                    Gcost.put(np, gtemp); //record the neighbor’s new g value
                    int htemp = getDist(np, end); //Calculate the distance from the neighbor to the end point
                    int ftemp = gtemp + htemp; //Calculate the neighbor’s f value
                    Fcost.put(np, ftemp); //record f value
                    previous.put(np, current); //Record the neighbor’s previous point
                }
            }
            closedset.add(current); //Move the current node to the closed set
            openset.remove(current); //need to remove from open set
        }



        return path;
    }

    public int getDist(Point start, Point end){
        return Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
    }

}
