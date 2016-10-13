package xyz.codingdaddy.lesson2;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * Enables triangle rendering.
 * 
 * @author serhiy
 */
public class Renderer implements GLEventListener {

	Cube cube = new Cube();
	FloatBuffer vertexBuffer;
	IntBuffer indexBuffer;
	FloatBuffer colorBuffer;
	int positionAttributeLocation;
	int colorAttributeLocation;
	int programId;
	int vertexShaderId;
	int fragmentShaderId;
	
	public void init(GLAutoDrawable glAutoDrawable) {
		GL2 gl2 = glAutoDrawable.getGL().getGL2();
		
		try {
			
			String vertexShaderCode = ShaderProgram.loadResource("shaders/default.vs");
			String fragmentShaderCode = ShaderProgram.loadResource("shaders/default.fs");
			
			programId = gl2.glCreateProgram();
			vertexShaderId = ShaderProgram.createShader(gl2, programId, vertexShaderCode, GL2.GL_VERTEX_SHADER);
			fragmentShaderId = ShaderProgram.createShader(gl2, programId, fragmentShaderCode, GL2.GL_FRAGMENT_SHADER);
			
			ShaderProgram.link(gl2, programId);
			
			vertexBuffer = Buffers.newDirectFloatBuffer(cube.getVertices().length);
			indexBuffer = Buffers.newDirectIntBuffer(cube.getIndices().length);
			colorBuffer = Buffers.newDirectFloatBuffer(cube.getColors().length);
			
			vertexBuffer.put(cube.getVertices());
			indexBuffer.put(cube.getIndices());
			colorBuffer.put(cube.getColors());
			
			positionAttributeLocation = gl2.glGetAttribLocation(programId, "inPosition");
			colorAttributeLocation = gl2.glGetAttribLocation(programId, "inColor");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gl2.glEnable(GL2.GL_DEPTH_TEST);
		gl2.glDepthMask(true);
	}

	public void dispose(GLAutoDrawable glAutoDrawable) {
		GL2 gl2 = glAutoDrawable.getGL().getGL2();
		
		gl2.glDetachShader(programId, vertexShaderId);
		gl2.glDetachShader(programId, fragmentShaderId);
		gl2.glDeleteProgram(programId);
	}

	public void display(GLAutoDrawable glAutoDrawable) {
		GL2 gl2 = glAutoDrawable.getGL().getGL2();
		
		gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		gl2.glUseProgram(programId);
		
		gl2.glEnableVertexAttribArray(positionAttributeLocation);
		gl2.glEnableVertexAttribArray(colorAttributeLocation);
		
		gl2.glVertexAttribPointer(positionAttributeLocation, 3,
				GL2.GL_FLOAT, false, 0, vertexBuffer.rewind());
		
		gl2.glVertexAttribPointer(colorAttributeLocation, 3,
				GL2.GL_FLOAT, false, 0, colorBuffer.rewind());

		gl2.glDrawElements(GL2.GL_TRIANGLES, cube.getIndices().length,
				GL2.GL_UNSIGNED_INT, indexBuffer.rewind());

		gl2.glDisableVertexAttribArray(positionAttributeLocation);
		gl2.glDisableVertexAttribArray(colorAttributeLocation);
		
		gl2.glUseProgram(0);
	}

	public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width,
			int height) {
		/* no action to be taken on reshape */
	}

}
