package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

// 1 = Wall
// 2 = Box
// 3 = Player
// 4 = Dot
// 5 = Box on a dot
// 6 = Restart button
// 7 = Play Again
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Level lvl = new Level(primaryStage);
			
			root.getChildren().add(lvl);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
