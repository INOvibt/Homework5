import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonPlaceholderApiExample {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        JsonPlaceholderApiExample apiClient = new JsonPlaceholderApiExample();

        // Завдання 1
        apiClient.createUser();
        apiClient.updateUser(1); // Припустимо, що користувач з id=1 існує
        apiClient.deleteUser(1);
        apiClient.getAllUsers();
        apiClient.getUserById(1);
        apiClient.getUserByUsername("Bret");

        // Завдання 2
        apiClient.getCommentsAndSaveToFile(1);

        // Завдання 3
        apiClient.getOpenTasks(1);
    }

    private void createUser() {
        try {
            URL url = new URL(BASE_URL + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Замініть цей рядок на власний JSON для створення нового користувача
            String requestBody = "{\"name\":\"John Doe\",\"username\":\"john_doe\",\"email\":\"john@example.com\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Created user: " + response.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUser(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Замініть цей рядок на власний JSON для оновлення користувача
            String requestBody = "{\"id\":1,\"name\":\"Updated Name\",\"username\":\"updated_username\",\"email\":\"updated_email@example.com\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Updated user: " + response.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("User deleted successfully");
            } else {
                System.out.println("Failed to delete user. Response code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAllUsers() {
        try {
            URL url = new URL(BASE_URL + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("All users: " + response.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUserById(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("User by ID: " + response.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUserByUsername(String username) {
        try {
            URL url = new URL(BASE_URL + "/users?username=" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("User by username: " + response.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для завдання 2
    private void getCommentsAndSaveToFile(int userId) {
        try {
            // Отримання останнього поста користувача
            URL postsUrl = new URL(BASE_URL + "/users/" + userId + "/posts");
            HttpURLConnection postsConnection = (HttpURLConnection) postsUrl.openConnection();
            postsConnection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(postsConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Парсинг JSON для отримання ідентифікатора останнього поста
                // (знайдіть власний спосіб робити це або використовуйте бібліотеку JSON парсингу)
                int lastPostId = parseLastPostId(response.toString());

                // Отримання коментарів до останнього поста
                URL commentsUrl = new URL(BASE_URL + "/posts/" + lastPostId + "/comments");
                HttpURLConnection commentsConnection = (HttpURLConnection) commentsUrl.openConnection();
                commentsConnection.setRequestMethod("GET");

                try (BufferedReader commentsBr = new BufferedReader(new InputStreamReader(commentsConnection.getInputStream(), "utf-8"))) {
                    StringBuilder commentsResponse = new StringBuilder();
                    String commentsResponseLine;
                    while ((commentsResponseLine = commentsBr.readLine()) != null) {
                        commentsResponse.append(commentsResponseLine.trim());
                    }

                    // Збереження коментарів у файл
                    String fileName = "user-" + userId + "-post-" + lastPostId + "-comments.json";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                        writer.write(commentsResponse.toString());
                        System.out.println("Comments saved to file: " + fileName);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для завдання 3
    private void getOpenTasks(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId + "/todos");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Open tasks for user: " + response.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int parseLastPostId(String json) {
        // Власний код для парсингу JSON та отримання ідентифікатора останнього поста
        // В цьому прикладі ми просто припускаємо, що у JSON є поле "id", яке вказує на ідентифікатор поста
        // Реалізуйте це відповідно до структури ваших даних
        return 1;
    }
}
