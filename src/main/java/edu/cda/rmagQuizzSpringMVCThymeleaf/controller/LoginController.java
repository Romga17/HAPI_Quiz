package edu.cda.rmagQuizzSpringMVCThymeleaf.controller;

import edu.cda.rmagQuizzSpringMVCThymeleaf.model.PerformanceJoueur;
import edu.cda.rmagQuizzSpringMVCThymeleaf.model.JsonEntryPoint;
import edu.cda.rmagQuizzSpringMVCThymeleaf.model.Question;
import edu.cda.rmagQuizzSpringMVCThymeleaf.model.Timer;
import edu.cda.rmagQuizzSpringMVCThymeleaf.service.ListeQuestions;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Controller
@SessionAttributes({"pseudo", "index","tailleListe","score","questionlogin","listeQuestions", "reponsesQuestionInit", "repQuestionInit", "scoreIndex", "operator", "allQuestionsOverview", "allReponsesOverview","listeRepUser","tabscore"})
public class LoginController {

    @Autowired
    ListeQuestions listequest;

    private Timer timer;

    ArrayList<String> listeReponsesUtilisateur = new ArrayList<>();

    //Instanciation FinalScore et création du tableau:
    ArrayList<PerformanceJoueur> newPerf = new ArrayList<>();

    @GetMapping("/connexion")
    public String afficherMonAccueil() {
        return "index";
    }


    @GetMapping("/login")
    public String validerSonLogin(HttpSession session, Model model ,@RequestParam String pseudo) {
        //Purge de la liste de réponses de l'utilisateur entre chaque parties:
        listeReponsesUtilisateur.clear();

        //Instanciation d'un timer et top départ:
        timer = new Timer();
        timer.start();

        //Déclaration et initialisation d'un index, et on le passe à l'objet model:
        int index = 0;
        model.addAttribute("index", index);

        /*Ici on récupère le pseudo entré par l'utilisateur via le paramètre @RequestParam:*/
        model.addAttribute("pseudo", pseudo);

        //Déclaration et initialisation d'un score associé au joueur:
        int playerScore = 0;
        model.addAttribute("score", playerScore);

        //Initialisation et déclaration de l'index du score:
        int scoreIndex = 0;
        model.addAttribute("scoreIndex", scoreIndex);

        /*On instancie le JsonEntryPoint qui nous permet de manipuler dans le controller le Json
        reçu en retour de l'appel de l'API rest:*/
        JsonEntryPoint operator = listequest.retreiveList();
        model.addAttribute("operator", operator);

        //On commence par aller chercher l'information sur la taille du quizz:
        int tailleListe = listequest.retreiveList().getQuizzes().size();
        model.addAttribute("tailleListe", tailleListe);

        //On récupère la première question et on l'ajoute dans un attribut séparé au niveau du model,
        String questionEnCours = operator.getQuizzes().get(index).getQuestion();
        model.addAttribute("questionlogin",questionEnCours);

        //On déclare une liste qui servira à reccueillir l'ensemble des questions:
        ArrayList<String> allQuestions = new ArrayList<>();

        //Code utile à des informations de result.html____________________________________________________
        ArrayList<String> allQuestionsOverview = new ArrayList<>();
        for(Question question: operator.getQuizzes()){
            allQuestionsOverview.add(question.getQuestion());
        }
        model.addAttribute("allQuestionsOverview", allQuestionsOverview);

        ArrayList<String> allReponsesOverview = new ArrayList<>();
        for(Question question: operator.getQuizzes()){
            allReponsesOverview.add(question.getAnswer());
        }
        model.addAttribute("allReponsesOverview", allReponsesOverview);
        //__________________________________________________________________________________________


        //On ajoute la première question chargée à la liste complète de questions
        //et on l'envoi dans un attribut correspondant dans le model
        allQuestions.add(questionEnCours);
        model.addAttribute("listeQuestions", allQuestions);


        //Création d'une liste de réponses correspondant au tour en cours:
        ArrayList<String> reponsesQuestionsEnCours = new ArrayList<>();
        //On commence par ajouter les mauvaises réponses précisées dans l'objet question de l'index 0:
        for(String badAnswers : operator.getQuizzes().get(index).getBadAnswers()) {
            reponsesQuestionsEnCours.add(badAnswers);
        }
        //Ajout de la bonne réponse dans la même liste de réponses:
        String BonneReponseQuestionInit = operator.getQuizzes().get(index).getAnswer();
        reponsesQuestionsEnCours.add(BonneReponseQuestionInit);
        //On mélange les réponses et on envoi dans le model qui servira à afficher toutes les réponses:
        Collections.shuffle(reponsesQuestionsEnCours);
        model.addAttribute("reponsesQuestions",reponsesQuestionsEnCours);

        //Envoi dans le modèle de la bonne réponse du tour dans un attribut séparé:
        model.addAttribute("repQuestionInit",BonneReponseQuestionInit);

        return "quizz";
    }


    @RequestMapping(value = "/quizz", method = RequestMethod.POST)
        public String progressionQuestions(Model model, HttpSession session, @RequestParam String repuser) {

        model.addAttribute("timer", timer);

        if (repuser != null) {

            listeReponsesUtilisateur.add(repuser);
            model.addAttribute("listeRepUser", listeReponsesUtilisateur);
        }

        //Initialisation et transfert du score du joueur depuis le controller login:
        int scoremaj = (int) model.getAttribute("score");

        //Initialisation et transfert de l'index du controller login qui servira à manipuler les données:
        int manipulind = (int) model.getAttribute("index");

        JsonEntryPoint operatorProgression =(JsonEntryPoint) model.getAttribute("operator");

        String bonneReponseQuestionAvInc = operatorProgression.getQuizzes().get(manipulind).getAnswer();


        model.addAttribute("bonneRepQuestionAvInc", bonneReponseQuestionAvInc );


            //Incrémentation de l'index à chaque tour de jeu tant que l'on atteint pas le nombre de questions existantes:
            if (manipulind < (int) session.getAttribute("tailleListe")) {
                manipulind += 1;
                model.addAttribute("index", manipulind);
            }

            //retrait < à taille du Quizz pour remplacer par l'attribut:
            if(manipulind < (int) session.getAttribute("tailleListe")){
                String questionEnCours = operatorProgression.getQuizzes().get(manipulind).getQuestion();
                model.addAttribute("questionencours", questionEnCours);


                ArrayList<String> reponsesQuestionsEnCours = new ArrayList<>();
                for (String badAnswers : operatorProgression.getQuizzes().get(manipulind).getBadAnswers()) {
                    reponsesQuestionsEnCours.add(badAnswers);
                }

                String maBonneReponseQuestion = operatorProgression.getQuizzes().get(manipulind).getAnswer();
                reponsesQuestionsEnCours.add(maBonneReponseQuestion);
                model.addAttribute("bonneRepQuestion", maBonneReponseQuestion);

                Collections.shuffle(reponsesQuestionsEnCours);
                model.addAttribute("reponsesQuestion", reponsesQuestionsEnCours);

                ArrayList<String> listeReponsesOK = new ArrayList<>();
                listeReponsesOK.add(maBonneReponseQuestion);
            }
        //Calcul du score en fonction de l'index actuel qui permet la prise en compte du bon attribut:
        if ((int)model.getAttribute("scoreIndex") == 0) {

            String repOk = (String) model.getAttribute("repQuestionInit");

            if (repOk.equals(repuser)) {
                scoremaj += 1;
                model.addAttribute("score", scoremaj);
            }
        }

        if ((int)model.getAttribute("scoreIndex") > 0) {
            String repOkAp = (String)model.getAttribute("bonneRepQuestionAvInc");

            if (repOkAp.equals(repuser)) {
                scoremaj += 1;
                model.addAttribute("score", scoremaj);
            }
        }

            //Quand le nombre de question est atteint on renvoi cette fois la vue de la page de résultats:
            if (manipulind == (int) session.getAttribute("tailleListe")) {

                long tempsJoueur = timer.getTimeElapsed();
                model.addAttribute("tempsJoueur");

                //Gestion du scoreboard:
                PerformanceJoueur challenger = new PerformanceJoueur();
                challenger.setPseudo((String)model.getAttribute("pseudo"));
                challenger.setScorefinal((int)model.getAttribute("score"));

                //Gestion tableau des scores:
                if(newPerf.size()<5){
                    newPerf.add(challenger);
                }

                if(newPerf.size()==5){
                    for(PerformanceJoueur perf:newPerf)
                        if(perf.getScorefinal()<challenger.getScorefinal()){
                            perf.setPseudo(challenger.getPseudo());
                            perf.setScorefinal(challenger.getScorefinal());
                            newPerf.add(perf);
                        }
                }

                model.addAttribute("tabscore", newPerf);

                return "results";
            }

            int majScoreIndex = (int)model.getAttribute("scoreIndex");
            majScoreIndex +=1;
            model.addAttribute("scoreIndex",majScoreIndex);

            return "quizz";
        }

    @GetMapping("/scoreboard")
    public String afficherScoreboard() {
        return "scoreboard";
    }
    }

