package consegna03.smartRadar.devices;

import consegna03.smartRadar.common.Event;

public class ScanInfoReceived implements Event {
        private String message;
        
        public ScanInfoReceived(String msg){
                this.message = msg;
        }
        
        public String getMessage(){
                return this.message;
        }
}
