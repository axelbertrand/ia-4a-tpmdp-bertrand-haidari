package agent.rlagent;

import environnement.Action;
import environnement.Environnement;
import environnement.Etat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Renvoi 0 pour valeurs initiales de Q
 *
 * @author laetitiamatignon
 */
public class QLearningAgent extends RLAgent {
    /**
     * format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
     */
    protected HashMap<Etat, HashMap<Action, Double>> qvaleurs;

    //AU CHOIX: vous pouvez utiliser une Map avec des Pair pour clés si vous préférez
    //protected HashMap<Pair<Etat, Action>, Double> qvaleurs;

    /**
     * @param alpha
     * @param gamma
     * @param _env
     */
    public QLearningAgent(double alpha, double gamma, Environnement _env) {
        super(alpha, gamma, _env);
        qvaleurs = new HashMap<>();
    }

    /**
     * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e
     * (plusieurs actions sont renvoyees si valeurs identiques)
     * renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)
     */
    @Override
    public List<Action> getPolitique(Etat e) {
        // retourne action de meilleures valeurs dans e selon Q : utiliser getQValeur()
        // retourne liste vide si aucune action legale (etat terminal)
        List<Action> returnactions = new ArrayList<>();
        List<Action> actionsLegales = this.getActionsLegales(e);
        if (actionsLegales.isEmpty()) {//etat  absorbant; impossible de le verifier via environnement
            System.out.println("aucune action legale");
            return returnactions;
        }

        //*** VOTRE CODE

        double maxValeur = Double.NEGATIVE_INFINITY;
        for (Action a : actionsLegales) {
            double valeur = this.getQValeur(e, a);
            if (valeur > maxValeur) {
                returnactions.clear();
                returnactions.add(a);
                maxValeur = valeur;
            } else if (valeur == maxValeur) {
                returnactions.add(a);
            }
        }

        return returnactions;
    }

    @Override
    public double getValeur(Etat e) {
        //*** VOTRE CODE

        double max = Double.NEGATIVE_INFINITY;
        try {
            for (Action a : qvaleurs.get(e).keySet()) {
                max = Math.max(max, this.getQValeur(e, a));
            }
        }
        catch(Exception ex) {
            return 0.;
        }

        return max;
    }

    @Override
    public double getQValeur(Etat e, Action a) {
        //*** VOTRE CODE

        try {
            return this.qvaleurs.get(e).get(a);
        }
        catch(Exception ex) {
            return 0.;
        }
    }


    @Override
    public void setQValeur(Etat e, Action a, double d) {
        //*** VOTRE CODE

        this.qvaleurs.computeIfAbsent(e, k -> new HashMap<>());
        this.qvaleurs.get(e).put(a, d);

        // mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur de max pour tout s de V
        //vmin est la valeur de min pour tout s de V
        // ...

        for (Etat etat : this.qvaleurs.keySet()) {
            double valeur = this.getValeur(etat);
            this.vmax = Math.max(this.vmax, valeur);
            this.vmin = Math.min(this.vmin, valeur);
        }

        this.notifyObs();
    }


    /**
     * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
     * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
     *
     * @param e
     * @param a
     * @param esuivant
     * @param reward
     */
    @Override
    public void endStep(Etat e, Action a, Etat esuivant, double reward) {
        if (RLAgent.DISPRL)
            System.out.println("QL mise a jour etat " + e + " action " + a + " etat' " + esuivant + " r " + reward);

        //*** VOTRE CODE

        double newEstim = (1 - this.alpha) * this.getQValeur(e, a) + this.alpha * (reward + this.gamma * this.getValeur(esuivant));
        this.setQValeur(e, a, newEstim);
    }

    @Override
    public Action getAction(Etat e) {
        this.actionChoisie = this.stratExplorationCourante.getAction(e);
        return this.actionChoisie;
    }

    @Override
    public void reset() {
        super.reset();
        //*** VOTRE CODE

        for (Etat e : this.qvaleurs.keySet()) {
            for (Action a : this.qvaleurs.get(e).keySet()) {
                this.setQValeur(e, a, 0.);
            }
        }

        this.episodeNb = 0;
        this.notifyObs();
    }


}
