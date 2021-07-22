package View;

import Controller.*;

import javax.swing.*;
import java.awt.event.*;

public class Login
{

	public JPanel mainpanel;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JPanel panel4;
	private JPanel loginpanel;
	private JButton loginbutton;
	private JButton findpasswordbutton;
	private JTextField nametext;
	private JTextField passwordtext;
	private JLabel namelabel;
	private JLabel passwordlabel;

	public Login()
	{
		loginbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JButton button = (JButton) e.getSource();
				JFrame frame = (JFrame)button.getRootPane().getParent();
				String username = nametext.getText().trim();
				String password = passwordtext.getText().trim();
				User user = User.getUser(new WrongDialog(),username,password);
				if (user == null) return;
				user.getWrong().isWrong();

				frame.setVisible(false);
				frame = new JFrame("借阅记录");
				frame.setContentPane(new Logs(user).mainpanel);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocation(450,100);
				frame.setVisible(true);
			}
		});
		findpasswordbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new WrongDialog().printMessage("请与管理员联系以重置密码。");
			}
		});
	}
}
