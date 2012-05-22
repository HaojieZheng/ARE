package com.haojie;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.R.array;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

public class ARERenderer implements Renderer {

	 public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		    
		 	mPointList = new ArrayList<Float>();
	        // Set the background frame color
	        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	        
	        // initialize the triangle vertex array
	        initShapes();
	        
	        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
	        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
	        
	        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
	        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
	        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
	        GLES20.glLinkProgram(mProgram);                  // creates OpenGL program executables
	        
	        // get handle to the vertex shader's vPosition member
	        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");	        
	    }
	    
	    public void onDrawFrame(GL10 unused) {
	    
	    	initShapes();
	        // Redraw background color
	        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	        
	        // Add program to OpenGL environment
	        GLES20.glUseProgram(mProgram);
	        
	        // Prepare the triangle data
	        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 12, mVertexBuffer);
	        GLES20.glEnableVertexAttribArray(maPositionHandle);
            
	        // Create a rotation for the triangle
	        Matrix.setIdentityM(mMMatrix, 0);
	        
	        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
	        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
	        
	        // Apply a ModelView Projection transformation
	        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
	        
	        GLES20.glUniform1f(muRedHandle, (float) 0.0);
	        GLES20.glUniform1f(muGreenHandle, (float) 1.0);
	        GLES20.glUniform1f(muBlueHandle, (float) 1.0);
	        GLES20.glUniform1f(muAlphaHandle, (float) 0.2);
	        
	        // Draw the triangle
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VertexCount);	        
	    }
	    
	    public void onSurfaceChanged(GL10 unused, int width, int height) {
	    	 GLES20.glViewport(0, 0, width, height);
	         
	         float ratio = (float) width / height;
	         
	         // this projection matrix is applied to object coordinates
	         // in the onDrawFrame() method
	         Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	         
	         muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	         muRedHandle = GLES20.glGetUniformLocation(mProgram, "uRed");
	         muGreenHandle = GLES20.glGetUniformLocation(mProgram, "uGreen");
	         muBlueHandle = GLES20.glGetUniformLocation(mProgram, "uBlue");
	         muAlphaHandle = GLES20.glGetUniformLocation(mProgram, "uAlpha");
	         
	         
	         Matrix.setLookAtM(mVMatrix, 0, 0, 0, -4, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	    }
	    
	    
	    
	    private void initShapes(){
	        
	        
	        VertexCount = mPointList.size() / 3;
	        
	        // initialize vertex Buffer for triangle  
	        ByteBuffer vbb = ByteBuffer.allocateDirect(
	                // (# of coordinate values * 4 bytes per float)
	        		mPointList.size() * 4);
	        
	        
	        float[] floatArray = new float[mPointList.size()];

	        for (int i = 0; i < mPointList.size(); i++) {
	            Float f = mPointList.get(i);
	            floatArray[i] = (f != null ? f : 0.0f); // Or whatever default you want.
	        }
	        
	        vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
	        mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
	        mVertexBuffer.put(floatArray);    // add the coordinates to the FloatBuffer
	        mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	    
	    }
	    
	    public void AddPoint(float x, float y, float z)
	    {
	    	float width = 0.01f;
	    	
	    	mPointList.add(new Float(x));
	    	mPointList.add(new Float(y));
	    	mPointList.add(new Float(z));
	    	
	    	mPointList.add(new Float(x + width));
	    	mPointList.add(new Float(y));
	    	mPointList.add(new Float(z));
	    	
	    	mPointList.add(new Float(x + width));
	    	mPointList.add(new Float(y + width));
	    	mPointList.add(new Float(z));	    	
	    	
	    	mPointList.add(new Float(x + width));
	    	mPointList.add(new Float(y + width));
	    	mPointList.add(new Float(z));
	    	
	    	mPointList.add(new Float(x));
	    	mPointList.add(new Float(y + width));
	    	mPointList.add(new Float(z));	 	    	
	    	
	    	mPointList.add(new Float(x));
	    	mPointList.add(new Float(y));
	    	mPointList.add(new Float(z));	 	    	
	    	
	    }
	    
    
	    private int VertexCount;
	    private FloatBuffer mVertexBuffer;
	    private int mProgram;
	    private int maPositionHandle;	
	    
	    private int muMVPMatrixHandle, muRedHandle, muGreenHandle, muBlueHandle, muAlphaHandle;
	    private float[] mMVPMatrix = new float[16];
	    private float[] mMMatrix = new float[16];
	    private float[] mVMatrix = new float[16];
	    private float[] mProjMatrix = new float[16];	    
	    
	    private ArrayList<Float> mPointList;
	    
	    private int loadShader(int type, String shaderCode){
	        
	        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	        int shader = GLES20.glCreateShader(type); 
	        
	        // add the source code to the shader and compile it
	        GLES20.glShaderSource(shader, shaderCode);
	        GLES20.glCompileShader(shader);
	        
	        return shader;
	    }	    
	    
	    private final String vertexShaderCode = 
	            // This matrix member variable provides a hook to manipulate
	            // the coordinates of the objects that use this vertex shader
	            "uniform mat4 uMVPMatrix;   \n" +
	            
	            "attribute vec4 vPosition;  \n" +
	            "void main(){               \n" +
	            
	            // the matrix must be included as a modifier of gl_Position
	            " gl_Position = uMVPMatrix * vPosition; \n" +
	            
	            "}  \n";
	        
	        private final String fragmentShaderCode = 
	            "precision mediump float;  \n" +
	            "uniform float uRed, uGreen, uBlue, uAlpha; \n" +
	            "void main(){              \n" +
	            " gl_FragColor = vec4 (uRed, uGreen, uBlue, uAlpha); \n" +
	            "}                         \n";	    

}
