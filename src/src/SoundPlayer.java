import javax.sound.sampled.*; // Gives the classes for playing audio
import java.io.File; // Helps find the sound files
import java.io.IOException; // Exception Handler

public class SoundPlayer {
    // Method to play a sound, given the file path
    public static void playSound(String filePath) {
        try {
            File soundFile = new File(filePath); // Creates an object for the file
            if (!soundFile.exists()) {
                System.err.println("Sound file not found: " + filePath);
                return; // Leave is the sound file doesn't exist
            }
            // Creates an audio stream so it can be read
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            // Creates the clip to be played
            Clip clip = AudioSystem.getClip();
            // Adds the audio into the clip
            clip.open(audioStream);
            // Plays the clip
            clip.start();
        // Catches the usual errors and gives the failure message
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }
}
