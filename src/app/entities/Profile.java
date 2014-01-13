package app.entities;

/**
 * Created by Barak on 11/01/14.
 */
public class Profile
{
	private int id;
	private ProfileType type;
	private String descr;

	public Profile(int id, ProfileType type, String descr)
	{
		this.id = id;
		this.type = type;
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

	public ProfileType getType()
	{
		return type;
	}

	public void setType(ProfileType type)
	{
		this.type = type;
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
