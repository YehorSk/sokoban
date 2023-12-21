package application;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Box_a extends ImageView{
	private Image img;
	
	public Box_a(float x,float y) {
		super();
		img = new Image(new File("resources/box_a.png").toURI().toString(), 50, 50, false, false);
		setImage(img);
		this.setLayoutX(x*50);
		this.setLayoutY(y*50);
	}
}
