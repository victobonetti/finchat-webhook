package br.com.kod3.models.evolution;

import io.quarkus.logging.Log;

public class EvolutionPayloadConverter {
    public EvolutionPayloadConverter(){}

    public static ConvertedDto parse(WebhookBodyDto dto){
        MessageType type = dto.data().messageType();
        var builder = ConvertedDto.builder().type(type).telefone(dto.data().key().remoteJid().split("@")[0]);

        if(type.equals(MessageType.audioMessage) || type.equals(MessageType.imageMessage)){
            return builder.base64(dto.data().message().base64()).build();
        }

        if(type.equals(MessageType.conversation)){
            return builder.textMessage(dto.data().message().conversation()).build();
        }

        throw new RuntimeException("Erro ao converter webhook.");
    }
}
