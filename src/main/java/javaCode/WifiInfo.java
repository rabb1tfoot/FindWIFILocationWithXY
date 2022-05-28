package javaCode;

public class WifiInfo
{
	private String[] param;
	//distance
	/*String X_SWIFI_MGR_NO;
	String X_SWIFI_WRDOFC;
	String X_SWIFI_MAIN_NM;
	String X_SWIFI_ADRES1;
	String X_SWIFI_ADRES2;
	String X_SWIFI_INSTL_FLOOR;
	String X_SWIFI_INSTL_TY;	
	String X_SWIFI_INSTL_MBY;	
	String X_SWIFI_SVC_SE;	
	String X_SWIFI_CMCWR;	
	String X_SWIFI_CNSTC_YEAR;
	String X_SWIFI_INOUT_DOOR;
	String X_SWIFI_REMARS3;	
	String LAT;	
	String LNT;	
	String WORK_DTTM;*/
	public WifiInfo()
	{
		this.param = new String[17];
	}
	public int ParamSize()
	{
		return this.param.length;
	}
	public String GetParam(int index)
	{
		return param[index];
	}
	public void SetParam(int index, String value)
	{
		param[index] = new String();
		param[index] = value;
	}

}