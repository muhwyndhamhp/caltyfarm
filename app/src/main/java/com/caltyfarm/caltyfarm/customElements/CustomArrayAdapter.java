package com.caltyfarm.caltyfarm.customElements;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.caltyfarm.caltyfarm.R;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {
    // Initialise custom font, for example:
    Typeface font = Typeface.createFromAsset(getContext().getAssets(),
            "font/Constantia.ttf");

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    public CustomArrayAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(font);
        view.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        view.setTextSize(18);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }
}
