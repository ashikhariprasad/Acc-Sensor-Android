package com.sjsu.proj.accsensor;

/**
 * Created by ashik on 4/20/2015.
 */
public class DataObject{

    private float currentAcceleration;
    private double currentNoiseInDb;

    public void updateAcceleration(float a){
        this.currentAcceleration = a;
    }
    public float getAcceleration(){
        return this.currentAcceleration;
    }
    public void updateNoise(double n){
        this.currentNoiseInDb = n;
    }
    public double getNoise(){
        return currentNoiseInDb;
    }
}
