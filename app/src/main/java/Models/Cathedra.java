package Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Cathedra implements IJsonInterface{

    public int Id;
    public String Name;

    @Override
    public String toString(){
        return Name;
    }

    public Cathedra getFromJson(JSONObject json){
        Cathedra cathedra = new Cathedra();
        try {
            cathedra.Id = json.getInt("Id");
            cathedra.Name = json.getString("Name");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return cathedra;
    }

}
