package progettodb;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

public class ProgettoDB {
    
    private MainWindow mw = new MainWindow();
    private MainWindow.Image im = null;
    private Hero eroe = null;
    public Connection conn = null;
    private String player = null;
    private static Game g = null;
    private int worldState = 0;
    private String stats[] = {"update mp","update hp","select abilità","insert apprende",
        "select possiede","select abilità","insert personaggio","select giocatore",
        "select nome personaggio","delete personaggio","Lista zone",
        "select caratteristiche zona","select appartiene","update disponibile",
        "select inventario","consuma oggetto","seleziona danno arma","Seleziona hp personaggio",
        "seleziona mp personaggio","select arma equipaggiata","select zona personaggio",
        "select classe personaggio","seleziona livello personaggio","seleziona stat personaggio",
        "seleziona esperienza personaggio",
        "select quantità oggetto","aggiungi oggetto","update quantità","select exp da livello",
        "update exp person","update livello pers","update stat personaggio","update zona personaggio",
        "update arma","select hp mostro","select razza mostro","select danno razza","select nomeattacco mostro"};
    private long time = 0;
    
    private Integer nStats[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    
    private class Game
    {
        
        public Game()
        {
            try{
                conn = DB.connect(false);
                time = System.currentTimeMillis();
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        Sort.pMergeSort(nStats, stats);
                        for(int i = 0; i<stats.length; i++)
                        {
                            System.out.println(stats[i] + "\t\t\t\t\t\t" + nStats[i]);
                        }
                        System.out.println((double)(System.currentTimeMillis()-time)/1000 + " secondi");
                        try
                        {
                            conn.close();
                        }
                        catch(SQLException e)
                        {
                            System.out.println(e);
                            infoSQLBox(e,"Errore");
                            System.exit(1);
                        }
                    }
                }));
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                System.exit(1);
            }  
        }
        
        public void start()
        {
            mainMenu();
            mw.clearText();
            Refresh r = new Refresh();
            while(true)
            {
                adventure();
                if(worldState == 1)
                {
                    worldState = 0;
                    mw.clearText();
                    mw.clearEnemy();
                    eroe = new Hero(eroe.getName());
                }
            }
        }
        
        public void gameOver()
        {
            im.deathAni();
            mw.appendText("\nGame Over! vuoi ricominciare?(Y/N)");
            String ch = null;
            do
            {
                ch = mw.stringAnswer();
            }while(!ch.toUpperCase().equals("Y") && !ch.toUpperCase().equals("N"));
            
            if(ch.toUpperCase().equals("Y"))
            {
                eroe.setZone("Zona1");
                try
                {
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("UPDATE personaggio SET hp=? WHERE nome=?;");                   
                    nStats[1]++;
                    pst.setInt(1, 100);
                    pst.setString(2, eroe.getName());

                    pst.executeUpdate();
                    
                    pst = null;
                    pst = conn.prepareStatement("UPDATE personaggio SET mp=? WHERE nome=?;");                   
                    nStats[0]++;
                    pst.setInt(1, 100);
                    pst.setString(2, eroe.getName());

                    pst.executeUpdate();
                    im.restart();
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                    infoSQLBox(e,"Errore");
                    System.exit(1);
                }
                worldState = 1;
                return;
            }
            else if(ch.toUpperCase().equals("N"))
            {
                eroe.setZone("Zona1");
                try
                {
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("UPDATE personaggio SET hp=? WHERE nome=?;");                   
                    nStats[1]++;
                    pst.setInt(1, 100);
                    pst.setString(2, eroe.getName());

                    pst.executeUpdate();
                    
                    pst = null;
                    pst = conn.prepareStatement("UPDATE personaggio SET mp=? WHERE nome=?;");                   
                    nStats[0]++;
                    pst.setInt(1, 100);
                    pst.setString(2, eroe.getName());

                    pst.executeUpdate();
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                    infoSQLBox(e,"Errore");
                    System.exit(1);
                }
                System.exit(0);
            }
        }
        
        private void mainMenu()
        {
            mw.appendText("Progetto DB - Avventura Testuale");
            mw.appendText("\nSeleziona il giocatore");
            player = selectPlayer();
            //mw.appendText("\nHai scelto " + player);
            mw.appendText("\n----------------------------",false);
            mw.appendText("\n1 - Nuovo Personaggio");
            mw.appendText("\n2 - Continua un personaggio");
            mw.appendText("\n3 - Elimina eroe");
            mw.appendText("\n----------------------------", false);

            while(true)
            {
                int c = mw.intChoice(1,3);
                if(c == 1)
                {
                    String hn = null;
                    String classe = null;
                    while(true)
                    {
                        hn = askHeroName();
                        classe = askHeroClass();
                        if(createHero(hn,classe)) 
                        {
                            break;
                        }
                        else
                        {
                            mw.appendText("\nErrore durante la creazione");
                        }
                    }
                    eroe = new Hero(hn);
                    eroe.addItem("PozioneBlu");
                    try
                    {
                        Statement statement = conn.createStatement();
                        ResultSet rs = null;
                        if(eroe.getClasse().toUpperCase().equals("GUERRIERO"))
                        {
                            rs=statement.executeQuery("SELECT nome,livello FROM abilità WHERE tipo='Attacco';");
                            nStats[2]++;
                        }
                        else if(eroe.getClasse().toUpperCase().equals("MAGO"))
                        {
                            rs=statement.executeQuery("SELECT nome,livello FROM abilità WHERE tipo='Magia';");
                            nStats[2]++;
                        }
                        while(rs.next())
                        {
                            if(rs.getInt("livello") == 1)
                            {
                                PreparedStatement pst = conn.prepareStatement("INSERT INTO apprende(personaggio,abilità) "
                                        + "VALUES (?,?)");
                                nStats[3]++;
                                pst.setString(1,eroe.getName());
                                pst.setString(2, rs.getString("nome"));

                                pst.executeUpdate();
                            }
                        }
                    }
                    catch(SQLException e)
                    {
                        System.out.println(e);
                        infoSQLBox(e,"Errore");
                        System.exit(1);
                    }
                    im = mw.new Image(0,classe);
                    im.start();
                    return;
                }
                else if(c==2)   
                {
                    String ch = selectHero();
                    if(ch.equals(""))
                    {
                        mw.appendText("\nNon hai eroi salvati");
                    }
                    else
                    {
                        eroe = new Hero(ch);
                        im = mw.new Image(0,eroe.getClasse());
                        im.start();
                        return;
                    }
                }
                else if(c==3)
                {
                    String ch = selectHero();
                    deleteHero(ch);
                    mw.clearText();
                    mw.appendText("\n1 - Nuovo Personaggio");
                    mw.appendText("\n2 - Continua un personaggio");
                    mw.appendText("\n3 - Elimina eroe");
                    mw.appendText("\n----------------------------", false);
                }
            }
        }
        
        private void adventure()
        {
            String nz = null;
            int ch = 0;
            while(true)
            {
                mw.appendText("Cosa vuoi fare?");
                mw.appendText("\n1- Viaggia");
                mw.appendText("\n2- Apri Inventario");
                mw.appendText("\n3- Esci dal gioco");
                
                ch = mw.intChoice(1,3);
                if(ch == 1)
                {
                    mw.appendText("\nDove vuoi andare?");
                    nz = selectZone();
                    travel(nz);
                    if(worldState == 1)
                    {
                        return;
                    }
                    mw.clearText();
                }
                else if (ch == 2)
                {
                    mw.clearText();
                    openInventory();
                }
                else if(ch == 3)
                {
                    System.exit(0);
                }
            }
        }
        
        private void battle(String enemyName)
        {
            int ch = 0;
            boolean performed = false;
            ArrayList<String> n = null;
            ArrayList<Integer> d = null;
            ArrayList<Integer> c = null;
            mw.clearText();
            mw.appendText("Hai trovato " + enemyName + "! ");
            Enemy nemico = new Enemy(enemyName);
            while(true)
            {
                mw.appendText("\nCosa vuoi fare?");
                mw.appendText("\n1- Attacca con l'arma");
                mw.appendText("\n2- Attacca con un'abilità");
                mw.appendText("\n3- Apri inventario");
                mw.appendText("\n4- Fuggi (50% di possibilità)");
                mw.appendText("\n----------------------------", false);
                ch = mw.intChoice(1,4);
                if(ch == 1)
                {
                    nemico.damage(eroe.getDanno());
                    mw.appendText("\nHai inflitto " + eroe.getDanno() + " danni!");
                    if(nemico.getCurrentHP() == 0)
                    {
                        int j = 0;
                        ArrayList<String> drops = new ArrayList<String>(1);
                        mw.appendText("\nHai sconfitto " + nemico.getName() + "!");
                        if(nemico.getName().toUpperCase().equals("SCOTTY"))
                        {
                            mw.appendText("\nCOMPLIMENTI! Hai sconfitto il mostro più forte del gioco e hai vinto!");
                            try
                            {
                                PreparedStatement pst = null;
                                pst = conn.prepareStatement("UPDATE personaggio SET GiocoVinto=? WHERE nome=?");     
                                nStats[9]++;
                                pst.setBoolean(1,true);
                                pst.setString(2, eroe.getName());
                                pst.executeUpdate();
                                return;
                            }
                            catch(SQLException e)
                            {
                                System.out.println(e);
                                infoSQLBox(e,"Errore");
                                System.exit(1);
                            }
                            tsleep(1000);
                        }
                        try
                        {
                            Statement statement = conn.createStatement();
                            ResultSet rs = null;
                            rs=statement.executeQuery("SELECT oggetto FROM possiede WHERE mostro='" + nemico.getName() + "';");
                            nStats[4]++;
                            while(rs.next())
                            {
                                drops.add(rs.getString("oggetto"));
                                j++;
                            }
                            if(j>0)
                            {
                                int rnd = randInt(0,j-1);
                                eroe.addItem(drops.get(rnd));
                                mw.appendText("Hai ottenuto " + drops.get(rnd));
                            }
                            eroe.addExp(nemico.getExp());
                            mw.clearEnemy();
                            mw.appendText("\nL'avventura continua!");
                            tsleep(1000);
                            mw.clearText();
                            return;
                        }
                        catch(SQLException e)
                        {
                             System.out.println(e);
                             infoSQLBox(e,"Errore");
                             System.exit(1);
                        }
                    }
                    performed = true;
                }
                else if(ch == 2)
                {
                    try
                    {
                        n = new ArrayList<String>(1);
                        d = new ArrayList<Integer>(1);
                        c = new ArrayList<Integer>(1);
                        int k = 0;
                        Statement statement = conn.createStatement();
                        ResultSet rs = null;
                        rs=statement.executeQuery("SELECT nome,danno,costo "
                                + "FROM abilità AS ab JOIN apprende AS ap "
                                + "ON ab.nome = ap.abilità "
                                + "WHERE personaggio = '" + eroe.getName() + "';");
                        nStats[5]++;
                        while(rs.next())
                        {
                            mw.appendText("\n" + ++k + "- " + rs.getString("nome") + " (costo: " + rs.getInt("costo") + ", danno: " + rs.getInt("danno") + ")");
                            n.add(rs.getString("nome"));
                            d.add(rs.getInt("danno"));
                            c.add(rs.getInt("costo"));
                        }
                        mw.appendText("\n" + (k+1) + " - Esci");
                        int abC = mw.intChoice(1,k+1);
                        if(abC != k+1)
                        {
                            if(c.get(abC-1)<=eroe.getMP())
                            {
                                nemico.damage(d.get(abC-1));
                                eroe.consumeMP(c.get(abC-1));
                                mw.appendText("\nHai inflitto " + d.get(abC-1) + " danni!");
                                performed = true;
                                if(nemico.getCurrentHP() == 0)
                                {
                                    int j = 0;
                                    ArrayList<String> drops = new ArrayList<String>(1);
                                    mw.appendText("\nHai sconfitto " + nemico.getName() + "!");
                                    if(nemico.getName().toUpperCase().equals("SCOTTY"))
                                    {
                                        mw.appendText("\nCOMPLIMENTI! Hai sconfitto il mostro più forte del gioco e hai vinto!");
                                        try
                                        {
                                            PreparedStatement pst = null;
                                            pst = conn.prepareStatement("UPDATE personaggio SET GiocoVinto=? WHERE nome=?");     
                                            nStats[9]++;
                                            pst.setBoolean(1,true);
                                            pst.setString(2, eroe.getName());
                                            pst.executeUpdate();
                                            return;
                                        }
                                        catch(SQLException e)
                                        {
                                            System.out.println(e);
                                            infoSQLBox(e,"Errore");
                                            System.exit(1);
                                        }
                                    }
                                    try
                                    {
                                        rs = null;
                                        rs=statement.executeQuery("SELECT oggetto FROM possiede WHERE mostro='" + nemico.getName() + "';");
                                        nStats[4]++;
                                        while(rs.next())
                                        {
                                            drops.add(rs.getString("oggetto"));
                                            j++;
                                        }
                                        if(j>0)
                                        {
                                            int rnd = randInt(0,j-1);
                                            eroe.addItem(drops.get(rnd));
                                            mw.appendText("\nHai ottenuto " + drops.get(rnd));
                                            tsleep(1000);
                                        }
                                        eroe.addExp(nemico.getExp());
                                        mw.clearEnemy();
                                        mw.appendText("\nL'avventura continua!");
                                        tsleep(1000);
                                        mw.clearText();
                                        return;
                                    }
                                    catch(SQLException e)
                                    {
                                         System.out.println(e);
                                         infoSQLBox(e,"Errore");
                                         System.exit(1);
                                    }
                                }
                            }
                            else
                            {
                                mw.appendText("\n Non hai abbastanza mana!");
                            }
                        }
                    }
                    catch(SQLException e)
                    {
                        System.out.println(e);
                        infoSQLBox(e,"Errore");
                        System.exit(1);
                    }
                }
                else if(ch == 3)
                {
                    mw.clearText();
                    if(openInventory())
                    {
                        performed = true;
                    }
                }
                else if(ch == 4)
                {
                    if(randInt(0,1) == 1)
                    {
                        mw.appendText("\nFuga riuscita!");
                        tsleep(1000);
                        mw.clearEnemy();
                        return;
                    }
                    else
                    {
                        mw.appendText("\nFuga non riuscita!");
                        tsleep(1000);
                        performed = true;
                    }
                }
                //TURNO DEL NEMICO
                if(performed == true)
                {
                    performed = false;
                    mw.appendText("\nIl nemico attacca con " + nemico.getAttacco() + " e infligge " + nemico.getForza() + " danni!");
                    eroe.damage(nemico.getForza());
                    if(worldState == 1)
                    {
                        return;
                    }
                }
            }
        }
        
        private String askHeroName()
        {
            
            mw.appendText("\nInserisci un nome");
            String name = mw.stringAnswer();
            mw.appendText("\nSei sicuro della scelta? (Y/N)");

            while(true)
            { 
                String nChoice = mw.stringAnswer();
                if(nChoice.toUpperCase().equals("Y"))
                {
                    break;
                }
                else if(nChoice.toUpperCase().equals("N"))
                {
                    mw.appendText("\nInserisci un nome");
                    name = mw.stringAnswer();
                    mw.appendText("\nSei sicuro della scelta? (Y/N)");
                }
            }
            return name;
        }
        
        private String askHeroClass()
        {
            mw.appendText("\nChe classe avrà il tuo eroe? Guerriero o Mago?");
            String classe = null;
            while(true)
            { 
                classe = mw.stringAnswer();
                if(classe.toUpperCase().equals("GUERRIERO") || classe.toUpperCase().equals("MAGO"))
                {
                    return classe;
                }
                else
                {
                    mw.appendText("\nChe classe avrà il tuo eroe? Guerriero o Mago?");
                    classe = mw.stringAnswer();
                }
            }
        }
        
        private boolean createHero(String name,String classe)
        {
            try
            {
                PreparedStatement pst = null;
                pst = conn.prepareStatement("INSERT INTO personaggio(Nome,Forza,Intelligenza,Classe,Giocatore,Zona) VALUES (?,?,?,?::tipoClasse,?,?);");                   
                nStats[6]++;
                if(classe.toUpperCase().equals("GUERRIERO"))
                {
                  pst.setString(1, name);
                  pst.setInt(2, 2);
                  pst.setNull(3, java.sql.Types.INTEGER);
                  pst.setString(4,"Guerriero");
                  pst.setString(5,player);
                  pst.setString(6, "Zona2");
                }
                else
                {
                    pst.setString(1, name);
                    pst.setNull(2, java.sql.Types.INTEGER);
                    pst.setInt(3, 2);
                    pst.setString(4,"Mago");
                    pst.setString(5,player);
                    pst.setString(6, "Zona2");
                }
                pst.executeUpdate();
                return true;
            }
            catch(SQLException e)
            {
                System.out.println(e);
                return false;
            }
        }
        
        private String selectPlayer()
        {
            int c = 0;
            ArrayList<String> p = new ArrayList<String>(1);
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT nickname FROM giocatore");
                nStats[7]++;
                while(rs.next())
                {
                    c++;
                    p.add(rs.getString("nickname"));
                    mw.appendText("\n" + c + " - " + rs.getString("nickname"));
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            int ch = mw.intChoice(1,c);
            return p.get(ch-1);

        }
        
        private String selectHero()
        {
            int c = 0;
            ArrayList<String> h = new ArrayList<String>(1);
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT nome,giocovinto FROM personaggio WHERE giocatore = '" + player + "';");
                nStats[8]++;
                while(rs.next())
                {
                    c++;
                    h.add(rs.getString("nome"));
                    mw.appendText("\n" + c + " - " + rs.getString("nome"));
                    if(rs.getBoolean("giocovinto"))
                    {
                        mw.appendText(" (Gioco completato)");
                    }

                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                System.exit(1);
            }
            if(c == 0)
            {
                return "";
            }
            int ch = mw.intChoice(1,c);
            return h.get(ch-1);

        }
        
        private void deleteHero(String ch)
        {
            String yn = null;
            if(ch.equals(""))
            {
                mw.appendText("\nNon hai eroi!");
                tsleep(500);
                return;
            }

            mw.appendText("\nNe sei davvero sicuro? (Y/N)");

            while(true)
            { 
                yn = mw.stringAnswer();
                if(yn.toUpperCase().equals("Y"))
                {
                    try
                    {
                        PreparedStatement pst = null;
                        pst = conn.prepareStatement("DELETE FROM personaggio WHERE nome=?;");     
                        nStats[9]++;
                        pst.setString(1,ch);
                        pst.executeUpdate();
                        return;
                    }
                    catch(SQLException e)
                    {
                        System.out.println(e);
                        infoSQLBox(e,"Errore");
                        System.exit(1);
                    }
                }
                else if(yn.toUpperCase().equals("N"))
                {
                    return;
                }
            }
        }
        
        private void tsleep(int ms)
        {
            try {
                Thread.sleep(ms);
            }
            catch(InterruptedException e) {}
        }
        
        private String selectZone()
        {
            int c = 0;
            ArrayList<String> h = new ArrayList<String>(1);
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT Zona2,Richiede "
                        + "FROM conduce AS c JOIN zona AS z "
                        + "ON c.zona2 = z.nome "
                        + "WHERE Zona1 = '" + eroe.zona + "';");
                nStats[10]++;
                while(rs.next())
                {
                    c++;
                    h.add(rs.getString("Zona2"));
                    mw.appendText("\n" + c + " - " + rs.getString("Zona2"));
                    if(rs.getString("Richiede") != null)
                    {
                        mw.appendText(" (Richiede " + rs.getString("Richiede") + ")");
                    }

                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            if(c == 0)
            {
                return "";
            }
            int ch = mw.intChoice(1,c);
            return h.get(ch-1);
        }
        
        private void travel(String zone)
        {
            int c = 0;
            String type = null;
            String item = null;
            String rich = null;
            boolean disp = false;
            ArrayList<String> h = new ArrayList<String>(1);
            
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("(SELECT nome,tipo,situato,disponibile,richiede " +
                                                    "FROM zona AS z LEFT JOIN inventario AS i " +
                                                    "ON z.richiede = i.oggetto " +
                                                    "WHERE personaggio = '" + eroe.getName() + "')" +
                                                    "UNION" +
                                                    "(SELECT nome,tipo,situato,disponibile,richiede " +
                                                    "FROM zona " +
                                                    "WHERE richiede IS NULL);");
                nStats[10]++;
                while(rs.next())
                {
                    if(rs.getString("nome").equals(zone))
                    {
                        c++;
                        type = rs.getString("tipo");
                        item = rs.getString("situato");
                        disp = rs.getBoolean("disponibile");
                        rich = rs.getString("richiede");
                    }
                }
                if(c == 0)
                {
                    mw.appendText("\nNon hai i requisiti per andare in questa zona!");
                    tsleep(1000);
                    return;
                }
                else
                {
                    if(rich != null)
                    {
                        consume(rich);
                    }
                    if(zone.toUpperCase().equals("ZONA12"))
                    {
                        battle("Scotty");
                    }
                    else if(randInt(0,1) == 1)
                    {
                        //BATTAGLIA
                        rs = statement.executeQuery("SELECT mostro FROM appartiene WHERE zona='" + zone + "';");
                        nStats[12]++;
                        while(rs.next())
                        {
                            h.add(rs.getString("mostro"));
                        }
                        if(h.size()>0)
                        {
                            battle(h.get(randInt(0,h.size()-1)));
                            if(worldState == 1) //gameOver
                            {
                                return;
                            }
                        }
                        //FINE BATTAGLIA
                    }
                    try
                    {
                        PreparedStatement pst = null;
                        eroe.setZone(zone);
                        if(type.toUpperCase().equals("SAFEZONE"))
                        {
                            mw.appendText("\nHai trovato una safezone! Vuoi riposarti? (Y/N)");
                            String ans = null;
                            while(true)
                            {
                                ans = mw.stringAnswer();
                                if(ans.toUpperCase().equals("Y"))
                                {
                                    eroe.heal(100);
                                    eroe.restoreMP(100);
                                    return;
                                }
                                else if(ans.toUpperCase().equals("N"))
                                {
                                    return;
                                }
                            }
                        }
                        else if(type.toUpperCase().equals("TESORO") && item != null && disp == true)
                        {
                            
                            mw.appendText("\nHai trovato un tesoro!");
                            mw.appendText("\nNello scrigno hai trovato: " + item);
                            eroe.addItem(item);
                            tsleep(1000);
                            pst = null;
                            pst = conn.prepareStatement("UPDATE zona SET disponibile=false WHERE nome=?;");
                            nStats[13]++;
                            pst.setString(1,zone);
                            
                            pst.executeUpdate();
                            return;
                        }
                        return;
                    }
                    catch(SQLException e)
                    {
                        System.out.println(e);
                        infoSQLBox(e,"Errore");
                        System.exit(1);
                    }
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                System.exit(1);
            }
        }
        
        private boolean openInventory()
        {
            int c = 0;
            int ch = 0;
            ArrayList<String> h = new ArrayList<String>(1);
            ArrayList<Integer> q = new ArrayList<Integer>(1);
            ArrayList<String> t = new ArrayList<String>(1);
            ArrayList<Integer> pvr = new ArrayList<Integer>(1);
            ArrayList<Integer> pmr = new ArrayList<Integer>(1);
            ArrayList<String> d = new ArrayList<String>(1);
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT oggetto, quantità, tipo, pvriprist, pmriprist, danno "
                        + "FROM inventario AS i JOIN oggetto AS o "
                        + "ON i.oggetto = o.nome "
                        + "WHERE personaggio = '" + eroe.getName() + "';");
                nStats[13]++;
                mw.appendText("Oggetto\t\tQuantità");
                while(rs.next())
                {
                    c++;
                    h.add(rs.getString("oggetto"));
                    q.add(rs.getInt("quantità"));
                    t.add(rs.getString("tipo"));
                    pvr.add(rs.getInt("pvriprist"));
                    pmr.add(rs.getInt("pmriprist"));
                    d.add(rs.getString("danno"));
                    mw.appendText("\n" + c + " - " + rs.getString("oggetto"));
                    if((Integer) rs.getInt("pvriprist")!= 0)
                    {
                        mw.appendText(" Pv: " + rs.getInt("pvriprist"));
                    }
                    if((Integer) rs.getInt("pmriprist")!= 0)
                    {
                        mw.appendText(" Pm: " + rs.getInt("pmriprist"));
                    }
                    mw.appendText("\t\t" + rs.getInt("quantità"));
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
                return false;
            }
            mw.appendText("\n" + Integer.toString(c+1) + " - Esci\n");
            while(true)
            {
                ch = mw.intChoice(1,c+1);
                if(ch == c+1)
                {
                    mw.appendText("\n");
                    return false;
                }
                else
                {
                    if(t.get(ch-1).equals("Pozione"))
                    {
                        int done = 0;
                        
                        if(eroe.getHP() == 100 && pvr.get(ch-1) != 0)
                        {
                            mw.appendText("\nHai già il massimo degli hp!\n");
                        }
                        else if(pvr.get(ch-1) != 0)
                        {
                            if(pvr.get(ch-1) != 0) eroe.heal(pvr.get(ch-1));
                            done++;
                        }
                        
                        if(eroe.getMP() == 100 && pmr.get(ch-1) != 0)
                        {
                            mw.appendText("\nHai già il massimo degli mp!\n");
                        }
                        else if(pmr.get(ch-1) != 0)
                        {
                            if(pmr.get(ch-1) != 0) eroe.restoreMP(pmr.get(ch-1));
                            done++;
                        }
                        
                        if (done >= 1)
                        {
                            consume(h.get(ch-1));
                            mw.clearText();
                            return true;
                        }
                        return false; 
                    }
                    else if(t.get(ch-1).equals("Chiave"))
                    {
                        mw.appendText("\nNon puoi usare questo oggetto");
                    }
                    else if(t.get(ch-1).equals("Arma"))
                    {
                        eroe.equipWeapon(h.get(ch-1));
                        consume(h.get(ch-1));
                        mw.clearText();
                        return true;
                    }
                }
            }
        }
        
        private void consume(String obj)
        {
            int currQty = 0;
            try
            { 
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT quantità FROM inventario WHERE oggetto='" + obj + "' AND personaggio='" + eroe.getName() + "';");
                while(rs.next())
                {
                    currQty = rs.getInt("quantità");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                System.exit(1);
            }
            if(currQty == 1)
            {
                try
                {
                    Statement statement = conn.createStatement();
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("DELETE FROM inventario WHERE personaggio = '"+ eroe.getName() + "' AND oggetto = '" + obj + "';");   
                    nStats[14]++;
                    pst.executeUpdate();
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                    infoSQLBox(e,"Errore");
                    System.exit(1);
                }
            }
            else
            {
                try
                {
                    Statement statement = conn.createStatement();
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("UPDATE inventario SET quantità = " + Integer.toString(currQty-1) + " WHERE personaggio = '"+ eroe.getName() + "' AND oggetto = '" + obj + "';");                   
                    nStats[14]++;
                    pst.executeUpdate();
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                    infoSQLBox(e,"Errore");
                    System.exit(1);
                }
            }
        }
    }
    
    private class Hero
    {
        private int hp = 0;
        private int mp = 0;
        private int danno = 0;
        private int stat = 0;
        private int livello = 0;
        private int exp = 0;
        private String classe = null;
        private String nome = null;
        private String arma = null;
        private String zona = null;
        
        public Hero(String name)
        {
            this.setName(name);//20 MAX
            this.setLvl(getLvl());
            
            hp = this.getHP();
            mw.setHP(hp);
            
            mp = this.getMP();
            mw.setMP(mp);
            
            this.setExp(this.getExp());
            
            classe = this.getClasse();
            
            if(classe.toUpperCase().equals("GUERRIERO"))
            {
                mw.setStatName("Forza");
            }
            else
            {
                mw.setStatName("Intelligenza");
            }
            
            this.setStat(this.getStat());
            danno = this.getStat();
            arma = this.getWeapon();
            
            if(arma != null)
            {  
                 try
                {
                    Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT danno FROM oggetto WHERE nome = '" + arma + "';");
                    nStats[15]++;
                    while(rs.next())
                    {
                        danno += rs.getInt("danno");
                        mw.setWeaponDamage(rs.getInt("danno"));
                    }
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                }
                 mw.setWeapon(arma);
            }
            
            this.setZone(this.getZone());
        }
        
        public int getHP()
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT hp FROM personaggio WHERE nome = '" + nome + "';");
                nStats[16]++;
                while(rs.next())
                {
                    return Integer.parseInt(rs.getString("hp"));
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return -1;
        }
        
        public int getMP()
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT mp FROM personaggio WHERE nome = '" + nome + "';");
                nStats[17]++;
                while(rs.next())
                {
                    return Integer.parseInt(rs.getString("mp"));
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return -1;
        }
        
        public String getWeapon()
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT equipaggia FROM personaggio WHERE nome = '" + nome + "';");
                nStats[18]++;
                while(rs.next())
                {
                    return rs.getString("equipaggia");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return null;
        }
        
        public String getZone()
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT zona FROM personaggio WHERE nome = '" + nome + "';");
                nStats[20]++;
                while(rs.next())
                {
                    return rs.getString("zona");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return null;
        }
        
        public String getClasse() //getClasse per override con Object
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT classe FROM personaggio WHERE nome = '" + nome + "';");
                nStats[21]++;
                while(rs.next())
                {
                    return rs.getString("classe");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return null;
        }
        
        public int getLvl()
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT livello FROM personaggio WHERE nome = '" + nome + "';");
                nStats[22]++;
                while(rs.next())
                {
                    return rs.getInt("livello");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return -1;
        }
        
        public int getStat()
        {
            try
            {
                ResultSet rs = null;
                Statement statement = conn.createStatement();
                if(this.getClasse().toUpperCase().equals("GUERRIERO"))
                {
                    rs = statement.executeQuery("SELECT forza AS s FROM personaggio WHERE nome = '" + nome + "';");
                    nStats[23]++;
                }
                else
                {
                    rs = statement.executeQuery("SELECT Intelligenza AS s FROM personaggio WHERE nome = '" + nome + "';");
                    nStats[23]++;
                }
                
                while(rs.next())
                {
                    return rs.getInt("s");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return -1;
        }
        
        public String getName()
        {
            return nome;
        }
        
        public int getDanno()
        {
            return danno;
        }
        
        private int getExp()
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT exp FROM personaggio WHERE nome = '" + nome + "';");
                nStats[24]++;
                while(rs.next())
                {
                    return rs.getInt("exp");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return -1;
        }
        
        
        public void damage(int qty)
        {
            im.dmgAni();
            hp -= qty;
            if (hp<0) hp = 0;
            try
            {
                PreparedStatement pst = null;
                pst = conn.prepareStatement("UPDATE personaggio SET hp=? WHERE nome=?;");                   
                nStats[1]++;
                pst.setInt(1, hp);
                pst.setString(2, nome);
                        
                pst.executeUpdate();
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
            mw.setHP(hp);
            if(this.hp == 0)
            {
                g.gameOver();
            }
        }

        public void heal(int qty)
        {
            im.healAni();
            hp += qty;
            if (hp>100) hp = 100;
            try
            {
                PreparedStatement pst = null;
                pst = conn.prepareStatement("UPDATE personaggio SET hp=? WHERE nome=?;");
                nStats[1]++;
                
                pst.setInt(1, hp);
                pst.setString(2, nome);
                        
                pst.executeUpdate();
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
            mw.setHP(hp);
        }
        
        public void consumeMP(int qty)
        {
            mp -= qty;
            if (mp<0) mp = 0;
            try
            {
                Statement statement = conn.createStatement();
                PreparedStatement pst = null;
                pst = conn.prepareStatement("UPDATE personaggio SET mp=? WHERE nome=?;");  
                nStats[0]++;
                
                pst.setInt(1, mp);
                pst.setString(2, nome);
                        
                pst.executeUpdate();
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
            mw.setMP(mp);
        }
        
        public void restoreMP(int qty)
        {
            mp += qty;
            if (mp>100) mp = 100;
            try
            {
                PreparedStatement pst = null;
                pst = conn.prepareStatement("UPDATE personaggio SET mp=? WHERE nome=?;");
                nStats[0]++;
                
                pst.setInt(1, mp);
                pst.setString(2, nome);
                        
                pst.executeUpdate();
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
            mw.setMP(mp);
        }
        
        public void addItem(String itemName, int qty)
        {
            int q = 0;
            
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT quantità "
                        + "FROM inventario "
                        + "WHERE personaggio = '" + this.getName() + "' "
                        + "AND oggetto='" + itemName +"';");
                nStats[25]++;
                while(rs.next())
                {
                    q = rs.getInt("quantità");
                }
                if(q==0)
                {
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("INSERT INTO inventario(personaggio,oggetto,quantità) VALUES (?,?,?);");                   
                    nStats[26]++;
                    pst.setString(1, nome);
                    pst.setString(2, itemName);
                    pst.setInt(3,qty);

                    pst.executeUpdate();
                }
                else
                {
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("UPDATE inventario SET quantità=? WHERE personaggio=? AND oggetto=?;");                   
                    nStats[27]++;
                    pst.setInt(1, q+qty);
                    pst.setString(2, this.getName());
                    pst.setString(3,itemName);

                    pst.executeUpdate();
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
        }
        
        public void addItem(String itemName)
        {
            int q = 0;
            
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT quantità "
                        + "FROM inventario "
                        + "WHERE personaggio = '" + this.getName() + "' "
                        + "AND oggetto='" + itemName +"';");
                nStats[25]++;
                while(rs.next())
                {
                    q = rs.getInt("quantità");
                }
                if(q==0)
                {
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("INSERT INTO inventario(personaggio,oggetto,quantità) VALUES (?,?,?);");
                    nStats[26]++;

                    pst.setString(1, nome);
                    pst.setString(2, itemName);
                    pst.setInt(3,1);

                    pst.executeUpdate();
                }
                else
                {
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("UPDATE inventario SET quantità=? WHERE personaggio=? AND oggetto=?;");
                    nStats[27]++;

                    pst.setInt(1, q+1);
                    pst.setString(2, this.getName());
                    pst.setString(3,itemName);

                    pst.executeUpdate();
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
        }
        
        private void setName(String name)
        {
            nome = name;
            mw.setName(name);
        }
        
        private void setStat(int to)
        {
            stat = to;
            if(arma != null)
            {  
                try
                {
                    Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT danno FROM oggetto WHERE nome = '" + arma + "';");
                    nStats[15]++;
                    while(rs.next())
                    {
                        danno = stat + rs.getInt("danno");
                        mw.setWeaponDamage(rs.getInt("danno"));
                    }
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                }
                mw.setWeapon(arma);
            }
            mw.setStat(to);
        }
        
        private void setLvl(int to)
        {
            livello = to;
            mw.setLvl(to);
        }
        
        private void setExp(int to)
        {
            exp = to;
            mw.setExp(to, false);
        }
        
        public void addExp(int qty)
        {
            int currExp = 0;
            int currLiv = 0;
            int oQty = 0;
            ArrayList<Integer> exp = new ArrayList<Integer>(1);
            mw.appendText("\nHai guadagnato " + qty + " Punti esperienza!");
            
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT livello, exp "
                        + "FROM personaggio "
                        + "WHERE nome = '" + nome + "';");
                nStats[22]++;
                nStats[24]++;
                while(rs.next())
                {
                    currExp = rs.getInt("exp");
                    currLiv = rs.getInt("Livello");
                }
                if(currLiv == 5) return;
                Statement statement2 = conn.createStatement();
                ResultSet rs2 = statement2.executeQuery("SELECT exp "
                        + "FROM livello;");
                nStats[28]++;
                while(rs2.next())
                {
                    exp.add(rs2.getInt("exp"));
                }
                
                oQty = qty + currExp;
                
                while(currLiv < 5 && currExp + qty >= exp.get(currLiv-1))
                {
                    currExp += exp.get(currLiv-1);
                    qty -= exp.get(currLiv-1);
                    mw.setExp(exp.get(currLiv-1),true);
                    mw.appendText("\nSei salito di un livello!");
                    levelUp(++currLiv);
                }
                
                if(currLiv == 5) oQty = 500;
                mw.setExp(oQty,true);
                PreparedStatement pst = null;
                pst = conn.prepareStatement("UPDATE personaggio SET exp=? WHERE nome=?");
                nStats[29]++;

                pst.setInt(1, oQty);
                pst.setString(2, nome);

                pst.executeUpdate();
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
        }
        
        private void levelUp(int to)
        {
            try
            {
                PreparedStatement pst = null;
                pst = conn.prepareStatement("UPDATE personaggio SET livello=? WHERE nome=?;");  
                nStats[30]++;
                
                pst.setInt(1,to);
                pst.setString(2, nome);
                        
                pst.executeUpdate();
                
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
            this.setLvl(to);
            try
            {
                PreparedStatement pst = null;
                if(getClasse().toUpperCase().equals("GUERRIERO"))
                {
                    pst = conn.prepareStatement("UPDATE personaggio SET forza=? WHERE nome=?;");
                    nStats[31]++;
                }
                else if(getClasse().toUpperCase().equals("MAGO"))
                {
                    pst = conn.prepareStatement("UPDATE personaggio SET intelligenza=? WHERE nome=?;");
                    nStats[31]++;
                }
                
                pst.setInt(1,stat+2);
                pst.setString(2, nome);
                
                pst.executeUpdate();
                
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = null;
                if(this.getClasse().toUpperCase().equals("GUERRIERO"))
                {
                    rs=statement.executeQuery("SELECT nome,livello FROM abilità WHERE tipo='Attacco';");
                    nStats[5]++;
                }
                else if(this.getClasse().toUpperCase().equals("MAGO"))
                {
                    rs=statement.executeQuery("SELECT nome,livello FROM abilità WHERE tipo='Magia';");
                    nStats[5]++;
                }
                while(rs.next())
                {
                    if(rs.getInt("livello") == to)
                    {
                        PreparedStatement pst = conn.prepareStatement("INSERT INTO apprende(personaggio,abilità) "
                                + "VALUES (?,?)");
                        nStats[6]++;
                        pst.setString(1,this.getName());
                        pst.setString(2, rs.getString("nome"));

                        pst.executeUpdate();
                    }
                }
                
                
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
            this.setStat(stat+2);
            mw.setLvl(to);
        }
        
        private void setZone(String name)
        {
            try
            {
                PreparedStatement pst = null;
                pst = conn.prepareStatement("UPDATE personaggio SET zona=? WHERE nome=?;");                   
                nStats[32]++;
                pst.setString(1,name);
                pst.setString(2, this.getName());

                pst.executeUpdate();
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
            zona = name;
            mw.setZone(name);
        }
        
        public void equipWeapon(String name)
        {
            if(this.arma == null)
            {
                arma = name;
                try
                {
                    Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT danno FROM oggetto WHERE nome = '" + arma + "';");
                    nStats[15]++;
                    while(rs.next())
                    {
                        mw.setWeaponDamage(rs.getInt("danno"));
                        danno += rs.getInt("danno");
                    }
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("UPDATE personaggio SET equipaggia=? WHERE nome=?;");
                    nStats[33]++;

                    pst.setString(1, arma);
                    pst.setString(2, this.getName());

                    pst.executeUpdate();
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                    infoSQLBox(e,"Errore");
                    System.exit(1);
                }
                mw.setWeapon(name);
            }
            else
            {
                try
                {
                    Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT danno FROM oggetto WHERE nome = '" + arma + "';");
                    nStats[15]++;
                    while(rs.next())
                    {
                        danno -= rs.getInt("danno");
                    }
                    this.addItem(arma);
                    arma=name;
                    rs = statement.executeQuery("SELECT danno FROM oggetto WHERE nome = '" + arma + "';");
                    nStats[15]++;
                    while(rs.next())
                    {
                        mw.setWeaponDamage(rs.getInt("danno"));
                        danno += rs.getInt("danno");
                    }
                    PreparedStatement pst = null;
                    pst = conn.prepareStatement("UPDATE personaggio SET equipaggia=? WHERE nome=?;");
                    nStats[33]++;

                    pst.setString(1, arma);
                    pst.setString(2, this.getName());

                    pst.executeUpdate();
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                    infoSQLBox(e,"Errore");
                    System.exit(1);
                }
                mw.setWeapon(name);
            }
        }
    }
    
    private class Enemy
    {
        private int hp = 0;
        private int livForza = 0;
        private int expGain = 0;
        private String razza = null;
        private String attacco = null;
        private String nome = null;
        
        public Enemy(String name)
        {
            this.setName(name);
            this.setHP(this.getHP());
            this.setRazza(this.getRazza());
            this.setForza(this.getDBForza());
            expGain = livForza*4;
        }
        
        public int getHP()
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT hp FROM mostro WHERE nome = '" + nome + "';");
                nStats[34]++;
                while(rs.next())
                {
                    return Integer.parseInt(rs.getString("hp"));
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return -1;
        }
        
        public String getRazza()
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT razza FROM mostro WHERE nome = '" + nome + "';");
                nStats[35]++;
                while(rs.next())
                {
                    return rs.getString("razza");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return null;
        }
        
        public int getExp()
        {
            return this.expGain;
        }
        
        public int getForza()
        {
            return livForza;
        }
        
        private int getDBForza()
        {
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT danno FROM razza WHERE nomerazza = '" + razza + "';");
                nStats[36]++;
                while(rs.next())
                {
                    return Integer.parseInt(rs.getString("danno"));
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
            }
            return -1;
        }
        
        public String getName()
        {
            return this.nome;
        }
        
        public int getCurrentHP()
        {
            return this.hp;
        }
        
        public String getAttacco()
        {
            return this.attacco;
        }
        
        public void damage(int qty)
        {
            this.hp -= qty;
            if (this.hp<0) this.hp = 0;
            mw.setEnemyHP(hp);
        }
        
        private void setName(String name)
        {
            nome = name;
            mw.setEnemyName(name);
        }
        
        private void setHP(int to)
        {
            hp = to;
            mw.setEnemyHP(to);
        }
        
        private void setRazza(String raz)
        {
            razza = raz;
            try
            {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT nomeattacco "
                                                    + "FROM mostro AS m JOIN razza AS r "
                                                    + "ON m.razza = r.nomerazza "
                                                    + "WHERE nome = '" + this.getName() + "';");
                nStats[37]++;
                while(rs.next())
                {
                    attacco = rs.getString("nomeattacco");
                }
            }
            catch(SQLException e)
            {
                System.out.println(e);
                infoSQLBox(e,"Errore");
                    System.exit(1);
            }
            mw.setEnemyRazza(raz);
        }
        
        private void setForza(int to)
        {
            livForza = to;
            mw.setEnemyForza(to);
        }
    }
    
    private class Refresh extends Thread
    {
        private Timer t = null;
        private ArrayList<String> n = null;
        int rand = 0;
        public Refresh()
        {
            n = new ArrayList<String>(1);
            t = new Timer();
            t.scheduleAtFixedRate(new TimerTask()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Statement statement = conn.createStatement();
                        ResultSet rs = statement.executeQuery("SELECT nome FROM zona WHERE disponibile = false");
                        while(rs.next())
                        {
                            n.add(rs.getString("nome"));
                        }
                        if(n.size() > 0)
                        {
                            rand = randInt(0,n.size()-1);
                            PreparedStatement pst = null;
                            pst = conn.prepareStatement("UPDATE zona SET disponibile=? WHERE nome=?;");                   
                            pst.setBoolean(1, true);
                            pst.setString(2, n.get(rand));

                            pst.executeUpdate();
                        }
                        
                    }
                    catch(SQLException e)
                    {
                        System.out.println(e);
                        infoSQLBox(e,"Errore");
                        System.exit(1);
                    }
                }
            },10*1000,10*1000);
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        ProgettoDB prog = new ProgettoDB();
        g =  prog.new Game();
        g.start();
    } 
    
    private int randInt(int min, int max) 
    {
        Random rand = new Random();
        if(max<0) max=0;
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    public static void infoSQLBox(SQLException infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
