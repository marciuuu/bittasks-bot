package com.bittasks.bittasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // Olha a mágica do Spring aqui!
    // Ele lê esse nome e entende: "Busque uma Lista de Tarefas onde o ChatId seja X e o status Concluida seja Y"
    List<Tarefa> findByChatIdAndConcluida(String chatId, boolean concluida);

}