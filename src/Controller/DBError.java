package Controller;

public interface DBError
{
	public boolean isWrong();
	public void addMessage(String message);
	public void printMessage();
	public void printMessage(String message);
}
