package com.example.crystalball;


import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crystalball.ShakeDetector.OnShakeListener;

public class MainActivity extends Activity {
	private CrystalBall ball = new CrystalBall();
	private TextView answerView;
	private Button getAnswer;
	private ImageView crystalBallImage;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private ShakeDetector shakeDetector;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        //Instantiate layout member variables 
        answerView = (TextView) findViewById(R.id.textView1);
        getAnswer = (Button) findViewById(R.id.button1);
    	crystalBallImage = (ImageView)findViewById(R.id.imageView1);
        
    	
    	//For button press event
        getAnswer.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				handleNewAnswer();
			}
		});
        
        //For shake event
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(new OnShakeListener() {
			
			@Override
			public void onShake() 
			{
				handleNewAnswer();
			}
		});
    }
    
    //Method to animate crystal ball
    private void animateBall()
    {
    	crystalBallImage.setImageResource(R.drawable.ball_animation);
    	AnimationDrawable ballAnimation = (AnimationDrawable)crystalBallImage.getDrawable();
    	if(ballAnimation.isRunning())
    		ballAnimation.stop();
    	ballAnimation.start();
    }
    
    //Method to animate crystal ball answer
    private void animateAnswer()
    {
    	AlphaAnimation fadeInAnimation = new AlphaAnimation(0,1);
    	fadeInAnimation.setDuration(1500);
    	fadeInAnimation.setFillAfter(true);
    	answerView.setAnimation(fadeInAnimation);
    }
    
    //Method to play crystal ball sound
    private void playSound()
    {
    	 MediaPlayer player = MediaPlayer.create(this, R.raw.crystal_ball);
    	 player.start();
    	 player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) 
			{
				mp.release();
			}
		});
    }
    
    //Method to handle new answer request event
    private void handleNewAnswer() {
		String answer = ball.getAnAnswer();
		answerView.setText(answer);
		animateBall();
		animateAnswer();
		playSound();
	}

    @Override
    public void onResume()
    {
    	super.onResume();
    	sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	sensorManager.unregisterListener(shakeDetector);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	/**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
