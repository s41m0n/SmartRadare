package consegna03.smartRadar.devices;

import consegna03.smartRadar.common.Event;

public class ButtonReleased implements Event {
	private ButtonImpl source;
	
	public ButtonReleased(ButtonImpl source){
		this.source = source;
	}
	
	public ButtonImpl getSourceButton(){
		return source;
	}
}
