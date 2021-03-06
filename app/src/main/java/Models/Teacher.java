package Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Teacher implements IJsonInterface {

    public int Id;
    public String Name;

    public int CathedraId;
    public Cathedra Cathedra;

    @Override
    public String toString(){
        return Name;
    }

    public Teacher getFromJson(JSONObject json){
        Teacher teacher = new Teacher();
        try{
            teacher.Id = json.getInt("Id");
            teacher.Name = json.getString("Name");
            teacher.CathedraId = json.getInt("CathedraId");

            teacher.Cathedra = new Cathedra().getFromJson(json.getJSONObject("Cathedra"));
        } catch (JSONException e){
            e.printStackTrace();
        }
        return teacher;
    }
}
