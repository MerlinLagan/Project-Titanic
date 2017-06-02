// Model for managing templates based on input in MessengerView

/**
 * Created by Tobias on 2017-05-05.
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class TemplatesHandlerModel {

    private HashMap<String, String> templates;
    public HashMap<String, String> getTemplates(){
        return templates;
    }

    public void addTemplate(String key, String value){
        templates.put(key, value);
    }

    public void loadTemplatesMap(String mapName){
        Properties properties = new Properties();
        HashMap<String, String> templates = new HashMap<String, String>();
        templates.put("","");
        try {
            properties.load(new FileInputStream(mapName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String key : properties.stringPropertyNames()) {
            templates.put(key, properties.get(key).toString());
        }
        this.templates = templates;
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

    public String getTemplateFromKey(String key){
        return templates.get(key);
    }

    public void removeTemplate(String key){
        templates.remove(key);
    }
}