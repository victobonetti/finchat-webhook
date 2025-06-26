package br.com.kod3;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.evolution.requestpayload.converter.EvolutionPayloadConverter;
import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.user.PerfilInvestidorType;
import br.com.kod3.models.user.User;
import br.com.kod3.services.*;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WebhookTest {
  @Mock EvolutionApiService evolutionApiService;

  @Mock TransactionService transactionService;

  @Mock UserService userService;

  @Mock ResponseHandler res;

  @Mock EvolutionPayloadConverter converter;

  @Mock Messages messages;

  @InjectMocks MainResource resource;

  private final String TEST_TXT = "teste";
  private final String TEST_PHONE = "5519998467194";

  @ParameterizedTest
  @EnumSource(MessageType.class)
  void testResponseForUnregisteredUserWithAllMessageTypes(MessageType type) {
    var mockDto = Mockito.mock(WebhookBodyDto.class);
    Mockito.when(converter.parse(mockDto))
        .thenReturn(ConvertedDto.builder().type(type).data(TEST_TXT).telefone(TEST_PHONE).build());

    resource.webhook(mockDto);

    Mockito.verify(res, Mockito.times(1))
        .send(CodigosDeResposta.SOLICITA_CADASTRO, type, Response.Status.NO_CONTENT);
  }

  @ParameterizedTest
  @EnumSource(MessageType.class)
  void testInserePendenciaPerfilInvestidorWithAllMessageTypes(MessageType type) {
    var mockDto = Mockito.mock(WebhookBodyDto.class);
    Mockito.when(converter.parse(mockDto))
        .thenReturn(ConvertedDto.builder().type(type).data(TEST_TXT).telefone(TEST_PHONE).build());

    Mockito.when(userService.findByPhone(TEST_PHONE))
        .thenReturn(Optional.of(User.builder().perfilInvestidor(null).build()));

    resource.webhook(mockDto);

    Mockito.verify(res, Mockito.times(1))
        .send(CodigosDeResposta.INSERE_PENDENCIA_E_SOLICITA_PERFIL, type, Response.Status.CREATED);
  }

  @ParameterizedTest
  @EnumSource(
      value = MessageType.class,
      names = {"audioMessage", "imageMessage", "conversation"})
  void testResentSolicitacaoPerfil(MessageType type) {
    var mockDto = Mockito.mock(WebhookBodyDto.class);
    Mockito.when(converter.parse(mockDto))
        .thenReturn(ConvertedDto.builder().type(type).data(TEST_TXT).telefone(TEST_PHONE).build());

    Mockito.when(userService.findByPhone(TEST_PHONE))
        .thenReturn(
            Optional.of(
                User.builder().perfilInvestidor(PerfilInvestidorType.CADASTRO_PENDENTE).build()));

    resource.webhook(mockDto);

    Mockito.verify(res, Mockito.times(1))
        .send(CodigosDeResposta.SOLICITA_PERFIL, type, Response.Status.OK);
  }

  @Test
  void testInserePerfilInvestidor() {
    var mockDto = Mockito.mock(WebhookBodyDto.class);
    Mockito.when(converter.parse(mockDto))
        .thenReturn(
            ConvertedDto.builder()
                .type(MessageType.listResponseMessage)
                .data("arrojado")
                .telefone(TEST_PHONE)
                .build());

    Mockito.when(userService.findByPhone(TEST_PHONE))
        .thenReturn(
            Optional.of(
                User.builder().perfilInvestidor(PerfilInvestidorType.CADASTRO_PENDENTE).build()));

    resource.webhook(mockDto);

    Mockito.verify(res, Mockito.times(1))
        .send(
            CodigosDeResposta.CONFIRMA_PERFIL,
            MessageType.listResponseMessage,
            Response.Status.CREATED);
  }

  @Test
  void testValorInvalidoInvestidor() {
    var mockDto = Mockito.mock(WebhookBodyDto.class);
    Mockito.when(converter.parse(mockDto))
        .thenReturn(
            ConvertedDto.builder()
                .type(MessageType.listResponseMessage)
                .data(TEST_TXT) // VALOR INVALIDO
                .telefone(TEST_PHONE)
                .build());

    Mockito.when(userService.findByPhone(TEST_PHONE))
        .thenReturn(
            Optional.of(
                User.builder().perfilInvestidor(PerfilInvestidorType.CADASTRO_PENDENTE).build()));

    resource.webhook(mockDto);

    Mockito.verify(res, Mockito.times(1))
        .send(
            CodigosDeResposta.PERFIL_INVESTIDOR_INVALIDO,
            MessageType.listResponseMessage,
            Response.Status.BAD_REQUEST);
  }

  @Test
  void testInsereTransacaoValorInvalido() {
    var mockDto = Mockito.mock(WebhookBodyDto.class);
    Mockito.when(converter.parse(mockDto))
        .thenReturn(
            ConvertedDto.builder()
                .type(MessageType.listResponseMessage)
                .data(TEST_TXT) // VALOR INVALIDO
                .telefone(TEST_PHONE)
                .transactionPayloadDto(TransactionPayloadDto.builder().build())
                .build());

    Mockito.when(userService.findByPhone(TEST_PHONE))
        .thenReturn(
            Optional.of(User.builder().perfilInvestidor(PerfilInvestidorType.ARROJADO).build()));

    resource.webhook(mockDto);

    Mockito.verify(res, Mockito.times(1))
        .send(
            CodigosDeResposta.ERRO_VALIDACAO_RESPOSTA_TRANSACAO,
            MessageType.listResponseMessage,
            Response.Status.BAD_REQUEST);
  }

  @Test
  void testInsereTransacao() {
    var mockDto = Mockito.mock(WebhookBodyDto.class);
    Mockito.when(converter.parse(mockDto))
        .thenReturn(
            ConvertedDto.builder()
                .type(MessageType.listResponseMessage)
                .data("confirmar")
                .telefone(TEST_PHONE)
                .transactionPayloadDto(TransactionPayloadDto.builder().build())
                .build());

    Mockito.when(userService.findByPhone(TEST_PHONE))
        .thenReturn(
            Optional.of(User.builder().perfilInvestidor(PerfilInvestidorType.ARROJADO).build()));

    resource.webhook(mockDto);

    Mockito.verify(res, Mockito.times(1))
        .send(
            CodigosDeResposta.CONFIRMA_TRANSACAO,
            MessageType.listResponseMessage,
            Response.Status.CREATED);
  }

  @Test
  void testCancelaTransacao() {
    var mockDto = Mockito.mock(WebhookBodyDto.class);
    Mockito.when(converter.parse(mockDto))
        .thenReturn(
            ConvertedDto.builder()
                .type(MessageType.listResponseMessage)
                .data("cancelar")
                .telefone(TEST_PHONE)
                .transactionPayloadDto(TransactionPayloadDto.builder().build())
                .build());

    Mockito.when(userService.findByPhone(TEST_PHONE))
        .thenReturn(
            Optional.of(User.builder().perfilInvestidor(PerfilInvestidorType.ARROJADO).build()));

    resource.webhook(mockDto);

    Mockito.verify(res, Mockito.times(1))
        .send(
            CodigosDeResposta.CANCELA_TRANSACAO,
            MessageType.listResponseMessage,
            Response.Status.NO_CONTENT);
  }
}
