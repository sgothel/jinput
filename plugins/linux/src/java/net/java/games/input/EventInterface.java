package net.java.games.input;

public class EventInterface {
    
    static {
        System.out.println("Loading jinput-linux");
        System.loadLibrary("jinput-linux");
    }
    
    public static native void eventInit();
    public static native int getNumDevices();
    public static native boolean getFFEnabled(int device);
    public static native String getName(int device);
    public static native void rumble(int device, float force);
    public static native int getBusType(int device);
    public static native int getVendorID(int device);
    public static native int getProductID(int device);
    public static native int getVersion(int device);
    public static native void getSupportedRelAxes(int device, int supportedAxis[]);
    public static native void getSupportedAbsAxes(int device, int supportedAxis[]);
    public static native void getSupportedButtons(int device, int supportedButtons[]);
    public static native int poll(int device);
    public static native void getPolledData(int device, int relAxesData[], int absAxesData[], int buttonData[]);
    public static native int getAbsAxisMinimum(int device, int axisNumber);
    public static native int getAbsAxisMaximum(int device, int axisNumber);
    public static native int getAbsAxisFuzz(int device, int axisNumber);
    public static native int isValidDevice(int device);
    public static native void cleanup(int device);
    public static native int getNumberRelAxes(int device);
    public static native int getNumberAbsAxes(int device);
    public static native int getNumberButtons(int device);
    
    public static void main(String args[]) {
        eventInit();
        int numDevices = getNumDevices();
        
        for(int i=0;i<numDevices;i++) {
            System.out.println("Device name: " + getName(i));
            if(getFFEnabled(i)) {
                try {
                    rumble(i,1f);
                    Thread.sleep(1000);
                    rumble(i,0.9f);
                    Thread.sleep(1000);
                    rumble(i,0.8f);
                    Thread.sleep(1000);
                    rumble(i,0.7f);
                    Thread.sleep(1000);
                    rumble(i,0.6f);
                    Thread.sleep(1000);
                    rumble(i,0.5f);
                    Thread.sleep(1000);
                    rumble(i,0.4f);
                    Thread.sleep(1000);
                    rumble(i,0.3f);
                    Thread.sleep(1000);
                    rumble(i,0.2f);
                    Thread.sleep(1000);
                    rumble(i,0.1f);
                    Thread.sleep(1000);
                    rumble(i,0f);
                    Thread.sleep(1000);
                    cleanup(i);
                } catch (InterruptedException e) {
                
                }
            }
        }
    }

}
