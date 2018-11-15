import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// Server file extending thread to provide multithreading mechanism
class Server extends Thread {
    public static final int port = 4004;
    private Socket socket;

    // Server constructor
    private Server(Socket socket) {
        this.socket = socket;
        String clientAddress = socket.getInetAddress().getHostAddress();
        System.out.println("Client Connected, Address: " + clientAddress);
        start();
    }

    public static void main(String[] args) {
        System.out.println("Server started");
        System.out.println("Waiting for Client");
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            while (true) {
                new Server(server.accept());
            }
        } catch (IOException ex) {
            System.out.println("Server Error " + ex);
        } finally {
            try {
                if (server != null)
                    server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void run() {
        // printwriter and bufferedreader declaration
        PrintWriter output = null;
        BufferedReader input = null;
        try {
            // printwriter and bufferedreader instances
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // receive the choice from client
            String choice = input.readLine();
            String request;
            System.out.println("choice received: " + choice);
            // based on the choice respond the action
            switch (Integer.parseInt(choice)) {
                case 1:
                    request = "Login";
                    break;
                case 2:
                    request = "Register";
                    break;
                case 3:
                    request = "Exit";
                    break;
                default:
                    request = "Wrong Input";
                    break;
            }
            output.println(request);
            String username = null;
            String password = null;
            boolean filetransfer = false;
            String reply = "unsuccessful";
            // file containing the credential information
            File user = new File("user.txt");
            // Login action: authenticating the client
            if (Integer.parseInt(choice) == 1) {
                username = input.readLine();
                password = input.readLine();
                String readline;
                boolean flag = false;
                if (user.exists()) {
                    try {
                        FileReader fileReader = new FileReader(user);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        readline = bufferedReader.readLine();
                        // Authenticating the user
                        while (readline != null) {
                            String[] line = readline.split(",");
                            if (username.equalsIgnoreCase(line[0]) && password.equalsIgnoreCase(line[1])) {
                                System.out.println(username + " verified");
                                flag = true;
                                break;
                            }
                            readline = bufferedReader.readLine();
                        }
                        fileReader.close();
                        bufferedReader.close();
                        // if authentication successful respond successful and forward the client for file transfer option
                        if (flag) {
                            filetransfer = true;
                            reply = "successful";
                        }
                        output.println(reply);
                    } catch (Exception e) {
                        System.out.println("File Error:" + e);
                    }
                } else {
                    System.out.println("File doesn't exist");
                }
            }
            // Register action: registering the client
            if (Integer.parseInt(choice) == 2) {
                String readline;
                if (user.exists()) {
                    try {
                        FileReader fileReader = new FileReader(user);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        boolean flag = true;
                        while (flag) {
                            username = input.readLine();
                            password = input.readLine();
                            readline = bufferedReader.readLine();
                            // checking whether the entered username already exist, if so retry
                            while (readline != null) {
                                String[] line = readline.split(",");
                                if (username.equalsIgnoreCase(line[0])) {
                                    reply = "retry";
                                    output.println(reply);
                                    flag = true;
                                    break;
                                }
                                readline = bufferedReader.readLine();
                                flag = false;
                            }
                        }
                        FileWriter fileWriter = new FileWriter(user, true);
                        fileWriter.write("\n" + username + "," + password);
                        fileWriter.flush();
                        fileWriter.close();
                        fileReader.close();
                        bufferedReader.close();
                        reply = "successful";
                        output.println(reply);
                        filetransfer = true;

                    } catch (Exception e) {
                        System.out.println("File Error: " + e);
                    }
                }
            }
            // File transfer choices
            String s = "0";
            if (reply.equalsIgnoreCase("successful")) {
                s = input.readLine();
                System.out.println("Choice received: " + s);
            }
            // File transfer
            if (filetransfer && s.equalsIgnoreCase("1")) {
                output.println("File Transfer");
                String filename = input.readLine();
                // checking whether the requested file exist
                File requestedFile = new File("Files/" + filename + ".txt");
                if (requestedFile.exists()) {
                    output.println("exist");
                    while (true) {
                        System.out.println("Sending File :: File: " + filename + " Client: " + username);
                        FileReader fileReader = new FileReader(requestedFile);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String read = bufferedReader.readLine();
                        while (read != null) {
                            String encrypt = "";
                            // encryption -- reverse the string
                            for (int i = read.length() - 1; i >= 0; i--) {
                                encrypt += read.charAt(i);
                            }
                            output.println(encrypt);
                            int hash = read.hashCode();
                            // Byzantine Behavior
                            if (Math.random() < 0.25) {
                                System.out.println("Byzantine Failure");
                                hash = encrypt.hashCode();
                            }
                            output.println(Integer.toString(hash));
                            read = bufferedReader.readLine();
                        }
                        bufferedReader.close();
                        fileReader.close();
                        output.println(read);
                        System.out.println("File Transfered :: File: " + filename + " Client: " + username);
                        // If the checksum at clients end matches then break the loop
                        if (input.readLine().equalsIgnoreCase("noretry")) {
                            break;
                        }
                        System.out.println("Resending :: File: " + filename + " Client: " + username);
                    }
                } else
                    output.println("Doesn't exist");
            }
            if (s.equalsIgnoreCase("2"))
                output.println("Exit");
            System.out.println("Client Connection Closed, Address: " + socket.getInetAddress().getHostAddress());
        } catch (IOException ex) {
            System.out.println("Client Connection Closed, Address: " + socket.getInetAddress().getHostAddress());
        } finally {
            try {
                output.close();
                input.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}