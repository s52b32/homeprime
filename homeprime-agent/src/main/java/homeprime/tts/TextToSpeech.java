package homeprime.tts;
//package strawa.hab.tts;
//import java.io.File;
//import java.io.FileOutputStream;
//
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//
//import com.voicerss.tts.AudioCodec;
//import com.voicerss.tts.AudioFormat;
//import com.voicerss.tts.Languages;
//import com.voicerss.tts.VoiceParameters;
//import com.voicerss.tts.VoiceProvider;
//
//public class TextToSpeech {
//    public static void main (String args[]) throws Exception {
//        VoiceProvider tts = new VoiceProvider("44d62d3b34fb45ba9a03d2c12de0858a");
//		
//        VoiceParameters params = new VoiceParameters("Hello, world!", Languages.English_UnitedStates);
//        params.setCodec(AudioCodec.WAV);
//        params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
//        params.setBase64(false);
//        params.setSSML(false);
//        params.setRate(0);
//		
//        byte[] voice = tts.speech(params);
//		
//        FileOutputStream fos = new FileOutputStream("voice.wav");
//        fos.write(voice, 0, voice.length);
//        fos.flush();
//        fos.close();
//        
//        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("voice.wav"));
//        Clip clip = AudioSystem.getClip();
//        clip.open(audioIn);
//        clip.start();
//    }
//    
//    public static synchronized void playSound() {
//        new Thread(new Runnable() {
//        // The wrapper thread is unnecessary, unless it blocks on the
//        // Clip finishing; see comments.
//          public void run() {
//            try {
//              Clip clip = AudioSystem.getClip();
//              AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("voice.wav"));
//              clip.open(inputStream);
//              clip.start(); 
//            } catch (Exception e) {
//              System.err.println(e.getMessage());
//            }
//          }
//        }).start();
//      }
//}