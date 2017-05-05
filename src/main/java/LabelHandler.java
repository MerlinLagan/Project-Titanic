import java.util.Arrays;


public class LabelHandler{


    public static String[] getLabels(String str){
        boolean recording = false;
        int pos = 0;
        String[] labelList = new String[8];
        String currentLabel = "";
        for (int i = 0; i < str.length(); i++) {

            if (str.charAt(i) == ']'){
                recording = false;
                labelList[pos] = currentLabel;
                currentLabel = "";
                pos++;
            }
            if (recording)
                currentLabel = currentLabel + str.charAt(i);
            if (str.charAt(i) == '['){
                recording = true;
            }
        }
        return shortenList(labelList);
    }

    public static int[] getLabelPositions(String str){
        int pos = 0;
        int[] labelPositionsList = new int[10];
        for (int i = 0; i < str.length(); i++) {

            if (str.charAt(i) == ']'){
                labelPositionsList[pos] = i+1;
                pos++;
            }
            if (str.charAt(i) == '['){
                labelPositionsList[pos] = i+1;
                pos++;
            }
        }
        return shortenList(labelPositionsList);
    }

    public static String[] shortenList(String[] list){
        int length = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null)
                length++;
        }
        String[] shortenedLabelList = new String[length];
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null)
                shortenedLabelList[i] = list[i];
        }
        return shortenedLabelList;
    }

    public static int[] shortenList(int[] list){
        int length = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != 0)
                length++;
        }
        int[] shortenedLabelList = new int[length];
        for (int i = 0; i < list.length; i++) {
            if (list[i] != 0)
                shortenedLabelList[i] = list[i];
        }
        return shortenedLabelList;
    }

	/*
	public String applyLabelsToMessage(String messageText) {
		int[] LabelPositions = LabelHandler.getLabelPositions(messageText);
		String newString = "";
		Boolean firstiteration = true;
		Component[] JTexts = labelField.getComponents();
		for (int i = 1; i <= JTexts.length; i++)
		{
			JTextField currentTextField = (JTextField)JTexts[i-1];
			String currentLabelEntry = currentTextField.getText();
			int leftBracketPos = LabelPositions[(2*(i-1))];
			System.out.println(leftBracketPos);
			int rightBracketPos = LabelPositions[(2*(i)-1)];
			System.out.println(rightBracketPos);
			if (firstiteration == false) {
				int previousRightBracketPos = LabelPositions[(2*(i-2)+1)];
				System.out.println("prev right bracket pos = " + previousRightBracketPos);
				newString = newString + messageText.substring(previousRightBracketPos, leftBracketPos-1) +
						currentLabelEntry;
			}
			else
				newString = messageText.substring(0, leftBracketPos-1) + currentLabelEntry;
			firstiteration = false;
		}
		return newString;
	}
	 */

    public static void main(String[] args) {
		/*
		String string = "Hej [firstname], tack för ett trevligt [action]. jag är en [animal]";
		String[] labels = getLabels(string);
		System.out.println(Arrays.toString(labels));
		int[] labelpositions = getLabelPositions(string);
		System.out.println(Arrays.toString(labelpositions));
		System.out.println(labelpositions[0]);
		*/
    }

}