package reentrantlock;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import common.Logger;


 //Gestion de l'aéroport avec le mécanisme REENTRANTLOCK
 
public class AeroportReentrantLock {
    
    private final int NB_PISTES_TOTAL = 2;
    private final int NB_PORTES_TOTAL = 4;
    
    private final ReentrantLock lock = new ReentrantLock();
    
    private final Condition pistesArriveeLibres;
    private final Condition pistesDepartLibres;
    private final Condition portesLibres;
    
    private int pistesDisponibles;
    private int portesDisponibles;
    
    // COMPTEURS 
    private int nbArrivesEnAttente = 0;
    private int nbDepartsEnAttente = 0;
    
    // CONSTRUCTEUR
    public AeroportReentrantLock() {
        this.pistesDisponibles = NB_PISTES_TOTAL;
        this.portesDisponibles = NB_PORTES_TOTAL;
        
        // Créer les conditions à partir du lock
        this.pistesArriveeLibres = lock.newCondition();
        this.pistesDepartLibres = lock.newCondition();
        this.portesLibres = lock.newCondition();
        
        Logger.info("Aéroport initialisé (REENTRANTLOCK) : " + NB_PISTES_TOTAL + 
                    " pistes, " + NB_PORTES_TOTAL + " portes");
    }
    
   
    public void demanderPisteArrivee(String avionId) {
        lock.lock();  
        try {
            nbArrivesEnAttente++;
            
            Logger.warning(avionId + " attend une piste pour atterrir " +
                           "(Pistes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + 
                           ", Arrivées en attente: " + nbArrivesEnAttente + ")");
            
            // Attendre dans la salle "pistesArriveeLibres"
            while (pistesDisponibles == 0) {
                pistesArriveeLibres.await();
            }
            
            // Prendre une piste
            nbArrivesEnAttente--;
            pistesDisponibles--;
            
            Logger.success(avionId + " a obtenu une piste pour atterrir " +
                           "(Pistes restantes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + ")");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.error(avionId + " - Interruption pendant l'attente d'une piste");
        } finally {
            lock.unlock();  // 🔓 TOUJOURS déverrouiller !
        }
    }
    
    /**
     * Demander une piste pour DÉCOLLER (moins prioritaire)
     * @param avionId Identifiant de l'avion
     */
    public void demanderPisteDepart(String avionId) {
        lock.lock();  
        try {
            nbDepartsEnAttente++;
            
            Logger.warning(avionId + " attend une piste pour décoller " +
                           "(Pistes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + 
                           ", Arrivées en attente: " + nbArrivesEnAttente + ")");
            
            // Attendre dans la salle "pistesDepartLibres"
            while (pistesDisponibles == 0 || nbArrivesEnAttente > 0) {
                pistesDepartLibres.await();
            }
            
            // Prendre une piste
            nbDepartsEnAttente--;
            pistesDisponibles--;
            
            Logger.success(avionId + " a obtenu une piste pour décoller " +
                           "(Pistes restantes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + ")");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.error(avionId + " - Interruption pendant l'attente d'une piste");
        } finally {
            lock.unlock();  
        }
    }
    
   
    public void libererPiste(String avionId, String typeAction) {
        lock.lock();  
        try {
            pistesDisponibles++;
            
            Logger.info(avionId + " libère une piste après " + typeAction + 
                        " (Pistes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + ")");
            
            if (nbArrivesEnAttente > 0) {
                pistesArriveeLibres.signal();  // Réveille UNE arrivée
            } else if (nbDepartsEnAttente > 0) {
                pistesDepartLibres.signal();   // Réveille UN départ
            }
            
        } finally {
            lock.unlock();  
        }
    }
    
   
    public void demanderPorte(String avionId) {
        lock.lock();  
        try {
            Logger.warning(avionId + " cherche une porte " +
                           "(Portes: " + portesDisponibles + "/" + NB_PORTES_TOTAL + ")");
            
            // Attendre dans la salle "portesLibres"
            while (portesDisponibles == 0) {
                portesLibres.await();
            }
            
            // Prendre une porte
            portesDisponibles--;
            
            Logger.success(avionId + " a obtenu une porte " +
                           "(Portes restantes: " + portesDisponibles + "/" + NB_PORTES_TOTAL + ")");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.error(avionId + " - Interruption pendant l'attente d'une porte");
        } finally {
            lock.unlock();  
        }
    }
    
    /**
     * Libérer une porte
     * @param avionId Identifiant de l'avion
     */
    public void libererPorte(String avionId) {
        lock.lock();  
        try {
            portesDisponibles++;
            
            Logger.info(avionId + " libère une porte " +
                        "(Portes: " + portesDisponibles + "/" + NB_PORTES_TOTAL + ")");
            
            // Réveiller UN avion qui attend une porte
            portesLibres.signal();
            
        } finally {
            lock.unlock();  
        }
    }
    
    
    public void afficherEtat() {
        lock.lock();  
        try {
            Logger.info("ÉTAT DE L'AÉROPORT (REENTRANTLOCK)");
            System.out.println("    Pistes : " + pistesDisponibles + "/" + NB_PISTES_TOTAL);
            System.out.println("   Portes : " + portesDisponibles + "/" + NB_PORTES_TOTAL);
            System.out.println("    Arrivées en attente : " + nbArrivesEnAttente);
            System.out.println("    Départs en attente : " + nbDepartsEnAttente);
        } finally {
            lock.unlock(); 
        }
    }
}