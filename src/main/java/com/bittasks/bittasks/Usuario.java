package com.bittasks.bittasks;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuarios") // Nome da tabela no banco de dados
public class Usuario {

    @Id
    private String chatId; // A "Chave Primária". O ID único do Telegram de quem mandou mensagem

    private int nivel;
    private int xp;

    // O Spring Boot exige um construtor vazio nos bastidores
    public Usuario() {
    }

    // Construtor para quando alguém mandar /start pela primeira vez
    public Usuario(String chatId) {
        this.chatId = chatId;
        this.nivel = 1; // Todo aventureiro começa no Nível 1
        this.xp = 0;    // E com 0 de experiência
    }

    // Getters e Setters (Para o Java conseguir ler e modificar esses dados depois)
    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
}