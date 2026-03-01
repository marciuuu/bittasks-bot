# ⚔️ BitTasks Bot

Um ecossistema de produtividade gamificado operando via Telegram. O sistema transforma a rotina real do usuário em um RPG, utilizando arquitetura back-end robusta para gerenciar tarefas e níveis.

## 🚀 Tecnologias Utilizadas
* **Java 21:** Foco em Orientação a Objetos e lógica escalável.
* **Spring Boot 4.0.3:** (Spring Web, Spring Data JPA).
* **API do Telegram:** Comunicação operando via *Long Polling*.
* **Banco de Dados:** H2 Database (Dev).

## 🎯 Status do Projeto: Fase 1 (MVP) Concluída
O núcleo do sistema está de pé. Atualmente o bot é capaz de:
- [x] Cadastrar automaticamente o perfil do Aventureiro (Nível 1, 0 XP) no primeiro `/start`.
- [x] CRUD de Missões: Comandos nativos para `/nova` (criar) e `/feito` (concluir e ganhar XP).
- [x] Listagem Dinâmica: Comando `/listar` para exibir o quadro de missões ativas.
- [x] Sistema de Nivelamento Automático: A cada 50 XP acumulados, o Aventureiro sobe de nível.
- [x] Persistência de Dados: O banco relacional salva o histórico do jogador e as missões ativas e concluídas.

## 🗺️ Roadmap e Próximas Fases
- **Fase 2 (Game Design):** Implementação de Classes (Mago, Guerreiro) e Atributos específicos.
- **Fase 3 (Automação):** Daemons e rotinas em background (`@Scheduled`) para o bot enviar cobranças inteligentes e ativas sobre tarefas atrasadas.
- **Fase 4 (Integrações Corporativas):** Conexão via OAuth 2.0 com o Google Calendar e Tasks API para gerar missões automaticamente a partir da agenda real do usuário.

## 📚 Aprendizado e Foco Acadêmico
Este projeto serve como laboratório prático para consolidar estudos avançados em:
* Arquitetura Limpa e Injeção de Dependências no ecossistema Spring.
* Comportamento de rotinas em *background* e *daemons* (Linux/Sistemas Operacionais) aplicados à automação de mensagens ativas.
* Lógica matemática e Teoria dos Grafos para futura modelagem das Árvores de Habilidades (*Skill Trees*) das classes de RPG.
