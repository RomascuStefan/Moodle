package com.POS_API_MONGO.Controller;

import com.POS_API_MONGO.Model.DTO.MaterieFilesResponseDTO;
import com.POS_API_MONGO.Service.MaterieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/academia_mongo")
public class MaterieController {

    private final MaterieService materieService;

    @Autowired
    public MaterieController(MaterieService materieService) {
        this.materieService = materieService;
    }

    @PostMapping("/{codMaterie}/upload_file")
    public ResponseEntity<String> addFisierInLaborator(
            @PathVariable String codMaterie,
            @RequestParam String locatie,
            @RequestParam("file") MultipartFile file) {

        materieService.saveFile(locatie, codMaterie, file);
        return ResponseEntity.ok("Fișier salvat cu succes.");
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

    @GetMapping("/{codMaterie}")
    public ResponseEntity<MaterieFilesResponseDTO> getAllFiles(@PathVariable String codMaterie) {
        MaterieFilesResponseDTO response = materieService.getAllFilesForMaterie(codMaterie);
        return ResponseEntity.ok(response);
    }

}
