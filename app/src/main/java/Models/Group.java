package Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Group implements IJsonInterface{

    public int Id;
    public String Name;

    @Override
    public String toString(){
        return Name;
    }

    public Group getFromJson(JSONObject json){
        Group group = new Group();
        try {
            group.Id = json.getInt("Id");
            group.Name = json.getString("Name");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return group;
    }
}
