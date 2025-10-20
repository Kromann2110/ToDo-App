import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TodoApp extends Application {
    // ListView variables for each stage of todo items
    private ListView<String> todoList;
    private ListView<String> inProgressList;
    private ListView<String> doneList;

    // ObservableLists hold the actual data displayed in the ListViews
    private ObservableList<String> todos = FXCollections.observableArrayList();
    private ObservableList<String> inProgress = FXCollections.observableArrayList();
    private ObservableList<String> done = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args); // Launches the JavaFX application
    }
    // The main entry point for JavaFX applications
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple Todo App");

        // Create ListViews
        todoList = new ListView<>(todos);
        inProgressList = new ListView<>(inProgress);
        doneList = new ListView<>(done);

        todoList.setEditable(true);         // ListView for "Todo" items
        inProgressList.setEditable(true);   // ListView for "In Progress" items
        doneList.setEditable(true);         // ListView for "Done" items

        // Make ListViews editable so the user can change the text directly
        todoList.setCellFactory(TextFieldListCell.forListView());
        inProgressList.setCellFactory(TextFieldListCell.forListView());
        doneList.setCellFactory(TextFieldListCell.forListView());

        // Buttons
        Button addTodoBtn = new Button("Add Todo");
        addTodoBtn.setOnAction(e -> addTodo());

        Button moveRightBtn = new Button("→");
        Button moveLeftBtn = new Button("←");

        moveRightBtn.setOnAction(e -> moveRight());
        moveLeftBtn.setOnAction(e -> moveLeft());

        // Layout using VBox (vertical box) and HBox (Horizontal box)
        VBox todoBox = new VBox(10, new Label("Todos"), todoList);
        VBox progressBox = new VBox(10, new Label("In Progress"), inProgressList);
        VBox doneBox = new VBox(10, new Label("Done"), doneList);

        // Center the items in each VBox
        todoBox.setAlignment(Pos.CENTER);
        progressBox.setAlignment(Pos.CENTER);
        doneBox.setAlignment(Pos.CENTER);

        // Place all three VBox sections in a horizontal row (HBox)
        HBox lists = new HBox(10, todoBox, progressBox, doneBox);
        lists.setAlignment(Pos.CENTER);
        lists.setPadding(new Insets(10));

        // Buttons at the bottom of the window
        HBox buttons = new HBox(10, addTodoBtn, moveLeftBtn, moveRightBtn);
        buttons.setAlignment(Pos.CENTER);

        // Root layout containing everything
        VBox root = new VBox(10, lists, buttons);
        root.setPadding(new Insets(15));

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show(); // Display the window
    }

    // Add a new Todo item
    private void addTodo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Todo");
        dialog.setHeaderText("Add a new todo");
        dialog.setContentText("Todo text:");

        // If user enters text and clicks OK
        dialog.showAndWait().ifPresent(todo -> {
            if (!todo.trim().isEmpty()) {
                todos.add(todo.trim());
            }
        });
    }

    // Move the selected item forward
    private void moveRight() {
        if (todoList.getSelectionModel().getSelectedItem() != null) {

            // Move item from Todos → In Progress
            String item = todoList.getSelectionModel().getSelectedItem();
            todos.remove(item);
            inProgress.add(item);
        } else if (inProgressList.getSelectionModel().getSelectedItem() != null) {
            // Move item from In Progress → Done
            String item = inProgressList.getSelectionModel().getSelectedItem();
            inProgress.remove(item);
            done.add(item);
        }
    }

    // Move the selected item backward
    private void moveLeft() {
        if (doneList.getSelectionModel().getSelectedItem() != null) {

            // Move item from Done → In Progress
            String item = doneList.getSelectionModel().getSelectedItem();
            done.remove(item);
            inProgress.add(item);
        } else if (inProgressList.getSelectionModel().getSelectedItem() != null) {
            // Move item from In Progress → Todos
            String item = inProgressList.getSelectionModel().getSelectedItem();
            inProgress.remove(item);
            todos.add(item);
        }
    }
}
