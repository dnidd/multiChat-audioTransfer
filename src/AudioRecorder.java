import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class to capture TargetDataLine audio.
 * Create, store and return Storage containing audio data.
 */
public class AudioRecorder {

    private AudioFormat format = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2, 8000.0F, false);
    private DataLine.Info info;
    private TargetDataLine lineIn;
    private ByteArrayOutputStream baos;
    private Thread recordingThread;


    /**
     * Capture TargetDataLine audio to ByteArrayOutputStream.
     * @throws IOException = IOException
     */
    public void startAudioRecording() throws IOException {
        try {
            // Audio input handled by TargetDataLine with info from DataLine and format.
            info = new DataLine.Info(TargetDataLine.class, format);

            lineIn = (TargetDataLine) AudioSystem.getLine(info);

            // Open/prepare device to capture audio (not recording yet).
             lineIn.open(format);
            // starts the microphone recording, not the writing to file.
            lineIn.start();

            recordingThread = new Thread(() -> {
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];

                try {
                    int numBytesRead;
                    // read audio
                    // write to baos (ByteArrayOutputStream)
                    while((numBytesRead = lineIn.read(buffer, 0, buffer.length)) != -1) {

                        if(numBytesRead > 0) {
                            baos.write(buffer, 0, numBytesRead);
                        }
                    }
                    baos.close();
                } catch (IOException e) {
                    System.err.println("I/O error: " + e.getMessage());
                }
            });

            recordingThread.start();
            System.out.println("Recording…");

        } catch (LineUnavailableException e) {
            throw new IOException(e);
        }

    }

    /**
     * Stop recording & create Storage containing audio data.
     * @return Storage.
     */
    public Storage stopAudioRecording() {
        lineIn.stop();
        lineIn.close();

        System.out.println("Stopping the recording.");

        return new Storage(baos, "audio");
    }


}
