package com.team.mere.teacherschedule;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import Helpers.JsonDownloadTask;
import Helpers.JsonHelper;
import Models.Faculty;
import Models.Lesson;

import static Helpers.JsonDownloadTask.*;


public class TeacherActivity extends AppCompatActivity
       implements OnJsonDownloadedListener {

    private LinearLayout llWeek1;
    private LinearLayout llWeek2;
    private JsonHelper helper;
    private static boolean FileIsExist = false;
    private Lesson[] lessons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        setTitle(getIntent().getExtras().getString("TeacherName"));

        llWeek1 = (LinearLayout)findViewById(R.id.week1);
        llWeek2 = (LinearLayout)findViewById(R.id.week2);

        int teacherId = getIntent().getExtras().getInt("TeacherId");
        String url = "http://ulstuschedule.azurewebsites.net/api/teachers/" + teacherId;
        String path = "lessons" + teacherId + ".json";

        helper = new JsonHelper(path, getApplicationContext());
        helper.DownloadJson(url, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onJsonDownloaded(JSONArray data) {
        if(!FileIsExist) {
            helper.SaveJsonToFile(data);
            FileIsExist = true;
        }

        ArrayList<Lesson> lessonsData = null;
        try {
            lessonsData = helper.GetListOfModels(data, new Lesson());
        } catch (JsonHelper.JsonDownloadException e) {
            e.printStackTrace();
            return;
        }

        if(lessonsData.size() != 0)
        {
            lessons = lessonsData.toArray(new Lesson[lessonsData.size()]);

            for (int day = 1; day < 7; day++) {
                Lesson[] week1Lessons = getDayLessons(1, day);
                Lesson[] week2Lessons = getDayLessons(2, day);
                if (week1Lessons.length > 0)
                    llWeek1.addView(createLessonView(week1Lessons));
                if (week2Lessons.length > 0)
                    llWeek2.addView(createLessonView(week2Lessons));
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Пар нет", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private LinearLayout createLessonView(Lesson[] dayLessons){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams layParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (dayLessons == null || dayLessons.length == 0)
            return layout;

        layout.setPadding(0, 0, 0, 16);

        TextView dayOfWeek = new TextView(this);
        dayOfWeek.setText(getDayOfWeek(dayLessons[0].DayOfWeek));
        dayOfWeek.setTextSize(20);
        dayOfWeek.setTypeface(Typeface.DEFAULT_BOLD);
        dayOfWeek.setLayoutParams(layParams);
        layout.addView(dayOfWeek);

        for (Lesson lesson : dayLessons) {
            TextView lessonNumber = new TextView(this);
            lessonNumber.setText(getLessonNumber(lesson.Number));
            lessonNumber.setTextSize(16);
            lessonNumber.setTypeface(Typeface.DEFAULT_BOLD);
            lessonNumber.setPadding(10, 0, 0, 0);
            lessonNumber.setLayoutParams(layParams);
            layout.addView(lessonNumber);

            TextView groupAndCabinet = new TextView(this);
            groupAndCabinet.setText(lesson.Group.Name + "  " + lesson.Cabinet);
            groupAndCabinet.setTextSize(16);
            groupAndCabinet.setPadding(13, 0, 0, 0);
            groupAndCabinet.setLayoutParams(layParams);
            layout.addView(groupAndCabinet);

            TextView lessonName = new TextView(this);
            lessonName.setText(lesson.Name);
            lessonName.setTextSize(16);
            lessonName.setPadding(13, 0, 0, 8);
            lessonName.setLayoutParams(layParams);
            layout.addView(lessonName);
        }

        return layout;
    }

    private String getDayOfWeek(int day){
        String[] daysOfWeek = getResources().getStringArray(R.array.daysOfWeek);
        return daysOfWeek[day - 1];
    }

    private String getLessonNumber(int number){
        String[] lessonNumbers = getResources().getStringArray(R.array.lessonNumbers);
        return lessonNumbers[number - 1];
    }

    private Lesson[] getDayLessons(int week, int day){
        ArrayList<Lesson> dayLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.NumberOfWeek == week && lesson.DayOfWeek == day)
                dayLessons.add(lesson);
        }
        Lesson[] result = dayLessons.toArray(new Lesson[dayLessons.size()]);
        Arrays.sort(result);
        return result;
    }
}
