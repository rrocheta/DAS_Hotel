package BLL;

import Model.EstacionamentoAPI.Estacionamento;
import Model.EstacionamentoAPI.ResponseTicket;
import Model.EstacionamentoAPI.Ticket;
import Model.EstacionamentoAPI.TicketInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EstacionamentoBLL {

    private static final String parkAPIURL = "https://services.inapa.com/parking4hotel/api/";
    private static final String apiID = "FG4";
    private static final String apiSecret = "4#hpGg)A6(";
    private final HttpClient httpClient;


    /**
     * Construtor da classe EstacionamentoBLL.
     * Inicializa um cliente HTTP com um tempo limite de conexão de 30 segundos.
     */
    public EstacionamentoBLL() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }

    /**
     * Método para obter informações sobre os lugares disponíveis no estacionamento.
     * Realiza uma chamada à API de estacionamento para obter informações sobre os lugares disponíveis.
     * Se a chamada for bem sucedida, o resultado é convertido em um objeto do tipo Estacionamento usando a biblioteca ObjectMapper.
     * Se a chamada falhar ou o resultado estiver vazio, o método retorna null.
     * Em caso de erro durante a conversão do resultado para o objeto Estacionamento, é lançada uma exceção.
     *
     * @return um objeto do tipo Estacionamento com informações sobre os lugares disponíveis, ou null se a chamada falhar ou o resultado estiver vazio.
     * @throws RuntimeException em caso de erro durante a conversão do resultado para o objeto Estacionamento.
     */
    public Estacionamento GetLugares() {
        try {

            String result = GETRequestParkingAPI("park");

            if (result.isEmpty())
                return null;

            ObjectMapper mapper = new ObjectMapper();
            Estacionamento teste = null;
            teste = mapper.readValue(result, Estacionamento.class);

            return teste;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para obter informações sobre os bilhetes criados no estacionamento.
     * Realiza uma chamada à API de estacionamento para obter informações sobre os bilhetes criados.
     * Se a chamada for bem sucedida, o resultado é convertido em um objeto do tipo Ticket usando a biblioteca ObjectMapper.
     * Se a chamada falhar ou o resultado estiver vazio, o método retorna null.
     * Em caso de erro durante a conversão do resultado para o objeto Ticket, é lançada uma exceção.
     *
     * @return um objeto do tipo Ticket com informações sobre os bilhetes criados, ou null se a chamada falhar ou o resultado estiver vazio.
     * @throws RuntimeException em caso de erro durante a conversão do resultado para o objeto Ticket.
     */
    public Ticket GetTicketsCriados() {
        try {

            String result = GETRequestParkingAPI("ticket");

            if (result.isEmpty())
                return null;

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(result, Ticket.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método privado para codificar as credenciais da API.
     * Recebe como parâmetros o ID e a chave secreta da API e retorna a codificação em Base64 dessas informações.
     * A codificação é realizada convertendo a string no formato "{ID}:{secret}" para bytes e aplicando a codificação Base64.
     *
     * @param id     o ID da API.
     * @param secret a chave secreta da API.
     * @return a codificação em Base64 das credenciais da API.
     */
    private String encondeAPICredentials(String id, String secret) {
        return Base64.getEncoder().encodeToString((id + ":" + secret).getBytes());
    }

    /**
     * Método privado para realizar uma requisição GET à API de estacionamento.
     * Recebe como parâmetro o endpoint da API e realiza uma chamada HTTP GET à API, incluindo as credenciais codificadas na requisição.
     * A resposta da API é retornada como uma string.
     * Em caso de erro, uma exceção é lançada.
     *
     * @param endpoint o endpoint da API para a qual a requisição será realizada.
     * @return a resposta da API como uma string.
     * @throws RuntimeException em caso de erro durante a requisição à API.
     */
    private String GETRequestParkingAPI(String endpoint) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .setHeader("Authorization", "Basic " + encondeAPICredentials(apiID, apiSecret))
                    .uri(URI.create(parkAPIURL + endpoint))
                    .build();

            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            return response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtém um ticket de estacionamento a partir de seu ID.
     * Faz uma chamada a API de estacionamento usando o método GETRequestParkingAPI
     * e fornece o endpoint "ticket/{id}". O resultado é convertido em um objeto Ticket
     * usando a biblioteca ObjectMapper.
     *
     * @param id ID do ticket de estacionamento a ser obtido.
     * @return O objeto Ticket correspondente ao ID fornecido. Retorna nulo se o resultado da chamada API estiver vazio.
     */
    public Ticket GETParkingTicket(String id) {
        try {
            String result = GETRequestParkingAPI("ticket/" + id);
            if (result.isEmpty())
                return null;

            ObjectMapper mapper = new ObjectMapper();
            Ticket teste = null;
            teste = mapper.readValue(result, Ticket.class);

            return teste;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que cria uma nova reserva no estacionamento a partir das informações fornecidas.
     *
     * @param body Informações da reserva a ser criada.
     * @return Resposta da API sobre a criação da reserva.
     * @throws RuntimeException em caso de falha ao processar ou enviar a requisição.
     */
    public ResponseTicket POSTCreateParkingReservation(TicketInfo body) {
        try {
            var objectMapper = new ObjectMapper();

            String requestBody = objectMapper
                    .writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Authorization", "Basic " + encondeAPICredentials(apiID, apiSecret))
                    .uri(URI.create(parkAPIURL + "ticket"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            String parsedResponse = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

            return objectMapper.readValue(parsedResponse, ResponseTicket.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que permite atualizar uma reserva de estacionamento existente.
     *
     * @param body     Informações da reserva a ser atualizada.
     * @param ticketID ID do ticket da reserva existente.
     * @return Objeto {@link ResponseTicket} com informações da atualização da reserva.
     * @throws RuntimeException em caso de falha na atualização da reserva.
     */
    public ResponseTicket PUTCreateParkingReservation(TicketInfo body, String ticketID) {
        try {
            var objectMapper = new ObjectMapper();

            String requestBody = objectMapper
                    .writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Authorization", "Basic " + encondeAPICredentials(apiID, apiSecret))
                    .uri(URI.create(parkAPIURL + "ticket/" + ticketID))
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            String parsedResponse = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

            return objectMapper.readValue(parsedResponse, ResponseTicket.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para apagar um ticket pelo seu ID.
     *
     * @param idTicket ID do ticket a ser apagado.
     * @return Verdadeiro se a operação for bem sucedida, falso caso contrário.
     * @throws RuntimeException se houver algum erro durante a execução da operação.
     */
    public Boolean DeleteTicket(String idTicket) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(parkAPIURL + "Ticket/" + idTicket))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Authorization", "Basic " + encondeAPICredentials(apiID, apiSecret))
                    .DELETE()
                    .build();

            var client = HttpClient.newHttpClient();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 202) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}