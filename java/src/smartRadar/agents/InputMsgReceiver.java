package consegna03.smartRadar.agents;

import consegna03.smartRadar.common.BasicController;

import consegna03.smartRadar.common.BasicEventLoopController;
import consegna03.smartRadar.common.Serial;
import consegna03.smartRadar.devices.ScanInfoReceived;

public class InputMsgReceiver extends BasicController {

	private Serial serialDevice;
	private BasicEventLoopController centralUnit;
	
	public InputMsgReceiver(Serial serialDevice, BasicEventLoopController cu){
		this.serialDevice = serialDevice;
		this.centralUnit = cu;
	}
	
	@Override
	public void run() {
		while (true){
			try {
			    String msg = serialDevice.waitForMsg();
			    this.centralUnit.notifyEvent(new ScanInfoReceived(msg));
			} catch (Exception ex){
			    ex.printStackTrace();
			}
		}
	}

}
