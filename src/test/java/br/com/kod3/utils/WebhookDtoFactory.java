package br.com.kod3.utils;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto.DataDto;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto.DataDto.ContextInfoDto;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto.DataDto.KeyDto;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto.DataDto.MessageDto;
import java.util.List;

/** Factory for creating WebhookBodyDto instances for testing purposes. */
public class WebhookDtoFactory {

  /**
   * Creates a WebhookBodyDto for a standard text message scenario.
   *
   * @return A fully populated {@link WebhookBodyDto} object.
   */
  public static WebhookBodyDto createWebhookTextMessage() {
    return new WebhookBodyDto(
        "messages.upsert",
        "Finchat",
        new DataDto(
            new KeyDto("5519998467194@s.whatsapp.net", false, "3EB0062835B784532CE8B8"),
            "Mariana M. Macêdo",
            "DELIVERY_ACK",
            new MessageDto(
                null, // listResponseMessage
                "gastei 25,78 com Uber", // conversation
                null, // audioMessage
                null, // imageMessage
                new MessageDto.MessageContextInfoDto(
                    new MessageDto.MessageContextInfoDto.DeviceListMetadataDto(
                        "2GZdtdx4m0aiRQ==",
                        "1748701972",
                        "aGIbAe0okuuVEw==",
                        "1748853187",
                        "E2EE",
                        "E2EE"),
                    2,
                    "ZHxBLzK1Fbr8C8lo1yHmU143gZK33A2FFAZPD1GQY3s="),
                null // base64
                ),
            null, // contextInfo
            MessageType.conversation,
            1749766101L,
            "188cbb13-bcb7-4d5a-979d-ba0fe75a3ee9",
            "web"),
        "https://n8n-nk44c4gg4o8w8s080kkwww80.wwwacedo.com/webhook/1becb0ac-5488-448e-9797-dbf9594c8a8e",
        "2025-06-12T19:08:21.833Z",
        "5519998467194@s.whatsapp.net",
        "https://evo-eo4kkc8ssckggkk0wos008ww.wwwacedo.com",
        "0FABE7621441-4921-8196-42BF3953B6AA");
  }

  /**
   * Creates a WebhookBodyDto for an image message scenario.
   *
   * @return A fully populated {@link WebhookBodyDto} object.
   */
  public static WebhookBodyDto createWebhookImageMessage() {
    return new WebhookBodyDto(
        "messages.upsert",
        "Finchat",
        new DataDto(
            new KeyDto("5519998467194@s.whatsapp.net", false, "3EB0647DB9DBAB05DA6BCC"),
            "Mariana M. Macêdo",
            "DELIVERY_ACK",
            new MessageDto(
                null, // listResponseMessage
                null, // conversation
                null, // audioMessage
                new MessageDto.ImageMessageDto(
                    "https://mmg.whatsapp.net/o1/v/t24/f2/m231/AQOKPiNAxv9_A1PYxrPhIISOBI518MCc6HjwO9NpWyFBjP_HFY0AfuuStjomP9usfwJzTo5IsiyFiS70jvqePxMfpaAyo8sOO90VbDjD7Q?ccb=9-4&oh=01_Q5Aa1wGeCyyO1R76WdG0HhS2F8wtnUPd6dKSnDjuYeMFFR22tw&oe=6872C90E&_nc_sid=e6ed6c&mms3=true",
                    "image/jpeg",
                    "0Fvo4+OrhA3QMtiILr+M5LAcefeO36UPp17d1cQdjSM=",
                    "226806",
                    1280,
                    960,
                    "l8m0JdI4Nb1+K3e89LYUNcREiPdjfZ2Pfz/FD2u73Eg=",
                    "8nMsH0uf4M5tcVW/guaCo1KSNyz1aBjeIpO1OQDlNns=",
                    "/o1/v/t24/f2/m231/AQOKPiNAxv9_A1PYxrPhIISOBI518MCc6HjwO9NpWyFBjP_HFY0AfuuStjomP9usfwJzTo5IsiyFiS70jvqePxMfpaAyo8sOO90VbDjD7Q?ccb=9-4&oh=01_Q5Aa1wGeCyyO1R76WdG0HhS2F8wtnUPd6dKSnDjuYeMFFR22tw&oe=6872C90E&_nc_sid=e6ed6c",
                    "1749763927",
                    "/9j/4AAQSkZJRgABAQAAAQABAAD",
                    null,
                    null,
                    false),
                new MessageDto.MessageContextInfoDto( // Added missing context info
                    new MessageDto.MessageContextInfoDto.DeviceListMetadataDto(
                        "2GZdtdx4m0aiRQ==",
                        "1748701972",
                        "aGIbAe0okuuVEw==",
                        "1748853187",
                        "E2EE",
                        "E2EE"),
                    2,
                    null),
                null // base64
                ),
            null,
            MessageType.imageMessage,
            1749763927L,
            "188cbb13-bcb7-4d5a-979d-ba0fe75a3ee9",
            "web"),
        "https://n8n-nk44c4gg4o8w8s080kkwww80.wwwacedo.com/webhook/1becb0ac-5488-448e-9797-dbf9594c8a8e",
        "2025-06-12T19:09:33.212Z",
        "5519998467194@s.whatsapp.net",
        "https://evo-eo4kkc8ssckggkk0wos008ww.wwwacedo.com",
        "0FABE7621441-4921-8196-42BF3953B6AA");
  }

  /**
   * Creates a WebhookBodyDto for an audio message scenario.
   *
   * @return A fully populated {@link WebhookBodyDto} object.
   */
  public static WebhookBodyDto createWebhookAudioMessage() {
    return new WebhookBodyDto(
        "messages.upsert",
        "Finchat",
        new DataDto(
            new KeyDto("5519998467194@s.whatsapp.net", false, "3A35903C2393D681D816"),
            "Mariana M. Macêdo",
            "DELIVERY_ACK",
            new MessageDto(
                null, // listResponseMessage
                null, // conversation
                new MessageDto.AudioMessageDto(
                    "https://mmg.whatsapp.net/v/t62.7117-24/11928092_718197107271007_3126797790610630769_n.enc?ccb=11-4&oh=01_Q5Aa1wHmGJUWHVourU4Z50YeFMNDJZdBdzwCCh0l_xpncUon3w&oe=6872AB9D&_nc_sid=5e03e0&mms3=true",
                    "audio/ogg; codecs=opus",
                    "ViTMFBxdXV25Lq2uqIxokalqZH0zTKMPv5KNI3AUTqg=",
                    7397L,
                    3,
                    true,
                    "4n9wpkETNAIeGnLeDwEo+4uqHrLTzYz/eXymcPdBee0=",
                    "rtDA44OE+cKTd/Bl/gnBvNre6waP70CmgNRl7jKGthA=",
                    "/v/t62.7117-24/11928092_718197107271007_3126797790610630769_n.enc?ccb=11-4&oh=01_Q5Aa1wHmGJUWHVourU4Z50YeFMNDJZdBdzwCCh0l_xpncUon3w&oe=6872AB9D&_nc_sid=5e03e0",
                    1749766129L,
                    "tpHToVg0kjLj8A==",
                    "AAABAgMEBAQEBAMDAwMDAwopSGNaUUhQWWJkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkX1JENy8nIA=="),
                null, // imageMessage
                new MessageDto.MessageContextInfoDto(
                    new MessageDto.MessageContextInfoDto.DeviceListMetadataDto(
                        "A7Kt9khrsxdpxw==",
                        "1748701972",
                        "aGIbAe0okuuVEw==",
                        "1748853187",
                        null,
                        null),
                    2,
                    "xd3KB2k8b4FTF3IOJSflwYyB+OrFbt2bLlbaEsQEUjM="),
                "T2dnUwACAAAAAAAAAABkAAAAAAAAADI5MFABE09wdXNIZWFkAQE4AYA"),
            null, // contextInfo
            MessageType.audioMessage,
            1749766134L,
            "188cbb13-bcb7-4d5a-979d-ba0fe75a3ee9",
            "ios"),
        "https://n8n-nk44c4gg4o8w8s080kkwww80.wwwacedo.com/webhook/1becb0ac-5488-448e-9797-dbf9594c8a8e",
        "2025-06-12T19:08:55.650Z",
        "5519998467194@s.whatsapp.net",
        "https://evo-eo4kkc8ssckggkk0wos008ww.wwwacedo.com",
        "0FABE7621441-4921-8196-42BF3953B6AA");
  }

  /**
   * Creates a WebhookBodyDto for a list response message scenario.
   *
   * @return A fully populated {@link WebhookBodyDto} object.
   */
  public static WebhookBodyDto createWebhookListResponseMessage() {
    var rows =
        List.of(
            new ContextInfoDto.QuotedMessageDto.ListMessageDto.SectionDto.RowDto(
                "conservador",
                "Prefiro segurança e estabilidade. Aceito ganhar menos, desde que o valor investido esteja protegido. Fico desconfortável com oscilações ou perdas, mesmo que temporárias.",
                "conservador"),
            new ContextInfoDto.QuotedMessageDto.ListMessageDto.SectionDto.RowDto(
                "moderado",
                "Busco um equilíbrio entre segurança e rentabilidade. Estou disposto a aceitar alguma volatilidade em troca de retornos melhores no longo prazo, mas ainda valorizo uma certa previsibilidade.",
                "moderado"),
            new ContextInfoDto.QuotedMessageDto.ListMessageDto.SectionDto.RowDto(
                "arrojado",
                "Estou disposto a correr riscos maiores para buscar ganhos mais altos. Entendo que o investimento pode oscilar bastante e aceito possíveis perdas no curto prazo em busca de valorização futura.",
                "arrojado"));

    var sections =
        List.of(
            new ContextInfoDto.QuotedMessageDto.ListMessageDto.SectionDto(
                "Ações Disponíveis", rows));

    var listMessage =
        new ContextInfoDto.QuotedMessageDto.ListMessageDto(
            "Pesquisa de perfil de investidor",
            "Para começar a usar o Finchat, antes, é necessário cadastrar o seu perfil de investidor. Vamos lá?\\n\\nImagine que você está prestes a fazer um investimento de médio prazo (de 3 a 5 anos). Qual das alternativas abaixo mais se aproxima da sua atitude em relação ao risco?\\n",
            "Ver Opções",
            "SINGLE_SELECT",
            sections,
            "");

    var quotedMessage =
        new ContextInfoDto.QuotedMessageDto(
            new MessageDto.MessageContextInfoDto(null, 0, null), listMessage);

    var contextInfo =
        new ContextInfoDto(
            "3EB060F42B56D9F572036DD7DFD58C58F81A96DB",
            "5519998467194@s.whatsapp.net",
            quotedMessage,
            null);

    return new WebhookBodyDto(
        "messages.upsert",
        "Finchat",
        new DataDto(
            new KeyDto("5519998467194@s.whatsapp.net", false, "3EB08C4ECC5588D6786A2E"),
            "Mariana M. Macêdo",
            "DELIVERY_ACK",
            new MessageDto(
                new MessageDto.ListResponseMessageDto(
                    "arrojado",
                    "SINGLE_SELECT",
                    new MessageDto.ListResponseMessageDto.SingleSelectReplyDto("arrojado"),
                    contextInfo,
                    "Estou disposto a correr riscos maiores para buscar ganhos mais altos. Entendo que o investimento pode oscilar bastante e aceito possíveis perdas no curto prazo em busca de valorização futura."),
                null, // conversation
                null, // audioMessage
                null, // imageMessage
                new MessageDto.MessageContextInfoDto(
                    new MessageDto.MessageContextInfoDto.DeviceListMetadataDto(
                        "2GZdtdx4m0aiRQ==",
                        "1748701972",
                        "aGIbAe0okuuVEw==",
                        "1748853187",
                        "E2EE",
                        "E2EE"),
                    2,
                    null),
                null // base64
                ),
            contextInfo,
            MessageType.listResponseMessage,
            1749766033L,
            "188cbb13-bcb7-4d5a-979d-ba0fe75a3ee9",
            "web"),
        "https://n8n-nk44c4gg4o8w8s080kkwww80.wwwacedo.com/webhook/1becb0ac-5488-448e-9797-dbf9594c8a8e",
        "2025-06-12T19:07:14.263Z",
        "5519998467194@s.whatsapp.net",
        "https://evo-eo4kkc8ssckggkk0wos008ww.wwwacedo.com",
        "0FABE7621441-4921-8196-42BF3953B6AA");
  }

  /**
   * Creates a WebhookBodyDto for a list response message confirming a transaction.
   *
   * @return A fully populated {@link WebhookBodyDto} object.
   */
  public static WebhookBodyDto createWebhookListResponseMessageWithTransaction() {
    var rows =
        List.of(
            new ContextInfoDto.QuotedMessageDto.ListMessageDto.SectionDto.RowDto(
                "✅ Confirmar gasto", "Confirmar gasto", "confirmar"),
            new ContextInfoDto.QuotedMessageDto.ListMessageDto.SectionDto.RowDto(
                "❌ Cancelar gasto", "Cancelar gasto", "cancelar"));

    var sections =
        List.of(
            new ContextInfoDto.QuotedMessageDto.ListMessageDto.SectionDto(
                "Ações Disponíveis", rows));

    var listMessage =
        new ContextInfoDto.QuotedMessageDto.ListMessageDto(
            "Registrar gasto",
            "Descrição: HCT Restaurante LTDA\nCategoria: Alimentação\nValor: R$ 54.45\n",
            "Ver Opções",
            "SINGLE_SELECT",
            sections,
            "");

    var quotedMessage =
        new ContextInfoDto.QuotedMessageDto(
            new MessageDto.MessageContextInfoDto(null, 0, null), listMessage);

    var contextInfo =
        new ContextInfoDto(
            "3EB0AAFC000283EA3313A8FBC70BD4F68C141476",
            "5511991113699@s.whatsapp.net",
            quotedMessage,
            null);

    return new WebhookBodyDto(
        "messages.upsert",
        "Finchat",
        new DataDto(
            new KeyDto("5511978703935@s.whatsapp.net", false, "3EB027C14FDCBDFD3A2ED7"),
            "Mariana M. Macêdo",
            "DELIVERY_ACK",
            new MessageDto(
                new MessageDto.ListResponseMessageDto(
                    "✅ Confirmar gasto",
                    "SINGLE_SELECT",
                    new MessageDto.ListResponseMessageDto.SingleSelectReplyDto("confirmar"),
                    contextInfo,
                    "Confirmar gasto"),
                null, // conversation
                null, // audioMessage
                null, // imageMessage
                new MessageDto.MessageContextInfoDto(
                    new MessageDto.MessageContextInfoDto.DeviceListMetadataDto(
                        "2GZdtdx4m0aiRQ==",
                        "1748701972",
                        "aGIbAe0okuuVEw==",
                        "1748853187",
                        "E2EE",
                        "E2EE"),
                    2,
                    null),
                null // base64
                ),
            contextInfo,
            MessageType.listResponseMessage,
            1749766195L,
            "188cbb13-bcb7-4d5a-979d-ba0fe75a3ee9",
            "web"),
        "https://n8n-nk44c4gg4o8w8s080kkwww80.wwwacedo.com/webhook/1becb0ac-5488-448e-9797-dbf9594c8a8e",
        "2025-06-12T19:09:56.047Z",
        "5511991113699@s.whatsapp.net",
        "https://evo-eo4kkc8ssckggkk0wos008ww.wwwacedo.com",
        "0FABE7621441-4921-8196-42BF3953B6AA");
  }
}
