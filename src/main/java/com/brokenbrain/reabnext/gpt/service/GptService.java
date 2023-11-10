package com.brokenbrain.reabnext.gpt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Data
public class GptService {

    // - Setando parâmetros de entrada para o input na API da Open AI -
    private static String KEY = "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";
    private String PROMPT = "";
    private long MAX_TOKENS = 1000;
    private float TEMPERATURE = 1;
    private String MODEL = "gpt-3.5-turbo-instruct"; // "text-davinci-003";

    // - outputGptMap é o mapeamento do Json do Output da API da OpenAI
    private Map<String, Object> outputGptMap = new LinkedHashMap<>();

    // - gerarTreino é o método que integra a API da Open AI
    // - fazendo o trabalho de passar o input e receber/mapear o output
    public Map<String, Object> gerarTreino() {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost
                    ( "https://api.openai.com/v1/completions" );

            StringEntity inputGptEntity = new StringEntity
                    ( "{" +
                            "\"model\" : " +
                            "\"" +
                            MODEL +
                            "\"," +
                            "\"prompt\" : \"" +
                            this.getPROMPT() +
                            "\"," +
                            "\"max_tokens\" : " +
                            MAX_TOKENS +
                            "," +
                            "\"temperature\" : " +
                            TEMPERATURE +
                            "}" );

            System.out.println( "Input Gpt: " + inputGptEntity );

            // - Setando parâmetros para efetuar a requisição HTTP na API da OpenAI
            inputGptEntity.setContentType( "application/json" );
            post.setHeader( "Content-Type", "application/json" );
            post.setHeader( "Authorization", "Bearer " + KEY );
            post.setEntity( inputGptEntity );

            // - Executando a requisição POST na API da OpenAI
            HttpResponse response = client.execute( post );

            // - if para verificar se a requisição http foi executada com sucesso
            if (response.getStatusLine().getStatusCode() != 201 && response.getStatusLine().getStatusCode() != 200) {
                System.out.println( "HTTP Status Code: " + response.getStatusLine().getStatusCode() );
            } else {
                // - Lendo o output da API da OpenAI
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader( response.getEntity().getContent() ) );

                // - Mapeando o output da API da OpenAI
                ObjectMapper mapper = new ObjectMapper();
                outputGptMap = mapper.readValue( rd, new TypeReference<Map<String, Object>>() {
                } );
            }

        } catch (Exception exception) {
            System.out.println( exception.getMessage() );
        }
        return outputGptMap;
    }

}