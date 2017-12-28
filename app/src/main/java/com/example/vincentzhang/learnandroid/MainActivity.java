package com.example.vincentzhang.learnandroid;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vincentzhang.Sprite.Controller.ButtonEventDispatcher;
import com.example.vincentzhang.Sprite.Controller.ButtonEventListener;
import com.example.vincentzhang.Sprite.Utilities;

import com.example.vincentzhang.learnandroid.R;

import static com.example.vincentzhang.Sprite.DIRECTIONS.DOWN;
import static com.example.vincentzhang.Sprite.DIRECTIONS.LEFT;
import static com.example.vincentzhang.Sprite.DIRECTIONS.RIGHT;
import static com.example.vincentzhang.Sprite.DIRECTIONS.UNKNOWN;
import static com.example.vincentzhang.Sprite.DIRECTIONS.UP;

public class MainActivity extends AppCompatActivity {
    private GestureDetector detector;

    class MyOnTouchListener implements View.OnTouchListener{
        private ButtonEventListener l;
        public MyOnTouchListener(){
            l = ButtonEventDispatcher.inst();
        }

        private ImageButton createImageButton(Context context, int imgId){
            ImageButton imgButton = new ImageButton(context);
            imgButton.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), imgId));
            imgButton.setScaleType(ImageView.ScaleType.FIT_XY);
            imgButton.setAdjustViewBounds(true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
            imgButton.setLayoutParams(params);
            return imgButton;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_BUTTON_RELEASE:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    l.onClick(UNKNOWN);
                    return false;
            }

            LinearLayout buttonGallery = (LinearLayout)findViewById(R.id.button_gallery);

            switch(v.getId()){
                case R.id.leftbutton:
                     l.onClick(LEFT);
                    // l.onClick(DOWNLEFT);
                    break;
                case R.id.rightbutton:
                    l.onClick(RIGHT);
                    // l.onClick(UPRIGHT);
                    break;
                case R.id.upbutton:
                    l.onClick(UP);
                    // l.onClick(UPLEFT);
                    break;
                case R.id.downbutton:
                    l.onClick(DOWN);
                    // l.onClick(DOWNRIGHT);
                    break;
                case R.id.button_attack:
                    // l.onClick('A');
//                    android:layout_width="wrap_content"
//                    android:layout_height="match_parent"
//                    android:adjustViewBounds="true"
//                    android:scaleType="fitXY"

                    buttonGallery.removeAllViews();

                    buttonGallery.addView(createImageButton(buttonGallery.getContext(), R.drawable.bomb));
                    break;
                case R.id.button_building:
                    buttonGallery.removeAllViews();
                    buttonGallery.addView(createImageButton(buttonGallery.getContext(), R.drawable.magic_tower));
                    buttonGallery.addView(createImageButton(buttonGallery.getContext(), R.drawable.hospital));
//                case R.id.button_c:
//                    l.onClick('C');
            }

            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_main);
        }catch(Exception e){
            Log.e("Exception happened!", "Exception during initing main window", e);
            return;
        }

        Log.i("Memory info", "Max memory:" + Runtime.getRuntime().maxMemory() + "," + " Cur memory:" + Runtime.getRuntime().totalMemory());

        Utilities.verifyStoragePermissions(this);



        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        final MyGameView gameView = (MyGameView) findViewById(R.id.gamesurfaceview);

        Button leftButton = (Button) findViewById(R.id.leftbutton);
        Button rightButton = (Button) findViewById(R.id.rightbutton);
        Button upButton = (Button) findViewById(R.id.upbutton);
        Button downButton = (Button) findViewById(R.id.downbutton);
        Button AButton = (Button) findViewById(R.id.button_attack);
        Button BButton = (Button) findViewById(R.id.button_building);
        // Button CButton = (Button) findViewById(R.id.button_c);

        MyOnTouchListener myOnTouchListener = new MyOnTouchListener();
        leftButton.setOnTouchListener(myOnTouchListener);
        rightButton.setOnTouchListener(myOnTouchListener);
        upButton.setOnTouchListener(myOnTouchListener);
        downButton.setOnTouchListener(myOnTouchListener);
        AButton.setOnTouchListener(myOnTouchListener);
        BButton.setOnTouchListener(myOnTouchListener);
        //CButton.setOnTouchListener(myOnTouchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
