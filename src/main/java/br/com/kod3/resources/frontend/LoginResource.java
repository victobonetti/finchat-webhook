package br.com.kod3.resources.frontend;

import br.com.kod3.models.verificationcode.CreateVerificationCodeRequestDto;
import br.com.kod3.models.verificationcode.ValidateVerificationCodeRequestDto;
import br.com.kod3.services.evolution.EvolutionApiService;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import br.com.kod3.services.verificationcode.VerificationCodeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("api/v1/login")
public class LoginResource {

    private final EvolutionApiService evolutionApiService;
    private final VerificationCodeService verificationCodeService;

    @Inject
    public LoginResource(EvolutionApiService evolutionApiService, VerificationCodeService verificationCodeService) {
        this.evolutionApiService = evolutionApiService;
        this.verificationCodeService = verificationCodeService;
    }

    @POST
    @Path("/verification-code/generate")
    Response sendVerificationCode(CreateVerificationCodeRequestDto request){
        var phone = request.telefone();
        var evo = new EvolutionMessageSender(evolutionApiService, phone);

        boolean exists = verificationCodeService.existsFor(phone);

        if (!exists) {
            Optional<String> code = verificationCodeService.generateFor(phone);

            if (code.isPresent()) {

                evo.send("Seu código de verificação é: " +
                        code.get().substring(0, 3) + "-" +
                        code.get().substring(3, 6)
                );

                return Response.status(201).build();
            }
        }

        return Response.ok().build();
    }

    @POST
    @Path("/verification-code/validate")
    Response validateVerificationCode(ValidateVerificationCodeRequestDto request){
        var phone = request.telefone();
        var evo = new EvolutionMessageSender(evolutionApiService, phone);

        boolean exists = verificationCodeService.existsFor(phone);

        if (!exists) {
            Optional<String> code = verificationCodeService.generateFor(phone);

            if (code.isPresent()) {

                evo.send("Foi efetuado um novo login em sua conta!");

                return Response.status(201).build();
            }
        }

        return Response.ok().build();
    }
}
