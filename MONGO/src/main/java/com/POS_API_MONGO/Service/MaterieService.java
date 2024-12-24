package com.POS_API_MONGO.Service;

import com.POS_API_MONGO.Model.DTO.MaterieFilesResponseDTO;
import com.POS_API_MONGO.Model.Materie;
import com.POS_API_MONGO.Model.POJO.Fisier;
import com.POS_API_MONGO.Repository.MaterieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterieService {

    private final MaterieDAO materieRepo;
    private final RestTemplate restTemplate;

    @Value("${sql.service.url}")
    private String sqlServiceUrl;
    private static final String BASE_PATH = "src/main/resources/Files";

    @Autowired
    public MaterieService(MaterieDAO materieRepo, RestTemplate restTemplate) {
        this.materieRepo = materieRepo;
        this.restTemplate = restTemplate;
    }

    private void verifyDisciplineExists(String codMaterie) {
        if (!existsByCod(codMaterie)) {
            throw new IllegalArgumentException("Materia nu exista");
        }
    }

    public void saveFile(String locatie, String codMaterie, MultipartFile file) {
        verifyDisciplineExists(codMaterie);

        if (!(locatie.equals("laborator") || locatie.equals("curs"))) {
            throw new IllegalArgumentException("Locația trebuie să fie 'laborator' sau 'curs'.");
        }

        Path materiePath = Paths.get(BASE_PATH, codMaterie);
        Path locatiePath = materiePath.resolve(locatie);

        try {
            File materieDir = materiePath.toFile();
            if (!materieDir.exists()) {
                materieDir.mkdirs();
            }

            File locatieDir = locatiePath.toFile();
            if (!locatieDir.exists()) {
                locatieDir.mkdirs();
            }

            File savedFile = new File(locatieDir, file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(savedFile)) {
                fos.write(file.getBytes());
            }

            Optional<Materie> optionalMaterie = materieRepo.findByCodMaterie(codMaterie);
            if (optionalMaterie.isPresent()) {
                Materie materie = optionalMaterie.get();

                Fisier fisier = new Fisier();
                fisier.setNume(file.getOriginalFilename());
                fisier.setPath(savedFile.getAbsolutePath());

                if (locatie.equals("laborator")) {
                    materie.getFisiereLaborator().add(fisier);
                } else if (locatie.equals("curs")) {
                    materie.getFisiereCurs().add(fisier);
                }

                materieRepo.save(materie);
            }

        } catch (IOException e) {
            throw new RuntimeException("Eroare la salvarea fișierului: " + e.getMessage(), e);
        }
    }

    public Resource getFileResource(String codMaterie, String locatie, String numeFisier) {
        verifyDisciplineExists(codMaterie); // Verifică existența codului materiei

        if (!(locatie.equals("laborator") || locatie.equals("curs"))) {
            throw new IllegalArgumentException("Locația trebuie să fie 'laborator' sau 'curs'.");
        }

        Path filePath = Paths.get(BASE_PATH, codMaterie, locatie, numeFisier);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new IllegalArgumentException("Fișierul specificat nu există.");
        }

        return new FileSystemResource(file);
    }

    public MaterieFilesResponseDTO getAllFilesForMaterie(String codMaterie) {
        verifyDisciplineExists(codMaterie); // Verifică existența codului materiei

        Optional<Materie> optionalMaterie = materieRepo.findByCodMaterie(codMaterie);
        if (optionalMaterie.isEmpty()) {
            throw new IllegalArgumentException("Codul materiei nu există în baza de date.");
        }

        Materie materie = optionalMaterie.get();

        List<String> fisiereLaborator = materie.getFisiereLaborator().stream()
                .map(Fisier::getNume)
                .collect(Collectors.toList());

        List<String> fisiereCurs = materie.getFisiereCurs().stream()
                .map(Fisier::getNume)
                .collect(Collectors.toList());

        MaterieFilesResponseDTO responseDTO = new MaterieFilesResponseDTO();
        responseDTO.setFisiereLaborator(fisiereLaborator);
        responseDTO.setFisiereCurs(fisiereCurs);

        return responseDTO;
    }

    private boolean existsByCod(String cod) {
        String url = sqlServiceUrl + "/lectures/" + cod + "/exists";
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class);
        return response.getBody();
    }

}
