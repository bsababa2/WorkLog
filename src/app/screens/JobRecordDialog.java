package app.screens;

import app.db.DBManager;
import app.entities.Customer;
import app.entities.Job;
import app.utils.Utils;
import com.alee.extended.date.WebDateField;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.sun.deploy.panel.NumberDocument;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Barak
 * Date: 03/10/13
 * Time: 09:37
 * To change this template use File | Settings | File Templates.
 */
public class JobRecordDialog extends JDialog
{
	public static Dimension DEFAULT_DATE_SIZE = new Dimension(180, 25);
	public static Dimension DEFAULT_PRICE_SIZE = new Dimension(150, 25);
	public static Dimension DEFAULT_LABEL_SIZE = new Dimension(50, 25);
	public static Dimension DEFAULT_BUTTON_SIZE = new Dimension(75, 25);

	public static String DEFAULT_JOB_DESC_TEXT = "עבודה שנעשתה..";
	public static String DEFAULT_REMARKS_TEXT = "הערות..";

	private JXLabel dateLabel = new JXLabel("בתאריך:");
	private WebDateField datePicker = new WebDateField(Calendar.getInstance().getTime());
	private JXLabel customerLabel = new JXLabel("לקוח:");
	private JComboBox<Customer> customerCombo = new JComboBox<Customer>();
	private JXButton addNewCustomerButton = new JXButton("הוספה");
	private JXButton updateCustomerButton = new JXButton("עדכון");
	private WebTextArea jobDescField = new WebTextArea(DEFAULT_JOB_DESC_TEXT);
	private JXLabel priceLabel = new JXLabel("מחיר:");
	private JTextField priceField = new JTextField();
	private JTextArea remarksField = new JTextArea(DEFAULT_REMARKS_TEXT);
	private JXButton feedButton = new JXButton("הזן");
	private JXButton finishButton = new JXButton("סיים");

	private Job returnedJob = null;
	private boolean isFinished = false;
	private Job jobToUpdate = null;

	public JobRecordDialog(Frame owner, Job jobToUpdate)
	{
		super(owner, true);
		this.setTitle("הוספת רשומה חדשה");
		Utils.setSoftSize(this, new Dimension(450, 430));
		this.setLocationRelativeTo(owner);
		this.jobToUpdate = jobToUpdate;

		try
		{
			initComponents();
		}
		catch (Exception e)
		{
			Utils.showExceptionMsg(this, e);
			e.printStackTrace();
			this.dispose();
		}

		GroupPanel datePanel = new GroupPanel(5, Box.createHorizontalStrut(5), dateLabel, datePicker);

		GroupPanel customerPanel = new GroupPanel(5, Box.createHorizontalStrut(5), customerLabel, customerCombo,
			addNewCustomerButton, updateCustomerButton);

		GroupPanel workPanel = new GroupPanel(GroupingType.fillMiddle, Box.createHorizontalStrut(5),
			new WebScrollPane(jobDescField).setPreferredHeight(100), Box.createHorizontalStrut(5));

		GroupPanel pricePanel = new GroupPanel(5, Box.createHorizontalStrut(5), priceLabel, priceField, new JXLabel("ש\"ח"));

		GroupPanel remarksPanel = new GroupPanel(GroupingType.fillMiddle, Box.createHorizontalStrut(5),
			new WebScrollPane(remarksField).setPreferredHeight(100), Box.createHorizontalStrut(5));

		JXPanel mainPanel = new JXPanel();
		Utils.setPageLayout(mainPanel);
		Utils.addStandardRigid(mainPanel);
		mainPanel.add(datePanel);
		Utils.addStandardRigid(mainPanel);
		mainPanel.add(customerPanel);
		Utils.addStandardRigid(mainPanel);
		mainPanel.add(workPanel);
		Utils.addStandardRigid(mainPanel);
		mainPanel.add(pricePanel);
		Utils.addStandardRigid(mainPanel);
		mainPanel.add(remarksPanel);
		Utils.addStandardRigid(mainPanel);

		JXPanel buttonPanel = new JXPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
		buttonPanel.add(feedButton);
		buttonPanel.add(finishButton);

		this.setLayout(new BorderLayout());
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		this.setVisible(true);
	}

	private void initComponents() throws Exception
	{
		priceField.setDocument(new NumberDocument());
		priceField.setText("0");
		jobDescField.setLineWrap(true);
		jobDescField.setWrapStyleWord(true);
		remarksField.setLineWrap(true);
		remarksField.setWrapStyleWord(true);

		addNewCustomerButton.setFocusable(false);
		updateCustomerButton.setFocusable(false);

		datePicker.setDateFormat(WorkLogScr.defaultDateFormat);

		initSizesAndFonts();
		initEventHandlers();
		Utils.initCustomerCombo(customerCombo, false);

		getRootPane().setDefaultButton(jobToUpdate != null ? finishButton : feedButton);

		if (jobToUpdate != null)
		{
			this.setTitle("עדכון רשומה קיימת");

			this.datePicker.setDate(jobToUpdate.getJobDate());
			this.customerCombo.setSelectedItem(jobToUpdate.getCustomer());
			this.jobDescField.setText(jobToUpdate.getJobDescription());
			this.priceField.setText((int)jobToUpdate.getPrice() + "");
			this.remarksField.setText(jobToUpdate.getRemarks());

			feedButton.setVisible(false);
		}
	}

	private void initSizesAndFonts()
	{
		Utils.setSoftSize(customerCombo, DEFAULT_DATE_SIZE);
		Utils.setHardSize(datePicker, DEFAULT_DATE_SIZE);
		Utils.setHardSize(priceField, DEFAULT_PRICE_SIZE);
		Utils.setSoftSize(feedButton, WorkLogScr.DEFAULT_BUTTON_SIZE);
		Utils.setSoftSize(finishButton, WorkLogScr.DEFAULT_BUTTON_SIZE);
		Utils.setHardSize(dateLabel, DEFAULT_LABEL_SIZE);
		Utils.setHardSize(customerLabel, DEFAULT_LABEL_SIZE);
		Utils.setHardSize(priceLabel, DEFAULT_LABEL_SIZE);
		Utils.setSoftSize(addNewCustomerButton, DEFAULT_BUTTON_SIZE);
		Utils.setSoftSize(updateCustomerButton, DEFAULT_BUTTON_SIZE);

		datePicker.setFont(WorkLogScr.DEFAULT_TEXT_FONT);
	}

	private void initEventHandlers()
	{
		addNewCustomerButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String name = Utils.showInputMsg(JobRecordDialog.this, "שם לקוח: ", "הוספת לקוח חדש");
				if (name != null && !name.isEmpty())
				{
					try
					{
						if (DBManager.getSingleton().getCustomerByName(name) != null)
						{
							throw new Exception("הלקוח כבר קיים!");
						}

						Customer customer = new Customer(name);
						customer.setId(DBManager.getSingleton().addCustomer(customer));
						Utils.initCustomerCombo(customerCombo, false);
						customerCombo.setSelectedItem(customer);
					}
					catch (Exception e1)
					{
						Utils.showExceptionMsg(JobRecordDialog.this, e1);
						e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
					}
				}
			}
		});

		updateCustomerButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String name = Utils.showInputMsg(JobRecordDialog.this, "שם הלקוח החדש: ", "עדכון לקוח קיים");
				if (name != null && !name.isEmpty())
				{
					try
					{
						for (int i = 0; i < customerCombo.getModel().getSize(); i++)
						{
							Customer currCustomer = customerCombo.getModel().getElementAt(i);
							if (name.trim().equals(currCustomer.getName().trim()))
							{
								Utils.showErrorMsg(JobRecordDialog.this, "לקוח בשם זה כבר קיים!");
								return;
							}
						}

						Customer customer = (Customer)customerCombo.getSelectedItem();
						customer.setName(name);
						DBManager.getSingleton().updateCustomer(customer);
						Utils.initCustomerCombo(customerCombo, false);
					}
					catch (Exception e1)
					{
						Utils.showExceptionMsg(JobRecordDialog.this, e1);
						e1.printStackTrace();
					}
				}
			}
		});

		jobDescField.addFocusListener(getFocusAdapter(jobDescField, DEFAULT_JOB_DESC_TEXT, false));
		jobDescField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyChar() == KeyEvent.VK_TAB)
				{
					if (e.isShiftDown())
					{
						customerCombo.grabFocus();
					}
					else
					{
						priceField.requestFocus();
					}
					e.consume();
				}
			}
		});
		jobDescField.addMouseListener(getDefaultTextMouseAdapter(jobDescField, DEFAULT_JOB_DESC_TEXT));

		priceField.addFocusListener(getFocusAdapter(priceField, "0", true));

		remarksField.addFocusListener(getFocusAdapter(remarksField, DEFAULT_REMARKS_TEXT, false));
		remarksField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyChar() == KeyEvent.VK_TAB)
				{
					if (e.isShiftDown())
					{
						priceField.grabFocus();
					}
					else
					{
						feedButton.grabFocus();
					}
					e.consume();
				}
			}
		});
		remarksField.addMouseListener(getDefaultTextMouseAdapter(remarksField, DEFAULT_REMARKS_TEXT));

		feedButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					if (priceField.getText().isEmpty() || customerCombo.getSelectedIndex() == -1)
					{
						Utils.showErrorMsg(JobRecordDialog.this, "יש להזין ערכים בכל השדות!");
						return;
					}

					Job job = new Job(datePicker.getDate(), (Customer) customerCombo.getSelectedItem(),
						jobDescField.getText(), Double.parseDouble(priceField.getText()),
						remarksField.getText().equals(DEFAULT_REMARKS_TEXT) ? "" : remarksField.getText());

					job.setId(DBManager.getSingleton().addJob(job));

					JobRecordDialog.this.returnedJob = job;
					JobRecordDialog.this.dispose();
				}
				catch (Exception e1)
				{
					Utils.showExceptionMsg(JobRecordDialog.this, e1);
					e1.printStackTrace();
				}
			}
		});

		finishButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (jobToUpdate != null)
				{
					returnedJob = new Job(jobToUpdate.getId(), datePicker.getDate(), (Customer) customerCombo.getSelectedItem(),
						jobDescField.getText(), Double.parseDouble(priceField.getText()), remarksField.getText());
					returnedJob.setUpdated(true);
				}

				isFinished = true;
				JobRecordDialog.this.dispose();
			}
		});

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
		{
			@Override
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
				{
					JobRecordDialog.this.dispose();
					e.consume();
				}

				return false;
			}
		});
	}

	private MouseAdapter getDefaultTextMouseAdapter(final JTextComponent textComponent, final String defaultText)
	{
		return new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (textComponent.getText().equals(defaultText))
				{
					textComponent.setText("");
				}
			}
		};
	}

	private FocusAdapter getFocusAdapter(final JTextComponent textComponent, final String defaultText, final boolean isPriceField)
	{
		return new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent e)
			{
				if (isPriceField)
				{
					textComponent.selectAll();
				}
				else
				{
					if (!textComponent.getText().isEmpty()) textComponent.setCaretPosition(textComponent.getText().length());
				}
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				if (textComponent.getText().trim().equals(""))
				{
					textComponent.setText(defaultText);
				}
			}
		};
	}

	public Job getReturnedJob()
	{
		return this.returnedJob;
	}

	public boolean isFinished()
	{
		return isFinished;
	}
}
