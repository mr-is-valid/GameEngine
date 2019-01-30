package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.TexturePath;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;

public class MainGameLoop {

	public static void main(String[] args) {

		TexturePath tp = new TexturePath();
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		//*********************************TERRAIN TEXTURE PACK ************************
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture(tp.getGrassy()));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture(tp.getDirt()));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture(tp.getGrassy2()));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture(tp.getFloor1()));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blenMap = new TerrainTexture(loader.loadTexture("blendMapCopy"));
		
		//********************************************************************************
		
		ModelData data = OBJFileLoader.loadOBJ("tree");
		
		RawModel treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(),
											  data.getNormals(), data.getIndices());
		
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
									new ModelTexture(loader.loadTexture(tp.getTreeTest())));
		
		TexturedModel lolipopTree = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader),
									new ModelTexture(loader.loadTexture(tp.getLowPolyTree())));
		
		TexturedModel pinkFlower = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
				  				   new ModelTexture(loader.loadTexture(tp.getFlower())));
					  
		TexturedModel pinkFlowerB = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
									new ModelTexture(loader.loadTexture(tp.getFlower1())));
					 
		TexturedModel weeds = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
				  			  new ModelTexture(loader.loadTexture(tp.getFern1())));
					 
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
							  new ModelTexture(loader.loadTexture(tp.getGrassTexture())));
					  
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern",loader),
				  			 new ModelTexture(loader.loadTexture(tp.getFern())));
		  
		  
	  pinkFlower.getTexture().setHasTransparency(true);
	  pinkFlower.getTexture().setUseFakeLighting(true);
	  
	  pinkFlowerB.getTexture().setHasTransparency(true);
	  pinkFlowerB.getTexture().setUseFakeLighting(true);
	 
	  weeds.getTexture().setHasTransparency(true);
	  weeds.getTexture().setUseFakeLighting(true);
	  
	  grass.getTexture().setHasTransparency(true);
	  grass.getTexture().setUseFakeLighting(true);
		  
	  fern.getTexture().setHasTransparency(true);
	  fern.getTexture().setUseFakeLighting(true);
		  
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(676452);
		for(int i=0;i<500;i++){
			
			if(i % 7 == 0) {
				entities.add(new Entity(tree, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
				entities.add(new Entity(lolipopTree, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -800),0,0,0,1));
			}
			
			if(i % 5 == 0) {
				entities.add(new Entity(weeds, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,2));
				entities.add(new Entity(fern, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,0.8f));
			}
			
			if(i % 3 == 0) {
				entities.add(new Entity(grass, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,1));
				entities.add(new Entity(pinkFlower, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,2));
				entities.add(new Entity(pinkFlowerB, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -800),0,0,0,2));
			}
		}
		
		Light light = new Light(new Vector3f(20000,40000,2000),new Vector3f(1,1,1));
		
		Terrain terrain  = new Terrain(0,-1,loader, texturePack , blenMap);
		Terrain terrain2 = new Terrain(-1,-1,loader,texturePack , blenMap);
		
		Camera camera = new Camera();	
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel bunnyModel = OBJLoader.loadObjModel("stanfordBunny", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
		
		Player player = new Player(stanfordBunny , new Vector3f(1,0,-50) , 0,0,0,1);
		
		while(!Display.isCloseRequested()){
			camera.move();
			player.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
