package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 20;
	private float yaw = 0;
	private float roll;
	
	private Player player;
	
	public Camera(Player player){
		this.player = player;
	}
	
	public void move(){
		calculateZoom();
		calculatePitch();
		calculateAngelAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance   = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance , verticalDistance);
		this.yaw = 180 - (player.getRotY() +  this.angleAroundPlayer);
//		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
//			position.z+=0.5f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
//			position.z-=0.5f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
//			position.x+=0.5f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
//			position.x-=0.5f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
//			position.y+=0.5f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
//			position.y-=0.5f;
//		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		
		this.position.x = player.getPosition().x - offsetX;
		this.position.z = player.getPosition().z - offsetZ;
		this.position.y = player.getPosition().y + verticalDistance;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(this.pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(this.pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		this.distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			this.pitch -= pitchChange;
		}
	}
	
	private void calculateAngelAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			this.angleAroundPlayer -= angleChange;
		}
	}

}
