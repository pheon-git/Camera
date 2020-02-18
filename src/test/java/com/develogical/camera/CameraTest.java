package com.develogical.camera;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class CameraTest {
    Sensor mocksensor = mock(Sensor.class);
    MemoryCard memcard = mock(MemoryCard.class);
    Camera cam = new Camera(mocksensor, memcard);

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {
        cam.powerOn();
        verify(mocksensor).powerUp();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        cam.powerOff();
        verify(mocksensor).powerDown();
    }

    @Test
    public void pressingShutterCopiesData() {
        Sensor mocksensor1 = mock(Sensor.class);
        MemoryCard memcard1 = mock(MemoryCard.class);
        when(mocksensor1.readData()).thenReturn(new byte[]{42});
        Camera cam1 = new Camera(mocksensor1, memcard1);
        cam1.powerOn();
        cam1.pressShutter();
        verify(memcard1).write(eq(new byte[]{42}), any());
        //Why does moving when to the top work?
    }

    @Test
    public void pressingShutterWhenPowerOffDoesNothing() {
        cam.powerOff();
        cam.pressShutter();
        verify(mocksensor, never()).readData();
        verify(memcard, never()).write(eq(mocksensor.readData()), any());
    }

    @Test
    public void doesNotSwitchOffWhenWritingData() {
        cam.powerOn();
        cam.pressShutter();
        cam.powerOff();
        verify(mocksensor, never()).powerDown();
    }

    @Test
    public void shutsDownOnceWriteComplete() {
        cam.powerOn();
        cam.pressShutter();
        cam.powerOff();
        ArgumentCaptor<WriteCompleteListener> listenerCaptor = ArgumentCaptor.forClass(WriteCompleteListener.class);
        verify(mocksensor, never()).powerDown();
        verify(memcard).write(any(), listenerCaptor.capture());
        listenerCaptor.getValue().writeComplete();
        verify(mocksensor).powerDown(); //verify PD
    }
    @Test public void DoNotShutDownOnceWriteComplete(){
        cam.powerOn();
        cam.pressShutter();
        ArgumentCaptor<WriteCompleteListener> listenerCaptor = ArgumentCaptor.forClass(WriteCompleteListener.class);
        verify(mocksensor, never()).powerDown();
        verify(memcard).write(any(), listenerCaptor.capture());
        listenerCaptor.getValue().writeComplete();
        verify(mocksensor, never()).powerDown(); //Dont Verify PD
    }

}
