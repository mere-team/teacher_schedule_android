package ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.mere.teacherschedule.CathedriesActivity;
import com.team.mere.teacherschedule.FacultiesActivity;
import com.team.mere.teacherschedule.R;
import com.team.mere.teacherschedule.TeachersActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Models.Section;

public class SectionListArrayAdapter extends ArrayAdapter<Section> {

    private ArrayList<Section> _sections;

    public SectionListArrayAdapter(Context context, int resource, int textViewResourceId, List<Section> objects) {
        super(context, resource, textViewResourceId, objects);

        _sections = new ArrayList<>(objects);
    }

    class ViewHolder extends View{
        ImageView image;
        TextView name;

        public ViewHolder(Context context) {
            super(context);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if (convertView == null){
            LayoutInflater vi = (LayoutInflater)getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.section_list_item, null);
            holder = new ViewHolder(getContext());
            holder.image = (ImageView)convertView.findViewById(R.id.sectionListImage);
            holder.name = (TextView)convertView.findViewById(R.id.sectionListLabel);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        Section section = _sections.get(position);
        holder.name.setText(section.Name);
        int[] colors = new int[] { R.color.blue, R.color.aqua, R.color.green, R.color.yellow};
        holder.image.setBackgroundColor(new Random().nextInt((colors.length-1)));
        holder.setTag(section);

        return convertView;
    }


}
