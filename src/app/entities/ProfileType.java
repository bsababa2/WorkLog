package app.entities;

/**
 * Created by Barak on 11/01/14.
 */
public class ProfileType
{
	private int id;
	private String descr;

	public ProfileType(int id, String descr)
	{
		this.id = id;
		this.descr = descr;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getDescr()
	{
		return descr;
	}

	public void setDescr(String descr)
	{
		this.descr = descr;
	}
}
