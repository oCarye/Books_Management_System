package View;

import Controller.DBError;

import static javax.swing.JOptionPane.*;

public class WrongDialog implements DBError
{
	private boolean iswrong = false;
	private String message;

	public WrongDialog()  {}
	public WrongDialog(String message)
	{
		this.message = message;
		showMessageDialog(null,message);
	}
	@Override
	public boolean isWrong()
	{
		if (iswrong == false) return false;
		iswrong = false; return true;
	}

	@Override
	public void addMessage(String message)
	{
		iswrong = true;  this.message = message;
	}

	@Override
	public void printMessage()
	{
		showMessageDialog(null,message);
	}

	@Override
	public void printMessage(String message) { iswrong = true;  showMessageDialog(null,message); }
}
