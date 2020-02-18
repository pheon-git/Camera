package com.develogical.camera;

public class Camera {

    private final Sensor sensor;
    private final MemoryCard memorycard;
    private boolean cameraPowerStatus;
    private boolean offTriggered;
    private boolean writing;

    public Camera(Sensor sensor, MemoryCard memorycard) {
        this.sensor = sensor;
        this.memorycard = memorycard;
        this.cameraPowerStatus = false;
        this.writing = false;
    }

    public void pressShutter() {
        //this.sensor.powerUp();
        if(this.cameraPowerStatus ==true){
            byte[] sensorData  = this.sensor.readData();
            this.writing = true;
            this.memorycard.write(sensorData,new listener());
        }
    }

    public void powerOn() {
        this.offTriggered = false;
        this.cameraPowerStatus = true;
        this.sensor.powerUp();
    }

    public void powerOff() {
        this.offTriggered= true;
        if(this.writing==false){
            this.cameraPowerStatus = false;
            this.sensor.powerDown();
        }
    }
   class listener implements WriteCompleteListener{
       @Override
       public void writeComplete() {
           Camera.this.writing = false;
           if(Camera.this.offTriggered==true){
               Camera.this.sensor.powerDown();
           }
       }
   }
}

