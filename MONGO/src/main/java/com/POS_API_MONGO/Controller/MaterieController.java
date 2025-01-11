package com.POS_API_MONGO.Controller;

import auth.AuthServiceOuterClass;
import com.POS_API_MONGO.DTO.*;
import com.POS_API_MONGO.Service.AuthService;
import com.POS_API_MONGO.Service.MaterieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.POS_API_MONGO.Service.AuthService.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/academia_mongo")
public class MaterieController {

    private final MaterieService materieService;
    private final AuthService authService;

    @Autowired
    public MaterieController(MaterieService materieService, AuthService authService) {
        this.materieService = materieService;
        this.authService = authService;
    }

    @PostMapping("/{codMaterie}")
    public ResponseEntity<EntityModel<AddFileResponseDTO>> uploadFile(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String codMaterie,
            @RequestParam String locatie,
            @RequestBody MultipartFile file) {

        authService.canModifyResource(codMaterie, authorizationHeader, List.of(PROFESOR));

        materieService.saveFile(locatie, codMaterie, file);

        AddFileResponseDTO responseDTO = new AddFileResponseDTO(file.getOriginalFilename(), locatie);

        EntityModel<AddFileResponseDTO> response = EntityModel.of(
                responseDTO,
                linkTo(methodOn(MaterieController.class).uploadFile(null, codMaterie, locatie, file)).withSelfRel().withType("POST"),
                linkTo(methodOn(MaterieController.class).getAllFiles(null, codMaterie)).withRel("all-files").withType("GET")
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{codMaterie}")
    public ResponseEntity<Object> FileRequstHandler(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String codMaterie,
            @RequestParam String action,
            @RequestParam(required = false) String locatie,
            @RequestParam(required = false) String numeFisier) {

        switch (action) {
            case "download":
                if (locatie == null || locatie.trim().isEmpty())
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File location is missing");

                if(numeFisier == null || numeFisier.trim().isEmpty())
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File name is missing");

                return downloadFile(authorizationHeader, codMaterie, locatie, numeFisier);

            case "get_files":
                return getAllFiles(authorizationHeader, codMaterie);

            case "get_grading":
                return getGrading(authorizationHeader, codMaterie);

            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid action parameter");
        }
    }


    public ResponseEntity<Object> downloadFile(String authorizationHeader, String codMaterie, String locatie, String numeFisier) {

        authService.canAccessResource(codMaterie, authorizationHeader, List.of(PROFESOR, STUDENT));

        Resource resource = materieService.getFileResource(codMaterie, locatie, numeFisier);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    public ResponseEntity<Object> getAllFiles(String authorizationHeader, String codMaterie) {
        authService.canAccessResource(codMaterie, authorizationHeader, List.of(PROFESOR, STUDENT));

        MaterieFilesResponseDTO response = materieService.getAllFilesForMaterie(codMaterie);

        EntityModel<MaterieFilesResponseDTO> entityModel = EntityModel.of(
                response,
                linkTo(methodOn(MaterieController.class).getAllFiles(null, codMaterie)).withSelfRel().withType("GET")
        );

        return ResponseEntity.ok(entityModel);
    }


    public ResponseEntity<Object> getGrading(String authorizationHeader, String codMaterie) {
        AuthServiceOuterClass.Role role = authService.canAccessResource(codMaterie, authorizationHeader, List.of(PROFESOR, STUDENT, ADMIN));

        GradingDTO grading = materieService.getGradingByCodMaterie(codMaterie);

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(MaterieController.class).getGrading(null, codMaterie)).withSelfRel().withType("GET"));

        if (role == PROFESOR) {
            links.add(linkTo(methodOn(MaterieController.class).modifyGrading(null, codMaterie, null)).withRel("update-grading").withType("PUT"));
        }

        EntityModel<GradingDTO> gradingModel = EntityModel.of(grading, links);

        return ResponseEntity.ok(gradingModel);
    }


    @PatchMapping("/{codMaterie}")
    public ResponseEntity<EntityModel<GradingDTO>> modifyGrading(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String codMaterie,
            @RequestBody @Valid GradingDTO gradingDTO) {

        authService.canModifyResource(codMaterie, authorizationHeader, List.of(PROFESOR));

        GradingDTO updatedGrading = materieService.updateGrading(codMaterie, gradingDTO.getProbeExaminare());

        EntityModel<GradingDTO> gradingModel = EntityModel.of(
                updatedGrading,
                linkTo(methodOn(MaterieController.class).modifyGrading(null, codMaterie, gradingDTO)).withSelfRel().withType("PUT"),
                linkTo(methodOn(MaterieController.class).getGrading(null, codMaterie)).withRel("get-grading").withType("GET")
        );

        return ResponseEntity.ok(gradingModel);
    }

    @PostMapping
    public ResponseEntity<String> addMaterie(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody CreateMaterieRequestDTO createMaterieRequestDTO) {
        authService.verifyRequest(authorizationHeader, List.of(SQL));

        materieService.createMaterie(createMaterieRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Materie creata");
    }

    @DeleteMapping("/{codMaterie}")
    public ResponseEntity<EntityModel<DeleteFileResponseDTO>> deleteFisierInLaborator(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String codMaterie,
            @RequestParam String locatie,
            @RequestParam String numeFisier) {

        authService.canModifyResource(codMaterie, authorizationHeader, List.of(PROFESOR));

        materieService.deleteFile(locatie, codMaterie, numeFisier);

        DeleteFileResponseDTO responseDTO = new DeleteFileResponseDTO(numeFisier, locatie);

        EntityModel<DeleteFileResponseDTO> response = EntityModel.of(
                responseDTO,
                linkTo(methodOn(MaterieController.class).deleteFisierInLaborator(authorizationHeader, codMaterie, locatie, numeFisier)).withSelfRel().withType("DELETE"),
                linkTo(methodOn(MaterieController.class).getAllFiles(authorizationHeader, codMaterie)).withRel("all-files").withType("GET"),
                linkTo(methodOn(MaterieController.class).uploadFile(authorizationHeader, codMaterie, null, null)).withRel("upload-file").withType("POST")

        );

        return ResponseEntity.ok(response);
    }


}
