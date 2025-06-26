package br.com.kod3;

import static org.junit.jupiter.api.Assertions.*;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.converter.EvolutionPayloadConverter;
import br.com.kod3.utils.WebhookDtoFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ConversorTest {

  @Inject
  EvolutionPayloadConverter c;

  @Test
  void test() {
    var expectedText = c.parse(WebhookDtoFactory.createWebhookTextMessage());
    var expectedImage = c.parse(WebhookDtoFactory.createWebhookImageMessage());
    var expectedAudio = c.parse(WebhookDtoFactory.createWebhookAudioMessage());
    var expectedListResponseSimple = c.parse(WebhookDtoFactory.createWebhookListResponseMessage());
    var expectedListResponseWithTransaction =
        c.parse(WebhookDtoFactory.createWebhookListResponseMessageWithTransaction());

    assertAll(
        "Verify all parsed message types and payloads",
        () ->
            assertEquals(
                MessageType.conversation,
                expectedText.getType(),
                "Should be a conversation message type"),
        () ->
            assertEquals(
                MessageType.imageMessage,
                expectedImage.getType(),
                "Should be an image message type"),
        () ->
            assertEquals(
                MessageType.audioMessage,
                expectedAudio.getType(),
                "Should be an audio message type"),
        () ->
            assertEquals(
                MessageType.listResponseMessage,
                expectedListResponseSimple.getType(),
                "Should be a list response message type"),
        () ->
            assertEquals(
                MessageType.listResponseMessage,
                expectedListResponseWithTransaction.getType(),
                "Should be a list response message type"),
        () ->
            assertNull(
                expectedListResponseSimple.getTransactionPayloadDto(),
                "Simple list response should have a null transaction payload"),
        () ->
            assertNotNull(
                expectedListResponseWithTransaction.getTransactionPayloadDto(),
                "List response with transaction should have a non-null transaction payload"),
        () ->
            assertTrue(
                expectedListResponseWithTransaction.getData().toLowerCase().contains("confirmar")
                    || expectedListResponseWithTransaction
                        .getData()
                        .toLowerCase()
                        .contains("cancelar"),
                "The data should contain either 'Confirmar' or 'Cancelar'"));
  }
}
