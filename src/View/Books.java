package View;

import Controller.*;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.*;

import static Controller.MyJTable.*;
import static Controller.Check.*;

public class Books
{

	JPanel mainpanel;
	private JPanel bookpanel;
	private JRadioButton bookbutton;
	private JRadioButton logbutton;
	private JRadioButton userbutton;
	private JTextField idtext;
	private JTextField nametext;
	private JTextField numbertext;
	private JTextField authortext;
	private JTextField publishertext;
	private JPanel messagepanel;
	private JPanel functionpanel;
	private JButton selectbutton;
	private JButton updatebutton;
	private JButton insertbutton;
	private JButton deletebutton;
	private JTable resulttable;

	public Books(User user)
	{
		DBError wrong = user.getWrong();
		ButtonGroup group = new ButtonGroup();
		group.add(bookbutton);  group.add(logbutton);  group.add(userbutton);
		selectbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Map<String,String> conditions = getMap();
				ResultSet result = user.searchBooks(conditions);
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
				Map<String,String> attributes = getMap();
				attributes.remove("id");
				String id = idtext.getText().trim();
				if (checkId(id,wrong))
					user.changeBook(Integer.parseInt(id),attributes);
				wrong.isWrong();
			}
		});
		insertbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Map<String,String> attributes = getMap();
				user.addBook(attributes);
				wrong.isWrong();
			}
		});
		deletebutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String id = idtext.getText().trim();
				if (checkId(id,wrong))
					user.deleteBook(Integer.parseInt(id));
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
				nametext.setText(resulttable.getValueAt(row, 1).toString());
				authortext.setText(resulttable.getValueAt(row, 2).toString());
				publishertext.setText(resulttable.getValueAt(row, 3).toString());
				numbertext.setText(resulttable.getValueAt(row, 4).toString());
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
		String name = nametext.getText().trim();
		if (name.equals("") == false)
			map.put("name","'"+name+"'");
		String number = numbertext.getText().trim();
		if (number.equals("") == false)
			map.put("number",number);
		String author = authortext.getText().trim();
		if (author.equals("") == false)
			map.put("author","'"+author+"'");
		String publisher = publishertext.getText().trim();
		if (publisher.equals("") == false)
			map.put("publisher","'"+publisher+"'");
		return map;
	}
}
