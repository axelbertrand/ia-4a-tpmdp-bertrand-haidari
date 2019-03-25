package agent.planningagent;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import environnement.MDP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration
 * et choisit ses actions selon la politique calculee.
 *
 * @author laetitiamatignon
 */
public class ValueIterationAgent extends PlanningValueAgent {
    /**
     * discount facteur
     */
    protected double gamma;

    /**
     * fonction de valeur des etats
     */
    protected HashMap<Etat, Double> V;

    /**
     * @param gamma
     * @param mdp
     */
    public ValueIterationAgent(double gamma, MDP mdp) {
        super(mdp);
        this.gamma = gamma;

        this.V = new HashMap<Etat, Double>();
        for (Etat etat : this.mdp.getEtatsAccessibles()) {
            V.put(etat, 0.0);
        }
    }


    public ValueIterationAgent(MDP mdp) {
        this(0.9, mdp);

    }

    /**
     * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
     * et notifie ses observateurs.
     * Ce n'est pas la version inplace (qui utilise la nouvelle valeur de V pour mettre a jour ...)
     */
    @Override
    public void updateV() {
        //delta est utilise pour detecter la convergence de l'algorithme
        //Dans la classe mere, lorsque l'on planifie jusqu'a convergence, on arrete les iterations
        //lorsque delta < epsilon
        //Dans cette classe, il  faut juste mettre a jour delta
        //*** VOTRE CODE

        double deltaMax = Double.NEGATIVE_INFINITY;
        HashMap<Etat, Double> VTemp = new HashMap<>();
        for (Etat e : this.mdp.getEtatsAccessibles()) {
            if (this.mdp.estAbsorbant(e)) {
                VTemp.put(e, 0.);
                continue;
            }

            double vmax = Double.NEGATIVE_INFINITY;

            for (Action action : this.mdp.getActionsPossibles(e)) {
                try {
                    double somme = 0.;
                    Map<Etat, Double> TMap = this.mdp.getEtatTransitionProba(e, action);

                    for (Etat e2 : TMap.keySet()) {
                        somme += TMap.get(e2) * (this.mdp.getRecompense(e, action, e2) + this.gamma * this.getValeur(e2));
                    }
                    vmax = Math.max(vmax, somme);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            deltaMax = Math.max(deltaMax, Math.abs(this.getValeur(e) - vmax));
            VTemp.put(e, vmax);
        }

        this.delta = deltaMax;
        this.V = VTemp;

        //mise a jour de vmax et vmin utilise pour affichage du gradient de couleur:
        //vmax est la valeur max de V pour tout s
        //vmin est la valeur min de V pour tout s
        // ...

        for (Etat e : V.keySet()) {
            this.vmin = Math.min(this.getValeur(e), this.vmin);
            this.vmax = Math.max(this.getValeur(e), this.vmax);
        }

        //******************* laisser cette notification a la fin de la methode
        this.notifyObs();
    }


    /**
     * renvoi l'action executee par l'agent dans l'etat e
     * Si aucune actions possibles, renvoi Action2D.NONE
     */
    @Override
    public Action getAction(Etat e) {
        //*** VOTRE CODE
        List<Action> actions = this.getPolitique(e);
        if (actions.size() > 0) {
            return actions.get(0);
        }

        return Action2D.NONE;
    }


    @Override
    public double getValeur(Etat _e) {
        //Renvoie la valeur de l'Etat _e, c'est juste un getter, ne calcule pas la valeur ici
        //(la valeur est calculee dans updateV)
        //*** VOTRE CODE

        return this.V.get(_e);
    }

    /**
     * renvoi action(s) de plus forte(s) valeur(s) dans etat
     * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
     */
    @Override
    public List<Action> getPolitique(Etat _e) {
        //*** VOTRE CODE

        // retourne action de meilleure valeur dans _e selon V,
        // retourne liste vide si aucune action legale (etat absorbant)
        List<Action> returnactions = new ArrayList<Action>();
        if (this.mdp.estAbsorbant(_e)) {
            return returnactions;
        }

        double sommeMax = Double.NEGATIVE_INFINITY;
        for (Action action : this.mdp.getActionsPossibles(_e)) {
            try {
                double somme = 0.;
                Map<Etat, Double> TMap = this.mdp.getEtatTransitionProba(_e, action);
                for (Etat e2 : TMap.keySet()) {
                    somme += TMap.get(e2) * (this.mdp.getRecompense(_e, action, e2) + this.gamma * this.getValeur(e2));
                }
                if (somme > sommeMax) {
                    returnactions.clear();
                    returnactions.add(action);
                    sommeMax = somme;
                } else if (somme == sommeMax) {
                    returnactions.add(action);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return returnactions;
    }

    @Override
    public void reset() {
        super.reset();
        //reinitialise les valeurs de V
        //*** VOTRE CODE

        for (Etat e : this.V.keySet()) {
            this.V.put(e, 0.);
        }

        this.notifyObs();
    }


    public HashMap<Etat, Double> getV() {
        return V;
    }

    public double getGamma() {
        return gamma;
    }

    @Override
    public void setGamma(double _g) {
        System.out.println("gamma= " + gamma);
        this.gamma = _g;
    }


}