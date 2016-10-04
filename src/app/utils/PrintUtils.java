package app.utils;

/**
 * Created by Barak on 21/04/2014.
 */

import org.jfree.report.*;
import org.jfree.report.elementfactory.*;
import org.jfree.report.function.PageOfPagesFunction;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;
import org.jfree.util.Log;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;

public class PrintUtils
{
	private static final FontDefinition TITLE_FONT = new FontDefinition("ARIAL", 20, true, false, false, false);
	private static final FontDefinition FOOTER_FONT = new FontDefinition("ARIAL", 14, true, false, false, false);
	private static final FontDefinition CELL_ITEM_FONT = new FontDefinition("ARIAL", 12, false, false, false, false);
	private static final int HEADER_HEIGHT = 30;
	private static final int COLUMN_HEADER_HEIGHT = 30;
	private static final int ROW_HEIGHT = 13;

	static
	{
		JFreeReportBoot.getInstance().start();
	}

	public static void printTable(JFrame owner, JTable tb, String title, String reportFooterText, double[] columnPercentageWidth)
	{
		EntityTableModel tm;
		float columnsWidth[];
		float columnPos[];
		int titleWidth = owner.getFontMetrics(TITLE_FONT.getFont()).stringWidth(title) + 5;

		try
		{
			tm = ((EntityTableModel) tb.getModel()).clone();
			Collections.reverse(tm.getCurrentEntityList());
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			return;
		}
		tm.reverseColumns();
		final JFreeReport report = new JFreeReport();
		report.setName(title);
		PageFormat format = new PageFormat();
		format.setOrientation(PageFormat.LANDSCAPE);
		report.setPageDefinition(new SimplePageDefinition(format));
		columnsWidth = new float[tm.getColumnCount()];
		columnPos = new float[tm.getColumnCount()];
		for (int i = 0; i < tm.getColumnCount(); i++)
		{
			columnsWidth[i] = (float) (report.getPageDefinition().getWidth() * columnPercentageWidth[i]);
		}
		columnPos[0] = 0;
		for (int i = 1; i < tm.getColumnCount(); i++)
		{
			columnPos[i] = columnsWidth[i - 1] + columnPos[i - 1];
		}

		createReportHeader(title, titleWidth, tm, columnsWidth, columnPos, report);
		initPageHeader(title, titleWidth, report);
		initPageFooter(report);
		initReportFooter(report, reportFooterText);

		initReportDataItems(tm, getCellMaxHeight(owner, tm, columnsWidth), columnsWidth, columnPos, report);

		try
		{
			final PreviewDialog preview = new PreviewDialog(report, owner, true);
			preview.setSize(800, 700);
			preview.setLocationRelativeTo(null);
			preview.setVisible(true);
		} catch (ReportProcessingException e)
		{
			Log.error("Failed to generate report ", e);
		}
	}

	private static int getCellMaxHeight(JFrame owner, EntityTableModel tableModel, float[] columnsWidth)
	{
		int cellMaxWidth = 0;
		FontMetrics fontMetrics = owner.getFontMetrics(CELL_ITEM_FONT.getFont());

		int jobCol = tableModel.findColumn(WorkTableModel.JOBS_DESCR_COL);
		for (int i = 0; i < tableModel.getRowCount(); i++)
		{
			int width = fontMetrics.stringWidth(tableModel.getValueAt(i, jobCol).toString());
			if (cellMaxWidth < width) cellMaxWidth = width;
		}

		cellMaxWidth += 5;
		return (int)Math.ceil(cellMaxWidth/columnsWidth[jobCol]) * ROW_HEIGHT;
	}

	private static void createReportHeader(String title, int titleWidth, TableModel tm, float[] columnWidth, float[] columnPos, JFreeReport report)
	{
		final ReportHeader header = new ReportHeader();
		header.setName("Report-Header");
		header.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new Dimension(0, HEADER_HEIGHT + COLUMN_HEADER_HEIGHT));
		header.getStyle().setFontDefinitionProperty(TITLE_FONT);

		final LabelElementFactory factory = getHeaderLabelElementFactory(title, titleWidth, report);
		factory.setName("Report-Header-Label");
		header.addElement(factory.createElement());

		factory.setFontSize(16);
		factory.setHorizontalAlignment(ElementAlignment.CENTER);
		factory.setVerticalAlignment(ElementAlignment.MIDDLE);

		for (int i = 0; i < tm.getColumnCount(); i++)
		{
			factory.setName("Column" + String.valueOf(i));
			factory.setAbsolutePosition(new Point2D.Float(columnPos[i], HEADER_HEIGHT));
			factory.setMinimumSize(new FloatDimension(columnWidth[i], COLUMN_HEADER_HEIGHT));
			factory.setText(tm.getColumnName(i));
			header.addElement(factory.createElement());
			header.addElement(getHeaderShapeElement(columnPos[i]));
		}
		header.addElement(getHeaderShapeElement(report.getPageDefinition().getWidth() - 1));
		header.addElement(getHorizontalLine(HEADER_HEIGHT));
		header.addElement(getHorizontalLine(HEADER_HEIGHT + COLUMN_HEADER_HEIGHT));
		report.setReportHeader(header);
	}

	private static LabelElementFactory getHeaderLabelElementFactory(String title, int titleWidth, JFreeReport report)
	{
		final LabelElementFactory factory = new LabelElementFactory();
		float center = report.getPageDefinition().getWidth() / 2;
		factory.setAbsolutePosition(new Point2D.Float(center - titleWidth / 2, 0));
		factory.setMinimumSize(new Dimension(titleWidth, HEADER_HEIGHT));
		factory.setHorizontalAlignment(ElementAlignment.CENTER);
		factory.setVerticalAlignment(ElementAlignment.MIDDLE);
		factory.setText(title);
		return factory;
	}

	private static ShapeElement getHeaderShapeElement(float x)
	{
		return StaticShapeElementFactory.createRectangleShapeElement(null, null, new BasicStroke(1),
			new Rectangle((int) x, HEADER_HEIGHT, 1, COLUMN_HEADER_HEIGHT), true, false);
	}

	private static void initPageHeader(String title, int titleWidth, JFreeReport report)
	{
		final PageHeader pHeader = new PageHeader();
		pHeader.getStyle().setFontDefinitionProperty(TITLE_FONT);
		pHeader.setName("Page-pHeader");
		final LabelElementFactory rhFactory = getHeaderLabelElementFactory(title, titleWidth, report);
		pHeader.addElement(rhFactory.createElement());
		pHeader.addElement(getHorizontalLine(HEADER_HEIGHT));
		pHeader.setDisplayOnFirstPage(false);
		report.setPageHeader(pHeader);
	}

	private static void initPageFooter(JFreeReport report)
	{
		final PageFooter pageFooter = new PageFooter();
		pageFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension(0, 24));
		LabelElementFactory pgFactory = getLabelElementFactory();
		pgFactory.setVerticalAlignment(ElementAlignment.BOTTOM);
		pageFooter.setName("Page-Footer");

		final PageOfPagesFunction pageFunction = new PageOfPagesFunction();
		pageFunction.setName("pageXofY");
		pageFunction.setFormat("page {0} of {1}");
		report.addExpression(pageFunction);

		final TextFieldElementFactory elementFactory = new TextFieldElementFactory();
		elementFactory.setAbsolutePosition(new Point2D.Float(0, 4));
		elementFactory.setMinimumSize(new FloatDimension(-100, 18));
		elementFactory.setVerticalAlignment(ElementAlignment.MIDDLE);
		elementFactory.setHorizontalAlignment(ElementAlignment.RIGHT);
		elementFactory.setFieldname("pageXofY");
		pageFooter.addElement(elementFactory.createElement());
		report.setPageFooter(pageFooter);
	}

	private static void initReportFooter(JFreeReport report, String reportFooterText)
	{
		final ReportFooter footer = new ReportFooter();
		footer.setName("Report-Footer");
		footer.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension(0, 50));
		footer.getStyle().setFontDefinitionProperty(FOOTER_FONT);

		final LabelElementFactory rfFactory = getLabelElementFactory();
		rfFactory.setVerticalAlignment(ElementAlignment.BOTTOM);
		rfFactory.setText(reportFooterText);
		footer.addElement(rfFactory.createElement());
		report.setReportFooter(footer);
	}

	private static void initReportDataItems(TableModel tm, int cellMaxHeight, float[] columnsWidth, float[] columnPos, JFreeReport report)
	{
		final ItemBand items = report.getItemBand();
		items.getStyle().setFontDefinitionProperty(CELL_ITEM_FONT);
		items.setName("Items");
		TextFieldElementFactory tfFactory = new TextFieldElementFactory();
		initTextCellElementFactory(tfFactory);
		for (int i = 0; i < tm.getColumnCount(); i++)
		{
			if (tm.getColumnName(i).equals(WorkTableModel.DATE_COL))
			{
				tfFactory = new DateFieldElementFactory();
				initTextCellElementFactory(tfFactory);
				((DateFieldElementFactory) tfFactory).setFormat(new SimpleDateFormat("dd/MM/yyyy"));
			}
			else if (tm.getColumnName(i).equals(WorkTableModel.PRICE_COL))
			{
				tfFactory = new NumberFieldElementFactory();
				initTextCellElementFactory(tfFactory);
				((NumberFieldElementFactory) tfFactory).setFormat(NumberFormat.getCurrencyInstance());
			}
			else
			{
				tfFactory = new TextFieldElementFactory();
				initTextCellElementFactory(tfFactory);
			}
			tfFactory.setName(tm.getColumnName(i));
			tfFactory.setMinimumSize(new FloatDimension(columnsWidth[i] - 4, cellMaxHeight));
			tfFactory.setAbsolutePosition(new Point2D.Float(columnPos[i], 0));
			tfFactory.setFieldname(tm.getColumnName(i));
			items.addElement(tfFactory.createElement());
			items.addElement(getVerticalLine(columnPos[i]));
		}

		items.addElement(getVerticalLine(report.getPageDefinition().getWidth()));
		items.addElement(getHorizontalLine(cellMaxHeight));


		report.setData(tm);
	}

	private static void initTextCellElementFactory(TextFieldElementFactory textFieldElementFactory)
	{
		textFieldElementFactory.setColor(Color.black);
		textFieldElementFactory.setNullString("");
		textFieldElementFactory.setHorizontalAlignment(ElementAlignment.CENTER);
		textFieldElementFactory.setVerticalAlignment(ElementAlignment.MIDDLE);
	}

	private static LabelElementFactory getLabelElementFactory()
	{
		final LabelElementFactory factory = new LabelElementFactory();
		factory.setAbsolutePosition(new Point2D.Float(0, 0));
		factory.setMinimumSize(new FloatDimension(-100, 24));
		factory.setHorizontalAlignment(ElementAlignment.CENTER);
		return factory;
	}

	private static ShapeElement getHorizontalLine(float pos)
	{
		return StaticShapeElementFactory.createHorizontalLine("", Color.black, new BasicStroke(1), pos);
	}

	private static ShapeElement getVerticalLine(float pos)
	{
		return StaticShapeElementFactory.createVerticalLine("", Color.black, new BasicStroke(1), pos);
	}
}