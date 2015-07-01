package Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Faculty {

    public int Id;
    public String Name;

    @Override
    public String toString(){
        return Name;
    }

    public static Faculty getFromJson(JSONObject json){
        Faculty faculty = new Faculty();
        try {
            faculty.Id = json.getInt("Id");
            faculty.Name = json.getString("Name");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return faculty;
    }
}
