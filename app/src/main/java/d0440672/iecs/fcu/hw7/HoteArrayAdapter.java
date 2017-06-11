package d0440672.iecs.fcu.hw7;

import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by 123 on 2017/6/11.
 */

public class HoteArrayAdapter extends ArrayAdapter<Hotel>{

    Context context;

    public HoteArrayAdapter (Context context, ArrayList<Hotel> items){
        super(context,0,items);
        this.context=context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater=LayoutInflater.from(context);

        LinearLayout itemlayout=null;
        if(convertView==null){
            itemlayout = (LinearLayout)inflater.inflate(R.layout.hotlelist,null);
        }
        else{
            itemlayout=(LinearLayout)convertView;
        }

        Hotel item=(Hotel)getItem(position);
        TextView name=(TextView)itemlayout.findViewById(R.id.name_tv);
        name.setText(item.getName());
        TextView tel=(TextView)itemlayout.findViewById(R.id.tel_tv);
        tel.setText(item.getTel());
        TextView web=(TextView)itemlayout.findViewById(R.id.web_tv);
        web.setText(item.getWeb());
        ImageView img=(ImageView)itemlayout.findViewById(R.id.hotel_iv);
        img.setImageBitmap(item.getImg());

        return itemlayout;
    }
}
