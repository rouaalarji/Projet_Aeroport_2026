package common;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

 //Affiche l'heure et le message de manière synchronisée
 
public class Logger {
    
    // Format pour afficher l'heure
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss");
    
    
    public static synchronized void log(String message) {
        String heure = LocalTime.now().format(TIME_FORMATTER);
        System.out.println("[" + heure + "] " + message);
    }
    
        public static void info(String message) {
        log("information " + message);
    }
    
    
    public static void success(String message) {
        log( message);
    }
    
    public static void warning(String message) {
        log( message);
    }
    
    public static void error(String message) {
        log( message);
    }
    
    /**
     * Affiche une ligne de séparation
     */
    public static void separateur() {
        System.out.println("═══════════════════════════════════════════════════");
    }
    
  
    public static void titre(String titre) {
        separateur();
        log( titre);
        separateur();
    }
}