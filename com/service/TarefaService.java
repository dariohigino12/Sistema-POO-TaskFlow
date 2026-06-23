package com.taskflow.service;

import com.taskflow.exception.AcessoNaoAutorizadoException;
import com.taskflow.exception.CampoInvalidoException;
import com.taskflow.exception.TarefaNaoEncontradaException;
import com.taskflow.model.*;
import com.taskflow.repository.LembreteRepository;
import com.taskflow.repository.TarefaComPrazoRepository;
import com.taskflow.repository.TarefaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de regras de negócio relacionadas a tarefas e lembretes.
 * Gerencia os três tipos de tarefa do modelo UML:
 * <ul>
 *   <li>{@link TarefaSimples} — tarefa básica com rótulo</li>
 *   <li>{@link TarefaRecorrente} — tarefa com frequência de repetição</li>
 *   <li>{@link TarefaComPrazo} — tarefa independente com data limite</li>
 * </ul>
 * Também gerencia {@link Lembrete}.
 *
 * <p>Garante que um usuário não consiga manipular tarefas/lembretes
 * de outro usuário (verificação de propriedade).</p>
 */
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaComPrazoRepository tarefaComPrazoRepository;
    private final LembreteRepository lembreteRepository;

    public TarefaService(TarefaRepository tarefaRepository,
                         TarefaComPrazoRepository tarefaComPrazoRepository,
                         LembreteRepository lembreteRepository) {
        this.tarefaRepository = tarefaRepository;
        this.tarefaComPrazoRepository = tarefaComPrazoRepository;
        this.lembreteRepository = lembreteRepository;
    }

    // ---------------------------------------------------------------
    // TarefaSimples (herda de Tarefa)
    // ---------------------------------------------------------------

    public TarefaSimples criarTarefaSimples(int idUsuario, String titulo, String descricao,
                                            Prioridade prioridade, String rotulo) {
        validarTitulo(titulo);
        Prioridade prioridadeFinal = (prioridade == null) ? Prioridade.MEDIA : prioridade;
        int id = tarefaRepository.proximoId();
        TarefaSimples tarefa = new TarefaSimples(id, idUsuario, titulo.trim(),
                descricao != null ? descricao.trim() : "", prioridadeFinal, rotulo);
        return (TarefaSimples) tarefaRepository.salvar(tarefa);
    }

    // ---------------------------------------------------------------
    // TarefaRecorrente (herda de Tarefa)
    // ---------------------------------------------------------------

    public TarefaRecorrente criarTarefaRecorrente(int idUsuario, String titulo, String descricao,
                                                  Prioridade prioridade, int frequenciaDias) {
        validarTitulo(titulo);
        Prioridade prioridadeFinal = (prioridade == null) ? Prioridade.MEDIA : prioridade;
        int id = tarefaRepository.proximoId();
        TarefaRecorrente tarefa = new TarefaRecorrente(id, idUsuario, titulo.trim(),
                descricao != null ? descricao.trim() : "", prioridadeFinal, frequenciaDias);
        return (TarefaRecorrente) tarefaRepository.salvar(tarefa);
    }

    // ---------------------------------------------------------------
    // TarefaComPrazo (classe independente)
    // ---------------------------------------------------------------

    public TarefaComPrazo criarTarefaComPrazo(int idUsuario, String titulo, String descricao,
                                              Prioridade prioridade, LocalDate dataLimite) {
        validarTitulo(titulo);
        Prioridade prioridadeFinal = (prioridade == null) ? Prioridade.MEDIA : prioridade;
        int id = tarefaComPrazoRepository.proximoId();
        TarefaComPrazo tarefa = new TarefaComPrazo(id, idUsuario, titulo.trim(),
                descricao != null ? descricao.trim() : "", prioridadeFinal, dataLimite);
        return tarefaComPrazoRepository.salvar(tarefa);
    }

    // ---------------------------------------------------------------
    // Operações comuns (Tarefa abstrata — TarefaSimples/TarefaRecorrente)
    // ---------------------------------------------------------------

    public Tarefa editarTarefa(int idTarefa, int idUsuario, String novoTitulo,
                               String novaDescricao, Prioridade novaPrioridade)
            throws TarefaNaoEncontradaException, AcessoNaoAutorizadoException {
        Tarefa tarefa = buscarEValidarDonoTarefa(idTarefa, idUsuario);

        if (novoTitulo != null) {
            validarTitulo(novoTitulo);
            tarefa.setTitulo(novoTitulo.trim());
        }
        if (novaDescricao != null) {
            tarefa.setDescricao(novaDescricao);
        }
        if (novaPrioridade != null) {
            tarefa.setPrioridade(novaPrioridade);
        }
        return tarefa;
    }

    public void excluirTarefa(int idTarefa, int idUsuario)
            throws TarefaNaoEncontradaException, AcessoNaoAutorizadoException {
        buscarEValidarDonoTarefa(idTarefa, idUsuario);
        tarefaRepository.excluir(idTarefa);
    }

    public Tarefa alterarStatusTarefa(int idTarefa, int idUsuario, StatusTarefa novoStatus)
            throws TarefaNaoEncontradaException, AcessoNaoAutorizadoException {
        Tarefa tarefa = buscarEValidarDonoTarefa(idTarefa, idUsuario);
        if (novoStatus == StatusTarefa.CONCLUIDA) {
            tarefa.concluir();
        } else {
            tarefa.reabrir();
        }
        return tarefa;
    }

    public List<Tarefa> listarTodasTarefas(int idUsuario) {
        return tarefaRepository.listarPorUsuario(idUsuario);
    }

    public List<Tarefa> listarTarefasPendentes(int idUsuario) {
        return tarefaRepository.listarPorUsuarioEStatus(idUsuario, StatusTarefa.PENDENTE);
    }

    public List<Tarefa> listarTarefasConcluidas(int idUsuario) {
        return tarefaRepository.listarPorUsuarioEStatus(idUsuario, StatusTarefa.CONCLUIDA);
    }

    public List<Tarefa> listarTarefasOrdenadoPorPeso(int idUsuario) {
        return tarefaRepository.listarPorUsuarioOrdenadoPorPeso(idUsuario);
    }

    // ---------------------------------------------------------------
    // Operações comuns (TarefaComPrazo — classe independente)
    // ---------------------------------------------------------------

    public TarefaComPrazo editarTarefaComPrazo(int idTarefa, int idUsuario, String novoTitulo,
                                               String novaDescricao, Prioridade novaPrioridade,
                                               LocalDate novaDataLimite)
            throws TarefaNaoEncontradaException, AcessoNaoAutorizadoException {
        TarefaComPrazo tarefa = buscarEValidarDonoTarefaComPrazo(idTarefa, idUsuario);

        if (novoTitulo != null) {
            validarTitulo(novoTitulo);
            tarefa.setTitulo(novoTitulo.trim());
        }
        if (novaDescricao != null) {
            tarefa.setDescricao(novaDescricao);
        }
        if (novaPrioridade != null) {
            tarefa.setPrioridade(novaPrioridade);
        }
        if (novaDataLimite != null) {
            tarefa.setDataLimite(novaDataLimite);
        }
        return tarefa;
    }

    public void excluirTarefaComPrazo(int idTarefa, int idUsuario)
            throws TarefaNaoEncontradaException, AcessoNaoAutorizadoException {
        buscarEValidarDonoTarefaComPrazo(idTarefa, idUsuario);
        tarefaComPrazoRepository.excluir(idTarefa);
    }

    public TarefaComPrazo alterarStatusTarefaComPrazo(int idTarefa, int idUsuario, StatusTarefa novoStatus)
            throws TarefaNaoEncontradaException, AcessoNaoAutorizadoException {
        TarefaComPrazo tarefa = buscarEValidarDonoTarefaComPrazo(idTarefa, idUsuario);
        if (novoStatus == StatusTarefa.CONCLUIDA) {
            tarefa.concluir();
        } else {
            tarefa.reabrir();
        }
        return tarefa;
    }

    public List<TarefaComPrazo> listarTodasTarefasComPrazo(int idUsuario) {
        return tarefaComPrazoRepository.listarPorUsuario(idUsuario);
    }

    public List<TarefaComPrazo> listarTarefasComPrazoPendentes(int idUsuario) {
        return tarefaComPrazoRepository.listarPorUsuarioEStatus(idUsuario, StatusTarefa.PENDENTE);
    }

    public List<TarefaComPrazo> listarTarefasComPrazoConcluidas(int idUsuario) {
        return tarefaComPrazoRepository.listarPorUsuarioEStatus(idUsuario, StatusTarefa.CONCLUIDA);
    }

    public List<TarefaComPrazo> listarTarefasComPrazoAtrasadas(int idUsuario) {
        return tarefaComPrazoRepository.listarAtrasadas(idUsuario);
    }

    public List<TarefaComPrazo> listarTarefasComPrazoOrdenadoPorPeso(int idUsuario) {
        return tarefaComPrazoRepository.listarPorUsuarioOrdenadoPorPeso(idUsuario);
    }

    // ---------------------------------------------------------------
    // Lembretes
    // ---------------------------------------------------------------

    public Lembrete criarLembrete(int idUsuario, String mensagem, LocalDateTime dataHora) {
        if (mensagem == null || mensagem.isBlank()) {
            throw new CampoInvalidoException("A mensagem do lembrete não pode estar vazia.");
        }
        return lembreteRepository.salvar(idUsuario, mensagem.trim(), dataHora);
    }

    public void excluirLembrete(int idLembrete, int idUsuario) {
        Lembrete lembrete = lembreteRepository.buscarPorId(idLembrete);
        if (lembrete.getIdUsuario() != idUsuario) {
            throw new CampoInvalidoException("Você não tem permissão para excluir este lembrete.");
        }
        lembreteRepository.excluir(idLembrete);
    }

    public List<Lembrete> listarLembretes(int idUsuario) {
        return lembreteRepository.listarPorUsuario(idUsuario);
    }

    // ---------------------------------------------------------------
    // Validações internas
    // ---------------------------------------------------------------

    private Tarefa buscarEValidarDonoTarefa(int idTarefa, int idUsuario)
            throws TarefaNaoEncontradaException, AcessoNaoAutorizadoException {
        Tarefa tarefa = tarefaRepository.buscarPorId(idTarefa);
        if (tarefa.getIdUsuario() != idUsuario) {
            throw new AcessoNaoAutorizadoException();
        }
        return tarefa;
    }

    private TarefaComPrazo buscarEValidarDonoTarefaComPrazo(int idTarefa, int idUsuario)
            throws TarefaNaoEncontradaException, AcessoNaoAutorizadoException {
        TarefaComPrazo tarefa = tarefaComPrazoRepository.buscarPorId(idTarefa);
        if (tarefa.getIdUsuario() != idUsuario) {
            throw new AcessoNaoAutorizadoException();
        }
        return tarefa;
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new CampoInvalidoException("O título da tarefa não pode estar vazio.");
        }
    }
}
