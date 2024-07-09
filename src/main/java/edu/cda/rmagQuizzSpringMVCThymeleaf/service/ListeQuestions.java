package edu.cda.rmagQuizzSpringMVCThymeleaf.service;

import edu.cda.rmagQuizzSpringMVCThymeleaf.model.JsonEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class ListeQuestions {

    // Définition de la source de notre appel API:
    @Value("${post.placehorlder.url}")
    private String path;

    public JsonEntryPoint retreiveList(){

        /*Appel de l'API rest de la source choisie, puis traitement des objets
        Json reçus en réponse que l'on injecte dans le modèle correspondant*/
        RestClient restClient = RestClient.builder()
                .baseUrl(path)
                .defaultHeader("Content-Type", "application/json")
                .build();

        JsonEntryPoint jsonEntryPoint = restClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

    return jsonEntryPoint;
}
    }