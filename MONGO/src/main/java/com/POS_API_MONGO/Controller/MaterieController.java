package com.POS_API_MONGO.Controller;

import com.POS_API_MONGO.DTO.CreateMaterieRequestDTO;
import com.POS_API_MONGO.DTO.GradingDTO;
import com.POS_API_MONGO.DTO.MaterieFilesResponseDTO;
import com.POS_API_MONGO.Service.MaterieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/academia_mongo")
public class MaterieController {

    private final MaterieService materieService;

    @Autowired
    public MaterieController(MaterieService materieService) {
        this.materieService = materieService;
    }

    @PostMapping("/{codMaterie}/upload_file")
    public ResponseEntity<EntityModel<String>> addFisierInLaborator(
            @PathVariable String codMaterie,
            @RequestParam String locatie,
            @RequestParam("file") MultipartFile file) {

        materieService.saveFile(locatie, codMaterie, file);

        EntityModel<String> response = EntityModel.of(
                "Fișier salvat cu succes.",
                linkTo(methodOn(MaterieController.class).addFisierInLaborator(codMaterie, locatie, file)).withSelfRel().withType("POST"),
                linkTo(methodOn(MaterieController.class).getAllFiles(codMaterie)).withRel("all-files").withType("GET")
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{codMaterie}/download_file")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String codMaterie,
            @RequestParam String locatie,
            @RequestParam String numeFisier) {

        Resource resource = materieService.getFileResource(codMaterie, locatie, numeFisier);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/{codMaterie}/files")
    public ResponseEntity<CollectionModel<EntityModel<String>>> getAllFiles(@PathVariable String codMaterie) {
        MaterieFilesResponseDTO response = materieService.getAllFilesForMaterie(codMaterie);

        List<EntityModel<String>> laboratorFiles = response.getFisiereLaborator().stream()
                .map(fileName -> EntityModel.of(
                        fileName,
                        linkTo(methodOn(MaterieController.class).downloadFile(codMaterie, "laborator", fileName)).withRel("download-laborator-file").withType("GET")
                ))
                .collect(Collectors.toList());

        List<EntityModel<String>> cursFiles = response.getFisiereCurs().stream()
                .map(fileName -> EntityModel.of(
                        fileName,
                        linkTo(methodOn(MaterieController.class).downloadFile(codMaterie, "curs", fileName)).withRel("download-curs-file").withType("GET")
                ))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<String>> filesModel = CollectionModel.of(
                Stream.concat(laboratorFiles.stream(), cursFiles.stream()).collect(Collectors.toList()),
                linkTo(methodOn(MaterieController.class).getAllFiles(codMaterie)).withSelfRel().withType("GET")
        );

        return ResponseEntity.ok(filesModel);
    }

    @GetMapping("/{codMaterie}/grading")
    public ResponseEntity<EntityModel<GradingDTO>> getGrading(@PathVariable String codMaterie) {
        GradingDTO grading = materieService.getGradingByCodMaterie(codMaterie);

        EntityModel<GradingDTO> gradingModel = EntityModel.of(
                grading,
                linkTo(methodOn(MaterieController.class).getGrading(codMaterie)).withSelfRel().withType("GET"),
                linkTo(methodOn(MaterieController.class).modifyGrading(codMaterie, grading)).withRel("update-grading").withType("PUT")
        );

        return ResponseEntity.ok(gradingModel);
    }

    @PutMapping("/{codMaterie}/grading")
    public ResponseEntity<EntityModel<GradingDTO>> modifyGrading(
            @PathVariable String codMaterie,
            @RequestBody GradingDTO gradingDTO) {

        GradingDTO updatedGrading = materieService.updateGrading(codMaterie, gradingDTO.getProbeExaminare());

        EntityModel<GradingDTO> gradingModel = EntityModel.of(
                updatedGrading,
                linkTo(methodOn(MaterieController.class).modifyGrading(codMaterie, gradingDTO)).withSelfRel().withType("PUT"),
                linkTo(methodOn(MaterieController.class).getGrading(codMaterie)).withRel("get-grading").withType("GET")
        );

        return ResponseEntity.ok(gradingModel);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addMaterie(@RequestBody CreateMaterieRequestDTO createMaterieRequestDTO) {
        materieService.createMaterie(createMaterieRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Materie creata");
    }

    @DeleteMapping("/{codMaterie}/delete_file")
    public ResponseEntity<EntityModel<String>> deleteFisierInLaborator(
            @PathVariable String codMaterie,
            @RequestParam String locatie,
            @RequestParam String numeFisier) {

        materieService.deleteFile(locatie, codMaterie, numeFisier);

        EntityModel<String> response = EntityModel.of(
                "Fișier șters cu succes.",
                linkTo(methodOn(MaterieController.class).deleteFisierInLaborator(codMaterie, locatie, numeFisier)).withSelfRel().withType("DELETE"),
                linkTo(methodOn(MaterieController.class).getAllFiles(codMaterie)).withRel("all-files").withType("GET")
        );

        return ResponseEntity.ok(response);
    }
}
