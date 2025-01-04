package com.POS_API_MONGO.Controller;

import auth.AuthServiceOuterClass;
import com.POS_API_MONGO.DTO.*;
import com.POS_API_MONGO.Service.AuthService;
import com.POS_API_MONGO.Service.MaterieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/{codMaterie}/upload_file")
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

    @GetMapping("/{codMaterie}/download_file")
    public ResponseEntity<Resource> downloadFile(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String codMaterie,
            @RequestParam String locatie,
            @RequestParam String numeFisier) {

        AuthServiceOuterClass.Role role = authService.canAccessResource(codMaterie, authorizationHeader, List.of(PROFESOR, STUDENT));

        Resource resource = materieService.getFileResource(codMaterie, locatie, numeFisier);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/{codMaterie}/files")
    public ResponseEntity<EntityModel<MaterieFilesResponseDTO>> getAllFiles(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable String codMaterie) {
        AuthServiceOuterClass.Role role = authService.canAccessResource(codMaterie, authorizationHeader, List.of(PROFESOR, STUDENT));

        MaterieFilesResponseDTO response = materieService.getAllFilesForMaterie(codMaterie);

        EntityModel<MaterieFilesResponseDTO> entityModel = EntityModel.of(
                response,
                linkTo(methodOn(MaterieController.class).getAllFiles(null, codMaterie)).withSelfRel().withType("GET")
        );

        return ResponseEntity.ok(entityModel);
    }


    @GetMapping("/{codMaterie}/grading")
    public ResponseEntity<EntityModel<GradingDTO>> getGrading(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable String codMaterie) {
        AuthServiceOuterClass.Role role = authService.canAccessResource(codMaterie, authorizationHeader, List.of(PROFESOR, STUDENT, ADMIN));

        GradingDTO grading = materieService.getGradingByCodMaterie(codMaterie);

        EntityModel<GradingDTO> gradingModel = EntityModel.of(
                grading,
                linkTo(methodOn(MaterieController.class).getGrading(null, codMaterie)).withSelfRel().withType("GET"),
                linkTo(methodOn(MaterieController.class).modifyGrading(null, codMaterie, grading)).withRel("update-grading").withType("PUT")
        );

        return ResponseEntity.ok(gradingModel);
    }

    @PutMapping("/{codMaterie}/grading")
    public ResponseEntity<EntityModel<GradingDTO>> modifyGrading(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String codMaterie,
            @RequestBody @Valid GradingDTO gradingDTO) {

        AuthServiceOuterClass.Role role = authService.canModifyResource(codMaterie, authorizationHeader, List.of(PROFESOR));

        GradingDTO updatedGrading = materieService.updateGrading(codMaterie, gradingDTO.getProbeExaminare());

        EntityModel<GradingDTO> gradingModel = EntityModel.of(
                updatedGrading,
                linkTo(methodOn(MaterieController.class).modifyGrading(null, codMaterie, gradingDTO)).withSelfRel().withType("PUT"),
                linkTo(methodOn(MaterieController.class).getGrading(null, codMaterie)).withRel("get-grading").withType("GET")
        );

        return ResponseEntity.ok(gradingModel);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addMaterie(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody CreateMaterieRequestDTO createMaterieRequestDTO) {
        authService.verifyRequest(authorizationHeader, List.of(SQL));

        materieService.createMaterie(createMaterieRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Materie creata");
    }

    @DeleteMapping("/{codMaterie}/delete_file")
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
                linkTo(methodOn(MaterieController.class).getAllFiles(authorizationHeader, codMaterie)).withRel("all-files").withType("GET")
        );

        return ResponseEntity.ok(response);
    }


}
