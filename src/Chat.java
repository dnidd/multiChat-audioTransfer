import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Connects to server via Socket.
 * Create communication Thread.
 * Take user input - send to Handler.
 * Take image coming from server.
 * Chat can read from the keyboard while ChatSocketHandler is listening to the server.
 */
public class Chat {

    private Socket socket;
    private ChatSocketHandler chatSocketHandler;
    private AudioRecorder audioRecorder;
    private SourceDataLine lineOut;

    /**
     * Constructor
     * @param host = server address/host
     * @param port = port
     */
    public Chat(String host, int port) throws IOException {
        // Connect socket to server
        socket = new Socket(host, port);
        System.out.println("Connected to " + host + ":" + port);
    }

    /**
     * User manages audio recording.
     * Recording is sent to server via ChatSocketHandler.
     * @throws IOException propagates to main in Client
     */
    public void startAudioChat() throws IOException {

        chatSocketHandler = new ChatSocketHandler(this, socket);
        // Chat can read from the keyboard while
        // ChatSocketHandler is listening to the server.
        Thread thread = new Thread(chatSocketHandler);
        thread.start();

        audioRecorder = new AudioRecorder();

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("r to record." );
            System.out.println("s to stop + send." );
            System.out.println("q to quit." );

            String input = scanner.nextLine();

            // Start recording.
            if (input.equals("r")) {
                audioRecorder.startAudioRecording();
            }
            // Stop recorder and store recording in Storage object.
            else if (input.equals("s")) {
                Storage storage = audioRecorder.stopAudioRecording();
                // Send audio recording to chatSocketHandler to send to server.
                chatSocketHandler.sendAudioData(storage);
            }
            else if (input.equals("q")) {
                socket.close();
                break;
            }
        }
    }


    /**
     * Play received audio.
     * Audio Storage fetched from server (in chatSocketHandler).
     * @param storage = audio data.
     */
    public void getAudioBytesFromChatHandler(Storage storage) throws IOException {

        try {
            byte[] audio = storage.getData();

            AudioFormat format = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    8000.0F, 16, 1, 2, 8000.0F, false);
            InputStream input = new ByteArrayInputStream(audio);
            AudioInputStream ais = new AudioInputStream(input, format, audio.length / format.getFrameSize());

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            lineOut = (SourceDataLine) AudioSystem.getLine(info);

            // .open() takes AudioFormat
            lineOut.open(format);
            // Line engagement to data I/O.
            lineOut.start();

            byte[] buffer = new byte[4096];

            int c;
            while ((c = ais.read(buffer, 0, buffer.length)) != -1) {
                if (c > 0) {
                    lineOut.write(buffer, 0, c);
                }
            }

            // Drains queued data from the line
            lineOut.drain();
            // Closes the line, releases resources.
            lineOut.close();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }



}



