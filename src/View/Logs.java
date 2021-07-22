package View;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static Controller.Check.*;
import static Controller.MyJTable.buildTable;

public class Logs
{
	JPanel mainpanel;
	private JPanel logpanel;
	private JRadioButton bookbutton;
	private JRadioButton logbutton;
	private JRadioButton userbutton;
	private JPanel messagepanel;
	private JPanel functionpanel;
	private JTextField idtext;
	private JTextField user_nametext;
	private JTextField book_idtext;
	private JTextField numbertext;
	private JTextField outdatetext;
	private JTextField indatetext;
	private JButton selectbutton;
	private JButton updatebutton;
	private JButton borrowbutton;
	private JButton returnbutton;
	private JTable resulttable;

	public Logs(User user)
	{
		DBError wrong = user.getWrong();
		ButtonGroup group = new ButtonGroup();
		group.add(bookbutton);  group.add(logbutton);  group.add(userbutton);
		ResultSet record = user.getRecord();
		if (record != null)
			buildTable(record,resulttable,wrong);
		selectbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Map<String,String> conditions = getMap();
				ResultSet result = user.searchLogs(conditions);
				JTable table = null;
				if (result != null)
					buildTable(result,resulttable,wrong);
				resulttable.validate();
				wrong.isWrong();
			}
		});
		updatebutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				wrong.printMessage("记录暂不允许修改。");
			}
		});
		borrowbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String book_id = book_idtext.getText().trim();
				String number = numbertext.getText().trim();
				String outdate = outdatetext.getText().trim();
				if (checkId(book_id,wrong)  && checkId(number,wrong))
					user.borrowBooks(Integer.parseInt(book_id),Integer.parseInt(number),outdate);
				wrong.isWrong();
			}
		});
		returnbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String id = idtext.getText().trim();
				String indate = indatetext.getText().trim();
				if (indate.equals(""))
				{
					wrong.printMessage("必须指明还书日期。");
					return;
				}
				if (checkId(id,wrong))
					user.returnBooks(Integer.parseInt(id),indate);
				wrong.isWrong();
			}
		});
		resulttable.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int row = resulttable.rowAtPoint(e.getPoint());
				idtext.setText(resulttable.getValueAt(row, 0).toString());
				user_nametext.setText(resulttable.getValueAt(row, 1).toString());
				book_idtext.setText(resulttable.getValueAt(row, 2).toString());
				numbertext.setText(resulttable.getValueAt(row, 3).toString());
				outdatetext.setText(resulttable.getValueAt(row, 4).toString());
				Object indate = resulttable.getValueAt(row, 5);
				if (indate == null) indatetext.setText("");
				else indatetext.setText(indate.toString());
			}
		});
		bookbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JRadioButton button = (JRadioButton) e.getSource();
				JFrame frame = (JFrame)button.getRootPane().getParent();
				frame.setVisible(false);
				frame = new JFrame("图书管理");
				frame.setContentPane(new Books(user).mainpanel);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocation(500,110);
				frame.setVisible(true);
			}
		});
		userbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JRadioButton button = (JRadioButton) e.getSource();
				JFrame frame = (JFrame)button.getRootPane().getParent();
				frame.setVisible(false);
				frame = new JFrame("用户管理");
				frame.setContentPane(new Users(user).mainpanel);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocation(550,120);
				frame.setVisible(true);
			}
		});
	}

	private Map<String,String> getMap()
	{
		Map<String,String> map = new HashMap<String,String>();
		String id = idtext.getText().trim();
		if (id.equals("") == false)
			map.put("id",id);
		String user_name = user_nametext.getText().trim();
		if (user_name.equals("") == false)
			map.put("user_name","'"+user_name+"'");
		String book_id = book_idtext.getText().trim();
		if (book_id.equals("") == false)
			map.put("book_id",book_id);
		String number = numbertext.getText().trim();
		if (number.equals("") == false)
			map.put("number",number);
		String outdate = outdatetext.getText().trim();
		if (outdate.equals("") == false)
			map.put("outdate","'"+outdate+"'");
		String indate = indatetext.getText().trim();
		if (indate.equals("") == false)
			map.put("indate","'"+indate+"'");
		return map;
	}
}
