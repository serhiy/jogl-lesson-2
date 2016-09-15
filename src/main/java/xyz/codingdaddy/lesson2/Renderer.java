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
	int positionAttributeLocation;
	int programId;
	
	public void init(GLAutoDrawable glAutoDrawable) {
		GL2 gl2 = glAutoDrawable.getGL().getGL2();
		
		try {
			
			String vertexShaderCode = ShaderProgram.loadResource("shaders/default.vs");
			String fragmentShaderCode = ShaderProgram.loadResource("shaders/default.fs");
			
			programId = gl2.glCreateProgram();
			int vertexShaderId = ShaderProgram.createShader(gl2, programId, vertexShaderCode, GL2.GL_VERTEX_SHADER);
			int fragmentShaderId = ShaderProgram.createShader(gl2, programId, fragmentShaderCode, GL2.GL_FRAGMENT_SHADER);
			
			ShaderProgram.link(gl2, programId);
			
			vertexBuffer = Buffers.newDirectFloatBuffer(cube.getVertices().length);
			indexBuffer = Buffers.newDirectIntBuffer(cube.getIndices().length);
			
			vertexBuffer.put(cube.getVertices());
			indexBuffer.put(cube.getIndices());
			
			positionAttributeLocation = gl2.glGetAttribLocation(programId, "inPosition");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dispose(GLAutoDrawable glAutoDrawable) {
		/* nothing to dispose yet */
	}

	public void display(GLAutoDrawable glAutoDrawable) {
		GL2 gl2 = glAutoDrawable.getGL().getGL2();
		
		gl2.glUseProgram(programId);
		
		gl2.glEnableVertexAttribArray(positionAttributeLocation);
		
		gl2.glVertexAttribPointer(positionAttributeLocation, 3,
				GL2.GL_FLOAT, false, 0, vertexBuffer.rewind());

		gl2.glDrawElements(GL2.GL_TRIANGLES, cube.getIndices().length,
				GL2.GL_UNSIGNED_INT, indexBuffer.rewind());

		gl2.glDisableVertexAttribArray(positionAttributeLocation);
		
		gl2.glUseProgram(0);
	}

	public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width,
			int height) {
		/* no action to be taken on reshape */
	}

}
