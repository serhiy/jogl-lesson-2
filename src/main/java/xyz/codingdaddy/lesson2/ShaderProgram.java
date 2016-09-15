package xyz.codingdaddy.lesson2;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;

import com.jogamp.opengl.GL2;

public class ShaderProgram {
	public static int createShader(GL2 gl2, int programId, String shaderCode, int shaderType) throws Exception {
		int shaderId = gl2.glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Shader id is zero.");
		}
		
		gl2.glShaderSource(shaderId, 1, new String[] { shaderCode }, null);
		gl2.glCompileShader(shaderId);
		
		IntBuffer intBuffer = IntBuffer.allocate(1);
		gl2.glGetShaderiv(shaderId, GL2.GL_COMPILE_STATUS, intBuffer);

		if (intBuffer.get(0) != 1) {
			gl2.glGetShaderiv(shaderId, GL2.GL_INFO_LOG_LENGTH, intBuffer);
			int size = intBuffer.get(0);
			if (size > 0) {
				ByteBuffer byteBuffer = ByteBuffer.allocate(size);
				gl2.glGetShaderInfoLog(shaderId, size, intBuffer, byteBuffer);
				System.out.println(new String(byteBuffer.array()));
			}
			throw new Exception("Error compiling shader!");
		}

		gl2.glAttachShader(programId, shaderId);

		return shaderId;
	}
	
	public static String loadResource(String fileName) throws Exception {
		try (InputStream in = ShaderProgram.class.getClassLoader().getResourceAsStream(fileName)) {
			return new Scanner(in, "UTF-8").useDelimiter("\\A").next();
		}
	}
	
	public static void link(GL2 gl2, int programId) throws Exception {
		gl2.glLinkProgram(programId);

		IntBuffer intBuffer = IntBuffer.allocate(1);
		gl2.glGetProgramiv(programId, GL2.GL_LINK_STATUS, intBuffer);

		if (intBuffer.get(0) != 1) {
			gl2.glGetProgramiv(programId, GL2.GL_INFO_LOG_LENGTH, intBuffer);
			int size = intBuffer.get(0);
			if (size > 0) {
				ByteBuffer byteBuffer = ByteBuffer.allocate(size);
				gl2.glGetProgramInfoLog(programId, size, intBuffer, byteBuffer);
				System.out.println(new String(byteBuffer.array()));
			}
			throw new Exception("Error linking shader program!");
		}

		gl2.glValidateProgram(programId);

		intBuffer = IntBuffer.allocate(1);
		gl2.glGetProgramiv(programId, GL2.GL_VALIDATE_STATUS, intBuffer);

		if (intBuffer.get(0) != 1) {
			gl2.glGetProgramiv(programId, GL2.GL_INFO_LOG_LENGTH, intBuffer);
			int size = intBuffer.get(0);
			if (size > 0) {
				ByteBuffer byteBuffer = ByteBuffer.allocate(size);
				gl2.glGetProgramInfoLog(programId, size, intBuffer, byteBuffer);
				System.out.println(new String(byteBuffer.array()));
			}
			throw new Exception("Error validating shader program!");
		}
	}
}
