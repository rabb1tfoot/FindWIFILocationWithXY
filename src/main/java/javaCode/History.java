package javaCode;

public class History {
	
	private String[] param;
	/*String ID
	String LAT
	String LNT
	String SEARCH_DT
	*/
	public History()
	{
		this.param = new String[4];
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
