package be4r.lgwf.pointmanager.sound;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 *
 * @author Be4rJP
 */
public class PMSound {
    public static void playPMSound(Player player, SoundType type){
        switch(type){
            case ERROR:
                player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.flat(0, Note.Tone.G));
                player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.flat(0, Note.Tone.G));
                break;
            case SUCCESS:
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 2F);
                break;
            case CONGRATULATIONS:
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                break;
        }
    }
}
