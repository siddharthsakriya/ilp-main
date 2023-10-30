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

    public void setgScore(Double gScore) {
        this.gScore = gScore;
    }
    public void sethScore(Double hScore) {
        this.hScore = hScore;
    }

    public void setParentLngLat(Node parentNode) {
        this.parentNode = parentNode;
    }
    public void setCurrLngLat(LngLat currLngLat) {
        this.currLngLat = currLngLat;
    }

    public Double getgScore() {
        return gScore;
    }
    public Double gethScore() {
        return hScore;
    }
    public Node getParentLngLat() {
        return parentNode;
    }
    public LngLat getCurrLngLat() {
        return currLngLat;
    }

}
