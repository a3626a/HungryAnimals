package oortcloud.hungryanimals.core.network;

public class SyncIndex {
	//HandlerGeneralClient
	public static final int HERBICIDE_SETBLOCK = 0;
	
	//HandlerGeneralServer
	public static final int ENTITYOVERLAY_SYNC = 0;
	public static final int ENTITYOVERLAY_EDIT_INT = 2;
	public static final int ENTITYOVERLAY_EDIT_DOUBLE = 3;
	public static final int ENTITYOVERLAY_SYNC_REQUEST = 4;
	
	//HandlerPlayerServer
	public static final int DEBUG_SETTARGET = 0;
	
	public static final int TAMING_LEVEL_SYNC = 0;
	public static final int STOMACH_SYNC = 1;
	public static final int PRODUCTION_SYNC = 2;
}
