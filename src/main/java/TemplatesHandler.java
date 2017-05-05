    /**
     * Created by Tobias on 2017-05-05.
     */
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.util.HashMap;
    import java.util.Properties;

    public class TemplatesHandler {

            private HashMap<String, String> templates;


            public HashMap<String, String> getTemplates(){
                return templates;
            }

            public void loadTemplatesMap(String mapName){
                Properties properties = new Properties();
                HashMap<String, String> newTemplates = new HashMap<String, String>();
                try {
                    properties.load(new FileInputStream(mapName));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (String key : properties.stringPropertyNames()) {
                    newTemplates.put(key, properties.get(key).toString());
                }
                templates = newTemplates;
            }

            public void saveTemplatesMap(){
                Properties properties = new Properties();
                properties.putAll(templates);
                try {
                    properties.store(new FileOutputStream("data.properties"), null);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            public void action(String clickedTemplate, MessengerGUI gui){

                String message = templates.get(clickedTemplate);
                gui.setMessageBox(message);
                gui.labelField.removeAll();

                String[] labels = LabelHandler.getLabels(message);
                for (String str : labels) {
                    gui.addLabel(str);
                }
            }
        }