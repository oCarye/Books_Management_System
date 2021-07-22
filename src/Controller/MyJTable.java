package Controller;

import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.*;

public class MyJTable
{
	public static void buildTable(ResultSet rs, JTable table, DBError wrong)
	{
		try
		{
			ResultSetMetaData metaData = rs.getMetaData();

			// names of columns
			Vector<String> columnNames = new Vector<String>();
			int columnCount = metaData.getColumnCount();
			for (int column = 1; column <= columnCount; column++)
			{
				columnNames.add(metaData.getColumnName(column));
			}

			// data of the table
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next())
			{
				Vector<Object> vector = new Vector<Object>();
				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++)
				{
					vector.add(rs.getObject(columnIndex));
				}
				data.add(vector);
			}

			TableModel model =  new DefaultTableModel(data, columnNames)
			{
				@Override
				public boolean isCellEditable(int row, int column)
				{
					//每一个单元格都不可编辑
					return false;
				}
			};
			table.setModel(model);

		}catch(SQLException throwables)
		{
			wrong.addMessage("建立表格出错。");
			wrong.printMessage();
		}
		return;
	}
}
