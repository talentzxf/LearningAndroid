package com.example.vincentzhang.learnandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vincentzhang.Sprite.SpriteWorld;

import static com.example.vincentzhang.Sprite.DIRECTIONS.DOWNLEFT;
import static com.example.vincentzhang.Sprite.DIRECTIONS.DOWNRIGHT;
import static com.example.vincentzhang.Sprite.DIRECTIONS.UNKNOWN;
import static com.example.vincentzhang.Sprite.DIRECTIONS.UPLEFT;
import static com.example.vincentzhang.Sprite.DIRECTIONS.UPRIGHT;

public class MainActivity extends AppCompatActivity {
    private GestureDetector detector;

    class MyOnTouchListener implements View.OnTouchListener{
        private SpriteWorld world;
        public MyOnTouchListener(SpriteWorld world){
            this.world = world;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_BUTTON_RELEASE:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    world.onClick(UNKNOWN);
                    return false;
            }

            switch(v.getId()){
                case R.id.leftbutton:
                    // world.onClick(LEFT);
                    world.onClick(DOWNLEFT);
                    break;
                case R.id.rightbutton:
                    // world.onClick(RIGHT);
                    world.onClick(UPRIGHT);
                    break;
                case R.id.upbutton:
                    // world.onClick(UP);
                    world.onClick(UPLEFT);
                    break;
                case R.id.downbutton:
                    // world.onClick(DOWN);
                    world.onClick(DOWNRIGHT);
                    break;
                case R.id.button_a:
                    world.onClick('A');
                    break;
            }

            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_main);
        }catch(Exception e){
            Log.e("Exception happened!", "Exception during initing main window", e);
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        final MyGameView gameView = (MyGameView) findViewById(R.id.gamesurfaceview);

        Button leftButton = (Button) findViewById(R.id.leftbutton);
        Button rightButton = (Button) findViewById(R.id.rightbutton);
        Button upButton = (Button) findViewById(R.id.upbutton);
        Button downButton = (Button) findViewById(R.id.downbutton);
        Button AButton = (Button) findViewById(R.id.button_a);

        MyOnTouchListener myOnTouchListener = new MyOnTouchListener(gameView.getWorld());
        leftButton.setOnTouchListener(myOnTouchListener);
        rightButton.setOnTouchListener(myOnTouchListener);
        upButton.setOnTouchListener(myOnTouchListener);
        downButton.setOnTouchListener(myOnTouchListener);
        AButton.setOnTouchListener(myOnTouchListener);
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
