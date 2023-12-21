package application;

import java.io.File;
import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
//1 = Wall
//2 = Box
//3 = Player
//4 = Dot
//5 = Box on a dot
public class Player extends ImageView{
	private Image[] sprites = {new Image(new File("resources/player/right_0.png").toURI().toString(), 50, 50, false, false),
							   new Image(new File("resources/player/left_0.png").toURI().toString(), 50, 50, false, false),
							   new Image(new File("resources/player/down_0.png").toURI().toString(), 50, 50, false, false),
							   new Image(new File("resources/player/up_0.png").toURI().toString(), 50, 50, false, false)};
	private static int cur_sprite = 0;
	private Level map;
	private char[][] lvl;
	private char[][] lvl_copy;
	private boolean moved = false;
	private int pos_x,pos_y;
	private int x_size,y_size;
	private int active_dots = 0;
	public Player(float x, float y,char[][] lvl,char[][] lvl_copy,int x_size, int y_size,int pos_x, int pos_y,Level map) {
		super();
		this.lvl = lvl;
		this.lvl_copy = lvl_copy;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.map = map;
		this.x_size = x_size;
		this.y_size = y_size;
		setImage(sprites[cur_sprite]);
		this.setLayoutX(x*50);
		this.setLayoutY(y*50);
		setOnKeyPressed((evt)->{
			changeSprite(evt.getCode().toString());
			move(evt.getCode().toString());
		});
		setFocusTraversable(true);
        setFocused(true);
        requestFocus();
	}
	
	public void changeSprite(String key) {
		switch (key) {
	        case "LEFT":
	        	cur_sprite = 1;
	            break;
	        case "RIGHT":
	        	cur_sprite = 0;
	            break;
	        case "UP":
	        	cur_sprite = 3;
	            break;
	        case "DOWN":
	        	cur_sprite = 2;
	            break;
		}
	}
	public void move(String key) {
	    int new_x = pos_x;
	    int new_y = pos_y;

	    switch (key) {
	        case "LEFT":
	            if (!checkWall(pos_x - 1, pos_y,key)) {
	                new_x = pos_x - 1;
	            }
	            break;
	        case "RIGHT":
	            if (!checkWall(pos_x + 1, pos_y,key)) {
	                new_x = pos_x + 1;
	            }
	            break;
	        case "UP":
	            if (!checkWall(pos_x, pos_y - 1,key)) {
	                new_y = pos_y - 1;
	            }
	            break;
	        case "DOWN":
	            if (!checkWall(pos_x, pos_y + 1,key)) {
	                new_y = pos_y + 1;
	            }
	            break;
	    }
	    if(!moved) {
	    	if(lvl_copy[pos_x][pos_y] == '4' || lvl_copy[pos_x][pos_y] == '5') {
	    		lvl[pos_x][pos_y] = '4';
	    	}else {
	    		lvl[pos_x][pos_y] = '0';
	    	}
			lvl[new_x][new_y] = '3';
			pos_x = new_x;
			pos_y = new_y;
			map.update(lvl);
	    }
	    moved = false;
	    check_dots();
	}
	public void check_dots() { // Counting amount of dots that are not activated by a box, and if that amount equals 0 go to next level
		for(int i = 0; i < x_size;i++) {
			for(int j = 0; j < y_size;j++) {
				if(lvl[i][j] == '4' || (lvl[i][j] == '3' && (lvl_copy[i][j] == '4' || lvl_copy[i][j] == '5'))) {
					active_dots++;
				}
			}
		}
		if(active_dots==0) {
			map.nextLevel();
		}
	}
	public boolean checkWall(int check_x, int check_y, String key) {
	    if (lvl[check_x][check_y] == '2' || lvl[check_x][check_y] == '5') {
	        int next_x = check_x;
	        int next_y = check_y;
	        switch (key) {
	            case "LEFT":
	                next_x = check_x - 1;
	                break;
	            case "RIGHT":
	                next_x = check_x + 1;
	                break;
	            case "UP":
	                next_y = check_y - 1;
	                break;
	            case "DOWN":
	                next_y = check_y + 1;
	                break;
	        }
	        if (lvl[next_x][next_y] == '0' || lvl[next_x][next_y] == '4') {
	            moveBox(check_x, check_y, key);
	            return true;
	        } else {
	            return true;
	        }
	    }
	    if (lvl[check_x][check_y] == '1') {
	        return true;
	    }
	    return false;
	}

	public void moveBox(int check_x, int check_y,String key) {
		int new_box_x = check_x;
	    int new_box_y = check_y;
	    boolean move_c = false;
		switch(key) {
		case "LEFT":
            if (!checkWall(check_x - 1, check_y,key)) {
            	new_box_x = check_x - 1;
            	move_c = true;
            }
            break;
        case "RIGHT":
            if (!checkWall(check_x + 1, check_y,key)) {
            	new_box_x = check_x + 1;
            	move_c = true;
            }
            break;
        case "UP":
            if (!checkWall(check_x, check_y - 1,key)) {
            	new_box_y = check_y - 1;
            	move_c = true;
            }
            break;
        case "DOWN":
            if (!checkWall(check_x, check_y + 1,key)) {
            	new_box_y = check_y + 1;
            	move_c = true;
            }
            break;
		}
		if(move_c) {
			if(lvl_copy[pos_x][pos_y] == '4' || lvl_copy[pos_x][pos_y] == '5') {
	    		lvl[pos_x][pos_y] = '4';
	    	}else {
	    		lvl[pos_x][pos_y] = '0';
	    	}
			pos_x = check_x;
			pos_y = check_y;
		    lvl[check_x][check_y] = '3';
			lvl[new_box_x][new_box_y] = '2';
			map.update(lvl);
			moved = true;
		}
	}
}
