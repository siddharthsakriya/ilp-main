package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;

public class OpenSetVal {
    private LngLat lngLat;
    private double fScore;

    public OpenSetVal(LngLat lngLat, double fScore){
        this.lngLat = lngLat;
        this.fScore = fScore;
    }

    public LngLat getLngLat(){
        return this.lngLat;
    }

    public double getFScore(){
        return this.fScore;
    }

    public void setFScore(double fScore){
        this.fScore = fScore;
    }

    public void setLngLat(LngLat lngLat){
        this.lngLat = lngLat;
    }
}
