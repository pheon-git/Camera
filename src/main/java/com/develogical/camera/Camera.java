package com.develogical.camera;

public class Camera {

    private final Sensor sensor;
    private final MemoryCard memorycard;
    private WriteCompleteListener listener;
    private boolean powerStatus;
    private boolean writing;

    public Camera(Sensor sensor, MemoryCard memorycard, WriteCompleteListener listener) {
        this.sensor = sensor;
        this.memorycard = memorycard;
        this.listener = listener;
        this.powerStatus = false;
        this.writing = false;
    }

    public void pressShutter() {
        if(this.powerStatus){
            byte[] sensorData  = this.sensor.readData();
            this.writing = true;
            this.memorycard.write(sensorData,this.listener);
        }
    }

    public void powerOn() {
        this.powerStatus = true;
        this.sensor.powerUp();
    }

    public void powerOff() {
        if(!this.writing){
        this.powerStatus = false;
        this.sensor.powerDown();
        }
    }

}

