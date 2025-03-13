package br.com.alura.service;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.domain.Abrigo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbrigoServiceTest {

    // Mockando (simulando) uma instancia de ClientHttpConfiguration
    private ClientHttpConfiguration client = mock(ClientHttpConfiguration.class);

    // Instanciando a classe que quer ser testado
    private AbrigoService abrigoService = new AbrigoService(client);

    // Mockando a resposta da requisição para pegar o body da requisição
    private HttpResponse<String> response = mock(HttpResponse.class);

    // Objeto de teste
    private Abrigo abrigo = new Abrigo("Teste", "21992472657", "teste@gmail.com");

    // Anotação para simbolizar que está é uma classe de teste
    @Test
    public void deveVerificarQuandoHaAbrigo() throws IOException, InterruptedException {
        abrigo.setId(0L);

        // O resultado esperado quando acionado requisição Get
        String expectedAbrigosCadastrados = "Abrigos cadastrados:";
        String expectedIdENome = "0 - Teste";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        // Quando houver corpo na resposta, então retorne um JSON de abrigo
        when(response.body()).thenReturn("[{"+abrigo.toString()+"}]");

        // Quando for disparado uma requisição GET, então retorne a resposta com o JSON
        when(client.dispararRequisicaoGet(anyString())).thenReturn(response);

        abrigoService.listarAbrigo();

        // Pegando a listagem de abrigos e comparando com o esperado e o que foi retornado
        String[] lines = baos.toString().split(System.lineSeparator());
        String actualAbrigosCadastrados = lines[0];
        String actualIdENome = lines[1];

        assertEquals(expectedAbrigosCadastrados, actualAbrigosCadastrados);
        assertEquals(expectedIdENome, actualIdENome);

    }

    @Test
    public void deveVerificarQuandoNaoHaAbrigo() throws IOException, InterruptedException {
        abrigo.setId(0L);
        String expected = "Não há abrigos cadastrados";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        when(response.body()).thenReturn("[]");
        when(client.dispararRequisicaoGet(anyString())).thenReturn(response);

        abrigoService.listarAbrigo();
        
        String[] lines = baos.toString().split(System.lineSeparator());
        String actualAbrigosCadastrados = lines[0];

        assertEquals(expected, actualAbrigosCadastrados);

    }

}
