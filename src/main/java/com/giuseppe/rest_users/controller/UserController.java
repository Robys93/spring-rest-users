package com.giuseppe.rest_users.controller;

import com.giuseppe.rest_users.model.dto.User;
import com.giuseppe.rest_users.service.api.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST per la gestione degli utenti
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    /**
     * Costruttore per iniettare il servizio utente
     */
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Restituisce una lista di utenti con opzioni di ordinamento e limite
     *
     * @param orderBy campo per l'ordinamento (email, name, surname) con suffisso _asc/_desc
     * @param limit numero massimo di utenti da restituire
     * @return una lista di utenti ordinata e limitata
     */
    @GetMapping
    public List<User> getAllUsers(
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) Integer limit) {
        return userService.getAllUsers(orderBy, limit);
    }

    /**
     * Restituisce un utente dato il suo indirizzo email
     *
     * @param email l'email dell'utente
     * @return (200) l'utente corrispondente o (404) utente non trovato
     */
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    /**
     * Crea un nuovo utente
     *
     * @param user l'utente da creare
     * @return (201) Se l'utente è creato, (409) se l'email esiste già
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Aggiorna i dati di un utente
     *
     * @param email l'email dell'utente da aggiornare
     * @param updatedUser i dati aggiornati
     * @return (200) se l'utente è aggiornato, (404) se l'utente con quella email non esiste
     */
    @PutMapping("/{email}")
    public ResponseEntity<User> udpateUser(@PathVariable String email, @RequestBody User updatedUser) {
        User user = userService.updateUser(email, updatedUser);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    /**
     * Elimina un utente data una email
     *
     * @param email l'indirizzo email dell'utente da eliminare
     * @return (204) se l'utente è eliminato, (404) se non è trovato
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        boolean deleted = userService.deleteUser(email);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}