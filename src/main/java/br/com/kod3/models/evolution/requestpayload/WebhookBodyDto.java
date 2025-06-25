package br.com.kod3.models.evolution.requestpayload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record WebhookBodyDto(
    String event,
    String instance,
    DataDto data,
    String destination,
    @JsonProperty("date_time") String dateTime,
    String sender,
    @JsonProperty("server_url") String serverUrl,
    String apikey) {

  public record DataDto(
      KeyDto key,
      String pushName,
      String status,
      MessageDto message,
      @JsonProperty("contextInfo") ContextInfoDto contextInfo,
      @NotNull MessageType messageType,
      long messageTimestamp,
      String instanceId,
      String source) {

    public record KeyDto(@JsonProperty("remoteJid") String remoteJid, boolean fromMe, String id) {}

    public record MessageDto(
        ListResponseMessageDto listResponseMessage,
        String conversation,
        AudioMessageDto audioMessage,
        ImageMessageDto imageMessage,
        @JsonProperty("messageContextInfo") MessageContextInfoDto messageContextInfo,
        String base64) {

      public record ListResponseMessageDto(
          String title,
          String listType,
          SingleSelectReplyDto singleSelectReply,
          @JsonProperty("contextInfo") ContextInfoDto contextInfo,
          String description) {
        public record SingleSelectReplyDto(String selectedRowId) {}
      }

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
          boolean viewOnce) {}

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
          String waveform) {}

      public record MessageContextInfoDto(
          @JsonProperty("deviceListMetadata") DeviceListMetadataDto deviceListMetadata,
          int deviceListMetadataVersion,
          String messageSecret) {
        public record DeviceListMetadataDto(
            String senderKeyHash,
            String senderTimestamp,
            String recipientKeyHash,
            String recipientTimestamp,
            String senderAccountType,
            String receiverAccountType) {}
      }
    }

    public record ContextInfoDto(
        String stanzaId,
        String participant,
        QuotedMessageDto quotedMessage,
        @JsonProperty("disappearingMode") DisappearingModeDto disappearingMode) {

      public record QuotedMessageDto(
          @JsonProperty("messageContextInfo") MessageDto.MessageContextInfoDto messageContextInfo,
          ListMessageDto listMessage) {
        public record ListMessageDto(
            String title,
            String description,
            String buttonText,
            String listType,
            List<SectionDto> sections,
            String footerText) {
          public record SectionDto(String title, List<RowDto> rows) {
            public record RowDto(String title, String description, String rowId) {}
          }
        }
      }

      public record DisappearingModeDto(String initiator) {}
    }
  }
}
