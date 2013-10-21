package com.swbgames.mesh;


import java.io.Serializable;
/**
 * Klasa u¿ywana do wczytywania zserializowanych obiektów.
 * @author Radek
 *
 */
public class MeshData implements Serializable{
	
	public MeshData(float[] p, float[] n, float[] u) {
		 positions=p;
		 normals=n;
		 uvs=u;
	 }
	public float[] positions;
	public float[] normals;
	public float[] uvs;
}
