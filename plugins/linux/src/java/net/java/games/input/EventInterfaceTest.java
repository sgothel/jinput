package net.java.games.input;

public class EventInterfaceTest {

    public static void main(String args[]) {
        EventInterface.eventInit();
        int numDevices = EventInterface.getNumDevices();
        
        for(int i=0;i<numDevices;i++) {
            System.out.println("Device name: " + EventInterface.getName(i));
            if(EventInterface.getFFEnabled(i)) {
                try {
                    EventInterface.rumble(i,1f);
                    Thread.sleep(1000);
                    EventInterface.rumble(i,0.8f);
                    Thread.sleep(1000);
                    EventInterface.rumble(i,0.6f);
                    Thread.sleep(1000);
                    EventInterface.rumble(i,0.4f);
                    Thread.sleep(1000);
                    
                    int numAbsAxis = EventInterface.getNumberAbsAxes(i);
                    int numRelAxis = EventInterface.getNumberRelAxes(i);
                    int numButtons = EventInterface.getNumberButtons(i);
                    
                    int absSupported[] = new int[numAbsAxis];
                    int relSupported[] = new int[numRelAxis];
                    int buttonsSupported[] = new int[numButtons];
                    
                    EventInterface.getSupportedAbsAxes(i, absSupported);
                    EventInterface.getSupportedRelAxes(i, relSupported);
                    EventInterface.getSupportedButtons(i, buttonsSupported);
                    
                    EventInterface.poll(i);
                    int absAxisData[] = new int[numAbsAxis];
                    int relAxisData[] = new int[numRelAxis];
                    int buttonData[] = new int[numButtons];
                    EventInterface.getPolledData(i, relAxisData, absAxisData, buttonData);
                    
                    EventInterface.rumble(i,0.2f);
                    Thread.sleep(1000);
                    EventInterface.rumble(i,0f);
                    Thread.sleep(1000);
                    EventInterface.cleanup(i);
                } catch (InterruptedException e) {
                
                }
            }
        }
    }
}

