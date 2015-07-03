package Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Faculty implements IJsonInterface{

    public int Id;
    public String Name;

    public Faculty() {
    }

    public Faculty(int Id, String Name)
    {
        this.Id = Id;
        this.Name = Name;
    }

    @Override
    public String toString(){
        return Name;
    }

    public Faculty getFromJson(JSONObject json){
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
