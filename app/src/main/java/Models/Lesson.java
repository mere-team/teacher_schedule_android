package Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Lesson {

    public int Id;
    public int Number;
    public String Name;
    public int DayOfWeek;
    public int NumberOfWeek;
    public String Cabinet;

    public int GroupId;
    public Group Group;

    public int TeacherId;
    public Teacher Teacher;

    public String toString(){
        return "N:" + Number + " Name:" + Name + " DW:" + DayOfWeek +
                " NW:" + NumberOfWeek + " C:" + Cabinet + " G:" + Group.Name +
                " T:" + Teacher.Name;
    }

    public Lesson getFromJson(JSONObject json){
        Lesson lesson = new Lesson();
        try{
            lesson.Id = json.getInt("Id");
            lesson.Number = json.getInt("Number");
            lesson.Name = json.getString("Name");
            lesson.DayOfWeek = json.getInt("DayOfWeek");
            lesson.NumberOfWeek = json.getInt("NumberOfWeek");
            lesson.Cabinet = json.getString("Cabinet");

            lesson.GroupId = json.getInt("GroupId");
            lesson.Group = new Group().getFromJson(json.getJSONObject("Group"));

            lesson.TeacherId = json.getInt("TeacherId");
            lesson.Teacher = new Teacher().getFromJson(json.getJSONObject("Teacher"));
        } catch(JSONException e){
            e.printStackTrace();
        }
        return lesson;
    }
}
