package View;

import Controller.DBError;
import Controller.User;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static Controller.MyJTable.buildTable;

public class Users
{

	JPanel mainpanel;
	private JPanel messagepanel;
	private JPanel functionpanel;
	private JTable resulttable;
	private JPanel userpanel;
	private JTextField usernametext;
	private JTextField passwordtext;
	private JRadioButton bookbutton;
	private JRadioButton logbutton;
	private JRadioButton userbutton;
	private JTextField gendertext;
	private JTextField positiontext;
	private JButton selectbutton;
	private JButton updatebutton;
	private JButton insertbutton;
	private JButton deletebutton;

	public Users(User user)
	{
		DBError wrong = user.getWrong();
		ButtonGroup group = new ButtonGroup();
		group.add(bookbutton);  group.add(logbutton);  group.add(userbutton);
		selectbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				/* String username = usernametext.getText().trim();
				ResultSet result = user.searchUser(username);
				if (result != null)
					buildTable(result,resulttable,wrong);
				resulttable.validate(); */

				Map<String,String> conditions = getMap();
				ResultSet result = user.searchUser(conditions);
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
				Map<String,String> attributes = getMap();
				attributes.remove("username");
				String username = usernametext.getText().trim();
				if (username.equals(""))
					wrong.printMessage("用户名不能为空。");
				else
					user.changeUser(username,attributes);
				wrong.isWrong();
			}
		});
		insertbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Map<String,String> attributes = getMap();
				user.addUser(attributes);
				wrong.isWrong();
			}
		});
		deletebutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String username = usernametext.getText().trim();
				if (username.equals(""))
					wrong.printMessage("用户名不能为空。");
				else
					user.deleteUser(username);
				wrong.isWrong();
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
		logbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JRadioButton button = (JRadioButton) e.getSource();
				JFrame frame = (JFrame)button.getRootPane().getParent();
				frame.setVisible(false);
				frame = new JFrame("借阅记录");
				frame.setContentPane(new Logs(user).mainpanel);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocation(450,100);
				frame.setVisible(true);
			}
		});
		resulttable.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int row = resulttable.rowAtPoint(e.getPoint());
				usernametext.setText(resulttable.getValueAt(row, 0).toString());
				gendertext.setText(resulttable.getValueAt(row, 1).toString());
				positiontext.setText(resulttable.getValueAt(row, 2).toString());
				/* Object credit = resulttable.getValueAt(row, 3);
				if (credit == null) passwordtext.setText("");
				else passwordtext.setText(credit.toString()); */
			}
		});
	}

	private Map<String,String> getMap()
	{
		Map<String,String> map = new HashMap<String,String>();
		String username = usernametext.getText().trim();
		if (username.equals("") == false)
			map.put("username","'"+username+"'");
		String password = passwordtext.getText().trim();
		if (password.equals("") == false)
			map.put("password","'"+password+"'");
		String gender = gendertext.getText().trim();
		if (gender.equals("") == false)
			map.put("gender","'"+gender+"'");
		String position = positiontext.getText().trim();
		if (position.equals("") == false)
			map.put("position","'"+position+"'");
		return map;
	}
}
