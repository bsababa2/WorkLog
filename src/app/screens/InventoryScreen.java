package app.screens;

import app.entities.ProfileType;
import app.utils.InventoryTableModel;
import app.utils.Utils;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.spinner.WebSpinner;
import com.alee.laf.text.WebTextField;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;

public class InventoryScreen extends JDialog
{
	private InventoryTableModel inventoryTableModel = new InventoryTableModel();
	private JXTable inventoryTable = new JXTable(inventoryTableModel);
	private JXButton addButton = new JXButton("הוסף");
	private JComboBox<ProfileType> profileTypeCombo = new JComboBox<ProfileType>();
	private WebTextField profileField = new WebTextField();
	private WebSpinner quantitySpinner = new WebSpinner();
	private WebTextField remarksField = new WebTextField();

	public InventoryScreen(JFrame owner)
	{
		super(owner, true);
		this.setTitle("ניהול מלאי");
		Utils.setSoftSize(this, new Dimension(800, 600));
		this.setLocationRelativeTo(owner);

		GroupPanel northPanel = new GroupPanel(GroupingType.fillAll, profileTypeCombo, profileField,
			quantitySpinner, remarksField, addButton);

		initSizes();

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(northPanel, BorderLayout.NORTH);
		this.getContentPane().add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
	}

	private void initSizes()
	{
		inventoryTable.getColumn(InventoryTableModel.PROFILE_TYPE_COL).setMinWidth(100);
		inventoryTable.getColumn(InventoryTableModel.PROFILE_TYPE_COL).setMaxWidth(200);
		inventoryTable.getColumn(InventoryTableModel.QUANTITY_COL).setMinWidth(70);
		inventoryTable.getColumn(InventoryTableModel.QUANTITY_COL).setMaxWidth(70);
	}
}
