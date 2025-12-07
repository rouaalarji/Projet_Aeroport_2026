package gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import common.*;
import monitor.*;
import semaphore.*;
import reentrantlock.*;

/**
 * Interface avec HISTORIQUE DÉTAILLÉ de chaque avion
 */
public class AeroportGUI extends JFrame {
    
    // ========== COULEURS ==========
    private final Color BG = new Color(240, 245, 250);
    private final Color CARD = Color.WHITE;
    private final Color ACCENT = new Color(52, 152, 219);
    private final Color SUCCESS = new Color(46, 204, 113);
    private final Color DANGER = new Color(231, 76, 60);
    private final Color WARNING = new Color(243, 156, 18);
    
    // ========== COMPOSANTS ==========
    private JLabel[] labelsPistes;
    private JLabel[] labelsPortes;
    private DefaultTableModel tableAvionsActifs;
    private DefaultTableModel tableHistorique;
    private JTextArea consoleArea;
    private JComboBox<String> versionCombo;
    private JLabel labelStats;
    
    // ========== DONNÉES ==========
    private Object aeroportActuel;
    private String versionActuelle = "Semaphore";
    private int compteurAvions = 1;
    private int avionsActifs = 0;
    private Map<String, List<String>> historiqueAvions = new HashMap<>();
    
    // ========== CONSTRUCTEUR ==========
    public AeroportGUI() {
        super("✈️ Aéroport - Gestion avec Historique");
        
        aeroportActuel = new AeroportSemaphore();
        
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG);
        
        creerInterface();
        
        setLocationRelativeTo(null);
        setVisible(true);
        
        console("🚀 Système démarré - Version Semaphore");
    }
    
    // ========== CRÉATION INTERFACE ==========
    
    private void creerInterface() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // HAUT : Header
        main.add(creerHeader(), BorderLayout.NORTH);
        
        // CENTRE : 3 colonnes
        JPanel centre = new JPanel(new GridLayout(1, 3, 10, 0));
        centre.setBackground(BG);
        
        centre.add(creerPanelRessources());
        centre.add(creerPanelAvionsActifs());
        centre.add(creerPanelHistorique());
        
        main.add(centre, BorderLayout.CENTER);
        
        // BAS : Console
        main.add(creerPanelConsole(), BorderLayout.SOUTH);
        
        add(main);
    }
    
    private JPanel creerHeader() {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setBackground(ACCENT);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titre = new JLabel("✈️ Système de Gestion Aéroportuaire");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        
        labelStats = new JLabel("0 actifs • 0 terminés");
        labelStats.setFont(new Font("Arial", Font.PLAIN, 13));
        labelStats.setForeground(Color.WHITE);
        
        JPanel gauche = new JPanel();
        gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
        gauche.setOpaque(false);
        gauche.add(titre);
        gauche.add(labelStats);
        
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        droite.setOpaque(false);
        
        JLabel lbl = new JLabel("Version:");
        lbl.setForeground(Color.WHITE);
        versionCombo = new JComboBox<>(new String[]{"Monitor", "Semaphore", "ReentrantLock"});
        versionCombo.setSelectedIndex(1);
        versionCombo.setPreferredSize(new Dimension(130, 28));
        versionCombo.addActionListener(e -> changerVersion());
        
        droite.add(lbl);
        droite.add(versionCombo);
        droite.add(creerBouton("🛬 Arrivée", SUCCESS, e -> ajouterAvion(true)));
        droite.add(creerBouton("🛫 Départ", WARNING, e -> ajouterAvion(false)));
        droite.add(creerBouton("🔄", DANGER, e -> reinitialiser()));
        
        header.add(gauche, BorderLayout.WEST);
        header.add(droite, BorderLayout.EAST);
        
        return header;
    }
    
    private JButton creerBouton(String texte, Color couleur, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texte);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(texte.length() > 2 ? 90 : 35, 28));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }
    
    private JPanel creerPanelRessources() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createTitledBorder("📊 Ressources"));
        
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(CARD);
        contenu.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Pistes
        JLabel titrePistes = new JLabel("🛬 Pistes");
        titrePistes.setFont(new Font("Arial", Font.BOLD, 13));
        contenu.add(titrePistes);
        contenu.add(Box.createVerticalStrut(10));
        
        labelsPistes = new JLabel[2];
        for (int i = 0; i < 2; i++) {
            JPanel piste = creerItemRessource("P" + (i + 1));
            labelsPistes[i] = (JLabel) piste.getComponent(1);
            contenu.add(piste);
            contenu.add(Box.createVerticalStrut(8));
        }
        
        contenu.add(Box.createVerticalStrut(10));
        
        // Portes
        JLabel titrePortes = new JLabel("🚪 Portes");
        titrePortes.setFont(new Font("Arial", Font.BOLD, 13));
        contenu.add(titrePortes);
        contenu.add(Box.createVerticalStrut(10));
        
        labelsPortes = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            JPanel porte = creerItemRessource("G" + (i + 1));
            labelsPortes[i] = (JLabel) porte.getComponent(1);
            contenu.add(porte);
            contenu.add(Box.createVerticalStrut(8));
        }
        
        panel.add(contenu, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel creerItemRessource(String nom) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        item.setBackground(new Color(248, 249, 250));
        item.setBorder(BorderFactory.createLineBorder(SUCCESS, 2));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel labelNom = new JLabel(nom);
        labelNom.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel labelStatut = new JLabel("LIBRE");
        labelStatut.setFont(new Font("Arial", Font.PLAIN, 11));
        labelStatut.setForeground(SUCCESS);
        
        item.add(labelNom);
        item.add(labelStatut);
        
        return item;
    }
    
    private JPanel creerPanelAvionsActifs() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createTitledBorder("✈️ Avions Actifs"));
        
        String[] cols = {"ID", "Type", "État"};
        tableAvionsActifs = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableAvionsActifs);
        table.setFont(new Font("Monospaced", Font.PLAIN, 11));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        table.getTableHeader().setBackground(ACCENT);
        table.getTableHeader().setForeground(Color.WHITE);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String etat = (String) table.getValueAt(row, 2);
                    if (etat.contains("Attente")) {
                        c.setBackground(new Color(255, 243, 205));
                    } else if (etat.contains("Atterrissage") || etat.contains("Décollage") || etat.contains("Stationnement")) {
                        c.setBackground(new Color(255, 228, 225));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel creerPanelHistorique() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createTitledBorder("📜 Historique Complet"));
        
        String[] cols = {"ID", "Trace Complète"};
        tableHistorique = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableHistorique);
        table.setFont(new Font("Monospaced", Font.PLAIN, 10));
        table.setRowHeight(100);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        
        // Renderer pour afficher le HTML
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (column == 1 && value != null) {
                    JTextArea textArea = new JTextArea(value.toString());
                    textArea.setWrapStyleWord(true);
                    textArea.setLineWrap(true);
                    textArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
                    textArea.setBackground(new Color(232, 245, 233));
                    textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    return textArea;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel creerPanelConsole() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createTitledBorder("📡 Console en Temps Réel"));
        panel.setPreferredSize(new Dimension(0, 130));
        
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Consolas", Font.PLAIN, 10));
        consoleArea.setBackground(new Color(30, 30, 30));
        consoleArea.setForeground(new Color(0, 255, 0));
        consoleArea.setLineWrap(true);
        consoleArea.setWrapStyleWord(true);
        
        panel.add(new JScrollPane(consoleArea), BorderLayout.CENTER);
        return panel;
    }
    
    // ========== ACTIONS ==========
    
    private void ajouterAvion(boolean estArrivee) {
        String id = "FL" + String.format("%03d", compteurAvions++);
        String type = estArrivee ? "🛬 Arrivée" : "🛫 Départ";
        
        avionsActifs++;
        mettreAJourStats();
        
        console("✨ " + id + " CRÉÉ (" + type + ")");
        
        // Créer l'historique pour cet avion
        List<String> historique = new ArrayList<>();
        historique.add("▶ Avion créé");
        historiqueAvions.put(id, historique);
        
        // Ajouter au tableau actifs
        tableAvionsActifs.insertRow(0, new Object[]{id, type, "Initialisé"});
        
        // Lancer dans un thread
        new Thread(() -> {
            try {
                if (estArrivee) {
                    cycleArrivee(id);
                } else {
                    cycleDepart(id);
                }
                
                // Retirer des actifs
                retirerDesActifs(id);
                
                // Ajouter à l'historique
                ajouterHistoriqueComplet(id);
                
                avionsActifs--;
                mettreAJourStats();
                
                console("✅ " + id + " → CYCLE TERMINÉ");
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private void cycleArrivee(String id) throws InterruptedException {
        List<String> histo = historiqueAvions.get(id);
        
        // Attente atterrissage
        majActif(id, "⏳ Attente piste");
        histo.add("⏳ Attente piste atterrissage");
        console(id + " → Attend une piste pour atterrir");
        Thread.sleep(500);
        
        // Atterrissage
        int piste = (int)(Math.random() * 2);
        majPiste(piste, id);
        majActif(id, "🛬 Atterrit P" + (piste + 1));
        histo.add("🛬 Atterrissage sur Piste " + (piste + 1) + " (2s)");
        console(id + " → ATTERRIT sur Piste " + (piste + 1));
        Thread.sleep(2000);
        majPiste(piste, null);
        histo.add("✓ Piste " + (piste + 1) + " libérée");
        
        // Attente porte
        majActif(id, "⏳ Attente porte");
        histo.add("⏳ Attente porte d'embarquement");
        console(id + " → Attend une porte");
        Thread.sleep(500);
        
        // Stationnement
        int porte = (int)(Math.random() * 4);
        majPorte(porte, id);
        majActif(id, "🚪 Stationne G" + (porte + 1));
        histo.add("🚪 Stationnement à Porte " + (porte + 1) + " (3s)");
        console(id + " → STATIONNE à Porte " + (porte + 1));
        Thread.sleep(3000);
        majPorte(porte, null);
        histo.add("✓ Porte " + (porte + 1) + " libérée");
        
        // Attente décollage
        majActif(id, "⏳ Attente décollage");
        histo.add("⏳ Attente piste décollage");
        console(id + " → Attend pour décoller");
        Thread.sleep(500);
        
        // Décollage
        int piste2 = (int)(Math.random() * 2);
        majPiste(piste2, id);
        majActif(id, "🛫 Décolle P" + (piste2 + 1));
        histo.add("🛫 Décollage depuis Piste " + (piste2 + 1) + " (2s)");
        console(id + " → DÉCOLLE de Piste " + (piste2 + 1));
        Thread.sleep(2000);
        majPiste(piste2, null);
        histo.add("✓ Piste " + (piste2 + 1) + " libérée");
        histo.add("🎉 Vol terminé avec succès");
    }
    
    private void cycleDepart(String id) throws InterruptedException {
        List<String> histo = historiqueAvions.get(id);
        
        // Attente décollage
        majActif(id, "⏳ Attente décollage");
        histo.add("⏳ Attente piste décollage");
        console(id + " → Attend pour décoller");
        Thread.sleep(500);
        
        // Décollage
        int piste = (int)(Math.random() * 2);
        majPiste(piste, id);
        majActif(id, "🛫 Décolle P" + (piste + 1));
        histo.add("🛫 Décollage depuis Piste " + (piste + 1) + " (2s)");
        console(id + " → DÉCOLLE de Piste " + (piste + 1));
        Thread.sleep(2000);
        majPiste(piste, null);
        histo.add("✓ Piste " + (piste + 1) + " libérée");
        histo.add("🎉 Vol terminé avec succès");
    }
    
    private void changerVersion() {
        versionActuelle = (String) versionCombo.getSelectedItem();
        switch (versionActuelle) {
            case "Monitor": aeroportActuel = new AeroportMonitor(); break;
            case "Semaphore": aeroportActuel = new AeroportSemaphore(); break;
            case "ReentrantLock": aeroportActuel = new AeroportReentrantLock(); break;
        }
        console("🔄 Version changée : " + versionActuelle);
    }
    
    private void reinitialiser() {
        changerVersion();
        compteurAvions = 1;
        avionsActifs = 0;
        tableAvionsActifs.setRowCount(0);
        tableHistorique.setRowCount(0);
        historiqueAvions.clear();
        consoleArea.setText("");
        
        for (int i = 0; i < 2; i++) majPiste(i, null);
        for (int i = 0; i < 4; i++) majPorte(i, null);
        
        mettreAJourStats();
        console("🔄 Système réinitialisé");
    }
    
    // ========== MISES À JOUR ==========
    
    private void mettreAJourStats() {
        SwingUtilities.invokeLater(() -> {
            int termines = tableHistorique.getRowCount();
            labelStats.setText(avionsActifs + " actifs • " + termines + " terminés");
        });
    }
    
    private synchronized void majActif(String id, String etat) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tableAvionsActifs.getRowCount(); i++) {
                if (tableAvionsActifs.getValueAt(i, 0).equals(id)) {
                    tableAvionsActifs.setValueAt(etat, i, 2);
                    break;
                }
            }
        });
    }
    
    private synchronized void retirerDesActifs(String id) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tableAvionsActifs.getRowCount(); i++) {
                if (tableAvionsActifs.getValueAt(i, 0).equals(id)) {
                    tableAvionsActifs.removeRow(i);
                    break;
                }
            }
        });
    }
    
    private synchronized void ajouterHistoriqueComplet(String id) {
        SwingUtilities.invokeLater(() -> {
            List<String> histo = historiqueAvions.get(id);
            String trace = String.join("\n", histo);
            tableHistorique.insertRow(0, new Object[]{id, trace});
        });
    }
    
    private synchronized void majPiste(int numero, String avionId) {
        SwingUtilities.invokeLater(() -> {
            JPanel parent = (JPanel) labelsPistes[numero].getParent();
            if (avionId == null) {
                labelsPistes[numero].setText("LIBRE");
                labelsPistes[numero].setForeground(SUCCESS);
                parent.setBorder(BorderFactory.createLineBorder(SUCCESS, 2));
            } else {
                labelsPistes[numero].setText(avionId);
                labelsPistes[numero].setForeground(DANGER);
                parent.setBorder(BorderFactory.createLineBorder(DANGER, 2));
            }
        });
    }
    
    private synchronized void majPorte(int numero, String avionId) {
        SwingUtilities.invokeLater(() -> {
            JPanel parent = (JPanel) labelsPortes[numero].getParent();
            if (avionId == null) {
                labelsPortes[numero].setText("LIBRE");
                labelsPortes[numero].setForeground(SUCCESS);
                parent.setBorder(BorderFactory.createLineBorder(SUCCESS, 2));
            } else {
                labelsPortes[numero].setText(avionId);
                labelsPortes[numero].setForeground(DANGER);
                parent.setBorder(BorderFactory.createLineBorder(DANGER, 2));
            }
        });
    }
    
    private void console(String msg) {
        SwingUtilities.invokeLater(() -> {
            String h = new SimpleDateFormat("HH:mm:ss").format(new Date());
            consoleArea.append("[" + h + "] " + msg + "\n");
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        });
    }
    
    // ========== MAIN ==========
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> new AeroportGUI());
    }
}