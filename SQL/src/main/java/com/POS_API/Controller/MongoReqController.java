package com.POS_API.Controller;

import com.POS_API.DTO.MongoRequestDTO;
import com.POS_API.Service.AuthService;
import com.POS_API.Service.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.POS_API.Helper.HelperFunctions.MONGO;

@RestController
@RequestMapping("/api/academia/validate")
public class MongoReqController {

    private final AuthService authService;
    private final DisciplinaService disciplinaService;

    @Autowired
    public MongoReqController(AuthService authService, DisciplinaService disciplinaService) {
        this.authService = authService;
        this.disciplinaService = disciplinaService;
    }

    @PostMapping("/access_resource")
    public ResponseEntity<String> canAccessResource(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody MongoRequestDTO mongoRequest) {
        boolean resp = false;

        authService.verifyRequest(authorizationHeader, List.of(MONGO));

        if (mongoRequest.getRole().equals("student") && disciplinaService.isAttending(mongoRequest.getCodDisciplina(), mongoRequest.getEmail()))
            resp = true;
        else if (mongoRequest.getRole().equals("profesor") && disciplinaService.isTeaching(mongoRequest.getCodDisciplina(), mongoRequest.getEmail()))
            resp = true;

        if (resp)
            return ResponseEntity.ok("View granted");
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("View NOT granted");
    }

    @PostMapping("/modify_resource")
    public ResponseEntity<String> canModifyResource(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @RequestBody MongoRequestDTO mongoRequest) {
        boolean resp = false;

        authService.verifyRequest(authorizationHeader, List.of(MONGO));

        if (mongoRequest.getRole().equals("profesor") && disciplinaService.isTeaching(mongoRequest.getCodDisciplina(), mongoRequest.getEmail()))
            resp = true;


        if (resp)
            return ResponseEntity.ok("Modification granted");
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Modification NOT granted");

    }


}
