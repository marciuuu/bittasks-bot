package com.bittasks.bittasks;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Component
public class BitTasksBot extends TelegramLongPollingBot {

    private final UsuarioRepository usuarioRepository;
    private final TarefaRepository tarefaRepository;

    // Injetando dois repositórios no construtor
    public BitTasksBot(UsuarioRepository usuarioRepository, TarefaRepository tarefaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tarefaRepository = tarefaRepository;
    }

    @Override
    public String getBotUsername() {
        return "COLOQUE_SEU_USERNAME_AQUI";
    }

    @Override
    public String getBotToken() {
        return "COLOQUE_SEU_TOKEN_AQUI";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String mensagemDoUsuario = update.getMessage().getText();
            String idDoChat = update.getMessage().getChatId().toString();

            SendMessage resposta = new SendMessage();
            resposta.setChatId(idDoChat);
            resposta.setParseMode("Markdown");

            if (mensagemDoUsuario.equals("/start")) {
                Optional<Usuario> usuarioExistente = usuarioRepository.findById(idDoChat);
                Usuario usuario;

                if (usuarioExistente.isPresent()) {
                    usuario = usuarioExistente.get();
                } else {
                    usuario = new Usuario(idDoChat);
                    usuarioRepository.save(usuario);
                }

                resposta.setText("⚔️ *Bem-vindo ao BitTasks, Aventureiro!* ⚔️\n\n" +
                        "Sua jornada começa agora. Aqui, o terminal é a sua taverna e cada tarefa do dia a dia é uma missão a ser cumprida.\n\n" +
                        "📊 *Seus Atributos:*\n" +
                        "Nível: " + usuario.getNivel() + "\n" +
                        "XP: " + usuario.getXp() + "\n\n" +
                        "Digite `/ajuda` para ver o grimório de comandos disponíveis.");
            }
            else if (mensagemDoUsuario.equals("/status")) {
                Optional<Usuario> usuario = usuarioRepository.findById(idDoChat);
                if (usuario.isPresent()) {
                    resposta.setText("📊 *Seu Status Atual:*\nNível: " + usuario.get().getNivel() + "\nXP: " + usuario.get().getXp());
                } else {
                    resposta.setText("Você ainda não é um aventureiro registrado. Digite `/start` para começar!");
                }
            }

            // --- COMANDO: /nova ---
            else if (mensagemDoUsuario.startsWith("/nova")) {
                // Verifica se a pessoa digitou apenas "/nova" ou "/nova Estudar Java"
                if (mensagemDoUsuario.trim().equals("/nova")) {
                    resposta.setText("⚠️ Aventureiro, você precisa me dizer o nome da missão!\n\nExemplo: `/nova Estudar Linux`");
                } else {
                    // Pega o texto que vem depois do "/nova " (que são 6 caracteres)
                    String descricaoDaMissao = mensagemDoUsuario.substring(6).trim();

                    // Toda missão vai dar 10 XP por enquanto (Nossa primeira Regra de Negócio)
                    int xpRecompensa = 10;

                    // Cria a missão e salva direto no banco de dados!
                    Tarefa novaTarefa = new Tarefa(idDoChat, descricaoDaMissao, xpRecompensa);
                    tarefaRepository.save(novaTarefa);

                    resposta.setText("📝 *Missão Adicionada ao Mural!*\n\n" +
                            "🎯 *Objetivo:* " + descricaoDaMissao + "\n" +
                            "💰 *Recompensa:* +" + xpRecompensa + " XP\n\n" +
                            "*(Nota: O comando /listar ainda será forjado no back-end!)*");
                }
            }

            // --- COMANDO: /listar ---
            else if (mensagemDoUsuario.equals("/listar")) {
                // O Spring vai no banco e traz só as missões que ainda estão com concluida = false
                List<Tarefa> missoesAtivas = tarefaRepository.findByChatIdAndConcluida(idDoChat, false);

                if (missoesAtivas.isEmpty()) {
                    resposta.setText("🍻 *Taverna Vazia!*\n\nVocê não tem nenhuma missão ativa no momento. Aproveite o descanso ou digite `/nova [missão]` para buscar novos desafios!");
                } else {
                    // Usamos StringBuilder porque é mais rápido e otimizado para montar textos grandes no Java
                    StringBuilder textoMural = new StringBuilder("📜 *Mural de Missões Ativas:*\n\n");

                    for (Tarefa tarefa : missoesAtivas) {
                        textoMural.append("🗡️ *ID:* `").append(tarefa.getId()).append("`\n")
                                .append("🎯 *Objetivo:* ").append(tarefa.getDescricao()).append("\n")
                                .append("💰 *Recompensa:* ").append(tarefa.getXpRecompensa()).append(" XP\n\n");
                    }

                    textoMural.append("Para concluir uma missão, digite `/feito [ID]`");
                    resposta.setText(textoMural.toString());
                }
            }

            // --- COMANDO: /feito ---
            else if (mensagemDoUsuario.startsWith("/feito")) {
                try {
                    // Pega o número que você digitou depois de "/feito " (ex: o "1" de "/feito 1")
                    Long idTarefa = Long.parseLong(mensagemDoUsuario.substring(7).trim());

                    // O Spring vai no banco procurar a missão com esse ID
                    Optional<Tarefa> tarefaOpt = tarefaRepository.findById(idTarefa);

                    if (tarefaOpt.isPresent()) {
                        Tarefa tarefa = tarefaOpt.get();

                        // Verifica se a missão é sua mesmo e se já não foi feita
                        if (tarefa.getChatId().equals(idDoChat) && !tarefa.isConcluida()) {

                            // 1. Marca a tarefa como concluída no banco
                            tarefa.setConcluida(true);
                            tarefaRepository.save(tarefa);

                            // 2. Busca seu perfil e te dá o XP!
                            Optional<Usuario> usuarioOpt = usuarioRepository.findById(idDoChat);
                            if (usuarioOpt.isPresent()) {
                                Usuario usuario = usuarioOpt.get();
                                usuario.setXp(usuario.getXp() + tarefa.getXpRecompensa());

                                // Lógica básica de Level Up (Sobe de nível a cada 50 XP)
                                if (usuario.getXp() >= usuario.getNivel() * 50) {
                                    usuario.setNivel(usuario.getNivel() + 1);
                                    resposta.setText("🎉 *LEVEL UP!* 🎉\n\nSua mente se expande! Você alcançou o *Nível " + usuario.getNivel() + "*!\n\n" +
                                            "Missão concluída: " + tarefa.getDescricao() + "\nRecompensa: +" + tarefa.getXpRecompensa() + " XP");
                                } else {
                                    resposta.setText("✅ *Missão Concluída!* 🗡️\n\n" +
                                            "Você finalizou: " + tarefa.getDescricao() + "\n" +
                                            "💰 Recompensa: +" + tarefa.getXpRecompensa() + " XP\n" +
                                            "📊 XP Total: " + usuario.getXp());
                                }
                                // Salva o seu novo XP e Nível no banco!
                                usuarioRepository.save(usuario);
                            }
                        } else if (tarefa.isConcluida()) {
                            resposta.setText("⚠️ Essa missão já foi concluída, aventureiro! Não tente farmar XP infinito. KKKK");
                        } else {
                            resposta.setText("⚠️ Essa missão pertence a outro jogador da guilda!");
                        }
                    } else {
                        resposta.setText("❓ Pergaminho não encontrado. Tem certeza que esse ID de missão existe?");
                    }
                } catch (Exception e) {
                    resposta.setText("⚠️ Comando inválido. Use o formato: `/feito [ID da Missão]`\nExemplo: `/feito 1`");
                }
            }

            else if (mensagemDoUsuario.equals("/ajuda")) {
                resposta.setText("📚 *Grimório de Comandos:*\n\n" +
                        "`/nova [tarefa]` - Cria uma nova missão.\n" +
                        "`/listar` - Mostra os nós e arestas das suas missões ativas.\n" +
                        "`/feito [id]` - Executa a missão e te dá XP.\n" +
                        "`/status` - Mostra seu Nível e XP atual.");
            }
            else {
                resposta.setText("Comando desconhecido... Parece que essa instrução deu erro de compilação. Tente digitar `/start` ou `/ajuda`.");
            }

            try {
                execute(resposta);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}