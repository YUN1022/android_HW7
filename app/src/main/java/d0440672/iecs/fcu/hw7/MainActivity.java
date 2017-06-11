package d0440672.iecs.fcu.hw7;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private HoteArrayAdapter adapter = null;

    private static final int LIST_hotel = 1;

    private Handler handler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what){
                case LIST_hotel:{
                    List<Hotel> hotels = (List<Hotel>)msg.obj;
                    refreshList(hotels);
                    break;
                }
            }
        }
    };

    private void refreshList(List<Hotel> hotels){
        adapter.clear();
        adapter.addAll(hotels);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv;
        lv=(ListView)findViewById(R.id.list);

        adapter=new HoteArrayAdapter(this,new ArrayList<Hotel>());
        lv.setAdapter(adapter);

        getDataFromFirebase();

    }

    public void getDataFromFirebase(){

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                new FirebaseThread(dataSnapshot).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("INF",databaseError.getMessage());
            }
        });

    }
    class FirebaseThread extends Thread{
        private DataSnapshot dataSnapshot;

        public FirebaseThread(DataSnapshot dataSnapshot){
            this.dataSnapshot=dataSnapshot;
        }

        public void run(){
            List<Hotel> lshotel = new ArrayList<>();

            for(DataSnapshot ds:dataSnapshot.getChildren()){
                DataSnapshot dsName=ds.child("Name");
                DataSnapshot dsTel=ds.child("Tel");
                DataSnapshot dsWeb=ds.child("Website");

                String hotelName=(String)dsName.getValue();
                String telephone=(String)dsTel.getValue();
                String website=(String)dsWeb.getValue();

                DataSnapshot dsImg=ds.child("Picture1");
                String imgUrl=(String)dsImg.getValue();
                Bitmap hotelImg=getImgBitmap(imgUrl);

                Hotel ahotel=new Hotel();
                ahotel.setName(hotelName);
                ahotel.setTel(telephone);
                ahotel.setWeb(website);
                ahotel.setImg(hotelImg);
                lshotel.add(ahotel);
                Log.v("INF",hotelName+";"+telephone+";"+website);
            }

            Message msg=new Message();
            msg.what=LIST_hotel;
            msg.obj=lshotel;
            handler.sendMessage(msg);

        }

        private Bitmap getImgBitmap(String imgUrl){
            try{
                URL url=new URL(imgUrl);
                Bitmap bm= BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bm;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

    }
}


