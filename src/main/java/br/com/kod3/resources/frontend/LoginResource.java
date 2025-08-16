package br.com.kod3.resources.frontend;

import br.com.kod3.models.verificationcode.CreateVerificationCodeRequestDto;
import br.com.kod3.models.verificationcode.ValidateVerificationCodeRequestDto;
import br.com.kod3.services.evolution.EvolutionApiService;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import br.com.kod3.services.security.SecurityService;
import br.com.kod3.services.user.UserService;
import br.com.kod3.services.verificationcode.VerificationCodeService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("v1/login")
public class LoginResource {

    private final EvolutionApiService evolutionApiService;
    private final VerificationCodeService verificationCodeService;
    private final SecurityService securityService;
    private final UserService userService;

    @Inject
    public LoginResource(EvolutionApiService evolutionApiService, VerificationCodeService verificationCodeService, SecurityService securityService, UserService userService) {
        this.evolutionApiService = evolutionApiService;
        this.verificationCodeService = verificationCodeService;
        this.securityService = securityService;
        this.userService = userService;
    }

    @POST
    @Path("verification-code/generate")
    @Consumes(APPLICATION_JSON)
    public Response sendVerificationCode(@Valid CreateVerificationCodeRequestDto request){
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
    @Path("verification-code/validate")
    @Consumes(APPLICATION_JSON)
    public Response validateVerificationCode(@Valid ValidateVerificationCodeRequestDto request){
        if (verificationCodeService.isValid(request.telefone(), request.code())) {
            var opt = userService.findByPhone(request.telefone());
            if (opt.isPresent()) {
                var user = opt.get();
                var tkn = securityService.generateTokenFor(user);
                return Response.accepted().entity(tkn).build();
            }
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
