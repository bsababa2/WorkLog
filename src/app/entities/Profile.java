package app.entities;

/**
 * Created by Barak on 11/01/14.
 */
public class Profile
{
	private int id;
	private ProfileType type;
	private String description;

	public Profile(int id, ProfileType type, String description)
	{
		this.id = id;
		this.type = type;
		this.description = description;
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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
