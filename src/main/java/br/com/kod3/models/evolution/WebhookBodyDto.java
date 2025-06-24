package br.com.kod3.models.evolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WebhookBodyDto(
        String event,
        String instance,
        DataDto data,
        String destination,
        @JsonProperty("date_time") String dateTime,
        String sender,
        @JsonProperty("server_url") String serverUrl,
        String apikey
) {

    public record DataDto(
            KeyDto key,
            String pushName,
            String status,
            MessageDto message,
            @JsonProperty("contextInfo")
            ContextInfoDto contextInfo,
            @NotNull
            MessageType messageType,
            long messageTimestamp,
            String instanceId,
            String source
    ) {

        public record KeyDto(
                @JsonProperty("remoteJid") String remoteJid,
                boolean fromMe,
                String id
        ) {}

        public record MessageDto(
                String conversation,
                AudioMessageDto audioMessage,
                ImageMessageDto imageMessage,
                @JsonProperty("messageContextInfo") MessageContextInfoDto messageContextInfo,
                String base64
        ) {

            public record ImageMessageDto(
                    String url,
                    String mimetype,
                    String fileSha256,
                    String fileLength,
                    int height,
                    int width,
                    String mediaKey,
                    String fileEncSha256,
                    String directPath,
                    String mediaKeyTimestamp,
                    String jpegThumbnail,
                    @JsonProperty("contextInfo") ContextInfoDto contextInfo,
                    boolean viewOnce
            ) {}

            public record AudioMessageDto(
                    String url,
                    String mimetype,
                    String fileSha256,
                    long fileLength,
                    int seconds,
                    boolean ptt,
                    String mediaKey,
                    String fileEncSha256,
                    String directPath,
                    long mediaKeyTimestamp,
                    String streamingSidecar,
                    String waveform
            ) {}

            public record MessageContextInfoDto(
                    @JsonProperty("deviceListMetadata") DeviceListMetadataDto deviceListMetadata,
                    int deviceListMetadataVersion,
                    String messageSecret
            ) {
                public record DeviceListMetadataDto(
                        String senderKeyHash,
                        String senderTimestamp,
                        String recipientKeyHash,
                        String recipientTimestamp,
                        String senderAccountType,
                        String receiverAccountType
                ) {}
            }
        }

        public record ContextInfoDto(
                @JsonProperty("disappearingMode") DisappearingModeDto disappearingMode
        ) {
            public record DisappearingModeDto(
                    String initiator
            ) {}
        }
    }
}