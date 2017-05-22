import java.util.ArrayList;

public class LogModel {

    ArrayList<String> logMessages;
    ArrayList<Integer> answeredMessages;
    int selectedLogMessageIndex;

    public LogModel(){
        logMessages = new ArrayList<String>();
        selectedLogMessageIndex = 0;
        answeredMessages = new ArrayList<Integer>();
    }

    public void setCurrentMessageAsAnswered(){
        answeredMessages.add(selectedLogMessageIndex);
    }

    public boolean isAnsweredTo(){
        return answeredMessages.contains(selectedLogMessageIndex);
    }

    public int[] getMessagePositions(){
        int[] messagePositionInfo = new int[2];
        messagePositionInfo[0] = logMessages.size();
        messagePositionInfo[1] = selectedLogMessageIndex;
        return messagePositionInfo;
    }

    public void addMessage(String str){
        logMessages.add(str);
    }

    public void addInfoToCurrentMessage(String str){
        logMessages.set(selectedLogMessageIndex, getLogMessage() + "\n \n " + str);
    }

    public String getLogMessage(){
        return logMessages.get(selectedLogMessageIndex);
    }

    public String goToNextMessage(){
        if (!(selectedLogMessageIndex == logMessages.size()-1)) {
            selectedLogMessageIndex++;
            return logMessages.get(selectedLogMessageIndex);
        }
        else throw new ArrayIndexOutOfBoundsException("end reached");
    }

    public String goToPreviousMessage(){
        if (!(selectedLogMessageIndex == 0)) {
            selectedLogMessageIndex--;
            return logMessages.get(selectedLogMessageIndex);
        }
        else throw new ArrayIndexOutOfBoundsException("beginning reached");
    }

    String[] labelList;
    int[] labelPositionsList;
    String message;

    public String getMessage(){
        return message;
    }
    public int getSelectedLogMessageIndex(){
        System.out.println("Logmodel index getted" + selectedLogMessageIndex);
        return selectedLogMessageIndex;
    }

    public String[] getLabels(){
        return labelList;
    }

    public void updateLabels(String str){
        updateLabelList(str);
        updateLabelPositionsInMessage(str);
    }

    public void updateLabelList(String str){
        boolean recording = false;
        int pos = 0;
        labelList = new String[100];
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
        labelList = shortenList(labelList);
    }

    public void updateLabelPositionsInMessage(String str){
        int pos = 0;
        labelPositionsList = new int[100];
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
        labelPositionsList = shortenList(labelPositionsList);
    }

    public String[] shortenList(String[] list){
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


    public void applyLabelsToMessage(String messageText, String[] labels) {

        labelList = labels;

        updateLabelPositionsInMessage(messageText);

        if (labelList.length == 0){
            message = messageText;
            return;
        }

        String newString = "";
        Boolean firstiteration = true;

        for (int i = 1; i <= labelList.length; i++)
        {
            String currentLabelEntry = labelList[i-1];
            int leftBracketPos = labelPositionsList[(2*(i-1))];
            int rightBracketPos = labelPositionsList[(2*(i)-1)];

            if (firstiteration == false) {
                int previousRightBracketPos = labelPositionsList[(2*(i-2)+1)];
                newString = newString + messageText.substring(previousRightBracketPos, leftBracketPos-1) +
                        currentLabelEntry;
                if (i == labelList.length) {
                    newString = newString + messageText.substring(rightBracketPos, messageText.length());
                }
            }
            else {
                newString = messageText.substring(0, leftBracketPos - 1) + currentLabelEntry;
            }
            firstiteration = false;
        }
        message = newString;
    }
}