import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

// client file
public class Client {

    // client constructor
    public Client(String host, int port) {
        try {
            System.out.println("Server Connected, Address: " + host);
            Socket socket = null;
            PrintWriter output = null;
            BufferedReader input = null;
            String username = null;
            String password = null;
            try {
                socket = new Socket(host, port);
                output = new PrintWriter(socket.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Host Connection Error: " + host);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Server Error");
                System.exit(1);
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Welcome to File Transfer System");
            System.out.println("-------------------------------");
            System.out.println("Select any one option:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            String choice = bufferedReader.readLine();
            // sending choice to the server
            output.println(choice);
            Boolean filetransfer = false;
            // server response
            System.out.println("You selected this option: " + input.readLine());
            // login action
            if (Integer.parseInt(choice) == 1) {
                System.out.println("Enter Username: ");
                username = bufferedReader.readLine();
                output.println(username);
                System.out.println("Enter Password: ");
                password = bufferedReader.readLine();
                output.println(password);
                String reply = input.readLine();
                // authentication response from server
                if (reply.equalsIgnoreCase("successful")) {
                    filetransfer = true;
                    System.out.println("Authentication successful");
                } else
                    System.out.println("Authentication unsuccessful");
            }
            // register action
            if (Integer.parseInt(choice) == 2) {
                while (true) {
                    System.out.println("Enter Username: ");
                    username = bufferedReader.readLine();
                    output.println(username);
                    System.out.println("Enter Password: ");
                    password = bufferedReader.readLine();
                    output.println(password);
                    // if username already exist retry
                    if (!input.readLine().equalsIgnoreCase("retry")) {
                        filetransfer = true;
                        System.out.println("Registration Successful");
                        break;
                    }
                    System.out.println("Username already exist, please try another username");
                }
            }
            // file transfer
            if (filetransfer) {
                System.out.println("Welcome to File Transfer System");
                System.out.println("-------------------------------");
                System.out.println("Select any one option:");
                System.out.println("1. File Transfer");
                System.out.println("2. Exit");
                choice = bufferedReader.readLine();
                output.println(choice);
                System.out.println("You selected this option: " + input.readLine());
                // send file request to the server
                if (choice.equalsIgnoreCase("1")) {
                    System.out.println("Enter the filename: ");
                    String filename = bufferedReader.readLine();
                    output.println(filename);
                    // checking whether file exist
                    if (input.readLine().equalsIgnoreCase("exist")) {
                        File newDirectory = new File(username);
                        newDirectory.mkdir();
                        File newFile = new File(username + "/" + filename + ".txt");
                        int count = 1;
                        Boolean checksum = false;
                        while (true) {
                            System.out.println("Attempting to Receive File...");
                            FileWriter fileWriter = new FileWriter(newFile, true);
                            String read = input.readLine();
                            while (!read.equalsIgnoreCase("null")) {
                                String decrypt = "";
                                // decryption -- reverse the string
                                for (int i = read.length() - 1; i >= 0; i--) {
                                    decrypt += read.charAt(i);
                                }
                                int clientHash = decrypt.hashCode();
                                int serverHash = Integer.parseInt(input.readLine());
                                if (clientHash != serverHash) {
                                    checksum = true;
                                    break;
                                }
                                fileWriter.write(decrypt);
                                read = input.readLine();
                            }
                            fileWriter.flush();
                            fileWriter.close();
                            // If the checksum doesn't match then retry else ask to retransmitt
                            if (!checksum) {
                                System.out.println("File Transmission Successful");
                                output.println("noretry");
                                break;
                            } else {
                                if (count <= 5) {
                                    System.out.println("File Transmission:: checksum error :: Retrying attempt :" + count);
                                    output.println("retry");
                                    checksum = false;
                                    count++;
                                } else {
                                    newFile.delete();
                                    newDirectory.delete();
                                    System.out.println("File Transmission Unsuccessful");
                                    output.println("noretry");
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("File doesn't exist");
                    }
                }

            }
            // closing connection
            System.out.println("Connection closed");
            output.close();
            input.close();
            bufferedReader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        int port = 4004;
        new Client("10.234.136.55", port);
        //new Client("127.0.0.1", port);
    }
}