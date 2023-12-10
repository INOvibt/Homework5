import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonPlaceholderApiExample {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        JsonPlaceholderApiExample apiClient = new JsonPlaceholderApiExample();

       
        apiClient.createUser();
        apiClient.updateUser(1);
        apiClient.deleteUser(1);
        apiClient.getAllUsers();
        apiClient.getUserById(1);
        apiClient.getUserByUsername("Bret");

       
        apiClient.getCommentsAndSaveToFile(1);

     
        apiClient.getOpenTasks(1);
    }

    private void createUser() {
        try {
            URL url = new URL(BASE_URL + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

          
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

    
    private void getCommentsAndSaveToFile(int userId) {
        try {
           
            URL postsUrl = new URL(BASE_URL + "/users/" + userId + "/posts");
            HttpURLConnection postsConnection = (HttpURLConnection) postsUrl.openConnection();
            postsConnection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(postsConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

               
                int lastPostId = parseLastPostId(response.toString());

                
                URL commentsUrl = new URL(BASE_URL + "/posts/" + lastPostId + "/comments");
                HttpURLConnection commentsConnection = (HttpURLConnection) commentsUrl.openConnection();
                commentsConnection.setRequestMethod("GET");

                try (BufferedReader commentsBr = new BufferedReader(new InputStreamReader(commentsConnection.getInputStream(), "utf-8"))) {
                    StringBuilder commentsResponse = new StringBuilder();
                    String commentsResponseLine;
                    while ((commentsResponseLine = commentsBr.readLine()) != null) {
                        commentsResponse.append(commentsResponseLine.trim());
                    }

                   
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
        return 1;
    }
}
