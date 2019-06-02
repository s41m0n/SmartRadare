package consegna03.smartRadar.devices;

import java.io.IOException;

import com.pi4j.io.gpio.*;

public class Led implements Light {
	private int pinNum;
	private GpioPinDigitalOutput pin;
	
	public Led(int pinNum){

		this.pinNum = pinNum;
		try {
		    GpioController gpio = GpioFactory.getInstance();
		    pin = gpio.provisionDigitalOutputPin(Config.pinMap[pinNum]);		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void switchOn() throws IOException {
		pin.high();		
	}

	@Override
	public synchronized void switchOff() throws IOException {
		pin.low();		
	}
	
	public synchronized int getWiredPinNum() {
	    return this.pinNum;
	}
	
	public synchronized int getPi4jPinNum() {
            return this.pinNum;
        }
}
