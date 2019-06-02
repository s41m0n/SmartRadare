package consegna03.smartRadar.common;

public interface Serial {
	
	/* async interface */
	boolean isMsgAvailable();
	void	sendMsg(String msg);
	
	/* sync interface */
	String 	waitForMsg() throws InterruptedException;
}
