package com.quantum.auth.controller;

import org.apache.commons.lang3.RandomStringUtils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.quantum.auth.service.IUserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.quantum.auth.dto.UserDTO;
import com.quantum.auth.exception.ModelNotFoundException;
import com.quantum.auth.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = userService.findAll().stream().map(p -> mapper.map(p, UserDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        UserDTO dtoResponse;
        User obj = userService.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            dtoResponse = mapper.map(obj, UserDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody UserDTO dto) {
        // Generar una contraseña aleatoria segura (12 caracteres alfanuméricos)
        String rawPassword = RandomStringUtils.randomAlphanumeric(12);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        dto.setPassword(encodedPassword);

        User p = userService.save(mapper.map(dto, User.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdUser()).toUri();

        // Opcional: puedes retornar la contraseña generada en el body o por otro canal seguro
        // return ResponseEntity.created(location).body(rawPassword);

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @Valid @RequestBody UserDTO dto) {
        // Verificar que el usuario exista
        User obj = userService.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        // Mapear el DTO a la entidad User
        User user = mapper.map(dto, User.class);

        // Asignar el ID desde la URL al objeto mapeado
        user.setIdUser(id);

        // Actualizar el usuario
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }


    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        User obj = userService.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            userService.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}