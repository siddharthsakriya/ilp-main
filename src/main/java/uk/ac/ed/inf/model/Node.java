package uk.ac.ed.inf.model;

import uk.ac.ed.inf.ilp.data.LngLat;

public class Node {
    private Double gScore;
    private Double hScore;
    private LngLat currLngLat;
    private Node parentNode;

    public Node(LngLat currLngLat, Node parentNode, Double gScore, Double hScore){
        this.currLngLat = currLngLat;
        this.parentNode = parentNode;
        this.gScore = gScore;
        this.hScore = hScore;
    }

    public Double getGScore() {
        return gScore;
    }
    public Double getHScore() {
        return hScore;
    }
    public Node getParentLngLat() {
        return parentNode;
    }
    public LngLat getCurrLngLat() {
        return currLngLat;
    }

}
