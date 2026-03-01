package com.bittasks.bittasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // Só de herdar o JpaRepository, você ganha comandos como:
    // save() -> Salva no banco
    // findById() -> Busca um usuário pelo Chat ID
    // delete() -> Apaga do banco
}