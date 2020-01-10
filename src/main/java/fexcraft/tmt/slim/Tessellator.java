package fexcraft.tmt.slim;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ebf.tim.utility.DebugUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
* @Author EternalBlueFlame
* 
*/
@SideOnly(Side.CLIENT)
public class Tessellator{

	public static Tessellator INSTANCE = new Tessellator();

	private static Float x, y, z,nX, nY, nZ;
	private static boolean normalMapped=false;
	private static List<float[]> verticies = new ArrayList<>(); //0,1,2 are the position, 3,4,5,6 are the texture vectors.

	public static Tessellator getInstance(){
		return INSTANCE;
	}

	//use this to reset and define the drawing mode
	public void startDrawing(int mode){
		verticies=new ArrayList<>();
		normalMapped=false;
		nX=0f;nY=0f;nZ=0f;
		GL11.glBegin(mode);
	}

	public void draw(){
		if(normalMapped) {
			GL11.glNormal3f(nX, nY, nZ);
		}
		for(float[] f : verticies){
			if(f.length>3) {
				GL11.glTexCoord2f(f[3], f[4]);
			}
			GL11.glVertex3f(f[0],f[1],f[2]);
		}
		GL11.glEnd();
	}
	
	public void addVertex(float i, float j, float k){
		if(x!=null){
			verticies.add(new float[]{i + x, j + y, k + z});

		} else {
			verticies.add(new float[]{i, j, k});
		}
	}
	
	public void addVertexWithUV(float i, float j, float k, float u, float v){
		if(x!=null){
			verticies.add(new float[]{i + x, j + y, k + z, u, v});
		} else {
			verticies.add(new float[]{i, j, k, u, v});
		}
	}

	public void addVertexWithUVW(float i, float j, float k, float l, float m, float n){
		this.setTextureUVW(l, m, n);
		this.addVertex(i, j, k);
	}

	public void setTextureUV(float u, float v){
		float[] vert = verticies.get(verticies.size()-1);
		verticies.set(verticies.size()-1, new float[]{vert[0],vert[1],vert[2],u,v});
	}
	
	public void setTextureUVW(float u, float v, float w){
		float[] vert = verticies.get(verticies.size()-1);
		verticies.set(verticies.size()-1, new float[]{vert[0],vert[1],vert[2],u,v,0.0f,w});
	}

	public static void setTranslation(float xOffset, float yOffset, float zOffset){
		x = xOffset; y = yOffset; z = zOffset;
	}
	
	public static void addTranslation(float xOffset, float yOffset, float zOffset){
		x += xOffset; y += yOffset; z += zOffset;
	}

	public static void bindTexture(ResourceLocation uri){
		TextureManager.bindTexture(uri);
	}

	public static void setNormal(List<PositionTransformVertex> vertex){
		normalMapped=true;

		Vec3f p0,p1;
		for(int i=0, j=1; i<vertex.size(); i++,j++) {

			if (j == vertex.size()){j=1;}
			p0 = vertex.get(i).vector3F;// current vertex
			p1 = vertex.get(j).vector3F;// next vertex

			nX += (p0.yCoord - p1.yCoord) * (p0.zCoord + p1.zCoord);
			nY += (p0.zCoord - p1.zCoord) * (p0.xCoord + p1.xCoord);
			nZ += (p0.xCoord - p1.xCoord) * (p0.yCoord + p1.yCoord);
		}
	}



}
