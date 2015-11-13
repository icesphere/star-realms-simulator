package starrealmssimulator.model;

public class Choice
{
    private int choiceNumber;
    private String text;

    public Choice(int choiceNumber, String text)
    {
        this.choiceNumber = choiceNumber;
        this.text = text;
    }

    public int getChoiceNumber()
    {
        return choiceNumber;
    }

    public String getText()
    {
        return text;
    }
}
