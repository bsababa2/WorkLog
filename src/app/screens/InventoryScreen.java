package app.screens;


import app.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Barak on 11/01/14.
 */
public class InventoryScreen extends JDialog
{
	public InventoryScreen(JFrame owner)
	{
		super(owner, true);
		this.setTitle("ניהול מלאי");
		Utils.setSoftSize(this, new Dimension(800, 600));
		this.setLocationRelativeTo(owner);
	}
}