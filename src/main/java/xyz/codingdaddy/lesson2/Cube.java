package xyz.codingdaddy.lesson2;

public class Cube {
	private final float [] vertices = { 0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
			  1.0f, 1.0f, 0.0f,
			  0.0f, 1.0f, 0.0f};
	
	private final int [] indices = {0, 1, 2,
								2, 0, 3};

	public float [] getVertices() {
		return vertices;
	}

	public int [] getIndices() {
		return indices;
	}
}
