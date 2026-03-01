package com.bittasks.bittasks;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_tarefas") // Nome da tabela no banco H2
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco de dados vai gerar esse ID sozinho
    private Long id;

    private String chatId; // Para sabermos de qual aventureiro é essa missão
    private String descricao; // O nome da missão
    private boolean concluida; // Status: 0 (falsa) ou 1 (verdadeira)
    private int xpRecompensa; // Quanto XP o jogador ganha ao finalizar

    // Construtor vazio obrigatório do Spring
    public Tarefa() {
    }

    // Construtor para criar missões novas rapidamente
    public Tarefa(String chatId, String descricao, int xpRecompensa) {
        this.chatId = chatId;
        this.descricao = descricao;
        this.xpRecompensa = xpRecompensa;
        this.concluida = false; // Toda missão nasce pendente
    }

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }

    public int getXpRecompensa() { return xpRecompensa; }
    public void setXpRecompensa(int xpRecompensa) { this.xpRecompensa = xpRecompensa; }
}