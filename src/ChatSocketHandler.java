import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class used in separate threads for each connected client.
 * implements Runnable to be used in thread.
 * Communicator to server via socket.
 * Takes incoming images from server.
 * Forward incoming images to Chat.
 * ChatSocketHandler is listening to the server while Chat can read from keyboard.
 */
public class ChatSocketHandler implements Runnable {

    private Chat chat;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Constructor.
     * Create out stream.
     * @param chat = chat
     * @param socket = connected socket
     * @throws IOException propagates to startChat in Chat
     */
    public ChatSocketHandler(Chat chat, Socket socket) throws IOException {
        this.chat = chat;
        this.socket = socket;
        createStreams();
    }


    /**
     * Listening for incoming audio
     * to be sent to the receiver.
     */
    @Override
    public void run() {
        try {
            while (true) {

                in = new ObjectInputStream(socket.getInputStream());
                Object object = in.readObject();

                if (object instanceof Storage) {
                    Storage storage = (Storage) object;

                    chat.getAudioBytesFromChatHandler(storage);
                }
            }

        } catch (IOException e) {
            System.err.println("Connection down.");
        } catch (ClassNotFoundException e) {
            System.err.println("Class Not Found");
        }
    }


    /**
     * Send audio data object to server via socket (out).
     * @param storage = serialized audio object
     */
    public void sendAudioData(Storage storage) {
        try {
            out.writeObject(storage);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Create object streams to server via socket.
     * out: to server
     * in: from server
     * @throws IOException propagates to constructor.
     */
    private void createStreams() throws IOException {
        // write object to socket
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
    }

}
