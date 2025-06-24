package br.com.kod3;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class WebhookTest {
    @Test
    public void webhookTextMessage(){
        String textMessagePayload = """
                {
                            "event": "messages.upsert",
                            "instance": "Finchat",
                            "data": {
                            "key": {
                            "remoteJid": "5519998467194@s.whatsapp.net",
                            "fromMe": false,
                            "id": "3EB0062835B784532CE8B8"
                            },
                            "pushName": "Mariana M. Macêdo",
                            "status": "DELIVERY_ACK",
                            "message": {
                            "conversation": "gastei 25,78 com Uber",
                            "messageContextInfo": {
                            "deviceListMetadata": {
                            "senderKeyHash": "2GZdtdx4m0aiRQ==",
                            "senderTimestamp": "1748701972",
                            "senderAccountType": "E2EE",
                            "receiverAccountType": "E2EE",
                            "recipientKeyHash": "aGIbAe0okuuVEw==",
                            "recipientTimestamp": "1748853187"
                            },
                            "deviceListMetadataVersion": 2,
                            "messageSecret": "ZHxBLzK1Fbr8C8lo1yHmU143gZK33A2FFAZPD1GQY3s="
                            }
                            },
                            "messageType": "conversation",
                            "messageTimestamp": 1749766101,
                            "instanceId": "188cbb13-bcb7-4d5a-979d-ba0fe75a3ee9",
                            "source": "web"
                            },
                            "destination": "https://n8n-nk44c4gg4o8w8s080kkwww80.wwwacedo.com/webhook/1becb0ac-5488-448e-9797-dbf9594c8a8e",
                            "date_time": "2025-06-12T19:08:21.833Z",
                            "sender": "5519998467194@s.whatsapp.net",
                            "server_url": "https://evo-eo4kkc8ssckggkk0wos008ww.wwwacedo.com",
                            "apikey": "0FABE7621441-4921-8196-42BF3953B6AA"
                            }
                """;

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(textMessagePayload)
                .when()
                .post("/v1/webhook")
                .then()
                .statusCode(200)
                .header("FC-X-TYPE", String.valueOf(MessageType.conversation));

    }

    @Test
    public void webhookImage(){
        String imagePayload = """
                      {
                      "event": "messages.upsert",
                      "instance": "Finchat",
                      "data": {
                        "key": {
                          "remoteJid": "5519998467194@s.whatsapp.net",
                          "fromMe": false,
                          "id": "3EB0647DB9DBAB05DA6BCC"
                        },
                        "pushName": "Mariana M. Macêdo",
                        "status": "DELIVERY_ACK",
                        "message": {
                          "imageMessage": {
                            "url": "https://mmg.whatsapp.net/o1/v/t24/f2/m231/AQOKPiNAxv9_A1PYxrPhIISOBI518MCc6HjwO9NpWyFBjP_HFY0AfuuStjomP9usfwJzTo5IsiyFiS70jvqePxMfpaAyo8sOO90VbDjD7Q?ccb=9-4&oh=01_Q5Aa1wGeCyyO1R76WdG0HhS2F8wtnUPd6dKSnDjuYeMFFR22tw&oe=6872C90E&_nc_sid=e6ed6c&mms3=true",
                            "mimetype": "image/jpeg",
                            "fileSha256": "0Fvo4+OrhA3QMtiILr+M5LAcefeO36UPp17d1cQdjSM=",
                            "fileLength": "226806",
                            "height": 1280,
                            "width": 960,
                            "mediaKey": "l8m0JdI4Nb1+K3e89LYUNcREiPdjfZ2Pfz/FD2u73Eg=",
                            "fileEncSha256": "8nMsH0uf4M5tcVW/guaCo1KSNyz1aBjeIpO1OQDlNns=",
                            "directPath": "/o1/v/t24/f2/m231/AQOKPiNAxv9_A1PYxrPhIISOBI518MCc6HjwO9NpWyFBjP_HFY0AfuuStjomP9usfwJzTo5IsiyFiS70jvqePxMfpaAyo8sOO90VbDjD7Q?ccb=9-4&oh=01_Q5Aa1wGeCyyO1R76WdG0HhS2F8wtnUPd6dKSnDjuYeMFFR22tw&oe=6872C90E&_nc_sid=e6ed6c",
                            "mediaKeyTimestamp": "1749763927",
                            "jpegThumbnail": "/9j/4AAQSkZJRgABAQAAAQABAAD",
                            "contextInfo": {
                              "disappearingMode": {
                                "initiator": "CHANGED_IN_CHAT"
                              }
                            },
                            "viewOnce": false
                          },
                          "messageContextInfo": {
                            "deviceListMetadata": {
                              "senderKeyHash": "2GZdtdx4m0aiRQ==",
                              "senderTimestamp": "1748701972",
                              "senderAccountType": "E2EE",
                              "receiverAccountType": "E2EE",
                              "recipientKeyHash": "aGIbAe0okuuVEw==",
                              "recipientTimestamp": "1748853187"
                            },
                            "deviceListMetadataVersion": 2,
                            "messageSecret": "peNjpRGGocTlYJph2bn/V2KsuJyq91BgTbucOEMVJpk="
                          },
                          "base64": "/9j/4AAQSkZJRgABAQAAAQABAAD"
                        },
                        "contextInfo": {
                          "disappearingMode": {
                            "initiator": "CHANGED_IN_CHAT"
                          }
                        },
                        "messageType": "imageMessage",
                        "messageTimestamp": 1749766171,
                        "instanceId": "188cbb13-bcb7-4d5a-979d-ba0fe75a3ee9",
                        "source": "web"
                      },
                      "destination": "https://n8n-nk44c4gg4o8w8s080kkwww80.wwwacedo.com/webhook/1becb0ac-5488-448e-9797-dbf9594c8a8e",
                      "date_time": "2025-06-12T19:09:33.212Z",
                      "sender": "5519998467194@s.whatsapp.net",
                      "server_url": "https://evo-eo4kkc8ssckggkk0wos008ww.wwwacedo.com",
                      "apikey": "0FABE7621441-4921-8196-42BF3953B6AA"
                    }
              """;

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(imagePayload)
                .when()
                .post("/v1/webhook")
                .then()
                .statusCode(200)
                .header("FC-X-TYPE", String.valueOf(MessageType.imageMessage));

    }

    @Test
    public void webhookAudio(){
        String audioPayload = """
                {
                "event": "messages.upsert",
                "instance": "Finchat",
                "data": {
                "key": {
                "remoteJid": "5519998467194@s.whatsapp.net",
                "fromMe": false,
                "id": "3A35903C2393D681D816"
                },
                "pushName": "Mariana M. Macêdo",
                "status": "DELIVERY_ACK",
                "message": {
                "audioMessage": {
                "url": "https://mmg.whatsapp.net/v/t62.7117-24/11928092_718197107271007_3126797790610630769_n.enc?ccb=11-4&oh=01_Q5Aa1wHmGJUWHVourU4Z50YeFMNDJZdBdzwCCh0l_xpncUon3w&oe=6872AB9D&_nc_sid=5e03e0&mms3=true",
                "mimetype": "audio/ogg; codecs=opus",
                "fileSha256": "ViTMFBxdXV25Lq2uqIxokalqZH0zTKMPv5KNI3AUTqg=",
                "fileLength": "7397",
                "seconds": 3,
                "ptt": true,
                "mediaKey": "4n9wpkETNAIeGnLeDwEo+4uqHrLTzYz/eXymcPdBee0=",
                "fileEncSha256": "rtDA44OE+cKTd/Bl/gnBvNre6waP70CmgNRl7jKGthA=",
                "directPath": "/v/t62.7117-24/11928092_718197107271007_3126797790610630769_n.enc?ccb=11-4&oh=01_Q5Aa1wHmGJUWHVourU4Z50YeFMNDJZdBdzwCCh0l_xpncUon3w&oe=6872AB9D&_nc_sid=5e03e0",
                "mediaKeyTimestamp": "1749766129",
                "streamingSidecar": "tpHToVg0kjLj8A==",
                "waveform": "AAABAgMEBAQEBAMDAwMDAwopSGNaUUhQWWJkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkX1JENy8nIA=="
                },
                "messageContextInfo": {
                "deviceListMetadata": {
                "senderKeyHash": "A7Kt9khrsxdpxw==",
                "senderTimestamp": "1748701972",
                "recipientKeyHash": "aGIbAe0okuuVEw==",
                "recipientTimestamp": "1748853187"
                },
                "deviceListMetadataVersion": 2,
                "messageSecret": "xd3KB2k8b4FTF3IOJSflwYyB+OrFbt2bLlbaEsQEUjM="
                },
                "base64": "T2dnUwACAAAAAAAAAABkAAAAAAAAADI5MFABE09wdXNIZWFkAQE4AYA"
                },
                "contextInfo": null,
                "messageType": "audioMessage",
                "messageTimestamp": 1749766134,
                "instanceId": "188cbb13-bcb7-4d5a-979d-ba0fe75a3ee9",
                "source": "ios"
                },
                "destination": "https://n8n-nk44c4gg4o8w8s080kkwww80.wwwacedo.com/webhook/1becb0ac-5488-448e-9797-dbf9594c8a8e",
                "date_time": "2025-06-12T19:08:55.650Z",
                "sender": "5519998467194@s.whatsapp.net",
                "server_url": "https://evo-eo4kkc8ssckggkk0wos008ww.wwwacedo.com",
                "apikey": "0FABE7621441-4921-8196-42BF3953B6AA"
                },
                """;

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(audioPayload)
                .when()
                .post("/v1/webhook")
                .then()
                .statusCode(200)
                .header("FC-X-TYPE", String.valueOf(MessageType.audioMessage));

    }

    @Test
    public void webhookResponseMessage(){
        String responseMessagePayload = """
                {
                  "event": "messages.upsert",
                  "instance": "Finchat",
                  "data": {
                    "key": {
                      "remoteJid": "5511978703935@s.whatsapp.net",
                      "fromMe": false,
                      "id": "3EB08C4ECC5588D6786A2E"
                    },
                    "pushName": "Mariana M. Macêdo",
                    "status": "DELIVERY_ACK",
                    "message": {
                      "messageContextInfo": {
                        "deviceListMetadata": {
                          "senderKeyHash": "2GZdtdx4m0aiRQ==",
                          "senderTimestamp": "1748701972",
                          "senderAccountType": "E2EE",
                          "receiverAccountType": "E2EE",
                          "recipientKeyHash": "aGIbAe0okuuVEw==",
                          "recipientTimestamp": "1748853187"
                        },
                        "deviceListMetadataVersion": 2
                      },
                      "listResponseMessage": {
                        "title": "arrojado",
                        "listType": "SINGLE_SELECT",
                        "singleSelectReply": {
                          "selectedRowId": "arrojado"
                        },
                        "contextInfo": {
                          "stanzaId": "3EB060F42B56D9F572036DD7DFD58C58F81A96DB",
                          "participant": "5511991113699@s.whatsapp.net",
                          "quotedMessage": {
                            "messageContextInfo": {},
                            "listMessage": {
                              "title": "Pesquisa de perfil de investidor",
                              "description": "Para começar a usar o Finchat, antes, é necessário cadastrar o seu perfil de investidor. Vamos lá?\\n\\nImagine que você está prestes a fazer um investimento de médio prazo (de 3 a 5 anos). Qual das alternativas abaixo mais se aproxima da sua atitude em relação ao risco?\\n",
                              "buttonText": "Ver Opções",
                              "listType": "SINGLE_SELECT",
                              "sections": [
                                {
                                  "title": "Ações Disponíveis",
                                  "rows": [
                                    {
                                      "title": "conservador",
                                      "description": "Prefiro segurança e estabilidade. Aceito ganhar menos, desde que o valor investido esteja protegido. Fico desconfortável com oscilações ou perdas, mesmo que temporárias.",
                                      "rowId": "conservador"
                                    },
                                    {
                                      "title": "moderado",
                                      "description": "Busco um equilíbrio entre segurança e rentabilidade. Estou disposto a aceitar alguma volatilidade em troca de retornos melhores no longo prazo, mas ainda valorizo uma certa previsibilidade.",
                                      "rowId": "moderado"
                                    },
                                    {
                                      "title": "arrojado",
                                      "description": "Estou disposto a correr riscos maiores para buscar ganhos mais altos. Entendo que o investimento pode oscilar bastante e aceito possíveis perdas no curto prazo em busca de valorização futura.",
                                      "rowId": "arrojado"
                                    }
                                  ]
                                }
                              ],
                              "footerText": ""
                            }
                          }
                        },
                        "description": "Estou disposto a correr riscos maiores para buscar ganhos mais altos. Entendo que o investimento pode oscilar bastante e aceito possíveis perdas no curto prazo em busca de valorização futura."
                      }
                    },
                    "contextInfo": {
                      "stanzaId": "3EB060F42B56D9F572036DD7DFD58C58F81A96DB",
                      "participant": "5511991113699@s.whatsapp.net",
                      "quotedMessage": {
                        "messageContextInfo": {},
                        "listMessage": {
                          "title": "Pesquisa de perfil de investidor",
                          "description": "Para começar a usar o Finchat, antes, é necessário cadastrar o seu perfil de investidor. Vamos lá?\\n\\nImagine que você está prestes a fazer um investimento de médio prazo (de 3 a 5 anos). Qual das alternativas abaixo mais se aproxima da sua atitude em relação ao risco?\\n",
                          "buttonText": "Ver Opções",
                          "listType": "SINGLE_SELECT",
                          "sections": [
                            {
                              "title": "Ações Disponíveis",
                              "rows": [
                                {
                                  "title": "conservador",
                                  "description": "Prefiro segurança e estabilidade. Aceito ganhar menos, desde que o valor investido esteja protegido. Fico desconfortável com oscilações ou perdas, mesmo que temporárias.",
                                  "rowId": "conservador"
                                },
                                {
                                  "title": "moderado",
                                  "description": "Busco um equilíbrio entre segurança e rentabilidade. Estou disposto a aceitar alguma volatilidade em troca de retornos melhores no longo prazo, mas ainda valorizo uma certa previsibilidade.",
                                  "rowId": "moderado"
                                },
                                {
                                  "title": "arrojado",
                                  "description": "Estou disposto a correr riscos maiores para buscar ganhos mais altos. Entendo que o investimento pode oscilar bastante e aceito possíveis perdas no curto prazo em busca de valorização futura.",
                                  "rowId": "arrojado"
                                }
                              ]
                            }
                          ],
                          "footerText": ""
                        }
                      }
                    },
                    "messageType": "listResponseMessage",
                    "messageTimestamp": 1749766033,
                    "instanceId": "188cbb13-bcb7-4d5a-979d-ba0fe75a3ee9",
                    "source": "web"
                  },
                  "destination": "https://n8n-nk44c4gg4o8w8s080kkwww80.wwwacedo.com/webhook/1becb0ac-5488-448e-9797-dbf9594c8a8e",
                  "date_time": "2025-06-12T19:07:14.263Z",
                  "sender": "5511991113699@s.whatsapp.net",
                  "server_url": "https://evo-eo4kkc8ssckggkk0wos008ww.wwwacedo.com",
                  "apikey": "0FABE7621441-4921-8196-42BF3953B6AA"
                }
                """;
    }
}


