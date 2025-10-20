import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TodoApp extends Application {
    private ListView<String> todoList;
    private ListView<String> inProgressList;
    private ListView<String> doneList;

    private ObservableList<String> todos = FXCollections.observableArrayList();
    private ObservableList<String> inProgress = FXCollections.observableArrayList();
    private ObservableList<String> done = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("My ToDo application");

        // Initialize with sample data
        todos.addAll("Todo 10", "Todo 11", "Todo 12", "New Todo Item");
        inProgress.addAll("Todo 6", "Todo 8", "Todo 9");
        done.addAll("Todo 1", "Todo 4", "Todo 3", "Todo 5");

        // Create ListViews
        todoList = new ListView<>(todos);
        inProgressList = new ListView<>(inProgress);
        doneList = new ListView<>(done);

        todoList.setPrefSize(200, 300);
        inProgressList.setPrefSize(200, 300);
        doneList.setPrefSize(200, 300);

        // Create column headers
        Label todoHeader = new Label("ToDos");
        Label progressHeader = new Label("In progress");
        Label doneHeader = new Label("Done");

        todoHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        progressHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        doneHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create buttons between ToDos and In Progress
        Button moveToProgressBtn = new Button(">");
        Button moveBackToTodoBtn = new Button("<");
        moveToProgressBtn.setPrefSize(40, 30);
        moveBackToTodoBtn.setPrefSize(40, 30);

        VBox todoToProgressBtns = new VBox(5, moveToProgressBtn, moveBackToTodoBtn);
        todoToProgressBtns.setAlignment(Pos.CENTER);

        // Create buttons between In Progress and Done
        Button moveToDoneBtn = new Button(">");
        Button moveBackToProgressBtn = new Button("<");
        moveToDoneBtn.setPrefSize(40, 30);
        moveBackToProgressBtn.setPrefSize(40, 30);

        VBox progressToDoneBtns = new VBox(5, moveToDoneBtn, moveBackToProgressBtn);
        progressToDoneBtns.setAlignment(Pos.CENTER);

        // Create column containers
        VBox todoColumn = new VBox(10, todoHeader, todoList);
        VBox progressColumn = new VBox(10, progressHeader, inProgressList);
        VBox doneColumn = new VBox(10, doneHeader, doneList);

        todoColumn.setAlignment(Pos.TOP_CENTER);
        progressColumn.setAlignment(Pos.TOP_CENTER);
        doneColumn.setAlignment(Pos.TOP_CENTER);

        // Create main content area with columns and buttons
        HBox mainContent = new HBox(15);
        mainContent.getChildren().addAll(
                todoColumn,
                todoToProgressBtns,
                progressColumn,
                progressToDoneBtns,
                doneColumn
        );
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(20, 20, 10, 20));

        // Create buttons at bottom
        Button createNewBtn = new Button("Create new ToDo");
        Button deleteBtn = new Button("Delete");
        createNewBtn.setPrefSize(150, 30);
        deleteBtn.setPrefSize(100, 30);
        HBox bottomBox = new HBox(10, createNewBtn, deleteBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));

        // Main layout
        BorderPane root = new BorderPane();
        root.setCenter(mainContent);
        root.setBottom(bottomBox);
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Button actions
        moveToProgressBtn.setOnAction(e -> moveItem(todoList, todos, inProgress));
        moveBackToTodoBtn.setOnAction(e -> moveItem(inProgressList, inProgress, todos));
        moveToDoneBtn.setOnAction(e -> moveItem(inProgressList, inProgress, done));
        moveBackToProgressBtn.setOnAction(e -> moveItem(doneList, done, inProgress));
        createNewBtn.setOnAction(e -> createNewTodo());
        deleteBtn.setOnAction(e -> deleteSelectedItem());

        Scene scene = new Scene(root, 700, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void moveItem(ListView<String> fromList, ObservableList<String> fromData, ObservableList<String> toData) {
        String selected = fromList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            fromData.remove(selected);
            toData.add(selected);
        }
    }

    private void createNewTodo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Todo");
        dialog.setHeaderText("Create a new todo");
        dialog.setContentText("Todo text:");

        dialog.showAndWait().ifPresent(todo -> {
            if (!todo.trim().isEmpty()) {
                todos.add(todo.trim());
            }
        });
    }

    private void deleteSelectedItem() {
        // Check which list has a selected item and delete it
        if (todoList.getSelectionModel().getSelectedItem() != null) {
            String selected = todoList.getSelectionModel().getSelectedItem();
            todos.remove(selected);
        } else if (inProgressList.getSelectionModel().getSelectedItem() != null) {
            String selected = inProgressList.getSelectionModel().getSelectedItem();
            inProgress.remove(selected);
        } else if (doneList.getSelectionModel().getSelectedItem() != null) {
            String selected = doneList.getSelectionModel().getSelectedItem();
            done.remove(selected);
        }
    }
}