package control.user.com.usercontrol.act.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;



public class UserListAdapter extends ArrayAdapter<String>{
    private Context context;
    private List<String> objects;

    public UserListAdapter(@NonNull Context context, int resource, List<String> objects){
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        TextView nameView = new TextView(context);
        nameView.setTextColor(Color.BLACK);
        nameView.setTextSize(25);
        nameView.setText(objects.get(position));
        return nameView;

    }
}
