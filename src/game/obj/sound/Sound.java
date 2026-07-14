package game.obj.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    private final URL shoot;
    private final URL hit;
    private final URL destroy;

    public Sound() {
        this.shoot  = this.getClass().getClassLoader().getResource("game/obj/sound/shoot.wav");
        this.hit = this.getClass().getClassLoader().getResource("game/obj/sound/hit.wav");
        this.destroy = this.getClass().getClassLoader().getResource("game/obj/sound/destroy.wav");
    }

    public void soundShoot() {
        playSound(shoot);
    }
    public void soundHit() {
        playSound(hit);
    }
    public void soundDestroy() {
        playSound(destroy);
    }

    private void playSound(URL url) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.addLineListener(event -> {
                if(event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            }
            );
            audioInputStream.close();
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            throw new RuntimeException();
        }
    }
}
