package com.haojie;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class AREActivity extends Activity {
	   private GLSurfaceView mGLView;
	   
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        // Create a GLSurfaceView instance and set it
	        // as the ContentView for this Activity
	        mGLView = new HelloOpenGLES20SurfaceView(this);
	        setContentView(mGLView);
	    }
	    
	    @Override
	    protected void onPause() {
	        super.onPause();
	        // The following call pauses the rendering thread.
	        // If your OpenGL application is memory intensive,
	        // you should consider de-allocating objects that
	        // consume significant memory here.
	        mGLView.onPause();
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
	        // The following call resumes a paused rendering thread.
	        // If you de-allocated graphic objects for onPause()
	        // this is a good place to re-allocate them.
	        mGLView.onResume();
	    }
	}
	  
	class HelloOpenGLES20SurfaceView extends GLSurfaceView {

	    public HelloOpenGLES20SurfaceView(Context context){
	        super(context);
	        // Create an OpenGL ES 2.0 context.
	        setEGLContextClientVersion(2);
	            
	        // set the mRenderer member
	        mRenderer = new ARERenderer();
	        setRenderer(mRenderer);
	        
	        // Render the view only when there is a change
	        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	    }
	    
	    
	    @Override 
	    public boolean onTouchEvent(MotionEvent e) {
	        // MotionEvent reports input details from the touch screen
	        // and other input controls. In this case, you are only
	        // interested in events where the touch position changed.

	        float x = e.getX();
	        float y = e.getY();
	        
	        switch (e.getAction()) {
	            case MotionEvent.ACTION_MOVE:
	                break;
	                
	            case MotionEvent.ACTION_UP:
	            	float pX = (float)x / getHeight();
	            	float pY = (float)y / getHeight();
	            	mRenderer.AddPoint(pX, pY, 0);
	            	requestRender();
	            	break;
	        }

	        mPreviousX = x;
	        mPreviousY = y;
	        return true;
	    }     
	    
	    private ARERenderer mRenderer;
	    private float mPreviousX;
	    private float mPreviousY;    
}