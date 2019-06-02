package consegna03.smartRadar.devices;

import consegna03.smartRadar.common.Event;

public class ButtonPressed implements Event {
	private ButtonImpl source;
	
	public ButtonPressed(ButtonImpl source){
		this.source = source;
	}
	
	public ButtonImpl getSourceButton(){
		return source;
	}
}
