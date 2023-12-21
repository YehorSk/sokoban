package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Level extends Group {
    private static int levels_amount = 7;
    private int current_level = 1; // Type here whatever level you want, 1- is menu, 7 - is final , 2-6 main levels
    private FileReader fr;
    private BufferedReader br;
    private char[][] lvl;
    private int x, y;
    private int pos_x, pos_y;
    private BorderPane root;
    private String rows = "";
    private char[][] lvl_copy;
    private Scene scene;
    private Stage primaryStage;
    private String row;

    public Level(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setLevel();
    }

    public void setLevel() {
        root = new BorderPane();
        
        x = 0;
        y = 0;
        readFile();
        lvl = new char[x][y];
        lvl_copy = new char[x][y];
        scene = new Scene(root, x * 50, y * 50);
        scene.setOnKeyPressed((evt)->{
        	if(evt.getCode().toString().equals("R")) {
        		restart();
        	}
        	if(evt.getCode().toString().equals("Q")) {
        		nextLevel();
        	}
        });
        root.setStyle("-fx-background-color: black;");
        primaryStage.setScene(scene);
        if(current_level == 1 || current_level == levels_amount) { // Setting fixed sizes for a menu and a final level
        	primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(400);
            root.setStyle("-fx-background-color: white;");
        }else {
        	primaryStage.setMinWidth(x * 50+10);
            primaryStage.setMinHeight(y * 50+30);
            primaryStage.setMaxWidth(x * 50+10);
            primaryStage.setMaxHeight(y * 50+30);
        }
        fill();
        printLvl(lvl);
        this.requestFocus();
    }

    public void readFile() { 
    	rows = "";
        try {
            fr = new FileReader(new File("levels/lvl_" + current_level + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        br = new BufferedReader(fr);
        row = "";
        try {
            while ((row = br.readLine()) != null) {
                x = Math.max(x, row.length());
                y++;
                rows += row + " ";
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void fill() { // Filling arrays lvl and lvl_copy
        String[] new_r = rows.split(" ");
        for (int i = 0; i < new_r.length; i++) {
            for (int j = 0; j < new_r[i].length(); j++) {
                lvl[j][i] = new_r[i].charAt(j);
                lvl_copy[j][i] = new_r[i].charAt(j);
            }
        }
    }

    public void update(char[][] lvl) { // Updating level, when something is moved
        root.getChildren().clear();
        printLvl(lvl);
    }

    public void nextLevel() { 
        if (current_level + 1 <= levels_amount) {
            current_level++;
        }
        setLevel();
    }
    public void restart() {
    	setLevel();
    }

    public void printLvl(char[][] lvl) { 
        root.getChildren().clear();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                switch (lvl[i][j]) {
                    case '1':
                        root.getChildren().add(new Wall(i, j));
                        break;
                    case '2':
                        if(lvl_copy[i][j]=='4' || lvl_copy[i][j]=='5') {
                        	root.getChildren().add(new Box_a(i, j));
                        }else {
                        	root.getChildren().add(new Box_d(i, j));
                        }
                        break;
                    case '3':
                        pos_x = i;
                        pos_y = j;
                        Player player = new Player(i, j, lvl, lvl_copy, x, y, pos_x, pos_y, this);
                        root.getChildren().add(player);
                        player.requestFocus();
                        break;
                    case '4':
                        root.getChildren().add(new Dot(i, j));
                        break;
                    case '5':
                        root.getChildren().add(new Box_a(i, j));
                        break;
                    case '6':
                    	VBox vbox = new VBox();
                    	Text instructionsText = new Text("Game Instructions:\n" +
                                "If you want to restart the game, press 'R'.\n" +
                                "To control the player, use arrow keys (left, right, up, down).\n"
                                + "Jump to the next level by pressing 'Q'");
                    	instructionsText.setFont(Font.font("Cambria", 15));
                    	Button playButton = new Button("Play");
                    	playButton.setOnMouseClicked((evt)->nextLevel());
                    	vbox.setAlignment(Pos.CENTER);
                    	vbox.getChildren().addAll(instructionsText,playButton);
                    	root.setTop(vbox);
                    	
                    	break;
                    case '7':
                    	VBox vbox2 = new VBox();
                    	Text textFinal = new Text("You did it!!! \n"
                    			+ "If you want to play again press the button below.");
                    	textFinal.setFont(Font.font("Cambria", 15));
                    	Button replay = new Button("Restart");
                    	replay.setOnMouseClicked((evt)->{
                    		current_level = 1;
                    		restart();
                    	});
                    	vbox2.setAlignment(Pos.CENTER);
                    	vbox2.getChildren().addAll(textFinal,replay);
                    	root.setTop(vbox2);
                        break;
                }
            }
        }
    }
}
